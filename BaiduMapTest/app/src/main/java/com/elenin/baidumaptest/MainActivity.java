package com.elenin.baidumaptest;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.inner.GeoPoint;

import java.util.List;

import static android.R.id.list;

public class MainActivity extends Activity {

    private LocationManager locationManager;

    private String provider;

    private BMapManager manager;

    private MapView mapView = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        manager = new BMapManager();
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setBuildInZoomControls(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider to use",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LocationManager location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            navigateTo(location);
        }
    }

    private void navigateTo(Location location) {
        MapController controller = mapView.getController();
        controller.setZoom(16)
        GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
                (int) (location.getLongitude() * 1E6));
        controller.setCenter(point);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(mapView);
        LocationData locationData = new LocationData();
        locationData.latitude = location.getLatitude();
        locationData.longitude = location.getLongitude();
        myLocationConfiguration.setData(locationData);
        mapVierw.getOverlays().add(myLocationOverlay);
        mapView.refresh();


    }

    @Override
    protected void onResume() {
        mapView.onResume();
        if (manager != null) {
            manager.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        if (manager != null) {
            manager.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.destroy();
        if (manager != null) {
        manager.destroy();
        manager = null;
    }
    super.onDestroy();
    }



}
