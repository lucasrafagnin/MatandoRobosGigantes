package com.mmidgard.matandorobosgigantes.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class Selecionado extends Activity {
	private Episodio episodio;
	private TextView titulo;
	private TextView descricao;
	private ImageButton play;
	private ImageButton next;
	private ImageButton previous;
	private ImageButton download;
	private ImageButton favourite;
	private ImageButton pause;
	private MediaPlayer mediaPlayer;
	private boolean ouvindo = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_selecionado);

		Intent i = getIntent();
		Bundle b = i.getExtras();
		episodio = (Episodio)b.get("episodio");

		setarInformacoes();
	}

	private void setarInformacoes() {
		titulo = (TextView)findViewById(R.id.selecionado_titulo);
		descricao = (TextView)findViewById(R.id.selecionado_descricao);
		play = (ImageButton)findViewById(R.id.btnPlay);
		next = (ImageButton)findViewById(R.id.btnNext);
		previous = (ImageButton)findViewById(R.id.btnPrevious);
		download = (ImageButton)findViewById(R.id.btnDownload);
		favourite = (ImageButton)findViewById(R.id.btnFavorite);
		pause = (ImageButton)findViewById(R.id.btnPause);

		titulo.setText(episodio.getTitle());
		descricao.setText(episodio.getDescription());

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		clicks();
	}

	private void clicks() {
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ouvindo)
					mediaPlayer.start();
				else {
					streaming(episodio.getLink());
					ouvindo = true;
				}

				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);
			}
		});

		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ouvindo) {
					mediaPlayer.pause();
				}
				play.setVisibility(View.VISIBLE);
				pause.setVisibility(View.GONE);
			}
		});
	}

	public void streaming(String url) {
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
