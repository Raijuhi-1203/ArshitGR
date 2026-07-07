package codesgesture.app.arshitgr.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.AddressBook;
import codesgesture.app.arshitgr.Activities.Grocery.Dashboard;
import codesgesture.app.arshitgr.Activities.MainActivity;
import codesgesture.app.arshitgr.Activities.Grocery.MenuFragment;
import codesgesture.app.arshitgr.Activities.PageAbout;
import codesgesture.app.arshitgr.Activities.Grocery.PageCancleOrder;
import codesgesture.app.arshitgr.Activities.PageContact;
import codesgesture.app.arshitgr.Activities.Grocery.PageOrder;
import codesgesture.app.arshitgr.Activities.Grocery.PageProfile;
import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class AccountFragment extends AppCompatActivity  {
    TextView txname,txmob;
    ImageView person,cart;
    CardView btnohistory,btnpinfo,btaddress,btncorder,btnwish,btnsupport,btnabout,btnlogout;
    LinearLayout home,wishlist,shop,account,cartbtn;
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_myac);
        customerModel=(CustomerModel)SessionManage.getCurrentUser(getApplicationContext());
        txname=findViewById(R.id.txname);
        txmob=findViewById(R.id.txmob);
        btnohistory=findViewById(R.id.btnohistory);
        btnpinfo=findViewById(R.id.btnpinfo);
        btaddress=findViewById(R.id.btaddress);
        btncorder=findViewById(R.id.btncorder);
        btnwish=findViewById(R.id.btnwish);
        btnsupport=findViewById(R.id.btnsupport);
        btnabout=findViewById(R.id.btnabout);
        btnlogout=findViewById(R.id.btnlogout);
        cartbtn=findViewById(R.id.cartbtn);
        account=findViewById(R.id.account);
        shop=findViewById(R.id.shop);
        wishlist=findViewById(R.id.wishlist);
        home=findViewById(R.id.home);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);
        txname.setText(customerModel.getCustomer_name());
        txmob.setText(customerModel.getCustomer_mobileno());

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(AccountFragment.this, WishListFragment.class));
            }
        });
//        person.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(3,true);
//                startActivity(new Intent(AccountFragment.this, AccountFragment.class));
//            }
//        });
        btnohistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this, PageOrder.class));
            }
        });
        btnpinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this, PageProfile.class));
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManage.ClearSession(getApplicationContext());
                startActivity(new Intent(AccountFragment.this, MainActivity.class));
               finish();
            }
        });
        btaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this, AddressBook.class));
            }
        });
        btncorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(AccountFragment.this, PageCancleOrder.class));
            }
        });
//        btnwish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), PageWishList.class));
//            }
//        });
        btnsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this, PageContact.class));
            }
        });
        btnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this, PageAbout.class));
            }
        });
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(4,true);
                startActivity(new Intent(AccountFragment.this, CartFragment.class));
            }
        });
//        account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                viewPager.setCurrentItem(3,true);
//                startActivity(new Intent(AccountFragment.this, AccountFragment.class));
//            }
//        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(2,true);
                startActivity(new Intent(AccountFragment.this, MenuFragment.class));
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(1,true);
                startActivity(new Intent(AccountFragment.this, WishListFragment.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                viewPager.setCurrentItem(0,true);
                startActivity(new Intent(AccountFragment.this, Dashboard.class));
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountFragment.this,PageSearchProduct.class));
            }
        });

    }


}