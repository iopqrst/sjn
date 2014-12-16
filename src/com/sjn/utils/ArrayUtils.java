package com.sjn.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组操作类
 * @author houzhiqing
 *
 */
public class ArrayUtils {
	
	public static final String DEFAULT_SPLIT = ",";
	
	public static boolean isEmpty(Object[] obj) {
		return (null == obj || obj.length == 0);
	}
	
	/**
	 * 默认以“,"分割
	 * @param obj
	 * @return
	 */
	public static String join(Object[] obj) {
		return join(obj,DEFAULT_SPLIT);
	}
	
	/**
	 * 将数组元素按照一定格式分割成字符串
	 * 
	 * <b>请注意：</b>
	 * 
	 * List<String> b = new ArrayList<String>();
	 * b.add(null);
	 * 
	 * b.size() == 1 --> [null]
	 * 
	 * 所以在使用join之前保证List集合中没有null值，否则会出现字符类型的null
	 * 
	 * @param obj 数组
	 * @param split 分隔符
	 */
	public static String join(Object[] obj,String split) {
		StringBuffer sb = new StringBuffer();
		if(null != obj && obj.length > 0) {
			for(int i = 0 ; i < obj.length ; i++) {
				sb.append(obj[i]);
				if(i != obj.length - 1) {
					sb.append(split);
				}
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		System.out.println(join(list.toArray(),","));
	}

}
