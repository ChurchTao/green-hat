package com.greenhat.jdbc;

import com.greenhat.annotation.DAOMethod;
import com.greenhat.util.ClassUtil;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiacheng on 2017/8/9.
 */
public class ActionParser {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final String DEFAULT_PARENT_CLASS_NAME = "com.greenhat.jdbc.BaseDAO";
    private static final Pattern findPattern3 = Pattern.compile("^(find|query|get|delete|remove)([A-Z][\\w$_]*)?By([A-Z][\\w$_]*)$");

    private static final ClassPool cpool = new ClassPool(true);
    static {
//        ClassUtil.loadClass(ClassPath.class.getName());
        cpool.appendClassPath(new LoaderClassPath(ClassUtil.getClassLoader()));
        cpool.importPackage("com.greenhat.jdbc");
        cpool.importPackage("com.greenhat.util");
        cpool.importPackage("com.greenhat.orm");
        cpool.importPackage("com.greenhat.test");
        cpool.importPackage("java.util");
    }

    public static <T> void createProxyDAOBean(String daoClass, Class<T> tClass) throws Exception {
        //获取到抽象类
        CtClass ct = cpool.get(daoClass);
        Class<?> entityClass = ClassUtil.getEntityClass(tClass);
        //为它动态生成实现类 XXXImpl
        StringBuilder className = new StringBuilder(daoClass);
        className.append("Impl").append(counter.incrementAndGet());
        CtClass cc = cpool.makeClass(className.toString());
        CtClass cp = cpool.get(DEFAULT_PARENT_CLASS_NAME);

        if (ct.isInterface()) {
            cc.addInterface(ct);
            cc.setSuperclass(cp);
        } else {
            if (ct.subtypeOf(cp)) {
                if (!Modifier.isAbstract(ct.getModifiers())) {
                    DAOFactory.register(daoClass, (BaseDAO) tClass.newInstance());
                    cc.detach();
                    return;
                }
                cc.setSuperclass(ct);
            } else {
                throw new IllegalStateException("dao[" + daoClass + "] must be subtype of[" + DEFAULT_PARENT_CLASS_NAME + "]");
            }
        }

        try {
            CtMethod[] methods = ct.getMethods();
            parseMethods(cc, methods, entityClass);
            BaseDAO dao = (BaseDAO) cc.toClass().newInstance();
            dao.settClass(entityClass);
            DAOFactory.register(daoClass, dao);
//            return dao;
        } catch (Exception e) {
            throw new IllegalStateException("daoClass[" + daoClass + "] create proxy entity failed.", e);
        } finally {
            cc.detach();
        }
    }

    private static void parseMethods(CtClass cc, CtMethod[] methods, Class<?> entityClass) throws Exception {
        for (CtMethod m : methods) {
            String fn = m.getName();
            if (!m.hasAnnotation(DAOMethod.class)) {
                continue;
            }
            DAOMethod an = (DAOMethod) m.getAnnotation(DAOMethod.class);
            if (!StringUtils.isEmpty(an.sql())) {
                createSqlMethod(cc, m, entityClass);
                continue;
            }

            String action;
            Matcher matcher = findPattern3.matcher(fn);
            if (matcher.find()) {
                action = matcher.group(1);
                String fieldName = StringUtils.uncapitalize(matcher.group(3));

                switch (action) {
                    case "find":
                    case "query":
                        createFind(cc, m, fieldName, entityClass);
                        break;
                    case "get":
                        createGet(cc, m, fieldName, entityClass);
                        break;
                    case "delete":
                    case "remove":
                        createDelete(cc, m, fieldName, entityClass);
                        break;
                }
            } else {
                throw new IllegalStateException("method[" + fn + "] is invalid.");
            }
        }
    }

    private static void createSqlField(CtClass cc, String fieldName, String sql) throws Exception {
        CtField ctField = new CtField(cpool.getCtClass("java.lang.String"), fieldName, cc);
        ctField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
        cc.addField(ctField, "\"" + sql + "\"");
    }

    private static void createSqlMethod(CtClass cc, CtMethod m, Class<?> entityClass) throws Exception {
        DAOMethod an = (DAOMethod) m.getAnnotation(DAOMethod.class);
        String sql = an.sql();
        String methodName = m.getName();
        String sqlFieldName = "sql_" + methodName;
        //获取注解中的sql语句 新建一个变量存起来
        createSqlField(cc, sqlFieldName, sql);
        Map<String, Object> ctx = new HashMap<>();
        putBase(entityClass, m, ctx);
        ctx.put("sqlField", sqlFieldName);
        createMethod(cc, m, CreateWay.sql, ctx);
    }

    private  static void createMethod(CtClass cc, CtMethod m, CreateWay type, Map<String, Object> ctx) throws Exception {
        try {
            String method = MethodCreator.createMethod(type, ctx);
            CtMethod cm = CtNewMethod.make(method, cc);
            copyAnnotations(cm, m);
            cc.addMethod(cm);

        } catch (CannotCompileException e) {
            throw new IllegalStateException("dao method[" + m.getName() + "] compile failed.", e);
        }
    }

    private static void copyAnnotations(CtMethod cm, CtMethod m) throws Exception {
        Object[] ans = m.getAnnotations();

        MethodInfo methodInfo = cm.getMethodInfo();
        ConstPool constpool = methodInfo.getConstPool();
        AnnotationsAttribute at = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
        for (Object a : ans) {
            if (a instanceof DAOMethod) {
                continue;
            }
            Annotation annot = new Annotation(a.toString().substring(1), constpool);
            at.addAnnotation(annot);
        }
        methodInfo.addAttribute(at);
    }


    private static void createGet(CtClass cc, CtMethod m, String fieldName, Class<?> entityClass) throws Exception {
        Map<String, Object> ctx = new HashMap<String, Object>();
        putBase(entityClass, m, ctx);
        ctx.put("fieldName", fieldName);
        createMethod(cc, m, CreateWay.get, ctx);
    }

    private static void createDelete(CtClass cc, CtMethod m, String fieldName, Class<?> entityClass) throws Exception {
        Map<String, Object> ctx = new HashMap<String, Object>();
        putBase(entityClass, m, ctx);
        ctx.put("fieldName", fieldName);
        createMethod(cc, m, CreateWay.delete, ctx);
    }

    private static void createFind(CtClass cc, CtMethod m, String fieldName, Class<?> entityClass) throws Exception {
        DAOMethod an = (DAOMethod) m.getAnnotation(DAOMethod.class);
        Map<String, Object> ctx = new HashMap<String, Object>();
        putBase(entityClass, m, ctx);
        ctx.put("fieldName", fieldName);
        ctx.put("orderBy", an.orderBy());
        ctx.put("limit", an.limit());
        createMethod(cc, m, CreateWay.find, ctx);
    }

    private static void putBase(Class<?> entityClass, CtMethod m, Map<String, Object> ctx) throws Exception {
        CtClass returnType = m.getReturnType();
        ctx.put("methodName", m.getName());
        ctx.put("returnType", returnType.getName());
        ctx.put("args", m.getParameterTypes());
        ctx.put("exceptions", m.getExceptionTypes());
        ctx.put("isList", returnType.getSimpleName().equals("List"));
        ctx.put("entityClass", entityClass);
    }

}
