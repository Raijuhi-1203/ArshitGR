package codesgesture.app.arshitgr.Activities.Restaurant;

import android.os.Bundle;
import android.view.View;
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

import codesgesture.app.arshitgr.Adapter.CategoryRAdapter;
import codesgesture.app.arshitgr.Adapter.RCategoryAdapter;
import codesgesture.app.arshitgr.Adapter.RestaurantAdapter;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.ExtraCallBack;

public class PageRestaurant extends AppCompatActivity {
    RecyclerView recycler,rvcat;
    LinearLayout norecord;
    CategoryModel categoryModel;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();
    CategoryRAdapter categoryAdapter;
    CustomerModel customerModel;
    RestaurantAdapter restaurantAdapter;
    ArrayList<RestaurantModel> restaurantModels=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_restaurant);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        categoryModel=(CategoryModel)getIntent().getSerializableExtra("data");
        norecord=findViewById(R.id.norecord);
        recycler=findViewById(R.id.recycler);
        rvcat=findViewById(R.id.rvcat);

        ImageView btback=findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=findViewById(R.id.title);
        title.setText(categoryModel.getCategory_name());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvcat.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryRAdapter(PageRestaurant.this, categoryModels,R.layout.round_category);
        categoryAdapter.ecb = new ExtraCallBack() {
            @Override
            public void OnCompleted(String arguments,String name) {
                title.setText(name);
                GetRView(arguments);
            }
        };
        rvcat.smoothScrollToPosition(0);
        rvcat.setAdapter(categoryAdapter);
        rvcat.setItemViewCacheSize(categoryModels.size());
        Bindcategory();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(PageRestaurant.this, 1);
        recycler.setLayoutManager(gridLayoutManager);
        restaurantAdapter = new RestaurantAdapter(PageRestaurant.this, restaurantModels, R.layout.item_resturent);
        recycler.setAdapter(restaurantAdapter);
        recycler.setItemViewCacheSize(restaurantModels.size());
        GetRView(categoryModel.getCategory_id());

    }

    private void Bindcategory() {
        categoryModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageRestaurant.this);
        jc.SendRequest("get_Food_Category", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    CategoryModel product = new CategoryModel();
                    product.setCategory_name(obj.getString("category_name"));
                    product.setCategory_id(obj.getString("category_id"));
                    product.setCategory_photo(obj.getString("category_photo"));
                    categoryModels.add(product);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void GetRView(String arguments) {
        restaurantModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageRestaurant.this);
        param.add(new NetParam("category_id",arguments));
        jc.SendRequest("get_category_restaurant", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    RestaurantModel product = new RestaurantModel();
                    product.setSeller_firm_name(obj.getString("seller_firm_name"));
                    product.setSeller_address_line_1(obj.getString("seller_address_line_1"));
                    product.setSeller_address_line_2(obj.getString("seller_address_line_2"));
                    product.setSeller_state_name(obj.getString("seller_state_name"));
                    product.setSeller_city_name(obj.getString("seller_city_name"));
                    product.setSeller_pincode(obj.getString("seller_pincode"));
                    product.setSeller_photo(obj.getString("seller_photo"));
                    product.setSeller_id(obj.getString("seller_id"));
                    product.setCategory_id(obj.getString("category_id"));
                    product.setOpening_time(obj.getString("opening_time"));
                    product.setClosing_time(obj.getString("closing_time"));
                    product.setArea(obj.getString("area"));
                    restaurantModels.add(product);
                }
                restaurantAdapter.notifyDataSetChanged();
                BindDataToView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataToView();
            }
        }, "", "Loading..");
    }

    private void BindDataToView() {
        if(restaurantModels.size()>0){
            recycler.setVisibility(View.VISIBLE);
            norecord.setVisibility(View.GONE);
        }
        else{
            recycler.setVisibility(View.GONE);
            norecord.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}