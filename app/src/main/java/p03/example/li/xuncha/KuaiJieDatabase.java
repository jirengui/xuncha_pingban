package p03.example.li.xuncha;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class KuaiJieDatabase extends SQLiteOpenHelper {

    public static final String CREATE_USERDATABASE = "create table KuaiJieDatabase ("
            + "id integer primary key autoincrement, "
            + "t1 text, "
            + "t2 text, "
            + "t3 text, "
            + "t4 text, "
            + "flag text,"
            + "status text, "
            + "provider text,"
            + "userId text) ";

    private Context mContext;

    public KuaiJieDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATABASE);
        ContentValues values = new ContentValues();
        values.put("t1", "教师正在走动");
        values.put("t2", "教师正在讲课");
        values.put("t3", "教师正在为学生解答");
        values.put("t4", "教师不在教室");
        values.put("flag", "teacher_true");
        values.put("provider", 0);//提供者
        values.put("status", "老师状态");
        values.put("userId", 0);//使用者
        db.insert("KuaiJieDatabase", null, values);
        values.clear();

        values.put("t1", "迟到 人");
        values.put("t2", "缺勤 人");
        values.put("t3", "带食物 人");
        values.put("t4", "玩手机和游戏 人");
        values.put("flag", "studet_true");
        values.put("provider", 0);//提供者
        values.put("status", "学生状态");
        values.put("userId", 0);//使用者
        db.insert("KuaiJieDatabase", null, values);
        values.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
