package com.mmidgard.matandorobosgigantes.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.mmidgard.matandorobosgigantes.EpisodioFeedParser;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class MainActivity extends Activity {

	private Button podcast;
	private Button vinhetas;
	private Button show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		podcast = (Button)findViewById(R.id.menu_ouvir);
		vinhetas = (Button)findViewById(R.id.menu_vinhetas);
		show = (Button)findViewById(R.id.menu_mrgshow);

		btnMenu();

		new AsyncTask<Void, Void, List<Episodio>>() {

			@Override
			protected List<Episodio> doInBackground(Void... params) {
				EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
				return parser.parse();
			}

			protected void onPostExecute(List<Episodio> result) {
				EpisodioDAO epdao = new EpisodioDAO(MainActivity.this);
				for (Episodio episodio : result) {
					if (epdao.getById(episodio.getId()) == null)
						epdao.insert(episodio);
				}
			};
		}.execute();

	}

	private void btnMenu() {
		podcast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Podcast.class);
				startActivity(i);
			}
		});

		vinhetas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
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
