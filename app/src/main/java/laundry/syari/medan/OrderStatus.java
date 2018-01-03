package laundry.syari.medan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by IQBAL on 6/20/2016.
 */
public class OrderStatus extends AppCompatActivity {

    //fungsi line ini untuk ambil data user - email
    // nama sharepreference login
    private static final String PREF_NAME = "Sesi";
    SharedPreferences sharedpreferences;


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

           /* ambil data login email */
        sharedpreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String share_email =  sharedpreferences.getString("email", null);


        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://api.laundrysyari.com/order-status.php?email="+share_email);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }


}
