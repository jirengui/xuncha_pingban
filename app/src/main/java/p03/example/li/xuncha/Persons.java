package p03.example.li.xuncha;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by li on 2018/1/15.
 */

public class Persons implements Comparable<SortModel>, Comparator<SortModel> {
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    /* 将字符串转为时间戳 */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 100000;
    }
    /* 时间比对 */
    @Override
    public int compareTo(@NonNull SortModel o) {
        return (int) (getStringToDate(this.getTime()) - getStringToDate(o
                .getLetters()));
    }

    @Override
    public int compare(SortModel o1, SortModel o2) {
        return  o1.getLetters().compareTo(o2.getLetters());
    }
}