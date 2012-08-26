package com.hackaton.mafiaclient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalScore extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = (TextView)findViewById(R.id.result);
        if(Magic.pm.won)
        	tv.setText("Congratulation, you team won this round.");
        else
        	tv.setText("What a pity, you've loosed this round. Don't worry, try another time.");
        	
        super.setContentView(R.layout.activity_final);
    }
}
