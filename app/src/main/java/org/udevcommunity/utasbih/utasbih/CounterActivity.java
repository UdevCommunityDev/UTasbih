/**
 * CounterActivity.java
 *
 * CounterActivity Activity
 *
 * @author :   UDevCommunity <Contact@UDevCommunity.com>
 */

package org.udevcommunity.utasbih.utasbih;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Vibrator;
/**
 * CounterActivity
 *
 * CounterActivity Activity
 *
 * @package :
 * @author :  UDevCommunity <Contact@UDevCommunity.com>
 * @license :
 * @link : https://github.com/ztickm/UTasbih
 */
public class CounterActivity extends Activity
{
    int mode;
    int counter = 0;
    int maxSalatTasbih = 100;// counter used to stock the number of tasbih
    TextView tasbihText = null;     // Text of Tasbih ex: 'الحمد لله'
    TextView counterView = null;       // TextView of the counter 'used to display the number in counter'
   // Button incrementCounter = null;     // Incrementing Button for the counter
    RelativeLayout principalLayout = null;   // principal layout 'used to set the color when rich 33,66,99,100 etc ...
    Vibrator vibr_tasbih = null;  // create vibr_tasbih objet from vibrator class
    SharedPreferences sharedPreferences;
    Boolean vibrationOn;
    public final String PREF_NAME = "UTasbihPref";
    UTasbihSQLiteHelper database = null;  // Database


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // getting Views from the resources
        tasbihText = (TextView) findViewById(R.id.textTasbih);
        counterView = (TextView) findViewById(R.id.counter);

        principalLayout = (RelativeLayout) findViewById(R.id.layoutCounter);
        vibr_tasbih = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        database = new UTasbihSQLiteHelper(this);

        // getting the parameter of mode
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode",1);

        // initialize the activity according to the mode
        initActivity(mode);

        sharedPreferences = this.getSharedPreferences(PREF_NAME, 0);
        vibrationOn = sharedPreferences.getBoolean("vibration", false);
    }

    // onClick method for incrementing the tasbeeh number
    public void onBackgroundClick(View v){
        if (mode == 1)
        {
            // call the incCounterSalat function
            incCounterSalat();
        }
        else
        {
            // call the incCounterBasic function
            incCounterBasic();
        }
    }


    // If we exit the activity
    @Override
    public void onStop()
    {
        super.onStop();

        if (mode != 1) // if we are in free style mode
        {
            database.addStat(mode, (counter%100)); // we add the rest of Devision (counter / 100) Because we execute addStat every time counter rich a 100k
            counter = 0;
            this.counterView.setText("0");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings)
        {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    public void initActivity(int mode)
    {
        switch (mode) {
            case 1:
                tasbihText.setText("سبحان الله");
                break;
            case 2:
                tasbihText.setText("سبحان الله");
                break;
            case 3:
                tasbihText.setText("الحمد لله");
                break;
            case 4:
                tasbihText.setText("الله أكبر");
                break;
        }

    }

    /**
     * incCounterSalat.
     *
     * incCounterSalat.
     *
     * @param : none.
     * @expectedException : none.
     * @return void.
     * @link  https://github.com/ztickm/UTasbih
     */
    public void incCounterSalat()
    {
        // Conditions to set the Text+Backgtound when rich 33,66,99,100 .
        if (this.counter == 33)
        {
            vibrateTasbih(500, vibrationOn); // Vibrate for 500 milliseconds
            tasbihText.setText("الحمد لله");
            principalLayout.setBackgroundColor(Color.rgb(103, 58, 183));
        }
        else if (this.counter == 66)
        {
            vibrateTasbih(500, vibrationOn); // Vibrate for 500 milliseconds
            tasbihText.setText("الله أكبر");
            principalLayout.setBackgroundColor(Color.rgb(255, 152, 0));
        }
        else if (this.counter == 99)
        {
            vibrateTasbih(500, vibrationOn); // Vibrate for 500 milliseconds
            tasbihText.setText("لا إله إلا الله وحده لا شريك له ,له الملك و له الحمد و هو على كل شيئ قدير");
            principalLayout.setBackgroundColor(Color.rgb(121, 85, 72));
            //incrementCounter.setText("نهاية الأذكار");
        }
        // Return to the main Activty
        else if (this.counter == maxSalatTasbih)
        {
            vibrateTasbih(1000, vibrationOn); //Vibrate for 1000 milliseconds  --  end of tasbih

            database.addStat(mode, 1); // add 1 to database

            // Opening the Main Activity
            // Intent counterActivity = new Intent(CounterActivity.this, MainActivity.class);
            // startActivity(counterActivity);
            finish();
        }

        // Incrementing the counter
        if (this.counter < maxSalatTasbih ) this.counter++;

        // Set the new value of counter to the Textview "counterView"
        this.counterView.setText(Integer.toString(this.counter));
    }

    //
    public void incCounterBasic()
    {
        if ((this.counter % 100) == 0 && this.counter > 0)
        {
            vibrateTasbih(500, vibrationOn); //Vibrate for 1000 milliseconds  I choice 1000 to fill the difference --  end of tasbih
            database.addStat(mode, 100); // add 100 to database
        }
        // Incrementing the counter
        this.counter++;

        // Set the new value of counter to the Textview "counterView"
        this.counterView.setText(Integer.toString(this.counter));
    }

    public void vibrateTasbih( int vibrationDurationMillis, Boolean vibrationOn){
        if (vibrationOn){
            vibr_tasbih.vibrate(vibrationDurationMillis); // Vibrate for 500 milliseconds
        }
    }
}
