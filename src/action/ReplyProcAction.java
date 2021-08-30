package action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BbsDAO;
import model.BbsDTO;

public class ReplyProcAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		request.setCharacterEncoding("utf-8");
		BbsDTO dto = new BbsDTO();
		dto.setWname(request.getParameter("wname"));
		dto.setTitle(request.getParameter("title"));
		dto.setContent(request.getParameter("content"));
		dto.setPasswd(request.getParameter("passwd"));
		dto.setBbsno(Integer.parseInt(request.getParameter("bbsno")));
		dto.setIndent(Integer.parseInt(request.getParameter("indent")));//들여쓰기 이게 관건인데 
		dto.setAnsnum(Integer.parseInt(request.getParameter("ansnum")));//부모글 기준 답변이 달리면 증가 //부모의 indent, ansnum은 0, 답글이 달리면 기존에 있던 답변의 answernum++
		dto.setGrpno(Integer.parseInt(request.getParameter("grpno")));//그룹넘버
		BbsDAO dao = new BbsDAO();
		
		Map map = new HashMap();
		map.put("grpno", dto.getGrpno());
		map.put("ansnum", dto.getAnsnum());

		dao.upAnsnum(map);
		boolean flag = dao.createReply(dto);
		
		request.setAttribute("flag", flag);
		
		return "/view/replyProc.jsp";
	}

}
