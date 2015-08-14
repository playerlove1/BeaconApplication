package com.example.user.beaconapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class patient extends AppCompatActivity {

     String beaconid;
    TextView t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Bundle bundle = getIntent().getExtras();
        beaconid=bundle.getString("p1");
        t=(TextView) findViewById(R.id.center);
        t.setText("取得beacon id2:"+beaconid);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconApplication) this.getApplicationContext()).setMonitoringActivity(this);
        ((BeaconApplication) this.getApplicationContext()).setp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconApplication) this.getApplicationContext()).setMonitoringActivity(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void stopthis()
    {
        this.finish();
    }
}
