package codesgesture.app.arshitgr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.PageSearchProduct;
import codesgesture.app.arshitgr.Adapter.FAQAdapter;
import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.PolicyModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageFAQ extends AppCompatActivity  {
    TextView txpolicy;

    CustomerModel customerModel;
    ArrayList<PolicyModel> policyModels=new ArrayList<>();
    FAQAdapter faqAdapter;
    RecyclerView recycler;
    ImageView person,cart;

    AutoCompleteTextView searchbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());


        txpolicy=findViewById(R.id.txpolicy);
        recycler=findViewById(R.id.recycler);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);
        searchbar=findViewById(R.id.searchbar);


        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageFAQ.this, PageSearchProduct.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageFAQ.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageFAQ.this, AccountFragment.class));
            }
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(PageFAQ.this, 1);
        recycler.setLayoutManager(mLayoutManager);
        faqAdapter = new FAQAdapter(PageFAQ.this, policyModels, R.layout.item_faq);
        recycler.setAdapter(faqAdapter);
        recycler.setItemViewCacheSize(policyModels.size());
        GetData();

    }


    private void GetData() {
        policyModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageFAQ.this);
        jc.SendRequest("get_faq", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    PolicyModel product = new PolicyModel();
                    product.setFaq_description(obj.getString("faq_description"));
                    product.setFaq_question(obj.getString("faq_question"));
                    policyModels.add(product);
                }
                faqAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPostError(String msg) {

            }
        }, "", "Loading..");
    }


}