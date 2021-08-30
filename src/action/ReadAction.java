package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BbsDAO;
import model.BbsDTO;

public class ReadAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.setCharacterEncoding("utf-8");
		
		int bbsno = Integer.parseInt(request.getParameter("bbsno"));
		BbsDAO dao = new BbsDAO();
	    dao.upViewcnt(bbsno);
	    BbsDTO dto = dao.read(bbsno);
	    String content = dto.getContent().replaceAll("\r\n", "<br>");  

	    dto.setContent(content);
	    
	    request.setAttribute("dto", dto);
	    
	    return "/view/read.jsp";
	}

}
