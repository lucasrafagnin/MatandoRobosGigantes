package com.mmidgard.matandorobosgigantes.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.mmidgard.matandorobosgigantes.EpisodioFeedParser;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class MainActivity extends Activity {

	private Button podcast;
	private Button vinhetas;
	private Button show;
	private ProgressDialog dialog;

	private ImageButton beto;
	private ImageButton diogo;
	private ImageButton affonso;
	private EpisodioDAO epdao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		podcast = (Button)findViewById(R.id.menu_ouvir);
		vinhetas = (Button)findViewById(R.id.menu_vinhetas);
		show = (Button)findViewById(R.id.menu_mrgshow);
		beto = (ImageButton)findViewById(R.id.beto);
		diogo = (ImageButton)findViewById(R.id.diogo);
		affonso = (ImageButton)findViewById(R.id.affonso);

		dialog = new ProgressDialog(this);

		btnMenu();

		epdao = new EpisodioDAO(MainActivity.this);

		new AsyncTask<Void, Integer, Void>() {

			private int valor = 0;

			protected void onPreExecute() {
				dialog.setIndeterminate(false);
				dialog.setMax(400);
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setCancelable(false);
				dialog.setTitle("Aguarde...");
				dialog.setMessage("Lendo feed do MRG");
				dialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
				List<Episodio> list = parser.parse();
				dialog.setMax(list.size());
				for (Episodio episodio : list) {
					if (epdao.getValor(episodio.getTitle(), "title") == null)
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

		beto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=56361939"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/56361939"));
				}
				startActivity(intent);
			}
		});

		diogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=76767920"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/76767920"));
				}
				startActivity(intent);
			}
		});

		affonso.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					getPackageManager().getPackageInfo("com.twitter.android", 0);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=77225484"));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				} catch (Exception e) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/77225484"));
				}
				startActivity(intent);
			}
		});
	}

}
