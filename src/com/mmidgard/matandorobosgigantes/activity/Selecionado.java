package com.mmidgard.matandorobosgigantes.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
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
	private SeekBar progresso;
	private MediaPlayer mediaPlayer;
	private boolean ouvindo = false;
	private EpisodioDAO epDao;

	private Handler seekHandler = new Handler();

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
		progresso = (SeekBar)findViewById(R.id.barraProgresso);
		titulo = (TextView)findViewById(R.id.selecionado_titulo);
		descricao = (TextView)findViewById(R.id.selecionado_descricao);
		play = (ImageButton)findViewById(R.id.btnPlay);
		next = (ImageButton)findViewById(R.id.btnNext);
		previous = (ImageButton)findViewById(R.id.btnPrevious);
		download = (ImageButton)findViewById(R.id.btnDownload);
		favourite = (ImageButton)findViewById(R.id.btnFavorite);
		pause = (ImageButton)findViewById(R.id.btnPause);

		if (episodio.isFavorito())
			favourite.setBackgroundResource(R.drawable.favourite_pressed);
		else
			favourite.setBackgroundResource(R.drawable.favourite);

		titulo.setText(episodio.getTitle());
		descricao.setText(episodio.getDescription());

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		epDao = new EpisodioDAO(Selecionado.this);

		clicks();

		streaming(episodio.getLink());
		progresso.setMax(mediaPlayer.getDuration());

		seekUpdation();
	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() {
		progresso.setProgress(mediaPlayer.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}

	private void clicks() {
		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mediaPlayer.start();
				play.setVisibility(View.GONE);
				pause.setVisibility(View.VISIBLE);
			}
		});

		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mediaPlayer.pause();
				play.setVisibility(View.VISIBLE);
				pause.setVisibility(View.GONE);
			}
		});

		favourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (episodio.isFavorito()) {
					episodio.setFavorito(false);
					favourite.setBackgroundResource(R.drawable.favourite);
				} else {
					episodio.setFavorito(true);
					favourite.setBackgroundResource(R.drawable.favourite_pressed);
				}
				epDao.update(episodio);
			}
		});

		// progresso.setOnSeekBarChangeListener(new
		// SeekBar.OnSeekBarChangeListener() {
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar seekBar) {
		//
		// }
		//
		// @Override
		// public void onStartTrackingTouch(SeekBar seekBar) {
		//
		// }
		//
		// @Override
		// public void onProgressChanged(SeekBar seekBar, int progress, boolean
		// fromUser) {
		// if (mediaPlayer != null && fromUser) {
		// mediaPlayer.seekTo(progress * 1000);
		// }
		// }
		// });
	}

	public void streaming(String url) {
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
