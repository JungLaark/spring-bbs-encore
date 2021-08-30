package properties; 

import java.io.File; 
import java.io.FileInputStream; 
import java.io.IOException; 
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.util.Iterator; 
import java.util.Properties; 

public class PropertyTest { 

    public static void main(String[] args) { 
    	
        Properties prop = new Properties(); 
         
        FileInputStream fis = null;   // 파일을 읽어 오는 역활을 합니다. 
        //  경로  산출 
        File file = new File("jdbc.properties"); 
        System.out.println("절대경로: " + file.getAbsoluteFile()); 
                
        try { 
        	
            fis = new FileInputStream(file); 
            //속성 파일을 객체로 로딩, 키와 문자열 값으로 저장됩니다. 
            prop.load(fis); 
            
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            if (fis != null) 
            	try { fis.close(); 
            	} catch(IOException ex) 
            {} 
        } 
         
        // properties 객체에서 키목록을 추출합니다. 
        // driver, url, account, password 
        //set 키 중복 안되고 순서 상관없는 key 
        Iterator keyIter = prop.keySet().iterator(); 
        
        while( keyIter.hasNext() ) { 
            String key = (String)keyIter.next();  //키 추출 
            String value = prop.getProperty(key); //키에 따른 값 추출 
            System.out.println(key + "=" + value); 
        } 
         
        Connection con = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        String sql = ""; 
        String driver = ""; 
        String url = ""; 
        String account = ""; 
        String password=""; 
         
        driver = prop.getProperty("driver"); 
        url = prop.getProperty("url"); 
        account = prop.getProperty("account"); 
        password = prop.getProperty("password"); 

        try{ 
            Class.forName(driver); 
            con = DriverManager.getConnection(url, account, password); 
            sql = "SELECT count(*) cnt FROM tab"; 
            pstmt = con.prepareStatement(sql); 
            rs = pstmt.executeQuery(); 
            if (rs.next()){ 
                System.out.print(account + " 계정의 테이블 갯수: "); 
                System.out.println(rs.getInt("cnt")); 
            } 
        }catch(Exception e){ 
            System.out.println(e); 
        }         
    } 
} 