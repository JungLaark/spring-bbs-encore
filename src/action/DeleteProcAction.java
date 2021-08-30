package action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BbsDAO;

public class DeleteProcAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		int bbsno = Integer.parseInt(request.getParameter("bbsno"));
		String passwd = request.getParameter("passwd");
		
		Map map = new HashMap();
		BbsDAO dao = new BbsDAO();
		
		map.put("bbsno", bbsno);
		map.put("passwd", passwd);

		boolean flag = false;
		boolean pflag = dao.passCheck(map);
		if (pflag) {
			flag = dao.delete(bbsno);
		}

		request.setAttribute("flag", flag);
		request.setAttribute("pflag", pflag);
		return "/view/deleteProc.jsp";
	}

}
