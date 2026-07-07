package codesgesture.app.arshitgr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageContact extends AppCompatActivity  {
    Button btncall,btnwhtsapp;
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;
    ImageView person,cart;
    String str;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        str=getString(R.string.con);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageContact.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageContact.this, AccountFragment.class));
            }
        });

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageContact.this, PageSearchProduct.class));
            }
        });
        btnwhtsapp=findViewById(R.id.btnwhtsapp);
        btncall=findViewById(R.id.btncall);

        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "7070090046"));
                    startActivity(intent);
                }
            }
        });
        btnwhtsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number="7070090046";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 26) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9876543210"));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}