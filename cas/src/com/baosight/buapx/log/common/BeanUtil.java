package com.baosight.buapx.log.common;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

public class BeanUtil {
public static Map<String,Object> beanToMap(Object bean){
	BeanMap beanMap=new BeanMap(bean);
	Map<String,Object> result=new HashMap<String, Object>();
	for(Object key:beanMap.keySet()){
		result.put((String)key, beanMap.get(key));
	}
	result.remove("class");
	return result;
}

public static <T extends Object> T mapToBean(Map<String,Object> map,T bean) throws IllegalAccessException, InvocationTargetException{
	BeanUtils.populate(bean,map);
	return bean;
}
}
