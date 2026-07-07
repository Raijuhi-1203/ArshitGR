package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Adapter.ProductAdapter;
import codesgesture.app.arshitgr.Adapter.RoundCategory;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.ExtraCallBack;
import codesgesture.app.arshitgr.interfaces.Notify;

public class PageMostView extends AppCompatActivity {
    RecyclerView recycler;
    LinearLayout norecord;
    ProductAdapter productAdapter;
    ImageView person,cart;
    CustomerModel customerModel;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    AutoCompleteTextView searchbar;
    String str;
    LinearLayout cartamt;
    TextView txamt,btcart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_grocery_items);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        str=getString(R.string.con);
        norecord=findViewById(R.id.norecord);
        recycler=findViewById(R.id.recycler);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);
        GridLayoutManager mLayoutManager = new GridLayoutManager(PageMostView.this, 2);
        recycler.setLayoutManager(mLayoutManager);
        productAdapter = new ProductAdapter(PageMostView.this, productModels, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetData();
                BindCart();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetData();
                BindCart();
            }
        },"G","0","0");
        recycler.setAdapter(productAdapter);
        recycler.setItemViewCacheSize(productModels.size());


        searchbar=findViewById(R.id.searchbar);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageMostView.this,PageSearchProduct.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageMostView.this, CartFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageMostView.this, AccountFragment.class));
            }
        });

        txamt=findViewById(R.id.txamt);
        btcart=findViewById(R.id.btcart);
        cartamt=findViewById(R.id.cartamt);

        BindCart();
        GetData();

    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            UpdateCArtCount();
        }
    };
    private void UpdateCArtCount() {
        // cartno.setText(Constants.CARTCOUNT); //Constants.CARTCOUNT
        handler.postDelayed(runnable,1000);
    }

    private void BindCart() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageMostView.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_cart_qty", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONObject obj = UserUtil.ConvertStringToJsonObject(json);
                Constants.CARTCOUNT = obj.getString("carttotal");
                UpdateCArtCount();

                int total = Integer.parseInt(obj.getString("carttotal"));
                String mrptotal = obj.getString("mrptotal");

                if(total > 0){
                    cartamt.setVisibility(View.VISIBLE);
                    txamt.setText("Rs. "+ mrptotal +"  ("+String.valueOf(total)+" items)");

                    btcart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(PageMostView.this,CartFragment.class));
                        }
                    });
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");

    }

    private void GetData() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageMostView.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_random_product_all", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    ProductModel product = new ProductModel();
                    product.setId(obj.getString("id"));
                    product.setProduct_id(obj.getString("product_id"));
                    product.setProduct_final_sell_price(obj.getString("product_final_sell_price"));
                    product.setProduct_market_price(obj.getString("product_market_price"));
                    product.setPhoto_path(obj.getString("photo_path"));
                    product.setProduct_full_name(obj.getString("product_full_name"));
                    product.setProduct_discount_percentage(obj.getString("product_discount_percentage"));
                    product.setProduct_description(obj.getString("product_description"));
                    product.setCart_qty(Integer.parseInt(obj.getString("cart_qty")));
                    product.setQty(Integer.parseInt(obj.getString("cart_qty")));
                    product.setProduct_parent_category_id(obj.getString("product_parent_category_id"));
                    product.setProduct_unit(obj.getString("product_unit"));
                    product.setProduct_unit_value(obj.getString("product_unit_value"));
                    product.setProduct_discount_percentage(obj.getString("product_discount_percentage"));
                    product.setProduct_discount_price(obj.getString("product_discount_price"));
                    product.setProduct_GST_percentage(obj.getString("product_GST_percentage"));
                    product.setProduct_GST_rate(obj.getString("product_GST_rate"));

                    product.setProduct_CGST_percentage(obj.getString("product_CGST_percentage"));
                    product.setProduct_CGST_rate(obj.getString("product_CGST_rate"));
                    product.setProduct_SGST_percentage(obj.getString("product_SGST_percentage"));
                    product.setProduct_SGST_rate(obj.getString("product_SGST_rate"));

                    product.setProduct_GST_type(obj.getString("product_GST_type"));
                    product.setProduct_with_gst_Price(obj.getString("product_with_gst_Price"));
                    product.setProduct_stock(obj.getString("product_stock"));
                    product.setProduct_shipping_charge(obj.getString("product_shipping_charge"));
                    product.setProduct_sell_price(obj.getString("product_sell_price"));
                    BindCart();
                    productModels.add(product);
                }
                productAdapter.notifyDataSetChanged();
                BindDataToView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataToView();
            }
        }, "", "Loading..");
    }

    private void BindDataToView() {
        if(productModels.size()>0){
            norecord.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
        else{
            norecord.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



}