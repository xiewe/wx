package com.framework.widget;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

public class FfmpegUtil {
	private final static Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

	public static void amrToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();
		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		try {
			encoder.encode(source, target, attrs);
			logger.debug(String.format("转码成功，源文件：%s , 目标文件：%s", sourcePath, targetPath));
		} catch (Exception e) {
			logger.error(String.format("转码异常，源文件：%s , 目标文件：%s", sourcePath, targetPath), e.getLocalizedMessage());
		}
	}
}
