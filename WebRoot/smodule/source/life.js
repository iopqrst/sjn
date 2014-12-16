define(function(require, exports, module) {
	var $ = require('jquery');
			require('blockUI');
			require('ui');
			
	var bb = require('bootbox');
			
	exports.page = function(){
		$(function() {
			$("#S_beginPolt").on('click', triggerBeginPolt);
			$("#S_endPolt").on('click', triggerEndPolt);
			$("a[id^='a_tl_']").on('click', selectTimelinePolt); // 选择一个时间节点
			
			$("#S_cancel").on('click', back);

			$(":input[name='lifeItem.whichDay']").datepicker({
				showButtonPanel : true,
				maxDate : 0
			});
			
			$("#S_save").on('click', {type:'add'}, valid);
			$("#S_lm_update").on('click', {type:'modify'}, valid);
			$("#S_last_data").on('click', userLastData); //使用上次life's item数据
		});

		function triggerBeginPolt() {
			$("body").data("field", 'begin');
			timelineRange();
			openDialog('请选择开始时间');
		}

		function triggerEndPolt() {
			$("body").data("field", 'end');
			timelineRange();
			openDialog('请选择结束时间');
		}

		function openDialog(_title) {
			
			$('#S_timeline_title').html(_title);
			$('#S_timeLineDialog').modal('show');
			
			$('#S_timeline_clear').on('click',function(){
				var field = $("body").data("field");
				if ('begin' == field) {
					$(":input[name='lifeItem.beginPolt']").val('');
					$("#S_beginPolt").val('');
				} else {
					$(":input[name='lifeItem.endPolt']").val('');
					$("#S_endPolt").val('');
				}
				$('#S_timeLineDialog').modal('hide');
				$(this).off('click');//结束后取消点击事件
			});
			
			return;
		}

		// 选择一个时间节点
		function selectTimelinePolt() {

			var field = $("body").data("field");

			if ($(this).html()) {
				if ('begin' == field) {
					$("#S_beginPolt").val($(this).html());
				} else if ('end' == field) {
					$("#S_endPolt").val($(this).html());
				}
			}

			if (this.id) {
				var _val = this.id.substring(5);

				var $beginPolt = $(":input[name='lifeItem.beginPolt']");
				var $endPolt = $(":input[name='lifeItem.endPolt']");

				if ('begin' == field) {
					$beginPolt.val(_val);
				} else if ('end' == field) {
					$endPolt.val(_val);
				}

				$("body").data("field", '');
				//$("#S_timeLineDialog").dialog("close");
				$('#S_timeLineDialog').modal('hide');
			}

		}

		// 修改时间轴的可选范围
		function timelineRange() {

			$("a[id^='a_tl_']").on('click', selectTimelinePolt).removeClass(
					'disabled');

			var field = $("body").data('field');
			var beginPolt = $(":input[name='lifeItem.beginPolt']").val();
			var endPolt = $(":input[name='lifeItem.endPolt']").val();

			if ('' == beginPolt && '' == endPolt) {
				// 如果开始时间和结束时间都为空的话就没有必要向下进行了，随便选择就行。
				return;
			}

			$("a[id^='a_tl_']").each(
					function() {
						var poltVal = parseFloat(this.id.substring(5));
						var compareVal = 'begin' == field ? parseFloat(endPolt)
								: parseFloat(beginPolt);

						// console.info(compareVal);
						if ('begin' == field) {
							if (poltVal >= compareVal) {
								$(this).off('click').addClass('disabled');
							}
						}
						if ('end' == field) {
							// 判断一下开始时间是否超过了6点，如果超过6点了结束时间就可以选择0点，否则不能选择
							if (compareVal < 18) {
								if (poltVal <= compareVal) {
									$(this).off('click').addClass('disabled');
								}
							} else {
								if (poltVal != 0 && poltVal <= compareVal) {
									$(this).off('click').addClass('disabled');
								}
							}

						}

					});

		}
		
		function valid(e) {
			var fname, wday, type = e.data.type; //whichDay, type:当前的操作, fname： form's name
			
			if('add' == type) {
				wday = $(":input[name='lifeItem.whichDay']").val();
		
				if ($.trim(wday) == '' || null == wday) {
					bb.alert('日期怎么没有了');
					return false;
				}
				fname = 'ItemAddForm';
			} else {
				wday = $("#S_whichDay").val();
				fname = 'ItemModifyForm';
			}

			var bp = $(":input[name='lifeItem.beginPolt']").val();
			var ep = $(":input[name='lifeItem.endPolt']").val();

			if ($.trim(bp) == '' || $.trim(ep) == '') {
				return false;
			}

			bp = parseFloat(bp);
			ep = parseFloat(ep);

			if (bp == ep) {
				bb.alert('开始时间与结束时间相同了，是不是选错了？');
				return false;
			}

			ep = ep == 0 ? 24 : ep;
			if (bp > ep) {
				bb.alert('咦，开始时间大于结束时间，是不是选错了？');
				return false;
			}

			if ((ep - bp) % 0.5 != 0) {
				bb.alert('看来是同行呀！同是同行，相煎何急！');
				return false;
			}

			var $doWhat = $(":input[name='lifeItem.doWhat']");

			var params = {
					'lifeItem.beginPolt' : bp,
					'lifeItem.endPolt' : ep,
					'lifeItem.whichDay' : wday,
					'operate': type,
					'lid': $("body").data('lid')
			};
			
			queryExistItems(params, function(){
				document.forms[fname].submit();
			});

		}
		
		/**
		 * 查询某天某个时间段是否已经添加了时间 
		 */
		function queryExistItems(params, fn) {
			$.blockUI();
			$.ajax({
				type : 'post',
				url : '/life/existItems',
				dataType : 'json',
				data : params,
				success : function(result, status) {
					
					if(result.size == 0) {
						fn && fn();
					} else {
						var r = result.detail;
						var msg = [];
						msg.push('您当前填入的时间已经添加了');
						msg.push(result.size);
						msg.push('次，时间：');
						for(var d in r){
							msg.push(r[d].begin + '~' + r[d].end + ' 共计' + r[d].interval + '小时');
							msg.push('，');
						}
						msg.push('我觉得这不是你想要的，修改一下呗！');
						bb.alert(msg.join(''));
					}
					$.unblockUI();
				},
				error : function() {
					$.unblockUI();
				}
			});

		}
		
		/**
		 * 使用上次的数据（必须填写开始和结束时间段，根据时间节点查询上次的doWhat）
		 */
		function userLastData() {
			
			var bp = $(":input[name='lifeItem.beginPolt']").val();
			var ep = $(":input[name='lifeItem.endPolt']").val();
			
			if ('' == bp || '' == ep) {
				bb.alert('请选择时间节点后在进行该操作');
				return;
			}
			
			$.blockUI();
			$.ajax({
				type : 'post',
				url : '/life/queryLastData',
				dataType : 'json',
				data : {"lifeItem.beginPolt":bp, "lifeItem.endPolt":ep},
				success : function(result, status) {
					if(result.doWhat && result.category) {
						$(":input[name='lifeItem.doWhat']").val(result.doWhat);
						$(":input[name='category']").val(result.category);
					} else {
						bb.alert('没有查到相同时间节点的项目');
					}
					$.unblockUI();
				},
				error : function(e) {
					$.unblockUI();
				}
			});

		};
		
		function back() {
			window.location.href = '/life';
		}
	};
	
	exports.list = function(){
		$(":input[name='search_begin']").datepicker({
			showButtonPanel : true,
			maxDate : 0
		});
		
		$(":input[name='search_end']").datepicker({
			showButtonPanel : true,
			maxDate : 0
		});
		
		$(":input[name='search_submit']").on('click', function(){
			
		});
	};

});