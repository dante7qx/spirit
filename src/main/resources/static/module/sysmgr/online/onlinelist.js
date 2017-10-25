var OnlinePage = {
		
		init: function() {
			$(window).resize(function(){
				$('#onlineGridlist').datagrid({fitColumns:true});
			});
			this.loadOnlineList();
		},
		loadOnlineList: function() {
			$('#onlineGridlist').datagrid({
			    url: ctx+'/sysmgr/online/query_page',
			    title: '在线用户列表',
			    rownumbers: true,
			    rownumberWidth: 20,
			    singleSelect: true,
			    fitColumns: true,
			    pagination: false,
			    toolbar: '#onlineGridToolbar',
			    sortName: 'lastAccessedTime',
			    sortOrder: 'desc',
			    remoteSort: false,
			    height: $(window).height() - 20,
			    columns:[[
			    	{field:'sessionId',title:'会话Id',width:130,halign:'center'},
			        {field:'loginUserId',title:'帐号',width:80,halign:'center',align:'center'},
			        {field:'ip',title:'IP地址',width:90,halign:'center',align:'center'},
			        {field:'createDate',title:'创建时间',width:100,halign:'center',align:'left',sortable:true},
			        {field:'lastAccessedTime',title:'最后访问时间',width:110,halign:'center',align:'left',sortable:true}
			    ]]
			});
		},
		search: function() {
		},
		reset: function() {
		}
		
};