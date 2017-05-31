package com.framework.widget;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import magick.CompositeOperator;
import magick.CompressionType;
import magick.DrawInfo;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;
import magick.PreviewType;

/**
 * 图片压缩组件,使用前提要安装ImageMagick
 * 
 * @author dengyong
 *
 */
public class JMagick {
	private final static Logger logger = LoggerFactory.getLogger(JMagick.class);

	static {
		// 要把libJMagick.so拷贝到/usr/lib 目录下,不能漏掉这个，不然jmagick.jar的路径找不到
		// 直接用System.out.println(System.getProperty("java.library.path")).
		// 打印出当前环境的路径，然后再把jmagick.dll或jmagick.so 复制到其中的一个路径文件夹中问题解决。
		System.setProperty("jmagick.systemclassloader", "no");
	}

	/**
	 * 压缩图片,创建缩略图
	 * 
	 * @param filePath
	 *            源文件路径
	 * @param toPath
	 *            缩略图路径
	 */
	public static void createThumbnail(String filePath, String toPath)  {
		ImageInfo info = null;
		MagickImage image = null;
		Dimension imageDim = null;
		MagickImage scaled = null;
		try {
			info = new ImageInfo(filePath);
			image = new MagickImage(info);
			imageDim = image.getDimension();
			int wideth = imageDim.width;
			int height = imageDim.height;
			if (wideth > height) {
				height = 660 * height / wideth;
				wideth = 660;
			}
			// scaled = image.scaleImage(wideth, height);// 小图片文件的大小.
			scaled = image.scaleImage(90, 90);
			scaled.setFileName(toPath);
			scaled.writeImage(info);
			logger.info("Create Thumbnail from: " + filePath + ",to:" + toPath);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (scaled != null) {
				scaled.destroyImages();
			}
		}
	}

	/**
	 * 水印(图片logo)
	 * 
	 * @param filePath
	 *            源文件路径
	 * @param toImg
	 *            修改图路径
	 * @param logoPath
	 *            logo图路径
	 * @throws MagickException
	 */
	public static void initLogoImg(String filePath, String toImg, String logoPath) throws MagickException {
		ImageInfo info = new ImageInfo();
		MagickImage fImage = null;
		MagickImage sImage = null;
		MagickImage fLogo = null;
		MagickImage sLogo = null;
		Dimension imageDim = null;
		Dimension logoDim = null;
		try {
			fImage = new MagickImage(new ImageInfo(filePath));
			imageDim = fImage.getDimension();
			int width = imageDim.width;
			int height = imageDim.height;
			if (width > 660) {
				height = 660 * height / width;
				width = 660;
			}
			sImage = fImage.scaleImage(width, height);

			fLogo = new MagickImage(new ImageInfo(logoPath));
			logoDim = fLogo.getDimension();
			int lw = width / 8;
			int lh = logoDim.height * lw / logoDim.width;
			sLogo = fLogo.scaleImage(lw, lh);

			sImage.compositeImage(CompositeOperator.AtopCompositeOp, sLogo, width - (lw + lh / 10),
					height - (lh + lh / 10));
			sImage.setFileName(toImg);
			sImage.writeImage(info);
		} finally {
			if (sImage != null) {
				sImage.destroyImages();
			}
		}
	}

	/**
	 * 水印(文字)
	 * 
	 * @param filePath
	 *            源文件路径
	 * @param toImg
	 *            修改图路径
	 * @param text
	 *            名字(文字内容自己随意)
	 * @throws MagickException
	 */
	public static void initTextToImg(String filePath, String toImg, String text) throws MagickException {
		ImageInfo info = new ImageInfo(filePath);
		if (filePath.toUpperCase().endsWith("JPG") || filePath.toUpperCase().endsWith("JPEG")) {
			info.setCompression(CompressionType.JPEGCompression); // 压缩类别为JPEG格式
			info.setPreviewType(PreviewType.JPEGPreview); // 预览格式为JPEG格式
			info.setQuality(95);
		}
		MagickImage aImage = new MagickImage(info);
		Dimension imageDim = aImage.getDimension();
		int wideth = imageDim.width;
		int height = imageDim.height;
		if (wideth > 660) {
			height = 660 * height / wideth;
			wideth = 660;
		}
		int a = 0;
		int b = 0;
		String[] as = text.split("");
		for (String string : as) {
			if (string.matches("[\u4E00-\u9FA5]")) {
				a++;
			}
			if (string.matches("[a-zA-Z0-9]")) {
				b++;
			}
		}
		int tl = a * 12 + b * 6 + 300;
		MagickImage scaled = aImage.scaleImage(wideth, height);
		if (wideth > tl && height > 5) {
			DrawInfo aInfo = new DrawInfo(info);
			aInfo.setFill(PixelPacket.queryColorDatabase("white"));
			aInfo.setUnderColor(new PixelPacket(0, 0, 0, 100));
			aInfo.setPointsize(12);
			// 解决中文乱码问题,自己可以去随意定义个自己喜欢字体，我在这用的微软雅黑
			String fontPath = "C:/WINDOWS/Fonts/MSYH.TTF";
			// String fontPath = "/usr/maindata/MSYH.TTF";
			aInfo.setFont(fontPath);
			aInfo.setTextAntialias(true);
			aInfo.setOpacity(0);
			aInfo.setText("　" + text + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "，版权归作者所有！");
			aInfo.setGeometry("+" + (wideth - tl) + "+" + (height - 5));
			scaled.annotateImage(aInfo);
		}
		scaled.setFileName(toImg);
		scaled.writeImage(info);
		scaled.destroyImages();
	}

	/**
	 * 切图
	 * 
	 * @param imgPath
	 *            源图路径
	 * @param toPath
	 *            修改图路径
	 * @param w
	 * @param h
	 * @param x
	 * @param y
	 * @throws MagickException
	 */
	public static void cutImg(String imgPath, String toPath, int w, int h, int x, int y) throws MagickException {
		ImageInfo infoS = null;
		MagickImage image = null;
		MagickImage cropped = null;
		Rectangle rect = null;
		try {
			infoS = new ImageInfo(imgPath);
			image = new MagickImage(infoS);
			rect = new Rectangle(x, y, w, h);
			cropped = image.cropImage(rect);
			cropped.setFileName(toPath);
			cropped.writeImage(infoS);

		} finally {
			if (cropped != null) {
				cropped.destroyImages();
			}
		}
	}

	public static void main(String[] args) {

	}
}
