<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*"%>
<%!Color getRandColor(int min, int max) { //随机产生指定区域内的RGB颜色
		Random random1 = new Random();
		if (min >= 255)
			min = 255;
		if (max >= 255)
			max = 255;
		int r = min + random1.nextInt(max - min);
		int g = min + random1.nextInt(max - min);
		int b = min + random1.nextInt(max - min);
		return new Color(r, g, b);
	}%>
<%
	//禁止页面缓冲
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	//在缓存中创建图形对象，然后输出
	int width = 60, height = 20; //输出图片的大小
	BufferedImage buff = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB); //指定缓冲的图片和颜色结构
	Graphics g = buff.getGraphics();
	Random rand = new Random();
	//背景色,200-250色段
	g.setColor(getRandColor(200, 250));
	g.fillRect(0, 0, width, height);
	//设置字体
	g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
	//画出30条随机干扰线，160-200色段
	g.setColor(getRandColor(160, 200)); //在循环外面干扰线颜色一样
	for (int i = 1; i <= 30; i++) {
		int x = rand.nextInt(width); //线条的起始位置
		int y = rand.nextInt(height);
		int tx = rand.nextInt(12);
		int ty = rand.nextInt(12);
		g.drawLine(x, y, x + tx, y + ty);
	}
	//随机产生4个验证码
	String coding = ""; //保存得到的验证码字符串
	for (int i = 0; i < 4; i++) {
		String temp = String.valueOf(rand.nextInt(10)); //0-9的数字
		coding += temp;
		//显示验证码,20-140色段
		g.setColor(getRandColor(20, 140));
		g.drawString(temp, 13 * i + 6, 16);
	}
	//信息存入session
	session.setAttribute("LOGIN_RAND", coding);
	//图象生成，显示到页面
	g.dispose();
	ServletOutputStream sos = response.getOutputStream();
	ImageIO.write(buff, "jpeg", sos);
	sos.flush(); //强行将缓冲区的内容输入到页面
	sos.close();
	sos = null;
	response.flushBuffer();
	out.clear();
	//Return a new BodyContent object, save the current "out" JspWriter, and update the value of the "out" attribute in the page scope attribute namespace of the PageContext
	out = pageContext.pushBody();
%>