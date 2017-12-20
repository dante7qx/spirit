var SysLogPage = {
	init: function(startDate) {
		$(window).resize(function(){
			$('#sysLogGridlist').datagrid({fitColumns:false});
		});
		this.initQueryStartDateEvt(startDate);
		this.loadSysLogList();
	},
	initQueryStartDateEvt: function(startDate) {
		$('#queryStartDate','#sysLogGridToolbar').datebox({
			onSelect: function(date){
				SysLogPage.fillEndDate();
			}
		});
		$('#queryStartDate','#sysLogGridToolbar').datebox("setValue", startDate);
		this.fillEndDate();
	},
	loadSysLogList: function() {
		$('#sysLogGridlist').datagrid({
		    url: ctx+'/sysmgr/syslog/query_page',
		    title: '系统访问日志列表',
		    rownumbers: true,
		    rownumberWidth: 20,
		    singleSelect: true,
		    fitColumn: false,
		    pagination: true,
		    pageSize: COMMON_CONFIG['PAGESIZE'],
		    pageList: COMMON_CONFIG['PAGELIST'],
		    toolbar: '#sysLogGridToolbar',
		    sortName: 'visitTime',
		    sortOrder: 'desc',
		    remoteSort: true,
		    height: $(window).height() - 20,
		    queryParams: {
		    	'q[startDate]': $('#queryStartDate','#sysLogGridToolbar').datebox("getValue"),
				'q[endDate]': $('#queryEndDate','#sysLogGridToolbar').datebox("getValue")
		    },
		    columns:[[
		    	{field:'spendTime',title:'请求耗时',width:70,halign:'center',align:'center',sortable:true},
		        {field:'url',title:'URL',width:280,halign:'center'},
		        {field:'uri',title:'URI',width:170,halign:'center'},
		        {field:'requestMethod',title:'请求方式',width:60,halign:'center',align:'center'},
		        {field:'clazz',title:'类名',width:300,halign:'center'},
		        {field:'methodName',title:'方法名',width:150,halign:'center'},
		        {field:'params',title:'请求参数',width:800,halign:'center'}
		    ]],
		    frozenColumns: [[
		    	{field:'account',title:'用户',width:90,halign:'center',align:'left'},
		        {field:'ip',title:'IP',width:90,halign:'center'},
		        {field:'visitTime',title:'访问时间',width:125,halign:'center',sortable:true}
		    ]]
		});
	},
	search: function() {
		if(!$('#sysLogQueryForm').form('validate')) {
			return;
		}
		$('#sysLogGridlist').datagrid('load', {
			'q[account]': $('#queryAccount','#sysLogGridToolbar').textbox("getValue"),
			'q[ip]': $('#queryIP','#sysLogGridToolbar').textbox("getValue"),
			'q[startDate]': $('#queryStartDate','#sysLogGridToolbar').datebox("getValue"),
			'q[endDate]': $('#queryEndDate','#sysLogGridToolbar').datebox("getValue")
		});
	},
	reset: function() {
		$('#sysLogQueryForm').form('clear');
	},
	fillEndDate: function() {
		var startDate = $('#queryStartDate','#sysLogGridToolbar').datebox("getValue");
		var year = parseInt(startDate.substring(0, 4), 10);
		var month = parseInt(startDate.substring(5, 7), 10);
		var day = parseInt(startDate.substring(8, 10), 10);
		var endDate = new Date();
		endDate.setFullYear(year, month - 1, day);
		endDate.setDate(endDate.getDate() + 7);
		$('#queryEndDate','#sysLogGridToolbar').datebox("setValue", endDate.toLocaleDateString().replace(new RegExp("/", 'g'),"-"));
	}
};
$.extend($.fn.validatebox.defaults.rules, {
	checkLimitDate: {
		validator: function(value, param){
			if(!value) return true;
			var start = $('#queryStartDate','#sysLogGridToolbar').datebox("getValue");
			if(!start) return true;
			start = start.replace(/-/g, "/");
			var end = value.replace(/-/g, "/");
			var diffday = Date.parse(end) - Date.parse(start);
			diffday = diffday.toFixed(2) / 86400000;
			if(diffday > 7) {
				return false;
			}
			return true;
        },
        message: '每次只能查询7天内的日志！'
	}
});