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
	private boolean doubleClick = false;
	private boolean preparado = false;
	private Handler seekHandler = new Handler();
	private ProgressDialog dialog;
	private ProgressDialog dialog2;

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
		dialog2 = new ProgressDialog(this);

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
	}

	private void prepararPlayer() {
		if (!offline)
			streaming(episodio.getLink());
		else
			playLocal();
		progresso.setMax(mediaPlayer.getDuration());

		preparado = true;
	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() {
		try {
			if (mediaPlayer != null) {
				progresso.setProgress(mediaPlayer.getCurrentPosition());
				duracaoAtual.setText(getTimeString(mediaPlayer.getCurrentPosition()));
				seekHandler.postDelayed(run, 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				if (!preparado) {
					new AsyncTask<Void, Void, String>() {

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							if (!episodio.isBaixado()) {
								dialog.setIndeterminate(true);
								dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								dialog.setCancelable(false);
								dialog.setTitle("Aguarde...");
								dialog.setMessage("Fazendo o streaming do episódio");
								dialog.show();
							}
							play.setVisibility(View.GONE);
							pause.setVisibility(View.VISIBLE);
						}

						@Override
						protected String doInBackground(Void... params) {
							try {
								prepararPlayer();
								mediaPlayer.start();
							} catch (Exception e) {
								e.printStackTrace();
								return "Conecte-se à internet para ouvir o episódio";
							}
							return "";
						}

						protected void onPostExecute(String result) {
							if (!result.equals(""))
								Toast.makeText(SelecionadoActivity.this, result, Toast.LENGTH_SHORT).show();
							if (!episodio.isBaixado())
								dialog.dismiss();
							seekUpdation();
						};
					}.execute();
				} else {
					mediaPlayer.start();
					play.setVisibility(View.GONE);
					pause.setVisibility(View.VISIBLE);
				}
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

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naoDisponivel();
			}
		});

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				naoDisponivel();
			}
		});
	}

	private void naoDisponivel() {
		Toast.makeText(SelecionadoActivity.this, "Esta função estará disponível em breve", Toast.LENGTH_SHORT).show();
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
			episodio.setBaixado(false);
			epDao.update(episodio);
			download.setBackgroundResource(R.drawable.download);
			if (!offline)
				streaming(episodio.getLink());
		}
	}

	public void baixarEpisodio(final String link, final String nome) {
		new AsyncTask<Void, Integer, Boolean>() {

			@Override
			protected void onPreExecute() {
				dialog2.setIndeterminate(false);
				dialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				dialog2.setCancelable(false);
				dialog2.setTitle("Aguarde...");
				dialog2.setMessage("Baixando o episódio");
				dialog2.show();
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
				dialog2.setProgress(progress[0]);
			}

			protected void onPostExecute(Boolean result) {
				dialog2.dismiss();
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

	@Override
	public void onBackPressed() {
		if (mediaPlayer.isPlaying()) {
			if (doubleClick) {
				doubleClick = false;
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
				finish();
				return;
			}
			doubleClick = true;
			Toast.makeText(SelecionadoActivity.this, "Pressione novamente para sair\nIrá parar o episódio", Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					doubleClick = false;
				}
			}, 2000);
		} else {
			finish();
		}
	}

}
