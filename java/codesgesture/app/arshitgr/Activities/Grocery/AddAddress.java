package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Fragment.AccountFragment;
import codesgesture.app.arshitgr.Fragment.CartFragment;
import codesgesture.app.arshitgr.Fragment.WishListFragment;
import codesgesture.app.arshitgr.Models.CityModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.PincodeModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.Models.StateModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class AddAddress extends AppCompatActivity {
    EditText txnm,txmail,txmob,txadd1,txadd2,txcity,txstate,txarea;
    Spinner spnrpincode;
    Button btn_save;
    CheckBox chkdefault;
    CustomerModel customerModel;
    String spnrstid,spnrctid,statnm,citynm,order_section,pincode;
    String carttotal,market_price_total;
    ArrayList<ProductModel> productModels;
//    ArrayAdapter<StateModel> stateModelArrayAdapter;
//    ArrayList<StateModel> stateModels=new ArrayList<>();
//    ArrayAdapter<CityModel> cityModelArrayAdapter;
//    ArrayList<CityModel> cityModels=new ArrayList<>();

    ArrayAdapter<PincodeModel> pincodeModelArrayAdapter;
    ArrayList<PincodeModel> pincodeModels=new ArrayList<>();
    AutoCompleteTextView searchbar;
    String id;
    ImageView person,cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        carttotal=getIntent().getStringExtra("total");
        market_price_total=getIntent().getStringExtra("market_price_total");
        order_section=getIntent().getStringExtra("order_section");
        productModels=(ArrayList<ProductModel>)getIntent().getSerializableExtra("productModel");
        id=getIntent().getStringExtra("id");
        Bindids();
        searchbar=findViewById(R.id.searchbar);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAddress.this,PageSearchProduct.class));

            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAddress.this, WishListFragment.class));
            }
        });

        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddAddress.this, AccountFragment.class));
            }
        });

        spnrpincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = spnrpincode.getSelectedItemPosition();
                spnrstid=String.valueOf(pincodeModels.get(pos).getState_id());
                statnm=String.valueOf(pincodeModels.get(pos).getState_name());
                citynm=String.valueOf(pincodeModels.get(pos).getDistrict_name());
                spnrctid=String.valueOf(pincodeModels.get(pos).getDistrict_id());
                pincode=String.valueOf(pincodeModels.get(pos).getPincode());

                txstate.setText(String.valueOf(pincodeModels.get(pos).getState_name()));
                txcity.setText(String.valueOf(pincodeModels.get(pos).getDistrict_name()));
                txarea.setText(String.valueOf(pincodeModels.get(pos).getArea()));

               // CityCall(spnrstid);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

//        spnrarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                int pos = spnrarea.getSelectedItemPosition();
//                spnrctid=String.valueOf(cityModels.get(pos).getDistrict_id());
//                citynm=String.valueOf(cityModels.get(pos).getDistrict_name());
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) { }
//        });

        pincodeModels = new ArrayList<>();
        pincodeModelArrayAdapter = new ArrayAdapter<PincodeModel>(AddAddress.this, android.R.layout.simple_spinner_item, pincodeModels);
        pincodeModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrpincode.setAdapter(pincodeModelArrayAdapter);

//        cityModels = new ArrayList<>();
//        cityModelArrayAdapter = new ArrayAdapter<CityModel>(AddAddress.this, android.R.layout.simple_spinner_item, cityModels);
//        cityModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnrarea.setAdapter(cityModelArrayAdapter);

        AreaJsonCall();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txnm.getText().length()==0){
                    txnm.setError("Enter name");
                }else if(txmob.getText().length()==0){
                    txmob.setError("Enter mobile");
                }else if(txadd1.getText().length()==0){
                    txadd1.setError("Enter address 1");
                }else if(txadd2.getText().length()==0){
                    txadd2.setError("Enter address 2");
                }else {
                    Add_Address();
                }
            }
        });

    }

//    private void CityCall(String spnrstid) {
//        cityModels.clear();
//        ArrayList<NetParam> param;
//        param = new ArrayList<NetParam>();
//        CallJson jc = new CallJson(AddAddress.this);
//        param.add(new NetParam("state_id",spnrstid));
//        jc.SendRequest("get_city", param, new JsonCallbacks() {
//            @Override
//            public void onPostSuceess(String json, String method) throws JSONException {
//                JSONArray array = new JSONArray(json);
//                for (int s = 0; s < array.length(); s++) {
//                    JSONObject obj = array.getJSONObject(s);
//                    CityModel mod = new CityModel();
//                    mod.setDistrict_id(obj.getString("district_id"));
//                    mod.setDistrict_name(obj.getString("district_name"));
//                    cityModels.add(mod);
//                    cityModelArrayAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onPostError(String msg) {
//            }
//        }, "LOGIN", "Please wait while getting..");
//    }
    private void AreaJsonCall() {
        pincodeModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(AddAddress.this);
        jc.SendRequest("get_pincode", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    PincodeModel mod = new PincodeModel();
                    mod.setState_id(obj.getString("state_id"));
                    mod.setState_name(obj.getString("state_name"));
                    mod.setDistrict_id(obj.getString("district_id"));
                    mod.setDistrict_name(obj.getString("district_name"));
                    mod.setPincode(obj.getString("pincode"));
                    mod.setId(obj.getString("id"));
                    mod.setArea(obj.getString("area"));
                    pincodeModels.add(mod);
                    pincodeModelArrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, "LOGIN", "Please wait while getting..");
    }
    
	private void Add_Address() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(AddAddress.this);
        param.add(new NetParam("address_customer_name",txnm.getText().toString()));
        param.add(new NetParam("address_customer_mobileno",txmob.getText().toString()));
        param.add(new NetParam("address_customer_email",txmail.getText().toString()));
        param.add(new NetParam("address_line_1",txadd1.getText().toString()));
        param.add(new NetParam("address_line_2",txadd2.getText().toString()));
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("address_pincode",pincode));
        param.add(new NetParam("address_pincode",pincode));
        param.add(new NetParam("address_city_id",spnrctid));
        param.add(new NetParam("address_city_name",citynm));
        param.add(new NetParam("address_state_id",spnrstid));
        param.add(new NetParam("address_state_name",statnm));
        String mdefault;
        if(chkdefault.isChecked()){
            mdefault="Yes";
        }else {
            mdefault="No";
        }
        param.add(new NetParam("address_default",mdefault));
        param.add(new NetParam("token_id",customerModel.getToken_id()));
        jc.SendRequest("addaddress", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {

                if (id.equals("1")){
                    Intent intent=new Intent(AddAddress.this,PageAddress.class);
                    intent.putExtra("total",carttotal);
                    intent.putExtra("market_price_total",market_price_total);
                    intent.putExtra("productModel",productModels);
                    intent.putExtra("order_section",order_section);
                    startActivity(intent);
                }else if(id.equals("2")){
                    Intent intent=new Intent(AddAddress.this,AddressBook.class);
                    intent.putExtra("total",carttotal);
                    intent.putExtra("market_price_total",market_price_total);
                    intent.putExtra("productModel",productModels);
                    intent.putExtra("order_section",order_section);
                    startActivity(intent);
                }
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
    }
    private void Bindids() {
        txnm=findViewById(R.id.txnm);txmail=findViewById(R.id.txmail);
        txmob=findViewById(R.id.txmob);txadd1=findViewById(R.id.txadd1);
        txadd2=findViewById(R.id.txadd2);
        // spnrarea=findViewById(R.id.spnrarea);
        spnrpincode=findViewById(R.id.spnrpincode);txarea=findViewById(R.id.txarea);
        btn_save=findViewById(R.id.btn_save);chkdefault=findViewById(R.id.chkdefault);
        txstate=findViewById(R.id.txstate);txcity=findViewById(R.id.txcity);
    }

}
