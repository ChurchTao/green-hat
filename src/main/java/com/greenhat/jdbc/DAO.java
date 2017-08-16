package com.greenhat.jdbc;

/**
 * Created by jiacheng on 2017/8/10.
 */
public interface DAO<T> {
    String getEntityName();
    String getKeyField();
    void setKeyField(String pkName);
    void setEntityName(String name);
    boolean save(T t);
    boolean delete(T t, String pkName);
    T get(int id, String pkName);
    boolean update(T t, String pkName);
}
