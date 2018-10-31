package p03.example.li.xuncha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartBaritemActivity extends Activity {

    private BarChart barChart;
    private TextView tvZhineng1;
    private List<ZhiNengBean> list = new ArrayList<>();
    private YAxis yAxis;
    private XAxis xAxis;
    private BarData lineData;
    private BarDataSet lineDataSet;
    //老师的名字是KEY，异常多少次是值。
    private Map<String, Integer> map;
    // 获取老师的名字
    private List<String> xlist;
    //判断是哪个
    private int flag = 0;
    private String teacher;
    private String tite = null;
    private int g = 5;
    private int error = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_baritem);
        initView();
    }

    public void initView() {
        barChart = (BarChart) findViewById(R.id.barChart);
        tvZhineng1 = (TextView) findViewById(R.id.tv_zhineng1);

        //1为老师异常，2为班级异常，3为巡查人的巡查次数
        Intent intent = getIntent();
        list = (List<ZhiNengBean>) intent.getSerializableExtra("list");
        flag = intent.getIntExtra("flag", -1);
        teacher = intent.getStringExtra("name");
        List<BarEntry> entryList = new ArrayList<>();
        status();
        for (int i = 0; i < xlist.size(); i++) {
            entryList.add(new BarEntry(i, map.get(xlist.get(i))));
        }
        xAxis = barChart.getXAxis();
        yAxis = barChart.getAxisLeft();
//        Legend legend = barChart.getLegend(); 图例
//        legend.setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);//显示边界
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(0.1f);
        lineDataSet = new BarDataSet(entryList, tite);
        lineDataSet.setColor(Color.BLUE);
        xAxis.setLabelCount(xlist.size());//X轴显示几个值
        //自定义增加X轴的值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] strings = xlist.get((int) value % xlist.size()).split(",");
                return strings[0] + ", " + strings[1];
            }
        });
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        lineData = new BarData();
        LimitLine yLimitLine = new LimitLine(1f, "异常警戒线");//限制线
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yAxis.addLimitLine(yLimitLine);
        lineData.setBarWidth(0.3f);
        lineData.addDataSet(lineDataSet);
        barChart.setData(lineData);
        barChart.setVisibleXRangeMaximum(g);//需要在设置数据源后生效X轴显示的个数
//        barChart.getAxisRight().setXOffset(15f);
//        barChart.getAxisLeft().setXOffset(10f);
        barChart.getAxisRight().setEnabled(false);
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDescription(null);//去除Description
        barChart.setDragEnabled(true);//设置可以拖动
        barChart.animateY(3000);
        barChart.animateX(3000);
        tvZhineng1.setText(getZhineng());
    }

    public void status() {
        tite = "异常状态";
        g = 2;
        map = new TreeMap<String, Integer>();
        xlist = new ArrayList<>();
        //判断List中相同异常的个数
        if (flag == 1) {
            for (int j = 0; j < list.size(); j++) {
//            String i = list.get(j).getTeacherName();
                if (list.get(j).getTeacherName().equals(teacher) && !list.get(j).getPicture().equals("null")) {
                    error++;
                    String i = list.get(j).getStudent_status() + "," + list.get(j).getTeacher_status();
                    if (map.get(i) == null) {
                        xlist.add(i);
                        map.put(i, 1);
                    } else {
                        map.put(i, map.get(i) + 1);
                    }
                }
//            else {
//                String i = list.get(j).getStudent_status() + "," + list.get(j).getTeacher_status();
//                if (map.get(i) == null) {
//                    xlist.add(i);
//                    map.put(i, 0);
//                }
//            }
            }
        } else if (flag == 2) {
            for (int j = 0; j < list.size(); j++) {
//            String i = list.get(j).getTeacherName();
                if (list.get(j).getStudent_class().equals(teacher) && !list.get(j).getPicture().equals("null")) {
                    error++;
                    String i = list.get(j).getStudent_status() + ",    " + list.get(j).getTeacher_status();//空格为分割符，用来在表中换行
                    if (map.get(i) == null) {
                        xlist.add(i);
                        map.put(i, 1);
                    } else {
                        map.put(i, map.get(i) + 1);
                    }
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
        String teacher1 = null;
        for (int i = 0; i < slist.size(); i++) {
            if (i == 0) {
                teacher1 = slist.get(i);
            } else {
                teacher1 = teacher1 + "、" + slist.get(i);
            }
        }
        String stutas = null;
        for (int i = 0; i < xlist.size(); i++) {
            if (map.get(xlist.get(i)) > 1) {
                cishu++;
                if (stutas == null) {
                    stutas = xlist.get(i);
                } else {
                    stutas = stutas + "\n        " + xlist.get(i);
                }
            }
            if (map.get(xlist.get(i)) == 0) {
                mincishu++;
            }
        }
        String jianyi = "就目前这种状况，我是真的不想给你建议。";
        if (teacher1 != null && teacher1.contains("手机")) {
            jianyi = "建议准备一个手机袋，上课前把手机放进去";
        }
        if (teacher1 != null && teacher1.contains("讲话")) {
            jianyi = "建议调整上课方式，使课堂变得更加有趣，在加强纪律的管理";
        }

        String zn = null;
        if (stutas == null && error <= 1) {
            if (flag == 1) {
                zn = "        最近" + teacher + "老师上课很好，可以继续保持。\n        身为人工智能的我就不给啥建议了。";
            } else if (flag == 2) {
                zn = "        最近" + teacher + "这个班级表现良好，可以继续保持。\n        身为人工智能的我就不给啥建议了。";
            }
        } else if (stutas == null && error > 1) {
            if (flag == 1) {
                zn = "        最近" + teacher + "老师上课，虽然没有多次犯同一个错误，但却犯了多个错误，注意警惕！";
            } else if (flag == 2) {
                zn = "        最近" + teacher + "这个班级虽然没有多次犯同一个错误，但却犯了多个错误，注意警惕！";
            }
        } else {
            if (flag == 1) {
                zn = teacher + "老师多次异常的情况是:\n        " + stutas + "情况，\n"
                        + "其中最多的情况是：" + teacher1 + "，有" + max + "次异常。"
                        + "\n\n" + "        " + jianyi;
            } else if (flag == 2) {
                zn =  teacher + "班级多次异常的情况是:\n        " + stutas + "情况，\n"
                        + "其中最多的情况是：" + teacher1 + "，有" + max + "次异常。"
                        + "\n\n" + "        " + jianyi;
            }
        }
//        if (flag == 1) {
//            zn = "超过警戒线的人次有" + String.valueOf(cishu) + "人次，"
//                    + "其中最高的是：" + teacher + "，有" + max + "次异常。" + "有" + mincishu + "名老师一次异常情况都没有。";
//        }else if (flag == 2){
//            zn = "超过警戒线的班级有" + String.valueOf(cishu) + "个，"
//                    + "其中最高的是：" + teacher + "，有" + max + "次异常。" + "有" + mincishu + "个班级一次异常情况都没有。";
//        }
        return zn;
    }

}

