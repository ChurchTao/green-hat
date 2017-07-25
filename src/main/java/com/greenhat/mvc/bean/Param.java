package com.greenhat.mvc.bean;


import com.greenhat.util.CastUtil;
import com.greenhat.util.MapUtil;

import java.util.Map;

/**
 * Created by jiacheng on 2017/7/19.
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    public String getString(String name) {
        return CastUtil.castString(get(name));
    }

    public double getDouble(String name) {
        return CastUtil.castDouble(get(name));
    }

    public long getLong(String name) {
        return CastUtil.castLong(get(name));
    }

    public int getInt(String name) {
        return CastUtil.castInt(get(name));
    }

    public Object get(String name) {
        return paramMap.get(name);
    }

    public Map<String,Object> getMap(){
         return paramMap;
     }

     public boolean isEmpty(){
        return MapUtil.isEmpty(paramMap);
     }
}
