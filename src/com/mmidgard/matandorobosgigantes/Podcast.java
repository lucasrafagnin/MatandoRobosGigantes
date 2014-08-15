package com.mmidgard.matandorobosgigantes;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Podcast extends Activity implements OnItemClickListener {

	private ListView listPodcast;
	private AdapterListPodcast<Episodio> adapterPodcast;
	private List<Episodio> episodios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		Toast.makeText(Podcast.this, ep.getLink(), Toast.LENGTH_LONG).show();
	}
}
