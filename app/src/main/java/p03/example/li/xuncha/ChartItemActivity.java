package p03.example.li.xuncha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class ChartItemActivity extends Fragment implements MainActivity.upJiluListenr {

    private LineChart lineChart;
    private List<ZhiNengBean> list = new ArrayList<>();
    private YAxis yAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    //智能分析文本框
    private TextView tv;
    //老师的名字是KEY，异常多少次是值。
    private Map<String, Integer> map;
    // 获取老师的名字
    private List<String> xlist;
    //判断是哪个
    private int flag = 1;
    private String tite = null;
    private int g = 5;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).setJiluLinstenr(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_item, null);
        initView(view);
        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chart_item);
//        initView();
//    }

    public void initView(View view) {
        //1为老师异常，2为班级异常，3为巡查人的巡查次数
//        final Intent intent = getIntent();
//        list = (List<ZhiNengBean>) intent.getSerializableExtra("list");
//        flag = intent.getIntExtra("name", -1);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        tv = view.findViewById(R.id.tv_zhineng);
//        List<Entry> entryList = new ArrayList<>();
//        switch (flag) {
//            case -1:
//                Toast.makeText(getContext(), "传值错误", Toast.LENGTH_LONG).show();
////                finish();
//                break;
//            case 1:
//                teacher();
//                break;
//            case 2:
//                classr();
//                break;
//        }

//        for (int i = 0; i < xlist.size(); i++) {
//            entryList.add(new Entry(i, map.get(xlist.get(i))));
//        }
//        xAxis = lineChart.getXAxis();
//        yAxis = lineChart.getAxisLeft();
//        lineChart.setDrawGridBackground(false);
//        lineChart.setDrawBorders(false);//显示边界
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        lineDataSet = new LineDataSet(entryList, tite);
//        lineDataSet.setColor(Color.RED);
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        xAxis.setLabelCount(xlist.size() );//X轴显示几个值
//        //自定义增加X轴的值
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xlist.get((int) value % xlist.size());
//            }
//        });
//        xAxis.setGranularityEnabled(true);
//        xAxis.setGranularity(1f);
//        lineData = new LineData();
//        LimitLine yLimitLine = new LimitLine(1f, "异常警戒线");//限制线
//        yLimitLine.setLineColor(Color.RED);
//        yLimitLine.setTextColor(Color.RED);
//        yAxis.addLimitLine(yLimitLine);
//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setDrawAxisLine(true);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTextColor(Color.TRANSPARENT);
//        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        lineData.addDataSet(lineDataSet);
//        lineChart.setData(lineData);
//        lineChart.setVisibleXRangeMaximum(g);//需要在设置数据源后生效X轴显示的个数
//        lineChart.getAxisRight().setXOffset(15f);
//        lineChart.getAxisLeft().setXOffset(10f);
//        lineChart.getAxisRight().setEnabled(true);
//        lineChart.setTouchEnabled(true); // 设置是否可以触摸
//        lineChart.setDescription(null);//去除Description
//        lineChart.setDragEnabled(true);//设置可以拖动
//        lineChart.animateY(3000);
//        lineChart.animateX(3000);
//        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                System.out.println("点击事件：" + e.toString()
//                 + "Y: " + e.getY());
//                Intent intent1 = new Intent();
//                intent1.setClass(getContext(), ChartBaritemActivity.class);
//                intent1.putExtra("list", (Serializable)list);
//                intent1.putExtra("name", xlist.get((int) e.getX()));
//                intent1.putExtra("flag", flag);
//                startActivity(intent1);
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        });
//        tv.setText(getZhineng());
    }

    //教师异常
    public void teacher() {
        tite = "老师异常次数";
        g = 7;
        map = new TreeMap<String, Integer>();
        xlist = new ArrayList<>();
        //判断List中相同老师的个数
        for (int j = 0; j < list.size(); j++) {
            String i = list.get(j).getTeacherName();
            if (!list.get(j).getPicture().equals("null")) {
                if (map.get(i) == null) {
                    xlist.add(i);
                    map.put(i, 1);
                } else {
                    map.put(i, map.get(i) + 1);
                }
            } else {
                if (map.get(i) == null) {
                    xlist.add(i);
                    map.put(i, 0);
                }
            }
        }
        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < xlist.size(); i++) {
            entryList.add(new Entry(i, map.get(xlist.get(i))));
        }
        xAxis = lineChart.getXAxis();
        yAxis = lineChart.getAxisLeft();
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);//显示边界
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineDataSet = new LineDataSet(entryList, tite);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        xAxis.setLabelCount(xlist.size() );//X轴显示几个值
        xAxis.setTextColor(Color.WHITE);
        //自定义增加X轴的值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                axis.setAxisLineColor(Color.WHITE);
                axis.setTextColor(Color.WHITE);
                axis.setGridColor(Color.WHITE);
                return xlist.get((int) value % xlist.size());
            }
        });
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);

        lineData = new LineData();
        LimitLine yLimitLine = new LimitLine(1f, "异常警戒线");//限制线
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yAxis.addLimitLine(yLimitLine);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.TRANSPARENT);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(5);//需要在设置数据源后生效X轴显示的个数
        lineChart.getAxisRight().setXOffset(15f);
        lineChart.getAxisLeft().setXOffset(10f);
        lineChart.getAxisRight().setEnabled(true);
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDescription(null);//去除Description
        lineChart.setDragEnabled(true);//设置可以拖动
        lineChart.animateY(3000);
        lineChart.animateX(3000);



        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println("点击事件：" + e.toString()
                        + "Y: " + e.getY());
                Intent intent1 = new Intent();
                intent1.setClass(getContext(), ChartBaritemActivity.class);
                intent1.putExtra("list", (Serializable)list);
                intent1.putExtra("name", xlist.get((int) e.getX()));
                intent1.putExtra("flag", flag);
                startActivity(intent1);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        tv.setText(getZhineng());
    }
    //判断班级异常
    public void classr() {
        tite = "班级异常次数";
        map = new TreeMap<String, Integer>();
        xlist = new ArrayList<>();
        //判断List中相同班级的个数
        for (int j = 0; j < list.size(); j++) {
            String i = list.get(j).getStudent_class();
            if (!list.get(j).getPicture().equals("null")) {
                if (map.get(i) == null) {
                    xlist.add(i);
                    map.put(i, 1);
                } else {
                    map.put(i, map.get(i) + 1);
                }
            } else {
                if (map.get(i) == null) {
                    xlist.add(i);
                    map.put(i, 0);
                }
            }
        }
    }

    //智能提示语句
    private String getZhineng() {
        List<Integer> mlist = new ArrayList<>();
        List<String> slist = new ArrayList<>();
        int max = 0, j = 0;
        for (int i = 0; i < xlist.size(); i++) {
            if (map.get(xlist.get(i)) > max) {
                max = map.get(xlist.get(i));
                j = i;
            }
        }
        for (int i = 0; i < xlist.size(); i++) {
            if (map.get(xlist.get(i)) == max) {
                slist.add(xlist.get(i));
                mlist.add(max);
            }
        }
        int cishu = 0, mincishu = 0;
        String teacher = null;
        for (int i = 0; i < slist.size(); i++) {
            if (i == 0) {
                teacher = slist.get(i);
            } else {
                teacher = teacher + "、" + slist.get(i);
            }
        }
        for (int i = 0; i < xlist.size(); i++) {
            if (map.get(xlist.get(i)) > 1) {
                cishu++;
            }
            if (map.get(xlist.get(i)) == 0) {
                mincishu++;
            }
        }
        String zn = null;
        if (flag == 1) {
            zn = "        超过警戒线的人次有" + String.valueOf(cishu) + "人次，"
                    + "其中最高的是：" + teacher + "，有" + max + "次异常。" + "\n        有" + mincishu + "名老师一次异常情况都没有。";
        }else if (flag == 2){
            zn = "        超过警戒线的班级有" + String.valueOf(cishu) + "个，"
                    + "其中最高的是：" + teacher + "，有" + max + "次异常。" + "\n        有" + mincishu + "个班级一次异常情况都没有。";
        }
        return zn;
    }

    @Override
    public void getJilu(List<ZhiNengBean> list) {
        this.list = list;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                teacher();

            }
        });
    }
}
