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
import codesgesture.app.arshitgr.Adapter.CategoryAdapter;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class ShopFragment extends AppCompatActivity {
    CustomerModel model;
    ImageView person,cart;
    RecyclerView recycler;
    CategoryAdapter categoryAdapter;
    LinearLayout home,wishlist,shop,account,cartbtn;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();
    AutoCompleteTextView searchbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_categories);
        model=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        recycler=findViewById(R.id.recycler);
        cartbtn=findViewById(R.id.cartbtn);
        account=findViewById(R.id.account);
        shop=findViewById(R.id.shop);
        wishlist=findViewById(R.id.wishlist);
        home=findViewById(R.id.home);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(ShopFragment.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(ShopFragment.this, AccountFragment.class));
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(ShopFragment.this, CartFragment.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(ShopFragment.this, AccountFragment.class));
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(2,true);
                startActivity(new Intent(ShopFragment.this, MenuFragment.class));
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(1,true);
                startActivity(new Intent(ShopFragment.this, WishListFragment.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(0,true);
                startActivity(new Intent(ShopFragment.this, Dashboard.class));
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(ShopFragment.this,PageSearchProduct.class));
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(ShopFragment.this, 3);
        recycler.setLayoutManager(mLayoutManager);
        categoryAdapter = new CategoryAdapter(ShopFragment.this, categoryModels,R.layout.item_category);
        recycler.setAdapter(categoryAdapter);
        GetData();



    }

    private void GetData() {
        categoryModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(ShopFragment.this);
        jc.SendRequest("get_Category", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    CategoryModel product = new CategoryModel();
                    product.setCategory_name(obj.getString("category_name"));
                    product.setCategory_id(obj.getString("category_id"));
                    product.setCategory_photo(obj.getString("category_photo"));
//                    product.setLast_trade_price(obj.getString("last_trade_price"));
//                    product.setStk_id(Float.parseFloat(obj.getString("stk_id")));
//                    product.setStk_nm(obj.getString("stk_nm"));
//                    product.setStk_symbol(obj.getString("stk_symbol"));
//                    product.setStock_type(obj.getString("stock_type"));
//                    product.setUserid(String.valueOf(obj.getInt("userid")));
//                    product.setSubgrp_id(obj.getString("subgrp_id"));
                    categoryModels.add(product);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

}