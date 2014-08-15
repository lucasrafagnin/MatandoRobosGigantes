package com.mmidgard.matandorobosgigantes;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = new Intent(MainActivity.this, Podcast.class);
		startActivity(i);

//		new AsyncTask<Void, Void, List<Episodio>>() {
//
//			@Override
//			protected List<Episodio> doInBackground(Void... params) {
//				EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
//				return parser.parse();
//			}
//
//			protected void onPostExecute(List<Episodio> result) {
//				EpisodioDAO epdao = new EpisodioDAO(MainActivity.this);
//				for (Episodio episodio : result) {
//					epdao.insert(episodio);
//				}
//				
//				Intent i = new Intent(MainActivity.this, Podcast.class);
//				startActivity(i);
//			};
//		}.execute();

	}

	public void streaming() {
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
	}

}
