var UserPage = {
		ADMIN: 'superadmin',
		STATUS: {
			NORMAL: 'NORMAL',
			LOCK: 'LOCK',
			DEL: 'DEL'
		},
		init: function() {
			$(window).resize(function(){
				$('#userGridlist').datagrid({fitColumns:true});
			});
			this.loadUserList();
		},
		loadUserList: function() {
			$('#userGridlist').datagrid({
			    url: ctx+'/sysmgr/user/query_page',
			    title: '用户列表',
			    rownumbers: true,
			    rownumberWidth: 20,
			    singleSelect: true,
			    fitColumns: true,
			    pagination: true,
			    pageSize: COMMON_CONFIG['PAGESIZE'],
			    pageList: COMMON_CONFIG['PAGELIST'],
			    toolbar: '#userGridToolbar',
			    sortName: 'name',
			    sortOrder: 'desc',
			    remoteSort: false,
			    height: $(window).height() - 20,
			    queryParams: {'q[status]': UserPage.STATUS.NORMAL},
			    frozenColumns: [[
                     {field:'ck',checkbox:true}
                ]],
			    columns:[[
			        {field:'account',title:'帐号',width:80,halign:'center',align:'center',formatter:function(value,row,index) {
			        	if(UserPage.ADMIN == value) {
			        		return value;
			        	}
			        	return '<a class="bdhref" href="javascript:;" onclick="UserPage.editUser('+row['id']+')">'+value+'</a>';
			        }},
			        {field:'name',title:'姓名',width:80,halign:'center',sortable:true},
			        {field:'email',title:'邮箱',width:100,halign:'center',align:'left'},
			        {field:'updateUserName',title:'更新人',width:80,halign:'center',align:'left'},
			        {field:'updateDate',title:'更新时间',width:100,halign:'center',align:'left'}
			    ]],
			    onSelect: function(index,row) {
			    	if(row['account'] != UserPage.ADMIN) {
			    		$('#editCommonUserSpan').show();
			    	} else {
			    		$('#editCommonUserSpan').hide();
			    	}
			    }
			});
		},
		search: function() {
			var lockChecked = $('#queryStatus','#userGridToolbar').is(':checked');
			$('#userGridlist').datagrid('load', {
				'q[name]': $('#queryName','#userGridToolbar').textbox("getValue"),
				'q[email]': $('#queryEmail','#userGridToolbar').textbox("getValue"),
				'q[status]': lockChecked ? UserPage.STATUS.LOCK : UserPage.STATUS.NORMAL
			});
		},
		reset: function() {
			$('#userQueryForm').form('clear');
		},
		editUser: function(id) {
			$('#userWindow').show().window({
				title: (id ? '编辑用户' : '新增用户'),
				closed: false,
				cache: false,
				href: (id ? 'edituser?id='+id : 'edituser'),
				extractor: function(data) {
					return data;
				}	
			});
		},
		modifyPassword: function() {
			var row = $('#userGridlist').datagrid('getChecked');
			if(row.length == 0) {
				$.messager.alert('提示','请至少选择一个用户！');
				return;
			}
			$('#modifyPasswordWindow').show().window('open');
		},
		submitPassword: function() {
			var newPassword = $('#newPassword','#userModifyPasswordForm').val();
			var repeatPassword = $('#repeatPassword','#userModifyPasswordForm').val();
			if(newPassword != repeatPassword) {
				$.messager.alert('提示','两次密码输入不一致，请重新输入！');
				return;
			}
			var row = $('#userGridlist').datagrid('getChecked');
			$('#id','#userModifyPasswordForm').val(row[0]['id']);
			$('#userModifyPasswordForm').form('submit', {
				iframe: false,
			    url: ctx+'/sysmgr/user/modify_password',
			    success:function(result){
			    	var result = eval('(' + result + ')');
			    	if(result['resultCode'] == COMMON_CONFIG['SUCCESS']) {
			    		$.messager.alert('提示','密码修改成功！');
			    		$('#modifyPasswordWindow').form('clear');
			        	$('#modifyPasswordWindow').window('close');
			        } else {
			        	$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
			        }
			    }
			});
		},
		resetPassword: function() {
			$('#modifyPasswordWindow').form('clear');
		},
		lockUser: function() {
			var row = $('#userGridlist').datagrid('getChecked');
			if(row.length == 0) {
				$.messager.alert('提示','请至少选择一个用户！');
				return;
			}
			$.messager.confirm('提示', '您确定要锁定用户吗?', function(r){
				if (r){
					$.ajax({
						url: ctx+'/sysmgr/user/lock_user',
						type: 'post',
						data: {
							id: row[0]['id']
						},
						success: function(result) {
							if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
								$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
								return;
							}
							$('#userGridlist').datagrid('reload');
						}
					});
				}
			});
		},
		releaseLock: function() {
			var row = $('#userGridlist').datagrid('getChecked');
			if(row.length == 0) {
				$.messager.alert('提示','请至少选择一个用户！');
				return;
			}
			$.messager.confirm('提示', '您确定要解锁该用户吗?', function(r){
				if (r){
					$.ajax({
						url: ctx+'/sysmgr/user/release_lock_user',
						type: 'post',
						data: {
							id: row[0]['id']
						},
						success: function(result) {
							if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
								$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
								return;
							}
							$('#userGridlist').datagrid('reload');
						}
					});
				}
			});
		}
};

$.extend($.fn.validatebox.defaults.rules, {
	checkUserPassword: {
		validator: function(value, param){
			if(!value) return true;
			var row = $('#userGridlist').datagrid('getSelected');
			var result = JSON.parse($.ajax({
				url: 'user/check_password',
				type: 'post',
				async: false,
				cache: false,
				data: {
					id: row['id'],
					oldPassword: value
				}
			}).responseText);
			console.log(result)
			if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
				return false;
				$.fn.validatebox.defaults.rules.message = '系统错误，请稍后重试';
			}
			return result['data'];
        },
        message: '原始密码错误，请重新输入！'
	}
});