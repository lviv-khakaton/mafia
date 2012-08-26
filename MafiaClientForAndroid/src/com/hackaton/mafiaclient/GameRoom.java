package com.hackaton.mafiaclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	Thread cycle;
	
	 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.cycle = new Thread(new ActionReader(this));
        try {
			setupConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void setupConnection() throws IOException {
		Socket socket = new Socket("10.0.2.2",4444);
    	os = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
        String myName = Magic.pm.name;
        os.writeUTF(myName);
        Initialize();
	}
    
	private void loadData() throws IOException {
		int n = is.readInt();
		names = new ArrayList<String>();
		actives = new ArrayList<Boolean>();
		for(int i=0;i<n;i++) {
			String s = is.readUTF();
			names.add(s);
			actives.add(true);
		}
		Magic.pm.type = is.readUTF();
		int w = 1;
		while(w*w < n)
			w++;
		this.width = w;
		Log.i("#GMR","before refresh1");
		refreshView();
		Log.i("#GMR","after refresh1");
		cycle.start();
	}
	
	void endGame() {
		Intent intent = new Intent(this.getApplicationContext(), FinalScore.class);
		startActivity(intent);
	}
	
	private void Initialize() {
		
		cycle = new Thread(new Runnable(){
	        public void run(){
	            runOnUiThread(new Runnable(){
	                @Override
	                public void run() {
	                    
	                	while(true) {
	            			int code = -1;
	            			try {
	            				code = is.readInt();
	            				Log.i("#GMR",code + " read");
	            			} catch (IOException e) {
	            				e.printStackTrace();
	            			}
	            			if(code==0) {
	            				List<Boolean> tmp = new ArrayList<Boolean>();
	            				for(int i=0;i<actives.size();i++)
	            					try {
	            						tmp.add(is.readBoolean());
	            					} catch (IOException e) {
	            						e.printStackTrace();
	            					}
	            				actives = tmp;
	            				Log.i("#GMR","before refresh_X");
	            				refreshView();
	            				Log.i("#GMR","after refresh_X");
	            				continue;
	            			}
	            			
	            			if(code==1) {
	            				//you have died
	            				continue;
	            			}
	            			if(code==2) {
	            				try {
	            					boolean won = is.readBoolean();
	            					Magic.pm.won = won;
	            					endGame();
	            				} catch (IOException e) {
	            					e.printStackTrace();
	            				}
	            				break;
	            			}
	            			if(code==7) {
	            				//TODO fall asleep
	            				
	            				continue;
	            			}
	            			
	            			
	            			break;
	            		}
	                	
	                }
	            });
	        }
        });
		
		//final ProgressDialog dialog = ProgressDialog.show( GameRoom.this, "Waiting for other players", "Please wait...", true);
		Thread thread=new Thread(new Runnable(){
	        public void run() {       	
        		runOnUiThread(new Runnable(){
        			@Override
	                public void run() {
        				Log.i("#GMR","loadData start");
        				try {
							loadData();
						} catch (IOException e) {
							e.printStackTrace();
						}
        				Log.i("#GMR","loadData finish");
//	                    if(dialog.isShowing()) dialog.dismiss();
	                    Log.i("#GMR","Timer removed");
        			}
        		});
	            
	        }
        });
		thread.start();
		
	}

	void refreshView() {
		
		TableLayout layout = new TableLayout (this);
        layout.setLayoutParams( new TableLayout.LayoutParams(4,5) );
        layout.setPadding(1,1,1,1);
		
		TextView tv = new TextView(this);
		tv.setText("Name : " + Magic.pm.name + " .::. Role : " + Magic.pm.type);
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
                //b.setId(index);
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

	@Override
	public void onClick(View view) {
		int id = ((Button) view).getId();
		try {
			os.writeInt(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
/*
class ActionReader implements Runnable {

	private GameRoom gr;
	
	public ActionReader(GameRoom gr) {
		this.gr = gr;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			int code = -1;
			try {
				code = gr.is.readInt();
				Log.i("#GMR",code + " read");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(code==0) {
				List<Boolean> tmp = new ArrayList<Boolean>();
				for(int i=0;i<gr.actives.size();i++)
					try {
						tmp.add(gr.is.readBoolean());
					} catch (IOException e) {
						e.printStackTrace();
					}
				gr.actives = tmp;
				gr.refreshView();
				continue;
			}
			
			if(code==1) {
				//you have died
				continue;
			}
			if(code==2) {
				try {
					boolean won = gr.is.readBoolean();
					Magic.pm.won = won;
					gr.endGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			if(code==7) {
				//TODO fall asleep
				
				continue;
			}
			
			
			break;
		}
	}
	
}
*/