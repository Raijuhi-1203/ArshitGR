package codesgesture.app.arshitgr.Activities.Grocery;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
import codesgesture.app.arshitgr.Services.Utility;
import codesgesture.app.arshitgr.Utils.Constants;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageProfile extends AppCompatActivity  {
    EditText txnm,txmail,txmob,txdob,txcp,txnp;
    TextView imguserpicnm;
    ImageView img_user;
    Spinner spnrgender;
    String spnrid;
    DatePickerDialog datePickerDialog;

    private static final int PERMISSION_REQUEST_CODE = 200 ;
    private static final int PERMISSION_REQUEST_CODE1 = 100 ;
    private static final int PERMISSION_REQUEST_CODE2 = 300 ;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap userphoto;
    CustomerModel customerModel;
    ImageView person,cart;
    AutoCompleteTextView searchbar;
    Button btn_submit2,btn_submit,btn_submit3;
    String imgString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());
        txnm=findViewById(R.id.txnm);
        imguserpicnm=findViewById(R.id.imguserpicnm);
        txmob=findViewById(R.id.txmob);
        txmail=findViewById(R.id.txmail);
        txdob=findViewById(R.id.txdob);
        txcp=findViewById(R.id.txcp);
        txnp=findViewById(R.id.txnp);
        img_user=findViewById(R.id.img_user);
        spnrgender=findViewById(R.id.spnrgender);
        cart=findViewById(R.id.cart);
        person=findViewById(R.id.person);
        btn_submit2=findViewById(R.id.btn_submit2);
        btn_submit=findViewById(R.id.btn_submit);
        btn_submit3=findViewById(R.id.btn_submit3);

        txnm.setText(customerModel.getCustomer_name());
        txmob.setText(customerModel.getCustomer_mobileno());
        txmail.setText(customerModel.getCustomer_email());
        txdob.setText(customerModel.getCustomer_dob());

        if (checkPermission()) {
        } else {
            requestPermission();
        }

        searchbar=findViewById(R.id.searchbar);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProfile.this, PageSearchProduct.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProfile.this, WishListFragment.class));
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageProfile.this, AccountFragment.class));
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrgender.setAdapter(adapter);

        spnrgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnrid = spnrgender.getSelectedItem().toString();;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txdob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                BindDate();
                return false;
            }
        });

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        btn_submit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txcp.getText().length()==0){
                    txcp.setError("Please enter current password");
                }else if(txnp.getText().length()==0){
                    txnp.setError("Please enter new password");
                }else {
                    UpdatePassword();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imguserpicnm.getText().length()==0){
                    imguserpicnm.setError("Please select Picture first");
                }else {
                    SavePicture();
                }
            }
        });

        btn_submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txnm.getText().length()==0){
                    txnm.setError("Please enter name");
                }else  if (txmail.getText().length()==0){
                    txmail.setError("Please enter mail id");
                }else if (txmob.getText().length()==0){
                    txmob.setError("Please enter mobile no");
                }else if (txdob.getText().length()==0){
                    txdob.setError("Please enter Dob");
                }else {
                    UpdateData();
                }
            }
        });

    }


    private void UpdatePassword() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProfile.this);
        param.add(new NetParam("password",txcp.getText().toString()));
        param.add(new NetParam("newpass",txnp.getText().toString()));
        param.add(new NetParam("custid",customerModel.getCustomer_id()));
        jc.SendRequest("update_password", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Password Updated !!",PageProfile.this);
            }
            @Override
            public void onPostError(String msg) {

            }
        }, " ", "Loading..");
    }

    private void SavePicture() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProfile.this);
        param.add(new NetParam("b64string",imgString));
        param.add(new NetParam("custid",customerModel.getCustomer_id()));
        param.add(new NetParam("filenm",imguserpicnm.getText().toString()));
        jc.SendRequest("update_picture", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Picture Uploaded !!",PageProfile.this);
            }
            @Override
            public void onPostError(String msg) {

            }
        }, " ", "Loading..");
    }

    private void UpdateData() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageProfile.this);
        param.add(new NetParam("name",txnm.getText().toString()));
        param.add(new NetParam("mobile",txmob.getText().toString()));
        param.add(new NetParam("gender",spnrgender.getSelectedItem().toString()));
        param.add(new NetParam("mail",txmail.getText().toString()));
        param.add(new NetParam("dob",txdob.getText().toString()));
        jc.SendRequest("update_user", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Details Updated !!",PageProfile.this);
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
                        finish();
                    }
                } catch (JSONException e) {
                    Utility.ShowMEssage(PageProfile.this, "Invalid OTP !");
                    e.printStackTrace();
                }
            }
            @Override
            public void onPostError(String msg) {

            }
        }, " ", "Loading..");
    }

    private void BindDate() {
        // perform click event on text view
        txdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(PageProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                int mnth = monthOfYear+1;
                                int day = dayOfMonth;

                                if(mnth <= 9 && day <= 9){
                                    txdob.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                }else if(mnth <= 9){
                                    txdob.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }else if(day <= 9){
                                    txdob.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                }else {
                                    txdob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE1);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            userphoto = (Bitmap) data.getExtras().get("data");
            img_user.setImageBitmap(userphoto);
           // String imgnm3 = txnm.getText().toString() + "CP" + ".png";
            String imgnm3 = "demoCP" + ".png";


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            userphoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteFormat = stream.toByteArray();
            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

            imguserpicnm.setText(imgnm3);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PageProfile.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
