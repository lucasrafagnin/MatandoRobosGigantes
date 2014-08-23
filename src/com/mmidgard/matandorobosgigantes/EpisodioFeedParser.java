package com.mmidgard.matandorobosgigantes;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xml.sax.Attributes;

import android.annotation.SuppressLint;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import android.util.Xml.Encoding;

import com.mmidgard.matandorobosgigantes.entity.Episodio;

@SuppressLint("SimpleDateFormat")
public class EpisodioFeedParser extends BaseFeedParser {

	private Episodio episode;

	public EpisodioFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Episodio> parse() {
		episode = new Episodio();
		final List<Episodio> episodes = new ArrayList<Episodio>();

		InputStream istream = getInputStream();

		RootElement root = new RootElement("rss");
		Element channel = root.requireChild("channel");

		Element item = channel.requireChild("item");
		Element enclosure = item.requireChild("enclosure");

		enclosure.setStartElementListener(new StartElementListener() {
			@Override
			public void start(Attributes attributes) {
				String episodeUrl = attributes.getValue("", "url");
				episode.setLink(episodeUrl);
			}
		});

		item.getChild("title").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				episode.setTitle(body);
			}
		});

		item.getChild("link").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				episode.setLinkSite(body);
			}
		});

		item.getChild("pubDate").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				Date formatedDate;
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss", Locale.ENGLISH);
					formatedDate = sdf.parse(body);
					episode.setPubDate(formatedDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		item.getChild("http://purl.org/rss/1.0/modules/content/", "encoded").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				// Log.i("AKI", body);
				// nao sera usado no momento, pois a maioria dos episodios nao
				// tem as imgs, e os que tem o link nao funciona mais
			}
		});

		item.getChild("http://www.itunes.com/dtds/podcast-1.0.dtd", "summary").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				episode.setDescription(body);
			}
		});

		item.getChild("http://www.itunes.com/dtds/podcast-1.0.dtd", "duration").setEndTextElementListener(new EndTextElementListener() {
			@Override
			public void end(String body) {
				episode.setDuration(body);
			}
		});

		item.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				episodes.add(episode);
				episode = new Episodio();
			}
		});

		try {
			Xml.parse(istream, Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return episodes;
	}
}