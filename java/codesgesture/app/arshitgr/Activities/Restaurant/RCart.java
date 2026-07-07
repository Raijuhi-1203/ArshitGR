package codesgesture.app.arshitgr.Activities.Restaurant;

import android.content.Intent;
import android.os.Bundle;
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

import codesgesture.app.arshitgr.Activities.Grocery.PageAddress;
import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Adapter.CartAdapter;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.Notify;

public class RCart extends AppCompatActivity  {
    RecyclerView recycler;
    LinearLayout norecord;
    CartAdapter cartAdapter;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    CustomerModel customerModel;
    TextView cart_total,bt_checkout;
    double total;
    String totalfinal,price_total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rcart_page);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        recycler=findViewById(R.id.rvproducts);
        norecord=findViewById(R.id.norecord);
        cart_total=findViewById(R.id.cart_total);
        bt_checkout=findViewById(R.id.bt_checkout);

        ImageView btback=findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=findViewById(R.id.title);
        title.setText("My cart");

        GridLayoutManager mLayoutManager2 = new GridLayoutManager(RCart.this, 1);
        recycler.setLayoutManager(mLayoutManager2);
        cartAdapter = new CartAdapter(RCart.this, productModels, R.layout.item_cart, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetProductData();
            }

            @Override
            public void onRemove(ProductModel data) {
                TotalAmount();
            }
        });

        recycler.setAdapter(cartAdapter);
        recycler.setItemViewCacheSize(productModels.size());
        GetProductData();

        bt_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total==0){
                    UserUtil.ShowMsg("Sorry ! Your cart is Empty.", RCart.this);
                }else {
                    cHECKoUT();
                }
            }
        });

    }

    private void GetProductData() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(RCart.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("cart_guest_id",""));
        jc.SendRequest("get_rcartproduct", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    ProductModel product = new ProductModel();
                    product.setId(obj.getString("id1"));
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
                TotalAmount();
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }
    private void cHECKoUT() {
        totalfinal=String.valueOf(total);//cart_total.getText().toString().replace("₹ ", "");
        Intent intent=new Intent(RCart.this, PageAddress.class);
        intent.putExtra("total",totalfinal);
        intent.putExtra("market_price_total",price_total);
        intent.putExtra("productModel", productModels);
        intent.putExtra("order_section", "Restaurant");
        startActivity(intent);
    }
    private void checkcart() {
        if(productModels.size()<=0)
            norecord.setVisibility(View.VISIBLE);
        else
            norecord.setVisibility(View.GONE);
    }
    private void TotalAmount() {
        Double totalamount = 0.0;
        Double mtotalamount = 0.0;
        for (ProductModel cart : productModels) {
            totalamount += (Double.parseDouble(cart.getProduct_final_sell_price()) * cart.getCart_qty());
            mtotalamount += (Double.parseDouble(cart.getProduct_market_price()) * cart.getCart_qty());
        }
        String price =String.valueOf(String.format("%.02f",totalamount));
        String mprice =String.valueOf(String.format("%.02f",mtotalamount));
        cart_total.setText("₹ "+price);
        total=totalamount;
        price_total=mprice;
        checkcart();
    }


}