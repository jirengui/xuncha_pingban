package p03.example.li.xuncha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInformationActivity extends AppCompatActivity {
    CircleImageView civ ;
    TextView tv_userName, tv_userZhiwu;
//    TextView tv_jianshao;
    private Uri originalUri;
    private String uri = null;
    SocketFuWuQi socketFuWuQi;
    private String userId;
    private String userName = null, userZhiwu = null,userJieShao = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_user_information);
        Intent intent = getIntent();
        userId = String.valueOf(intent.getIntExtra("userId", -1));
        userName = intent.getStringExtra("userName");
        userZhiwu = intent.getStringExtra("userZhiwu");
        System.out.println("UserId: "+  userId);
        tv_userName = (TextView) findViewById(R.id.user_userName);
        tv_userName.setText(userName);
        tv_userZhiwu = findViewById(R.id.zhiwu);
        tv_userZhiwu.setText(userZhiwu);
//        tv_jianshao = (TextView) findViewById(R.id.user_jianJie);
        LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.user_layout);
        SharedPreferences pref = getSharedPreferences( "name", MODE_PRIVATE);
        uri = pref.getString(userId + "uri", null);
        civ = relativeLayout.findViewById(R.id.user_touXiang);
        if (uri != null){
            civ.setImageURI(Uri.parse(uri));
        }
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });
        userName = tv_userName.getText().toString().trim();
//        userJieShao = tv_jianshao.getText().toString().trim();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && uri != null){
            Intent mIntent = new Intent();
            mIntent.putExtra("uri", uri);
            UserInformationActivity.this.setResult(3, mIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, 2);
    }

    //从相册获取图片返回,上传至服务器
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("TAG->onresult", "ActivityResult resultCode error");
            return;
        }
        // 此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == 2) {
            originalUri = data.getData();        //获得图片的uri
            uri = getImagePathFromURI(originalUri);
            civ.setImageURI(Uri.parse(uri));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        socketFuWuQi = new SocketFuWuQi();
                        socketFuWuQi.shangChuanTouXiang(uri, userId, "李红军",null, userZhiwu);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    //正确的图片地址
    public String getImagePathFromURI(Uri uri) {
        Cursor cursor = UserInformationActivity.this.getContentResolver().query(uri, null, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = UserInformationActivity.this.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }
}
