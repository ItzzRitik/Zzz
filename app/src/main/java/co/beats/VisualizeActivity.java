package co.beats;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;


public class VisualizeActivity extends AppCompatActivity {
    NavigationTabStrip tabs;
    CombinedChart chart;
    int mode = -1;

    // No Movement
    int[][] noA = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0},
            {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    int[][] noG = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0},
            {0, 0, 0}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    double[] noTemp = {36.53, 36.53, 36.53, 36.53, 36.53, 36.53, 36.53};

    // Without Binaural Beats
    int[][] noBinA = {
            {660, 28, 14824},
            {616, -48, 14716},
            {716, 108, 14692},
            {596, -28, 14804},
            {508, -16, 14860},
            {644, -12, 14716},
            {522, 32, 14848}};
    int[][] noBinG = {
            {-163, -368, -263},
            {-160, -328, -203},
            {-177, -348, -178},
            {-147, -340, -226},
            {-147, -309, -221},
            {-175, -358, -221},
            {-170, -354, -217}};
    double[] noBinTemp = {28.95, 29.00, 29.05, 29.09, 29.14, 29.19, 29.28};

    // With Binaural Beats
    int[][] binA = {
            {-6304, 1516, 1312},
            {-6212, 1472, 13136},
            {14136, 1608, 6772},
            {9340, 1200, 12064},
            {3816, 5428, 13648},
            {1096, 912, 15104},
            {-6000, 1172, 13084}};
    int[][] binG = {
            {466, 26, -1110},
            {348, -321, -349},
            {348, -286, -661},
            {1602, -5647, -840},
            {-2472, -2130, -476},
            {-1577, 6489, -2150},
            {-20, 489, -146}};
    double[] binTemp = {29.19, 29.14, 29.33, 29.28, 29.28, 29.38, 29.33};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);

        chart = findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)



        tabs = findViewById(R.id.tabs);
        tabs.setTabIndex(0, true);
        tabs.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int i) {
                if(mode == 0) newChart((i==0)?noA:noG, noTemp);
                else if(mode == 1) newChart((i==0)?noBinA:noBinG, noBinTemp);
                else if(mode == 2) newChart((i==0)?binA:binG, binTemp);
            }
            @Override public void onEndTabSelected(String title, int index) {}
        });

        mode = getIntent().getIntExtra("mode", -1);
        if(mode == 0) {
            getSupportActionBar().setTitle("No Movement");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.noM)));
            getWindow().setStatusBarColor(getResources().getColor(R.color.noMDark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.noMDark));
            tabs.setStripColor(getColor(R.color.noM));
            tabs.setActiveColor(getColor(R.color.noM));
            newChart(noA, noTemp);
        }
        else if(mode == 1) {
            getSupportActionBar().setTitle("Without Binaural Beats");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.notBinaural)));
            getWindow().setStatusBarColor(getResources().getColor(R.color.notBinauralDark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.notBinauralDark));
            tabs.setStripColor(getColor(R.color.notBinaural));
            tabs.setActiveColor(getColor(R.color.notBinaural));
            newChart(noBinA, noBinTemp);
        }
        else if(mode == 2) {
            getSupportActionBar().setTitle("With Binaural Beats");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.binaural)));
            getWindow().setStatusBarColor(getResources().getColor(R.color.binauralDark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.binauralDark));
            tabs.setStripColor(getColor(R.color.binaural));
            tabs.setActiveColor(getColor(R.color.binaural));
            newChart(binA, binTemp);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: finish();
            return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void newChart(final int[][] arr, double[] temp){
        CombinedData data = new CombinedData();
        data.setData(generateBarData(arr, temp));
        data.setData(generateLineData(arr));
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return arr[(int) value][0]+"";
            }
        });
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        chart.setData(data);
        chart.invalidate();
    }

    private LineData generateLineData(int[][] arr) {

        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) entries.add(new Entry(i + 0.5f, arr[i][1]/100));

        LineDataSet set = new LineDataSet(entries, "Y Values/100");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }


    private BarData generateBarData(int[][] arr, double[] temp) {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            entries1.add(new BarEntry(i, (float) arr[i][2]/100));
            entries2.add(new BarEntry(i, (float)temp[i]));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Z Values/100");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "Temperature");
        set2.setColors(Color.rgb(61, 165, 255));
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

}
