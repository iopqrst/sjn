/*******************************第一阶段*******************************/
1、注册发送验证码时判断手机号是否已经注册，已注册的手机号发送短信失败【待验证】
2、记住登录密码
3、忘记密码【做吗？】
4、很多字段的长度并没有做校验
5、修改密码、个人资料、winner settings
6、类别限制添加个数（V可以添加多个）


= bug
1、登录或者注册的时候，回车事件bug 【待验证】
2、在手机屏幕上登录或注册页面输入框页面被提示文字覆盖

3、登录、注册页面错误提示大屏时显示在页面的右面，但是手机屏幕小的时候就出现问题。
	怎么判断？
	http://stackoverflow.com/questions/11381673/javascript-solution-to-detect-mobile-browser

4、手机上时间轴页面点击保存弹出2次保存成功（今天没有重现，稍后在做测试）
5、弹出框在ipad的浏览器上会出现闪屏的效果


/*******************************第二阶段*******************************/
1、简单的统计（对天、月的统计）
2、(这里有个时间轴 弄成时间轴确实不错，两种形式自由转换，哈哈) http://www.cnblogs.com/sanshi/p/3229220.html
3、统计（一次做多查看1个月的报表）多了就不行，v可以查看更多（甚至导出数据）

/*******************************第三阶段*******************************/
1、添加计划（提醒、完成率等等）
2、完成统计



js 自动发布的问题目前只是一个临时方案，后期需要继续改进。

