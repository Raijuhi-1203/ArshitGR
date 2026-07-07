package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Adapter.AddressAdapter;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.AddressModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.Notify;

public class PageAddress extends AppCompatActivity  {
    Button btn_add;
    RecyclerView recycler;
    LinearLayout norecord;
    ArrayList<ProductModel> productModel;
    TextView btproceed;
    String carttotal,market_price_total,order_section;
    CustomerModel customerModel;
    AddressAdapter addressAdapter;

    ArrayList<AddressModel> addressModels=new ArrayList<>();
    AutoCompleteTextView searchbar;
    ImageView person,cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_book);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        carttotal=getIntent().getStringExtra("total");
        market_price_total=getIntent().getStringExtra("market_price_total");
        order_section=getIntent().getStringExtra("order_section");
        productModel=(ArrayList<ProductModel>)getIntent().getSerializableExtra("productModel");
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageAddress.this,PageSearchProduct.class));

            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageAddress.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageAddress.this, AccountFragment.class));
            }
        });
        norecord=findViewById(R.id.norecord);
        btn_add=findViewById(R.id.btn_add);
        btproceed=findViewById(R.id.btproceed);
        recycler=findViewById(R.id.recycler);

        btproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addressModels.size() == 0)
                {
                    UserUtil.ShowMsg("Please add address than order placed.",PageAddress.this);
                }
                else
                {
                    Intent intent=new Intent(PageAddress.this,Page_CheckOut.class);
                    intent.putExtra("total",carttotal);
                    intent.putExtra("market_price_total",market_price_total);
                    intent.putExtra("order_section",order_section);
                    intent.putExtra("productModel",productModel);
                    intent.putExtra("address",addressModels);
                    startActivity(intent);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PageAddress.this,AddAddress.class);
                intent.putExtra("total",carttotal);
                intent.putExtra("market_price_total",market_price_total);
                intent.putExtra("order_section",order_section);
                intent.putExtra("productModel",productModel);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(PageAddress.this, 1);
        recycler.setLayoutManager(mLayoutManager);
        addressAdapter = new AddressAdapter(PageAddress.this, addressModels, R.layout.item_address, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetData();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetData();
            }
        },carttotal,market_price_total,productModel);
        recycler.setAdapter(addressAdapter);
        recycler.setItemViewCacheSize(addressModels.size());
        GetData();

    }


    private void GetData() {
        addressModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageAddress.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_address", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    AddressModel product = new AddressModel();
                    product.setAddress_city_name(obj.getString("address_city_name"));
                    product.setAddress_default(obj.getString("address_default"));
                    product.setAddress_customer_email(obj.getString("address_customer_email"));
                    product.setAddress_city_name(obj.getString("address_city_name"));
                    product.setAddress_customer_mobileno(obj.getString("address_customer_mobileno"));
                    product.setAddress_customer_name(obj.getString("address_customer_name"));
                    product.setAddress_line_1(obj.getString("address_line_1"));
                    product.setAddress_line_2(obj.getString("address_line_2"));
                    product.setAddress_state_name(obj.getString("address_state_name"));
                    product.setCustomer_id(obj.getString("customer_id"));
                    product.setAddress_pincode(obj.getString("address_pincode"));
                    product.setId(obj.getString("id"));
                    addressModels.add(product);
                }
                addressAdapter.notifyDataSetChanged();
                BindDataToView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataToView();
            }
        }, "", "Loading..");
    }
    private void BindDataToView() {
        if(addressModels.size()>0)
            norecord.setVisibility(View.GONE);
        else
            norecord.setVisibility(View.VISIBLE);
    }

}
