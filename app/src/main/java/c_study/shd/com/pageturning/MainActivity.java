package c_study.shd.com.pageturning;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PagerTurningView  bookPageView=findViewById(R.id.bookPageView);
        bookPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(event.getY() < bookPageView.getViewHeight()/2){//从上半部分翻页
                            bookPageView.setTouchPoint(event.getX(),event.getY(),bookPageView.STYLE_TOP_RIGHT);
                        }else if(event.getY() >= bookPageView.getViewHeight()/2) {//从下半部分翻页
                            bookPageView.setTouchPoint(event.getX(),event.getY(),bookPageView.STYLE_LOWER_RIGHT);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        bookPageView.setTouchPoint(event.getX(),event.getY(),"");
                        break;
                    case MotionEvent.ACTION_UP:
                        bookPageView.setDefaultPath();//回到默认状态
                        break;
                }
                return false;
            }
        });

    }
}
