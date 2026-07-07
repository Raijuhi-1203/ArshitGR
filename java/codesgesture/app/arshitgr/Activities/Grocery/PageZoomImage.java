package codesgesture.app.arshitgr.Activities.Grocery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.Constants;

public class PageZoomImage extends AppCompatActivity {
    String s,img;
    ImageView image,close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_zoom_image);
        img=getIntent().getStringExtra("data");

        image=findViewById(R.id.image);
        close=findViewById(R.id.close);

        String url = Constants.BASEURI2+img;
        Glide.with(PageZoomImage.this).load(url).into(image);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
