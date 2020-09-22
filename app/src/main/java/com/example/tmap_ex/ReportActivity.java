package com.example.tmap_ex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;


public class ReportActivity extends AppCompatActivity {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;
    private TMapGpsManager tmapgps = null;
    private TMapView tmapview = null;
    private TMapPoint currentPoint = null;
    private TMapPoint reportPoint = null;
    private double latitude, longitude;
    private static String mApiKey = "l7xx54970a28096b40faaf92b3017b524f8c";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);

        Intent intent = getIntent();
        latitude = intent.getExtras().getDouble("cur_latitude");
        longitude = intent.getExtras().getDouble("cur_longitude");
        currentPoint = new TMapPoint(latitude, longitude);
        Log.d("넘겨받는 값", currentPoint.getLatitude()+", "+currentPoint.getLongitude());

        ConstraintLayout mapViewLayout = (ConstraintLayout) findViewById(R.id.map_view_layout);
        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        tmapview = new TMapView(this);
        tmapview.setSKTMapApiKey(mApiKey);

        tmapview.setCompassMode(true); //현재보는방향 자이로
        tmapview.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.
        tmapview.setSightVisible(true); //시야표출여부를 설정
        tmapview.setZoomLevel(300);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapview.setLocationPoint(currentPoint.getLongitude(),currentPoint.getLatitude());
        tmapview.setCenterPoint(currentPoint.getLongitude(),currentPoint.getLatitude());
        mapViewLayout.addView(tmapview);

        confirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                reportPoint = tmapview.getCenterPoint();
                Intent intent = new Intent();
                intent.putExtra("rep_latitude",reportPoint.getLatitude());
                intent.putExtra("rep_longitude",reportPoint.getLongitude());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

