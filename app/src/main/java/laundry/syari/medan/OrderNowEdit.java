package laundry.syari.medan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderNowEdit extends AppCompatActivity {

    GPSTracker gps;

    //Respon ambil lokasi saat Di Set di Shared Preferences
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    //shared prefernces untuk data order dan harga
    public static final String oderPREFERENCES = "orderPreference" ;
    SharedPreferences orderSharedpreferences;

    // untuk location manager
    //location fucking initialitation
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    Button btnTanggal, btnJam, btnNext, lokasianda;
    EditText ed_detail_lokasi;
    EditText ed_info_tambahan;

    private Spinner spinner1;

    private TimePicker timePicker1;
    private TextView time;
    private String format = "";

    String KirimTanggal = "";
    String KirimJam = "";

    String stringLatitude;
    String stringLongitude;

    String lokasi_jemput= "";
    String lat_j = "";
    String log_j = "";

    String tanggalSelesai = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_now);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);


        orderSharedpreferences = getSharedPreferences(oderPREFERENCES, Context.MODE_PRIVATE);

        String tampilTanggal            = orderSharedpreferences.getString("view_tanggal", null);
        String tampilJam                = orderSharedpreferences.getString("view_jam", null);
        String tampilinfoTambahan       = orderSharedpreferences.getString("info_tambahan", null);
        String tampiDetailLokasi        = orderSharedpreferences.getString("detail_lokasi", null);

        String getTanggal               = orderSharedpreferences.getString("tgl", null);
        String getJam                   = orderSharedpreferences.getString("jam", null);
        String getLat                   = orderSharedpreferences.getString("lat_j", null);
        String getLog                   = orderSharedpreferences.getString("log_j", null);
        String getAlamat                = orderSharedpreferences.getString("alamat_jemput", null);

        KirimTanggal = getTanggal;
        KirimJam = getJam;
        lat_j = getLat;
        log_j = getLog;
        lokasi_jemput = getAlamat;

        String untildate = KirimTanggal;
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );
        Log.e("Set Tanggalnya = ","I shouldn't be " + untildate );

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cll = Calendar.getInstance();
        try {
            cll.setTime( dateFormat.parse(untildate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cll.add( Calendar.DATE, 1 );
        String convertedDate=dateFormat.format(cll.getTime());
        Log.e("KODE TANGGAL + 1","I shouldn't be " + convertedDate );
        tanggalSelesai = convertedDate;

        if( getIntent().getExtras() != null)
        {
            Intent myIntent = getIntent();
            lat_j               = myIntent.getStringExtra("lat_j");
            log_j               = myIntent.getStringExtra("log_j");
            lokasi_jemput       = myIntent.getStringExtra("lokasi_jemput");
        }

        //bermain di data lokasi

        // Deklarasi element view
        ed_detail_lokasi = (EditText) findViewById(R.id.ed_detail_lokasi);
        btnTanggal = (Button) findViewById(R.id.btnTanggal);
        btnJam = (Button) findViewById(R.id.btnJam);
        btnNext = (Button) findViewById(R.id.btnNext);
        lokasianda = (Button) findViewById(R.id.lokasianda);
        spinner1 = (Spinner) findViewById(R.id.spinner_info_tambahan);



        //########## data hasil shared preferences ditampilkan kembali
        //============================================================
        btnTanggal.setText(tampilTanggal);
        btnJam.setText(tampilJam);
        ed_detail_lokasi.setText(tampiDetailLokasi);
        //ed_info_tambahan.setText(tampilinfoTambahan);
        spinner1.setSelection(PosisiSpinner(spinner1, tampilinfoTambahan));



        //########## data hasil shared preferences ditampilkan kembali
        //============================================================


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month + 1, day);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        //showTime(hour, minute);

        if(lokasi_jemput == null || lokasi_jemput == ""){
            lokasianda.setText("Pilih Lokasi Jemput Pakaian");
        }else{
            lokasianda.setText(lokasi_jemput);
        }


        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {
                showDialog(999);
            }
        });

        btnJam.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {
                // ketika button jam di klik
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OrderNowEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        showTime(selectedHour, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            //On click function
            public void onClick(View view) {

                String detail_lokasi = ed_detail_lokasi.getText().toString();
                String info_tambahan = String.valueOf(spinner1.getSelectedItem());

                String view_tanggal = btnTanggal.getText().toString();
                String view_jam = btnJam.getText().toString();

                // simpan data ke shared preferences
                orderSharedpreferences = getSharedPreferences(oderPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = orderSharedpreferences.edit();
                editor.putString("tgl_selesai",tanggalSelesai);
                editor.putString("tgl", KirimTanggal);
                editor.putString("jam", KirimJam);
                editor.putString("detail_lokasi", detail_lokasi);
                editor.putString("info_tambahan", info_tambahan);
                editor.putString("view_tanggal", view_tanggal);
                editor.putString("view_jam", view_jam);
                editor.putString("alamat_jemput", lokasi_jemput);
                editor.putString("lat_j", lat_j);
                editor.putString("log_j", log_j);
                editor.commit();

                Intent intent = new Intent(OrderNowEdit.this, EstimasiBerat.class);
                intent.putExtra("tgl_selesai", tanggalSelesai);
                intent.putExtra("tgl", KirimTanggal);
                intent.putExtra("jam", KirimJam);
                intent.putExtra("detail_lokasi", detail_lokasi);
                intent.putExtra("info_tambahan", info_tambahan);
                intent.putExtra("view_tanggal", view_tanggal);
                intent.putExtra("view_jam", view_jam);
                intent.putExtra("alamat_jemput", lokasi_jemput);
                intent.putExtra("lat_j", lat_j);
                intent.putExtra("log_j", log_j);
                startActivity(intent);
            }
        });





        lokasianda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(OrderNowEdit.this);
                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    String strLatitude =  String.valueOf(latitude);
                    String strLogitude =  String.valueOf(longitude);

                    Intent i = new Intent(OrderNowEdit.this, PickerPlace.class);
                    i.putExtra( "lat_real" ,  strLatitude );
                    i.putExtra( "log_real",   strLogitude);
                    //i.putExtra( "lat_real" ,  "3.567208" );
                    //i.putExtra( "log_real",   "98.654804");
                    startActivity(i);

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }

    //jika back button di klik
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Dashboard.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }
    //jika back button di klik


    public void showTime(int hour, int min) {
        String kirim_jam = "";
        String kirim_menit ="";


        String jam = String.valueOf(hour);
        int karakter_jam = hitungkarakter(jam);
        if(karakter_jam == 1){
            kirim_jam = "0"+jam;
        }else{
            kirim_jam = jam;
        }

        String menit = String.valueOf(min);
        int karakter_menit = hitungkarakter(menit);
        if(karakter_menit == 1){
            kirim_menit = "0"+menit;
        }else{
            kirim_menit = menit;
        }



        btnJam.setText(new StringBuilder().append(kirim_jam).append(" : ").append(kirim_menit));

        KirimJam = kirim_jam+":"+kirim_menit+":00";
    }

//---------------------------------------- FORMAT TANGGAL ---------------------------------------------//

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        String kirim_bulan = "";
        String kirim_hari  ="";


        String bulan = String.valueOf(month);
        int karakter_bulan = hitungkarakter(bulan);
        if(karakter_bulan == 1){
            kirim_bulan = "0"+bulan;
        }else{
            kirim_bulan = bulan;
        }

        String hari = String.valueOf(day);
        int karakter_hari = hitungkarakter(hari);
        if(karakter_hari == 1){
            kirim_hari = "0"+hari;
        }else{
            kirim_hari = hari;
        }


        String untildate = String.valueOf(new StringBuilder().append(year).append("-")
                .append(kirim_bulan).append("-").append(kirim_hari));

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cll = Calendar.getInstance();
        try {
            cll.setTime( dateFormat.parse(untildate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cll.add( Calendar.DATE, 1 );
        String convertedDate=dateFormat.format(cll.getTime());

        Log.e("KODE TANGGAL + 1","I shouldn't be " + convertedDate );

        tanggalSelesai = convertedDate;




        btnTanggal.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));


        KirimTanggal = String.valueOf(new StringBuilder().append(year).append("-")
                .append(kirim_bulan).append("-").append(kirim_hari));
    }

    //---------------------------------------- FORMAT TANGGAL ---------------------------------------------//

    public int hitungkarakter(String s){
        int counter = 0;
        for( int i=0; i<s.length(); i++ ) {
            if( s.charAt(i) == '$' ) {
                counter++;
            }
        }
        return counter;
    }



    private int PosisiSpinner(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }



}