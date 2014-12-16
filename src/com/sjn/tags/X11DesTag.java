package com.sjn.tags;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sjn.utils.DESUtil;

@SuppressWarnings("unused")
public class X11DesTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(X11DesTag.class);

	private String value;
	/**
	 * decrypt/encrypt
	 */
	private String fn;
	private String keys;

	public int doStartTag() throws JspTagException {
		JspWriter out = pageContext.getOut();
		try {
			DESUtil des = null;
			if(!StringUtils.isEmpty(keys)) {
				des = new DESUtil(keys);
			} else {
				des = new DESUtil();
			}
			
			String r = "";
			
			if("encrypt".equals(fn)) {
				r = des.encrypt(value);
			} else if("decrypt".equals(fn)) {
				r = des.decrypt(value);
			}
			out.print(r);
		} catch (Exception e) {
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspTagException {
		try {
			if (bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
		} catch (IOException e) {
			throw new JspTagException("IO ERROR:" + e.getMessage());
		}
		return EVAL_PAGE;
	}

	public void doInitBody() throws JspTagException {
	}

	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

}
