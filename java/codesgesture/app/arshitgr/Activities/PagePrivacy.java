package codesgesture.app.arshitgr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PagePrivacy extends AppCompatActivity {
    TextView txpolicy;
    CustomerModel customerModel;
    AutoCompleteTextView searchbar;

    ImageView person,cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        txpolicy=findViewById(R.id.txpolicy);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PagePrivacy.this, PageSearchProduct.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PagePrivacy.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PagePrivacy.this, AccountFragment.class));
            }
        });

        BindPolicy();

    }

    private void BindPolicy() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PagePrivacy.this);
        jc.SendRequest("get_privacy", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONObject obj = UserUtil.ConvertStringToJsonObject(json);
                if (obj.length() != 0) {
                   // txpolicy.setText(obj.getString("privacy_information"));
                    txpolicy.setText(Html.fromHtml(obj.getString("privacy_information")));
                }
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");

    }


}