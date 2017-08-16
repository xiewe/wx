package com.framework.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.framework.AppConstants;
import com.framework.entity.SysUser;
import com.framework.exception.IncorrectCaptchaException;
import com.framework.shiro.CaptchaUsernamePasswordToken;
import com.framework.utils.Exceptions;
import com.framework.utils.PropertiesUtil;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LocaleResolver localeResolver;
    private static final String LOGIN_PAGE = "login";
    private static final String LOGIN_DIALOG = "login";

    @RequestMapping(method = RequestMethod.GET)
    public String login(Map<String, Object> map) {
        Cookie[] cookies = request.getCookies();
        String sessionid = "";
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if ("JSESSIONID".equals(cookies[i].getName())) {
                    sessionid = cookies[i].getValue();
                    break;
                }
            }
        }

        String inmos = PropertiesUtil.getInstance().getKeyValue("inoms");
        if (!"1".equals(inmos)) {
            return LOGIN_PAGE;
        }

        String loginUsername = isLogined(sessionid);
        LOG.debug("[login]sessionid:" + sessionid + " - loginUsername:" + loginUsername);
        if (!StringUtils.isEmpty(sessionid) && StringUtils.isNotEmpty(loginUsername)) {
            SecurityUtils.getSubject().login(
                    new CaptchaUsernamePasswordToken("oms", "123456", false, null, loginUsername));
            ((SysUser) SecurityUtils.getSubject().getPrincipal()).setIp(request.getRemoteAddr());
            ((SysUser) SecurityUtils.getSubject().getPrincipal()).setUserAgent(request.getHeader("User-Agent"));
            return "redirect:/index";
        }

        map.put("inoms", inmos);
        return LOGIN_PAGE;
    }

    private String isLogined(String sessionid) {
        String bret = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(AppConstants.AUTHRIZATION_URL + "=" + sessionid);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                bret = EntityUtils.toString(entity);
                bret = bret.substring(0, bret.length() - 1);
                LOG.debug("[isLogined] entity:" + bret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bret;
    }

    @RequestMapping(value = "/timeout", method = { RequestMethod.GET })
    public String timeout() {
        return LOGIN_DIALOG;
    }

    @RequestMapping(value = "/timeout/success", method = { RequestMethod.GET })
    public @ResponseBody String timeoutSuccess() {
        return "login success";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username,
            Map<String, Object> map, ServletRequest request) {

        String msg = parseException(request);

        map.put("msg", msg);
        map.put("username", username);

        return LOGIN_PAGE;
    }

    private String parseException(ServletRequest request) {
        String errorString = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        Class<?> error = null;
        try {
            if (errorString != null) {
                error = Class.forName(errorString);
            }
        } catch (ClassNotFoundException e) {
            LOG.error(Exceptions.getStackTraceAsString(e));
        }

        String msg = "其他错误！";
        if (error != null) {
            if (error.equals(UnknownAccountException.class))
                msg = "未知帐号错误！";
            else if (error.equals(IncorrectCredentialsException.class))
                msg = "密码错误！";
            else if (error.equals(IncorrectCaptchaException.class))
                msg = "验证码错误！";
            else if (error.equals(AuthenticationException.class))
                msg = "认证失败！";
            else if (error.equals(DisabledAccountException.class))
                msg = "账号被冻结！";
        }

        return "登录失败，" + msg;
    }

    @RequestMapping("/changeLocale/{language}")
    public String changeLocale(@PathVariable String language, HttpServletRequest request, HttpServletResponse response) {
        String[] lang = language.split("_");
        if (null != lang && lang.length >= 2) {
            Locale locale = new Locale(lang[0], lang[1]);
            // LocaleResolver localeResolver = RequestContextUtils
            // .getLocaleResolver(request);
            localeResolver.setLocale(request, response, locale);
        }
        return LOGIN_PAGE;
    }
}
