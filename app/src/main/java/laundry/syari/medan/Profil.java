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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iqbalhood on 06/03/16.
 */
public class Profil extends AppCompatActivity {

    // properti dari data detail list orderan
    EditText  profilName;
    EditText  profilHandphone;
    EditText  profilEmail;

    String nama, handphone;


    ImageView btn_simpan_profil;


    //ambil data email user untuk dicocokkan ke data pendaftaran
    String Email_User;

    // nama sharepreference login
    private static final String PREF_NAME = "Sesi";

    SharedPreferences sharedpreferences;


    // Progress Dialog
    private ProgressDialog pDialog;

    // buat json object
    JSONParser jParser = new JSONParser();

    // url untuk halaman single dari bukutamu
    private static final String url_pendaftaran_details = "http://api.laundrysyari.com/androidapi/pendaftaran/profil.php";
    private static final String url_ubah_profil = "http://api.laundrysyari.com/androidapi/pendaftaran/ubah_profil.php";


    // node node json
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROFIL = "profil";
    private static final String TAG_NAMA = "nama";
    private static final String TAG_HP = "hp";
    private static final String TAG_EMAIL = "email";




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        btn_simpan_profil = (ImageView)findViewById(R.id.btn_simpan_profil);

         /* ambil data login email */
        sharedpreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String share_email =  sharedpreferences.getString("email", null);

        Email_User = share_email;



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // get komplit pendaftaran dari thread di background
        new GetProfil().execute();


        btn_simpan_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilName            = (EditText)    findViewById(R.id.profilName);
                profilHandphone       = (EditText)    findViewById(R.id.profilHandphone);

                nama        = profilName.getText().toString();
                handphone   = profilHandphone.getText().toString();



                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                new SetProfil().execute();

                Toast.makeText(getApplicationContext(), "Data Berhasil Di Ubah", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }





    /**
     * jalankan get semua product di background
     * */
    class GetProfil extends AsyncTask<String, String, String> {

        /**
         * jika memulai get activity maka jalankan progress dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profil.this);
            pDialog.setMessage("Mengambil Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * mulai jalankan get semua daftar dan jalankan di background
         * */
        protected String doInBackground(String... params) {

            // pembaharuan ui dari thread yg dijalankan
            runOnUiThread(new Runnable() {
                public void run() {
                    // Cek tag success
                    int success;
                    try {
                        // buat paramater
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", Email_User));

                        // get detail dari daftar menggunakan http request
                        JSONObject json = jParser.makeHttpRequest(
                                url_pendaftaran_details, "GET", params);

                        // cek log kita dg json response
                        Log.d("Single Product Details", json.toString());

                        // tag success json
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // sukses mendapat detail daftar
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PROFIL); // JSON
                            // Array

                            // get objek daftar pertama dari json array
                            JSONObject pendaftaran = productObj
                                    .getJSONObject(0);

                            // temukan daftar menggunakan pid
                            profilName            = (EditText)    findViewById(R.id.profilName);
                            profilHandphone       = (EditText)    findViewById(R.id.profilHandphone);
                            profilEmail           = (EditText)    findViewById(R.id.profilEmail);

                            // tampilkan di edit text
                            profilName.setText(pendaftaran.getString(TAG_NAMA));
                            profilHandphone.setText(pendaftaran.getString(TAG_HP));
                            profilEmail.setText(Email_User);
                            profilEmail.setKeyListener(null);



                        } else {
                            // pid tidak ditemukan
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * jika pekerjaan di background selesai maka hentikan progress yg
         * berjalan
         * **/
        protected void onPostExecute(String file_url) {
            // hentikan progress dialog untuk get
            pDialog.dismiss();
        }
    }







    /**
     * async task untuk menyimpan pendaftaran
     * */
    class SetProfil extends AsyncTask<String, String, String> {

        /**
         * jika proses di background akan berjalan maka tampilkan progress
         * dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profil.this);
            pDialog.setMessage("Menyimpan data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * menyimpan data
         * */
        protected String doInBackground(String... args) {



            // buat parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("name", nama));
            params.add(new BasicNameValuePair(TAG_HP, handphone));
            params.add(new BasicNameValuePair(TAG_EMAIL, Email_User));

            // kirim data pembaharuan melalui http request
            // get detail dari daftar menggunakan http request
            JSONObject json = jParser.makeHttpRequest(
                    url_ubah_profil, "GET", params);

            // cek tag success json
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // sukses di perbaharui
                    // data pendaftaran berhasil di hapus
                    // kirimkan kode 100
                    Intent i = getIntent();
                    // jika pendaftaran / entri menuju buku tamu berhasil
                    setResult(100, i);
                    finish();
                } else {
                    // jika tidak maka gagal
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika pekerjaan di background selesai maka hentikan progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // hentikan progress dialog untuk menympan data
            pDialog.dismiss();
        }
    }




}
