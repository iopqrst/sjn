<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
<div class="footer">
	<div class="container">
		<p class="text-muted text-center">
			New shopping new life !
		</p>
	</div>
</div> --%>
<%
	//这段代码可以放在js那边，点击的时候在创建也不错？！
%>
<div id="S_timeline_setting_dialog" class="modal fade"
	style="display: none;">

	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">
					设置时间轴时间范围
				</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="s_201408071730">
						开始时间：
					</label>
					<select id="s_201408071730" name="sbeginPolt"
						class="form-control selectpicker show-tick">
						<option value="5">
							05:00
						</option>
						<option value="5.5">
							05:30
						</option>
						<option value="6">
							06:00
						</option>
						<option value="6.5">
							06:30
						</option>
						<option value="7">
							07:00
						</option>
						<option value="7.5">
							07:30
						</option>
						<option value="8">
							08:00
						</option>
					</select>
				</div>

				<div class="form-group">
					<label for="s_201408071732">
						结束时间：
					</label>
					<select id="s_201408071732" name="sendPolt"
						class="form-control selectpicker show-tick">
						<option value="0">
							00:00
						</option>
						<option value="23.5">
							23:30
						</option>
						<option value="23">
							23:00
						</option>
						<option value="22.5">
							22:30
						</option>
						<option value="22">
							22:00
						</option>
						<option value="21.5">
							21:30
						</option>
						<option value="21">
							21:00
						</option>
					</select>
				</div>
				<input type="hidden" name="iloveyou" />
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					关&nbsp;&nbsp;闭
				</button>
				<button type="button" class="btn btn-primary"
					id="S_pop_timeline_setting">
					保&nbsp;&nbsp;存
				</button>
			</div>
		</div>
	</div>

</div>
<script type="text/javascript" src="${jsVersion }/sea.js"></script>
<link href="/css/bootstrap-select.css" type="text/css" rel="stylesheet" />
