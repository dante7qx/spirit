var ResourcePage = {
	MENU_MAX_LEVEL: 3,
	initPage: function() {
		this.loadTree();
	},
	loadTree: function() {
		$('#resourceTree').tree({
		    url: ctx+'/sysmgr/resource/query_tree',
		    animate: true,
		    dnd: true,
		    onSelect: function(node){
		    	ResourcePage.loadData(node, false);
			},
			onBeforeDrop: function(target,source,point) {
				if(point == 'append') {
					var level = ResourcePage.getNodeLevel($('#resourceTree').tree('getNode',target))['count'];
					if(level == ResourcePage.MENU_MAX_LEVEL) {
						$.messager.alert('提示','系统只支持3级菜单！');
						return false;
					}
				}
				return true;
			},
			onDrop: function(target,source,point) {
				ResourcePage.dragNode($('#resourceTree').tree('getNode',target), source, point);
			},
			onContextMenu: function(e,node){
                e.preventDefault();
                $(this).tree('select',node.target);
                ResourcePage.limitNodeLevel(node);
                $('#resourceTreeMenu').menu('show',{
                    left: e.pageX,
                    top: e.pageY
                });
            }
		});
	},
	initAuthorityCombotree: function(authorityId) {
		$('#authorityId','#resourceForm').combotree({
		    url: ctx+'/sysmgr/authority/query_combotree',
		    required: true,
		    editable: false,
		    panelHeight: 450,
		    onLoadSuccess: function(node, data) {
		    	$('#authorityId','#resourceForm').combotree('tree').tree('expandAll');
		    	if(authorityId) {
		    		$('#authorityId','#resourceForm').combotree('setValue', authorityId);
		    	}
		    },
		    onBeforeSelect: function(node) {
		    	var authTree = $('#authorityId','#resourceForm').combotree('tree');
		    	if(!$(authTree).tree('isLeaf',node.target)) {
		    		$.messager.alert('提示','只能选择叶子节点的权限！');
		    		return false;
		    	} 
		    }
		    
		});
	},
	loadData: function(node, editable) {
		if(node['id'] < 0) {
			$('#resourceContainer').css('display', 'none');
		} else {
			$('#resourceContainer').show();
			this.limitNodeLevel(node);
			spirit.util.controlFormBtn(editable, 'resourceFormBtnContainer');
			$('#resourceForm').form('clear').form('load', node['attributes']);
			this.initAuthorityCombotree( node['attributes']['authorityId']);
			$('#pid','#resourceForm').val(node['attributes']['pid']);
			spirit.util.isEditForm('resourceForm', editable);
		}
	},
	limitNodeLevel: function(node) {
		var level = this.getNodeLevel(node);
		if(level['count'] >= ResourcePage.MENU_MAX_LEVEL) {
			$('[cusType]').hide();
		} else {
			$('[cusType]').show();
		}
	},
	addNode: function() {
		var curNode = $('#resourceTree').tree('getSelected');
		if(!curNode) {
			$.messager.alert('提示','请先选择一个父节点！');
			return;
		}
		this.initAuthorityCombotree();
		this.edit();
		$('#resourceContainer').show();
		$('#resourceForm').form('clear');
		$('#pid','#resourceForm').val(curNode['id'] > 0 ? curNode['id'] : '');
		$('#iconClass','#resourceForm').textbox('setValue','fa fa-file');
	},
	delNode: function() {
		var curNode = $('#resourceTree').tree('getSelected');
		if(!curNode) {
			$.messager.alert('提示','请先选择一个父节点！');
			return;
		} else if(curNode['id'] < 0) {
			$.messager.alert('提示','不允许删除所有菜单！');
			return;
		}
		$.messager.confirm('提示', '本操作会删除当前节点及其子节点，您确定要删除吗?', function(r){
			if (r){
				$.ajax({
					url: ctx+'/sysmgr/resource/delete_by_id',
					type: 'post',
					data: {
						id: curNode['id']
					},
					success: function(result) {
						if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
							$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
							return;
						}
						$('#resourceTree').tree('pop', curNode.target);
						$('#resourceTree').tree('select', $('#resourceTree').tree('getRoot').target);
					}
				});
			}
		});
	},
	expandAll: function() {
		$('#resourceTree').tree('expandAll');
	},
	collapseAll: function() {
		var node = $('#resourceTree').tree('find', -1);
		$('#resourceTree').tree('collapse', node.target).tree('select', node.target);
	},
	getNodeLevel: function(node) {
		var level = {count: 0};
		this.getLevel(level, node);
		return level;
	},
	getLevel: function(level, node) {
		var pNode = $('#resourceTree').tree('getParent', node.target);
		if(pNode) {
			level['count']++;
			this.getLevel(level, pNode);
		}
	},
	appendNode: function(data) {
		var id = $('#id','#resourceForm').val();
		var node = [];
		var newNode = {};
		newNode['id'] = data['id'];
		newNode['text'] = data['name'];
		newNode['state'] = 'open';
		var newNodeAttr = {};
		newNodeAttr['id'] = data['id'];
		newNodeAttr['name'] = data['name'];
		newNodeAttr['iconClass'] = data['iconClass'];
		newNodeAttr['url'] = data['url'];
		newNodeAttr['authorityId'] = data['authorityId'];
		newNodeAttr['pid'] = data['pid'];
		newNodeAttr['fullId'] = data['fullId'];
		newNodeAttr['showOrder'] = data['showOrder'];
		newNode['attributes'] = newNodeAttr;
		node.push(newNode);
		var selected = $('#resourceTree').tree('getSelected');
		if(id) {
			$('#resourceTree').tree('update', {
				target: selected.target,
				text: newNode['text'],
				attributes: newNode['attributes']
			});
		} else {
			$('#resourceTree').tree('append', {
				parent: selected.target,
				data: node
			});
			$('#resourceTree').tree('select', $('#resourceTree').tree('find', data['id']).target);
		}
	},
	submit: function() {
		$('#resourceForm').form('submit', {
			iframe: false,
		    url: ctx+'/sysmgr/resource/update_resource',
		    success:function(result){
		    	var result = eval('(' + result + ')');
		    	if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
					$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
					return;
				}
	        	ResourcePage.appendNode(result['data']);
	        	$('#resourceForm').form('load', result['data']);
	        	spirit.util.controlFormBtn(false, 'resourceFormBtnContainer');
	        	spirit.util.isEditForm('resourceForm', false);
		    }
		});
	},
	reset: function() {
		var curNode = $('#resourceTree').tree('getSelected');
		var id = $('#id','#resourceForm').val();
		if(id) {
			ResourcePage.loadData(curNode, true);
		} else {
			$('#resourceForm').form('clear');
			$('#pid','#resourceForm').val(curNode['id']);
		}
	},
	edit: function() {
		spirit.util.controlFormBtn(true, 'resourceFormBtnContainer');
		spirit.util.isEditForm('resourceForm', true);
	},
	dragNode: function(targetNode, sourceNode, point) {
		var targetId = targetNode['id'];
		var targetPid = '';
		var sourceId = sourceNode['id'];
		var targetParentNode = $('#resourceTree').tree('getParent',targetNode.target);
		if(targetParentNode && targetParentNode['id'] > 0) {
			targetPid = targetParentNode['id'];
		}
		var targetShowOrder = targetId > 0 ? targetNode['attributes']['showOrder'] : 1;
		$.ajax({
			url: ctx+'/sysmgr/resource/update_when_drag',
			type: 'post',
			data: {
				point: point,
				targetId: targetId,
				targetPid: targetPid,
				targetShowOrder: targetShowOrder,
				sourceId: sourceId
			},
			success: function(result) {
				if(result['resultCode'] != COMMON_CONFIG['SUCCESS']) {
					$.messager.alert('错误','系统错误，请联系系统管理员', 'error');
					return;
				}
				sourceNode['attributes']['pid'] = targetId;
			}
		});
	}
};