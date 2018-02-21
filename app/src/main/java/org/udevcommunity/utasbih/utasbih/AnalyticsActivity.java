package org.udevcommunity.utasbih.utasbih;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Color.BLACK;
/**
 * AnalyticsActivity
 *
 * This Class is used to render the Analytics charts for each mode
 *
 * @package :
 * @author : UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
public class AnalyticsActivity extends AppCompatActivity {

    public static final float MAX_SALAT_TASBIH = 6;
    public static final float MIN_SALAT_TASBIH = 1;

    private UTasbihSQLiteHelper database = null;
    private int SALAT_MODE = 1;
    private int TASBIH_MODE = 2;
    private int TAHMID_MODE = 3;
    private int TAKBEER_MODE = 4;

    //TODO put following configurables into a config Class
    private String MODE_TASBIH_LEGEND = "تسبيح";
    private String MODE_TAHMID_LEGEND = "تحميد";
    private String MODE_TAKBEER_LEGEND = "تكبير";

    private float TEXT_SIZE = 15f;

    private ViewFlipper mFlipper;
    private GestureDetector mGestureDetector;
    private float NEGATIVE_VELOCITY = -10.0f;

    private BarChart salatChart;
    private String salatChartDescription = "تسابيح الصلوت المفروضة";
    private CoordinatorLayout coordinatorLayout;
    private LineChart freeModeChart;

    private String salatChartDataDescritpion = "عدد التسبيح للصلوات المفروضة";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        mFlipper = (ViewFlipper) findViewById(R.id.charts_flipper);
        FloatingActionButton flippBtn = (FloatingActionButton) findViewById(R.id.fab);
        //retrieve tasbih data from the database
        database = new UTasbihSQLiteHelper(this);

        // Getting data from the DataBase as object (will be used for the graph)
        List<DayInfo> salatModeList = database.getAllInfos(SALAT_MODE);
        List<DayInfo> tasbihModeList = database.getAllInfos(TASBIH_MODE);
        List<DayInfo> tahmidModeList = database.getAllInfos(TAHMID_MODE);
        List<DayInfo> takbeerModeList = database.getAllInfos(TAKBEER_MODE);

        //init BarChart for Salat mode
        salatChart = (BarChart) findViewById(R.id.salatTasbihChart);

        BarData salatData = new BarData( getSalatChartXAxisValues(salatModeList), getSalatChartDataSet(salatModeList) );
        salatChart.setData(salatData);

        //set Description
        salatChart.setDescription(salatChartDescription);
        salatChart.setDescriptionTextSize(TEXT_SIZE);

        //set the chart's Xaxis
        setSalatChartXAxis();
        setSalatChartLegends();
        //refresh BarChart
        salatChart.invalidate();


        //define a LineChart for Free tasbih mode
        freeModeChart = (LineChart) findViewById(R.id.freeModeTasbihChart);

        //set the Lines for each mode
        freeModeChart.setData(getLineData(tasbihModeList, tahmidModeList,takbeerModeList));
        //TODO Strings shouldn't be harcoded
        freeModeChart.setDescription("عدد التسابيح خارج الصلوات المفروضة");
        freeModeChart.setDescriptionPosition(500f,10f);

        freeModeChart.setDescriptionTextSize(TEXT_SIZE);
        freeModeChart.invalidate();
        setfreeModeChartxAxis ();


        //flip between the chart by the floating button and the ViewFlipper
         flippBtn.setOnClickListener(
                 new View.OnClickListener() {
                     public void onClick(View v) {
                      //   Snackbar.make(coordinatorLayout, "معاينة نوع الأذكار التالي", Snackbar.LENGTH_SHORT).show();
                         mFlipper.showPrevious();
                     }
                 }
         );

    }



    /**
     * getSalatChartXAxisValues
     *
     *
     *
     * @param : a list of DayInfo.
     * @expectedException : none.
     * @return List of xAxis string values for BarChart
     * @link  https://github.com/ztickm/UTasbih
     */
    private List<String> getSalatChartXAxisValues(List<DayInfo> xValues) {

        List<String> xAxis = new ArrayList<>();
        for (int i=0;i<xValues.size();i++) {
            xAxis.add(String.valueOf(xValues.get(i).getDay()));
        }
        return xAxis;
    }

    /**
     * getSalatChartDataSet
     *
     *
     *
     * @param : a list of DayInfo.
     * @expectedException : none.
     * @return ArrayList of DataSet entries for BarChart
     * @link  https://github.com/ztickm/UTasbih
     */

    private ArrayList<BarDataSet> getSalatChartDataSet(List<DayInfo> salahList) {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int i=0;i<salahList.size();i++) {

            //Convert String values of the List to flaot
            String tasbihValue = String.valueOf(salahList.get(i).getNumber());
            // create Bar entries from tasbih values
            Integer tasbihEntry= Integer.parseInt(tasbihValue);

            BarEntry entry = new BarEntry(tasbihEntry,i);

            valueSet1.add(entry);

        }

        //create BarChart data set with the values extracted from the tasbih list
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, salatChartDataDescritpion);
        // barDataSet1.setColors(ColorTemplate.VORDIPLOM_COLORS);


        //add BarChart Dataset to an ArrayList

        dataSets.add(barDataSet1);
        return dataSets;
    }

    /**
     * setSalatChartXAxisValues
     *
     *  set xAxis parameters
     *
     * @param : none.
     * @expectedException : none.
     * @return void
     * @link  https://github.com/ztickm/UTasbih
     */

    private void setSalatChartXAxis (){

        salatChart.setDrawBarShadow(true);
        salatChart.setBackgroundColor(Color.WHITE);
        XAxis xAxis = salatChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TEXT_SIZE);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        //vertical line separating entries
        xAxis.setDrawGridLines(false);

        YAxis axisRight = salatChart.getAxisRight();
        axisRight.setEnabled(false);
        axisRight.setDrawAxisLine(false);

        YAxis axisLeft = salatChart.getAxisLeft();
        axisLeft.setTextSize(TEXT_SIZE);

        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMaxValue(MAX_SALAT_TASBIH);
        axisLeft.setAxisMinValue(MIN_SALAT_TASBIH);
        axisLeft.setLabelCount(7,true);


        axisLeft.setShowOnlyMinMax(true);

        String[] limitLinesLabel = {"الصبح","الظهر","العصر","المغرب","العشاء"};

        for(int idx=0;idx<limitLinesLabel.length;idx++){

            LimitLine tempLimitLine = new LimitLine(idx+1, null);
            tempLimitLine.setLineColor(Color.RED);
            tempLimitLine.setLineWidth(2f);
            tempLimitLine.setTextColor(Color.BLACK);
            tempLimitLine.setTextSize(15f);
            axisLeft.addLimitLine(tempLimitLine);
        }


    }
    /**
     * setSalatChartLegends
     *
     * set Legends parameters
     *
     * @param : none
     * @expectedException : none.
     * @return void
     * @link  https://github.com/ztickm/UTasbih
     */
    private void setSalatChartLegends(){

        Legend l = salatChart.getLegend();
        l.setFormSize(TEXT_SIZE); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

        l.setTextSize(12f);
        l.setTextColor(BLACK);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

    }
    /**
     * getLineData
     * @param : 3 lists of DayInfo for each mode.
     * @expectedException : none.
     * @return LineData for LineChart
     * @link  https://github.com/ztickm/UTasbih
     */

    private LineData getLineData(List<DayInfo> tasbih, List<DayInfo> tahmid,
                                 List<DayInfo> takbeer){

        //get the numbers of days
        List<DayInfo> date = getDates(tasbih, tahmid,takbeer);

        //init entries ArrayLists for each mode
        ArrayList<Entry> valsTasbih = new ArrayList<Entry>();
        ArrayList<Entry> valsTahmid = new ArrayList<Entry>();
        ArrayList<Entry> valsTakbeer = new ArrayList<Entry>();

        //init xAxis values ArrayList
        ArrayList<String> xVals = new ArrayList<String>();


        String freemode1Value;
        String freemode2Value;
        String freemode3Value;
        int datesCount = date.size();
        Log.d("Liste1 tasbih:", String.valueOf(tasbih.size()));
        Log.d("Liste2 tahmid:", String.valueOf(tahmid.size()));
        Log.d("Liste3 takbir:", String.valueOf(takbeer.size()));
        // have equal lists size
        for (int i=0;i<date.size();i++) {

            //convert each item of list to string then to float
            if ( i == tasbih.size() || tasbih.size()==0)  freemode1Value = "0";
            else freemode1Value= String.valueOf(tasbih.get(i).getNumber());

            if ( i == tahmid.size() || tahmid.size()==0) freemode2Value = "0";
            else freemode2Value = String.valueOf(tahmid.get(i).getNumber());

            if( i == takbeer.size() || takbeer.size() == 0) freemode3Value = "0";
            else freemode3Value = String.valueOf(takbeer.get(i).getNumber());

            Float freeMode1Entry = Float.parseFloat(freemode1Value);
            Float freeMode2Entry = Float.parseFloat(freemode2Value);
            Float freeMode3Entry = Float.parseFloat(freemode3Value);

            //Make a new entry and assigned to a Xaxis value (i)
            Entry freeMode1Point = new Entry(freeMode1Entry, i);
            Entry freeMode2Point = new Entry(freeMode2Entry, i);
            Entry freeMode3Point = new Entry(freeMode3Entry, i);

            //add each point to its specific line mode
            valsTasbih.add(freeMode1Point);
            valsTahmid.add(freeMode2Point);
            valsTakbeer.add(freeMode3Point);

            //xVals for date
            xVals.add(String.valueOf(date.get(i).getDay()));

        }
        //set a line data set for each mode by the lists created above
        LineDataSet setTasbih = new LineDataSet(valsTasbih, MODE_TASBIH_LEGEND);
        LineDataSet setTahmid = new LineDataSet(valsTahmid, MODE_TAHMID_LEGEND);
        LineDataSet setTakbeer = new LineDataSet(valsTakbeer, MODE_TAKBEER_LEGEND);

        setTasbih.setAxisDependency(YAxis.AxisDependency.LEFT);
        setTasbih.setCircleSize(5f);
        setTasbih.setValueTextSize(12f);

        setTahmid.setAxisDependency(YAxis.AxisDependency.LEFT);
        setTahmid.setCircleSize(5f);
        setTahmid.setValueTextSize(12f);

        setTakbeer.setAxisDependency(YAxis.AxisDependency.LEFT);
        setTakbeer.setCircleSize(5f);
        setTakbeer.setValueTextSize(12f);

        //set a color for each mode
        setTasbih.setColor(BLACK);
        setTahmid.setColor(Color.BLUE);
        setTakbeer.setColor(Color.RED);

        //create a list of data sets
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        //add each Line set to the list
        dataSets.add(setTasbih);
        dataSets.add(setTahmid);
        dataSets.add(setTakbeer);

        //create a lines data and return it
        return new LineData(xVals, dataSets);
    }

    /**
     * getDates: return the list with the max
     * date entries
     *
     *
     * @param : 3 lists of DayInfo for each mode.
     * @expectedException : none.
     * @return List of xAxis string values for BarChart
     * @link  https://github.com/ztickm/UTasbih
     */
    private List<DayInfo> getDates(List<DayInfo> list1,List<DayInfo> list2,
                                   List<DayInfo>list3){

        // list of all modes lists sizes
        List<Integer> sizes= new ArrayList<>();
        sizes.add(list1.size());
        sizes.add(list2.size());
        sizes.add(list3.size());

        List<DayInfo> dateList = new ArrayList<>();
        //return the list with the max size
        if (Collections.max(sizes)==list1.size()) dateList= list1;
        else if (Collections.max(sizes)==list2.size()) dateList = list2;
        else if (Collections.max(sizes)==list3.size()) dateList = list3;

        return dateList;

    }

    /**
     * setfreeModeChartXAxis
     *
     *  set Axis and legend parameters for Free Mode Chart
     *
     * @param : none.
     * @expectedException : none.
     * @return void
     * @link  https://github.com/ztickm/UTasbih
     */
    private void setfreeModeChartxAxis() {



        XAxis xAxis = freeModeChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(TEXT_SIZE);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setXOffset(30f);

        YAxis axisLeft = freeModeChart.getAxisLeft();
        axisLeft.setDrawLabels(true);
        axisLeft.setGridColor(Color.DKGRAY);
        axisLeft.setTextSize(TEXT_SIZE);
        axisLeft.setDrawAxisLine(true);

        YAxis axisRight = freeModeChart.getAxisRight();
        axisRight.setEnabled(false);

        Legend lg = freeModeChart.getLegend();
        lg.setTextSize(TEXT_SIZE);
        lg.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        lg.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);

    }

}
