package com.framework.entity;

/**
 * 国际化封装，通用实体属性以JSON存储多语言值
 * 
 * @author dengyong
 *
 */
public class I18N {

	/**
	 * 国家语言KEY，例：zh_CN
	 */
	private String k;
	/**
	 * 国家语言KEY对应的值
	 */
	private String v;

	public I18N() {

	}

	public I18N(String key, String value) {
		this.k = key;
		this.v = value;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	/**
	 * 不允许添加相同语言
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof I18N) {
			return this.k.equals(((I18N) o).k);
		}
		return false;
	}

}
