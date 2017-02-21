var grid;
var vue;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				projectSelectOptions: [],//工程下拉列表数据
				checkboxCols:[],//列表字段
				dialogGridSelectionVisible: false,//列表字段选择dialog显示
				fullscreenLoading: false,//全屏loading条
				toolBarForm: {
					value: "",
					condition: ""
				},//top工具form数据
				form: {//表单数据
					id: null,
			        domainName: null,
			        className: null,
			        tableName: null,
			        projectId:null
		        },
		        formRules: {
		          domainName: [
		            { required: true, message: '请输入名称', trigger: 'change' }
		          ],
		          className: [
		            { required: true, message: '请输入类名', trigger: 'change' }
		          ],
		          tableName: [
		            { required: true, message: '请输入表名', trigger: 'change' }
		          ],
		          projectId: [
		            { required: true, message: '请选择工程', trigger: 'change' }
		          ]
		        },
		        dialogFormVisible: false,//dialog是否显示
		        formEdit: false,
		        submitBtnName: "立即创建"
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
				var context = this;
				this.dialogFormVisible = true;
				this.formEdit = false;
				this.submitBtnName = "立即创建";
				PlatformUI.ajax({
					url: contextPath + "/project/all",
					afterOperation: function(data){
						context.projectSelectOptions = [];
						$(data).each(function(){
							context.projectSelectOptions.push({id: this.id, name: this.projectName});
						});
					}
				});
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
					url: contextPath + "/project/all",
					afterOperation: function(data){
						context.projectSelectOptions = [];
						$(data).each(function(){
							context.projectSelectOptions.push({id: this.id, name: this.projectName});
						});
						PlatformUI.ajax({
							url: contextPath + "/simpleModule/" + ids[0],
							afterOperation: function(data, textStatus,jqXHR){
								delete data.createDate;
								context.form.projectId = data.project.id;
								delete data.project;
								context.form = $.extend(context.form, data);
							}
						});
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
						url: contextPath + "/simpleModule",
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
				PlatformUI.exportGrid("list", "from robot_domain d left join robot_project p on d.projectId - p.id", null, {prefix:"d"});
			},
			resetForm: function(){
				this.dialogFormVisible = false;
				this.$refs['form'].resetFields();
				this.form = {
					id: null,
			        domainName: null,
			        className: null,
			        tableName: null,
			        projectId: null
		        };
			},
			onSubmit: function(){//弹出表单的提交
				var context = this;
        		this.$refs['form'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.form);
        				data['project.id'] = data.projectId;
        				var actionUrl = contextPath + "/simpleModule";
        				if(context.formEdit){
        					actionUrl = contextPath + "/simpleModule/" + data.id;
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
		    }
		}
	});
	
	//绑定jqgrid resize事件
	$(window).bind('resize', function() {
		PlatformUI.resizeGridWidth(grid, 65);
	});
	
	grid = $("#list").jqGrid({
        url: contextPath + "/simpleModule",
        datatype: "json",
        autowidth: true,
        height:300,
        mtype: "GET",
        multiselect: true,
        colNames: ["ID", "名称", "类名", "表名", "所属工程", "创建时间"],
        colModel: [
            { name: "id", index:"id",hidden: true},
            { name: "domainName", index:"domainName", align:"center", sortable: true},
            { name: "className", width:100,index:"className",align:"center", sortable: true },
            { name: "tableName", width:100,index:"tableName",align:"center", sortable: true },	
            { name: "projectName", width:100,index:"p.projectName",align:"center", sortable: true },	
            { name: "createDate", index:"d.createDate",align:"center", expType:"date",expValue:"yyyy-MM-dd HH:mm:ss",searchoptions:{dataInit:PlatformUI.defaultJqueryUIDatePick}, sortable: true ,formatter:"date",formatoptions: { srcformat: "U", newformat: "Y-m-d H:i:s" }}],
        pager: "#pager",
        rowNum: 10,
        rowList: [10, 20, 30],
        sortname: "d.createDate",
        sortorder: "desc",
        viewrecords: true,
        gridview: true,
        autoencode: true,
        caption: "员工列表",
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

