package codesgesture.app.arshitgr.Activities.Restaurant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import codesgesture.app.arshitgr.Activities.Grocery.PageOrder;
import codesgesture.app.arshitgr.Activities.MainActivity;
import codesgesture.app.arshitgr.Activities.PageAbout;
import codesgesture.app.arshitgr.Activities.PageContact;
import codesgesture.app.arshitgr.Activities.PageDeveloper;
import codesgesture.app.arshitgr.Activities.PageFAQ;
import codesgesture.app.arshitgr.Activities.PagePrivacy;
import codesgesture.app.arshitgr.Activities.PageRefund;
import codesgesture.app.arshitgr.Activities.PageTerm;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageRMenu extends AppCompatActivity {
    CustomerModel model;
    CardView order,carts,acc,lg,contact,rate,share,faq,about,term,privacy,refund,developer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_page);
        model=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        BindIds();

        ImageView btback=findViewById(R.id.btback);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=findViewById(R.id.title);
        title.setText("Menu");
    }

    private void BindIds() {
        developer=findViewById(R.id.developer);
        order=findViewById(R.id.order);
        carts=findViewById(R.id.carts);
        acc=findViewById(R.id.acc);
        lg=findViewById(R.id.lg);contact=findViewById(R.id.contact);
        share=findViewById(R.id.share);rate=findViewById(R.id.rate);
        about=findViewById(R.id.about);faq=findViewById(R.id.faq);
        term=findViewById(R.id.term);privacy=findViewById(R.id.privacy);
        refund=findViewById(R.id.refund);


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(PageRMenu.this, PageOrder.class));
            }
        });
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, RCart.class));
            }
        });

        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, RProfile.class));
            }
        });

        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManage.ClearSession(getApplicationContext());
                startActivity(new Intent(PageRMenu.this, MainActivity.class));
                finish();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageFAQ.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageAbout.class));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PagePrivacy.class));
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageContact.class));
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtil.ShareApp(PageRMenu.this);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageTerm.class));
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageDeveloper.class));
            }
        });
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageRMenu.this, PageRefund.class));
            }
        });

    }

    private void rateApp() {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 26)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


}