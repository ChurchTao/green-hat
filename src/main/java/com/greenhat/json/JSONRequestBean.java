package com.greenhat.json;

import java.util.Map;


public class JSONRequestBean {

	private final String serviceId;
	private final String method;
	private final Object[] parameters;
	private final Map<String,Object> properties;
	
	public JSONRequestBean(String serviceId,String method,Object[] parameters,Map<String,Object> properties){
		this.serviceId = serviceId;
		this.method = method;
		this.parameters = parameters;
		this.properties = properties;
	}
	
	public JSONRequestBean(String serviceId,String method,Object[] parameters){
		this(serviceId,method,parameters,null);
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public String getMethod() {
		return method;
	}

	public Object[] getParameters() {
		return parameters;
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
}
