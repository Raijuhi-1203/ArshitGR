package codesgesture.app.arshitgr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;

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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private ArrayList<ProductModel> arrayList;
    private Context context;
    String Userid="";
    private int layout;
    Notify notify;
    ExtraCallBack ecb;
String str;
    public WishlistAdapter(Context context, ArrayList<ProductModel> arrayList, int layout,Notify notify1) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.notify=notify1;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
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
        holder.offmrp.setText("₹ "+data.getProduct_final_sell_price());
        holder.mrp.setText("₹ "+data.getProduct_market_price());
        holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String url = Constants.BASEURI2+data.getPhoto_path();
        Glide.with(context).load(url).into(holder.prdimg);

        holder.btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCart(Userid, data.getProduct_id(),data.getId(),data.getProduct_final_sell_price(),data.getProduct_full_name(),data.getProduct_market_price(),data.getProduct_sell_price(),data.getProduct_discount_percentage(),data.getProduct_discount_price(),data.getProduct_with_gst_Price(),data.getProduct_final_sell_price(),data.getProduct_market_price());
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveWishlist(data.getProduct_id(),Userid,data.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prdnm,offmrp,mrp;
        ImageView remove;
        ImageView prdimg;
        Button btncart;

        ViewHolder(View view) {
            super(view);
            prdnm = (TextView) view.findViewById(R.id.prdnm);
            offmrp = (TextView) view.findViewById(R.id.offmrp);
            mrp = (TextView) view.findViewById(R.id.mrp);
            prdimg=(ImageView)view.findViewById(R.id.prdimg);
            btncart=(Button) view.findViewById(R.id.btncart);
            remove=(ImageView)view.findViewById(R.id.remove);
        }
    }

    public void updateList(ArrayList<ProductModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }

    private void AddCart(String userid, String product_id, String product_final_sell_price,String price,String product_name,String product_market_price,String product_sell_price,String product_discount_percentage,String product_discount_price,String product_with_gst_Price,String product_final_sell_price2,String total_market_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("cart_qty","1"));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        param.add(new NetParam("cart_guest_id",""));
        param.add(new NetParam("total_order_amount",price));
        param.add(new NetParam("product_name",product_name));
        param.add(new NetParam("product_market_price",product_market_price));
        param.add(new NetParam("product_sell_price",product_sell_price));
        param.add(new NetParam("product_discount_percentage",product_discount_percentage));
        param.add(new NetParam("product_discount_price",product_discount_price));
        param.add(new NetParam("product_with_gst_Price",product_with_gst_Price));
        param.add(new NetParam("product_final_sell_price",product_final_sell_price2));
        param.add(new NetParam("total_market_price",total_market_price));

        jc.SendRequest("addtocart", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Move to Cart !!",context);
                RemoveWishlist(product_id,Userid,product_final_sell_price);
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }

    private void RemoveWishlist(String product_id, String userid, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress((Activity) context);
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

}