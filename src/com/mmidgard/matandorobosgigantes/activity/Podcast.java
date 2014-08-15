package com.mmidgard.matandorobosgigantes.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mmidgard.matandorobosgigantes.AdapterListPodcast;
import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class Podcast extends Activity implements OnItemClickListener {

	private ListView listPodcast;
	private AdapterListPodcast<Episodio> adapterPodcast;
	private List<Episodio> episodios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.podcast);

		EpisodioDAO epdao = new EpisodioDAO(Podcast.this);
		episodios = epdao.getAll();

		updateList(episodios);
	}

	private void updateList(List<Episodio> users) {
		listPodcast = (ListView)findViewById(R.id.list_podcast);
		adapterPodcast = new AdapterListPodcast<Episodio>(Podcast.this, episodios);

		listPodcast.setAdapter(adapterPodcast);
		listPodcast.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Episodio ep = episodios.get(arg2);
		Intent i = new Intent(Podcast.this, Selecionado.class);
		i.putExtra("episodio", ep);
		startActivity(i);
	}
}
