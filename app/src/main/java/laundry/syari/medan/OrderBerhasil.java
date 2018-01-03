package laundry.syari.medan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ACER on 6/10/2016.
 */
public class OrderBerhasil extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private String TAG = OrderBerhasil.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    private AdView mAdView;

    Button backHome;
    TextView tampilIdOrder;
    String id_order ="";
    String tgl_selesai = "";

    TextView tglSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_berhasil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        Bundle bundle = getIntent().getExtras();
        id_order = bundle.getString("id_order");
        tgl_selesai = bundle.getString("tgl_selesai");

        String ts = tgl_selesai;
        String tgl_tampil ="";


        backHome = (Button)findViewById(R.id.backHome);
        tampilIdOrder = (TextView)findViewById(R.id.tampilIdOrder);
        tglSelesai = (TextView)findViewById(R.id.tglSelesai);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format1.parse(ts);
            tgl_tampil =  String.valueOf(format2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }



    /*    String tampilTanggal = "";

        try {

            tampilTanggal = forUser.format(myFormat.parse(tgl_selesai));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        tampilIdOrder.setText("#"+id_order);
        tglSelesai.setText("Tanggal Selesai : "+tgl_tampil);

        backHome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(OrderBerhasil.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
/*
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest ar = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(ar);


*/
        /*
        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });

            }
        }, SPLASH_TIME_OUT);

*/




    }


 /*   private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
*/

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }



}
