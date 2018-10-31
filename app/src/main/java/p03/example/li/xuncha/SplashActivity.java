package p03.example.li.xuncha;

/**
 * Created by li on 2017/10/20.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {
    private TextView tv;
    private MyCountDownTimer mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        tv = (TextView) findViewById(R.id.splash_tv);
        mc = new MyCountDownTimer(2000, 1000);
        mc.start();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent it = new Intent();
                it.setClass(SplashActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                it.putExtras(bundle);
                startActivity(it);
                finish();
            }
        }, 1000 * 3);
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         *
         * @param millisInFuture
         * 表示以毫秒为单位 倒计时的总数
         *
         * 例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         * 表示 间隔 多少微秒 调用一次 onTick 方法
         *
         * 例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        public void onFinish() {
            tv.setText("正在跳转");
        }
        public void onTick(long millisUntilFinished) {
            tv.setText("倒计时(" + millisUntilFinished / 1000 + ")");
        }
    }
}

