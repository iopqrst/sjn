define(function(require, exports, module){
	var $ = require('jquery');
	var bb = require('bootbox');
			require('colorPicker');
	
	var _DEL_CID = 0;//要删除的类别id
	var _PAGE_SIZE = 5;
	
	/**
	 * 绑定颜色筛选器，和文本框改变事件（实时修改要添加的样式）
	 */
	function _bindCategoryEvent () {
		
		$(".pickColor").colourPicker({title:false});
		
		var changeCat = function(){
			$("#S_reflect").text($(this).val());					
		};
		
		$(":input[name='name']").keydown(changeCat);
		$(":input[name='name']").change(changeCat);
		
		$(":input[name='bgColor']").change(function(){
			$("#S_reflect").css({'background':$(this).val()});					
		});
		
		$(":input[name='fontColor']").change(function(){
			$("#S_reflect").css({'color':$(this).val()});					
		});
	};
	
	exports.udefineCat = function(){
		
		$(function(){
			$("#S_cself").on('click', _openCategoryDialog); //添加自己的类别
			$("#S_pop_cat_save").on('click', _saveCategory);
			
			_bindCategoryEvent();
		});
		
		/**
		 * @introduction 打开自己类别的弹出框，我在想要不要吧列表页展示出来呢？
		 */
		function _openCategoryDialog(){
			$("#S_category_pop_dialog").modal('show');
			_querySelfCat();
			return;
		};

		/**
		 * 查询用户自定义的类别
		 */
		function _querySelfCat() {
			$.blockUI();
			$.ajax({
				type : 'post',
				url : '/cat/querySelfCat',
				dataType : 'json',
				data : null,
				success : function(result, status) {
					
					if(result && result.len > 0) {
						var atr = [];
						var cat = $.parseJSON(result.data);
						
						for(var i = 0; i < cat.length; i++) {
							atr.push('<tr>');
							atr.push('<td class="">' + (i+1) + '</td>');
							atr.push('<td>' + cat[i].name + '</td>');
							atr.push('<td> <span style="' + cat[i].style +'">设定的样式</span> </td>');
							atr.push('</tr>');
						}
						
						$("#S_cat_tbody").html(atr.join(''));
					}
					
					$.unblockUI();
				},
				error : function(e) {
					$.unblockUI();
				}
			});
		}

		/**
		 * @function 保存在弹出框中填写的类别
		 */
		function _saveCategory() {
			
			var name = $(":input[name='name']").val();
			var bgColor = $(":input[name='bgColor']").val();
			var fontColor = $(":input[name='fontColor']").val();
			
			if(!name || '' == $.trim(name)) {
				bb.alert('请填写类别名称');
				return false;
			} 
			
			$.blockUI();
			$.ajax({
				type : 'post',
				url : '/cat/save',
				dataType : 'json',
				data : {name: $.trim(name), bgColor: bgColor, fontColor: fontColor,s:'life', t: new Date().getTime()},
				success : function(result, status) {
					
					if(result && result.status == 'succ') {
						//这里应该让下拉列表中选中
						$("#S_item_category").append('<option style='+ result.style +' selected="selected" value="'+ result.id +'">'+ result.name +'</option>');
						$('#S_item_category').selectpicker('refresh');
						$("#S_category_pop_dialog").modal('hide');
					}
					
					$.unblockUI();
				},
				error : function(e) {
					$.unblockUI();
				}
			});
		};
	};
	
	exports.list = function(){
		require('blockUI');
		require('pagination');
		
		$(function(){
			$("#S_category_page_add").on('click', {type:'add'}, _openPageCategoryPop);//打开弹出框
			$("a[id^='S_cat_modify_']").on('click', {type:'modify'}, _openPageCategoryPop);//打开弹出框
			$("#S_page_cat_save").on('click', _savePageCategory);//保存或修改
			
			$("a[id^='S_cat_del_']").click(_delCategory);
			$("#S_btn_replace_cat").on('click', _replaceAndDelCat); //替换按钮事件
			
			
			_bindCategoryEvent();
		});
		
		/***
		 * 删除类别事件
		 * 
		 * 类别分为两种情况：
		 * 1、直接可以删除的
		 * 2、需要修改使用类别项目后才可以删除
		 */
		function _delCategory(){
			
			var _id = $(this).attr("id");
			_DEL_CID = 0; 
			
			if(_id) {
				$.blockUI();
				_DEL_CID = _id.substring(10);
				$.ajax({
					type : 'post',
					url : '/cat/del/' + _id.substring(10) + '-' + new Date().getTime() ,
					dataType : 'json',
					data : null,
					success : function(result, status) {
						
						if(!result) {
							$.unblockUI();
							return;
						};
						
						if('used' === result.status) {

							var useCount = result.count;
							
							bb.confirm('在您已有的记录里中有' + useCount + '条数据正在使用该类别，'+
									'删除将意味着让它们无家可归，但是你可以将使用该类别的数据替换为其他类别，是否要替换？', function(result){
								
								if(result) {
									_openItemDialog();
								} 
								
							});
							
						} else if('succ' == result.status){
							bb.alert('类别删除成功');
							window.location.href = '/cat';
						}
						$.unblockUI();
					},
					error : function(e) {
						$.unblockUI();
					}
				});
				
			} else {
				bb.alert('参数错误，无法删除相应的类别');
			}
			
		};
		
		function _openItemDialog() {
			_queryItemByCat();
			
			$("#S_del_cat_dialog").modal('show');
			return;
		}
		
		/**
		 * @function 根据类别查询使用该类别的所有items
		 */
		function _queryItemByCat(){
			
			if(0 == _DEL_CID) return;
			
			var params = {category: _DEL_CID , pageSize: _PAGE_SIZE, time: new Date()};
			$.blockUI();
			$.ajax({   
		        type: "post",    
		        url: '/life/queryByCat',  
		        data: params,
		        dataType: 'json',
				success: function(result,textStatus){
					if("" == result) {
						$.unblockUI();
						return;
					};

				  	var total = result.total;
				  	
				  	if(typeof(total) == "undefined"  || 0 == total){
				  		$('#Pagination').html("");
				  		$('#data').html(result);
				  	}else{
				  		_reflectTableData(result);
					  	
					  	$("#Pagination").pagination(total, {
							num_edge_entries: 3, //边缘页数
							num_display_entries: 3, //主体页数
							callback: pageselectCallback,
							items_per_page: _PAGE_SIZE, //每页显示10项
							prev_text: "上一页",
							next_text: "下一页"
						});
				  	}
				  	
				  	$.unblockUI();
				},
				error: function(result){
					$.unblockUI();
				}
			});
		};
		
		/**
		 * 分页回调函数
		 */
		function pageselectCallback(page_index, jq){
			if(0 == _DEL_CID) return;
			
			var params = {category: _DEL_CID ,pageNo: (page_index + 1), pageSize: _PAGE_SIZE, time: new Date()};
			$.blockUI();
			$.ajax({   
				type: "post",    
		        url: '/life/queryByCat',  
		        data: params,
		        dataType: 'json',                         
				success: function(result,textStatus){
					if("" == result) {
						$.unblockUI();
						return;
					};
					
				  	var total = result.total;
				  	if(typeof(total) == "undefined"  || 0 == total){
				  		$('#Pagination').html("");
				  		$('#data').html(result);
				  	}else{
				  		_reflectTableData(result);
				  	}
				  	
				  	$.unblockUI();
			  	},
				error: function(result){
			  		$.unblockUI();
				}
			});
			
		    return false;
		};
		
		/**
		 * 构建表格
		 */
		function _reflectTableData(result) {
			if(result.data){
				var list = result.data;
				var array = new Array();
				if(list instanceof Array){
					array.push("<table class='table table-bordered responsive-utilities text-center'>");
					array.push("<tr><th>开始时间</th><th>结束时间</th><th>花费时间</th><th>日期</th></tr>");
					for(var i = 0; i < list.length ; i++){
						var item = list[i];
						array.push("<tr>");
						array.push("<td>" + item.begin + "</td>");
						array.push("<td>" + item.end + "</td>");
						array.push("<td>" + item.interval + "</td>");
						array.push("<td>" + item.whichDay + "</td>");
						array.push("</tr>");
					}
					array.push("</table>");
				} else {
					// do something	
				}
		
				$("#data").html(array.join(""));
			}
			
		};
		
		/**
		 * 替换和删除类别
		 */
		function _replaceAndDelCat() {
			if(0 == _DEL_CID) return;
			
			bb.confirm('确定将列表中项目的类别替换为你选中的类别吗？', function(result) {
				if(result) {
					var replaceCat = $("select[name='replaceCat']").val();
					
					if(replaceCat) {
						
						$.blockUI();
						$.ajax({   
					        type: "post",    
					        url: '/cat/replaceAndDelCat/' + replaceCat + '-' + _DEL_CID + '-' + new Date().getTime(),  //要被替换成值/删除的值
					        data: '',
					        dataType: 'json',
							success: function(result,textStatus){
								if("" == result) {
									$.unblockUI();
									return;
								}
								
								if(result.s && result.s > 0) {
									bb.alert('类别替换删除成功');
									window.location.href = '/cat';
								}
								
							  	$.unblockUI();
							},
							error: function(result){
								$.unblockUI();
							}
						});
					} else {
						bb.alert('请选择一个要替换的类别');
					}
				}
			});
			
		};
		
		/**
		 * 打开页面的添加修改类别弹出框
		 */
		function _openPageCategoryPop(e) {
			var $pop = $("#S_category_page_dialog");
			if('add' === e.data.type) {
				//添加操作
				$(":input", $pop).val('').css({'background':''});
				$("#S_reflect").empty();
				
				$pop.data('catId', '');
			} else if('modify' === e.data.type) {
				//修改操作
				_queryCategoryById($(this).attr("id").substr(13));
			}
			
			$pop.modal('show');
		}
		
		/**
		 * 根据id查询类别
		 */
		function _queryCategoryById (catId) {
			
			if(!catId) { return ;}
			
			$.blockUI();
			$.ajax({
				type : 'post',
				url : '/cat/modify/' + catId + '-' + new Date().getTime(),
				dataType : 'json',
				data : {},
				success : function(result, status) {
					if(result) {
						if('succ' === result.status) {
							var $pop = $("#S_category_page_dialog");
							
							$(":input[name='name']",$pop).val(result.name);
							$(":input[name='bgColor']",$pop).val(result.bgColor || '').css({'background':(result.bgColor || '')});
							$(":input[name='fontColor']",$pop).val(result.fontColor || '').css({'background':(result.fontColor || '')});
							
							$("#S_reflect").text(result.name).css({'background': (result.bgColor || ''), 
								'fontColor': (result.fontColor || '')});
							
							$pop.data('catId', result.cid);
							
						} else if('parmas' === result.status) {
							bb.alert('参数信息错误');
						}
					} else {
						bb.alert('没有查到相关信息');
					}
					$.unblockUI();
				},
				error : function(e) {
					$.unblockUI();
				}
			});

		}
		
		/**
		 * 保存在弹出框中填写的类别（list 页面）
		 */
		function _savePageCategory() {
			
			var name = $(":input[name='name']").val();
			var bgColor = $(":input[name='bgColor']").val();
			var fontColor = $(":input[name='fontColor']").val();
			
			if(!name || '' == $.trim(name)) {
				bb.alert('请填写类别名称');
				return false;
			} 
			
			var _url; //声明url
			var cid = $("#S_category_page_dialog").data("catId");
			
			if(cid && '' != cid) {
				_url = '/cat/update/' + cid + '-' + new Date().getTime();
			} else {
				_url = '/cat/save/' + new Date().getTime();
			}

			$.blockUI();
			$.ajax({
				type : 'post',
				url : _url,
				dataType : 'json',
				data : {name: $.trim(name), bgColor: bgColor, fontColor: fontColor},
				success : function(result, status) {
					
					if(!result) {
						$.unblockUI();
						return;
					};
					
					if(result.status == 'succ') {
						$("#S_category_page_dialog").modal('hide');
						window.location['href'] = '/cat';
					} else if('params' === result.status) {
						bb.alert('参数信息不正确');
					}
					
					$.unblockUI();
				},
				error : function(e) {
					$.unblockUI();
				}
			});
		};

	};
	
});