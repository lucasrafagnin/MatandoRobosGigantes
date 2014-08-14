package com.mmidgard.matandorobosgigantes;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
				List<Episodio> episodes = parser.parse();
				for (Episodio episode : episodes) {
					Log.i("episode", episode.getTitle() + "------" + episode.getDescription());
				}
				return null;
			}
		}.execute();

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
