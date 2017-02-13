
var vue;

$(function(){

	vue = new Vue({
		el: "#app",
		data: function(){
			return {
				dbTypes:["oracle", "mysql", "sqlserver", "h2", "dm"],
				dbType:null,
				settingsDialogShow: false,
				dbDialogShow: false,
				settingsParams:{
					repositoryLocation: null,
					nexusUser: null,
					nexusPassword: null,
					nexusReleaseAddress: null,
					nexusSnapshotAddress: null,
					jdkVersion: null
				},
				dbParams:{
					url: null,
					driverClass: null,
					username: null,
					password: null,
					xaDsClassName: null,
					initialSize: null,
					minIdle: null,
					maxActive: null,
					maxWait: null,
					timeBetweenEvictionRunsMillis: null,
					minEvictableIdleTimeMillis: null,
					validationQuery: null,
					testWhileIdle: null,
					testOnBorrow: null,
					testOnReturn: null,
					poolPreparedStatements: null,
					maxPoolPreparedStatementPerConnectionSize: null,
					dialect: null
				},
				settingsFormRules: {
		          repositoryLocation: [
		            { required: true, message: '请输入本地仓库名称', trigger: 'change' }
		          ]
		        },
		        dbFormRules: {
		          url: [
		            { required: true, message: '请输入url', trigger: 'change' }
		          ]
		        }
			};
		},
		methods: {
			downloadMvnSettings: function(){//下载maven  settins文件
				var context = this;
        		this.$refs['settingsParamsForm'].validate(function(valid){
        			var params = {
        				repositoryLocation: context.settingsParams.repositoryLocation,
						nexusUser: context.settingsParams.nexusUser,
						nexusPassword: context.settingsParams.nexusPassword,
						nexusReleaseAddress: context.settingsParams.nexusReleaseAddress,
						nexusSnapshotAddress: context.settingsParams.nexusSnapshotAddress,
						jdkVersion: context.settingsParams.jdkVersion
        			};
        			if (valid) {
        				PlatformUI.simulateSubmitForm(contextPath + "/downloadMvnSettings", params, "post");
			            context.settingsDialogShow = false;
						context.$refs['settingsParamsForm'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
			},
			showSettingsDialog: function(){
				this.settingsDialogShow = true;
			},
			resetSettingsDialog: function(){
				this.settingsDialogShow = false;
				this.$refs['settingsParamsForm'].resetFields();
				this.settingsParams = {
					repositoryLocation: null,
					nexusUsername: null,
					nexusPassword: null,
					nexusReleaseAddress: null,
					nexusSnapshotAddress: null,
					jdkVersion: null
				};
			},
			downloadBootConfig: function(){//下载spring boot配置文件信息
				PlatformUI.simulateSubmitForm(contextPath + "/downloadBootConfig", null, "post");
			}
		}
	});

});

