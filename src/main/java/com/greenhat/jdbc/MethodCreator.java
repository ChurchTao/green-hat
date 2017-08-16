package com.greenhat.jdbc;

import javassist.CtClass;

import java.util.Map;

/**
 * Created by jiacheng on 2017/8/10.
 */
public class MethodCreator {

    public static String createMethod(CreateWay type, Map<String, Object> queryMap) {
        String baseMethod = createBase(queryMap);
        switch (type) {
            case get:
                return createMethodByGet(baseMethod, queryMap);
            case sql:
                return createMethodBySql(baseMethod, queryMap);
            case find:
                return createMethodByFind(baseMethod, queryMap);
            case query:
                return createMethodByQuery(baseMethod, queryMap);
            case delete:
                return createMethodByDelete(baseMethod, queryMap);
            case insert:
                return createMethodByInsert(baseMethod, queryMap);
            default:
                return "";
        }
    }

    /**
     * Base 自动装填 MethodName  returnType  Exceptions  还剩下{ @{args} @{methodBody} }
     */
    private static String createBase(Map<String, Object> queryMap) {
        String methodName = queryMap.get("methodName").toString();
        String returnType = queryMap.get("returnType").toString();
        CtClass[] exceptions = (CtClass[]) queryMap.get("exceptions");
        String resultStr = "public @{returnType} @{methodName}(@{args}) @{throws}{@{methodBody}}";
        resultStr = resultStr.replace("@{returnType}", returnType);
        resultStr = resultStr.replace("@{methodName}", methodName);
        if (exceptions.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("throws ");
            for (int i = 0; i < exceptions.length; i++) {
                builder.append(exceptions[i].getName());
                if (exceptions.length - i > 1) builder.append(",");
            }
            resultStr = resultStr.replace("@{throws}", builder.toString());
        } else {
            resultStr = resultStr.replace("@{throws}", "");
        }
        return resultStr;
    }

    private static String createMethodByGet(String baseMethod, Map<String, Object> queryMap) {
        CtClass[] argsType = (CtClass[]) queryMap.get("args");
        Class<?> entityClass = (Class<?>) queryMap.get("entityClass");
        boolean isList = (boolean) queryMap.get("isList");
        String resultMethod;
        resultMethod = baseMethod.replace("@{args}", getArgs(argsType));
        String params = getArgsArray(argsType);
        StringBuilder body = new StringBuilder();
        String fieldName = queryMap.get("fieldName").toString();
        if (isList) {
            body.append("return Query.selectList(")
                    .append(entityClass.getName())
                    .append(".class,\"").append(fieldName)
                    .append(" = ? \"")
                    .append(",params").append(");");
        }else {
            body.append("return Query.select(")
                    .append(entityClass.getName())
                    .append(".class,\"").append(fieldName)
                    .append(" = ? \"")
                    .append(",params").append(");");
        }
        return resultMethod.replace("@{methodBody}", body.toString());
    }

    /**
     * 通过SQL语句自定义查询
     */
    private static String createMethodBySql(String baseMethod, Map<String, Object> queryMap) {
        CtClass[] argsType = (CtClass[]) queryMap.get("args");
        String returnType = queryMap.get("returnType").toString();
        String sql = queryMap.get("sqlField").toString();
        Class<?> entityClass = (Class<?>) queryMap.get("entityClass");
        boolean isList = (boolean) queryMap.get("isList");
        String resultMethod;
        resultMethod = baseMethod.replace("@{args}", getArgs(argsType));
        String params = getArgsArray(argsType);
        StringBuilder body = new StringBuilder();
        body.append(params);
        if (isList) {
            body.append("return DatabaseLoader.queryEntityList(")
                    .append(entityClass.getName()).append(".class,").append(sql).append(",params")
                    .append(");");
        } else if (returnType.equals("void")) {
            body.append("DatabaseLoader.update(")
                    .append(sql).append(",params")
                    .append(")");
        } else if (resultMethod.equals("boolean")) {
            body.append("boolean result = DatabaseLoader.update(")
                    .append(sql).append(",params")
                    .append(")");
        } else {
            body.append("return DatabaseLoader.queryEntity(")
                    .append(entityClass.getName()).append(".class,").append(sql).append(",params").append(");");
        }
        return resultMethod.replace("@{methodBody}", body.toString());
    }

    private static String createMethodByFind(String baseMethod, Map<String, Object> queryMap) {
        CtClass[] argsType = (CtClass[]) queryMap.get("args");
        Class<?> entityClass = (Class<?>) queryMap.get("entityClass");
        String resultMethod;
        resultMethod = baseMethod.replace("@{args}", getArgs(argsType));
        String params = getArgsArray(argsType);
        StringBuilder body = new StringBuilder();
        body.append(params);
        String fieldName = queryMap.get("fieldName").toString();
        String orderBy = queryMap.get("orderBy").toString();
        int limit = (int) queryMap.get("limit");
        if (orderBy.equals("")) {
            body.append("return Query.selectList(")
                    .append(entityClass.getName())
                    .append(".class,\"").append(fieldName)
                    .append(" = ? \"")
                    .append(",params")
                    .append(");");
        } else {
            body.append("return Query.selectList(1,")
                    .append(limit).append(",")
                    .append(entityClass.getName())
                    .append(".class,\"").append(fieldName)
                    .append(" = ? \",\"").append(orderBy)
                    .append("\",params")
                    .append(");");
        }
        return resultMethod.replace("@{methodBody}", body.toString());
    }

    private static String createMethodByQuery(String baseMethod, Map<String, Object> queryMap) {
        return "";
    }

    private static String createMethodByDelete(String baseMethod, Map<String, Object> queryMap) {
        return "";
    }

    private static String createMethodByInsert(String baseMethod, Map<String, Object> queryMap) {
        return "";
    }


    private static String getArgs(CtClass[] argsType) {
        String args = "";
        if (argsType != null && argsType.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < argsType.length; i++) {
                builder.append(argsType[i].getName()).append(" ")
                        .append("arg").append(i);
                if (argsType.length - i > 1) builder.append(",");
            }
            args = builder.toString();
        }
        return args;
    }
    private static String getArgsArray(CtClass[] argsType){
        String params = "";
        if (argsType != null && argsType.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("Object[] params = new Object[").append(argsType.length).append("];");
            for (int i = 0; i < argsType.length; i++) {
                builder.append("params[").append(i).append("] = arg").append(i).append(";");
            }
            params=builder.toString();
        }
        return params;
    }
}
