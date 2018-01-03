package laundry.syari.medan;

/**
 * Created by iqbalhood on 05/03/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Cari_Lokasi extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    EditText txt_cari_lokasi_jemput;
    ImageView btn_cari_lokasi_jemput;

    //ambil data email user untuk dicocokkan ke data lokasi
    String Cari;

    // buat json object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> lokasiList;

    // url untuk get semua buku tamu
    private static String url_semua_lokasi = "http://api.laundrysyari.com/androidapi/pendaftaran/lokasi.php";
    private static String url_lokasi_cari = "http://api.laundrysyari.com/androidapi/pendaftaran/lokasi_cari.php";

    // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LOKASI = "lokasi";

    private static final String TAG_LATI = "lati";
    private static final String TAG_LOGI = "logi";
    private static final String TAG_NAMA = "nama";


    // lokasi JSONArray
    JSONArray lokasi = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cari_lokasi_jemput);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);


        txt_cari_lokasi_jemput = (EditText) findViewById(R.id.txt_cari_lokasi_jemput);
        btn_cari_lokasi_jemput = (ImageView) findViewById(R.id.btn_cari_lokasi_jemput);

        // Hashmap untuk ListView
        lokasiList = new ArrayList<HashMap<String, String>>();
        ListView lv = (ListView) findViewById(R.id.lv_lokasi);






        btn_cari_lokasi_jemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cari = txt_cari_lokasi_jemput.getText().toString();
                lokasiList.clear();
                //  Toast.makeText(getApplicationContext(), "Lokasi Dicari"+Cari, Toast.LENGTH_SHORT).show();
                new CariLokasi().execute();

            }
        });


        // Loading products in Background Thread
        new LoadSemuaLokasi().execute();

        //lvItem = (ListView)findViewById(R.id.lv_item);

        // Get listview


        // select single bukutamu
        // Jalankan tampilan edit buku tamu
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // dapatkan nilai dari list lokasi
                String lati = ((TextView) view.findViewById(R.id.lati)).getText()
                        .toString();

                String logi = ((TextView) view.findViewById(R.id.logi)).getText()
                        .toString();


                // Memulai aktifitas baru
                Intent in = new Intent(getApplicationContext(), PickerPlace.class);
                // kirimkan pid ke activity selanjutnya
                in.putExtra("lat_real", lati);
                in.putExtra("log_real", logi);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // memulai activity baru dengan mnghrap bbrapa kembalian response
                startActivityForResult(in, 100);
            }
        });



    }




    /**
     * Background Async Task untuk menampilkan semua daftar bukutamu menggunakan http request
     */
    class LoadSemuaLokasi extends AsyncTask<String, String, String> {

        /**
         * sebelum melakukan thread di background maka jalankan progres dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cari_Lokasi.this);
            pDialog.setMessage("Mohon tunggu, Loading Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * dapetkan semua produk dari get url di background
         */
        protected String doInBackground(String... args) {



            // Buat Parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // ambil json dari url
            JSONObject json = jParser.makeHttpRequest(url_semua_lokasi, "GET", params);


            // cek logcat untuk response dari json
            Log.d("Semua Lokasi: ", json.toString());

            try {
                // cek jika tag success
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // data ditemukan
                    // ambil array dari bukutamu
                    lokasi = json.getJSONArray(TAG_LOKASI);

                    // tampilkan perulangan semua produk
                    for (int i = 0; i < lokasi.length(); i++) {
                        JSONObject c = lokasi.getJSONObject(i);

                        // simpan pada variabel
                        String lati = c.getString(TAG_LATI);
                        String logi = c.getString(TAG_LOGI);
                        String nama = c.getString(TAG_NAMA);

                        // buat new hashmap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // key => value
                        map.put(TAG_LATI, lati);
                        map.put(TAG_LOGI, logi);
                        map.put(TAG_NAMA, nama);

                        // masukan HashList ke ArrayList
                        lokasiList.add(map);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lokasi Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika pekerjaan di belakang layar selesai maka hentikan progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // hentikan progress ketika semua data didapat
            pDialog.dismiss();

            // perbarui screen
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * perbarui json ke arraylist
                     * */
                    ListView lvItem;
                    lvItem = (ListView) findViewById(R.id.lv_lokasi);
                    ListAdapter adapter = new SimpleAdapter(
                            Cari_Lokasi.this, lokasiList,
                            R.layout.list_lokasi, new String[]{TAG_LATI,
                            TAG_LOGI, TAG_NAMA},
                            new int[]{R.id.lati, R.id.logi, R.id.nama});
                    // perbarui list lokasi
                    lvItem.setAdapter(null);
                    lvItem.setAdapter(adapter);

                }
            });

        }

    }


    /**
     * Background Async Task untuk menampilkan semua daftar bukutamu menggunakan http request
     */
    class CariLokasi extends AsyncTask<String, String, String> {

        /**
         * sebelum melakukan thread di background maka jalankan progres dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cari_Lokasi.this);
            pDialog.setMessage("Mohon tunggu, Loading Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * dapetkan semua produk dari get url di background
         */
        protected String doInBackground(String... args) {
            // Buat Parameter
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cari", Cari));

            // ambil json dari url
            JSONObject json = jParser.makeHttpRequest(url_lokasi_cari, "GET", params);


            // cek logcat untuk response dari json
            Log.d("Semua Lokasi: ", json.toString());

            try {
                // cek jika tag success
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // data ditemukan
                    // ambil array dari bukutamu
                    lokasi = json.getJSONArray(TAG_LOKASI);

                    // tampilkan perulangan semua produk
                    for (int i = 0; i < lokasi.length(); i++) {
                        JSONObject c = lokasi.getJSONObject(i);

                        // simpan pada variabel
                        String lati = c.getString(TAG_LATI);
                        String logi = c.getString(TAG_LOGI);
                        String nama = c.getString(TAG_NAMA);

                        // buat new hashmap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // key => value
                        map.put(TAG_LATI, lati);
                        map.put(TAG_LOGI, logi);
                        map.put(TAG_NAMA, nama);

                        // masukan HashList ke ArrayList
                        lokasiList.add(map);
                    }
                } else {
                    // jika tidak ada data
                    // maka jalankan tambahkan buku tamu
                    lokasiList.clear();
                   /* Intent i = new Intent(getApplicationContext(),
                          Cari_Lokasi_Jemput.class);
                    // tutup semua proses sebelumnya
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * jika pekerjaan di belakang layar selesai maka hentikan progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // hentikan progress ketika semua data didapat

            pDialog.dismiss();

            // perbarui screen
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * perbarui json ke arraylist
                     * */
                    ListView lvItem;
                    lvItem = (ListView) findViewById(R.id.lv_lokasi);
                    ListAdapter adapter = new SimpleAdapter(
                            Cari_Lokasi.this, lokasiList,
                            R.layout.list_lokasi, new String[]{TAG_LATI,
                            TAG_LOGI, TAG_NAMA},
                            new int[]{R.id.lati, R.id.logi, R.id.nama});
                    // perbarui list lokasi
                    lvItem.setAdapter(adapter);



                }
            });

        }

    }


}
