var grid;
var vue;

$(function(){
	
	vue = new Vue({
		el: '#app',
		data: function(){
			return {
				fieldDetailDialogVisible:false,
				rowCols:[], //行列裸数据
				dialogRowParamShow: false, //行参数框体显示
				dialogFieldsVisible: false, //字段配置框体显示
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
		        fieldDetailForm:{
		        	id:null,
		        	name:null,
		        	codeName:null,
		        	dataType:null,
		        	inputType: null
		        },
		        fieldDetailFormRules:{
			    	name: [
			        	{ required: true, message: '请输入字段名称', trigger: 'change' }
			    	],
			    	codeName: [
			        	{ required: true, message: '请输入字段代码名称', trigger: 'change' }
			    	],
			    	dataType: [
			        	{ required: true, message: '请选择数据类型', trigger: 'change' }
			    	],
			    	inputType: [
			        	{ required: true, message: '请选择表单类型', trigger: 'change' }
			    	]
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
		        dialogValidateFieldShow:false,
		        fieldValidateRuleForm:{
		        	id:null,
		        	validateType:null,
		        	errorMsg:null,
		        	validateTrigger:null,
		        	vValue: null
		        },
		        validateValueShow: false,
		        fieldValidateRuleFormRules:{
		          validateType: [
		            { required: true, message: '请选择验证类型', trigger: 'change' }
		          ],
		          errorMsg: [
		            { required: true, message: '请输入错误信息', trigger: 'change' }
		          ],
		          validateTrigger: [
		            { required: true, message: '请选择触发方式', trigger: 'change' }
		          ]
		        },
		        fieldMark:null,
		        validateRules:[],
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
		    },
		    showFieldsDialog: function(){
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
				if(ids.length != 1){
					PlatformUI.message({message:"请选择一条要操作的数据!", type:"warning"});
					return;
				}
				this.dialogFieldsVisible = true;
				var context = this;
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
		    	this.rowCols = [];
		    	PlatformUI.ajax({
		    		url: contextPath + "/simpleModule/listRowCol",
		    		data: {moduleId: ids[0]},
		    		afterOperation: function(data){
		    			for(var i = 0; i < data.length; i++){
		    				var cols = populateCols(i + 1, data[i].colNum, true);
		    				context.rowCols.push({id: data[i].id, row: i+1, cols:cols}); 
		    			}
		    		}
		    	});
		    },
		    showFieldsRow: function(){
		    	this.dialogRowParamShow = true;
		    },
		    addFieldsRow: function(colNum){
		    	var context = this;
		    	this.dialogRowParamShow = false;
		    	var cols = populateCols(context.rowCols.length + 1, colNum, false);
		    	var ids = grid.jqGrid ('getGridParam', 'selarrrow');
		    	PlatformUI.ajax({
		    		url: contextPath + "/simpleModule/addRowCol",
		    		type: "post",
		    		data: {colNum:colNum, moduleId: ids[0]},
		    		message: PlatformUI.message,
		    		afterOperation: function(response){
		    			context.rowCols.push({id: response.data, row:context.rowCols.length + 1, cols:cols});
		    		}
		    	});
		    },
		    delRow: function(index){
		    	var context = this;
		    	var rowColId = this.rowCols[index].id;
		    	var cols = this.rowCols[index].cols;
		    	var flag = false;
		    	for(var i = 0;i < cols.length;i++){
		    		if(cols[i].id){
		    			flag = true;
		    			break;
		    		}
		    	}
		    	if(flag){
		    		PlatformUI.message({message:"不能删除,删除的行中带有字段数据",type:"warning"});
		    	}else{
			    	PlatformUI.ajax({
			    		url: contextPath + "/simpleModule/delRowCol",
			    		type: "post",
			    		data: {rowColId: rowColId},
			    		message: PlatformUI.message,
			    		afterOperation: function(response){
			    			context.rowCols.splice(index, 1);
			    		}
			    	});
		    	}
		    },
		    showFieldDetailDialog: function(colId, rowIndex, colIndex){//双击弹出字段详细信息
		    	var context = this;
		    	this.fieldMark = rowIndex + "-" + colIndex;
		    	context.validateRules = [];
		    	if(colId){//读取字段信息
		    		PlatformUI.ajax({
						url: contextPath + "/field/" + colId,
						afterOperation: function(data){
							$.extend(context.fieldDetailForm, data);
							PlatformUI.ajax({
								url: contextPath + "/fieldValidateRule/findByFieldId",
								data:{fieldId: colId},
								afterOperation: function(data){
									$(data).each(function(){
										context.validateRules.push({id:this.id, validateType:this.validateType, validateTrigger: this.validateTrigger, errorMsg:this.errorMsg,vValue:this.vValue, fieldId:this.fieldId});
									});									
								}
				    		});
						}
		    		});
		    	}
		    	this.fieldDetailDialogVisible = true;
		    },
		    resetFieldDetailForm: function(){
		    	this.fieldDetailDialogVisible = false;
		    	this.$refs['fieldDetailForm'].resetFields();
				this.fieldDetailForm = {
		        	id:null,
		        	name:null,
		        	codeName:null,
		        	dataType:null,
		        	inputType: null
		        };
		    },
		    showValidateRuleDialog: function(){
		    	this.dialogValidateFieldShow = true;
		    },
		    resetFieldValidateRuleForm: function(){
		    	this.dialogValidateFieldShow = false;
				this.$refs['fieldValidateRuleForm'].resetFields();
				this.fieldValidateRuleForm = {
		        	id:null,
		        	validateType:null,
		        	errorMsg:null,
		        	validateTrigger:null,
		        	vValue: null
		        };
		    },
		    validateTypeSelectChange: function(value){
		    	if(value == "required" || value == "length" || value == "range"){
		    		this.validateValueShow = true;
		    	}else{
		    		this.validateValueShow = false;
		    	}
		    },
		    onFieldDetailSubmit: function(){
				var context = this;
        		this.$refs['fieldDetailForm'].validate(function(valid){
        			if (valid) {
        				var data = $.extend({}, context.fieldDetailForm);
        				data['mark'] = context.fieldMark;
        				var rowIndex = context.fieldMark.split("-")[0];
        				var colIndex = context.fieldMark.split("-")[1];
        				var actionUrl = contextPath + "/field";
        				if(context.fieldDetailForm.id){
        					actionUrl = contextPath + "/field/" + data.id;
				            data['_method'] = "put";
        				}
        				PlatformUI.ajax({
			            	url: actionUrl,
			            	type: "post",
			            	data: data,
			            	message:PlatformUI.message,
			            	afterOperation: function(response){
			            		context.rowCols[rowIndex].cols[colIndex].fieldName = data.name;
			            		if(response.data && response.data.id){
			            			context.rowCols[rowIndex].cols[colIndex].id = response.data.id;
			            		}
			            	}
			            });
			            context.fieldDetailDialogVisible = false;
						context.$refs['fieldDetailForm'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});		    
		    },
		    onValidateRuleSubmit: function(){
				var context = this;
        		this.$refs['fieldValidateRuleForm'].validate(function(valid){
        			if (valid) {
        				if(!context.fieldDetailForm.id){
        					PlatformUI.message({message:"需先保存字段基本信息", type:"warning"});
        					return;
        				}
        				var data = $.extend({}, context.fieldValidateRuleForm);
        				data['fieldId'] = context.fieldDetailForm.id;
        				var actionUrl = contextPath + "/fieldValidateRule";
        				var currentRuleId = context.fieldValidateRuleForm.id;
        				if(currentRuleId){
        					actionUrl = contextPath + "/fieldValidateRule/" + data.id;
				            data['_method'] = "put";
        				}
        				PlatformUI.ajax({
			            	url: actionUrl,
			            	type: "post",
			            	data: data,
			            	message:PlatformUI.message,
			            	afterOperation: function(response){
			            		if(response.data && response.data.id){
									context.validateRules.push({id:response.data.id, validateType:response.data.validateType, validateTrigger: response.data.validateTrigger, errorMsg:response.data.errorMsg,vValue:response.data.vValue, fieldId:response.data.fieldId});			            		
			            		}else{
			            			for(var i = 0; i < context.validateRules.length; i++){
			            				if(context.validateRules[i].id == currentRuleId){
			            					$.extend(context.validateRules[i], data);
			            					break;
			            				}
			            			}
			            		}
			            	}
			            });
			            context.dialogValidateFieldShow = false;
						context.$refs['fieldValidateRuleForm'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
     		},
     		showValidateRule: function(ruleId){
     			var context = this;
     			PlatformUI.ajax({
     				url: contextPath + "/fieldValidateRule/" + ruleId,
     				afterOperation: function(data){
     					$.extend(context.fieldValidateRuleForm, data)
     				}
     			});
     			context.dialogValidateFieldShow = true;
     		},
     		delValidateRule: function(ruleId, index){
     			var context = this;
     			PlatformUI.ajax({
     				url: contextPath + "/fieldValidateRule/" + ruleId,
     				type: "post",
     				data:{_method:"delete"},
     				message: PlatformUI.message,
     				afterOperation: function(data){
     					context.validateRules.splice(index);
     				}
     			});
     		},
     		deleteField: function(){//删除字段
     			var context = this;
     			if(!context.fieldDetailForm.id){
     				PlatformUI.message({message:"没有要删除的数据", type:"warning"});
     				return;
     			}
     			var rowIndex = context.fieldMark.split("-")[0];
				var colIndex = context.fieldMark.split("-")[1];
     			this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		        }).then(function(){
		        	PlatformUI.ajax({
	     				url: contextPath + "/field/" + context.fieldDetailForm.id,
	     				type: "post",
	     				data:{_method:"delete"},
	     				message: PlatformUI.message,
	     				afterOperation: function(data){
	     					context.fieldDetailDialogVisible = false;
							context.$refs['fieldDetailForm'].resetFields();
							context.rowCols[rowIndex].cols[colIndex].fieldName = "请配置字段..";
		            		context.rowCols[rowIndex].cols[colIndex].id = null;
		            		context.fieldMark = null;
	     				}
	     			});
		        });
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

//行列dialog中，填充列字段
function populateCols(rowNum, colNum, flag){
	var colMaxNum = 24;
	if(colNum == 5){//列为5时，对最大列数增1，方便计算
		colMaxNum = colMaxNum + 1;
	}
	var span = colMaxNum / colNum;
	var cols = [];
	var blankFieldName = "请配置字段..";
	for(var i = 0;i < colNum;i++){
		var newSpan = span;
		if(i == colNum - 1){
			if(colNum == 5){
				newSpan = span - 2;
			}else{
				newSpan = span - 1;
			}
		}
		if(flag){
			var mark = rowNum - 1 + "-" + i;
			PlatformUI.ajax({
				url: contextPath + "/field/findByMark",
				data: {mark: mark},
				async: false,
				afterOperation: function(data){
					if(data.id){
						cols.push({id:data.id, fieldName:data.name, span:newSpan});
					}else{
						cols.push({id:null, fieldName:blankFieldName, span:newSpan});
					}
				}
			});
		}else{
			cols.push({id:null, fieldName:blankFieldName, span:newSpan});
		}
	}
	return cols;
}