package com.baosight.buapx.security.properties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-12-29
 */
public class PropertiesManagerContainer {
	private static Map map = new HashMap();
	private static String fileName;
	public synchronized static Object getProperties(Class cls) throws IOException, DocumentException{
		if(fileName == null)
			throw new RuntimeException("PropertiesManagerContainer尚未初始化，");
		if(map.containsKey(cls)){
			Object o = map.get(cls);
			return  o;
			
		}else{
			Object o = null;
		    o = BuapxClientProperties.getProperties(fileName);			
			if(o == null)
				throw new RuntimeException("无法获得"+cls.getName());
			map.put(cls, o);
			return o;
		}
		
	}
	 
	public synchronized static void init(final String _fileName){
		if(fileName != null)
			throw new RuntimeException("已经被初始化，无法再次初始化!");
		fileName = _fileName;
		map = new HashMap(3);
	}
	
	

}
