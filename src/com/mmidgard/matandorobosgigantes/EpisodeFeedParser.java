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

public class EpisodeFeedParser extends BaseFeedParser {

	private Episode episode;

	protected EpisodeFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Episode> parse() {
		episode = new Episode();
		final List<Episode> episodes = new ArrayList<Episode>();

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

		item.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				episodes.add(episode);
				episode = new Episode();
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