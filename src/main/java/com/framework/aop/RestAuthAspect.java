package com.framework.aop;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.entity.GeneralResponseData;
import com.framework.service.RedisService;

@Component
@Aspect
public class RestAuthAspect {

	private static final Logger logger = LoggerFactory
			.getLogger(RestAuthAspect.class);

	/**
	 * 国际化语言与翻译语言映射关系, key : loacle 字符串 ，例：zh_CN value: 百度翻譯的語言
	 */
	public static final Map<String, String> LOCALE2LANG = new HashMap<String, String>() {
		{
			put("zh_CN", "zh");// 中文
			put("en_US", "en");// 英文
			put("ar_SA", "ara");// 阿拉伯语
			put("es_ES", "spa");// 西班牙语
			put("fr_FR", "fra");// 法语
			put("my_MM", "my");// 缅甸语，百度不支持此语种翻译
			put("ru_RU", "ru");// 俄语
			put("th_TH", "th");// 泰文
			put("ja_JP", "jp");// 日语
			put("ko_KR", "kor");// 韩语
		}
	};

	@Autowired
	private RedisService redisService;

	private ObjectMapper mapper = new ObjectMapper();

	@Pointcut("execution(* com.bizv2.controller.rest..*.*(..))")
	private void pointCutMethod() {
	}

	@SuppressWarnings("unused")
	@Around("pointCutMethod()")
	public Object doAuthentication(ProceedingJoinPoint point) throws Throwable {

		GeneralResponseData<String> ret = new GeneralResponseData<String>();

		Object[] args = point.getArgs();
		if (args.length < 3) {
			ret.setStatus(1);
			ret.setErrCode("1001");
			ret.setErrMsg("REST_REQ_PARAMETERS_MISSING_MSG");
			return mapper.writeValueAsString(ret);
		}

		String uid = (String) args[0];
		String token = (String) args[1];
		String lang = (String) args[2];

		if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token)) {
			ret.setStatus(1);
			ret.setErrCode("1001");
			ret.setErrMsg("REST_REQ_PARAMETERS_NULL_MSG");
			return mapper.writeValueAsString(ret);
		}

		if (!redisService.checkToken(uid, token)) {
			// if (false) {
			ret.setStatus(1);
			ret.setErrCode("1002");
			ret.setErrMsg("REST_REQ_AUTH_FAILED_MSG");
			return mapper.writeValueAsString(ret);
		}

		if (lang == null) {
			args[2] = "zh";
		} else {
			if (LOCALE2LANG.get(lang) != null) {
				args[2] = LOCALE2LANG.get(lang);
			} else {
				args[2] = "zh";
			}
		}

		Object returnValue = point.proceed(args);

		return returnValue;
	}

}