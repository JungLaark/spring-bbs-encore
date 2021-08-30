<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/ssi/ssi_bbs.jsp" %>
<%
List<BbsDTO> list = (List<BbsDTO>)request.getAttribute("list");
String paging = (String)request.getAttribute("paging");
String col = (String)request.getAttribute("col");
String word = (String)request.getAttribute("word");
int nowPage = (Integer)request.getAttribute("nowPage");
%> 

<!DOCTYPE html> 
<html> 
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
   <script type="text/javascript">
     function read(bbsno){
       var url = "read.do";
       url += "?bbsno="+bbsno;
       url += "&col=<%=col%>";
       url += "&word=<%=word%>";
       url += "&nowPage=<%=nowPage%>";
       location.href=url;

     }
  
  </script>

</head>
<body>
<div class="container">

  <h2>게시판 목록</h2>
  <form class="form-inline" action="./list.do">
    <div class="form-group">
      <select class="form-control" name="col">
        <option value="wname" <% if(col.equals("wname")) out.print("selected");%>>성명</option>
        <option value="title" <% if(col.equals("title")) out.print("selected");%>>제목</option>
        <option value="content"<% if(col.equals("content")) out.print("selected");%>>내용</option>
        <option value="title_content" <% if(col.equals("title_content")) out.print("selected");%>>제목+내용</option>
        <option value="total" <% if(col.equals("total")) out.print("selected");%>>전체출력</option>       
     </select>
    </div>
    <div class="form-group">
      <input type="text" class="form-control" placeholder="Enter 검색어" name="word" value="<%=word%>">
    </div>
    <button type="submit" class="btn btn-default" >검색</button>
    <button type="button" class="btn btn-default" onclick="location.href='./create.do'">등록</button>
  </form>
  
  <table class="table table-striped">
   <thead>
    <tr>
    <th>번호</th>
    <th>제목</th>
    <th>작성자</th>
    <th>grpno</th>
    <th>indent</th>
    <th>ansnum</th>
    </tr>
   </thead>
   <tbody>
   

<%if(list.size() ==0){ %> 
   <tr><td colspan="6">등록된 글이 없습니다.</td>

<%}else{
  
   for(int i=0; i<list.size();i++){
      BbsDTO dto = list.get(i);
      
%> 
   <tr>
    <td><%=dto.getBbsno() %></td>
    <td>
      <%
       for(int r=0; r<dto.getIndent(); r++){
              out.println("&nbsp;&nbsp;");
       }
       if(dto.getIndent()>0)
           // out.print("[답변]");
               out.print("<img src='../images/re.jpg' >");
       %>

   
    <a href="javascript:read('<%=dto.getBbsno() %>')"><%=dto.getTitle() %></a>
    <% if(Utility.compareDay(dto.getWdate().substring(0,10))){ %> 
    <img src="../images/new.gif"> 
    <% } %> 
    
    </td>
    <td><%=dto.getWname() %></td>
    <td><%=dto.getGrpno() %></td>
    <td><%=dto.getIndent() %></td>
    <td><%=dto.getAnsnum() %></td>
   </tr>
<%  
   } //for_end
   
  } //if_end
%>  
   </tbody>
  
  </table>
  <div>
      <%=paging %>
  </div>
</div>
</body> 
</html> 
