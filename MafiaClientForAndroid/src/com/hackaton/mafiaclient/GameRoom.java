package com.hackaton.mafiaclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameRoom extends Activity implements View.OnClickListener {
	
	DataOutputStream os;
	DataInputStream is;
	List<String> names;
	List<Boolean> actives;
	private int width;
	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
			setupConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
        super.setContentView(R.layout.activity_gameroom);
    }

	private void setupConnection() throws IOException {
		Socket socket = new Socket("10.0.2.2",4444);
    	os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
        String myName = "ivan";
        os.writeUTF(myName);
        Initialize();
	}
    
	private void Initialize() throws IOException {
		int n = is.readInt();
		names = new ArrayList<String>();
		actives = new ArrayList<Boolean>();
		for(int i=0;i<n;i++) {
			String s = is.readUTF();
			names.add(s);
			actives.add(true);
		}
		int w = 1;
		while(w*w < n)
			w++;
		this.width = w;
		refreshView();
	}

	private void refreshView() {
		
		TableLayout layout = new TableLayout (this);
        layout.setLayoutParams( new TableLayout.LayoutParams(4,5) );
        layout.setPadding(1,1,1,1);
		
		TextView tv = new TextView(this);
        tv.setText("Users");
        layout.addView(tv);

        for (int i=0; i<width; i++) {
            TableRow tr = new TableRow(this);
            for (int j=0; j<width; j++) {
            	int index = i*width + j;
            	if(index >= names.size())
            		break;
                Button b = new Button (this);
                b.setText(names.get(index) + " : " + i + j);
                b.setTextSize(10.0f);
                b.setTextColor(Color.rgb( 255, 0, 0));
                b.setOnClickListener(this);
                if(!actives.get(index))
                	b.setClickable(false);
                tr.addView(b, 100, 100);
            }
            layout.addView(tr);
        }

        super.setContentView(layout);
	}

	public void onClick(View view) {
	    ((Button) view).setText("*");
	    ((Button) view).setEnabled(false);
	}

}
