package com.example.tmap_ex;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static String mApiKey = "l7xx54970a28096b40faaf92b3017b524f8c";
    private TMapData tmapdata;
    private TMapPoint tmappoint;
    private TMapPoint tMapPoint;
    private String address;
    private ListView reportListView;
    private ReportItemListAdapter reportItemListAdapter;
    private ArrayList<Report_item_list> list_itemArrayList;
    private ArrayList<Report_item_list> newList_itemArrayList;
    private ArrayList<Report_item_list> todayList_itemArrayList;
    private CalendarView calendarView;
    private TextView noReport;
    String sDate;
    String Today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);

        new BackgroundTask().execute();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tMapPoint = new TMapPoint(37.545955, 126.964676);
        tmapdata = new TMapData();

        /*tmapdata.convertGpsToAddress(tMapPoint.getLatitude(), tMapPoint.getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback(){

            @Override
            public void onConvertToGPSToAddress(String s) {
                address = s;
            }
        });*/
        //Toast.makeText(StatsActivity.this, "주소 : " + address, Toast.LENGTH_SHORT).show();

        // 좌표를 주소로

        //tmappoint = new TMapPoint(35.504980, 129.382882);

/*
        tmapdata = new TMapData();
        tmapdata.convertGpsToAddress(35, 127, new TMapData.ConvertGPSToAddressListenerCallback() {
            @Override
            public void onConvertToGPSToAddress(String s) {
                address = s;
            }
        });

        final String address;
        try {
            address = new TMapData().convertGpsToAddress(tmappoint.getLatitude(), tmappoint.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ReverseGeocoding.this, address, Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(getApplicationContext(), "주소 : " + address, Toast.LENGTH_SHORT).show();


        try {
            address = tmapdata.convertGpsToAddress(37.566474, 126.985022);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


        try {
            final String address = new TMapData().convertGpsToAddress(tmappoint.getLatitude(), tmappoint.getLongitude());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ReverseGeocoding.this, address, Toast.LENGTH_SHORT).show();
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "주소 : " + address, Toast.LENGTH_SHORT).show();
*/
        // 캘린더뷰
        calendarView = (CalendarView) findViewById(R.id.simple_calendarview);
        noReport = (TextView)findViewById(R.id.noReport);

        long selectedDate = calendarView.getDate();
        Log.d("selected", Long.toString(selectedDate));

        Calendar mCalendar = Calendar.getInstance();
        Date today = mCalendar.getTime();
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Today = dfDate.format(today);
        Log.d("time: ", Today);

        // 리스트뷰
        reportListView = (ListView)findViewById(R.id.report_listView);
        list_itemArrayList = new ArrayList<Report_item_list>();
        todayList_itemArrayList = new ArrayList<Report_item_list>();
        //reportItemListAdapter = new ReportItemListAdapter(StatsActivity.this,list_itemArrayList);
        //reportListView.setAdapter(reportItemListAdapter);

    }

    // MySQL에서 데이터 받아오기
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected  void onPreExecute() {
            try {
                target = "http://ec2-18-216-239-216.us-east-2.compute.amazonaws.com/StatsReportRequest.php";
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("printStackTrace","왜안돼");
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                list_itemArrayList.clear();
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                Drawable category_image;
                String report_id;
                String report_category;
                String report_latitude;
                String report_longitude;
                String report_date;
                String report_state;


                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    category_image = null;
                    report_id = object.getString("report_id");
                    report_category = object.getString("report_category");
                    report_latitude = object.getString("report_latitude");
                    report_longitude = object.getString("report_longitude");
                    report_date = object.getString("report_date");
                    Log.d("report_date:", report_date.substring(0,10));
                    report_state = object.getString("report_state");

                    // category_image 지정

                    // report_category 지정
                    switch (report_category) {
                        case "1":
                            report_category = "근로자 추락";
                            category_image = getResources().getDrawable(R.drawable.ic_1);
                            break;
                        case "2":
                            report_category = "화물 낙하";
                            category_image = getResources().getDrawable(R.drawable.ic_2);
                            break;
                        case "3":
                            report_category = "협착 사고";
                            category_image = getResources().getDrawable(R.drawable.ic_3);
                            break;
                        case "4":
                            report_category = "화재 사고";
                            category_image = getResources().getDrawable(R.drawable.ic_4);
                            break;
                        case "5":
                            report_category = "가스누출 사고";
                            category_image = getResources().getDrawable(R.drawable.ic_5);
                            break;
                        case "6":
                            report_category = "교통 사고";
                            category_image = getResources().getDrawable(R.drawable.ic_6);
                            break;
                        case "7":
                            report_category = "기타";
                            category_image = getResources().getDrawable(R.drawable.ic_7);
                            break;
                    }

                    // report_state 지정
                    switch (report_state) {
                        case "0":
                            report_state = "처리중";
                            break;
                        case "1":
                            report_state = "해결됨";
                            break;
                    }

                    final Geocoder geocoder = new Geocoder(StatsActivity.this);
                    try{
                        List<Address> mResultList = geocoder.getFromLocation(Double.parseDouble(report_latitude), Double.parseDouble(report_longitude), 1);
                        address = mResultList.get(0).getAddressLine(0);
                        if(address.substring(0,4).equals("대한민국")){
                            address = address.substring(5,address.length());
                        }
                        Log.d("georesult: ",mResultList.get(0).getAddressLine(0));
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("georesult: ","fail");
                    }


                    Report_item_list report_item_list = new Report_item_list(category_image, report_category, address, report_date, report_state);
                    list_itemArrayList.add(report_item_list);
                    if(Today.equals(report_date.substring(0,10))){
                        todayList_itemArrayList.add(report_item_list);
                    }
                    count++;
                }
                Log.d("size:", String.valueOf(list_itemArrayList.size()));
                reportItemListAdapter = new ReportItemListAdapter(StatsActivity.this, todayList_itemArrayList);
                reportListView.setAdapter(reportItemListAdapter);
                //reportItemListAdapter.notifyDataSetChanged();
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        month+=1;
                        String tStringMonth = Integer.toString(month);
                        String StringMonth = null;
                        String tSdayOfMonth = Integer.toString(dayOfMonth);
                        String StringdayOfMonth = null;
                        if (month>=1 && month<=9){
                            StringMonth = "0"+tStringMonth;
                        }
                        else{
                            StringMonth = tStringMonth;
                        }
                        if (dayOfMonth>=1 && dayOfMonth<=9){
                            StringdayOfMonth = "0"+tSdayOfMonth;
                        }else{
                            StringdayOfMonth = tSdayOfMonth;
                        }
                        sDate = year+"-"+StringMonth+"-"+StringdayOfMonth;
                        //Toast.makeText(getApplicationContext(), year+"-"+StringMonth+"-"+StringdayOfMonth, Toast.LENGTH_SHORT).show();

                        int count = 0;
                        newList_itemArrayList = new ArrayList<Report_item_list>();
                        if (newList_itemArrayList != null){
                            newList_itemArrayList.clear();
                        }
                        while(count < list_itemArrayList.size()){
                            Log.d("redate:", list_itemArrayList.get(count).getReport_date());
                            if(sDate.equals(list_itemArrayList.get(count).getReport_date().substring(0,10))){
                                Report_item_list newReport_item_list = list_itemArrayList.get(count);
                                newList_itemArrayList.add(newReport_item_list);
                            }
                            count++;
                        }
                        reportItemListAdapter = new ReportItemListAdapter(StatsActivity.this, newList_itemArrayList);
                        reportListView.setAdapter(reportItemListAdapter);
                        //noReport.setText("신고된 사고가 없습니다.");
                        if(newList_itemArrayList.size()==0) {
                            noReport.setText("신고된 사고가 없습니다.");
                        }else{
                            noReport.setText("");
                        }
                    }
                });

                if(todayList_itemArrayList==null){
                    noReport.setText("신고된 사고가 없습니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("printStackTrace","왜안돼2");
            }

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                StringBuilder stringBuilder = new StringBuilder();

                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("printStackTrace","왜안돼3");            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
