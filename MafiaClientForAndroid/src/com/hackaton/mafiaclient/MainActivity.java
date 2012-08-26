package com.hackaton.mafiaclient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void Connect(View w) {
    	EditText et = (EditText) findViewById(R.id.login);
    	String login = et.getText().toString();
    	Magic.pm.name = login;
    	Toast.makeText(getApplicationContext(), "trying to connect you, " + login + "!", Toast.LENGTH_LONG).show();
    	Intent intent = new Intent(this.getApplicationContext(), GameRoom.class);
    	
    	startActivity(intent);
    }
}
