package com.mmidgard.matandorobosgigantes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser {
	final URL mFeedUrl;

	protected BaseFeedParser(String feedUrl) {
		try {
			mFeedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStream() {
		try {
			return mFeedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}
}
