package com.example.today;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.today.base.BaseData;
import com.example.today.base.RequstData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalenderButton = (ImageButton) findViewById(R.id.calendar_button);
//        responseText = (TextView) findViewById(R.id.tv);
        listView = (ListView) findViewById(R.id.main_lv);
        new ListViewAsyncTask().execute();
        mContext = MainActivity.this;
//        mList = new LinkedList<BaseDate>();//实例化LinkedList
//        sendRequestWithHttpURLConnection();
//        for( int i = 1; i <= 18 ; i++) {
//            BaseDate BaseDate = new BaseDate();      //循环创建studentData 对象
//            BaseDate.setTime(i);          //为对象设置姓名
//            BaseDate.setTitle("jigt");                             //为对象设置 年龄
//            BaseDate.setDetail("detail");             //为对象设置照片
//            mList.add(BaseDate);                  //将对象添加到列表中
//        }
        //获取日历对象
        calendar = Calendar.getInstance();
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        addHeaderView();
    }

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
        private String request(String url_date) {
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
                    String  time= TempObj.getString("year");
                    baseData.setTime(time);
                    String monthday =TempObj.getString("monthday");
                    baseData.setMonthday(monthday);
                    String title=TempObj.getString("title");
                    baseData.setTitle(title);
                    String detail =TempObj.getString("desc");
                    baseData.setDetail(detail);
                    baseDatas.add(baseData);
                }
                //绑定数据
                listView.setAdapter(new MyListAdapter(MainActivity.this,baseDatas));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

}

//    private void addHeaderView() {
//        //添加头布局和尾部局
//        View headerView = LayoutInflater.from(this).inflate(R.layout.item_time_line, null);
//        mainLV.addHeaderView(headerView);
//    }

