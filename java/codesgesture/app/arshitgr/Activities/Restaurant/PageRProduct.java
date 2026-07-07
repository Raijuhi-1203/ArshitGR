package codesgesture.app.arshitgr.Activities.Restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.Notify;

public class PageRProduct extends AppCompatActivity {
    RecyclerView recycler;
    LinearLayout norecord;
    RestaurantModel restaurantModel;
    ProductAdapter productAdapter;
    CustomerModel customerModel;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    LinearLayout cartamt;
    TextView txamt,btcart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_rproduct);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        restaurantModel=(RestaurantModel) getIntent().getSerializableExtra("data");
        norecord=findViewById(R.id.norecord);
        recycler=findViewById(R.id.recycler);

        ImageView btback=findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=findViewById(R.id.title);
        title.setText(restaurantModel.getSeller_firm_name());

        cartamt=findViewById(R.id.cartamt);
        txamt=findViewById(R.id.txamt);
        btcart=findViewById(R.id.btcart);

        GridLayoutManager mLayoutManager = new GridLayoutManager(PageRProduct.this, 1);
        recycler.setLayoutManager(mLayoutManager);
        productAdapter = new ProductAdapter(PageRProduct.this, productModels, R.layout.item_food, new Notify() {
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
        },"R",restaurantModel.getSeller_id(),restaurantModel.getSeller_firm_name());
        recycler.setAdapter(productAdapter);
        recycler.setItemViewCacheSize(productModels.size());
        
        GetData();
        BindCart();
       
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            UpdateCArtCount();
        }
    };
    private void UpdateCArtCount() {
        handler.postDelayed(runnable,1000);
    }

    private void BindCart() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageRProduct.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_rcart_qty", param, new JsonCallbacks() {
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
                            startActivity(new Intent(PageRProduct.this,RCart.class));
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
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageRProduct.this);
        param.add(new NetParam("category_id",restaurantModel.getCategory_id()));
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("price_seller_id",restaurantModel.getSeller_id()));
        jc.SendRequest("get_catwiseproduct_r", param, new JsonCallbacks() {
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
                    product.setProduct_GST_type(obj.getString("product_GST_type"));
                    product.setProduct_with_gst_Price(obj.getString("product_with_gst_Price"));
                    product.setProduct_stock(obj.getString("product_stock"));
                    product.setProduct_shipping_charge(obj.getString("product_shipping_charge"));
                    product.setProduct_sell_price(obj.getString("product_sell_price"));
                    product.setProduct_CGST_percentage(obj.getString("product_CGST_percentage"));
                    product.setProduct_CGST_rate(obj.getString("product_CGST_rate"));
                    product.setProduct_SGST_percentage(obj.getString("product_SGST_percentage"));
                    product.setProduct_SGST_rate(obj.getString("product_SGST_rate"));
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
