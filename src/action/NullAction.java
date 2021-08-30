package action; 

import javax.servlet.ServletException; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 

public class NullAction implements Action { 

    public String execute( 
        HttpServletRequest request, 
        HttpServletResponse response) 
    throws ServletException { 

        System.out.println("NullAction Loading"); 
         
        // Context Path을 제거한 경로 지정 
        return "/view/nullCommand.jsp"; 
    } 
} 
