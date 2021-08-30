<%@ page contentType="text/html; charset=UTF-8"%>
<%
String CONTENT_PAGE = (String) request.getAttribute("CONTENT_PAGE");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<!-- 상단 메뉴 -->
	<jsp:include page="./top.jsp"></jsp:include>
	<!-- 상단 메뉴 끝 -->

	<!-- 내용 시작 -->
	<div style="width: 100%; padding-top: 30px;">
		<jsp:include page="<%=CONTENT_PAGE%>" flush="false" />
	</div>
	<!-- 내용 끝 -->

</body>
</html>