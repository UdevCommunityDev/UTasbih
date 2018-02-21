package org.udevcommunity.utasbih.utasbih;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by acer on 01/11/2015.
 */
public class SettingsActivity extends Activity {

    public final String PREF_NAME = "UTasbihPref";

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    Button save;
    Switch vibration;
    Switch notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        save = (Button) findViewById(R.id.save);
        vibration = (Switch) findViewById(R.id.vibration);
        notification = (Switch) findViewById(R.id.notification);

        settings = getSharedPreferences(PREF_NAME, 0);
        editor = settings.edit();

        boolean vibrate = true;
        boolean notificate = true;
        try {
            vibrate = settings.getBoolean("vibration", false);
            notificate = settings.getBoolean("notification", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        vibration.setChecked(vibrate);
        notification.setChecked(notificate);

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("notification", true);
                } else {
                    editor.putBoolean("notification", false);
                }
            }
        });
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    editor.putBoolean("vibration", true);
                }
                else
                {
                    editor.putBoolean("vibration", false);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.commit();

                //TODO Kill actual activity and return to previous
                finish();
            }
        });
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
}