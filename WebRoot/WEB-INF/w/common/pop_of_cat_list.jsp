<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x11" uri="/WEB-INF/tlds/x11.tld" %>
<%--------------------------添加修改弹出框------------------------------ --%>
<div id="S_category_page_dialog" class="modal fade"
	style="display: none;">

	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">
					自己定义类别：
				</h4>
			</div>
			<div class="modal-body">
				<form role="form" class="form-horizontal">
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">
							类别：
						</label>
						<div class="col-sm-6 col-md-8">
							<input type="text" class="form-control" name="name"
								autocomplete="off">
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">
							背景颜色：
						</label>
						<div class="col-sm-6 col-md-8">
							<input type="text" name="bgColor" class="pickColor form-control" />
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">
							字体颜色：
						</label>
						<div class="col-sm-6 col-md-8">
							<input type="text" name="fontColor"
								class="pickColor form-control" />
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">
							实际效果：
						</label>
						<div class="col-sm-6 col-md-8">
							<span id="S_reflect"></span>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					关&nbsp;&nbsp;闭
				</button>
				<button type="button" class="btn btn-primary" id="S_page_cat_save">
					保&nbsp;&nbsp;存
				</button>
			</div>
		</div>
	</div>

</div>
<%--删除替换类别时使用的弹出框 --%>
<div id="S_del_cat_dialog" class="modal fade" style="display: none">
	
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">
					删除替换类别：
				</h4>
			</div>
			<div class="modal-body">
				<div id="item_content" class="clear">
					<div id="data"></div>
					<div id="Pagination"></div>
				</div>
				<div id="S_replace_cat" class="form-group"
					style="padding-top: 5px;">
					<label for="S_item_category">
						替换为：
					</label>
					<select name="replaceCat" class="form-control selectpicker show-tick">
						<c:forEach var="cat" items="${catList}">
							<option style="${cat.style }"
								value="<x11:des fn="encrypt" value="${cat.id }"/>">${cat.name }</option>
						</c:forEach>
					</select>
				</div>
				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					关&nbsp;&nbsp;闭
				</button>
				<button type="button" class="btn btn-primary" id="S_btn_replace_cat">
					替&nbsp;&nbsp;换
				</button>
			</div>
		</div>
	</div>
	
</div>