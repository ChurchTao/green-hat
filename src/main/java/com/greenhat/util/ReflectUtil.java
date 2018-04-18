package com.greenhat.util;

import com.greenhat.aop.AspectProxy;
import com.greenhat.mvc.fault.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ReflectUtil {

	private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
	public static Object invokeMehod(Object bean, Method method,
			Object... args) {
		try {
			Class<?>[] types = method.getParameterTypes();

			int argCount = args == null ? 0 : args.length;

			
			// 转参数类型
			for (int i = 0; i < argCount; i++) {
				args[i] = cast(args[i], types[i]);
			}

			return method.invoke(bean, args);
		} catch (Exception e) {
			if (e.getCause().getClass().getSimpleName().equals(ServerException.class.getSimpleName())){
				throw new ServerException(e.getCause());
			}else {
				logger.error("反射赋值到方法参数失败，请检查参数个数，类型，顺序是否与入参一致,{}",e);
			}
		}
		return null;
	}
	
	/** 类型转换 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object value, Class<T> type) {
		if (value != null && !type.isAssignableFrom(value.getClass())) {
			if (is(type, int.class, Integer.class)) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (is(type, long.class, Long.class)) {
				value = Long.parseLong(String.valueOf(value));
			} else if (is(type, float.class, Float.class)) {
				value = Float.parseFloat(String.valueOf(value));
			} else if (is(type, double.class, Double.class)) {
				value = Double.parseDouble(String.valueOf(value));
			} else if (is(type, boolean.class, Boolean.class)) {
				value = Boolean.parseBoolean(String.valueOf(value));
			} else if (is(type, String.class)) {
				value = String.valueOf(value);
			}
		}
		return (T) value;
	}
	
	/** 对象是否其中一个 */
	public static boolean is(Object obj, Object... mybe) {
		if (obj != null && mybe != null) {
			for (Object mb : mybe)
				if (obj.equals(mb))
					return true;
		}
		return false;
	}
	
}
