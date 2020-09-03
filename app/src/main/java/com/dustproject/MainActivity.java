package com.dustproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.dustproject.Dusts.Ch2oItemAdapter;
import com.dustproject.Dusts.Co2ItemAdapter;
import com.dustproject.Dusts.CoItemAdapter;
import com.dustproject.Dusts.DustAdapter;
import com.dustproject.Dusts.DustItemAdapter;
import com.dustproject.Dusts.DustMinItemAdapter;
import com.dustproject.Dusts.DustsAdapter;
import com.dustproject.Dusts.HumiItemAdapter;
import com.dustproject.Dusts.Pm1ItemAdapter;
import com.dustproject.Dusts.TempItemAdapter;
import com.dustproject.Dusts.TvocItemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final Handler dHandler = new Handler();

    private static String IP_ADDRESS = "211.206.115.62:81"; // 192.168.0.3 / 192.168.1.30 / 10.0.2.2 / 172.18.54.145 / 118.219.45.172
    private static String TAG = "PHP";
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2020;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    private RecyclerView dRecyclerView;
    private RecyclerView humiRV,tempRV,tvocRV,dustRV,dustminRV,pm1RV,coRV,ch2oRV,co2RV,dustItemRV;
    private ArrayList<DustData> dAL,humiAL,tempAL,tvocAL,dustAL,dustminAL,pm1AL,coAL,ch2oAL,co2AL,dustItemAL;
    private DustsAdapter dAT;
    private HumiItemAdapter humiAT;
    private TempItemAdapter tempAT;
    private TvocItemAdapter tvocAT;
    private DustAdapter dustAT;
    private DustMinItemAdapter dustminAT;
    private Pm1ItemAdapter pm1AT;
    private CoItemAdapter coAT;
    private Ch2oItemAdapter ch2oAT;
    private Co2ItemAdapter co2AT;
    private DustItemAdapter dustItemAT;
    private String dJsonString;
    private ImageView charView,imageColor,imageChar;
    private TextView mainFigure,colorText;
    private ConstraintLayout mainImage;
    private SwipeRefreshLayout swipeLayout;
    private Button mainBtn;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // note view 보여주기 위한 코드
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dRecyclerView = findViewById(R.id.listView_main_list);
        humiRV = findViewById(R.id.humi);tempRV = findViewById(R.id.temp);tvocRV = findViewById(R.id.tvoc);dustRV = findViewById(R.id.dust);
        dustminRV = findViewById(R.id.dustmin);pm1RV = findViewById(R.id.pm1);coRV = findViewById(R.id.co);ch2oRV = findViewById(R.id.ch2o);
        co2RV = findViewById(R.id.co2);dustItemRV = findViewById(R.id.dustView);mainImage = findViewById(R.id.mainImage);charView = findViewById(R.id.charView);
        mainFigure = findViewById(R.id.mainFigure);imageColor = findViewById(R.id.imageColor);imageChar = findViewById(R.id.imageChar);
        colorText = findViewById(R.id.colorText);swipeLayout = findViewById(R.id.swipeLayout);
        // note LayoutManager() 함수로 생성자 추가
        dRecyclerView.setLayoutManager(new LinearLayoutManager(this));humiRV.setLayoutManager(new LinearLayoutManager(this));
        tempRV.setLayoutManager(new LinearLayoutManager(this));tvocRV.setLayoutManager(new LinearLayoutManager(this));
        dustRV.setLayoutManager(new LinearLayoutManager(this));dustminRV.setLayoutManager(new LinearLayoutManager(this));
        pm1RV.setLayoutManager(new LinearLayoutManager(this));coRV.setLayoutManager(new LinearLayoutManager(this));
        ch2oRV.setLayoutManager(new LinearLayoutManager(this));co2RV.setLayoutManager(new LinearLayoutManager(this));
        dustItemRV.setLayoutManager(new LinearLayoutManager(this));
        // note ArrayList<>() 함수 추가
        dAL = new ArrayList<>();humiAL = new ArrayList<>();tempAL = new ArrayList<>();tvocAL = new ArrayList<>();dustAL = new ArrayList<>();
        dustminAL = new ArrayList<>();pm1AL = new ArrayList<>();coAL = new ArrayList<>();ch2oAL = new ArrayList<>();co2AL = new ArrayList<>();
        dustItemAL = new ArrayList<>();
        // note IricosAdapter() 함수에 ArrayList 생성
        dAT = new DustsAdapter(this, dAL);humiAT = new HumiItemAdapter(this, humiAL);tempAT = new TempItemAdapter(this, tempAL);
        tvocAT = new TvocItemAdapter(this, tvocAL);dustAT = new DustAdapter(this, dustAL);dustminAT = new DustMinItemAdapter(this, dustminAL);
        pm1AT = new Pm1ItemAdapter(this, pm1AL);coAT = new CoItemAdapter(this, coAL);ch2oAT = new Ch2oItemAdapter(this, ch2oAL);
        co2AT = new Co2ItemAdapter(this, coAL);dustItemAT = new DustItemAdapter(this, dustItemAL);
        // note dRecyclerView dAT 변수 불러오는 코드
        dRecyclerView.setAdapter(dAT);humiRV.setAdapter(humiAT);tempRV.setAdapter(tempAT);tvocRV.setAdapter(tvocAT);dustRV.setAdapter(dustAT);dustminRV.setAdapter(dustminAT);
        pm1RV.setAdapter(pm1AT);coRV.setAdapter(coAT);ch2oRV.setAdapter(ch2oAT);co2RV.setAdapter(co2AT);dustItemRV.setAdapter(dustItemAT);
        final GetData data = new GetData();
        data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else { checkRunTimePermission(); }

        gpsTracker = new GpsTracker(MainActivity.this);
        final double latitude = gpsTracker.getLatitude();
        final double longitude = gpsTracker.getLongitude();
        final TextView textView_address = findViewById(R.id.textGeoCoder);
        String address = getCurrentAddress(latitude, longitude);
        textView_address.setText(address);

        mainBtn = findViewById(R.id.mainSet);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                dAL.clear();humiAL.clear();tempAL.clear();tvocAL.clear();dustAL.clear();dustminAL.clear();
                pm1AL.clear();coAL.clear();ch2oAL.clear();co2AL.clear();dustItemAL.clear();
                GetData data = new GetData();
                data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");
/*                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();*/
                String address = getCurrentAddress(latitude, longitude);
                textView_address.setText(address);
            }
        });
        dHandler.postDelayed(new Runnable()  {
            public void run() {
                if(true){
                    dHandler.postDelayed(this,15000);
                    dAL.clear();humiAL.clear();tempAL.clear();tvocAL.clear();dustAL.clear();dustminAL.clear();
                    pm1AL.clear();coAL.clear();ch2oAL.clear();co2AL.clear();dustItemAL.clear();
                    GetData data = new GetData();
                    data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");
/*                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();*/
                    String address = getCurrentAddress(latitude, longitude);
                    textView_address.setText(address);
                }
            }
        },100);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        System.exit(1);
    }
    private class GetData extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait",null,true,true);
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, " 백그라운드 작업이 완료 된 후 결과값을 얻었어요. - " + result);

            if(result == null){
                Log.d(TAG, "edgsfgha r reg ARE G");
            } else {
                dJsonString = result;
                showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            // note 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // note POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = params[0];
            // note HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // note 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // note 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            // note String postParameters = "name=" + name + "&country=" + country;
            String postParameters = params[1];
            try {
                // note 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // note 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); // note 초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); // note 초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); // note 청 방식을 POST 로 합니다.
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                // note 전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                // note 3. 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // note 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    // note 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }
                // note 4. StringBuilder 를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                // note 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showResult(){
        final Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        final ImageView image = new ImageView(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(image);
        final AlertDialog dialog = builder.create();
        NotificationCompat.Builder notCB = null;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ch1","ch01",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            notCB = new NotificationCompat.Builder(this, "ch1");
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);
        String TAG_JSON = "dustData";
        String TAG_HUMI = "humi";
        String TAG_TEMP = "temp";
        String TAG_DUST = "dust";
        String TAG_DUSTMIN = "dustmin";
        String TAG_TVOC = "tvoc";
        String TAG_CO2 = "co2";
        String TAG_CO = "co";
        String TAG_ch2o = "ch2o";
        String TAG_PM1 = "pm1";
        try {
            JSONObject jsonObject = new JSONObject(dJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length();i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String humi = item.getString(TAG_HUMI);
                String temp = item.getString(TAG_TEMP);
                String dust = item.getString(TAG_DUST);
                String dustmin = item.getString(TAG_DUSTMIN);
                String tvoc = item.getString(TAG_TVOC);
                String co2 = item.getString(TAG_CO2);
                String co = item.getString(TAG_CO);
                String ch2o = item.getString(TAG_ch2o);
                String pm1 = item.getString(TAG_PM1);

                int dustItem = item.getInt(TAG_DUST);

                DustData dustData = new DustData();
                DustData dustData1 = new DustData();

                dustData.setHumi(humi);
                dustData.setTemp(temp);
                dustData.setDust(dust);
                dustData.setDustmin(dustmin);
                dustData.setTvoc(tvoc);
                dustData.setCo2(co2);
                dustData.setCo(co);
                dustData.setCh2o(ch2o);
                dustData.setPm1(pm1);

                dustData1.setDustItem(dustItem);

                dAL.add(dustData);
                humiAL.add(dustData);
                tempAL.add(dustData);
                tvocAL.add(dustData);
                dustAL.add(dustData);
                dustminAL.add(dustData);
                pm1AL.add(dustData);
                coAL.add(dustData);
                ch2oAL.add(dustData);
                co2AL.add(dustData);
                dustItemAL.add(dustData1);
                dAT.notifyDataSetChanged();
                humiAT.notifyDataSetChanged();
                tempAT.notifyDataSetChanged();
                tvocAT.notifyDataSetChanged();
                dustAT.notifyDataSetChanged();
                dustminAT.notifyDataSetChanged();
                pm1AT.notifyDataSetChanged();
                coAT.notifyDataSetChanged();
                ch2oAT.notifyDataSetChanged();
                co2AT.notifyDataSetChanged();
                dustItemAT.notifyDataSetChanged();

                if (dustItem <= 30) {
                    mainFigure.setText("안전");
                    mainFigure.setBackgroundColor(Color.parseColor("#03F30D"));
                    imageColor.setImageResource(R.drawable.image_color1);
                    imageChar.setImageResource(R.drawable.ico_dust1);
                    mainImage.setBackgroundResource(R.drawable.bg_step01);
                    colorText.setText("좋음");
                    colorText.setTextSize(20);
                    Glide.with(this).load(R.raw.char_step01).into(charView);
                } else if (dustItem > 30 && dustItem <= 80){
                    mainFigure.setText("안전");
                    mainFigure.setBackgroundColor(Color.parseColor("#288CFF"));
                    imageColor.setImageResource(R.drawable.image_color2);
                    imageChar.setImageResource(R.drawable.ico_dust2);
                    mainImage.setBackgroundResource(R.drawable.bg_step02);
                    colorText.setText("보통");
                    colorText.setTextSize(20);
                    Glide.with(this).load(R.raw.char_step02).into(charView);
                } else if (dustItem > 80 && dustItem <= 150){
                    mainFigure.setText("경고");
                    mainFigure.setBackgroundColor(Color.parseColor("#D2691E"));
                    imageColor.setImageResource(R.drawable.image_color3);
                    imageChar.setImageResource(R.drawable.ico_dust3);
                    mainImage.setBackgroundResource(R.drawable.bg_step03);
                    colorText.setText("나쁨");
                    colorText.setTextSize(20);
                    Glide.with(this).load(R.raw.char_step03).into(charView);
                    notCB.setContentTitle("!!!!!!!! 경고 !!!!!!!!!");
                    notCB.setContentText("나쁜 공기가 있어요! 마스크 꼭 착용하세요! ");
                    notCB.setSubText("　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
                    notCB.setSmallIcon(R.drawable.ico_dust3);
                    notCB.setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_dust3)));
                    notCB.setColor(Color.TRANSPARENT);
                    notCB.setContentIntent(pendingIntent);
                    notCB.setAutoCancel(true);
                    Notification notification = notCB.build();
                    manager.notify(1,notification);
                    vib.vibrate(new long[]{500,500},-1);
                    image.setImageResource(R.drawable.alert1);
                    dialog.show();
                } else if (dustItem > 150) {
                    mainFigure.setText("위험");
                    mainFigure.setBackgroundColor(Color.parseColor("#EB0000"));
                    imageColor.setImageResource(R.drawable.image_color4);
                    imageChar.setImageResource(R.drawable.ico_dust4);
                    mainImage.setBackgroundResource(R.drawable.bg_step04);
                    colorText.setText("매우\n나쁨");
                    colorText.setTextSize(15);
                    Glide.with(this).load(R.raw.char_step04).into(charView);
                    notCB.setContentTitle("!!!!!!!! 위험 !!!!!!!!!");
                    notCB.setContentText("밖에 외출을 자제해주세요! 비상사태!!! ");
                    notCB.setSubText("　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
                    notCB.setSmallIcon(R.drawable.ico_dust4);
                    notCB.setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_dust4)));
                    notCB.setColor(Color.TRANSPARENT);
                    notCB.setContentIntent(pendingIntent);
                    notCB.setAutoCancel(true);
                    Notification notification = notCB.build();
                    manager.notify(1,notification);
                    vib.vibrate(new long[]{500,500},-1);
                    image.setImageResource(R.drawable.alert);
                    dialog.show();
                }
            }
        } catch (JSONException e){
            Log.d(TAG,"showResult :",e);
        }
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if ( check_result ) {
                //위치 값을 가져올 수 있음;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }
    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견 / 허용시 App 종료 후 재실행 바랍니다", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        // return /*address.getAdminArea()+" "+address.getFeatureName()+" "+address.getThoroughfare();*/
        // return address.getAddressLine(0);
        return address.getSubLocality()+" "+address.getThoroughfare();
    }
    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
            break;
        }
    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}