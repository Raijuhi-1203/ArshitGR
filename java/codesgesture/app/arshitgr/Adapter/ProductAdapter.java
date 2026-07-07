package codesgesture.app.arshitgr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import codesgesture.app.arshitgr.Activities.Grocery.PageProductDetails;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;
import codesgesture.app.arshitgr.interfaces.ExtraCallBack;
import codesgesture.app.arshitgr.interfaces.Notify;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<ProductModel> arrayList;
    private Context context;
    String Userid="";
    private int layout;
    Notify notify;
    ExtraCallBack ecb;
    String str,token;
    String num ="N",seller_id,firm_name;
    public ProductAdapter(Context context, ArrayList<ProductModel> arrayList, int layout,Notify notify1,String n,String seller_id,String firm_name) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.notify=notify1;
        this.num=n;
        this.seller_id=seller_id;
        this.firm_name=firm_name;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
        this.token = SessionManage.getCurrentUser(context.getApplicationContext()).getToken_id();
        str= context.getString(R.string.con);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final ProductModel data = arrayList.get(i);

        holder.prdnm.setText(data.getProduct_full_name());
        holder.unit.setText(" "+data.getProduct_unit_value() + data.getProduct_unit());
        holder.offmrp.setText("₹ "+data.getProduct_final_sell_price());
        holder.mrp.setText("₹ "+data.getProduct_market_price());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (data.getProduct_discount_price().equals("0.0")){
            holder.dismrp.setVisibility(View.GONE);
            holder.offimg.setVisibility(View.GONE);
        }else {
            holder.dismrp.setText("₹ "+data.getProduct_discount_price()+"\noff");
        }
        String url = Constants.BASEURI2+data.getPhoto_path();
        Glide.with(context).load(url).into(holder.prdimg);

        holder.click_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(num, "R")){

                }else if (Objects.equals(num, "G")){
                    Intent intent=new Intent(context, PageProductDetails.class);
                    intent.putExtra("data",data);
                    context.startActivity(intent);
                }
            }
        });

        holder.nclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddWishlist(Userid,data.getProduct_id(),data.getId());
                holder.click.setVisibility(View.VISIBLE);
                holder.nclick.setVisibility(View.GONE);
            }
        });

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveWishlist(data.getProduct_id(),Userid,data.getId());
                holder.click.setVisibility(View.GONE);
                holder.nclick.setVisibility(View.VISIBLE);
            }
        });

        if(data.getCart_qty()!=0){
            holder.btncart.setVisibility(View.GONE);
            holder.cart_qty.setVisibility(View.VISIBLE);
            holder.cartno.setText(String.valueOf(data.getCart_qty()));
        }

        holder.btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String section = "";
                if (data.getProduct_id().startsWith("F")){
                    section="Restaurant";
                } else if (data.getProduct_id().startsWith("P")) {
                    section="Grocery";
                }

                AddCart(section,Userid, data.getProduct_id(),data.getId(),data.getProduct_final_sell_price(),data.getProduct_full_name(),data.getProduct_market_price(),data.getProduct_sell_price(),data.getProduct_discount_percentage(),data.getProduct_discount_price(),data.getProduct_with_gst_Price(),data.getProduct_final_sell_price(),data.getProduct_market_price());
                holder.btncart.setVisibility(View.GONE);
                holder.cart_qty.setVisibility(View.VISIBLE);
                holder.cartno.setText(String.valueOf(data.getCart_qty()));
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =data.getCart_qty();
                cq++;
                data.setQty(cq);
                data.setCart_qty(cq);
               // holder.cartno.setText(String.valueOf(data.getCart_qty()));
                String price = data.getProduct_final_sell_price() ;
                double totalamt = cq * Double.parseDouble(price) ;
                notify.onRemove(null);
                QtyChangeCart(data.getCart_qty(),Userid,data.getProduct_id(),data.getId(),totalamt);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =data.getQty();
                if (cq>1) {
                    cq--;
                    data.setQty(cq);
                    data.setCart_qty(cq);
                  //  holder.cartno.setText(String.valueOf(data.getQty()));
                }

                String price = data.getProduct_final_sell_price() ;
                double totalamt = cq * Double.parseDouble(price) ;
                notify.onRemove(null);
                QtyChangeCart(data.getQty(),Userid,data.getProduct_id(),data.getId(),totalamt);

            }
        });

    }

    private void QtyChangeCart(int cart_qty, String userid, String product_id, String product_final_sell_price, double totalamt) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("cart_qty",String.valueOf(cart_qty)));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        param.add(new NetParam("cart_guest_id",""));
        param.add(new NetParam("total_order_amount",String.valueOf(String.format("%.02f",totalamt))));
        jc.SendRequest("addtocart_changeqty", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
               // UserUtil.ShowMsg("change !!",context);
                if (ecb != null){
                    ecb.OnCompleted("removed","removed");
                }
                notify.onAdd(null);
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }

    private void AddCart(String section,String userid, String product_id, String product_final_sell_price,String price,String product_name,String product_market_price,String product_sell_price,String product_discount_percentage,String product_discount_price,String product_with_gst_Price,String product_final_sell_price2,String total_market_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("cart_qty","1"));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        param.add(new NetParam("cart_guest_id",""));
        param.add(new NetParam("total_order_amount",price));
        param.add(new NetParam("cart_section",section));
        param.add(new NetParam("product_name",product_name));
        param.add(new NetParam("product_market_price",product_market_price));
        param.add(new NetParam("product_sell_price",product_sell_price));
        param.add(new NetParam("product_discount_percentage",product_discount_percentage));
        param.add(new NetParam("product_discount_price",product_discount_price));
        param.add(new NetParam("product_with_gst_Price",product_with_gst_Price));
        param.add(new NetParam("product_final_sell_price",product_final_sell_price2));
        param.add(new NetParam("total_market_price",total_market_price));
        param.add(new NetParam("token_id",token));

        if (product_id.startsWith("P")){
            param.add(new NetParam("product_sellerid","0"));
            param.add(new NetParam("product_sellername","0"));
        }else if (product_id.startsWith("F")){
            param.add(new NetParam("product_sellerid",seller_id));
            param.add(new NetParam("product_sellername",firm_name));
        }

        jc.SendRequest("addtocart", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Add to Cart !!",context);
                if (ecb != null){
                    ecb.OnCompleted("removed","removed");
                }
                notify.onAdd(null);
                BindCart();
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }

    private void BindCart() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress((Activity) context);
        param.add(new NetParam("customer_id",Userid));
        jc.SendRequest("get_cart_qty", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONObject obj = UserUtil.ConvertStringToJsonObject(json);
                if (obj.length() != 1) {
                    Constants.CARTCOUNT = obj.getString("carttotal");
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");

    }

    private void RemoveWishlist(String product_id, String userid, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        jc.SendRequest("removetowishlist", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Remove from wishlist !!",context);
                if (ecb != null){
                    ecb.OnCompleted("removed","removed");
                }
                notify.onAdd(null);
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }

    private void AddWishlist(String userid, String product_id, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("wishlist_qty","1"));
        param.add(new NetParam("customer_id",userid));
        jc.SendRequest("addtowishlist", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Added in wishlist !!",context);
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Please wait..");
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prdnm,offmrp,mrp,cartno,unit,dismrp;
        ImageView nclick,click,add,minus;
        Button btncart;
        ImageView prdimg,offimg;
        LinearLayout click_layout,cart_qty;

        ViewHolder(View view) {
            super(view);
            unit = (TextView) view.findViewById(R.id.unit);
            prdnm = (TextView) view.findViewById(R.id.prdnm);
            offmrp = (TextView) view.findViewById(R.id.offmrp);
            cartno = (TextView) view.findViewById(R.id.cartno);
            mrp = (TextView) view.findViewById(R.id.mrp);
            prdimg=(ImageView)view.findViewById(R.id.prdimg);
            add=(ImageView)view.findViewById(R.id.add);
            minus=(ImageView)view.findViewById(R.id.minus);
            nclick=(ImageView)view.findViewById(R.id.nclick);
            click=(ImageView)view.findViewById(R.id.click);
            click_layout= view.findViewById(R.id.click_layout);
            btncart= view.findViewById(R.id.btncart);
            cart_qty= view.findViewById(R.id.cart_qty);
            dismrp= view.findViewById(R.id.dismrp);
            offimg= view.findViewById(R.id.offimg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList(ArrayList<ProductModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }

}
