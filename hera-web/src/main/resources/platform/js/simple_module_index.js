

$(function(){
	
	$(document).on('pjax:start', function() { NProgress.start(); });
 	$(document).on('pjax:end',   function() { NProgress.done();  });
	
});


/******************************方法区******************************/
function linkToPage(url){
	$.pjax({url: contextPath + url, container: '#container', fragment: "#container"});
}