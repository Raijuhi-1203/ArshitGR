package codesgesture.app.arshitgr.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.Dashboard;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Services.Utility;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class RegisterUser extends AppCompatActivity implements JsonCallbacks {
    Button btn_register,btn_login;
    EditText nm,mob,pass;
    ArrayList<NetParam> param;
    EditText text;
    String str,token;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        str=getString(R.string.con);
        nm = findViewById(R.id.nm);
        mob = findViewById(R.id.mob);
        pass = findViewById(R.id.pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        token = getAlphaNumericString(25);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUser.this, LoginPage.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validte()) {
                    Register_User();
                }
            }
        });
    }

    static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    private void Register_User() {
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(RegisterUser.this);
        param.add(new NetParam("mobile", mob.getText().toString()));
        param.add(new NetParam("name", nm.getText().toString()));
        param.add(new NetParam("password", pass.getText().toString()));
        param.add(new NetParam("token_id", token));
       // jc.SendRequest("register_user", param, RegisterUser.this, "register_user", "Please wait while verifying..");
        jc.SendRequest("register_user", param, RegisterUser.this, "register_user", "Please wait while verifying..");
    }

    @Override
    public void onPostSuceess(String json, String method) throws JSONException {
        CustomerModel sd = new CustomerModel();
        try {
            JSONObject obj = UserUtil.ConvertStringToJsonObject(json);
            if (obj.length() != 1) {
                sd.setCustomer_date(obj.getString("customer_date"));
                sd.setId(obj.getString("id"));
                sd.setCustomer_dob(obj.getString("customer_dob"));
                sd.setCustomer_email(obj.getString("customer_email"));
                sd.setCustomer_gender(obj.getString("customer_gender"));
                sd.setCustomer_id(obj.getString("customer_id"));
                sd.setCustomer_mobileno(obj.getString("customer_mobileno"));
                sd.setCustomer_name(obj.getString("customer_name"));
                sd.setCustomer_password(obj.getString("customer_password"));
                sd.setCustomer_profilephoto(obj.getString("customer_profilephoto"));
                sd.setCustomer_status(obj.getString("customer_status"));
                sd.setCustomer_temp_id(obj.getString("customer_temp_id"));
                sd.setOtp(obj.getString("otp"));
                sd.setEmail_verification_code(obj.getString("email_verification_code"));
                sd.setEmail_verified(obj.getString("email_verified"));
                sd.setMobileno_verified(obj.getString("mobileno_verified"));
                sd.setCustomer_time(obj.getString("customer_time"));
                sd.setToken_id(obj.getString("token_id"));
                SessionManage.SetCustomerSessions(getApplicationContext(), sd);
                Intent act = new Intent(RegisterUser.this, Dashboard.class); //OTPage
                startActivity(act);
                UserUtil.ShowMsg("Register Successfully !", RegisterUser.this);
                finish();
            } else {
                Utility.ShowMEssage(RegisterUser.this, "Invalid details !");
            }
        } catch (JSONException e) {
            Utility.ShowMEssage(RegisterUser.this, "Invalid details !");
            e.printStackTrace();
        }
    }

    @Override
    public void onPostError(String msg) {

    }

    private boolean validte() {
        Boolean valid = true;

        String m = mob.getText().toString();
        m= String.valueOf(m.length());
        int a = Integer.parseInt(m);

        if (nm.getText().length()==0){
            nm.setError("Please enter name");
            valid=false;
        }else  if (a != 10){
            mob.setError("Please enter valid mobile no");
            valid=false;
        }else  if (pass.getText().length()==0){
            pass.setError("Please enter password");
            valid=false;
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

}
