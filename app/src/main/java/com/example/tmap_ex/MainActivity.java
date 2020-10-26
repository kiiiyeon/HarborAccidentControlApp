package com.example.tmap_ex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TmapAuthentication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private TMapPoint currentPoint = null;
    private static String mApiKey = "l7xx54970a28096b40faaf92b3017b524f8c";
    private static int mMarkerID;
    private String mJsonString;
    private static final String TAG_JSON="webnautes";
    ArrayList<TMapMarkerItem> arr = new ArrayList<>();

    @Override
    public void onLocationChange(Location location){
        if(m_bTrackingMode){
            //Log.d("tag", "onlocationchange 불렸음");
            tmapview.setLocationPoint(location.getLongitude(),location.getLatitude());
            currentPoint = tmapgps.getLocation();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext =this;

        ConstraintLayout mapViewLayout = (ConstraintLayout) findViewById(R.id.map_view_layout);
        Button rp_btn =  (Button)findViewById(R.id.report_btn);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(mApiKey);

        tmapview.setCompassMode(true); //현재보는방향 자이로
        tmapview.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.
        tmapview.setSightVisible(true); //시야표출여부를 설정
        tmapview.setZoomLevel(18);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapview.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                for (TMapMarkerItem item : arrayList) {
                    Toast.makeText(getApplicationContext(), item.getID(), Toast.LENGTH_LONG).show();
                }
                Log.d("EndTest", "EndTest");
                return false;
            }
        });



        //마커 아이콘 설정
        //Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.download);
        //markerItem2.setIcon(markerIcon);





        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);//연결된 인터넷으로 현 위치를 받음. 실내일 때 사용.
        //tmapgps.setProvider(tmapgps.GPS_PROVIDER); //gps로 현 위치를 받음. 실외일때 사용가능.
        tmapgps.OpenGps();

        tmapview.setTrackingMode(true);
        mapViewLayout.addView(tmapview);

        rp_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                intent.putExtra("cur_latitude",currentPoint.getLatitude());
                intent.putExtra("cur_longitude",currentPoint.getLongitude());
                //startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GetData task = new GetData();
        task.execute("http://ec2-18-216-239-216.us-east-2.compute.amazonaws.com/ReportPointsRequest.php");
        GetGasData gasTask = new GetGasData();
        gasTask.execute("http://ec2-18-216-239-216.us-east-2.compute.amazonaws.com/GasValueRequest.php");
        //나중에 센서 위치 가져오고 지도에 표시하는 태스크 execute하는 코드 넣으면 됨.
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("report", "response  - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("report", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("report", "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    //위도 경도 이상있는지


    private class GetGasData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d("gas value", "response  - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                gasShowResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("gas value", "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("gas value", "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }



    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);


                double latitude = Double.parseDouble(item.getString("latitude"));
                double longitude = Double.parseDouble(item.getString("longitude"));
                int state = Integer.parseInt(item.getString("state"));
                if(state == 0){ //처리중
                    TMapPoint reportPoint = new TMapPoint(latitude, longitude);
                    TMapMarkerItem markerItem = new TMapMarkerItem();
                    markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem.setTMapPoint(reportPoint); // 마커의 좌표 지정
                    tmapview.addMarkerItem("marker"+i, markerItem); // 지도에 마커 추가
                    markerItem.setID("marker"+i);
                    arr.add(markerItem);

                }
                else{ //해결됨
                    TMapPoint reportPoint = new TMapPoint(latitude, longitude);
                    TMapMarkerItem markerItem = new TMapMarkerItem();
                    markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem.setTMapPoint(reportPoint); // 마커의 좌표 지정
                    tmapview.addMarkerItem("marker", markerItem); // 지도에 마커 추가
                    markerItem.setID("marker"+i);
                    arr.add(markerItem);
                    tmapview.addMarkerItem("marker"+i, markerItem);
                }
            }

        } catch (JSONException e) {

            Log.d("report", "showResult : ", e);
        }

    }

    private void gasShowResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString("id");
                double latitude = Double.parseDouble(item.getString("latitude")); //가스센서의 위치
                double longitude = Double.parseDouble(item.getString("longitude"));
                int state = Integer.parseInt(item.getString("state"));


                if(state == 0){ //아무것도 감지되지 않음

                }
                else{ //감지됨, 서클표시, 알람
                    TMapPoint reportPoint = new TMapPoint(latitude, longitude);
                    TMapMarkerItem markerItem = new TMapMarkerItem();
                    markerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem.setTMapPoint(reportPoint); // 마커의 좌표 지정
                    tmapview.addMarkerItem("marker", markerItem); // 지도에 마커 추가
                    markerItem.setID("marker"+i);
                    arr.add(markerItem);
                    tmapview.addMarkerItem("marker"+i, markerItem);

                    //알림 함수
                }
            }

        } catch (JSONException e) {

            Log.d("gasValue", "gasShowResult : ", e);
        }

    }


}
