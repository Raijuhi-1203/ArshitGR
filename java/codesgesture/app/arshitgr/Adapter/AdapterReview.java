package codesgesture.app.arshitgr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Models.ReviewModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class AdapterReview extends RecyclerView.Adapter<AdapterReview.ViewHolder> {
    private ArrayList<ReviewModel> arrayList;
    private Context context;
    String Userid="";
    private int layout;

    public AdapterReview(Context context, ArrayList<ReviewModel> arrayList, int layout) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
        this.Userid = SessionManage.getCurrentUser(context.getApplicationContext()).getCustomer_id();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final ReviewModel data = arrayList.get(i);

        holder.txnm.setText(data.getReviwer_name());
        holder.txreview.setText(data.getReviewer_message());
        holder.ratingBar.setRating(data.getReview_star());
        holder.ratingBar.setClickable(false);
        holder.ratingBar.setOnClickListener(null);
        holder.ratingBar.setFocusable(false);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txnm,txreview;
        RatingBar ratingBar;

        ViewHolder(View view) {
            super(view);
            txnm = (TextView) view.findViewById(R.id.txnm);
            ratingBar=(RatingBar) view.findViewById(R.id.ratingBar);
            txreview= view.findViewById(R.id.txreview);
        }
    }

    public void updateList(ArrayList<ReviewModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}