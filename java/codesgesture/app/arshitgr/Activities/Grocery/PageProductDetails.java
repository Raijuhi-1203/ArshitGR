package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Adapter.UnitAdapter;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductImageeModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.TopSliderModel;
import codesgesture.app.arshitgr.Models.UnitModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.ExtraCallBack;

public class PageProductDetails extends AppCompatActivity {
    ProductModel productModel;
    ArrayList<NetParam> param;
    ImageView nclick,click,add,minus; //img
    TextView txnm,offmrp,mrp,dis,fulldesp,bt_buy,btncart,cartqty,dismrp;
    CustomerModel customerModel;
    RecyclerView rvreview,rvlikeprd,rvunit;
    ExtraCallBack ecb;
    AutoCompleteTextView searchbar;
    UnitAdapter unitAdapter;
    ImageView person,cart;
    ArrayList<UnitModel> unitModels=new ArrayList<>();
    String value,cmrp,cdis,coffmrp,pid;
    String str;

    SliderLayout slider;

    ArrayList<ProductImageeModel> productImageeModels=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        customerModel=(CustomerModel)SessionManage.getCurrentUser(getApplicationContext());
        productModel=(ProductModel) getIntent().getSerializableExtra("data");
        str=getString(R.string.con);

        BindIds();

        CheckProduct();
        AddrecentView(customerModel.getCustomer_id(),productModel.getProduct_id());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvunit.setLayoutManager(layoutManager);
        unitAdapter = new UnitAdapter(PageProductDetails.this, unitModels,R.layout.item_unit,productModel.getProduct_unit(),productModel.getProduct_unit_value());
        rvunit.smoothScrollToPosition(0);
        unitAdapter.setOnRecyclerViewItemClickListener(new UnitAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(String text) {
                String[] arrOfStr = text.split(",", 2);
                String a=arrOfStr[0];
                String b=arrOfStr[1];
                getPrice(a,b);
            }
        });

        rvunit.setAdapter(unitAdapter);
        rvunit.setItemViewCacheSize(unitModels.size());
        GetunitData();

        int a = productModel.getCart_qty();
        String ab = String.valueOf(a);
        if(ab.equals("0")){
            cartqty.setText("1");
        }else {
            cartqty.setText(ab);
        }


//        String url = Constants.BASEURI2+productModel.getPhoto_path();
//        Glide.with(PageProductDetails.this).load(url).into(img);

        nclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddWishlist(customerModel.getCustomer_id(),productModel.getProduct_id(),productModel.getId());
                click.setVisibility(View.VISIBLE);
                nclick.setVisibility(View.GONE);
            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveWishlist(productModel.getProduct_id(),customerModel.getCustomer_id(),productModel.getId());
                click.setVisibility(View.GONE);
                nclick.setVisibility(View.VISIBLE);
            }
        });

        if(productModel.getCart_qty()!=0){
            cartqty.setText(String.valueOf(productModel.getCart_qty()));
        }

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCart(customerModel.getCustomer_id(), productModel.getProduct_id(),pid,productModel.getProduct_full_name(),cmrp,coffmrp,cdis,productModel.getProduct_discount_price(),productModel.getProduct_with_gst_Price(),productModel.getProduct_final_sell_price(),productModel.getProduct_market_price());
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =productModel.getCart_qty();
                cq++;
                productModel.setQty(cq);
                productModel.setCart_qty(cq);
                cartqty.setText(String.valueOf(cq));
                String price = productModel.getProduct_final_sell_price() ;
                double totalamt = cq * Double.parseDouble(price) ;
              //  QtyChangeCart(totalamt,productModel.getCart_qty(),customerModel.getCustomer_id(),productModel.getProduct_id(),productModel.getId());
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =productModel.getQty();
                if (cq>1) {
                    cq--;
                    productModel.setQty(cq);
                    productModel.setCart_qty(cq);
                    cartqty.setText(String.valueOf(cq));
                }

                String price = productModel.getProduct_final_sell_price() ;
                double totalamt = cq * Double.parseDouble(price) ;

             //   QtyChangeCart(totalamt,productModel.getQty(),customerModel.getCustomer_id(),productModel.getProduct_id(),productModel.getId());
            }
        });

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCart(customerModel.getCustomer_id(), productModel.getProduct_id(),pid,productModel.getProduct_full_name(),cmrp,coffmrp,cdis,productModel.getProduct_discount_price(),productModel.getProduct_with_gst_Price(),productModel.getProduct_final_sell_price(),productModel.getProduct_market_price());
                Intent intent=new Intent(PageProductDetails.this, CartFragment.class);
                startActivity(intent);
            }
        });

        FetchSlider();

    }


    private void FetchSlider() {
        productImageeModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageProductDetails.this);
        param.add(new NetParam("product_id",productModel.getProduct_id()));
        jc.SendRequest("get_picture", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                if (json.length() > 0) {
                    JSONArray array = new JSONArray(json);
                    for (int s = 0; s < array.length(); s++) {
                        JSONObject obj = array.getJSONObject(s);
                        ProductImageeModel model = new ProductImageeModel();
                        model.setPhoto_path(obj.getString("photo_path"));
                        productImageeModels.add(model);
                        DefaultSliderView defaultSliderView = new DefaultSliderView(PageProductDetails.this);
                        defaultSliderView.image(Constants.BASEURI2+model.getPhoto_path());
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                        defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Intent intent=new Intent(PageProductDetails.this, PageZoomImage.class);
                                intent.putExtra("data",model.getPhoto_path());
                                startActivity(intent);
                            }
                        });

                        slider.addSlider(defaultSliderView);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);


                       // slider.setCustomAnimation(new DescriptionAnimation());
                       // slider.setDuration(5000);
                    }
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Wait Loading...");
    }

    private void BindIds() {
        cart=findViewById(R.id.cart);person=findViewById(R.id.person);
        btncart=findViewById(R.id.btncart);bt_buy=findViewById(R.id.bt_buy);
       // img=findViewById(R.id.img);
        nclick=findViewById(R.id.nclick);
        click=findViewById(R.id.click);add=findViewById(R.id.add);
        minus=findViewById(R.id.minus);txnm=findViewById(R.id.txnm);
        offmrp=findViewById(R.id.offmrp);mrp=findViewById(R.id.mrp);
        dis=findViewById(R.id.dis);
        //dismrp=findViewById(R.id.dismrp);
        fulldesp=findViewById(R.id.fulldesp);

        cartqty=findViewById(R.id.cartqty);
        rvunit=findViewById(R.id.rvunit);
        searchbar=findViewById(R.id.searchbar);
        slider=findViewById(R.id.slider);

        txnm.setText(productModel.getProduct_full_name());
        offmrp.setText("₹ "+productModel.getProduct_final_sell_price());
        mrp.setText("₹ "+productModel.getProduct_market_price());
       // dismrp.setText("₹ "+productModel.getProduct_discount_percentage()+"\noff");
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        dis.setText(productModel.getProduct_discount_percentage()+"% OFF");
        fulldesp.setText(Html.fromHtml(productModel.getProduct_description()));

        cmrp=productModel.getProduct_market_price();
        cdis=productModel.getProduct_discount_percentage();
        coffmrp=productModel.getProduct_final_sell_price();
        pid=productModel.getId();

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProductDetails.this,PageSearchProduct.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProductDetails.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProductDetails.this, AccountFragment.class));
            }
        });
    }

    private void getPrice(String text, String b) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageProductDetails.this);
        param.add(new NetParam("product_unit",b));
        param.add(new NetParam("product_id",productModel.getProduct_id()));
        param.add(new NetParam("product_unit_value",text));
        jc.SendRequest("get_price", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    offmrp.setText("₹ "+obj.getString("product_final_sell_price"));
                    mrp.setText("₹ "+obj.getString("product_market_price"));
                    mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    dis.setText(obj.getString("product_discount_percentage")+"% OFF");

                    cmrp=obj.getString("product_market_price");
                    cdis=obj.getString("product_discount_percentage");
                    coffmrp=obj.getString("product_final_sell_price");
                    pid=obj.getString("id");

                  //  mrp.setText(obj.getString("status"));
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "LOGIN", "Please wait while getting..");
    }

    private void CheckProduct() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageProductDetails.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("product_id",productModel.getProduct_id()));
        param.add(new NetParam("product_price_id",productModel.getId()));
        jc.SendRequest("get_chkproduct", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    value = obj.getString("status");
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "LOGIN", "Please wait while getting..");
    }

    private void GetunitData() {
        unitModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageProductDetails.this);
        param.add(new NetParam("product_id",productModel.getProduct_id()));
        jc.SendRequest("get_product_unit", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    UnitModel product = new UnitModel();
                    product.setProduct_unit(obj.getString("product_unit"));
                    product.setProduct_unit_value(obj.getString("product_unit_value"));
                    unitModels.add(product);
                }
                unitAdapter.notifyDataSetChanged();
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void AddrecentView(String customer_id, String product_id) {
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(PageProductDetails.this);
        param.add(new NetParam("product_id", product_id));
        param.add(new NetParam("customer_id", customer_id));
        jc.SendRequest("addrecentview", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
               // UserUtil.ShowMsg("Review Posted Successfully.",PageProductDetails.this);
            }
            @Override
            public void onPostError(String msg) {

            }
        }, "post_review", "Please wait while getting..");
    }

    private void AddCart(String userid, String product_id, String product_final_sell_price,String product_name,String product_market_price,String product_sell_price,String product_discount_percentage,String product_discount_price,String product_with_gst_Price,String product_final_sell_price2,String total_market_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProductDetails.this);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("cart_qty",cartqty.getText().toString()));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        param.add(new NetParam("cart_guest_id",""));
        param.add(new NetParam("total_order_amount",productModel.getProduct_final_sell_price()));
        param.add(new NetParam("product_name",product_name));
        param.add(new NetParam("product_market_price",product_market_price));
        param.add(new NetParam("product_sell_price",product_sell_price));
        param.add(new NetParam("product_discount_percentage",product_discount_percentage));
        param.add(new NetParam("product_discount_price",product_discount_price));
        param.add(new NetParam("product_with_gst_Price",product_with_gst_Price));
        param.add(new NetParam("product_final_sell_price",product_final_sell_price2));
        param.add(new NetParam("total_market_price",total_market_price));
        param.add(new NetParam("token_id",customerModel.getToken_id()));
        param.add(new NetParam("product_sellerid","0"));
        param.add(new NetParam("product_sellername","0"));
        param.add(new NetParam("cart_section","Grocery"));
        jc.SendRequest("addtocart", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Add to Cart !!",PageProductDetails.this);
                if (ecb != null){
                    ecb.OnCompleted("removed","removed");
                }

            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }
    private void RemoveWishlist(String product_id, String userid, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProductDetails.this);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        jc.SendRequest("removetowishlist", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Remove from wishlist !!",PageProductDetails.this);
                if (ecb != null){
                    ecb.OnCompleted("removed","removed");
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }
    private void AddWishlist(String userid, String product_id, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProductDetails.this);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("wishlist_qty","1"));
        param.add(new NetParam("customer_id",userid));
        jc.SendRequest("addtowishlist", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Added in wishlist !!",PageProductDetails.this);
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Please wait..");
    }


}
