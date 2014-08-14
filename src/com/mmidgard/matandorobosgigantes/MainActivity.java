package com.mmidgard.matandorobosgigantes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO teste streaming: OK
		// try {
		// String url =
		// "http://jovemnerd.com.br/podpress_trac/web/100144/0/MRG230_Transformers4.mp3";
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// mediaPlayer.setDataSource(url);
		// mediaPlayer.prepare();
		// mediaPlayer.start();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// new AsyncTask<Void, Void, Void>() {
		// @Override
		// protected Void doInBackground(Void... params) {
		// new ReaderRSS().percorrerFeed();
		// return null;
		// }
		// }.execute();

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					URL url = new URL("http://jovemnerd.com.br/categoria/matando-robos-gigantes/feed/");
					RssFeed feed = RssReader.read(url);

					ArrayList<RssItem> rssItems = feed.getRssItems();
					for (RssItem rssItem : rssItems) {
						Log.i("RSS Reader", rssItem.getTitle());
						Log.i("Link: ", rssItem.getLink());
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();

	}

}
