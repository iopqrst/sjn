package com.sjn.model;

import com.jfinal.plugin.activerecord.Model;

public class Winner extends Model<Winner> {

	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_NORMAL = 0; 
	public static final int STATUS_FORBIDDEN = 1;

	public static final Winner dao = new Winner();

}
