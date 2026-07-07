package codesgesture.app.arshitgr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.OrderDetails;
import codesgesture.app.arshitgr.Models.Order;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class CancelOrderAdapter extends RecyclerView.Adapter<CancelOrderAdapter.ViewHolder> {
    private ArrayList<Order> arrayList;
    private Context context;
    String Userid="";
    private int layout;
    String msg1;

    public CancelOrderAdapter(Context context, ArrayList<Order> arrayList, int layout,String msg) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.msg1=msg;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final Order data = arrayList.get(i);

        holder.txorderid.setText("Order Id : "+data.getOrder_id());
        holder.txdate.setText(" Placed on : "+data.getOrder_delivery_date());
        holder.txstatus.setText("Status : "+data.getOrder_status());

      //  holder.cancel.setVisibility(View.GONE);

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetails.class);
                intent.putExtra("data",data);
                intent.putExtra("msg",msg1);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txorderid,txdate,txstatus;
        LinearLayout click;

      //  Button cancel;

        ViewHolder(View view) {
            super(view);
            txorderid = view.findViewById(R.id.txorderid);
            txdate= view.findViewById(R.id.txdate);
            txstatus= view.findViewById(R.id.txstatus);
            click= view.findViewById(R.id.click);
           // cancel= view.findViewById(R.id.cancel);
        }
    }

    public void updateList(ArrayList<Order> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}