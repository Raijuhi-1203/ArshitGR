package codesgesture.app.arshitgr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;

public class CustomAdapter extends ArrayAdapter<ProductModel> {
    private List<ProductModel> countryListFull;

    public CustomAdapter(@NonNull Context context, @NonNull List<ProductModel> countryList) {
        super(context, 0, countryList);
        countryListFull = new ArrayList<>(countryList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_people, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.nm);
       // ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);

        ProductModel countryItem = getItem(position);

        if (countryItem != null) {
            textViewName.setText(countryItem.getProduct_full_name());
           // imageViewFlag.setImageResource(countryItem.getFlagImage());
        }

        return convertView;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ProductModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(countryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ProductModel item : countryListFull) {
                    if (item.getProduct_full_name().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ProductModel) resultValue).getProduct_full_name();
        }
    };
}