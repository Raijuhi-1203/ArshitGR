package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.Order;
import codesgesture.app.arshitgr.Models.OrderProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class CancelOrderForm extends AppCompatActivity  {

    Order order;
    CustomerModel customerModel;
    Spinner spnr;
    String spnrid;
    ImageView person,cart;
    EditText reason;
    Button btnsubmit;
    OrderProductModel orderProductModel;

    AutoCompleteTextView searchbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_orderform);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        order=(Order)getIntent().getSerializableExtra("order");
        orderProductModel=(OrderProductModel)getIntent().getSerializableExtra("orderproduct");

        reason=findViewById(R.id.reason);
        btnsubmit=findViewById(R.id.btnsubmit);
        spnr=findViewById(R.id.spnr);
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CancelOrderForm.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CancelOrderForm.this, AccountFragment.class));
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CancelOrderForm.this,PageSearchProduct.class);
                intent.putExtra("search",searchbar.getText().toString());
                startActivity(intent);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cancelorder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr.setAdapter(adapter);

        spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnrid = spnr.getSelectedItem().toString();;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reason.getText().length()==0){
                    reason.setError("Please write reason..");
                }else {
                    if (orderProductModel == null){
                        CancelOrder();
                    }else if (order == null) {
                        CancelItem();
                    }
                }
            }
        });
    }

    private void CancelItem() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(CancelOrderForm.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("product_id",orderProductModel.getProduct_id()));
        param.add(new NetParam("order_return_reason",spnrid));
        param.add(new NetParam("order_return_comment",reason.getText().toString()));
        param.add(new NetParam("order_id",orderProductModel.getOrder_id()));
        param.add(new NetParam("token_id",customerModel.getToken_id()));
        jc.SendRequest("Cancel_order_Product", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Item Cancel Successfully.",CancelOrderForm.this);
                startActivity(new Intent(CancelOrderForm.this,CancelMessage.class));
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

    private void CancelOrder() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(CancelOrderForm.this);
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("order_return_reason",spnrid));
        param.add(new NetParam("order_return_comment",reason.getText().toString()));
        param.add(new NetParam("order_id",order.getOrder_id()));
        param.add(new NetParam("token_id",customerModel.getToken_id()));
        jc.SendRequest("Cancel_Allorder", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
               UserUtil.ShowMsg("Order Cancel Successfully.",CancelOrderForm.this);
               startActivity(new Intent(CancelOrderForm.this,CancelMessage.class));
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "", "Loading..");
    }

}
