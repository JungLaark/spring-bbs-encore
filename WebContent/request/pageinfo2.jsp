<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="request.PageInfo"%>
<% 
PageInfo info = (PageInfo)request.getAttribute("info"); 
//이건 새로운 request다
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 여기서 오류남 -->
	<div style="font-size: xx-large; text-align: center;">
		<h2>test_proc.jsp 입니다.</h2>
		nowPage:
		<%=info.getNowPage() %>
		<br /> searchColumn:
		<%=info.getSearchColumn() %>
		<br /> searchWord:
		<%=info.getSearchWord() %>
		<br />

	</div>
</body>
</html>