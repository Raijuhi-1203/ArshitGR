package codesgesture.app.arshitgr.Activities.Restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.PageProductDetails;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class RSearchProduct extends AppCompatActivity {
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;
    ArrayList<CategoryModel> productModels1=new ArrayList<>();
    ArrayAdapter<CategoryModel> productModelArrayAdapter;
    ArrayList<CategoryModel> productModelArrayList=new ArrayList<>();
    String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_search_product);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        str=getString(R.string.con);
        searchbar=findViewById(R.id.searchbar);
        searchbar.setHint("Search for item and resturent..");

//        searchbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String strr = searchbar.getText().toString();
//                FetchSearchProductData(strr);
//            }
//        });

        searchbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strr = searchbar.getText().toString();
                FetchSearchProductData(strr);
            }
        });

        productModelArrayList = new ArrayList<>();
        productModelArrayAdapter = new ArrayAdapter<CategoryModel>(RSearchProduct.this, R.layout.row_people,R.id.nm, productModelArrayList);
        searchbar.setThreshold(1);
        searchbar.setAdapter(productModelArrayAdapter);
        SearchProduct();
    }

    private void SearchProduct() {
        productModelArrayList.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(RSearchProduct.this);
        jc.SendRequest("get_search_text", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    CategoryModel product = new CategoryModel();
                    product.setCategory_name(obj.getString("category_name"));
                    product.setCategory_id(obj.getString("category_id"));
                    product.setCategory_photo(obj.getString("category_photo"));
                    product.setSeller_firm_name(obj.getString("seller_firm_name"));
                    productModelArrayList.add(product);
                    productModelArrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "LOGIN", "Please wait while getting..");
    }

    private void FetchSearchProductData(String str) {
        productModels1.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(RSearchProduct.this);
       // param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("pnm",str));
        jc.SendRequest("get_rsearchproducts", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    CategoryModel product = new CategoryModel();
                    product.setCategory_name(obj.getString("seller_firm_name"));
                    product.setCategory_id(obj.getString("category_id"));
                    productModelArrayList.add(product);
                    productModels1.add(product);

                    Intent intent=new Intent(RSearchProduct.this, PageRestaurant.class);
                    intent.putExtra("data",product);
                    startActivity(intent);

                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

}