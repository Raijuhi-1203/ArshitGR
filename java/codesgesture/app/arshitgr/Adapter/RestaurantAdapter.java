package codesgesture.app.arshitgr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import codesgesture.app.arshitgr.Activities.Restaurant.PageRProduct;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.Models.RestaurantModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private ArrayList<RestaurantModel> arrayList;
    private Context context;
    String Userid="";
    private int layout;

    public RestaurantAdapter(Context context, ArrayList<RestaurantModel> arrayList, int layout) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RestaurantAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RestaurantAdapter.ViewHolder holder, final int i) {
        final RestaurantModel data = arrayList.get(i);

        holder.name.setText(data.getSeller_firm_name());
        holder.add.setText(data.getArea()+", "+data.getSeller_state_name());

        Glide.with(context).load(Constants.BASEURI2+data.getSeller_photo()).into(holder.rimg);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String str = sdf.format(new Date());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checktimings(data.getOpening_time(),data.getClosing_time(),str)){
                    Intent intent=new Intent(context, PageRProduct.class);
                    intent.putExtra("data",data);
                    context.startActivity(intent);
                }else {
                    UserUtil.ShowMsg("Restaurent opening soon..",context);
                }

            }
        });


    }

    private boolean checktimings(String time, String endtime,String currentime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);
            Date date3 = sdf.parse(currentime);

            if(date3.after(date1) && date3.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,add,rating;
        LinearLayout click;
        ImageView rimg;
        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            rating= view.findViewById(R.id.rating);
            add= view.findViewById(R.id.add);
            click= view.findViewById(R.id.click);
            rimg= view.findViewById(R.id.rimg);
        }
    }

    public void updateList(ArrayList<RestaurantModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}