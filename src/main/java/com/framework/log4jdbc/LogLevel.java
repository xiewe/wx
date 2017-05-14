package com.framework.log4jdbc;

/**
 * 日志级别
 */

public enum LogLevel {
	TRACE("TRACE"),

	DEBUG("DEBUG"),

	INFO("INFO"),

	WARN("WARN"),

	ERROR("ERROR");

	private String value;

	LogLevel(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
