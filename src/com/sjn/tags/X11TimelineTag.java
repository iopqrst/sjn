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

import com.sjn.model.Timeline;
import com.sjn.thread.ParamInit;
import com.sjn.utils.DESUtil;
import com.sjn.utils.EhcacheFactoryUtils;

@SuppressWarnings("unused")
public class X11TimelineTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(X11TimelineTag.class);

	private String val;

	public int doStartTag() throws JspTagException {
		JspWriter out = pageContext.getOut();

		try {
			Object obj = EhcacheFactoryUtils.getInstance().get(
					EhcacheFactoryUtils.cache_name_system,
					ParamInit.cacheStart_timeline + val);

			if (null != obj) {
				Timeline tl = (Timeline) obj;
				out.print(tl.getStr("timePolt"));
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

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
