<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="S_category_pop_dialog" class="modal fade" style="display:none;">

	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">您自己定义的类别：</h4>
			</div>
			<div class="modal-body">
				<div id="S_cat_list" style="height:220px; overflow-y: scroll; width:100%;">
					<table class="table table-bordered table-hover responsive-utilities text-center">
						<thead>
							<tr>
								<th>序号</th>
								<th>类别</th>
								<th>样式</th>
							</tr>
						</thead>
						<tbody id="S_cat_tbody">
						</tbody>
					</table>
				</div>
				<br/>
				<form role="form" class="form-horizontal">
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">类别：</label>
						<div class="col-sm-6 col-md-8">
							<input type="text" class="form-control" name="name" autocomplete="off">
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">背景颜色：</label>
						<div class="col-sm-6 col-md-8">
							<input type="text" name="bgColor" class="pickColor form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">字体颜色：</label>
						<div class="col-sm-6 col-md-8">
						<input type="text" name="fontColor" class="pickColor form-control"/>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-4 col-md-3 text-right">实际效果：</label>
						<div class="col-sm-6 col-md-8">
						<span id="S_reflect"></span>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关&nbsp;&nbsp;闭</button>
				<button type="button" class="btn btn-primary" id="S_pop_cat_save">保&nbsp;&nbsp;存</button>
			</div>
		</div>
	</div>
	
</div>