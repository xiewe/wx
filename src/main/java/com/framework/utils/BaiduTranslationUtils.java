package com.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.utils.PropertiesUtil;

public class BaiduTranslationUtils {
    private static final Logger logger = LoggerFactory.getLogger(BaiduTranslationUtils.class);

    private static final String UTF8 = "utf-8";

    // 申请者开发者id，实际使用时请修改成开发者自己的appid
    private static final String appId = PropertiesUtil.getInstance().getKeyValue("BAIDU_APP_ID");

    // 申请成功后的证书token，实际使用时请修改成开发者自己的token
    private static final String token = PropertiesUtil.getInstance().getKeyValue("BAIDU_APP_SECRET");

    private static final String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    // 随机数，用于生成md5值，开发者使用时请激活下边第四行代码
    private static final Random random = new Random();

    public static String translate(String q, String from, String to) {
        // 用于md5加密
        int salt = random.nextInt(10000);
        // 本演示使用指定的随机数为1435660288
        // int salt = 1435660288;

        // 对appId+源文+随机数+token计算md5值
        StringBuilder md5String = new StringBuilder();
        md5String.append(appId).append(q).append(salt).append(token);
        String md5 = DigestUtils.md5Hex(md5String.toString());

        // 使用Post方式，组装参数
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("q", q));
        nvps.add(new BasicNameValuePair("from", from));
        nvps.add(new BasicNameValuePair("to", to));
        nvps.add(new BasicNameValuePair("appid", appId));
        nvps.add(new BasicNameValuePair("salt", String.valueOf(salt)));
        nvps.add(new BasicNameValuePair("sign", md5));
        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        try {
            // 创建httpclient链接，并执行
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpost);

            // 对于返回实体进行解析
            HttpEntity entity = response.getEntity();
            InputStream returnStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(returnStream, UTF8));
            StringBuilder result = new StringBuilder();
            String str = null;
            while ((str = reader.readLine()) != null) {
                result.append(str).append("\n");
            }

            // 转化为json对象，注：Json解析的jar包可选其它
            JSONObject resultJson = new JSONObject(result.toString());

            // 开发者自行处理错误，本示例失败返回为null
            try {
                String error_code = resultJson.getString("error_code");
                if (error_code != null) {
                    logger.error("[BaiduTranslationUtils] q: " + q + ", from:" + from + ", to:" + to);
                    logger.error("[BaiduTranslationUtils] error_code: " + error_code);
                    logger.error("[BaiduTranslationUtils] error_msg: " + resultJson.getString("error_msg"));
                    return null;
                }
            } catch (Exception e) {
            }

            // 获取返回翻译结果
            JSONArray array = (JSONArray) resultJson.get("trans_result");
            JSONObject dst = (JSONObject) array.get(0);
            String text = dst.getString("dst");
            text = URLDecoder.decode(text, UTF8);

            return text;
        } catch (ClientProtocolException e1) {
            logger.error("[BaiduTranslationUtils] ClientProtocolException: " + e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e1) {
            logger.error("[BaiduTranslationUtils] IOException: " + e1.getMessage());
            e1.printStackTrace();
        } catch (Exception e1) {
            logger.error("[BaiduTranslationUtils] Exception: " + e1.getMessage());
            e1.printStackTrace();
        }

        return null;
    }

    public static String translateHtml(String q, String from, String to) {
        char[] chars = q.toCharArray();
        String result = null;
        try {
            StringBuffer tmpRes = new StringBuffer();
            StringBuffer tmpSB = new StringBuffer();
            boolean enclosed = true;
            for (int i = 0; i < chars.length; i++) {
                if (enclosed && "<".equals(String.valueOf(chars[i]))) {
                    enclosed = false;

                    // trans
                    if (StringUtils.isNotEmpty(tmpSB.toString())) {
                        tmpRes.append(translate(tmpSB.toString(), from, to));
                    }
                    tmpSB.setLength(0);
                }

                if (!enclosed && ">".equals(String.valueOf(chars[i]))) {
                    enclosed = true;

                }

                if (!enclosed) {
                    tmpRes.append(chars[i]);
                } else {
                    if (">".equals(String.valueOf(chars[i]))) {
                        tmpRes.append(chars[i]);
                    } else {
                        tmpSB.append(chars[i]);
                    }
                }
            }

            if (StringUtils.isNotEmpty(tmpSB)) {
                tmpRes.append(translate(tmpSB.toString(), from, to));
            }

            result = tmpRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        String q = "贴子标题<h3 class=\"am-gallery-title\">贴子标题</h3>";
        String result = BaiduTranslationUtils.translateHtml(q, "auto", "th");

        System.out.println("====" + result);
    }

}
