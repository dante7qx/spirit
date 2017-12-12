var pc;
$.parser.onComplete = function () {
    if (pc) {
        clearTimeout(pc);
    }
    pc = setTimeout(function(){
    	$("#loading").fadeOut("normal", function () {
            $(this).remove();
        });
    }, 0);
}
var MainPage = {
	init: function() {
		/*布局部分*/
		$('#master-layout').layout({
			fit:true/*布局框架全屏*/
		});
		/*右侧菜单控制部分*/
		var left_control_status=true;
        var left_control_panel=$("#master-layout").layout("panel",'west');
        $(".left-control-switch").on("click",function(){
            if(left_control_status){
                left_control_panel.panel('resize',{width:60});
                left_control_status=false;
                $(".theme-left-normal").hide();
                $(".theme-left-minimal").show();
            }else{
                left_control_panel.panel('resize',{width:200});
                left_control_status=true;
                $(".theme-left-normal").show();
                $(".theme-left-minimal").hide();
            }
            $("#master-layout").layout('resize', {width:'100%'})
        });
		var cc1=$('#cc1').combo('panel');
		cc1.panel({cls:"theme-navigate-combobox-panel"}); 
	},
	currentTabTitle: null,
	openTab: function(target) {
		if(!$(target).next().length){
			var url=$(target).attr("url");
			var text=$(target).text();
			var iconCls=$("i",$(target).parent()).attr("class");
			this.openTab2(url,text,iconCls);
		}
	},
	openTab2: function(url, text, iconCls) {
		if ($("#mainTabs").tabs("exists", text)) {
	        $("#mainTabs").tabs("close", text);
	        this.addTab(url, text, iconCls);
	        $("#mainTabs").tabs("select", text);
	    } else {
//	        this.addTab(url, text, iconCls);
	    	$("#mainTabs").tabs("addIframeTab",  {
	    		tab:{
	                title: text,
	                iconCls: iconCls,
	                closable:true
	            },
	            iframe:{
            		src:url,
            		delay:'fast',
            		message:'努力加载中，请稍后...'
            	}
	    	});
	    }
	},
	addTab: function(url, text, iconCls) {
	    var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
	    $("#mainTabs").tabs("add", {
	        title: text,
	        iconCls: iconCls,
	        closable: true,
	        content: content
	    });
	},
	tabContextMenu: function(e, title,index){
		e.preventDefault();
        $('#mainMenuButton').menu('show',{
            left: e.pageX,
            top: e.pageY
        });
        MainPage.currentTabTitle = title;
	},
	closeOthers: function(){
		var targetTab= $("#mainTabs");
		var tabPanels=targetTab.tabs("tabs");
		var panelTit= null;
		for(var index=tabPanels.length-1;index>=0;index--){
			panelTit = tabPanels[index].panel("options").title;
			if(panelTit != MainPage.currentTabTitle){
				$("#mainTabs").tabs("close",panelTit);
			}
		};
	},
	closeAll: function(){
		var targetTab= $("#mainTabs");
		var tabPanels=targetTab.tabs("tabs");
		var panelTit= null;
		for(var index=tabPanels.length-1;index>=0;index--){
			panelTit = tabPanels[index].panel("options").title;
			$("#mainTabs").tabs("close",panelTit);
		};
	}
};