package codesgesture.app.arshitgr.Activities.Restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.AddAddress;
import codesgesture.app.arshitgr.Activities.Grocery.Dashboard;
import codesgesture.app.arshitgr.Activities.Grocery.MenuFragment;
import codesgesture.app.arshitgr.Activities.Grocery.PageOffer;
import codesgesture.app.arshitgr.Activities.Grocery.PageProduct;
import codesgesture.app.arshitgr.Adapter.RCategoryAdapter;
import codesgesture.app.arshitgr.Adapter.RestaurantAdapter;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Models.AreaModel;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.PincodeModel;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.Models.TopSliderModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class RDashBoard extends AppCompatActivity {
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;
    Spinner spnrarea;
    RecyclerView rvfoodcat,rvrestrant;
    LinearLayout home,menu,account,fcart;
    Button btngrcry,btnres;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();
    RCategoryAdapter categoryAdapter;
    RestaurantAdapter restaurantAdapter;
    ArrayList<RestaurantModel> restaurantModels=new ArrayList<>();
    LinearLayout cartamt;
    TextView txamt,btcart;
    SliderLayout slider;

    ArrayList<AreaModel> arrayList=new ArrayList<>();
    ArrayAdapter<AreaModel> arrayAdapter;
    String pincode;
    LinearLayout norecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rdashboard);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        fcart=findViewById(R.id.fcart);
        account=findViewById(R.id.account);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        spnrarea=findViewById(R.id.spnrarea);
        norecord=findViewById(R.id.norecord);

        rvfoodcat=findViewById(R.id.rvfoodcat);
        rvrestrant=findViewById(R.id.rvrestrant);

        searchbar=findViewById(R.id.searchbar);
        searchbar.setHint("Search for item and resturent..");

        btnres=findViewById(R.id.btnres);
        btngrcry=findViewById(R.id.btngrcry);

        cartamt=findViewById(R.id.cartamt);
        txamt=findViewById(R.id.txamt);
        btcart=findViewById(R.id.btcart);
        slider=findViewById(R.id.slider);

        btngrcry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RDashBoard.this,Dashboard.class));
            }
        });

//        btnres.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RDashBoard.this, RDashBoard.class));
//            }
//        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvfoodcat.setLayoutManager(layoutManager);
        categoryAdapter = new RCategoryAdapter(RDashBoard.this, categoryModels,R.layout.round_category);
        rvfoodcat.smoothScrollToPosition(0);
        rvfoodcat.setAdapter(categoryAdapter);
        rvfoodcat.setItemViewCacheSize(categoryModels.size());
        Bindcategory();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RDashBoard.this, 1);
        rvrestrant.setLayoutManager(gridLayoutManager);
        restaurantAdapter = new RestaurantAdapter(RDashBoard.this, restaurantModels, R.layout.item_resturent);
        rvrestrant.setAdapter(restaurantAdapter);
        rvrestrant.setItemViewCacheSize(restaurantModels.size());

        fcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RDashBoard.this, RCart.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RDashBoard.this, RProfile.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RDashBoard.this, PageRMenu.class));
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RDashBoard.this, RSearchProduct.class));
            }
        });

        BindCart();
        BindSlider();

        spnrarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spnrarea.getSelectedItemPosition();
                pincode = String.valueOf(arrayList.get(pos).getPincode());
                GetRView(pincode);
                // CityCall(spnrstid);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<AreaModel>(RDashBoard.this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrarea.setAdapter(arrayAdapter);
        AreaCall();

    }

    private void AreaCall() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(RDashBoard.this);
        jc.SendRequest("get_area", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    AreaModel product = new AreaModel();
                    product.setArea(obj.getString("area"));
                    product.setId(obj.getString("id"));
                    product.setPincode(obj.getString("pincode"));
                    arrayList.add(product);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void BindSlider() {
        restaurantModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(RDashBoard.this);
        jc.SendRequest("get_rslider", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                if (json.length() > 0) {
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
                        DefaultSliderView defaultSliderView = new DefaultSliderView(RDashBoard.this);
                        defaultSliderView.image(Constants.BASEURI2+product.getSeller_photo());
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                        defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Intent intent=new Intent(RDashBoard.this, PageRProduct.class);
                                intent.putExtra("data",product);
                                startActivity(intent);
                            }
                        });
                        slider.addSlider(defaultSliderView);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.setCustomAnimation(new DescriptionAnimation());
                        slider.setDuration(5000);

                    }
                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Wait Loading...");
    }

    private void GetRView(String pincode) {
        restaurantModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(RDashBoard.this);
        param.add(new NetParam("pincode",pincode));
        jc.SendRequest("get_restaurant", param, new JsonCallbacks() {
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
                BindDataView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataView();
            }
        }, "", "Loading..");
    }

    private void BindDataView() {
        if(restaurantModels.size()>0){
            norecord.setVisibility(View.GONE);
            rvrestrant.setVisibility(View.VISIBLE);
        }
        else{
            norecord.setVisibility(View.VISIBLE);
            rvrestrant.setVisibility(View.GONE);
        }

    }

    private void Bindcategory() {
        categoryModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(RDashBoard.this);
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
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(RDashBoard.this);
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
                            startActivity(new Intent(RDashBoard.this,RCart.class));
                        }
                    });
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");

    }

}
