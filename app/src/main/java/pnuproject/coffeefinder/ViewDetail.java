package pnuproject.coffeefinder;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

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
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.URLEncoder;

public class ViewDetail extends AppCompatActivity {
    private static final String TAG = "ViewDetail";
    PieChart pieChart;
    PieDataSet pieDataSet;
    PieData pieData;
    ViewDetail.Wrapper wrapper = new ViewDetail.Wrapper();
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetail);

        new getItemData().execute("스타벅스|카페베네|엔제리너스|이디야|투썸|더벤티|파스쿠찌");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("로딩중");

        final AppCompatButton easySearchButton =(AppCompatButton)findViewById(R.id.go_on_map);
        easySearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(wrapper.latitude) + "," + String.valueOf(wrapper.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "google streetview 앱을 설치하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("호불호");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 3, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    public class Wrapper
    {
        public StringBuilder responseBuilder;
        public String brand_name;
        public String category;
        public String coffee_name;
        public double ratings;
        public int positive_cnt;
        public int negative_cnt;
        public double latitude;
        public double longitude;
        public String taste_cnt;
        public String image_url;
    }


    private class getItemData extends AsyncTask<String, Void, ViewDetail.Wrapper> {
        protected ViewDetail.Wrapper doInBackground(String... _query) {
            StringBuilder responseBuilder = new StringBuilder();
            try {
                int radius = 300;

                // 검색을 위한 URL 생성
                URL url = new URL("https://pnu-coffee-project-heyask.c9users.io/http/rowdata.php?id=1");

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
            wrapper.brand_name = _query[0];

            return wrapper;
        }

        protected void onProgressUpdate(Void res) {

        }

        @Override
        protected void onPostExecute(ViewDetail.Wrapper responseSets) {
            StringBuilder response = responseSets.responseBuilder;
            String brand_name = responseSets.brand_name;

            try {
                JSONObject reader = new JSONObject(response.toString());
                wrapper.brand_name = reader.getString("brand_name");
                wrapper.category = reader.getString("category");
                wrapper.coffee_name = reader.getString("coffee_name");
                wrapper.ratings = reader.getDouble("ratings");
                wrapper.positive_cnt = reader.getInt("positive_cnt");
                wrapper.negative_cnt = reader.getInt("negative_cnt");
                wrapper.latitude = reader.getDouble("latitude");
                wrapper.longitude = reader.getDouble("longitude");
                wrapper.taste_cnt = reader.getString("taste_cnt");
                wrapper.image_url = reader.getString("image_url");

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

            Glide.with(ViewDetail.this).load(wrapper.image_url).into((ImageView) findViewById(R.id.image0));
            Glide.with(ViewDetail.this).load(wrapper.image_url).into((ImageView) findViewById(R.id.image1));
            Glide.with(ViewDetail.this).load(wrapper.image_url).into((ImageView) findViewById(R.id.image2));
            Glide.with(ViewDetail.this).load(wrapper.image_url).into((ImageView) findViewById(R.id.image3));


            collapsingToolbarLayout.setTitle(wrapper.coffee_name + " " + wrapper.brand_name);

            TextView ratings = (TextView)findViewById(R.id.ratings);
            ratings.setText(String.valueOf(wrapper.ratings) + "점!");

            pieChart = (PieChart) findViewById(R.id.chart_rating);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.setTouchEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            entries.add(new PieEntry(wrapper.positive_cnt, "긍정"));
            entries.add(new PieEntry(wrapper.negative_cnt, "부정"));

            pieDataSet = new PieDataSet(entries, "긍정,부정 리뷰 비율");
            pieDataSet.setDrawIcons(false);
            pieDataSet.setSelectionShift(5f);

            List<Integer> colors = new ArrayList<Integer>();
            colors.add(Color.rgb(111, 255, 0));
            colors.add(Color.rgb(255, 0, 64));
            pieDataSet.setColors(colors);

            pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.BLACK);
            pieChart.setData(pieData);

            pieChart.invalidate();

            pieChart.setCenterText(generateCenterSpannableText());


            Toast.makeText(getApplicationContext(), "brandName: " + wrapper.brand_name, Toast.LENGTH_LONG).show();

        }
    }
}



