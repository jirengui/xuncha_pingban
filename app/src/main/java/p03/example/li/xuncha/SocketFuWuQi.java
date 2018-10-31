package p03.example.li.xuncha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by li on 2017/11/20.
 */

public class SocketFuWuQi {
    //192.168.1.114 306B宿舍网
    //校园网172.29.135.19
    //手机热点192.168.43.57
    //阿里云IP：39.108.14.189
    //203: 192.168.1.114
    private String ip = "39.108.14.189";
    private Bitmap bmp = null;
    private File file;
    private int lenth = 0;
    private Socket socket = null;
    private String userId = "-1";
    private int i = 0;
    private BufferedReader in_1 = null;
    private PrintWriter out_1 = null;

    public SocketFuWuQi() {
//        try {
//            socket = new Socket(ip, 12345);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public SocketFuWuQi(int i) {
        try {
            if (socket == null) {
                socket = new Socket(ip, 12345);
            }
            in_1 = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out_1 = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLenth(String time, String name) throws IOException {
        socket = new Socket(ip, 12345);
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        // 把用户输入的内容发送给server
        String toServer = "0";
        out.println(toServer);
        out.flush();
        out.println(time);
        out.flush();
        out.println(name);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(),
                        "UTF-8"));
        lenth = Integer.valueOf(in.readLine());
        System.out.println("length: " + lenth);
        in.close();
        out.close();
        socket.close();
        return lenth;
    }

    public List<Bitmap> huoQuTuPian(String time, String name) throws IOException {
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        //返回多张图片根据时间和姓名-
        socket = new Socket(ip, 12345);
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        // 把用户输入的内容发送给server
        String toServer = "1";
        out.println(toServer);
        out.flush();
        out.println(time);
        out.flush();
        out.println(name);
        out.flush();
        DataInputStream dataInput = new DataInputStream(socket.getInputStream());
        lenth = dataInput.readInt();
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        for (int i = 0; i < lenth; i++) {
            int size = dataInput.readInt();
            byte[] data = new byte[size];
            int len = 0;
            while (len < size) {
                len += dataInput.read(data, len, size - len);
            }
            outPut = new ByteArrayOutputStream();
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outPut);
//                    imageView.setImageBitmap(bmp);
            bitmaps.add(bmp);
        }
        dataInput.close();
        outPut.close();
        out.close();
        socket.close();
        return bitmaps;
    }

    public void shangChuanTuPian(String str, ArrayList<String> uri) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "8";
        out.println(toServer);
        out.flush();
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(str);
        dos.flush();//字节输出流
        dos.writeInt(uri.size());
        dos.flush();
        for (int i = 0; i < uri.size(); i++) {
            dos = new DataOutputStream(socket.getOutputStream());
            String temp[] = uri.get(i).replaceAll("\\\\", "/").split("/");
            String fileName = "";
            if (temp.length > 1) {
                fileName = temp[temp.length - 1];

            }
            dos.writeUTF(fileName);
            dos.flush();
            dos = new DataOutputStream(socket.getOutputStream());
            Bitmap bitmap = getSmallBitmap(uri.get(i));
            File file = saveBitmapFile(bitmap, uri.get(i));
            dos.writeLong(file.length());
            dos.flush();
            FileInputStream fis = new FileInputStream(file);
            byte[] sendBytes = new byte[8192];
            while (true) {
                int read = 0;
                read = fis.read(sendBytes);
                if (read == -1) {
                    break;
                }
                dos.write(sendBytes, 0, read);
                dos.flush();// 发送给服务器
            }
        }
        dos.close(); //在发送消息完之后一定关闭，否则服务端无法继续接收信息后处理，手机卡机
        out.close();
        socket.close();
    }

    public void shangChuanTuPian1(String str) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "10";
        out.println(toServer);
        out.flush();
        out.println(str);
        out.flush();
        out.close();
        socket.close();
    }

    public int createUser(String userName, String userPassword) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        String toServer = "3";
        // 把用户输入的内容发送给server
        out.println(toServer);
        out.flush();
        String user = userName + "," + userPassword;
        out.println(user);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        String userId = in.readLine(); // 读取userId
        System.out.println("userId = " + Integer.parseInt(userId));
        out.close();
        in.close();
        socket.close();
        return Integer.parseInt(userId);
    }

    public String zhangHaoMiMaDengLu(String userName, String userPassword) throws IOException {
        String userId = "-1";
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);
        String toServer = "4";
        // 把用户输入的内容发送给server
        out.println(toServer);
        out.flush();
        String user = userName + "," + userPassword;
        out.println(user);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        userId = in.readLine(); // 读取userId
        String user_Name = in.readLine();
        String user_zhiwu = in.readLine();
        out.close();
        in.close();
        socket.close();
        String ss = userId + "," + user_Name + "," + user_zhiwu;
        System.out.println("ss: " + ss);
        return ss;
    }


    public void shangChuanTouXiang(String uri, String userId, String userName, String userAccount, String userJieShao) throws IOException {
        String informtion = userId + "," + userName + "," + userAccount + "," + userJieShao;
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "5";
        out.println(toServer);
        out.flush();
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(informtion);
        dos.flush();//字节输出流
        dos = new DataOutputStream(socket.getOutputStream());
        String temp[] = uri.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];

        }
        dos.writeUTF(fileName);
        dos.flush();
        dos = new DataOutputStream(socket.getOutputStream());
        Bitmap bitmap = getSmallBitmap(uri);
        File file = saveBitmapFile(bitmap, uri);
        dos.writeLong(file.length());
        dos.flush();
        FileInputStream fis = new FileInputStream(file);
        byte[] sendBytes = new byte[8192];
        while (true) {
            int read = 0;
            read = fis.read(sendBytes);
            if (read == -1) {
                break;
            }
            dos.write(sendBytes, 0, read);
            dos.flush();// 发送给服务器
        }
        dos.close(); //在发送消息完之后一定关闭，否则服务端无法继续接收信息后处理，手机卡机
        out.close();
        socket.close();
    }

    public String[] teacherInformation() throws IOException {
        String[] teacher = new String[200];
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);

        // 把用户输入的内容发送给server
        String toServer = "6";
        out.println(toServer);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String teacher1 = in.readLine();
        System.out.println("服务器教师 : " + teacher1);
        teacher = teacher1.split(",");
        out.flush();
        out.close();
        in.close();
        socket.close();
        return teacher;
    }
    public List banjiInformation(String jiaoshiName, String banji) throws IOException {
        List<String> teacher = new ArrayList<>();
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);

        // 把用户输入的内容发送给server
        String toServer = "20";
        out.println(toServer);
        out.println(jiaoshiName);
        out.println(banji);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String teacher1 = in.readLine();
        System.out.println("服务器教室 : " + teacher1);
        String[] jiaoshi = teacher1.split(",");
        teacher.addAll(Arrays.asList(jiaoshi));
        out.flush();
        out.close();
        in.close();
        socket.close();
        return  teacher;
    }

    public TeacherBean keCheng(String name) throws IOException {
        String[] keCheng = new String[200];
        String[] banJi = new String[200];
        TeacherBean teacherBean = new TeacherBean();
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);

        // 把用户输入的内容发送给server
        String toServer = "7";
        out.println(toServer);
        out.flush();
        out.println(name);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String kecheng = in.readLine();
        keCheng = kecheng.split(",");
        teacherBean.setKeCheng(keCheng);
        String banji = in.readLine();
        banJi = banji.split(",");
        teacherBean.setBanji(banJi);
        out.flush();
        out.close();
        in.close();
        socket.close();
        return teacherBean;
    }

    public String[] getJiLu(String id) throws IOException {
        String[] jilu;
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);

        // 把用户输入的内容发送给server
        String toServer = "9";
        out.println(toServer);
        out.flush();
        out.println(id);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String length = in.readLine();
        jilu = new String[Integer.parseInt(length)];
        System.out.println("上面");
        for (int i = 0; i < Integer.parseInt(length); i++) {

            jilu[i] = in.readLine();
            System.out.println("下面的啊：" + i);
        }
        System.out.println("下面");
        out.close();
        in.close();
        socket.close();
        return jilu;
    }

    public int getCishu(String userId) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        // 把用户输入的内容发送给server
        String toServer = "11";
        out.println(toServer);
        out.flush();
        out.println(userId);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String length = in.readLine();
        out.flush();
        out.close();
        in.close();
        socket.close();
        return Integer.valueOf(length);
    }

    public void setXiaoxi(String userId, String xiaoxi, String sendUserId) throws IOException {
        if (socket == null) {
            socket = new Socket(ip, 12345);
        }
        String[] ss = new String[5];
        //聊天室发送消息
        if (sendUserId.contains(",")) {
            ss = sendUserId.split(",");
        } else {
            ss[0] = "0";
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        // 把用户输入的内容发送给server
        String toServer = "12";
        out.println(toServer);
        out.flush();
        out.println(userId);
        out.flush();
        out.println(ss[0]);
        out.flush();
        out.println(xiaoxi);
        out.flush();
        out.close();
        socket.close();
    }

    public String getXiaoxi(String userId) throws IOException {
        if (socket == null) {
            socket = new Socket(ip, 12345);
        }
        if (this.userId.equals("-1")) {
            //聊天室接受消息
//             out_1 = new PrintWriter(new BufferedWriter(
//                    new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),
//                    true);
            // 把用户输入的内容发送给server
            String toServer = "13";
            out_1.println(toServer);
            out_1.flush();
            out_1.println(userId);
            out_1.flush();
//            in_1 = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream(),"UTF-8"));
        }
        this.userId = userId;
        String xx = null;
        try {
            while ((xx = in_1.readLine()) != null) {
                return xx;
            }
            System.out.println("消息：" + xx);
        } catch (IOException e) {
            return null;
        }
        return xx;
    }

    public void closeBuffer(String userId) throws IOException {
        if (socket == null || in_1 == null) {
            System.out.println("关闭异常没有socket或者没有Buffered");
            return;
        }
        System.out.println("传输流：" + in_1);
        String toServer = "15";
        out_1.println(toServer);
        out_1.flush();
        out_1.println(userId);
        out_1.flush();
        out_1.close();
        in_1.close();
        socket.close();

    }

    public ArrayList<String> getLiebiao() throws IOException {
        if (socket == null) {
            socket = new Socket(ip, 12345);
        }
        //聊天室接受列表
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())),
                true);
        // 把用户输入的内容发送给server
        String toServer = "14";
        out.println(toServer);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        ArrayList<String> liebiao = new ArrayList<String>();
        String xx;
        while ((xx = in.readLine()) != null) {
            liebiao.add(xx);
        }
        out.flush();
        out.close();
        in.close();
        socket.close();
        return liebiao;
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String getXinxi(String course_id) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "16";
        out.println(toServer);
        out.flush();
        out.println(course_id);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String xinxi = in.readLine();
        System.out.println("信息 : " + xinxi);
        out.close();
        in.close();
        socket.close();
        return xinxi;
    }

    public String getjihua(String user_Id) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "17";
        out.println(toServer);
        out.flush();
        out.println(user_Id);
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String jihua = in.readLine();
        System.out.println("计划 : " + jihua);
        out.close();
        in.close();
        socket.close();
        return jihua;
    }

    public void upjihua(String user_Id, String time, String bot) throws IOException {
        socket = new Socket(ip, 12345);
        //获取输出流向服务器端发消息
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8")),
                true);

        // 把用户输入的内容发送给server
        String toServer = "18";
        out.println(toServer);
        out.flush();
        out.println(user_Id);
        out.flush();
        out.println(time);
        out.flush();
        out.println(bot);
        out.flush();
        out.close();
        socket.close();
    }


}
