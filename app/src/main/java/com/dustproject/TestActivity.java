package com.dustproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TestActivity extends AppCompatActivity {
    private ConstraintLayout mainImage;
    private ImageView homePage;
    private SwipeRefreshLayout swipeLayout;
    private Button toolbar_btn,btn_01,btn_02;
    private Animation translateLeftAnim,translateRightAnim;
    private LinearLayout slidingPanel;
    boolean isPageOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mainImage = findViewById(R.id.mainImage);
        swipeLayout = findViewById(R.id.swipeLayout);toolbar_btn = findViewById(R.id.toolbar_btn);
        slidingPanel = findViewById(R.id.slidingPanel);homePage = findViewById(R.id.home_page);
        btn_01 = findViewById(R.id.btn_01);btn_02 = findViewById(R.id.btn_02);
        translateLeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_right);
        final SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);translateRightAnim.setAnimationListener(animListener);

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
            public void onClick(View v) {
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

        Toast.makeText(this,"어서오시오",Toast.LENGTH_LONG).show();
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
}
