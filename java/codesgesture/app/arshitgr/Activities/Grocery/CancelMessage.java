package codesgesture.app.arshitgr.Activities.Grocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import codesgesture.app.arshitgr.Models.CustomerModel;
import codesgesture.app.arshitgr.R;
import codesgesture.app.arshitgr.Utils.SessionManage;

public class CancelMessage extends AppCompatActivity {
    CustomerModel customerModel;
    Button bthome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_message);
        customerModel=(CustomerModel) SessionManage.getCurrentUser(getApplicationContext());

        bthome=findViewById(R.id.bthome);


        bthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CancelMessage.this,Dashboard.class));
            }
        });

    }

}
