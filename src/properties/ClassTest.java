package properties; 

/** 표준 인터페이스 */ 
interface Action{ 
    public void execute(); 
} 

class Spring implements Action{ 
    public void execute(){ 
        System.out.println("따뜻한 봄 입니다. - 새싹"); 
    } 
} 

class Summer implements Action{ 
    public void execute(){ 
        System.out.println("더운 여름입니다. - 바다"); 
    } 
} 

class Fall implements Action{ 
    public void execute(){ 
        System.out.println("시원한 가을입니다. - 등산"); 
    } 
} 

class Winter implements Action{ 
    public void execute(){ 
        System.out.println("눈이오는 겨울입니다. - X-MAS"); 
    } 
} 

public class ClassTest { 

    public static void main(String[] args) { 
        
        String className = args[0]; // 실행할 클래스명, 패키지 포함    
        try{ 
        	//어떤 클래스 타입이 올지 모를 때 Class.forName() 쓴다. Run Configuration / Argument / 패키지명.클래스이름 넣기 : properties.Winter 
            Class object = Class.forName(className); // JVM으로 소스 로딩 
            Action instance = (Action)object.newInstance(); // 객체 생성 
            instance.execute(); 
        }catch(Exception e){ 
            System.out.println(e); 
        } 

    } 

} 