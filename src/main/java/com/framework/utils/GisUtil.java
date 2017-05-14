package com.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GisUtil {

	private final static Logger logger = LoggerFactory.getLogger(GisUtil.class);

	/**
	 * 根据GPS经纬度获取地址信息
	 * 
	 * @param latitude纬度
	 * @param longitude经度
	 * @return 国家|城市
	 */
	public static String geocoder(double latitude, double longitude) {
		// 根据GPS经纬度获取地址信息json串
		String url = "http://api.map.baidu.com/geocoder/v2/?ak=TVpNE84wNZTgXH6XaGqlC9jV&location=%s&output=json&pois=0";
		String regLocation = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(String.format(url, latitude + ","
					+ longitude));
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 解析json串
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode node = objectMapper.readTree(sb.toString()); // 将Json串以树状结构读入内存
				JsonNode result = node.get("result");// 得到result这个节点下的信息
				JsonNode location = result.get("location");
				JsonNode formatted_address = result.get("formatted_address");
				// logger.debug(formatted_address); // 读取节点下的某个子节点的值
				JsonNode addressComponent = result.get("addressComponent");
				// logger.debug(addressComponent.get("country"));
				String country = addressComponent.get("country").textValue();
				String city = addressComponent.get("city").textValue();
				regLocation = country;
				if (!StringUtils.isEmpty(city)) {
					regLocation += "|" + city;
				}
				httpget.abort();
			}
		} catch (Exception e) {
			e.printStackTrace();
			regLocation = "";
		} finally {
			return regLocation;
		}
	}
}
