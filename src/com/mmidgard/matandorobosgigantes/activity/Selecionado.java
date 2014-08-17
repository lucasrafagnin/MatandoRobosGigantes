package com.mmidgard.matandorobosgigantes.activity;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mmidgard.matandorobosgigantes.R;
import com.mmidgard.matandorobosgigantes.dao.EpisodioDAO;
import com.mmidgard.matandorobosgigantes.entity.Episodio;

public class Selecionado extends Activity {
	private static final String LOG_TAG = "LOG";
	private Episodio episodio;
	private TextView titulo;
	private TextView descricao;
	private ImageButton play;
	private ImageButton next;
	private ImageButton previous;
	private ImageButton download;
	private ImageButton favourite;
	private ImageButton pause;
	private ImageButton abrirBrowser;
	private SeekBar progresso;
	private MediaPlayer mediaPlayer;
	private EpisodioDAO epDao;

	private Handler seekHandler = new Handler();
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_selecionado);

		Intent i = getIntent();
		Bundle b = i.getExtras();
		episodio = (Episodio)b.get("episodio");
		dialog = new ProgressDialog(this);

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
		abrirBrowser = (ImageButton)findViewById(R.id.abrir_browser);

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
					Toast.makeText(Selecionado.this, "Episódio adicionado aos favoritos!", Toast.LENGTH_LONG).show();
				}
				epDao.update(episodio);
			}
		});

		abrirBrowser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(episodio.getLinkSite()));
				startActivity(browserIntent);
			}
		});

		download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baixarEpisodio(episodio.getLink());
			}
		});
	}

	public void streaming(String url) {
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void baixarEpisodio(final String link) {
		new AsyncTask<Void, String, Void>() {

			@Override
			protected void onPreExecute() {
				dialog.setIndeterminate(false);
				dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog.setCancelable(false);
				dialog.setTitle("Aguarde...");
				dialog.setMessage("Baixando o episódio");
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				int count;
				try {
					URL url = new URL(link);
					URLConnection conexion = url.openConnection();
					conexion.connect();
					int lenghtOfFile = conexion.getContentLength();
					Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
					InputStream input = new BufferedInputStream(url.openStream());
					OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/mrg/");
					byte data[] = new byte[1024];
					long total = 0;
					while ((count = input.read(data)) != -1) {
						total += count;
						publishProgress("" + (int)((total * 100) / lenghtOfFile));
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onProgressUpdate(String... progress) {
				dialog.setProgress(Integer.parseInt(progress[0]));
			}

			protected void onPostExecute(Void result) {
				dialog.dismiss();
			};

		}.execute();
	}
}
