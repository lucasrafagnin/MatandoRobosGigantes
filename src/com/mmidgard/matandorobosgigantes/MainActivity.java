package com.mmidgard.matandorobosgigantes;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO teste streaming: OK
		// try {
		// String url =
		// "http://jovemnerd.com.br/podpress_trac/web/100144/0/MRG230_Transformers4.mp3";
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// mediaPlayer.setDataSource(url);
		// mediaPlayer.prepare();
		// mediaPlayer.start();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				new ReaderRSS().percorrerFeed();
				return null;
			}
		}.execute();
		
	}

	

}
