package com.example.tmap_ex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TmapAuthentication;

import java.util.ArrayList;
import java.util.logging.LogManager;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private Toolbar toolbar;
    private boolean m_bTrackingMode = true;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private TMapPoint currentPoint = null;
    private TMapPoint reportPoint = null;
    private double latitude, longitude;
    private static String mApiKey = "l7xx54970a28096b40faaf92b3017b524f8c";
    private static int mMarkerID;
    private String add;

    //private ArrayList<TMapPoint> tmapPointList = new ArrayList<TMapPoint>();
    //private ArrayList<String> ArrayMarkerIDList = new ArrayList<String>();
    //private ArrayList<TMapPoint> tmapPointList = new ArrayList<TMapPoint>();

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("툴바");

        mContext =this;

        ConstraintLayout mapViewLayout = (ConstraintLayout) findViewById(R.id.map_view_layout);
        Button rp_btn = (Button)findViewById(R.id.report_btn);
        Button stats_btn = (Button)findViewById(R.id.stats_btn);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(mApiKey);

        tmapview.setCompassMode(true); //현재보는방향 자이로
        tmapview.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.
        tmapview.setSightVisible(true); //시야표출여부를 설정
        tmapview.setZoomLevel(300);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

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
                startActivityForResult(intent, 1);
            }
        });

        stats_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                latitude = data.getExtras().getDouble("rep_latitude");
                longitude = data.getExtras().getDouble("rep_longitude");
                reportPoint = new TMapPoint(latitude, longitude);
                TMapCircle tcircle = new TMapCircle();
                tcircle.setCenterPoint(reportPoint);
                tcircle.setRadius(5);
                tcircle.setAreaColor(Color.BLUE);
                tcircle.setRadiusVisible(true);
                tmapview.addTMapCircle("circle1", tcircle);
                TMapData tmapdata = new TMapData();
                tmapdata.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback(){

                    @Override
                    public void onConvertToGPSToAddress(String s) {
                        add = s;
                    }
                });


                if (reportPoint!=null) {
                    Toast.makeText(MainActivity.this, "notnull주소 : " + add, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "주소 : null", Toast.LENGTH_SHORT).show();
                }
            } else {   // RESULT_CANCEL
                //Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
