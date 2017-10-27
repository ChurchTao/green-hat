package com.greenhat.jdbc.DAOFactory;

import com.greenhat.orm.EntityHelper;
import com.greenhat.orm.Query;

/**
 * Created by jiacheng on 2017/8/10.
 */
public class BaseDAO<T> implements DAO<T> {
    private Class<T> tClass;
    private String entityName;
    private String keyField;

    public void settClass(Class<T> tClass) {
        this.tClass = tClass;
    }

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
    public boolean delete(T t) {
        return Query.delete(t);
    }

    @Override
    public T get(Object... object) {
        StringBuilder condition = new StringBuilder();
        boolean isFirst = true;
        for (String pkName : EntityHelper.getPkName_Table(tClass)) {
            if (isFirst) {
                condition.append(pkName).append(" = ?");
                isFirst = false;
            } else {
                condition.append(" and ").append(pkName).append("= ?");
            }
        }
        return Query.select(tClass,condition.toString(),object);
    }

    @Override
    public boolean update(T t) {
        return Query.update(t);
    }
}
