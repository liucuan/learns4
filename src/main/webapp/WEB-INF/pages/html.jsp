<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>

</head>
<body>
<input type="button" value="call" onclick="call();"/>
<input type="button" value="deferred" onclick="deferred();"/>


<script type="text/javascript" src="<c:url value="/js/jquery.js" />"></script>
<script type="text/javascript">

	function call(){
		$.get('call',function(data){
			console.log(data);
		});
	}


	function deferred(){
		$.get('defer',function(data){
			console.log(data);
			deferred();//每次请求完成,再发一次请求,避免客户端定时刷新来获取数据
		});
	}


</script>
</body>
</html>