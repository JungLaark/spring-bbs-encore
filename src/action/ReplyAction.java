package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BbsDAO;
import model.BbsDTO;

public class ReplyAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		int bbsno = Integer.parseInt(request.getParameter("bbsno"));
	    BbsDAO dao = new BbsDAO();
		BbsDTO dto = dao.readReply(bbsno);
		request.setAttribute("dto", dto);
		return "/view/replyForm.jsp";
	}

}
