package com.dustproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DustActivity extends AppCompatActivity {
    Handler handler = new Handler(); // Runnable 을 실행 하기 위한 class
    Runnable runnable; // 병렬 실행이 가능한 Thread 를 만들어주는 class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        /* android app 을 띄우는 공간인 Window 의 속성을 변경하여 작업표시줄 등 시스템 UI를
        숨기고 전체화면으로 표시하는 코드*/
        // 일반 몰입 모드 활성화
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Runnable 실행되면 ListActivity::class.java 로 이동
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        // Handler 생성 후 runnable 을 2초 후에 실행
        handler.postDelayed(runnable, 2000);
    }
    // Activity pause 상태일 때는 runnable 도 중단
    @Override
    protected void onPause() {
        super.onPause();
        if(null != handler){
            handler.removeCallbacks(runnable);
        }
    }
}