package com.mmidgard.matandorobosgigantes.activity;

import java.util.Collections;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mmidgard.matandorobosgigantes.AdapterListPodcast;
import com.mmidgard.matandorobosgigantes.EpisodioFeedParser;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.Wifi;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class PodcastActivity extends Activity implements OnItemClickListener {

	private ListView listPodcast;
	private AdapterListPodcast<Episodio> adapterPodcast;
	private List<Episodio> episodios;
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
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		epdao = new EpisodioDAO(PodcastActivity.this);
		episodios = epdao.getAll();
		dialog = new ProgressDialog(this);

		btnTodos.setBackgroundColor(Color.parseColor("#ffffff"));
		btnTodos.setTextColor(Color.parseColor("#A32D3D"));

		btnBaixados.setBackgroundColor(Color.parseColor("#A32D3D"));
		btnBaixados.setTextColor(Color.parseColor("#ffffff"));
		btnFavoritos.setBackgroundColor(Color.parseColor("#A32D3D"));
		btnFavoritos.setTextColor(Color.parseColor("#ffffff"));

		updateList(episodios);

		clicks();
	}

	public void baixarPodcast() {
		new AsyncTask<Void, Integer, String>() {

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
			protected String doInBackground(Void... params) {
				if (Wifi.testConnection(PodcastActivity.this)) {
					EpisodioFeedParser parser = new EpisodioFeedParser("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
					List<Episodio> list = parser.parse();
					dialog.setMax(list.size());
					int total = 0;
					for (Episodio episodio : list) {
						if (epdao.getValor(episodio.getTitle(), "title") == null) {
							epdao.insert(episodio);
							total++;
						}
						valor++;
						onProgressUpdate(valor);
					}
					if (total == 0)
						return "";
					else
						return "Baixado " + total + " episódio(s)";
				} else
					return "Conecte-se à internet para baixar o Feed";
			}

			protected void onPostExecute(String v) {
				if (!v.equals(""))
					Toast.makeText(PodcastActivity.this, v, Toast.LENGTH_SHORT).show();
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

				EpisodioDAO epdao = new EpisodioDAO(PodcastActivity.this);
				
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

				EpisodioDAO epdao = new EpisodioDAO(PodcastActivity.this);

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
		if (menuselecionado != null && menuselecionado.size() > 0)
			Collections.sort(menuselecionado, Episodio.getListaOrdenada());
		adapterPodcast = new AdapterListPodcast<Episodio>(PodcastActivity.this, menuselecionado);

		listPodcast.setAdapter(adapterPodcast);
		listPodcast.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Episodio ep = (Episodio)arg0.getAdapter().getItem(arg2);
		Intent i = new Intent(PodcastActivity.this, SelecionadoActivity.class);
		i.putExtra("episodio", ep);
		if (ep.isBaixado())
			i.putExtra("offline", true);
		else
			i.putExtra("offline", false);

		startActivity(i);
	}
}
