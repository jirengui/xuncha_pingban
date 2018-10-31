package p03.example.li.xuncha;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jgll on 2017/10/31.
 */

public class User_yanzheng extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wangjimima);

        //登录按钮
        Button bt = (Button) findViewById(R.id.button_login);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //返回
        TextView tv = (TextView) findViewById(R.id.textView4);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
