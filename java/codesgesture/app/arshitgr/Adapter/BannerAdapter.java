package codesgesture.app.arshitgr.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import codesgesture.app.arshitgr.Models.BannerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.Constants;

public class BannerAdapter extends PagerAdapter {

    private Context Mcontext;
    private List<BannerModel> bannerModels;

    public BannerAdapter(Context Mcontext, List<BannerModel> bannerModels1) {
        this.Mcontext = Mcontext;
        this.bannerModels = bannerModels1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) Mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.item_banner,null);

        ImageView featured_image = sliderLayout.findViewById(R.id.imageView);

        Uri uri=Uri.parse(Constants.BASEURI2+bannerModels.get(position).getBanner_path());
        featured_image.setImageURI(uri);
        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return bannerModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
