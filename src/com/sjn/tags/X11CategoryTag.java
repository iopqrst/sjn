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

import com.sjn.model.LifeCategory;
import com.sjn.model.Timeline;
import com.sjn.thread.ParamInit;
import com.sjn.utils.DESUtil;
import com.sjn.utils.EhcacheFactoryUtils;

@SuppressWarnings("unused")
public class X11CategoryTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(X11CategoryTag.class);

	private String value; //对应的category id 
	private String fn; //对应两种方法，获取style 或者 获取name

	public int doStartTag() throws JspTagException {
		JspWriter out = pageContext.getOut();

		try {
			Object obj = EhcacheFactoryUtils.getInstance().get(
					EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_life_category + value);

			if (null != obj) {
				LifeCategory lc = (LifeCategory) obj;
				
				if("style".equals(fn)) {
					out.print(lc.getStr("style"));
				} else {
					out.print(lc.getStr("name"));
				}
				
			} else {
				out.print("");
			}
		} catch (IOException e) {
			e.printStackTrace();
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

}
