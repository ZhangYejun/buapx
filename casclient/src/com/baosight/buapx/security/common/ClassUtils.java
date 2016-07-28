package com.baosight.buapx.security.common;

import com.baosight.buapx.security.exception.ClassInstanceException;
import com.baosight.buapx.security.handler.IAuthPostHandler;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-12-28
 */
public class ClassUtils {
	public static Object getInstance(String className, Class inf) throws ClassInstanceException {
		Class clz = null;

		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		try {
			Class[] c = Class.forName(className).getInterfaces();
			boolean isInstanceof = false;
			for (int i = 0; i < c.length; i++) {
				if (c[i].equals(inf)) {
					isInstanceof = true;
					break;
				}
			}

			if (!isInstanceof)
				throw new ClassInstanceException("与传入类型不匹配!class:" + className + ",指定的接口:" + inf.getName());
			Object o = clzLoader == null ? Class.forName(className).newInstance() : clzLoader.loadClass(className).newInstance();
			return o;
		} catch (ClassNotFoundException e) {
			throw new ClassInstanceException(e.getMessage());
		} catch (InstantiationException e) {
			throw new ClassInstanceException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ClassInstanceException(e.getMessage());
		}
			

	}
	
	public static boolean isInstanceOf(Class inf,String targetClass) throws ClassInstanceException{
		Class[] c;
		try {
			c = Class.forName(targetClass).getInterfaces();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassInstanceException(e.getMessage());
		}
		boolean isInstanceof = false;
		for (int i = 0; i < c.length; i++) {
			if (c[i].equals(inf)) {
				isInstanceof = true;
				break;
			}
		}
		return isInstanceof;
	}
	
/*	public static boolean isInstanceOf(Class inf,String targetClass) throws ClassInstanceException{
		Object object;
		try {
			object= Class.forName(targetClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassInstanceException(e.getMessage());
		}
		return inf.isAssignableFrom(object.getClass());
	}*/
	
	
	public static void main(String args[]) throws ClassInstanceException{
		System.out.println(ClassUtils.isInstanceOf(IAuthPostHandler.class, "com.baosight.buapx.security.handler.TestSpringHandler"));
	}

}
