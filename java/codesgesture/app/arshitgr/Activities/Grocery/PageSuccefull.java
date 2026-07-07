package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class PageSuccefull extends AppCompatActivity {
    TextView txname,txmob;
    CustomerModel customerModel;
    Button bthome,btorder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successfull);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        txname=findViewById(R.id.txname);
        txmob=findViewById(R.id.txmob);
        btorder=findViewById(R.id.btorder);
        bthome=findViewById(R.id.bthome);

        txname.setText(customerModel.getCustomer_name());
        txmob .setText("You'll receive a SMS on "+customerModel.getCustomer_mobileno()+" once your order is confirmed.");

        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageSuccefull.this,Dashboard.class));
            }
        });
        btorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PageSuccefull.this,PageOrder.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
