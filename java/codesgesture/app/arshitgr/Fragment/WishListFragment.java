package codesgesture.app.arshitgr.Fragment;

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

import codesgesture.app.arshitgr.Activities.Grocery.Dashboard;
import codesgesture.app.arshitgr.Activities.Grocery.MenuFragment;
import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Adapter.WishlistAdapter;
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

public class WishListFragment extends AppCompatActivity  {
    RecyclerView recycler;
    LinearLayout norecord;
    ImageView person,cart;
    WishlistAdapter wishlistAdapter;
    LinearLayout home,wishlist,shop,account,cartbtn;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_wishlist);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        recycler=findViewById(R.id.recycler);
        norecord=findViewById(R.id.norecord);
        cartbtn=findViewById(R.id.cartbtn);
        account=findViewById(R.id.account);
        shop=findViewById(R.id.shop);
        wishlist=findViewById(R.id.wishlist);
        home=findViewById(R.id.home);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(4,true);
//                startActivity(new Intent(WishListFragment.this, WishListFragment.class));
//            }
//        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(WishListFragment.this, AccountFragment.class));
            }
        });

//        cartbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(4,true);
//                startActivity(new Intent(WishListFragment.this, WishListFragment.class));
//            }
//        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(WishListFragment.this, AccountFragment.class));
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(2,true);
                startActivity(new Intent(WishListFragment.this, MenuFragment.class));
            }
        });
//        wishlist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(1,true);
//                startActivity(new Intent(WishListFragment.this, WishListFragment.class));
//            }
//        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(0,true);
                startActivity(new Intent(WishListFragment.this, Dashboard.class));
            }
        });

        GridLayoutManager mLayoutManager2 = new GridLayoutManager(WishListFragment.this, 1);
        recycler.setLayoutManager(mLayoutManager2);
        wishlistAdapter = new WishlistAdapter(WishListFragment.this, productModels, R.layout.item_wishlist, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetProductData();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetProductData();
            }
        });
        recycler.setAdapter(wishlistAdapter);
        recycler.setItemViewCacheSize(productModels.size());
        GetProductData();
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishListFragment.this,PageSearchProduct.class));
            }
        });


    }

    private void GetProductData() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(WishListFragment.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_wishlistproduct", param, new JsonCallbacks() {
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
                wishlistAdapter.notifyDataSetChanged();
                BindDataView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataView();
            }
        }, "", "Loading..");
    }

    private void BindDataView() {
        if(productModels.size()<=0)
            norecord.setVisibility(View.VISIBLE);
        else
            norecord.setVisibility(View.GONE);
    }


}