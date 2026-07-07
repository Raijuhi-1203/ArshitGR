package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.MainActivity;
import codesgesture.app.arshitgr.Activities.PageAbout;
import codesgesture.app.arshitgr.Activities.PageContact;
import codesgesture.app.arshitgr.Activities.PageDeveloper;
import codesgesture.app.arshitgr.Activities.PageFAQ;
import codesgesture.app.arshitgr.Activities.PagePrivacy;
import codesgesture.app.arshitgr.Activities.PageRefund;
import codesgesture.app.arshitgr.Activities.PageTerm;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.ShopFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class MenuFragment extends AppCompatActivity  {
    CustomerModel model;
    ImageView person,cart;
    RecyclerView recycler;
    LinearLayout home,wishlist,shop,account,cartbtn;
    AutoCompleteTextView searchbar;
    CardView cat,offer,order,carts,wish,acc,track,lg,contact,rate,share,faq,about,term,privacy,refund,developer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop);
        model=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        recycler=findViewById(R.id.recycler);
        cartbtn=findViewById(R.id.cartbtn);
        account=findViewById(R.id.account);
        shop=findViewById(R.id.shop);
        wishlist=findViewById(R.id.wishlist);
        home=findViewById(R.id.home);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);


        BindIds();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(MenuFragment.this, CartFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(MenuFragment.this, AccountFragment.class));
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(MenuFragment.this, WishListFragment.class));
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(3,true);
                startActivity(new Intent(MenuFragment.this, AccountFragment.class));
            }
        });
//        shop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(2,true);
//                startActivity(new Intent(MenuFragment.this, MenuFragment.class));
//            }
//        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(1,true);
                startActivity(new Intent(MenuFragment.this, WishListFragment.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(0,true);
                startActivity(new Intent(MenuFragment.this, Dashboard.class));
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this,PageSearchProduct.class));
            }
        });

    }

    private void BindIds() {
        developer=findViewById(R.id.developer);cat=findViewById(R.id.cat);
        offer=findViewById(R.id.offer);order=findViewById(R.id.order);
        carts=findViewById(R.id.carts);wish=findViewById(R.id.wish);
        acc=findViewById(R.id.acc);track=findViewById(R.id.track);
        lg=findViewById(R.id.lg);contact=findViewById(R.id.contact);
        share=findViewById(R.id.share);rate=findViewById(R.id.rate);
        about=findViewById(R.id.about);faq=findViewById(R.id.faq);
        term=findViewById(R.id.term);privacy=findViewById(R.id.privacy);
        refund=findViewById(R.id.refund);

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, ShopFragment.class));
            }
        });
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageOffer.class));
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageOrder.class));
            }
        });
        carts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, CartFragment.class));
            }
        });
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, WishListFragment.class));
            }
        });
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, AccountFragment.class));
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageOffer.class));
            }
        });
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManage.ClearSession(getApplicationContext());
                startActivity(new Intent(MenuFragment.this, MainActivity.class));
                finish();
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageFAQ.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageAbout.class));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PagePrivacy.class));
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageContact.class));
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtil.ShareApp(MenuFragment.this);
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
                startActivity(new Intent(MenuFragment.this, PageTerm.class));
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageDeveloper.class));
            }
        });
        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFragment.this, PageRefund.class));
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