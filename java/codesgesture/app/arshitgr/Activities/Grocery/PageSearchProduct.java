package codesgesture.app.arshitgr.Activities.Grocery;

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

import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageSearchProduct extends AppCompatActivity {
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;
    ArrayList<ProductModel> productModels1=new ArrayList<>();
    ArrayAdapter<ProductModel> productModelArrayAdapter;
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();
    String str;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_search_product);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        str=getString(R.string.con);
        searchbar=findViewById(R.id.searchbar);
        searchbar.setHint("Search for products..");


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
        productModelArrayAdapter = new ArrayAdapter<ProductModel>(PageSearchProduct.this, R.layout.row_people,R.id.nm, productModelArrayList);
        searchbar.setThreshold(1);
        searchbar.setAdapter(productModelArrayAdapter);
        SearchProduct();

    }

    private void SearchProduct() {
        productModelArrayList.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageSearchProduct.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_product", param, new JsonCallbacks() {
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
        CallJson jc = new CallJson(PageSearchProduct.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("pnm",str));
        jc.SendRequest("get_searchproducts", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    ProductModel product = new ProductModel();
                    product.setProduct_id(obj.getString("product_id"));
                    product.setId(obj.getString("id"));
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
                    productModels1.add(product);

                    Intent intent=new Intent(PageSearchProduct.this, PageProductDetails.class);
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
