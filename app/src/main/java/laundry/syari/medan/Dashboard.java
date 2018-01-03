package laundry.syari.medan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Mohammad Iqbal Programmer Ganteng on 6/10/2016.
 */

public class Dashboard  extends Activity {

    //inisialisasi Script untuk cek apakah ada login
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    //shared prefernces untuk data order dan harga
    public static final String oderPREFERENCES = "orderPreference" ;
    public static final String hargaKey = "harga";
    SharedPreferences orderSharedpreferences;

    private AdView mAdView;

    ///tambahan script untuk login
    SessionManager session;


    ImageView img_order_now, img_bantuan, img_order_status, img_order_history, img_tos, img_about, img_pricing, img_partnership, img_what_news, img_profil;
    TextView tv_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());

        Intent i = getIntent();
        String harga_kiloan = i.getStringExtra("harga_kiloan");

        // simpan data harga kiloan sebelum di lempar ke dashboard atau login
        orderSharedpreferences = getSharedPreferences(oderPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = orderSharedpreferences.edit();
        editor.putString(hargaKey, harga_kiloan);
        editor.commit();


        String sts = String.valueOf(session.isLoggedIn());
        if(sts.equals("true")) {


            setContentView(R.layout.dashboard);

            tv_home = (TextView)findViewById(R.id.tvhome);


            img_order_now = (ImageView) findViewById(R.id.img_order_now);
            img_tos = (ImageView) findViewById(R.id.img_tos);
            img_about = (ImageView) findViewById(R.id.img_about);
            img_pricing = (ImageView) findViewById(R.id.img_pricing);
            img_partnership = (ImageView) findViewById(R.id.img_partnership);
            img_what_news = (ImageView) findViewById(R.id.img_what_news);
            img_profil = (ImageView) findViewById(R.id.img_profil);
            img_order_status = (ImageView) findViewById(R.id.img_order_status);
            img_order_history = (ImageView) findViewById(R.id.img_order_history);
            img_bantuan = (ImageView)findViewById(R.id.img_bantuan);




            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);


            img_order_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //Cek apakah gps hidup atau  mati
                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        dialogJikaGpsMati();
                    }else{
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Dashboard.this, OrderNow.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }
            });


            img_order_status.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, OrderStatus.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_order_history.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, OrderHistory.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_tos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, TOS.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_about.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, About.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_pricing.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   Intent intent = new Intent(Dashboard.this, Pricing.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
                }
            });


            img_partnership.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                   Intent intent = new Intent(Dashboard.this, Pathership.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
                }
            });

            img_what_news.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, WhatNews.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_profil.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(Dashboard.this, Profil.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            img_bantuan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // We normally won't show the welcome slider again in real app
                    // but this is for testing
                    PrefManager prefManager = new PrefManager(getApplicationContext());

                    // make first time launch TRUE
                    prefManager.setFirstTimeLaunch(true);

                    startActivity(new Intent(Dashboard.this, WelcomeActivity.class));
                    finish();
                }
            });

        }else {

            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            //	 Closing dashboard screen
            finish();

        }


    }


    private void dialogJikaGpsMati() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Untuk Menggunakan Fitur ini anda harus mengaktifkan GPS Anda, \n Ingin Menyalakan GPS ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

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




}
