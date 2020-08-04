package com.dustproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

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

public class MainActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "211.206.115.62:81"; // 192.168.0.3 / 192.168.1.30 / 10.0.2.2 / 172.18.54.145 / 118.219.45.172
    private static String TAG = "PHP";

    private RecyclerView dRecyclerView;
    private RecyclerView dustRecyclerView;
    private ArrayList<DustData> dArrayList;
    private ArrayList<DustData> dustArrayList;
    private DustsAdapter dAdapter;
    private DustItemAdapter dustAdapter;
    private String dJsonString;
    private ImageView charView;
    private ImageView imageColor;
    private ImageView imageChar;
    private TextView mainFigure;
    private ConstraintLayout mainImage;
    private TextView colorText;
    private SwipeRefreshLayout swipeLayout;
    final Handler mHandler = new Handler();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // note view 보여주기 위한 코드
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dRecyclerView = findViewById(R.id.listView_main_list);
        dustRecyclerView = findViewById(R.id.dustView);
        mainImage = findViewById(R.id.mainImage);
        charView = findViewById(R.id.charView);
        mainFigure = findViewById(R.id.mainFigure);
        imageColor = findViewById(R.id.imageColor);
        imageChar = findViewById(R.id.imageChar);
        colorText = findViewById(R.id.colorText);
        swipeLayout = findViewById(R.id.swipeLayout);
        // note LayoutManager() 함수로 생성자 추가
        dRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dustRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // note ArrayList<>() 함수 추가
        dArrayList = new ArrayList<>();
        dustArrayList = new ArrayList<>();
        // note IricosAdapter() 함수에 ArrayList 생성
        dAdapter = new DustsAdapter(this, dArrayList);
        dustAdapter = new DustItemAdapter(this, dustArrayList);
        // note dRecyclerView dAdapter 변수 불러오는 코드
        dRecyclerView.setAdapter(dAdapter);
        dustRecyclerView.setAdapter(dustAdapter);
        GetData data = new GetData();
        data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(false);
                dArrayList.clear(); // dAdapter.notifyDataSetChanged();
                dustArrayList.clear(); // dustAdapter.notifyDataSetChanged();
                GetData data = new GetData();
                data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");
            }
        });
        mHandler.postDelayed(new Runnable()  {
            public void run() {
                if(true){
                    mHandler.postDelayed(this,15000);
                    dArrayList.clear(); // dAdapter.notifyDataSetChanged();
                    dustArrayList.clear(); // dustAdapter.notifyDataSetChanged();
                    GetData data = new GetData();
                    data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");
                }
            }
        },100);
/*        Button button_all = (Button)findViewById(R.id.button_main_all);
        button_all.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                dArrayList.clear(); // dAdapter.notifyDataSetChanged();
                dustArrayList.clear(); // dustAdapter.notifyDataSetChanged();
                GetData data = new GetData();
                data.execute("http://"+IP_ADDRESS+"/GetJSON.php","");
            }
        });*/
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, " 백그라운드 작업이 완료 된 후 결과값을 얻었어요. - " + result);

            if(result == null){
                Log.d(TAG, "");
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
    private void showResult(){
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
            for (int i = 0; i < jsonArray.length();i++){
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
                DustData dustData2 = new DustData();

                dustData.setHumi(humi);
                dustData.setTemp(temp);
                dustData.setDust(dust);
                dustData.setDustmin(dustmin);
                dustData.setTvoc(tvoc);
                dustData.setCo2(co2);
                dustData.setCo(co);
                dustData.setCh2o(ch2o);
                dustData.setPm1(pm1);

                dustData1.setDustitem(dustItem);
                dustData2.setDustitem(dustItem);

                dArrayList.add(dustData);
                dAdapter.notifyDataSetChanged();
                dustArrayList.add(dustData1);
                dustAdapter.notifyDataSetChanged();
                if (dustItem <= 30){
                    mainFigure.setText("안전");
                    mainFigure.setBackgroundColor(Color.parseColor("#03F30D"));
                    imageColor.setImageResource(R.drawable.image_color1);
                    imageChar.setImageResource(R.drawable.ico_dust1);
                    mainImage.setBackgroundResource(R.drawable.bg_step01);
                    colorText.setText("좋음");
                    Glide.with(this).load(R.raw.char_nstep01).into(charView);
                } else if (dustItem > 30 && dustItem <= 80){
                    mainFigure.setText("안전");
                    mainFigure.setBackgroundColor(Color.parseColor("#288CFF"));
                    imageColor.setImageResource(R.drawable.image_color2);
                    imageChar.setImageResource(R.drawable.ico_dust2);
                    mainImage.setBackgroundResource(R.drawable.bg_step02);
                    colorText.setText("보통");
                    Glide.with(this).load(R.raw.char_step02).into(charView);
                } else if (dustItem > 80 && dustItem <= 150){
                    mainFigure.setText("경고");
                    mainFigure.setBackgroundColor(Color.parseColor("#D2691E"));
                    imageColor.setImageResource(R.drawable.image_color3);
                    imageChar.setImageResource(R.drawable.ico_dust3);
                    mainImage.setBackgroundResource(R.drawable.bg_step03);
                    colorText.setText("나쁨");
                    Glide.with(this).load(R.raw.char_step03).into(charView);
                } else if (dustItem > 150) {
                    mainFigure.setText("위험");
                    mainFigure.setBackgroundColor(Color.parseColor("#EB0000"));
                    imageColor.setImageResource(R.drawable.image_color4);
                    imageChar.setImageResource(R.drawable.ico_dust4);
                    mainImage.setBackgroundResource(R.drawable.bg_step04);
                    colorText.setText("극혐");
                    Glide.with(this).load(R.raw.char_step04).into(charView);
                }
            }
        } catch (JSONException e){
            Log.d(TAG,"showResult :",e);
        }
    }
}