package com.greenhat.json;

import java.util.HashMap;
import java.util.Map;

public class JSONResponseBean {
	private int code = 200;
	private String msg;
	private Object body;
	private Map<String,Object> properties;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Object getBody() {
		return body;
	}
	
	public void setBody(Object body) {
		this.body = body;
	}
	
	public Map<String,Object> getProperties() {
		return properties;
	}
	
	public Object getProperty(String nm){
		if(properties == null || properties.isEmpty()){
			return null;
		}
		return properties.get(nm);
	}
	
	public void setProperty(String name,Object val){
		if(properties == null){
			properties = new HashMap<>();
		}
		properties.put(name, val);
	}
}
