var UserDetailPage = {
		SUBMIT_FLAG: false,
		paramUserId: '',
		initPage: function(paramUserId) {
			this.paramUserId = paramUserId;
			this.loadData();
		},
		initRoleCombotree: function(roles) {
			$('#roleIds','#userDetailForm').combotree({
			    url: ctx+'/sysmgr/role/query_role_tree',
			    required: false,
			    editable: false,
			    multiple: true
			});
			if(roles) {
				$('#roleIds','#userDetailForm').combotree('setValues', roles);
			}
		},
		loadData: function() {
			if(this.paramUserId) {
				spirit.util.controlFormBtn(false, 'userFormBtnContainer');
				$('#account','#userDetailForm').textbox({
					required: false,
					validType: ''
				});
				$('#passwordTr','#userDetailForm').hide();
				$('#password','#userDetailForm').textbox({required: false});
				this.loadDataById(false);
			} else {
				spirit.util.controlFormBtn(true, 'userFormBtnContainer');
				this.initRoleCombotree();
				$('#userDetailForm').form('clear');
			}
		},
		loadDataById: function(editable) {
			$.ajax({
				url: ctx+'/sysmgr/user/query_by_id',
				type: 'post',
				data: {
					id: UserDetailPage.paramUserId
				},
				success: function(result) {
					if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
						$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
						return;
					}
					$('#userDetailForm').form('clear').form('load', result['data']);
					UserDetailPage.initRoleCombotree(result['data']['roleIds']);
					UserDetailPage.loadUpdateInfo(result['data']);
					spirit.util.isEditForm('userDetailForm', editable);
					spirit.util.isEditFormCommon('userDetailForm', false, 'account');
				}
			});
		},
		loadUpdateInfo: function(data) {
			var updateUserName = data['updateUserName'] ? data['updateUserName'] : '';
			var updateDate = data['updateDate'] ? data['updateDate'] : '';
			$('#userUpdateInfo').text(updateUserName + ' ' + updateDate);
		},
		submit: function() {
			if(UserDetailPage.SUBMIT_FLAG){
				return;
			}
			if($('#userDetailForm').form('validate')) {
				UserDetailPage.SUBMIT_FLAG = true;
			}
			$('#userDetailForm').form('submit', {
				iframe: false,
			    url: ctx+'/sysmgr/user/update_user',
			    success:function(result){
			    	UserDetailPage.SUBMIT_FLAG = false;
			    	var result = eval('(' + result + ')');
			    	if(result['resultCode'] == COMMON_CONFIG['SUCCESS']) {
			        	$('#userGridlist').datagrid('reload');
			        	$('#userWindow').window('close');
			        } else {
			        	$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
			        }
			    }
			});
		},
		reset: function() {
			if(this.paramUserId) {
				this.loadDataById(true);
			} else {
				$('#userDetailForm').form('clear');
			}
			UserDetailPage.SUBMIT_FLAG = false;
		},
		del: function() {
			$.messager.confirm('提示', '您确定要删除吗?', function(r){
				if (r){
					$.ajax({
						url: ctx+'/sysmgr/user/delete_user',
						type: 'post',
						data: {
							id: $('#id','#userDetailForm').val()
						},
						success: function(result) {
							if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
								$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
								return;
							}
							$('#userWindow').window('close');
							$('#userGridlist').datagrid('reload');
						}
					});
				}
			});
		},
		edit: function() {
			spirit.util.controlFormBtn(true, 'userFormBtnContainer');
			spirit.util.isEditForm('userDetailForm', true);
			spirit.util.isEditFormCommon('userDetailForm', false, 'account');
		}
};

$.extend($.fn.validatebox.defaults.rules, {
	uniqueUserAccount: {
		validator: function(value, param){
			if(!value) return true;
			var result = JSON.parse($.ajax({
				url: ctx+'/sysmgr/user/query_by_account',
				type: 'post',
				async: false,
				cache: false,
				data: {account: value}
			}).responseText);
			console.log(result)
			if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
				return false;
				$.fn.validatebox.defaults.rules.message = '系统错误，请稍后重试';
			}
			if(result['data'] && result['data']['id'] > 0 && result['data']['status'] != 'DEL') {
				return false;
			}
			return true;
        },
        message: '该账号已存在，请重新输入'
	}
});