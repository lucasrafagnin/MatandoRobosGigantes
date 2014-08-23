package com.mmidgard.matandorobosgigantes.activity;

import java.io.BufferedInputStream;
import java.io.File;
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
import android.os.Handler;
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

public class SelecionadoActivity extends Activity {
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
	private TextView duracaoTotal;
	private TextView duracaoAtual;
	private SeekBar progresso;
	private MediaPlayer mediaPlayer;
	private EpisodioDAO epDao;
	private boolean offline = false;

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
		offline = b.getBoolean("offline");

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
		duracaoTotal = (TextView)findViewById(R.id.duracao_total);
		duracaoAtual = (TextView)findViewById(R.id.duracao_atual);

		if (episodio.isFavorito())
			favourite.setBackgroundResource(R.drawable.favourite_pressed);
		else
			favourite.setBackgroundResource(R.drawable.favourite);

		if (episodio.isBaixado())
			download.setBackgroundResource(R.drawable.download_pressed);
		else
			download.setBackgroundResource(R.drawable.download);

		titulo.setText(episodio.getTitle());
		descricao.setText(episodio.getDescription());
		duracaoTotal.setText(episodio.getDuration());

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		epDao = new EpisodioDAO(SelecionadoActivity.this);

		clicks();

		if (!offline)
			streaming(episodio.getLink());
		else
			playLocal();
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
		duracaoAtual.setText(getTimeString(mediaPlayer.getCurrentPosition()));
		seekHandler.postDelayed(run, 1000);
	}

	private String getTimeString(long millis) {
		StringBuffer buf = new StringBuffer();

		int hours = (int)(millis / (1000 * 60 * 60));
		int minutes = (int)((millis % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int)(((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		buf.append(String.format("%01d", hours)).append(":").append(String.format("%02d", minutes)).append(":").append(String.format("%02d", seconds));

		return buf.toString();
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
					Toast.makeText(SelecionadoActivity.this, "Episódio adicionado aos favoritos!", Toast.LENGTH_LONG).show();
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
				baixarEpisodio(episodio.getLink(), episodio.getTitle());
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

	public void playLocal() {
		try {
			mediaPlayer.setDataSource(android.os.Environment.getExternalStorageDirectory() + "/mrg/" + episodio.getTitle() + ".mp3");
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(SelecionadoActivity.this, "Não foi encontrado o episódio na pasta 'mrg'.\nEfetue o download novamente", Toast.LENGTH_LONG).show();
		}
	}

	public void baixarEpisodio(final String link, final String nome) {
		new AsyncTask<Void, Integer, Boolean>() {

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
			protected Boolean doInBackground(Void... params) {
				int count;
				try {
					URL url = new URL(link);
					URLConnection conexion = url.openConnection();
					conexion.connect();
					int lenghtOfFile = conexion.getContentLength();

					InputStream input = new BufferedInputStream(url.openStream());

					File f = new File(android.os.Environment.getExternalStorageDirectory() + "/mrg");
					if (!f.exists())
						f.mkdirs();

					OutputStream output = new FileOutputStream(android.os.Environment.getExternalStorageDirectory() + "/mrg/" + nome + ".mp3");

					byte data[] = new byte[1024];

					long total = 0;

					while ((count = input.read(data)) != -1) {
						total += count;
						publishProgress((int)(total * 100 / lenghtOfFile));
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
					File f = new File(android.os.Environment.getExternalStorageDirectory() + "/mrg/" + nome + ".mp3");
					f.delete();
					return false;
				}
				return true;
			}

			protected void onProgressUpdate(Integer... progress) {
				dialog.setProgress(progress[0]);
			}

			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				if (result) {
					Toast.makeText(SelecionadoActivity.this, "Episódio baixado com sucesso!", Toast.LENGTH_LONG).show();
					episodio.setBaixado(true);
					epDao.update(episodio);
					download.setBackgroundResource(R.drawable.download_pressed);
				} else {
					Toast.makeText(SelecionadoActivity.this, "Ops, sua conexão com a internet falhou\nTente novamente!", Toast.LENGTH_LONG).show();
				}
			};

		}.execute();
	}

}