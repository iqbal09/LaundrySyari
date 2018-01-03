package laundry.syari.medan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 6/10/2016.
 */
public class EstimasiBerat extends AppCompatActivity {



    Button btnSubmit;
    Button btnTambah, btnKurang;
    TextView txtJumlah, txtKeterangan, txtHarga;
    LinearLayout ubah;


    int jumlah = 1;
    int hargaKiloan;
    int harga = 1;

    String tgl              = "";
    String jam              = "";
    String detail_lokasi    = "";
    String info_tambahan    = "";
    String pub_email;

    String view_tanggal     = "";
    String view_jam         = "";

    String alamat_jemput    = "";
    String lat_jemput       = "";
    String log_jemput       = "";

    String tanggal_selesai  = "";

    //fungsi line ini untuk ambil data user - email
    // nama sharepreference login
    private static final String PREF_NAME = "Sesi";
    SharedPreferences sharedpreferences;


    //shared prefernces untuk data order dan harga
    public static final String oderPREFERENCES = "orderPreference" ;
    public static final String hargaKey = "harga";
    SharedPreferences orderSharedpreferences;

    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    // url to membuat produk baru
    private static String url_tambah_order = "http://api.laundrysyari.com/androidapi/pendaftaran/create_order.php";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estimasi_berat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);




        //ambil data dari aktivitas order now
        Bundle bundle = getIntent().getExtras();
        tgl = bundle.getString("tgl");
        jam = bundle.getString("jam");
        detail_lokasi = bundle.getString("detail_lokasi");
        info_tambahan = bundle.getString("info_tambahan");
        view_tanggal = bundle.getString("view_tanggal");
        view_jam = bundle.getString("view_jam");
        alamat_jemput = bundle.getString("alamat_jemput");
        lat_jemput =  bundle.getString("lat_j");
        log_jemput =  bundle.getString("log_j");
        tanggal_selesai =  bundle.getString("tgl_selesai");



        btnSubmit           = (Button)findViewById(R.id.btnSubmit);
        btnTambah           = (Button)findViewById(R.id.btnTambah);
        btnKurang           = (Button)findViewById(R.id.btnKurang);

        txtJumlah           = (TextView)findViewById(R.id.txtJumlah);
        txtKeterangan       = (TextView)findViewById(R.id.txtKeterangan);
        txtHarga            = (TextView)findViewById(R.id.txtHarga);

        ubah = (LinearLayout)findViewById(R.id.ubah);

        txtJumlah.setText("1");

          /* ambil data login email */
        sharedpreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String share_email =  sharedpreferences.getString("email", null);

        orderSharedpreferences = getSharedPreferences(oderPREFERENCES, Context.MODE_PRIVATE);
        String hargaKiloanStr = orderSharedpreferences.getString("harga", null);
        txtHarga.setText("Rp. "+hargaKiloanStr);
        int hrgKilo =  Integer.parseInt(hargaKiloanStr);
        hargaKiloan = hrgKilo;

        pub_email = share_email;

        txtKeterangan.setText("Tanggal Penjemputan: \n" +view_tanggal+
                                                "\n Jam Penjemputan: \n"
                                                + view_jam +
                                                "\n Alamat: \n"
                                                +alamat_jemput+"\n"
                                                +"\n Detail Alamat: \n"
                                                +detail_lokasi+"\n"
                                                );

        ubah.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OrderNowEdit.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



        btnTambah.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                tambahJumlah();
                countHarga();
                txtJumlah.setText(jumlah + "");
                txtHarga.setText("Rp. "+harga);
            }
        });

        btnKurang.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                kurangJumlah();
                countHarga();
                txtJumlah.setText(jumlah + "");
                txtHarga.setText("Rp. "+harga);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
               // Intent intent = new Intent(EstimasiBerat.this, OrderBerhasil.class);
                //startActivity(intent);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // get komplit pendaftaran dari thread di background
                new CreateNewOrder().execute();
            }
        });
    }

    public void tambahJumlah(){
        if(jumlah ==100 ){
            return ;
        }
        jumlah = jumlah + 1;
    }

    public void kurangJumlah(){
        if(jumlah ==1 ){
            return ;
        }
        jumlah = jumlah - 1;
    }


    public void countHarga(){
         harga = jumlah * hargaKiloan;
    }



    public int getRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }


    //jika back button di klik
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), OrderNowEdit.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }
    //jika back button di klik



    @Override
    public void onDestroy() {

        super.onDestroy();

        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
    }







    /**
     * Background Async Task untuk membuat buku tamu baru
     * */
    class CreateNewOrder extends AsyncTask<String, String, String> {

        /**
         * tampilkan progress dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EstimasiBerat.this);
            pDialog.setMessage("Sedang memproses order laundry...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * buat bukutamu baru
         * */
        protected String doInBackground(String... args) {

           int ido = getRandomNumber(100000, 999999);
           String id_order = String.valueOf(ido);

            // parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_order", id_order));
            params.add(new BasicNameValuePair("tgl", tgl));
            params.add(new BasicNameValuePair("jam", jam));
            params.add(new BasicNameValuePair("detail_lokasi", detail_lokasi));
            params.add(new BasicNameValuePair("info_tambahan", info_tambahan));
            params.add(new BasicNameValuePair("jumlah", ""+jumlah));
            params.add(new BasicNameValuePair("email", ""+pub_email));
            params.add(new BasicNameValuePair("alamat", alamat_jemput));
            params.add(new BasicNameValuePair("lat", lat_jemput));
            params.add(new BasicNameValuePair("log", log_jemput));

            // json object
            JSONObject json = jsonParser.makeHttpRequest(url_tambah_order,
                    "POST", params);

            // cek respon di logcat
            Log.d("Create Response", json.toString());

            // cek tag success
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // sukses buat pendaftaran
                    Intent i = new Intent(getApplicationContext(), OrderBerhasil.class);
                    i.putExtra("id_order", id_order);
                    i.putExtra("tgl_selesai", tanggal_selesai);
                    startActivity(i);
                } else {
                    // jika gagal
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika proses selesai maka hentikan progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }



    }









}
