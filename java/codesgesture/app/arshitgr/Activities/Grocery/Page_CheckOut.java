package codesgesture.app.arshitgr.Activities.Grocery;

import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import codesgesture.app.arshitgr.Activities.MainActivity;
import codesgesture.app.arshitgr.Models.AddressModel;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.Models.ProductModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class Page_CheckOut extends AppCompatActivity {
    String carttotal,market_price_total,order_section;
    CustomerModel customerModel;
    TextView txmrp,txsmrp,txdis,txcdis,txpamt,btorder,idTVSelectedTime;
    EditText couponcode;
    Button btapply;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ArrayList<ProductModel> productModels;
    ArrayList<AddressModel> addressModels;
    AutoCompleteTextView searchbar;
    double discount=0,shipping=0,coupondis=0,totalpay=0;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute,addminute;
    String amPm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_details);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        carttotal=getIntent().getStringExtra("total");
        market_price_total=getIntent().getStringExtra("market_price_total");
        order_section=getIntent().getStringExtra("order_section");
        productModels=(ArrayList<ProductModel>)getIntent().getSerializableExtra("productModel");
        addressModels=(ArrayList<AddressModel>)getIntent().getSerializableExtra("address");

        radioGroup=findViewById(R.id.radioGroup);
        btapply=findViewById(R.id.btapply);
        couponcode=findViewById(R.id.couponcode);
        txmrp=findViewById(R.id.txmrp);
        txsmrp=findViewById(R.id.txsmrp);
        txdis=findViewById(R.id.txdis);
        txcdis=findViewById(R.id.txcdis);
        txpamt=findViewById(R.id.txpamt);
        btorder=findViewById(R.id.btorder);

        searchbar=findViewById(R.id.searchbar);
        idTVSelectedTime=findViewById(R.id.idTVSelectedTime);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Page_CheckOut.this,PageSearchProduct.class));

            }
        });

        txmrp.setText(market_price_total);
        Double finalp,mprice,dis;
        finalp=Double.parseDouble(carttotal);
        mprice=Double.parseDouble(market_price_total);
        dis=mprice-finalp;

        txdis.setText(String.format("%.02f",dis));

        float ctotal = Float.parseFloat(carttotal);
        if (ctotal < 99 && order_section.equals("Restaurant")){
            txsmrp.setText(productModels.get(0).getProduct_shipping_charge());
        } else if (ctotal < 149 && order_section.equals("Grocery")) {
            txsmrp.setText(productModels.get(0).getProduct_shipping_charge());
        }else {
            txsmrp.setText("0.0");
        }

        txcdis.setText("0.00");


        discount=Double.parseDouble(txdis.getText().toString());
        shipping=Double.parseDouble(txsmrp.getText().toString());
        coupondis=Double.parseDouble(txcdis.getText().toString());
        totalpay = Double.parseDouble(carttotal) + shipping-discount-coupondis;

        txpamt.setText(String.format("%.02f",totalpay));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton=findViewById(i);
                radioButton.getText().toString();
            }
        });

        btorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (idTVSelectedTime.getText().toString().equals("00:00")){
//                    idTVSelectedTime.setError("Please select delivery time after 30 min.");
//                }else {
//                    OrderNow();
//                }
                OrderNow();
            }
        });

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE,30);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        idTVSelectedTime.setText(formattedDate);

        idTVSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(Page_CheckOut.this,R.style.TimePicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay < 6){
                            UserUtil.ShowMsg("Time select between 06:00 AM to 10:00 PM",Page_CheckOut.this);
                        } else if (hourOfDay > 22) {
                            UserUtil.ShowMsg("Time select between 06:00 AM to 10:00 PM",Page_CheckOut.this);
                        }else {
                            idTVSelectedTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        }
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

    }

    private void OrderNow() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        String orderitms= new Gson().toJson(productModels);
        String address= new Gson().toJson(addressModels);
        CallJson jc = new CallJson(Page_CheckOut.this);
        param.add(new NetParam("payment_mode",radioButton.getText().toString()));
        param.add(new NetParam("coupan_value",txcdis.getText().toString()));
        param.add(new NetParam("coupan_code",couponcode.getText().toString()));

        float f = Float.parseFloat(carttotal);
        int ctotal = (int) f;
        param.add(new NetParam("total_order_amount",String.valueOf(ctotal)));

        param.add(new NetParam("order_section",order_section));
        param.add(new NetParam("items",orderitms));
        param.add(new NetParam("address",address));
        param.add(new NetParam("guest_id",""));
        param.add(new NetParam("customer_id",customerModel.getCustomer_id()));
        param.add(new NetParam("token_id",customerModel.getToken_id()));
        param.add(new NetParam("delivery_schedule_time",idTVSelectedTime.getText().toString()));
        jc.SendRequest("addorder", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                startActivity(new Intent(Page_CheckOut.this,PageSuccefull.class));
                UserUtil.ShowMsg("Order Placed !!",Page_CheckOut.this);
            }
            @Override
            public void onPostError(String msg) {

            }
        }, " ", "Loading..");
    }
}
