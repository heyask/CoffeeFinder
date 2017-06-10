package pnuproject.coffeefinder;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.transition.Slide;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.primary;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewDetail extends AppCompatActivity {
    class Entry
    {
        int value;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdetail);

        CollapsingToolbarLayout collapsingToolbarLayout;
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("모카 프라푸치노 - 스타벅스");

        /*final ImageView image = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE)).into(image, new Callback() {
            @Override public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override public void onError() {

            }
        });*/

        /*TextView title = (TextView) findViewById(R.id.title);
        title.setText(itemTitle);*/

        //CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("Title");

        PieChart pieChart = (PieChart) findViewById(R.id.chart_rating);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        //YourData[] dataObjects = ...;

        //List<PieEntry> entries = new ArrayList<>();
        //entries.add(new PieEntry(60f, "긍정"));
        //entries.add(new PieEntry(40f, "부정"));

        //PieDataSet set = new PieDataSet(entries, "Election Results");
        //PieData data = new PieData(set);
        //pieChart.setData(data);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(60f, "호"));
        entries.add(new PieEntry(40f, "불호"));

        PieDataSet dataSet = new PieDataSet(entries, "긍정,부정 리뷰 비율");
        dataSet.setDrawIcons(false);

        //dataSet.setSliceSpace(3f);
        //dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        List<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.rgb(111, 255, 0));
        colors.add(Color.rgb(255, 0, 64));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(mTfLight);
        pieChart.setData(data);
        //pieChart.setTextColor(Color.rgb(0, 0, 0));

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        pieChart.invalidate(); // refresh
        pieChart.setTouchEnabled(false);

        pieChart.setCenterText(generateCenterSpannableText());
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
}



