package com.mmidgard.matandorobosgigantes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;
import android.util.Xml.Encoding;

public class EpisodioFeedParser extends BaseFeedParser {

	private Episodio episode;

	protected EpisodioFeedParser(String feedUrl) {
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

		// TODO definir uma forma para buscar a descricao do podcast(episodio)
		// item.getChild("itunes").setEndTextElementListener(new
		// EndTextElementListener() {
		// @Override
		// public void end(String body) {
		// episode.setDescription(body);
		// }
		// });

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
			throw new RuntimeException(e);
		}

		return episodes;
	}
}