package codesgesture.app.arshitgr.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Adapter.CategoryAdapter;
import codesgesture.app.arshitgr.Adapter.ProductAdapter;
import codesgesture.app.arshitgr.Models.BannerModel;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.TopSliderModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.Notify;

public class HomeFragment extends Fragment{
    SliderLayout slider,slider2,slider3,slider4;
    CustomerModel customerModel;
    RecyclerView rvcat,rvdeal,rvend,rvrview;
    ArrayList<BannerModel> bannerModels=new ArrayList<>();
    ArrayList<TopSliderModel> topSliderModels=new ArrayList<>();
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();
    ProductAdapter productAdapter;
    ArrayList<ProductModel> productModels=new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getActivity().getApplicationContext());
        slider=view.findViewById(R.id.slider);
        slider2=view.findViewById(R.id.slider2);
        slider3=view.findViewById(R.id.slider3);
        slider4=view.findViewById(R.id.slider4);

        rvrview=view.findViewById(R.id.rvrview);
        rvend=view.findViewById(R.id.rvend);
        rvdeal=view.findViewById(R.id.rvdeal);
        rvcat=view.findViewById(R.id.rvcat);

        FetchBanner();
        FetchSlider();

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvcat.setLayoutManager(mLayoutManager);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModels,R.layout.item_category);
        rvcat.setAdapter(categoryAdapter);
        GetData();

        GridLayoutManager mLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        rvdeal.setLayoutManager(mLayoutManager2);
        productAdapter = new ProductAdapter(getActivity(), productModels, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetProductData();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetProductData();
            }
        },"G","0","0");
        rvdeal.setAdapter(productAdapter);
        rvdeal.setItemViewCacheSize(productModels.size());
        GetProductData();

        return view;
    }

//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        public void run() {
//            UpdateCArtCount();
//        }
//    };
//    private void UpdateCArtCount() {
//        cartno.setText(Constants.CARTCOUNT); //Constants.CARTCOUNT
//        handler.postDelayed(runnable,1000);
//    }

    private void GetProductData() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(getActivity());
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_TopDeals", param, new JsonCallbacks() {
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

                    productModels.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }
    private void GetData() {
        categoryModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(getActivity());
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
    private void FetchSlider() {
        topSliderModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(getActivity());
        jc.SendRequest("get_TopSlider", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                if (json.length() > 0) {
                    JSONArray array = new JSONArray(json);
                    for (int s = 0; s < array.length(); s++) {
                        JSONObject obj = array.getJSONObject(s);
                        TopSliderModel model = new TopSliderModel();
                        model.setSlider_photo(obj.getString("slider_photo"));
                        model.setSlider_status(obj.getString("slider_status"));
                        topSliderModels.add(model);
                        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                        defaultSliderView.image(Constants.BASEURI2+model.getSlider_photo());

                        slider.addSlider(defaultSliderView);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.setCustomAnimation(new DescriptionAnimation());
                        slider.setDuration(5000);

                        slider3.addSlider(defaultSliderView);
                        slider3.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider3.setCustomAnimation(new DescriptionAnimation());
                        slider3.setDuration(5000);
                    }
                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Wait Loading...");
    }
    private void FetchBanner() {
        bannerModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(getActivity());
        jc.SendRequest("get_Banner", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                if (json.length() > 0) {
                    JSONArray array = new JSONArray(json);
                    for (int s = 0; s < array.length(); s++) {
                        JSONObject obj = array.getJSONObject(s);
                        BannerModel model = new BannerModel();
                        model.setBanner_path(obj.getString("banner_path"));
                        model.setBanner_status(obj.getString("banner_status"));
                        bannerModels.add(model);
                        DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                        defaultSliderView.image(Constants.BASEURI2+model.getBanner_path());
                        slider2.addSlider(defaultSliderView);
                        slider2.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider2.setCustomAnimation(new DescriptionAnimation());
                        slider2.setDuration(5000);

                        slider4.addSlider(defaultSliderView);
                        slider4.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider4.setCustomAnimation(new DescriptionAnimation());
                        slider4.setDuration(5000);
                    }
                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Wait Loading...");
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("Refresh...", "Yes");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}