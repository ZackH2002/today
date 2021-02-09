package com.example.today;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.today.base.BaseData;
import com.example.today.base.ConsDate;
import com.example.today.base.RequstData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton CalenderButton;
    TextView responseText;
    private LinkedList<BaseData> mList = null;
    private ListView listView;
    private Context mContext;
    private MyListAdapter adapter = null;

    private Calendar calendar;
    private Date date;
    private Handler handler = new Handler();
    public String responsedata1 = null;
    ConsDate consDate = new ConsDate();
    //声明头布局中的TextView Cons-星座
    TextView TopTv, DayTv, WeekTv, ConsTv, QFriendTv, ColorTv, HealthTv, LoveTv, WorkTv, MoneyTv, SummaryTv,NameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalenderButton = (ImageButton) findViewById(R.id.calendar_button);
        listView = (ListView) findViewById(R.id.main_lv);
        new ListViewAsyncTask().execute();
        mContext = MainActivity.this;
        //获取日历对象
        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        addHeaderView();
//        jiexi(responsedata1);
    }

    //    public String request(String url_date) {
//        try {
//            String responsedata = null;
//            URL url = new URL(url_date);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(8000);
//            connection.setReadTimeout(8000);
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream in = connection.getInputStream();
//                byte[] b = new byte[1024 * 512];
//                int len;
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                while ((len = in.read(b)) > -1) {
//                    baos.write(b, 0, len);
//                }
//                responsedata = baos.toString();
//                Log.e("TAG", responsedata);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "连接网络错误", Toast.LENGTH_SHORT).show();
//            }
//            return responsedata;
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public class ListViewAsyncTask extends AsyncTask<Void, Void, String> {
        //自定义AsyncTask类，三个参数，第一个不定量入参，第二个：进度 第三个：结果
        protected void onPreExecute() {
            super.onPreExecute();//loading
        }

        //开启另一个线程，用于后台异步加载的工作
        protected String doInBackground(Void... voids) {
            String result = request("https://v1.alapi.cn/api/eventHitory");
            return result;
            //返回josn数据
        }

        protected String request(String url_date) {
            try {
                String responsedata = null;
                URL url = new URL(url_date);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    byte[] b = new byte[1024 * 512];
                    int len;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((len = in.read(b)) > -1) {
                        baos.write(b, 0, len);
                    }
                    responsedata = baos.toString();
                    Log.e("TAG", responsedata);

                } else {
                    Toast.makeText(getApplicationContext(), "连接网络错误", Toast.LENGTH_SHORT).show();
                }
                return responsedata;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String responsedata) {
            super.onPostExecute(responsedata);
            //Loading结束，处理数据
            //RequstData是用于存储对象信息的自定义类
            RequstData requstdata = new RequstData();
            //由于数据中有Json数据 所以需要一个ArrayList来存储
            List<BaseData> baseDatas = new ArrayList<>();
            try {
                JSONObject obj = new JSONObject(responsedata);
                requstdata.setMsg("msg");
                JSONArray jsonarray = obj.getJSONArray("data");
                for (int i = 0; i < jsonarray.length(); i++) {
                    BaseData baseData = new BaseData();
                    JSONObject TempObj = jsonarray.getJSONObject(i);
                    String time = TempObj.getString("year");
                    baseData.setTime(time);
                    String monthday = TempObj.getString("monthday");
                    String month=null;
                    month=monthday.substring(0,2);
                    String day=null;
                    day =monthday.substring(2);
                    baseData.setMonth(month);
                    baseData.setDay(day);
                    baseData.setMonthday(monthday);
                    String title = TempObj.getString("title");
                    baseData.setTitle(title);
                    String detail = TempObj.getString("desc");
                    baseData.setDetail(detail);
                    baseDatas.add(baseData);
                }
                //绑定数据
                listView.setAdapter(new MyListAdapter(MainActivity.this, baseDatas));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public static String GetUrl(String consName, String type) {
        String url = "http://web.juhe.cn:8080/constellation/getAll?consName=" + consName + "&type=" + type + "&key=300ae7250536d3de2a5c9050da825e67";
        return url;
    }

    private void addHeaderView() {
        //添加头布局和尾部局

        View headerView = LayoutInflater.from(this).inflate(R.layout.main_headerview, null);
        listView.addHeaderView(headerView);
        initHeaderView(headerView);

    }

    private void initHeaderView(View headerView) {
        //初始化头部局中的控件
        ConsDate consDate = new ConsDate();
        TopTv = headerView.findViewById(R.id.main_header_top);
        DayTv = headerView.findViewById(R.id.main_header_day);
        WeekTv = headerView.findViewById(R.id.main_header_week);
        ConsTv = headerView.findViewById(R.id.main_header_text_constellation);
        QFriendTv = headerView.findViewById(R.id.main_header_text_QFirend);
        ColorTv = headerView.findViewById(R.id.main_header_text_color);
        HealthTv = headerView.findViewById(R.id.main_header_text_health);
        LoveTv = headerView.findViewById(R.id.main_header_text_Love);
        WorkTv = headerView.findViewById(R.id.main_header_text_work);
        MoneyTv = headerView.findViewById(R.id.main_header_text_money);
        SummaryTv = headerView.findViewById(R.id.main_header_summary);
        NameTv=headerView.findViewById(R.id.main_header_text_constellation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cons_url = GetUrl("双鱼座", "today");
                try {
//                    String responsedata1=null;
                    URL url = new URL(cons_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream in = connection.getInputStream();
                        byte[] b = new byte[1024 * 512];
                        int len;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = in.read(b)) > -1) {
                            baos.write(b, 0, len);
                        }
                        responsedata1 = baos.toString();
                        Log.e("TAG", responsedata1);
                        List<ConsDate> consDates = new ArrayList<>();
                        try {

                            JSONObject jsonObject = new JSONObject(responsedata1);
                            String QFriend = jsonObject.getString("QFriend");
                            String color = jsonObject.getString("color");
                            String health = jsonObject.getString("health");
                            String love = jsonObject.getString("love");
                            String money = jsonObject.getString("money");
                            String summary = jsonObject.getString("summary");
                            String work=jsonObject.getString("work");
                            String date = jsonObject.getString("date");
                            String name=jsonObject.getString("name");
                            date=date.substring(6);
                            consDate.setDate(date);
                            consDate.setName(name);
                            consDate.setQFriend(QFriend);
                            consDate.setColor(color);
                            consDate.setHealth(health);
                            consDate.setLove(love);
                            consDate.setMoney(money);
                            consDate.setSummary(summary);
                            consDate.setWork(work);
                            consDates.add(consDate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "连接网络错误", Toast.LENGTH_SHORT).show();
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            QFriendTv.setText(consDate.getQFriend());
                            ColorTv.setText(consDate.getColor());
                            HealthTv.setText(consDate.getHealth());
                            LoveTv.setText(consDate.getLove());
                            MoneyTv.setText(consDate.getMoney());
                            SummaryTv.setText(consDate.getSummary());
                            WorkTv.setText(consDate.getWork());
                            DayTv.setText(consDate.getDate());
                            NameTv.setText(consDate.getName());
                        }
                    };
                    handler.post(runnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
//    public void show(){
//        new Thread(){
//            @Override
//            public void run(){
//                try {
//
//                }
//            }
//
//
//        }
//
//    }
//}
//    private void showResponse(final String response){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                QFriendTv.setText(consDate.getQFriend());
//                ColorTv.setText(consDate.getColor());
//            }
//        });




