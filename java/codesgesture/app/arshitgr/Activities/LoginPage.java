package codesgesture.app.arshitgr.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.arshitgr.Activities.Grocery.Dashboard;
import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Services.CallJson;
import codesgesture.app.arshitgr.Services.CallJsonWithoutProgress;
import codesgesture.app.arshitgr.Services.JsonCallbacks;
import codesgesture.app.arshitgr.Services.NetParam;
import codesgesture.app.arshitgr.Services.UserUtil;
import codesgesture.app.arshitgr.Services.Utility;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class LoginPage extends AppCompatActivity implements JsonCallbacks {
    EditText mob,pass;
    Button btn_submit;
    ArrayList<NetParam> param;
    CustomerModel customerModel;
    TextView register;
    String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        str=getString(R.string.con);
        btn_submit=findViewById(R.id.btn_submit);
        mob=findViewById(R.id.mob);
        pass=findViewById(R.id.pass);
        register=findViewById(R.id.register);
        CheckLogins();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate()) {
                    param = new ArrayList<NetParam>();
                    CallJson jc = new CallJson(LoginPage.this);
                    param.add(new NetParam("mobileno", mob.getText().toString()));
                    param.add(new NetParam("PASSWORD", pass.getText().toString()));
                    jc.SendRequest("loginprocess", param, LoginPage.this, "loginprocess", "Please wait while verifying..");
                   // jc.SendRequest(str,"loginprocess", param, LoginPage.this, "loginprocess", "Please wait while verifying..");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this,RegisterUser.class));
            }
        });

    }

    private void CheckLogins() {
        customerModel = SessionManage.getCurrentUser(this);
        if (customerModel != null) {
            if (customerModel.getCustomer_id() != null) {
                Intent act = new Intent(LoginPage.this, Dashboard.class);
                startActivity(act);
                finish();
            }
        }
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
                Intent act = new Intent(LoginPage.this, Dashboard.class);
                startActivity(act);
                UserUtil.ShowMsg("Login Successfully !", LoginPage.this);
                finish();
            } else {
                Utility.ShowMEssage(LoginPage.this, "Invalid Login user and Password!");
            }
        } catch (JSONException e) {
            Utility.ShowMEssage(LoginPage.this, "Invalid Login user and Password!");
            e.printStackTrace();
        }
    }

    @Override
    public void onPostError(String msg) {

    }

    private boolean Validate() {
        Boolean valid = true;

        String m = mob.getText().toString();
        m= String.valueOf(m.length());
        int a = Integer.parseInt(m);

        if (a != 10) {
            mob.setError("Please  Enter Valid Mobile No.");
            valid=false;
        } else if (pass.getText().length() == 0) {
            pass.setError("Please Enter Password");
            valid=false;
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
