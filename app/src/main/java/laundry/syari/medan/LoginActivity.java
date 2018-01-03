package laundry.syari.medan;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
	String username , mail,  pass ;
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputUsername;
	EditText inputPassword;
	TextView loginErrorMsg;
	Intent a;
	SessionManager session;
	String url, success;

	// Progress Dialog
	private ProgressDialog pDialog;

	// buat json object
	JSONParser jParser = new JSONParser();


	// url to membuat produk baru
	private static String url_tambah_pendaftaran = "http://api.laundrysyari.com/androidapi/pendaftaran/login_get.php";


	// node node json
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PROFIL = "profil";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_EMAIL = "email";



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panel_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		
		
		session = new SessionManager(getApplicationContext());

		// Importing all assets like buttons, text fields
		inputUsername = (EditText) findViewById(R.id.loginUsername);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				username = inputUsername.getText().toString();
				pass  = inputPassword.getText().toString();
				new GetProfil().execute();
								
				
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
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
			pDialog = new ProgressDialog(LoginActivity.this);
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
						params.add(new BasicNameValuePair("password", pass));

						// get detail dari daftar menggunakan http request
						JSONObject json = jParser.makeHttpRequest(
								url_tambah_pendaftaran, "GET", params);

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


							// tampilkan di edit text
							mail = pendaftaran.getString(TAG_EMAIL);
							pass = pendaftaran.getString(TAG_PASSWORD);

							session.createLoginSession(mail, pass);


							Intent i = new Intent(LoginActivity.this, Dashboard.class);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
							finish();

							Log.e("ok", " ambil data");

						} else {
							// pid tidak ditemukan
							Toast.makeText(getApplicationContext(), "Proses Login Gagal", Toast.LENGTH_SHORT).show();
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