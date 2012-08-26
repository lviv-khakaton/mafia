package com.hackaton.mafiaclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FinalScore extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_final);
        
        Log.i("#FS","step 1");
        TextView tv = (TextView)findViewById(R.id.result);
        Log.i("#FS","step 2");
        if(Magic.pm.won)
        	tv.setText("Congratulation, you team won this round.");
        else
        	tv.setText("What a pity, you've loosed this round. Don't worry, try another time.");
    }
}
