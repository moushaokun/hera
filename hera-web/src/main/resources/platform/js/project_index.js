var grid;
var vue;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				expireDate:null,
				expireDateDialogShow:false,
				checkboxCols:[],//列表字段
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},//top工具form数据
				form: {//表单数据
					id: '',
			        projectName: null,
			        packageName: null,
			        artifactId: null,
			        mvnVersion: null,
			        appId: null,
			        casUrl: null,
			        zookeeperUrl: null,
			        contextPath: null,
			        webPort: null
		        },
		        formRules: {
		          projectName: [
		            { required: true, message: '请输入工程名', trigger: 'change' }
		          ],
		          packageName: [
		            { required: true, message: '请输入根包名', trigger: 'change' }
		          ],
		          artifactId: [
		            { required: true, message: '请输入mvn名称', trigger: 'change' }
		          ],
		          mvnVersion: [
		            { required: true, message: '请输入mvn版本', trigger: 'change' }
		          ],
		          contextPath: [
		            { required: true, message: '请输入web上下文名称', trigger: 'change' }
		          ],
		          webPort: [
		            { required: true, message: '请输入web端口', trigger: 'change' }
		          ],
		          casUrl: [
		            { required: true, message: '请输入cas地址', trigger: 'change' }
		          ],
		          zookeeperUrl: [
		            { required: true, message: '请输入zookeeper地址', trigger: 'change' }
		          ],
		          appId: [
		            { required: true, message: '请输入appId', trigger: 'change' }
		          ]
		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建",
		        casFieldsShow: false
			};
		},
		methods:{
			compositeSearch: function(){//检索
				var context = this;
				grid.jqGrid("setGridParam", {
					postData: {filters:{}}
				});
				grid.jqGrid('searchGrid', {multipleSearch:true,drag:false,searchOnEnter:true,
					onSearch: function(){
						FieldtypeAddtionerFactory.create(grid).search();
						context.toolBarForm.value = '';
						context.toolBarForm.condition = '';
					}
				});
			},
			refreshPage: function(){//刷新
				this.fullscreenLoading = true;
				setTimeout(function(){
					location.reload();
				}, 1000);
			},
			add: function(){//新增
				this.casFieldsShow = false;
				this.dialogFormVisible = true;
				this.formEdit = false;
				this.submitBtnName = "立即创建";
			},
			edit: function(){//编辑
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message: "选择一条要编辑的数据!", type: "warning"});
					return;
				}
				var context = this;
				this.dialogFormVisible = true;
				this.formEdit = true;
				this.submitBtnName = "编辑提交";
				PlatformUI.ajax({
					url: contextPath + "/project/" + ids[0],
					afterOperation: function(data, textStatus,jqXHR){
						delete data.createDate;
						if(!data.appId){
							context.casFieldsShow = false;
						}else{
							context.casFieldsShow = true;
						}
						context.form = $.extend(context.form, data);
					}
				});
			},
			del: function(){//删除 
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length == 0){
					PlatformUI.message({message:"请至少选择一条要删除的数据!", type:"warning"});
					return;
				}
				this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		        }).then(function(){
		        	PlatformUI.ajax({
						url: contextPath + "/project",
						type: "post",
						data: {_method:"delete",ids:ids},
						message:PlatformUI.message,
						afterOperation: function(){
							PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
						}
					});
		        });
			},
			exp: function(){//导出
				PlatformUI.exportGrid("list", "from robot_project");
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				this.$refs['form'].resetFields();
				this.form = {
					id: '',
			        projectName: null,
			        packageName: null,
			        artifactId: null,
			        mvnVersion: null,
			        appId: null,
			        casUrl: null,
			        zookeeperUrl: null,
			        contextPath: null,
			        webPort: null
		        };
			},
			onSubmit: function(){//弹出表单的提交
				var context = this;
        		this.$refs['form'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.form);
        				if(!context.casFieldsShow){
        					data.casUrl = null;
        					data.zookeeperUrl = null;
        					data.appId = null;
        				}
        				var actionUrl = contextPath + "/project";
        				if(context.formEdit){
        					actionUrl = contextPath + "/project/" + data.id;
				            data['_method'] = "put";
        				}
        				PlatformUI.ajax({
			            	url: actionUrl,
			            	type: "post",
			            	data: data,
			            	message:PlatformUI.message,
			            	afterOperation: function(){
			            		context.toolBarForm.value = "";
			            		context.toolBarForm.condition = "";
			            		PlatformUI.refreshGrid(grid, {sortname:"createDate",sortorder:"desc"});
			            	}
			            });
			            context.dialogFormVisible = false;
						context.$refs['form'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
     		},
		    commonSearch: function(value){
		    	commonSearch();
		    },
		    selectGridColumn: function(){
		    	this.dialogGridSelectionVisible = true;
		    },
		    saveColVisible: function(){
		    	for(var i = 0; i < this.checkboxCols.length; i++){
		    		if(this.checkboxCols[i].visible){
		    			grid.showCol(this.checkboxCols[i].value);
		    		}else{
		    			grid.hideCol(this.checkboxCols[i].value);
		    		}
		    	}
		    	this.dialogGridSelectionVisible = false;
		    	//重设jqrid宽度
		    	PlatformUI.fineTuneGridSize(grid, 65);
		    },
		    showExpireDateDialog: function(){
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message:"请选择一条要操作的数据!", type:"warning"});
					return;
				}
		    	this.expireDateDialogShow = true;
		    },
		    downloadLicense: function(){
		    	var expireDate = null;
		    	if(this.expireDate){
		    		expireDate = ExtendDate.getFormatDateByLong(this.expireDate.getTime(), 'yyyy-MM-dd');
		    	}
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
		    	PlatformUI.simulateSubmitForm(contextPath + "/project/downloadLicense", {expireDate: expireDate, projectId: ids[0]}, "post");
		    },
		    resetExpireDateForm: function(){
		    	this.expireDate = null;
		    	this.expireDateDialogShow = false;
		    },
		    downloadProjectScaffold: function(){
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message:"请选择一条要操作的数据!", type:"warning"});
					return;
				}
				var ids = grid.jqGrid ('getGridParam', 'selarrrow');
		    	PlatformUI.simulateSubmitForm(contextPath + "/project/downloadProjectScaffold", {projectId: ids[0]}, "post");
		    }
		}
	});
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(grid, 65);
	});
	
	grid = $("#list").jqGrid({
        url: contextPath + "/project",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ["ID", "工程名称", "工程根包名", "appId", 'mvn名称', "web上下文名称", "web端口","创建时间"],
        colModel: [
            { name: "id", index:"id",hidden: true},
            { name: "projectName", index:"projectName", align:"center", sortable: true},
            { name: "packageName", index:"packageName", align:"center", sortable: true},
            { name: "appId", index:"appId", align:"center", sortable: true},
            { name: "artifactId", index:"artifactId", align:"center", sortable: true},
            { name: "contextPath", index:"contextPath", align:"center", sortable: true},
            { name: "webPort", index:"webPort", align:"center", sortable: true},
            { name: "createDate", index:"createDate",align:"center", expType:"date",expValue:"yyyy-MM-dd HH:mm:ss",searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:"date",formatoptions: { srcformat: "U", newformat: "Y-m-d H:i:s" }}],
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "工程列表",
    	gridComplete: function(){
    		PlatformUI.fineTuneGridSize(grid, 62);
    		//设置隐藏/显示列字段
    		vue.checkboxCols = [];
    		var gridColModel = grid.getGridParam("colModel");
	    	var gridColNames = grid.getGridParam("colNames");
	    	for(var i=0; i < gridColNames.length; i++){
	    		if(gridColNames[i].indexOf("role='checkbox'") == -1){
		    		var name = gridColNames[i];
					var value = gridColModel[i].name;
					var visible = true;
					if(gridColModel[i].hidden){
						visible = false;
					}
		    		vue.checkboxCols.push({name:name, value:value, visible:visible});
	    		}
	    	}
    	}
    });
			
});

/***********************方法区***************************/

function commonSearch(){
	var name = vue.toolBarForm.condition;
	var value = vue.toolBarForm.value;
	if(name == ""){
		PlatformUI.message({message:"请选择搜索条件", type:"warning"});
		return;
	}
	if(value == ""){
		PlatformUI.message({message:"请输入搜索内容", type:"warning"});
		return;
	}
	var rules = [{"field":name,"op":"cn","data":value.trim()}];
	var filters = {"groupOp":"AND","rules":rules};
	grid.jqGrid("setGridParam", {
		postData: {filters:JSON.stringify(filters)},
		page: 1
	}).trigger("reloadGrid");
}

