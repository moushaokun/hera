
var vue;

var dbTypeMap = PlatformUI.arrayMap();
dbTypeMap.put("oracle", ["oracle.jdbc.OracleDriver", "oracle.jdbc.xa.client.OracleXADataSource", "jdbc:oracle:thin:@[ip]:[1521]:[schema]"]);
dbTypeMap.put("mysql", ["com.mysql.jdbc.Driver", "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource", "jdbc:mysql://[ip]:[3306]/[schema]?useUnicode=true&characterEncoding=utf8&pinGlobalTxToPhysicalConnection=true"]);
dbTypeMap.put("sqlserver", ["com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerXADataSource", "jdbc:sqlserver://[ip]:[1433];databaseName=yzpt"]);
dbTypeMap.put("h2", ["org.h2.Driver", "org.h2.jdbcx.JdbcDataSource", "jdbc:h2:F://data//yzpt"]);
dbTypeMap.put("dm", ["dm.jdbc.driver.DmDriver", "dm.jdbc.xa.DmdbXADataSource", "jdbc:dm://[ip]:[5236]"]);

$(function(){
	
	
	$(document).on('pjax:start', function() { NProgress.start(); });
 	$(document).on('pjax:end',   function() { NProgress.done();  });

	vue = new Vue({
		el: "#app",
		data: function(){
			return {
				fullScreenShow:false,
				dbTypes:["oracle", "mysql", "sqlserver", "h2", "dm"],
				dbType:null,
				consoleDialogShow: false,
				settingsDialogShow: false,
				dbDialogShow: false,
				consoleParams:{
					packageName: null
				},
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
		          ],
		          driverClass: [
		            { required: true, message: '请输入驱动', trigger: 'change' }
		          ],
		          username: [
		            { required: true, message: '请输入用户名', trigger: 'change' }
		          ],
		          xaDsClassName: [
		            { required: true, message: '请输入XA数据源', trigger: 'change' }
		          ]
		        },
		        consoleFormRules: {
		        	packageName: [
		            { required: true, message: '请输入包名', trigger: 'change' }
		          ]
		        }
			};
		},
		methods: {
			downloadMvnSettings: function(){//下载maven  settins文件
				var context = this;
        		this.$refs['settingsParamsForm'].validate(function(valid){
        			if (valid) {
        				PlatformUI.simulateSubmitForm(contextPath + "/downloadMvnSettings", context.settingsParams, "post");
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
			},
			downloadDbConfig: function(){//下载db配置文件信息
				var context = this;
        		this.$refs['dbParamsForm'].validate(function(valid){
        			if (valid) {
        				PlatformUI.simulateSubmitForm(contextPath + "/downloadDBConfig", context.dbParams, "post");
			            context.dbDialogShow = false;
						context.$refs['dbParamsForm'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
			},
			resetDbDialog: function(){
				this.dbDialogShow = false;
				this.$refs['dbParamsForm'].resetFields();
				this.dbParams = {
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
				};
			},
			changeDbType: function(data){
				this.dbParams.url = dbTypeMap.get(data)[2]
				this.dbParams.xaDsClassName = dbTypeMap.get(data)[1]
				this.dbParams.driverClass = dbTypeMap.get(data)[0];
			},
			resetConsoleDialog: function(){
				this.consoleDialogShow = false;
				this.$refs['consoleForm'].resetFields();
				this.consoleParams = {
					packageName: null
				};
			},
			downloadConsoleZip: function(){//下载db配置文件信息
				var context = this;
        		this.$refs['consoleForm'].validate(function(valid){
        			if (valid) {
        				PlatformUI.simulateSubmitForm(contextPath + "/downloadConsoleZip", {packageName: context.consoleParams.packageName}, "post");
        				context.fullScreenShow = true;
        				setTimeout(function() {
				          context.fullScreenShow = false;
				        }, 2000);
			            context.consoleDialogShow = false;
						context.$refs['consoleForm'].resetFields();
			        } else {
			            PlatformUI.message({message:"表单验证失败", type:"error"});
			            return false;
			        }
        		});
			}
		}
	});

});

/******************************方法区******************************/
function linkToPage(url){
	$.pjax({url: contextPath + url, container: '#container', fragment: "#container"});
}
