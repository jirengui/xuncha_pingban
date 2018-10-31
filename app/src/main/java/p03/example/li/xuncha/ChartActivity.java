package p03.example.li.xuncha;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends Activity implements View.OnClickListener {
    private String[] jilu = new String[200];
    private String userId;
    private List<ZhiNengBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        initView();
    }
    public void initView(){
        findViewById(R.id.chart_laoshi).setOnClickListener(this);
        findViewById(R.id.chart_banji).setOnClickListener(this);
        findViewById(R.id.chart_xuncha).setOnClickListener(this);
        findViewById(R.id.chart_xuncharen).setOnClickListener(this);
        Intent intent = getIntent();
        jilu = intent.getStringArrayExtra("jilu");
        userId = intent.getStringExtra("userId");
        int j = 0;
        for (int i = 0; !jilu[i].equals("null"); i++){
            String[] string = jilu[i].split(",");
            ZhiNengBean zhiNengBean = new ZhiNengBean(string[0], string[1], string[2], string[3], string[4], string[5], string[6]);
            if (string.length >= 8) {
                if (string[7] != null && !string[7].equals("null")) {
                    zhiNengBean.setPicture(string[7]);
                }else {
                    zhiNengBean.setPicture("null");
                }
            }else {
                zhiNengBean.setPicture("null");
            }

            list.add(zhiNengBean);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_laoshi:
                //TODO implement
                Intent intent = new Intent();
                intent.putExtra("list", (Serializable)list);
                //1为老师异常
                intent.putExtra("name", 1);
                intent.setClass(ChartActivity.this, ChartItemActivity.class);
                startActivity(intent);
                break;
            case R.id.chart_banji:
                //TODO implement
                Intent mintent = new Intent();
                mintent.putExtra("list", (Serializable)list);
                //2为班级异常
                mintent.putExtra("name", 2);
                mintent.setClass(ChartActivity.this, ChartItemActivity.class);
                startActivity(mintent);
                break;
            case R.id.chart_xuncha:
                //TODO implement
                break;
            case R.id.chart_xuncharen:
                //TODO implement
                break;
        }
    }
}
