<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="S_timeLineDialog" class="modal fade">
	
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title" id="S_timeline_title"></h4>
			</div>
			<div class="modal-body clearfix">
				<div id="S_lingchen" class="time_polt_split">
					<h5>凌晨：</h5>
					<ul>
						<c:forEach var="tl" items="${timeLineList}">
							<c:if test="${tl.type == 0 }">
							<li><a id="a_tl_<fmt:formatNumber pattern="#.#" value="${tl.val }"/>" href="javascript:void(0);">${tl.timePolt }</a></li>
							</c:if>	
						</c:forEach>
					</ul>
				</div>
				<div id="S_shangwu" class="time_polt_split">
					<h5>上午：</h5>
					<ul>
						<c:forEach var="tl" items="${timeLineList}">
							<c:if test="${tl.type == 1 }">
							<li><a id="a_tl_<fmt:formatNumber pattern="#.#" value="${tl.val }"/>" href="javascript:void(0);">${tl.timePolt }</a></li>
							</c:if>	
						</c:forEach>
					</ul>
				</div>
				<div id="S_zhongwu" class="time_polt_split">
					<h5>中午：</h5>
					<ul>
						<c:forEach var="tl" items="${timeLineList}">
							<c:if test="${tl.type == 2 }">
							<li><a id="a_tl_<fmt:formatNumber pattern="#.#" value="${tl.val }"/>" href="javascript:void(0);">${tl.timePolt }</a></li>
							</c:if>	
						</c:forEach>
					</ul>
				</div>
				<div id="S_xiawu" class="time_polt_split">
					<h5>下午：</h5>
					<ul>
						<c:forEach var="tl" items="${timeLineList}">
							<c:if test="${tl.type == 3 }">
							<li><a id="a_tl_<fmt:formatNumber pattern="#.#" value="${tl.val }"/>" href="javascript:void(0);">${tl.timePolt }</a></li>
							</c:if>	
						</c:forEach>
					</ul>
				</div>
				<div id="S_wanshang" class="time_polt_split">
					<h5>晚上：</h5>
					<ul>
						<c:forEach var="tl" items="${timeLineList}">
							<c:if test="${tl.type == 4 }">
							<li><a id="a_tl_<fmt:formatNumber pattern="#.#" value="${tl.val }"/>" href="javascript:void(0);">${tl.timePolt }</a></li>
							</c:if>	
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关&nbsp;&nbsp;闭</button>
				<button type="button" class="btn btn-primary" id="S_timeline_clear">清&nbsp;&nbsp;空</button>
			</div>
		</div>
	</div>

</div>