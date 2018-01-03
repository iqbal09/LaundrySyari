package laundry.syari.medan;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Iqbal
 * @email iqbalhood@gmail.com
 */

public class RegisterActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    String username, name, email, phone, password;
    EditText inputUsername;
    EditText inputName;
    EditText inputEmail;
    EditText inputPhone;
    EditText inputDesc;

    Button btnLinkToLoginScreen;

    // url to membuat produk baru
    private static String url_tambah_pendaftaran = "http://api.laundrysyari.com/androidapi/pendaftaran/create_pendaftaran_get.php";

    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_register);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Edit Text
        inputUsername = (EditText) findViewById(R.id.registerUsername);
        inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputDesc = (EditText) findViewById(R.id.registerPassword);
        inputPhone = (EditText) findViewById(R.id.registerPhone);




        // button untuk buat pendaftaran baru
        Button btnCreatePendaftaran = (Button) findViewById(R.id.btnRegister);

        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // event jika di klik
        btnCreatePendaftaran.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // jalankan buat pendaftaran di background
                username = inputUsername.getText().toString();
                name = inputName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputDesc.getText().toString();
                phone  = inputPhone.getText().toString();
                new CreateProfil().execute();
            }
        });


        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // jalankan buat pendaftaran di background
               Intent k = new Intent(RegisterActivity.this, LoginActivity.class);
               k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(k);
            }
        });


    }








    /**
     * jalankan get semua product di background
     * */
    class CreateProfil extends AsyncTask<String, String, String> {

        /**
         * jika memulai get activity maka jalankan progress dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
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
                        params.add(new BasicNameValuePair("username", username));
                        params.add(new BasicNameValuePair("name", name));
                        params.add(new BasicNameValuePair("email", email));
                        params.add(new BasicNameValuePair("password", password));
                        params.add(new BasicNameValuePair("phone", phone));

                        // get detail dari daftar menggunakan http request
                        JSONObject json = jParser.makeHttpRequest(url_tambah_pendaftaran, "GET", params);

                        // cek log kita dg json response
                        Log.d("Single Product Details", json.toString());

                        // tag success json
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // sukses mendapat detail daftar





                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                            Log.e("ok", " ambil data");

                        } else {
                            // pid tidak ditemukan
                            Toast.makeText(getApplicationContext(), "Proses Registrasi Gagal", Toast.LENGTH_SHORT).show();
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
}