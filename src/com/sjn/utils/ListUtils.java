package com.sjn.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public class ListUtils {

	public static List getFiledList(List list, String filed) {
		if (null == list || list.size() == 0)
			return null;
		List filedList = new ArrayList();
		try {

			for (Object obj : list) {
				Class clazz = obj.getClass();// 获取集合中的对象类型
				Field[] fds = clazz.getDeclaredFields();// 获取他的字段数组
				for (Field field : fds) {// 遍历该数组
					String fdname = field.getName();// 得到字段名，

					if ((field.getModifiers() & Modifier.STATIC) == java.lang.reflect.Modifier.STATIC) {
						// System.out.println("----" + field );
					} else { // 对于静态变量不做处理
						Method method = clazz.getMethod("get" + change(fdname),
								null);// 根据字段名找到对应的get方法，null表示无参数

						if (null != method && filed.equals(fdname)) {
							Object val = method.invoke(obj, null);
							filedList.add(val);
						}
					}

				}
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return filedList;
	}

	/**
	 * @param src
	 *            源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	public static String change(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}

	/**
	 * 去除重复元素，不适合对象
	 * 
	 * @param list
	 */
	public static void removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		System.out.println(list);
	}

	public static void main(String[] args) {
		List<Student> list = new ArrayList<Student>();

		Student s1 = new Student();
		s1.setId(1);
		s1.setName("name1");
		s1.setAddress("address1");
		list.add(s1);

		Student s2 = new Student();
		s2.setId(2);
		s2.setName("name2");
		s2.setAddress("address2");
		list.add(s2);

		Student s3 = new Student();
		s3.setId(3);
		s3.setName("name3");
		s3.setAddress("address3");
		list.add(s3);

		Student s4 = new Student();
		s4.setId(4);
		s4.setName("name4");
		s4.setAddress("address4");
		list.add(s4);

		List filedList = getFiledList(list, "address");
		for (int i = 0; i < filedList.size(); i++) {
			System.out.println(filedList.get(i));
		}

		List<String> slist = new ArrayList<String>();
		slist.add("aaa");
		slist.add("aaa");
		slist.add("aaa");
		slist.add("bb");
		slist.add("bb");
		slist.add("侯志清");
		slist.add("侯志清");
		slist.add("侯志清2");

		removeDuplicate(slist);
	}
}

class Student {

	public static final int TYPE = 1;

	private int id;
	private String name;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
