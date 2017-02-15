<#macro html activeNavItem>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="javascript:location.href = contextPath">Code Robot</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<#if activeNavItem == "工程管理">
					  <li class="active"><a href="${request.contextPath}/project/index">工程管理</a></li>
					<#else>
					  <li><a href="${request.contextPath}/project/index">工程管理</a></li>
					</#if>
					
					<#if activeNavItem == "常规模块管理">
					  <li class="active"><a href="${request.contextPath}/simpleModule/index">常规模块管理</a></li>
					<#else>
					  <li><a href="${request.contextPath}/simpleModule/index">常规模块管理</a></li>
					</#if>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
</#macro> 