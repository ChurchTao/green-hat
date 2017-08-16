package com.greenhat.jdbc.bean;

/**
 * Created by jiacheng on 2017/8/9.
 */
public class QueryAction {
    private String action; //get find update insert
    private String table;
    private String condition; //条件  byXXX
    private int limit;
    private int start;
    private String orderBy;

    private QueryAction(String action, String table, String condition) {
        this.action = action;
        this.table = table;
        this.condition = condition;
    }

    private QueryAction(String action, String table, String condition, int limit, int start) {
        this.action = action;
        this.table = table;
        this.condition = condition;
        this.limit = limit;
        this.start = start;
    }

    private QueryAction(String action, String table, String condition, String orderBy) {
        this.action = action;
        this.table = table;
        this.condition = condition;
        this.orderBy = orderBy;
    }

    private QueryAction(String action, String table, String condition, int limit, int start, String orderBy) {
        this.action = action;
        this.table = table;
        this.condition = condition;
        this.limit = limit;
        this.start = start;
        this.orderBy = orderBy;
    }

    public QueryAction create(String action, String table, String condition) {
        return new QueryAction(action, table, condition);
    }

    public QueryAction createByOrder(String action, String table, String condition, String orderBy) {
        return new QueryAction(action, table, condition, orderBy);
    }

    public QueryAction createByLimit(String action, String table, String condition, int limit, int start) {
        return new QueryAction(action, table, condition, limit, start);
    }

    public QueryAction create(String action, String table, String condition, int limit, int start, String orderBy) {
        return new QueryAction(action, table, condition, limit, start, orderBy);
    }

    public String getAction() {
        return action;
    }

    public String getTable() {
        return table;
    }

    public String getCondition() {
        return condition;
    }

    public int getLimit() {
        return limit;
    }

    public int getStart() {
        return start;
    }

    public String getOrderBy() {
        return orderBy;
    }

}
