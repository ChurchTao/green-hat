package com.greenhat.jdbc;

import com.greenhat.orm.Query;

/**
 * Created by jiacheng on 2017/8/10.
 */
public class BaseDAO<T> implements DAO<T> {
    Class<T> tClass;
    String entityName;
    String keyField;

    public String getKeyField() {
        return keyField;
    }

    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public boolean save(T t) {
        return Query.insert(t);
    }

    @Override
    public boolean delete(T t,String pkName) {
        return Query.delete(t,pkName);
    }

    @Override
    public T get(int id,String pkName) {
        return Query.select(tClass,pkName+"=?",id);
    }

    @Override
    public boolean update(T t,String pkName) {
        return Query.update(t,pkName);
    }
}
