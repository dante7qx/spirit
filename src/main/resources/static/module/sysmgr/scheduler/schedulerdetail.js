var SchedulerDetailPage = {
		SUBMIT_FLAG: false,
		paramSchedulerId: '',
		initPage: function(paramSchedulerId) {
			this.paramSchedulerId = paramSchedulerId;
			this.initJobIdCombo();
			this.loadData();
		},
		initJobIdCombo: function() {
			$('#jobId','#schedulerDetailForm').combobox({
				url: ctx+'/sysmgr/scheduler/query_combo',
			    valueField: 'jobId',
			    textField: 'jobName',
			    onSelect: function(record) {
			    	$('#jobName','#schedulerDetailForm').textbox('setValue', record['jobName']);
			    	$('#jobClass','#schedulerDetailForm').val(record['jobClass']);
			    	$('#tmpJobId','#schedulerDetailForm').val(record['jobId']);
			    }
			});
		},
		loadData: function() {
			if(this.paramSchedulerId) {
				spirit.util.controlFormBtn(false, 'schedulerFormBtnContainer');
				this.loadDataById(false);
			} else {
				spirit.util.controlFormBtn(true, 'schedulerFormBtnContainer');
			}
		},
		loadDataById: function(editable) {
			$.ajax({
				url: ctx+'/sysmgr/scheduler/query_by_id',
				type: 'post',
				data: {
					id: SchedulerDetailPage.paramSchedulerId
				},
				success: function(result) {
					if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
						$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
						return;
					}
					$('#schedulerDetailForm').form('clear').form('load', result['data']);
					SchedulerDetailPage.loadUpdateInfo(result['data']);
					spirit.util.isEditForm('schedulerDetailForm', editable);
				}
			});
		},
		loadUpdateInfo: function(data) {
			var updateUserName = data['updateUserName'] ? data['updateUserName'] : '';
			var updateDate = data['updateDate'] ? data['updateDate'] : '';
			$('#schedulerUpdateInfo').text(updateUserName + ' ' + updateDate);
		},
		submit: function() {
			if(SchedulerDetailPage.SUBMIT_FLAG) {
				return;
			}
			if($('#schedulerDetailForm').form('validate')) {
				SchedulerDetailPage.SUBMIT_FLAG = true;
			}
			$('#schedulerDetailForm').form('submit', {
				iframe: false,
			    url: ctx+'/sysmgr/scheduler/update_scheduler',
			    success:function(result){
			    	SchedulerDetailPage.SUBMIT_FLAG = false;
			    	var result = eval('(' + result + ')');
			    	if(result['resultCode'] == COMMON_CONFIG['SUCCESS']) {
			        	$('#schedulerGridlist').datagrid('reload');
			        	$('#schedulerWindow').window('close');
			        } else {
			        	$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
			        }
			    }
			});
		},
		reset: function() {
			if(this.paramSchedulerId) {
				this.loadDataById(true);
			} else {
				$('#schedulerDetailForm').form('clear');
			}
			SchedulerDetailPage.SUBMIT_FLAG = false;
		},
		del: function() {
			$.messager.confirm('提示', '您确定要删除吗?', function(r){
				if (r){
					$.ajax({
						url: ctx+'/sysmgr/scheduler/delete_by_id',
						type: 'post',
						data: {
							id: $('#id','#schedulerDetailForm').val()
						},
						success: function(result) {
							if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
								$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
								return;
							}
							$('#schedulerWindow').window('close');
							$('#schedulerGridlist').datagrid('reload');
						}
					});
				}
			});
		},
		edit: function() {
			spirit.util.controlFormBtn(true, 'schedulerFormBtnContainer');
			spirit.util.isEditForm('schedulerDetailForm', true);
		}
};
$.extend($.fn.validatebox.defaults.rules, {
	uniqueJobId: {
		validator: function(value, param){
			if(!value) return true;
			var id = $('#id','#schedulerDetailForm').val();
			var jobId = $('#tmpJobId','#schedulerDetailForm').val();
			var result = JSON.parse($.ajax({
				url: ctx+'/sysmgr/scheduler/check_job_exist',
				type: 'post',
				async: false,
				cache: false,
				data: {
					id: id,
					jobId: jobId
				}
			}).responseText);
			if(result) {
				return false;
			}
			return true;
        },
        message: '该任务已存在，请重新输入！'
	}
});