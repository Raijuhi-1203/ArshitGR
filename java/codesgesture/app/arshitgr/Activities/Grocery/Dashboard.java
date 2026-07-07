package codesgesture.app.arshitgr.Activities.Grocery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.LoginPage;
import codesgesture.app.arshitgr.Activities.Restaurant.RDashBoard;
import codesgesture.app.arshitgr.Adapter.CategoryAdapter;
import codesgesture.app.arshitgr.Adapter.ProductAdapter;
import codesgesture.app.arshitgr.Adapter.RoundCategory;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.BannerModel;
import codesgesture.app.arshitgr.Models.CategoryModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.TopSliderModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Services.Utility;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.Notify;

public class Dashboard extends AppCompatActivity  {
    ImageView person,cart;
    AutoCompleteTextView searchbar;
    CustomerModel customerModel;
    LinearLayout home,wishlist,shop,account,cartbtn;
    SliderLayout slider,slider2,slider3,slider4;
    RecyclerView rvcat,rvdeal,rvend,rvrview,rvcategory,rvprd;
    ArrayList<BannerModel> bannerModels=new ArrayList<>();
    ArrayList<TopSliderModel> topSliderModels=new ArrayList<>();
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();

    ArrayList<CategoryModel> category=new ArrayList<>();

    ProductAdapter productAdapter;
    ProductAdapter productAdapter2;
    ProductAdapter productAdapter3;
    ArrayList<ProductModel> productModels=new ArrayList<>();
    ArrayList<ProductModel> productModels2=new ArrayList<>();
    ArrayList<ProductModel> productModels3=new ArrayList<>();
    Button btndealmore,btnmostmore,btnsalemore,btnviewmore;
    RoundCategory roundCategory;
    Button btngrcry,btnres;

    LinearLayout cartamt;

    TextView txamt,btcart;
    ShimmerFrameLayout shimmer_product,shimmer_category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Fresco.initialize(Dashboard.this);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        btnmostmore=findViewById(R.id.btnmostmore);
        btnsalemore=findViewById(R.id.btnsalemore);
        btnviewmore=findViewById(R.id.btnviewmore);

        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        txamt=findViewById(R.id.txamt);
        btcart=findViewById(R.id.btcart);

        cartbtn=findViewById(R.id.cartbtn);
        btndealmore=findViewById(R.id.btndealmore);
        account=findViewById(R.id.account);
        shop=findViewById(R.id.shop);
        wishlist=findViewById(R.id.wishlist);
        home=findViewById(R.id.home);
        searchbar=findViewById(R.id.searchbar);

        shimmer_product=findViewById(R.id.shimmer_product);
        shimmer_category=findViewById(R.id.shimmer_category);
        shimmer_category.startShimmer();
        shimmer_product.startShimmer();

        slider=findViewById(R.id.slider);
        slider2=findViewById(R.id.slider2);
        slider3=findViewById(R.id.slider3);
        slider4=findViewById(R.id.slider4);

        rvrview=findViewById(R.id.rvrview);
        rvend=findViewById(R.id.rvend);
        rvdeal=findViewById(R.id.rvdeal);
        rvcat=findViewById(R.id.rvcat);
        rvcategory=findViewById(R.id.rvcategory);
        rvprd=findViewById(R.id.rvprd);

        btnres=findViewById(R.id.btnres);
        btngrcry=findViewById(R.id.btngrcry);

        cartamt=findViewById(R.id.cartamt);

//        btngrcry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Dashboard.this,Dashboard.class));
//            }
//        });

        btnres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, RDashBoard.class));
            }
        });

        btnviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,PageRecentView.class));
            }
        });
        btnmostmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,PageMostView.class));
            }
        });
        btnsalemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,PageSaleView.class));
            }
        });
        FetchBanner();
        FetchSlider();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvcategory.setLayoutManager(layoutManager);
        roundCategory = new RoundCategory(Dashboard.this, category,R.layout.round_category);
        rvcategory.smoothScrollToPosition(0);
        rvcategory.setAdapter(roundCategory);
        rvcategory.setItemViewCacheSize(category.size());
        Bindcategory();

//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
//        rvcat.setLayoutManager(layoutManager2);
//        categoryAdapter = new CategoryAdapter(Dashboard.this, categoryModels,R.layout.item_category);
//        rvcat.setAdapter(categoryAdapter);
//        GetData();

        GridLayoutManager mLayoutManager = new GridLayoutManager(Dashboard.this, 3);
        rvcat.setLayoutManager(mLayoutManager);
        categoryAdapter = new CategoryAdapter(Dashboard.this, categoryModels,R.layout.item_category);
        rvcat.setAdapter(categoryAdapter);
        GetData();

        GridLayoutManager mLayoutManager2 = new GridLayoutManager(Dashboard.this, 2);
        rvdeal.setLayoutManager(mLayoutManager2);
        productAdapter3 = new ProductAdapter(Dashboard.this, productModels3, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetProductData();
                BindCart();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetProductData();
                BindCart();
            }
        },"G","0","0");
        rvdeal.setAdapter(productAdapter3);
        rvdeal.setItemViewCacheSize(productModels3.size());
        GetProductData();

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,PageSearchProduct.class));
            }
        });

        btndealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, PageOffer.class));
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, CartFragment.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AccountFragment.class));
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, MenuFragment.class));
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, WishListFragment.class));
            }
        });

//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Dashboard.this, Dashboard.class));
//            }
//        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AccountFragment.class));
            }
        });

        BindCart();

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(Dashboard.this, 2);
        rvrview.setLayoutManager(gridLayoutManager2);
        productAdapter2 = new ProductAdapter(Dashboard.this, productModels2, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetRecentView();
                BindCart();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetRecentView();
                BindCart();
            }
        },"G","0","0");
        rvrview.setAdapter(productAdapter2);
        rvrview.setItemViewCacheSize(productModels2.size());
        GetRecentView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Dashboard.this, 2);
        rvprd.setLayoutManager(gridLayoutManager);
        productAdapter = new ProductAdapter(Dashboard.this, productModels, R.layout.item_product, new Notify() {
            @Override
            public void onAdd(ProductModel data) {
                GetRecentView();
                BindCart();
            }

            @Override
            public void onRemove(ProductModel data) {
                GetRecentView();
                BindCart();
            }
        },"G","0","0");
        rvprd.setAdapter(productAdapter);
        rvprd.setItemViewCacheSize(productModels.size());
        GetRandomProduct();

    }

    private void GetRandomProduct() {
        productModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_random_product", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                shimmer_product.stopShimmer();
                shimmer_product.setVisibility(View.GONE);
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
                    product.setProduct_CGST_percentage(obj.getString("product_CGST_percentage"));
                    product.setProduct_CGST_rate(obj.getString("product_CGST_rate"));
                    product.setProduct_SGST_percentage(obj.getString("product_SGST_percentage"));
                    product.setProduct_SGST_rate(obj.getString("product_SGST_rate"));

                    productModels.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void Bindcategory() {
        category.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
        jc.SendRequest("get_Categories", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                shimmer_category.stopShimmer();
                shimmer_category.setVisibility(View.GONE);
                rvcat.setVisibility(View.VISIBLE);
                slider.setVisibility(View.VISIBLE);
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    CategoryModel product = new CategoryModel();
                    product.setCategory_name(obj.getString("category_name"));
                    product.setCategory_id(obj.getString("category_id"));
                    product.setCategory_photo(obj.getString("category_photo"));
                    category.add(product);
                }
                roundCategory.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void GetRecentView() {
        productModels2.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_recentview", param, new JsonCallbacks() {
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
                    product.setProduct_CGST_percentage(obj.getString("product_CGST_percentage"));
                    product.setProduct_CGST_rate(obj.getString("product_CGST_rate"));
                    product.setProduct_SGST_percentage(obj.getString("product_SGST_percentage"));
                    product.setProduct_SGST_rate(obj.getString("product_SGST_rate"));

                    productModels2.add(product);
                }
                productAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void GetProductData() {
        productModels3.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
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
                    product.setProduct_CGST_percentage(obj.getString("product_CGST_percentage"));
                    product.setProduct_CGST_rate(obj.getString("product_CGST_rate"));
                    product.setProduct_SGST_percentage(obj.getString("product_SGST_percentage"));
                    product.setProduct_SGST_rate(obj.getString("product_SGST_rate"));

                    productModels3.add(product);
                }
                productAdapter3.notifyDataSetChanged();
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
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
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
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
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
                        model.setSlider_link(obj.getString("slider_link"));
                        topSliderModels.add(model);
                        DefaultSliderView defaultSliderView = new DefaultSliderView(Dashboard.this);
                        defaultSliderView.image(Constants.BASEURI2+model.getSlider_photo());
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                        defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                if (model.getSlider_link().equals("offer")){
                                    startActivity(new Intent(Dashboard.this,PageOffer.class));
                                }else {
                                    Intent intent=new Intent(Dashboard.this, PageProduct.class);
                                    intent.putExtra("data",model.getSlider_link());
                                    startActivity(intent);
                                }
                            }
                        });
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
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
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
                        DefaultSliderView defaultSliderView = new DefaultSliderView(Dashboard.this);
                        defaultSliderView.image(Constants.BASEURI2+model.getBanner_path());
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.Fit);

                        slider2.addSlider(defaultSliderView);
                        slider2.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider2.setCustomAnimation(new DescriptionAnimation());
                        slider2.setDuration(5000);

//                        slider2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (model.getSlider_link().equals("offer")){
//                                    startActivity(new Intent(Dashboard.this,PageOffer.class));
//                                }else {
//                                    Intent intent=new Intent(Dashboard.this, PageProduct.class);
//                                    intent.putExtra("data",model.getSlider_link());
//                                    startActivity(intent);
//                                }
//                            }
//                        });

                        slider4.addSlider(defaultSliderView);
                        slider4.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider4.setCustomAnimation(new DescriptionAnimation());
                        slider4.setDuration(5000);
//                        slider4.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (model.getSlider_link().equals("offer")){
//                                    startActivity(new Intent(Dashboard.this,PageOffer.class));
//                                }else {
//                                    Intent intent=new Intent(Dashboard.this, PageProduct.class);
//                                    intent.putExtra("data",model.getSlider_link());
//                                    startActivity(intent);
//                                }
//                            }
//                        });
                    }
                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Wait Loading...");
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            UpdateCArtCount();
        }
    };
    private void UpdateCArtCount() {
      //  cartno.setText(Constants.CARTCOUNT); //Constants.CARTCOUNT
        handler.postDelayed(runnable,1000);
    }

    private void BindCart() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(Dashboard.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        jc.SendRequest("get_cart_qty", param, new JsonCallbacks() {
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
                            startActivity(new Intent(Dashboard.this,CartFragment.class));
                        }
                    });
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Are you sure you want to exit from Easy Lifes application ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BindCart();
//        Intent intent = new Intent(Dashboard.this, Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

}
