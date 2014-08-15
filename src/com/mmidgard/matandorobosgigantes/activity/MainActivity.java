package com.mmidgard.matandorobosgigantes.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		podcast = (Button)findViewById(R.id.menu_ouvir);
		vinhetas = (Button)findViewById(R.id.menu_vinhetas);
		show = (Button)findViewById(R.id.menu_mrgshow);

		dialog = new ProgressDialog(this);

		btnMenu();

		new AsyncTask<Void, Integer, Void>() {

			private int valor = 0;
			private EpisodioDAO epdao;

			protected void onPreExecute() {
				dialog.setIndeterminate(false);
				dialog.setMax(400);
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setCancelable(false);
				dialog.setTitle("Aguarde...");
				dialog.setMessage("Lendo feed do MRG");
				dialog.show();
				epdao = new EpisodioDAO(MainActivity.this);
			};

			@Override
			protected Void doInBackground(Void... params) {
				EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
				List<Episodio> list = parser.parse();
				dialog.setMax(list.size());
				for (Episodio episodio : list) {
					if (epdao.getById(episodio.getId()) == null)
						epdao.insert(episodio);
					valor++;
					onProgressUpdate(valor);
				}
				return null;
			}

			protected void onPostExecute(Void v) {
				dialog.dismiss();
			};

			protected void onProgressUpdate(Integer... progress) {
				dialog.setProgress(progress[0]);
			}

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
