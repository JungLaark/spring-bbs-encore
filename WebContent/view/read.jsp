<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ include file="/ssi/ssi_bbs.jsp" %>
<%    
    
    BbsDTO dto = (BbsDTO)request.getAttribute("dto");
        
    //페이징, 검색 유지
    String nowPage = request.getParameter("nowPage");
    String col = request.getParameter("col");
    String word = request.getParameter("word");
%> 

<!DOCTYPE html> 
<html> 
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">

  <script type="text/javascript">
    function updateM(){
      var url = "update.do";
      url += "?bbsno=<%=dto.getBbsno()%>";
      
      location.href=url;
    }
    function deleteM(){
      var url = "delete.do";
      url += "?bbsno=<%=dto.getBbsno()%>";
      
      location.href=url;
    }
    function replyM(){
        var url = "reply.do";
        url += "?bbsno=<%=dto.getBbsno()%>";
        
        location.href=url;
      }
    
    function listM(){
        var url = "list.do";
        url += "?nowPage=<%=nowPage%>";
        url += "&col=<%=col%>";
        url += "&word=<%=word%>";
        location.href=url;
      }
  </script>

</head>
<body>  
<div class="container">

<h2>조회</h2>
<div class="panel panel-default">
<div class="panel-heading">작성자</div>
<div class="panel-body"><%=dto.getWname() %></div>

<div class="panel-heading">제목</div>
<div class="panel-body"><%=dto.getTitle() %></div>

<div class="panel-heading">내용</div>
<div class="panel-body" style="height: 200px"><%=dto.getContent() %></div>

<div class="panel-heading">조회수</div>
<div class="panel-body"><%=dto.getViewcnt() %></div>

<div class="panel-heading">등록일</div>
<div class="panel-body"><%=dto.getWdate() %></div>
</div>

<div>
<button type="button" class="btn" onclick="location.href='./create.do'">등록</button>
<button type="button" class="btn" onclick="updateM()">수정</button>
<button type="button" class="btn" onclick="deleteM()">삭제</button>
<button type="button" class="btn" onclick="replyM()">답변</button>
<button type="button" class="btn" onclick="listM()">목록</button>
</div>

</div>
</body> 
</html> 