package com.sjn.model;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Model基础类
 * 
 * @author 董华健
 * @param <M>
 */
@Deprecated
public abstract class BaseModel<M extends Model<M>> extends Model<M> {

	private static final long serialVersionUID = -900378319414539856L;

	private static Logger log = Logger.getLogger(BaseModel.class);

	/**
	 * 获取表映射对象
	 * 
	 * @return
	 */
	public Table getTable() {
		return TableMapping.me().getTable(getClass());
	}

	/**
	 * 获取主键值
	 * 
	 * @return
	 */
	public String getPrimaryKeyValue() {
		return this.getStr(getTable().getPrimaryKey());
	}
	
	//TODO HOW ?
	@SuppressWarnings("unchecked")
	public Model<M> getModel() {
		Model<M> m = null;
		try {
			m = (Model<M>) (Class.forName(getClass().getSimpleName())).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return m;
	}

}
