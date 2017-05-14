package com.framework.shiro;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.framework.utils.Exceptions;

/**
 * 验证码
 * 
 */
public class PatchcaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PatchcaServlet.class);

	private static int WIDTH = 110;
	private static int HEIGHT = 50;
	private static int MAX_LENGTH = 4;
	private static int MIN_LENGTH = 4;

	private static String CACHE_NAME = "_pcatcha_";
	private static String CACHE_FILE = "ehcache/ehcache-pcaptcha.xml";

	private static Cache cache;
	private static ConfigurableCaptchaService configurableCaptchaService;

	@Override
	public void init() throws ServletException {
		configurableCaptchaService = new ConfigurableCaptchaService();

		int width = NumberUtils.toInt(this.getInitParameter("width"), WIDTH);
		int height = NumberUtils.toInt(this.getInitParameter("height"), HEIGHT);
		configurableCaptchaService.setWidth(width);
		configurableCaptchaService.setHeight(height);

		int maxLength = NumberUtils.toInt(this.getInitParameter("maxLength"),
				MAX_LENGTH);
		int minLength = NumberUtils.toInt(this.getInitParameter("minLength"),
				MIN_LENGTH);
		RandomWordFactory randomWordFactory = new RandomWordFactory();
		randomWordFactory.setMaxLength(maxLength);
		randomWordFactory.setMinLength(minLength);

		configurableCaptchaService.setWordFactory(randomWordFactory);

		ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
		Resource resource = rpr.getResource("classpath:" + CACHE_FILE);

		try {
			CacheManager cacheManager = CacheManager.create(resource
					.getInputStream());

			cache = cacheManager.getCache(CACHE_NAME);
			if (cache == null) {
				Configuration configuration = ConfigurationFactory
						.parseConfiguration(resource.getInputStream());

				cache = new Cache(configuration.getCacheConfigurations().get(
						CACHE_NAME));
				if (cache == null) {
					cache = cacheManager.getCache(Cache.DEFAULT_CACHE_NAME);
				} else {
					cacheManager.addCache(cache);
				}
			}
		} catch (CacheException e) {
			LOGGER.error("创建缓存[" + CACHE_NAME + "]出错，请检查配置参数:"
					+ Exceptions.getStackTraceAsString(e));
		} catch (IOException e) {
			LOGGER.error("缓存配置文件[" + CACHE_FILE + "]读取出错:"
					+ Exceptions.getStackTraceAsString(e));
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 清除缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);

		// 显示类型
		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();
		String patchca = EncoderHelper.getChallangeAndWriteImage(
				configurableCaptchaService, "png", outputStream);

		// 放入缓存
		Element element = new Element(request.getSession().getId(), patchca);
		cache.put(element);

		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 检查验证码是否正确
	 * 
	 * @param req
	 * @return
	 */
	public static boolean validate(String sessionId, String code) {
		Element element = cache.get(sessionId);
		if (element != null) {
			String sourceCode = (String) element.getObjectValue();

			cache.remove(sessionId);
			return StringUtils.equalsIgnoreCase(code, sourceCode);
		}

		return false;
	}

	@Override
	public void destroy() {
		configurableCaptchaService = null;
		CacheManager.getInstance().removeCache(CACHE_NAME);
	}
}
