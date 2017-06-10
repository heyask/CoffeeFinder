package pnuproject.coffeefinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;

    MarkerOptions myLocationMarker;
    Location mLastKnownLocation;
    CameraPosition mCameraPosition;
    LatLng mDefaultLocation = new LatLng(37.541, 126.986);
    Boolean mLocationPermissionGranted;

    CircularProgressView progressBar;

    static final double currentLocation[] = new double[2];
    static int currentZoomLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //커스텀 액션바
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        TextView actionBarTitle = (TextView)findViewById(R.id.action_bar_title);
        actionBarTitle.setText("내 주변의 인기 커피 찾기");

        //프로그레스 바
        progressBar = (CircularProgressView)findViewById(R.id.progress_bar);

        //지도
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        //간단검색 버튼
        final CircularProgressButton easySearchButton =(CircularProgressButton)findViewById(R.id.easysearch);
        easySearchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(easySearchButton.getProgress() > 0)
                        return;

                    easySearchButton.setIndeterminateProgressMode(true);
                    easySearchButton.setProgress(50);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            easySearchButton.setProgress(100);
                        }
                    }, 400);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(MainActivity.this, ViewDetail.class);
                            //intent.putExtra("text",String.valueOf(editText.getText()));
                            intent.putExtra("bene", 0);
                            startActivity(intent);
                            easySearchButton.setProgress(0);
                            easySearchButton.setIndeterminateProgressMode(false);
                        }
                    }, 500);
            }
        });
    }

    public class Wrapper
    {
        public StringBuilder responseBuilder;
        public String brandName;
    }

    private class findCafeFromCurrentLocation extends AsyncTask<String, Void, Wrapper> {
        protected Wrapper doInBackground(String... _query) {
            StringBuilder responseBuilder = new StringBuilder();
            Wrapper wrapper = new Wrapper();
            try {
                int radius = 300;
                switch(currentZoomLevel) {
                    case 17:
                        radius = 300;
                        break;
                    case 16:
                        radius = 600;
                        break;
                    case 15:
                        radius = 1200;
                        break;
                    case 14:
                        radius = 2400;
                        break;
                }
                // 검색을 위한 URL 생성
                URL url = new
                        URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBHqZY6Vqin32a8AXRAo_qhu6rlJcNgrOw"
                        + "&location=" + currentLocation[0] + "," + currentLocation[1]
                        + "&radius=" + radius
                        + "&keyword=" + URLEncoder.encode(_query[0], "UTF-8")
                        + "&rankyby=distance"
                        + "&hl=kr");

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();
            } catch (MalformedURLException me) {
                me.printStackTrace();
            } catch (UnsupportedEncodingException ue) {
                ue.printStackTrace();
            } catch (IOException ie) {
                ie.printStackTrace();
            }

            wrapper.responseBuilder = responseBuilder;
            wrapper.brandName = _query[0];

            return wrapper;
        }

        protected void onProgressUpdate(Void res) {

        }

        @Override
        protected void onPostExecute(Wrapper responseSets) {
            StringBuilder response = responseSets.responseBuilder;
            String brandName = responseSets.brandName;

            try {
                mMap.clear();
                JSONObject reader = new JSONObject(response.toString());
                JSONArray places = reader.getJSONArray("results");
                for (int i = 0; i < places.length(); i++) {
                    JSONObject place = places.getJSONObject(i);
                    JSONObject geometry = place.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    newMarker(lat, lng, place.getString("name"));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    private void getDeviceLocation() {
        // 위치 권한 체크
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void newMarker(double x, double y, String brand_name) {
        myLocationMarker = new MarkerOptions();
        myLocationMarker.position(new LatLng(x, y));

        System.out.println(brand_name);
        if (brand_name.matches(".*스타벅스.*") || brand_name.toLowerCase().matches(".*starbucks.*"))
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.starbucks));
        else if (brand_name.matches(".*카페베네.*") || brand_name.toLowerCase().matches(".*cafebene.*"))
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bene));
        else if (brand_name.matches(".*엔제리너스.*") || brand_name.matches(".*엔젤리너스.*") || brand_name.matches(".*엔제리스.*") || brand_name.toLowerCase().matches(".*angelinus.*"))
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.angelinus));
        else if (brand_name.matches(".*이디야.*") || brand_name.toLowerCase().matches(".*ediya.*"))
            myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ediya));

        mMap.addMarker(myLocationMarker);
    }

    /**
     * 구글맵 준비됨
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        updateLocationUI();
        getDeviceLocation();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMaxZoomPreference(17);
        mMap.setMinZoomPreference(14);

        // 서울 좌표
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //requestMyLocation();
                getDeviceLocation();
                return true;
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                currentLocation[0] = mMap.getCameraPosition().target.latitude;
                currentLocation[1] = mMap.getCameraPosition().target.longitude;

                progressBar.setVisibility(View.VISIBLE);
                currentZoomLevel = (int)mMap.getCameraPosition().zoom;
                // URL을 이용하여 검색을 하고, 결과를 받아 responseBuilder에 저장한다.
                new findCafeFromCurrentLocation().execute("스타벅스|카페베네|엔제리너스|이디야|투썸|더벤티|파스쿠찌");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * 구글플레이 서비스에 연결되었을때 구글맵로드
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionFailed (ConnectionResult result) {

    }

    @Override
    public void onConnectionSuspended (int _int) {

    }
}
