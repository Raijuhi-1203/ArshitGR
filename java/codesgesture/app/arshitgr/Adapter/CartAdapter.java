package codesgesture.app.arshitgr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ProductModel> arrayList;
    private Context context;
    String Userid="";
    private int layout;
    Notify notify;
    ExtraCallBack ecb;
    int a=0;

    public CartAdapter(Context context, ArrayList<ProductModel> arrayList, int layout,Notify notify1) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.notify=notify1;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
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

        if(data.getCart_qty()!=0){
            holder.cartno.setText(String.valueOf(data.getCart_qty()));
        }
        holder.prdimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PageProductDetails.class);
                intent.putExtra("data",data);
                context.startActivity(intent);
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =data.getCart_qty();
                cq++;
                data.setQty(cq);
                data.setCart_qty(cq);
                holder.cartno.setText(String.valueOf(data.getCart_qty()));
                String price = data.getProduct_final_sell_price() ;
                double totalamt = cq * Double.parseDouble(price) ;

                notify.onRemove(null);
                QtyChangeCart(totalamt,data.getCart_qty(),Userid,data.getProduct_id(),data.getId());
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cq =data.getQty();
                if (cq>0) {
                    cq--;
                    if(cq==0){
                        a=100;
                        RemoveCart(data.getProduct_id(),Userid,data.getId());
                    }else {
                        a=0;
                        data.setQty(cq);
                        data.setCart_qty(cq);
                        holder.cartno.setText(String.valueOf(data.getQty()));
                    }
                }

                if (a == 100) {

                }else if (a==0){
                    String price = data.getProduct_final_sell_price() ;
                    double totalamt = cq * Double.parseDouble(price) ;

                    notify.onRemove(null);
                    QtyChangeCart(totalamt,data.getQty(),Userid,data.getProduct_id(),data.getId());
                }
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveCart(data.getProduct_id(),Userid,data.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView prdnm,offmrp,mrp,cartno;
        ImageView add,minus,remove;
        ImageView prdimg;

        ViewHolder(View view) {
            super(view);
            prdnm = (TextView) view.findViewById(R.id.prdnm);
            offmrp = (TextView) view.findViewById(R.id.offmrp);
            mrp = (TextView) view.findViewById(R.id.mrp);
            prdimg=(ImageView)view.findViewById(R.id.prdimg);
            add=view.findViewById(R.id.add);
            cartno=view.findViewById(R.id.cartno);
            minus=view.findViewById(R.id.minus);
            remove=view.findViewById(R.id.remove);
        }
    }

    public void updateList(ArrayList<ProductModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }

    private void QtyChangeCart(double totalamt, int cart_qty, String userid, String product_id, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress((Activity) context);
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

    private void RemoveCart(String product_id, String userid, String product_final_sell_price) {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("product_id",product_id));
        param.add(new NetParam("product_price_id",product_final_sell_price));
        param.add(new NetParam("customer_id",userid));
        param.add(new NetParam("cart_guest_id",""));
        jc.SendRequest("removetocart", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Remove from Cart !!",context);
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

}