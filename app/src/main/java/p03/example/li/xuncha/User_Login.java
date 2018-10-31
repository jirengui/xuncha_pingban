package p03.example.li.xuncha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jgll on 2017/10/31.
 */

public class User_Login extends AppCompatActivity {

    final int[] userId = {-1};
    String userName = null;
    String userZhiwu = null;

    @SuppressLint("HandlerLeak") Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    userId[0] = msg.arg1;
                    Bundle bundle = msg.getData();
                    userName = bundle.getString("userName");
                    userZhiwu = bundle.getString("userZhiwu");
                    break;
            }
            if (userId[0] == -1) {
                Toast toast;
                toast = Toast.makeText(User_Login.this,"密码错误",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else if (userId[0] == -2){
                Toast toast;
                toast = Toast.makeText(User_Login.this,"没有此账号",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Toast.makeText(User_Login.this,"登陆成功",Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent();
                mIntent.putExtra("userId", String.valueOf(userId[0]));
                mIntent.putExtra("userName", userName);
                mIntent.putExtra("userZhiwu", userZhiwu);
                // 设置结果，并进行传送
                User_Login.this.setResult(1, mIntent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_login);
        final EditText edUserNname;
        final EditText edUserPassword;
        edUserNname = (EditText) findViewById(R.id.edit_username);
        edUserPassword = (EditText) findViewById(R.id.edit_pwd);

        final Intent it = getIntent();
        final String userName = it.getStringExtra("UserName");
        edUserNname.setText(userName);
        //按钮透明,点击事件
        Button bt = (Button) findViewById(R.id.button_login);

        bt.setAlpha((float) 0.9);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName1 = edUserNname.getText().toString();
                final String userPassword = edUserPassword.getText().toString();
                if (userName1.equals("")){
                    Toast toast;
                    toast = Toast.makeText(User_Login.this,"请输入账号",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (userPassword.equals("")){
                    Toast toast;
                    toast = Toast.makeText(User_Login.this,"请输入密码",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                LianJieWangLuo lianJieWangLuo = new LianJieWangLuo(userName1,userPassword);
                lianJieWangLuo.start();
                }

            }
        });
        //忘记密码
        TextView textViewv = (TextView) findViewById(R.id.textView3);
        textViewv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mintent = new Intent();
                mintent.setClass(User_Login.this, User_yanzheng.class);
                startActivity(mintent);
            }
        });

    }
    class LianJieWangLuo extends Thread{
        String userName;
        String userPassword;
        public LianJieWangLuo(String userName, String userPassword)
        {
            this.userName = userName;
            this.userPassword = userPassword;
        }
        @Override
        public void run() {
                SocketFuWuQi socketFuWuQi = new SocketFuWuQi();
                try {
                    String user = "-1";
                    user = socketFuWuQi.zhangHaoMiMaDengLu(userName, userPassword);
                    String[] userId = user.split(",");
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = Integer.valueOf(userId[0]);
                    Bundle bundle  = new Bundle();
                    bundle.putString("userName", userId[1]);
                    bundle.putString("userZhiwu", userId[2]);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
