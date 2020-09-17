package com.dustproject;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity {
    final Handler dHandler = new Handler();
    final Intent Alarm_intent = new Intent();

    private static String IP_ADDRESS = "211.206.115.62:81"; // 192.168.0.3 / 192.168.1.30 / 10.0.2.2 / 172.18.54.145 / 118.219.45.172
    private static String TAG = "PHP";
    private RecyclerView humiRV,tempRV,tvocRV,dustminRV,pm1RV,coRV,ch2oRV,co2RV,dustItemRV,dustRV;
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
    private ImageView charView,imageText,homePage;
    private TextView mainFigure;
    private ConstraintLayout mainImage;
    private SwipeRefreshLayout swipeLayout;
    private Button toolbar_btn,btn_01,btn_02;
    private Animation translateLeftAnim,translateRightAnim;
    private LinearLayout slidingPanel;
    private Switch alarmSwitch;
    boolean isPageOpen = false;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // note view 보여주기 위한 코드
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        humiRV = findViewById(R.id.humi);tempRV = findViewById(R.id.temp);tvocRV = findViewById(R.id.tvoc);/*dustRV = findViewById(R.id.dust);*/
        dustminRV = findViewById(R.id.dustmin);pm1RV = findViewById(R.id.pm1);coRV = findViewById(R.id.co);ch2oRV = findViewById(R.id.ch2o);
        co2RV = findViewById(R.id.co2);dustItemRV = findViewById(R.id.dustView);mainImage = findViewById(R.id.mainImage);charView = findViewById(R.id.charView);
        mainFigure = findViewById(R.id.mainFigure);swipeLayout = findViewById(R.id.swipeLayout);imageText = findViewById(R.id.text_image2);
        toolbar_btn = findViewById(R.id.toolbar_btn);slidingPanel = findViewById(R.id.slidingPanel);homePage = findViewById(R.id.home_page);
        btn_01 = findViewById(R.id.btn_01);btn_02 = findViewById(R.id.btn_02);
        alarmSwitch = findViewById(R.id.switch1);
        translateLeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_right);
        final SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);translateRightAnim.setAnimationListener(animListener);
        // note LayoutManager() 함수로 생성자 추가
        humiRV.setLayoutManager(new LinearLayoutManager(this));
        tempRV.setLayoutManager(new LinearLayoutManager(this));tvocRV.setLayoutManager(new LinearLayoutManager(this));
        /*dustRV.setLayoutManager(new LinearLayoutManager(this));*/dustminRV.setLayoutManager(new LinearLayoutManager(this));
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
        humiRV.setAdapter(humiAT);tempRV.setAdapter(tempAT);tvocRV.setAdapter(tvocAT);/*dustRV.setAdapter(dustAT)*/;dustminRV.setAdapter(dustminAT);
        pm1RV.setAdapter(pm1AT);coRV.setAdapter(coAT);ch2oRV.setAdapter(ch2oAT);co2RV.setAdapter(co2AT);dustItemRV.setAdapter(dustItemAT);
        data_clear();
        toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    slidingPanel.startAnimation(translateRightAnim);
                    slidingPanel.setVisibility(View.GONE);
                } else {
                    slidingPanel.startAnimation(translateLeftAnim);
                    slidingPanel.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");
                String Test_Message = "미세리코 어플 다운링크 :\n http://irico.co.kr ";
                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message); Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
            }
        });
        btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Information_intent = new Intent(getApplicationContext(),TestActivity.class);
                startActivity(Information_intent);
            }
        });
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://irico.co.kr";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                data_clear();
            }
        });
        dHandler.postDelayed(new Runnable()  {
            public void run() {
                dHandler.postDelayed(this,15000);
                data_clear();
            }
        },100);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar targetCal = Calendar.getInstance();
        final PendingIntent sender = PendingIntent.getBroadcast(this,1,Alarm_intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked){
                            alarmManager.cancel(sender);
                            sender.cancel();
                            Toast.makeText(MainActivity.this, "알림감추기", Toast.LENGTH_SHORT).show();
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),sender);
                            Toast.makeText(MainActivity.this, "알림띄우기", Toast.LENGTH_SHORT).show();
                            // offAlarm(null);
                        }
                    }
                });
            }
        });
    }
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }
        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen){
                slidingPanel.setVisibility(View.INVISIBLE);
                toolbar_btn.setBackgroundResource(R.drawable.toolbar_button);
                toolbar_btn.setHeight(50);
                isPageOpen = false;
            } else {
                toolbar_btn.setBackgroundResource(R.drawable.toolbar_button1);
                toolbar_btn.setHeight(1500);
                isPageOpen = true;
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
    public void data_clear(){
        dAL.clear();humiAL.clear();tempAL.clear();tvocAL.clear();dustAL.clear();dustminAL.clear();
        pm1AL.clear();coAL.clear();ch2oAL.clear();co2AL.clear();dustItemAL.clear();
        GetData data = new GetData();
        data.execute("http://" + IP_ADDRESS + "/GetJSON.php","");
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
                Log.d(TAG, "r reg ARE G");
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
    public void vib(){
        final Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(new long[]{500,500},-1);
        vib.cancel();
    }
    @SuppressLint({"WrongConstant", "RestrictedApi"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showResult(){
        ImageView image = new ImageView(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(image);
        final AlertDialog dialog = builder.create();
        NotificationCompat.Builder notCB;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        NotificationChannel channel = new NotificationChannel("ch1","ch01",NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        notCB = new NotificationCompat.Builder(this,"ch1");
        notCB.setSubText("　　　　　　　　　　　　　　　");
        notCB.setColor(Color.TRANSPARENT);
        notCB.setSmallIcon(R.drawable.icon);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,Alarm_intent,PendingIntent.FLAG_CANCEL_CURRENT);
        notCB.setContentIntent(pendingIntent);
        notCB.setAutoCancel(true);
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
        }, 15000);
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
                    mainFigure.setBackgroundResource(R.drawable.safety);mainImage.setBackgroundResource(R.drawable.background1);
                    charView.setBackgroundResource(R.drawable.good);imageText.setBackgroundResource(R.drawable.text1);
                    // Glide.with(this).load(R.raw.char_step01).into(charView);
                } else if (dustItem > 30 && dustItem <= 80){
                    mainFigure.setBackgroundResource(R.drawable.safety);mainImage.setBackgroundResource(R.drawable.background2);
                    charView.setBackgroundResource(R.drawable.ordinary);imageText.setBackgroundResource(R.drawable.text2);
                } else if (dustItem > 80 && dustItem <= 150){
                    mainFigure.setBackgroundResource(R.drawable.warning);mainImage.setBackgroundResource(R.drawable.background3);
                    charView.setBackgroundResource(R.drawable.bad);imageText.setBackgroundResource(R.drawable.text3);
                    notCB.setContentTitle("!!!!!!!! 경고 !!!!!!!!!");
                    notCB.setContentText("나쁜 공기가 있어요! 마스크 꼭 착용하세요! ");
                    notCB.setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_dust3)));
                    image.setImageResource(R.drawable.alert2);
                    Notification notification = notCB.build();
                    manager.notify(1,notification);vib();dialog.show();
                } else if (dustItem > 150) {
                    mainFigure.setBackgroundResource(R.drawable.danger);mainImage.setBackgroundResource(R.drawable.background4);
                    charView.setBackgroundResource(R.drawable.very_bad);imageText.setBackgroundResource(R.drawable.text4);
                    notCB.setContentTitle("!!!!!!!! 위험 !!!!!!!!!");
                    notCB.setContentText("밖에 외출을 자제해주세요! 비상사태!!! ");
                    notCB.setLargeIcon(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ico_dust4)));
                    image.setImageResource(R.drawable.alert1);
                    Notification notification = notCB.build();
                    manager.notify(2,notification);vib();dialog.show();
                }
            }
        } catch (JSONException e){
            Log.d(TAG,"showResult :",e);
        }
    }
}