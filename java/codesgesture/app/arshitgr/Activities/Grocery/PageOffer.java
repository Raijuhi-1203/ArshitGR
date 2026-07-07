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
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Adapter.ProductAdapter;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
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

public class PageOffer extends AppCompatActivity {
    RecyclerView recycler;
    LinearLayout norecord;
    ProductAdapter productAdapter;
    CustomerModel customerModel;
    ImageView person,cart;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    AutoCompleteTextView searchbar;
    String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        str=getString(R.string.con);
        norecord=findViewById(R.id.norecord);
        recycler=findViewById(R.id.recycler);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageOffer.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageOffer.this, AccountFragment.class));
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageOffer.this,PageSearchProduct.class));
            }
        });
        GridLayoutManager mLayoutManager2 = new GridLayoutManager(PageOffer.this, 2);
        recycler.setLayoutManager(mLayoutManager2);
        productAdapter = new ProductAdapter(PageOffer.this, productModels, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetProductData();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetProductData();
            }
        },"G","0","0");
        recycler.setAdapter(productAdapter);
        recycler.setItemViewCacheSize(productModels.size());
        GetProductData();

    }



    private void GetProductData() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageOffer.this);
        jc.SendRequest("get_TopDeals", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    ProductModel product = new ProductModel();
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
                    product.setProduct_GST_type(obj.getString("product_GST_type"));
                    product.setProduct_with_gst_Price(obj.getString("product_with_gst_Price"));
                    product.setProduct_stock(obj.getString("product_stock"));
                    product.setProduct_shipping_charge(obj.getString("product_shipping_charge"));
                    product.setProduct_sell_price(obj.getString("product_sell_price"));

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
        if(productModels.size()>0)
            norecord.setVisibility(View.GONE);
        else
            norecord.setVisibility(View.VISIBLE);
    }

}