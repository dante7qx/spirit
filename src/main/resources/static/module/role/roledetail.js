var RoleDetailPage = {
		SUBMIT_FLAG: false,
		paramRoleId: '',
		initPage: function(paramRoleId) {
			this.paramRoleId = paramRoleId;
			this.loadData();
		},
		initPageWidget: function() {
			$('#authorityTree').tree({
				url: 'role/query_role_authority',
				method: 'post',
				animate: true,
				checkbox: true,
				queryParams: {roleId: RoleDetailPage.paramRoleId ? RoleDetailPage.paramRoleId : 0}
			});
		},
		loadData: function() {
			this.initPageWidget();
			if(this.paramRoleId) {
				hnasys.util.controlFormBtn(false, 'roleFormBtnContainer');
				this.loadDataById(false);
			} else {
				hnasys.util.controlFormBtn(true, 'roleFormBtnContainer');
				$('#roleDetailForm').form('clear');
			}
		},
		loadDataById: function(editable) {
			$.ajax({
				url: 'role/query_by_id',
				type: 'post',
				data: {
					id: RoleDetailPage.paramRoleId
				},
				success: function(result) {
					if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
						$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
						return;
					}
					$('#roleDetailForm').form('clear').form('load', result['data']);
					RoleDetailPage.loadUpdateInfo(result['data']);
					hnasys.util.isEditForm('roleDetailForm', editable);
				}
			});
		},
		loadUpdateInfo: function(data) {
			var updateUserName = data['updateUserName'] ? data['updateUserName'] : '';
			var updateDate = data['updateDate'] ? data['updateDate'] : '';
			$('#roleUpdateInfo').text(updateUserName + ' ' + updateDate);
		},
		submit: function() {
			if(RoleDetailPage.SUBMIT_FLAG) {
				return;
			}
			if($('#roleDetailForm').form('validate')) {
				RoleDetailPage.SUBMIT_FLAG = true;
			}
			var checkNodeIds = this.getSelectedAuthority();
			$('#authorityIds','#roleDetailForm').val(checkNodeIds);
			$('#roleDetailForm').form('submit', {
				iframe: false,
			    url: 'role/update_role',
			    success:function(result){
			    	RoleDetailPage.SUBMIT_FLAG = false;
			    	var result = eval('(' + result + ')');
			    	if(result['resultCode'] == COMMON_CONFIG['SUCCESS']) {
			        	$('#roleGridlist').datagrid('reload');
			        	$('#roleWindow').window('close');
			        } else {
			        	$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
			        }
			    }
			});
		},
		reset: function() {
			if(this.paramRoleId) {
				this.initPageWidget();
				this.loadDataById(true);
			} else {
				$('#roleDetailForm').form('clear');
				var nodes = $('#authorityTree').tree('getChecked');
				if(nodes) {
					$.each(nodes, function(i, node) {
						$('#authorityTree').tree('uncheck', node.target);
					}); 
				}
			}
			RoleDetailPage.SUBMIT_FLAG = false;
		},
		del: function() {
			$.messager.confirm('提示', '您确定要删除吗?', function(r){
				if (r){
					$.ajax({
						url: 'role/delete_by_id',
						type: 'post',
						data: {
							id: $('#id','#roleDetailForm').val()
						},
						success: function(result) {
							if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
								$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
								return;
							}
							$('#roleWindow').window('close');
							$('#roleGridlist').datagrid('reload');
						}
					});
				}
			});
		},
		edit: function() {
			hnasys.util.controlFormBtn(true, 'roleFormBtnContainer');
			hnasys.util.isEditForm('roleDetailForm', true);
		},
		getSelectedAuthority: function() {
			var checkNodeIds = [];
			var nodes = $('#authorityTree').tree('getChecked');
			if(nodes) {
				$.each(nodes, function(i, node) {
					checkNodeIds.push(node['id']);
				}); 
			}
			return checkNodeIds;
		}
};