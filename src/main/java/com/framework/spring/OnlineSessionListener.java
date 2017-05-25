package com.framework.spring;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineSessionListener implements HttpSessionListener {
	private final static Logger log = LoggerFactory.getLogger(OnlineSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		log.debug("OnlineSessionListener sessionCreated");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		log.debug("OnlineSessionListener sessionDestroyed");
	}
}
