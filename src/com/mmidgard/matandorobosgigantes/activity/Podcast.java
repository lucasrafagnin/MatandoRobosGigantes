package com.mmidgard.matandorobosgigantes.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mmidgard.matandorobosgigantes.AdapterListPodcast;
import com.mmidgard.matandorobosgigantes.EpisodioFeedParser;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class Podcast extends Activity implements OnItemClickListener {

	private ListView listPodcast;
	private AdapterListPodcast<Episodio> adapterPodcast;
	private List<Episodio> episodios;
	private List<Episodio> baixados = new ArrayList<Episodio>();
	private List<Episodio> favoritos = new ArrayList<Episodio>();
	private EpisodioDAO epdao;

	private Button btnTodos;
	private Button btnBaixados;
	private Button btnFavoritos;
	private Button baixarPod;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.podcast);

		btnTodos = (Button)findViewById(R.id.menu_todos);
		btnBaixados = (Button)findViewById(R.id.menu_baixados);
		btnFavoritos = (Button)findViewById(R.id.menu_favoritos);
		listPodcast = (ListView)findViewById(R.id.list_podcast);
		baixarPod = (Button)findViewById(R.id.baixarpod);

		epdao = new EpisodioDAO(Podcast.this);
		episodios = epdao.getAll();
		dialog = new ProgressDialog(this);

		updateList(episodios);

		clicks();
	}

	public void baixarPodcast() {
		new AsyncTask<Void, Integer, Void>() {

			private int valor = 0;

			protected void onPreExecute() {
				dialog.setIndeterminate(false);
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
				updateList(epdao.getAll());
				dialog.dismiss();

				btnTodos.setBackgroundColor(Color.parseColor("#ffffff"));
				btnTodos.setTextColor(Color.parseColor("#A32D3D"));

				btnBaixados.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnBaixados.setTextColor(Color.parseColor("#ffffff"));
				btnFavoritos.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnFavoritos.setTextColor(Color.parseColor("#ffffff"));
			};

			protected void onProgressUpdate(Integer... progress) {
				dialog.setProgress(progress[0]);
			}

		}.execute();

	}

	private void clicks() {
		btnTodos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnTodos.setBackgroundColor(Color.parseColor("#ffffff"));
				btnTodos.setTextColor(Color.parseColor("#A32D3D"));

				btnBaixados.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnBaixados.setTextColor(Color.parseColor("#ffffff"));
				btnFavoritos.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnFavoritos.setTextColor(Color.parseColor("#ffffff"));
				updateList(episodios);
			}
		});

		btnBaixados.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnBaixados.setBackgroundColor(Color.parseColor("#ffffff"));
				btnBaixados.setTextColor(Color.parseColor("#A32D3D"));

				btnTodos.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnTodos.setTextColor(Color.parseColor("#ffffff"));
				btnFavoritos.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnFavoritos.setTextColor(Color.parseColor("#ffffff"));

				EpisodioDAO epdao = new EpisodioDAO(Podcast.this);

				updateList(epdao.getValor(true, "baixado"));
			}
		});

		btnFavoritos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnFavoritos.setBackgroundColor(Color.parseColor("#ffffff"));
				btnFavoritos.setTextColor(Color.parseColor("#A32D3D"));

				btnBaixados.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnBaixados.setTextColor(Color.parseColor("#ffffff"));
				btnTodos.setBackgroundColor(Color.parseColor("#A32D3D"));
				btnTodos.setTextColor(Color.parseColor("#ffffff"));

				EpisodioDAO epdao = new EpisodioDAO(Podcast.this);

				updateList(epdao.getValor(true, "favorito"));
			}
		});

		baixarPod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baixarPodcast();
			}
		});
	}

	private void updateList(List<Episodio> menuselecionado) {
		adapterPodcast = new AdapterListPodcast<Episodio>(Podcast.this, menuselecionado);

		listPodcast.setAdapter(adapterPodcast);
		listPodcast.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Episodio ep = (Episodio)arg0.getAdapter().getItem(arg2);
		Intent i = new Intent(Podcast.this, Selecionado.class);
		i.putExtra("episodio", ep);
		startActivity(i);
	}
}
