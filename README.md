# spring-bbs-encore
엔코아 Spring bbs 프로젝트 


1. Spring 4.9.0 설치 
2. 자바 환경변수 설정 
    1) JAVA_HOME : C:\Program Files\Java\jdk1.8.0_241
    2) CLASSPATH : .;%JAVA_HOME%\lib\tool.jar
3. Tomcat 
    1) C:\Program Files\Apache Software Foundation\Tomcat 9.0\conf\server.xml
    2) server.xml 69라인 : URIEncoding="UTF-8" 추가(GET방식 일 경우에 추가, POST는 소스 상에서 설정)
    3) bin 폴더에서 톰캣 실행 / web에서 localhost:8000 으로 확인 
4. Oracle 
    1) Run SQL Command Line 실행    
    2) system으로 Login
        - CREATE USER JCS IDENTIFIED BY JCS;
        - GRANT CREATE SESSION TO JCS;
        - GRANT CONNECT, RESOURCE TO JCS;
        - SELECT * FROM TAB;

        CREATE TABLE bbs ( 
            bbsno    NUMBER(7)       NOT NULL,   -- 글 일련 번호, -9999999 ~ +9999999 
            wname   VARCHAR(20)    NOT NULL,   -- 글쓴이 
            title        VARCHAR(100)  NOT NULL,   -- 제목(*) 
            content   VARCHAR(4000) NOT NULL,  -- 글 내용 
            passwd   VARCHAR(15)     NOT NULL,  -- 비밀 번호 
            viewcnt   NUMBER(5)       DEFAULT 0,  -- 조회수, 기본값 사용 
            wdate     DATE               NOT NULL,  -- 등록 날짜, sysdate 
            grpno     NUMBER(7)       DEFAULT 0, -- 부모글 번호 
            indent    NUMBER(2)       DEFAULT 0,  -- 답변여부,답변의 깊이
            ansnum  NUMBER(5)       DEFAULT 0,  -- 답변 순서 
            PRIMARY KEY (bbsno)  
            ); 

5. STS 4.9.0
    1)Window/Preference/General/Content Types/
    2)Text 클릭
    3)default encoding : UTF-8로 변경 
    4)General/Web Browser -> chrome으로 변경 
    5)General/Workspace Text file encoding -> Other : UTF-8
    6)General/Web -> UTF-8로 변경 
    7)General/Server/Runtime Environment tomcat 경로 추가 

    8)Dynamic Web Project 메뉴 생성 : Help/Install new Software/Work with 에서 맨 마지막거 클릭 
    9)Eclipse 와 JPA 있는 것들 설치 
    10)Create a project-Web-Dynamic Web Project 있는지 확인. 있으면 완료
    11)프로젝트/Properties/Project Facets 선택 / java -> 1.8로 변경 
    12)runtime 탭에서 Tomcat 버전 변경 
    13)C:\JCS\8.Spring\SpringWorkspace\mvc_bbs_20210302\WebContent\WEB-INF\config\config.properties
       WebContent\WEB-INF\web.xml 에서 수정 진행 
6.MVC 
    1)Beans 라는 것은 기존의 VO, DAO 포함 
    2)mvc_bbs_20210302/src/properties/ 클래스 연습하기 -> 새로 만들어보기 
    3)request, response, redirect 복습하기 
7.MVC의 구현(Service, Action)
    1)템플릿 기반의 URI Command Pattern에 기반한 MVC의 구현 
        - Dynamic Web Project 생성 
        - Generate web.xml deployment descriptor 클릭 
        - Command Handler를 Action한테 맡긴다 
        - WebContent/view 
        - WebContent/template -> 변하지 않는 메뉴들 
        - WEB-INF/config -> 명령어, 요청하는 주소 설정, tomcat 에 요청할 것들 만듬
        - WEB-INF/lib -> connector 들어감  
    2)WEB-INF/web.xml 파라미터, url mapping 
    3)Template
        -공통적인 ui 정의
        -주기마다 ui 가 바뀌니 코드 최소화 

[2021.03.03 수요일] 
0.Do, Get은 콜백함수
1.forward 방법
    1)pageContext.forward("~.jsp");
    2)<jsp:forward page="~.jsp"/>
    3)RequestDispatcher rd = request.getRequestDispatcher("~.jsp");
      rd.forward(request, response);
2.forward, sendRedirect 차이 
    1)forward : 새로운 page에서 request, response 그대로 사용 가능 /URL 변화 없음
    2)redirect : request, response 새롭게 생성 /URL 변화 있음
    response.sendRedirect

    session, DB에 변화가 생기는 요청(로그인, 회원가입, 글쓰기) ->redirct
    단순 조회 ->forward
    
    예제에서는 template.jsp 로 고정이 되어있다고 생각하면 되겠다. 

3.1)top.jsp 에 myinfo 하이퍼링크 추가 
  2)config.properties 에 추가
  3)action 추가 
  4)myinfo.jsp 추가
4.1)top.jsp 에 teamlist 추가 
  2)List<Map> list = new ArrayList<map>();
  2.1)Map<String, String> map = new Map<String, String>();
  3)<%for(){ %>
  4)<%}%>
  5)<%=value%>

  서블릿은 요청할 수 있는 클래스이다.
  컨테이너 즉. TOMCAT 에다가 서블릿 사용 명세 -> WEB.XML 
5.BBS 추가 (20210302_mvc_bbs)
    0)button 의 기본 타입은 submit
    1)request.setCharacterEncoding("UTF-8"); -> POST 방식의 인코딩 
    1)글 전체 목록 
    2)글 등록
    3)글 수정 
    4)글 삭제
    5)답변 달기
    98)검색어 검색
    99)페이징 - oracle : rownum 사용 좀 더 생각해봐야 한다  
    100)new image 나오는 방법... 와우 

    SELECT * FROM BBS;
    DELETE FROM BBS;

    INSERT INTO bbs(bbsno, wname, title, content, passwd, wdate,grpno)  
    VALUES((SELECT NVL(MAX(bbsno), 0) + 1 as bbsno FROM bbs), 
    '왕눈이', '제목', '내용', '123', sysdate,
    (SELECT NVL(MAX(grpno), 0) + 1  FROM bbs)
    );

    SELECT bbsno, wname, title, viewcnt, wdate, grpno, indent, ansnum
    FROM bbs  
    ORDER BY grpno DESC, ansnum ASC; 

    SELECT  BBSNO, GRPNO, INDENT, ANSNUM, rownum
    FROM bbs
    ORDER BY GRPNO DESC, ANSNUM ASC;

    SELECT bbsno, wname, title, viewcnt, wdate, grpno, 
    indent, ansnum, rownum AS R
    FROM ( 
        SELECT bbsno, wname, title, viewcnt, wdate, grpno,
        indent, ansnum 
        FROM bbs  
        ORDER BY grpno DESC, ansnum ASC 
    )WHERE ROWNUM >= 6 AND ROWNUM  <= 10;

    --ROWNUM 쓸 수 없다 제대로 // FROM 다음에 WHERE 

    101)페이징 하는 쿼리 
    SELECT bbsno, wname, title, viewcnt, wdate, grpno, 
    indent, ansnum,  r 
    From(
        SELECT bbsno, wname, title, viewcnt, wdate, grpno, 
        indent, ansnum, rownum as r
        FROM ( 
            SELECT bbsno, wname, title, viewcnt, wdate, grpno,
            indent, ansnum
            FROM bbs  
            ORDER BY grpno DESC, ansnum ASC 
        ) 
    )where r >= 3 and r <= 5

[2021.03.04 목요일]
1.Spring (http://lectureblue.pe.kr/reqtiles/list.jsp?col=&word=&nowPage=2&code=56) 13회차
    0)Framework
        0.1)빠른 구현시간
        0.2)쉬운 관리
        0.3)개발자들의 역량 획일화
        0.4)스트럿츠 
    1)Spring Framework 
        1.1)Lightweight 경량 : POJO
        1.2)Inversion of Control 제어 역행 : 객체의 생성, 생명주기를 Container들이 전담 ,개발자에서 Container(Servlet의 init)로 제어권이 바뀜
        1.3)Dependency Injection 의존성 관리 : 객체끼리 서로 의존한다. Framework가 의존성 관리(web.xml)
        1.4)Aspect-oriented Programming 관점지향 프로그래밍 : 반복적인 코드 줄이고, 핵심 비즈니스 로직(계죄이체, 이자계산)에만 집중
        1.5)Container : 특정 객체의 생성과 관리는 담당. 일반적으로 서버안에 포함 배포 구동, EJB J2EE   
    2)IoC 컨테이너 4가지 클래스 유형 - 14회차 
        2.1)Spring Legacy Project 
            2.1.1)maketplace에서 Spring Tools 3 AddOn for Spring Tool 4 설치 
            2.1.2)File-New-Other-Spring-Spring Legacy Project-Simple Spring Utility Project
            2.1.3)이름쓰고 toplevelPackage 는 com.df.df 이런 식으로 쓴다. 
            2.1.4)프로젝트명/properties/Project Facet/Java 1.8로 변경/runtime에서 jre체크 
            2.1.5)src/main/java - class 파일
                  src/main/resource - 관리파일
                  src/main/test - 테스트 관련
                  Maven - 자바 프로젝트의 빌드를 자동화하는 빌드 툴 
            2.1.6)pom(Project Object Model).xml - spring.framework.version에서 4.3.14로 변경 
            2.1.7)프로젝트명/maven/update project 하면 빨간색 모두 없어짐 (alt + F5)
            2.1.8)클래스의 결합도가 높다 낮다 응집도 -> vo 와 main 이 붙어있다? 
            2.1.9)캡슐화/디자인 패턴 
                2.1.9.1)팩토리 메서드 패턴 : 객체를 생성하기 위한 클래스 정의
            2.1.10)coupling 패키지 -> polymorphism 패키지 -> Factory 패키지 (Bean Factory 이거 연습)
            2.1.11)Run Configuration / java application 오른쪽 클릭 new configuration -> Main class 선택 facroty로 / argument 넣고 run 
            2.1.12)스프링 IoC(Inversion of control)를 이용
                2.1.12.1)ioc package 생성 
                2.1.12.2)src/main/resources/package명/META-INF/spring/app-context.xml
                         -> <bean id="tv" class="ioc.SamsungTV"></bean> 이거 추가 (태그, 속성)
                         -> 컨테이너가 app-context.xml 을 참조하여 객체를 관리한다. 관리되는 Object를 Bean이라고 부른다.
                2.1.12.3)Main함수 
                        -> AbstractApplicationContext factory = GenericXmlApplicationContext("META-INF/spirng/app-context.xml")
                        -> 기존의 tomcat 설정을 위한 web.xml과 비슷
                        -> 서블릿 컨테이너? 톰캣 안에 있고 web.xml 안에 있는 서블릿만 객체 생성 
    3)Spring XMl 설정
        3.1) <beans> 루트 엘리먼트 
            - 스프링 컨테이너는 <bean> 저장소에서 해당 xml 설정 파일을 참조하여 <bean>생명주기를 관리하고 여러가지 서비스를 제공한다.
            - <bean>, <description>, <alias>, <import> 등 네개의 자식 엘리멭트로 사용 할 수 있다. 이 것들 중 <bean>, <import>가 실제 프로젝트에 사용된다.
            3.1.1) <import> 엘리먼트 
                    -각각의 설정을 각각의 파일로 나누어 설정 후 import 할 수 있음 
                        <beans>
                            <import resource = "context-datasource.xml"></import>
                            <import resource = "context-transaction.xml"></import>
                        </beans>
        3.2) <bean> 엘리멘트
            - id, name 은 유일해야 한다.
            3.2.1)init-method 속성 
                - 스프링 설정 파일에 등록된 클래스를 객체 생성할 때 맴버변수 초기화 위해 존재 
            3.2.2)destroy-method 속성 
            3.2.3)lazy-init 속성 
                -<bean lazy-init="true"></bean> : <bean>을 미리 생성하지 않고 클라이언트가 요청시점에 생성
                -<bean lazy-init="default"></bean> : 컨테이너 객체가 생성되면 미리 가져옴 
            3.2.4)scope 속성 
                - <bean> 객체가 하나만 또는 getBean() 호출 시 마다 생성 할 수 있도록 설정 
                - <bean scope="singleton"> 하나만 생성, 기본값.
                - <bean scope="prototype"> getBean() 호출 시 마다 생성
    4)의존성 관리 - dependency injection 
        4.1)Dependency Lookup : getBean("") 으로 가지고 오는 방법
        4.2)Dependency Injection : 스프링 설정파일에 등록된 정보를 바탕으로 자동으로 처리
                                   Constructor Injection, Setter Injection 두가지 있음
            4.2.1)coupling 패키지/lgTV 클래스에서 예제 
            4.2.2)Constructor Injection : <bean> 하위에 <constructor-arg ref="speaker">사용 
                                          <bean id="speaker" class="ioc.AppleSpeaker"></bean>등록
                4.2.2.1)다중 매개변수 매핑 - 생성자 전달 값이 기본형이면 value 속성 사용 
                                          생성자 전달 값이 참조형이면 ref 속성 사용 
                                          ioc 패키지 참고 
                                        ex)
                                        <bean id="tv" class="ioc.SamsungTV" init-method="initMethod" destroy-method="destroyMethod" scope="prototype">
                                            <!-- 다른 클래스 참조형 -->
                                            <constructor-arg ref="speaker"></constructor-arg>
                                            <!-- 자기 클래스 기본형 -->
                                            <constructor-arg value="270000"></constructor-arg>
                                         </bean>
                                        <bean id="speaker" class="ioc.AppleSpeaker"></bean>

            4.2.3)Setter Injection : 클래스의 의존관계를 setter 메소드를 호출하여 처리 
                                     - AbstractApplicationContext factory = new GenericXmlApplicationContext("META-INF/spring/app-context.xml");
                                     - TV tv2 = (TV)factory.getBean("tv2");//app-context.xml 의 아이디
                4.2.3.1) ex) 
                        <bean id="tv2" class="ioc.LgTV">
                            <property name="speaker2" ref="speaker"></property>
                            <property name="price" value="1234455"></property>
                            name은 필드명, setter 이름과 동일하여야 한다. setSpeaker2
                        </bean>
            4.2.4)p namespace 사용 - http://www.springframework.org/schema/p
                                  - p:변수명="기본타입 데이터" 
                                    p:변수명-ref="참조타입"  
            4.2.5)Collection Type 프로퍼티 설정 
                4.2.5.1) New -> Other -> Spring -> Spring Bean Configuration File >>> src/main/resources/app-context2.xml 생성
                4.2.5.2) p prefix 추가
                4.2.5.3) coll 패키지 만들기
                4.2.5.4) CollectionBean 클래스 setter, getter 
                    [app-context.xml]
                    <bean id="coll_list" class="coll.CollectionBean">
                        <property name="list">
                            <list>
                                <value>대구시 북구 대현동</value>
                                <value>서울시 강남구 역삼동</value>
                                <value>Laark</value>
                            </list>		
                        </property>
                        <property name="map">
                            <map>
                                <entry>
                                    <key>
                                        <value>Laark</value>
                                    </key>
                                    <value>대구시 북구 대현동</value>
                                </entry>
                                <entry>
                                    <key>
                                        <value>Hello</value>
                                    </key>
                                    <value>Spring</value>
                                </entry>
                            </map>
                        </property>
                        <property name="proper">
                            <props>
                                <prop key="1">대구시 북구 대현동</prop>
                                <prop key="2">Hello Spring</prop>
                            </props>
                        </property>
                    </bean>
                                    
                    [CollectionBean.java]

                        public class CollectionBean {
        
                        private List<String> list;
                        private Map<String, String> map;
                        private Properties proper;
                        
                        public CollectionBean() {
                            System.out.println("CollectionBean 클래스 기본 생성자 호출 ");
                        }
                        
                        public void setList(List<String> list) {
                            this.list = list;
                            System.out.println("setList() 호출");
                        }
                        
                        public List<String> getList(){
                            return list;
                        }
                        
                        public void setMap(Map<String, String> map) {
                            this.map = map;
                        }
                        
                        public Map<String, String> getMap(){
                            return this.map;
                        }
                        
                        public void setProper(Properties proper) {
                            this.proper = proper;
                        }
                        
                        public Properties getProper() {
                            return this.proper;
                        }
                   }

                   [CollectionBeanClient.java]

                    AbstractApplicationContext factory = new GenericXmlApplicationContext("app-context2.xml");
		
                    CollectionBean coll_list = (CollectionBean)factory.getBean("coll_list");
                    //CollectionBean coll_map = (CollectionBean)factory.getBean("coll_map");
                    //CollectionBean coll_proper = (CollectionBean)factory.getBean("coll_proper");
                    
                    List<String> list = coll_list.getList();
                    Map<String, String> map = coll_list.getMap();
                    Properties proper = coll_list.getProper();
                    
                    System.out.println("**list**");
                    for(String a : list)
                        System.out.println("value : " + a);
                    System.out.println("**map**");
                    System.out.println(map.get("Laark"));
                    System.out.println(map.get("Hello"));
                    System.out.println("**Property**");
                    System.out.println(proper);
                    
                    factory.close();

                    코드 옮기기 : 블록지정 + Alt + 방향키

                    그냥 초기화하는 역할로.. reference 타입으로 주로 쓴다. 

                    정리 : 설정들을 컨테이너에 맡긴다 
[2021.03.05 금요일]
    5)Annotation 설정 
        5.1) Context Namespace 추가 
        5.2) @Component 추가 (SamsungTV, AppleSpeaker) - getBean("samsungTV") 클래스 이름 대문자 -> 소문자 바꿔줌
            5.2.1) @Component("tv") 이름 명시 -> getBean("tv")
        5.3) @Autowired 추가 (@Injection과 동일한 기능 제공) : 의존하는 객체 주입, 필드,메소드,setter,생성자에서 사용 가능
            5.3.1) Qualifier("apple") : DI 객체가 중복될 때 -> implement 가 다수일 경우 사용 
        5.4) @Require : 필수 프로퍼티 setter 메소드에서 
             @Controller : Action이 Spring에서는 Controller인데 그 때 사용 
             @Service : service 단 
             @Repository : DAO
        5.5) Annotaion 썼을 때 필드 초기화는 세터로? 이런 건 Bean 으로 추가해서 해야 함. 
    6)Spring MVC 구조 
        6.1)DispatcherServlet : Front Controller 기존의 서블릿 클래스, Controller(기존의 Action)에게 요청을 전달하고, 결과값을 View에 전달
        6.2)HandlerMapping : 기존의 Config.properties, 클라이언트의 요청을 어떤 Controller(Action) 가 처리할지 결정. 매핑관리
        6.3)Controller(Action) : 클라이언트의 실질적인 요청 처리. Model 결과를 DispatcherServlet에 반환 
        6.4)Model : 
        6.5)ViewResolver : View 선택 
        6.6)View : JSP
    7)Spring MVC 처리 순서 
        7.1)순서
            7.1.1) 요청을 DispatcherServlet 이 받음
            7.1.2) HandlerMapping 이 적절한 Controller 선택 
            7.1.3) Controller 가 처리하여 Model에 담아 return 
            7.1.4) ViewResolver는 DispathcerServlet 에서 온 것을 토대로 View 객체 찾음 (servlet-context.xml)
            7.1.5) View 보여짐
    8)DispatcherServlet 설정, Spring Context, 한글 설정 (web.xml 에 정의)
        8.0)<-class> : 실질적인 클래스 
        8.1)Spring Context : 공통으로 사용할 설정 개체. 
        8.2)Filter : 요청 및 결과를 걸러줌. 인터페이스 
        8.3)<servlet-mapping>
                <url-pattern>/</url-pattern> -> 모든 요청을 받겠다. 기존에는 *.do 로 해서 do로 끝나는 거만 받았는데 
    9)Spring MVC 개발 - 19번 
        9.1)Context : 스프링이 사용하는 메모리 영역이 생성되는데 이를 Context라고 한다? jsp에서도 봤던거같은데 
                      ApplicationContext
        9.2)servlet-context.xml : 관리해야 할 객체들 리스트 
        9.3)Annotaion 
            9.3.1)@Controller : Action. 클래스 타입에만 적용, 웹 요청처리 시 사용 
            9.3.2)@RequestMapping : 컨트롤러가 처리할 Get/Post // 클래스, 메소드에서 사용 가능 
            9.3.3)@GetMapping
            9.3.4)@PostMapping
            9.3.5)컨트롤러 메소드의 매개변수 타입
            9.3.6)컨트롤러 메소드의 리턴 형 
                9.3.6.1)@ResponseBody : data 형
            9.3.7)@RequestMaram : public String edit(@RequestParam("id") int id)
            9.3.8)@ModelAttribute : public void update(@ModelAttribute("board") Board board)

            Context : 메모리에 로드된 프로그램을 사용할 수 있는 영역

        9.4)Local 저장소 변경 
            9.4.1)메이븐 라이브러리 이동 
                    -원래는 C:\Users\JCS\.m2\repository
                <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
                                            http://maven.apache.org/xsd/settings-1.0.0.xsd"> 
                    <localRepository> C:/JCS/8.Spring/maven</localRepository> 
                    <interactiveMode>true</interactiveMode> 
                    <usePluginRegistry>false</usePluginRegistry> 
                    <offline>false</offline> 
                    <pluginGroups> 
                        <pluginGroup>org.codehaus.mojo</pluginGroup> 
                    </pluginGroups> 
                </settings> 
            
                1.경로 변경
                2.C:\JCS\8.Spring\maven\settings.xml 에 저장
                3.Eclipse STS -> Window -> Preferences -> Maven -> User Settings - User Settings: 새로운 maven 경로 저장
                4.Update Settings 클릭 and Apply and 재시작 
                5.프로젝트 오른쪽 클릭/maven/update project 

                 **404에러 날 경우 
                 : * 프로젝트 우클릭 >  properties
                   * Deployment Assembly > Add 버튼 > Java Build Path Entries > Maven Dependencies 선택 > Apply버튼

    10)Spring MVC 프로젝트 - 20번
        10.1)프로젝트 생성 : file-new-other-spring-lagecy-이름 넣고 Spring MVC Project-패키지명 spring.sts.webtest
        10.2)패키지 생성 : src/main/java 밑에 Package: spring.sts.webtest, spring.model.bbs, spring.utility.webtest 3개 생성 
        10.3)servlet-context.xml : 이거 뭐하는거라고 했노 
        10.4)WEB-INF/lib/ojdbc8.jar 추가 
        10.5)POM.xml - java version - 1.8
                       springframework version - 4.3.14
        10.6)webapp/storage 생성 - 파일 업로드 용  /// webapp == webContent
             webapp/ssi 생성
             webapp/images 생성
             WEB-INF/bbs 에 jsp파일 가져오기 createForm.jsp
             WEB-INF/web.xml 에 filter 추가 ->
                <filter>
                    <filter-name>encodingFilter</filter-name>
                <filter-class>
                org.springframework.web.filter.CharacterEncodingFilter
                </filter-class>
                <init-param>
                <param-name>encoding</param-name>
                <param-value>UTF-8</param-value>
                </init-param>
                    </filter>
                
                <filter-mapping>
                    <filter-name>encodingFilter</filter-name>
                    <url-pattern>/*</url-pattern>
                </filter-mapping>

             servlet-context.xml 에 추가 ->
                   <context:component-scan base-package="spring.model.bbs" /> 

            HomeController.java
            @RequestMapping(value="/", method=RequestMethod.GET)
            public String home(Locale, Model){}


        10.7)project Facets - java 1.8, runtime 톰캣 체크 
        10.8)File Upload 추가 
            10.8.1)sql 수정 - ALTER TABLE BBS 
                             ADD(FILENAME VARCHAR(50), FILESIZE NUMBER(7) DEFAULT 0);
            10.8.2)client 설정 - jsp 내에서 <form method="post" enctype="application/x-www-form-urlencoded"></form> 이 enctype은 문자열만됨
                   server 설정 - upload 된 파일을 참조할 수 있는 타입(클래스)가 필요함.. 자바에는 없는데 스프링에는 있음
            10.8.3)추가

                    >>> WEB-INF/spring/root-context.xml 
                    <!--    파일 전송이 있는 경우만 선언, File upload   --> 
                    
                        <bean id="multipartResolver" 
                            class="org.springframework.web.multipart.commons.CommonsMultipartResolver"> 
                        </bean> 
                    
                    >>> pom.xml 
                    <!-- fileupload setting -->                
                            <dependency> 
                            <groupId>commons-fileupload</groupId> 
                            <artifactId>commons-fileupload</artifactId> 
                            <version>1.2.1</version> 
                            </dependency> 
                            <dependency> 
                            <groupId>commons-io</groupId> 
                            <artifactId>commons-io</artifactId> 
                            <version>2.4</version> 
                            </dependency>  
        10.9)각종 파일 추가 - dao, dto, utility패키지
                          - dto의 create 함수 수정
                          - 쿼리문 수정한거 테스트하기위해 
        10.10)Controller 생성 - C:\JCS\8.Spring\SpringWorkspace\20210305_spring_test\src\main\java\spring\sts\webtest/BbsController.java
        10.11)Utility.java 에 업로드, 삭제 함수 추가 
        10.12)createForm.jsp -> form에서 enctype="multipart/form-data" 추가 
                - SEVERE: 클래스 [org.springframework.web.context.ContextLoaderListener]의 애플리케이션 리스너를 설정하는 중 오류 발생
                                java.lang.ClassNotFoundException: org.springframework.web.context.ContextLoaderListener
                해결책 : https://myblog.opendocs.co.kr/archives/1657

        10.13)실제 업로드된 파일 경로: C:\JCS\8.Spring\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\20210305_spring_test\storage
                                실행공간, 배포공간 다름, 톰캣에 배포하게 됨 
        10.14)View 수정 - Maven:Tiles2
              http://mvnrepository.com/ 
             -> https://mvnrepository.com/artifact/org.thymeleaf.extras/thymeleaf-extras-tiles2/2.0.1 
             pom.xml에 추가 
             10.14.1)tiles2 설정 - servlet-context.xml 에서는 우선순위 변경 
                                    <beans:property name="order" value="2" />

                                - root-context.xml 에서는 이거 추가 
                                <bean id="tilesconfigurer" class="org.springframework.web.servlet.view.tiles2.TilesConfigurer"> 
                                    <property name="definitions"> 
                                        <list> 
                                            <value>/WEB-INF/spring/bbs.xml</value> 
                                        </list> 
                                    </property> 
                                </bean>  
                                <bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver"> 
                                    <property name="viewClass"> 
                                        <value>org.springframework.web.servlet.view.tiles2.TilesView</value> 
                                    </property> 
                                    <!-- 밑에 꺼는 우선순위 -->
                                    <property name="order" value="1"></property>
                                </bean> 
                                
                                -기준이 되는 bbs.xml 추가 
                                <tiles-definitions> 
                                    <definition name="main" template="/WEB-INF/views/template/template.jsp"> 
                                        <put-attribute name="header" value="/WEB-INF/views/template/top.jsp" /> 
                                    </definition> 
                                    <definition name="/home" extends="main">
                                    <put-attribute name="title" value="기본페이지"></put-attribute>
                                    <put-attribute name="body"
                                            value="/WEB-INF/views/home.jsp" />
                                </definition>
                                <definition name="/bbs/create" extends="main"> 
                                        <put-attribute name="title" value="bbs 목록" ></put-attribute> 
                                        <put-attribute name="body" value="/WEB-INF/views/bbs/createForm.jsp" /> 
                                </definition>
                                </tiles-definitions>

                                -template 가져오기 

[2021.03.08 월요일]
1. BBSDAO.java - SELECT 문에 FILENAME 추가 
2. list 추가 - BbsController, bbs.xml 추가 -> list.jsp
3. read 추가 - BbsController, bbs.xml 추가 -> read.jsp
4. update 추가 - BbsController, bbs.xml 추가 -> updateForm.jsp
   error.jsp, passwdError.jsp 추가 
5. errorModal.
6. 24번 페이지 
    6.1 칼럼 추가 refnum -> 부모글 번호가 오면 삭제 못하게 하려고한다. 
                 ALTER TABLE BBS
                 ADD(refnum number(7) default 0);

                 DELETE FROM BBS WHERE INDENT > 0; //들여쓰기 된거 
                 replyForm -> multipart/form-data
7.delete bbs.xml 추가 
            
            
쉽지가 않다 어떻게 해야 효율이 좋아질까 복습복습 복습 안해서 그런거여 
delete 하는 부분 뭔가 이상함 -> 쿼리 띄어쓰기 

[2021.03.09 월요일]
1. 글 목록 - 업로드된 파일도 삭제되야 함 
          - C:\JCS\8.Spring\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\20210305_spring_test\storage
          - 댓글이 달리면 댓글의 REFNUM에 BBSNO가 INSERT 된다
2. EL Express Language
    - jsp 객체의 출력을 단순화 -> ${key}
    - pageContext        : PageContext 객체 
    - pageScope          : page 영역에 포함된 객체 
    - requestScope       : request 영역에 포함된 객체 
    - sessionScope       : session 영역에 포함된 객체 
    - applicationScope   : application 영역에 포함된 객체 
    - param              : HTTP의 파라미터들 
    - paramValues        : 하나의 파라미터의 값들 
    - header             : 헤더 정보들 
    - headerValues       : 하나의 헤더의 값들 
    - cookie             : 쿠키들 
    - initParam          : 컨텐츠의 초기화 파라미터들 

    (1) ${pageContext.request.requestURI}  : request URI 
    (2) ${sessionScope.profile}            : session 영역에서 profile이라는 이름으로 
                                              저장된 객체 
    (3) ${param.productId}                 : productId라는 이름의 파라미터 값 
    (4) ${paramValues.productId}           : productId라는 이름의 파라미터 값들 
    (5) ${pageContext.request.contextPath} : Context Path 추출  

3.예제
      ★el
        request의 name 속성(표현식 방식) : <%=request.getAttribute("name") %> <br> 
		스크립틀릿 :
		<% String name = (String)request.getAttribute("name");
		   out.print(name);
		%>
		<br/>
		request의 name 속성(EL-requestScope.name) : ${requestScope.name }<br /><br />
		request의 name 속성(EL-name) : ${name }<br/><br/>


        run as run on server 로 하면 index.jsp 를 찾는다 

      ★
        <%
			String code = request.getParameter("code");
			out.print(code);// 기본값 null
		%>
		<br>
		code 파라미터(스크립틀릿 방식) : <%out.println(code); //기본값 null %> <br>
		code 파라미터(EL 방식) : ${param.code}<br/>
		code 파라미터(EL 방식) : ${param.save }<br/>

      ★ 객체의 접근 

      <!-- 객체의 접근 -->
        <!--헤더-->
        <%
            ELDTO dto = new ELDTO("정찬식", "삼성");
            request.setAttribute("dto", dto);

        %>

        <h3>
		EL 사용 안한 경우 <br><br>
		<%
			Object obj = request.getAttribute("dto");
		
			ELDTO eldto = (ELDTO)obj;
			
			out.println("이름 : " + eldto.getMovie() + "<br/><br/>");
			out.println("기업명 : " + eldto.getName() + "<br/><br/>");
		%>
		<br><br>
		
		EL 사용한 경우 - name일 경우 getName() 호출하는 거래여 
		이름 : ${requestScope.dto.movie }<br><br>
		기업명 : ${requestScope.dto.name }<br><br>
		
		Type2: 주  연: ${dto.movie} - ${dto.name}<br><br> 
		Type3: 주  연(X): ${requestScope.movie}<br><br> 
		Type4: 주  연(X): ${movie}<br><br> 
	
	
	    </h3>

        page, session에는 scope 붙여서 써야하는데 request는 안해도 됨 

        ★  사용자 정의 
            1.static 메소드 만들어야 함 
            2.web 에서 el 에서 어떤 이름으로 쓸 건지 xml에 정의되어야 함 
            3.TLD(Tag Library Descriptor) xml파일 만들어야 함 
            4.web-inf/tlds/el-functions.tld

            java-editor-content assist-advanced-java proposals : 추천 함수 명 안나올 때
[2021.03.09 화요일]
4. JSTL 의 사용 - 27번 : 사람들이 추가로 해논 걸 sun에서 자체적으로 배표 
    4.1 라이브러리  
             기능                        접두어      관련 URL 

Core          변수지원,흐름제어,URL      c      http://java.sun.com/jsp/jstl/core ★ 
함수           콜렉션 처리, String          fn     http://java.sun.com/jsp/jstl/functions ★ 

    4.2 JSTL 다운 받는 곳 -> web-inf/lib
        else, else if 는 choose 
    4.3 set, remove test

        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <c:set var="num1" value="${20}"/>
        <c:set var="num2">10.5</c:set>
        <c:set var="today" value="<%=new java.util.Date() %>"></c:set>

        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>Insert title here</title>
        </head>
        <body>
            변수 num1 = ${num1 } <br>
            변수 num2 = ${num2 } <br>
            num1 + num2 = ${num1 + num2 } <br>
            오늘은 ${today } 입니다.
            
            <c:remove var="num1" scope="page"/>
            <p>
            삭제한 후의 num1 = ${num1}<br>
            삭제한 후의 num1 = ${num1 + num2}<br>
            
        </body>

        </html>

    4.4 map 객체 사용
        4.4.1 
            <c:set target="${map }" property="name" value="정찬식"></c:set>
	        <c:set target="${map }" property="goal" value="한컴"/>
        4.4.2 choose, when, otherwise
             foreach
             redirect 
             if
             set
             url
             c:catch -> 예외처리 not empty ex 
             fmt:formatNumber -> 통화 퍼센트 

할 수 있어요 
    4.5 el jstl 적용
        4.5.1 
               <%
                BbsDTO dto = (BbsDTO) request.getAttribute("dto");

                //페이징, 검색 유지
                String nowPage = request.getParameter("nowPage");
                String col = request.getParameter("col");
                String word = request.getParameter("word");
                %>

                => getPrameter -> param.변수명 
                   getAttribute -> dto.변수명

                ※ storage 폴더 사라지는 현상 발생...
                C:\JCS\8.Spring\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\20210309_spring_test_eljstl\storage
[2021.03.10 수요일]
1.Ajax 
    1.0 pom에 dependency 추가 
        <dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.7.2</version>
		</dependency>
    
    1.1 jQuery ajax 통신을 위한 라이브러리 설치
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    1.2 javascript 코드 추가 : # -> id
            동기 : 요청 후 응답대기   이미 요청한 페이지에서 다른 데이터만 가지고 오는 
            비동기(Ajax) : 요청도 하고 다른 일도 함. 해당 페이지에서 바로바로 바뀌는거 
            oldfile name은 어디서 가지고 오는거지? 

    1.3 @RequestBody : view 페이지에서 전달하는 json 형태의 데이터를 문자열 형태로 받는다
    1.4 @ResponseBody : Controller 에서 데이터 처리 후 자바 객체의 값을 JSON 형태로 변환

        <script type="text/javascript">
            // 페이지가 열리면 실행 되는 함수
            $(function(){
                //# -> id 
                //on -> 이벤트 설정
                $('#btn1').on('click', function(){
                    
                    //다음과 값을 넣어주겠다 {} 이면 JSON 형식 -> input 에 있는 값 넣어주기 위해 
                    var form = {
                            bbsno : $('#bbsno').val(),
                            passwd : $('#passwd').val(), //.val()
                            filename : $('#oldfile').val()
                    }//이렇게 하면 데이터 읽어옴 
                    
                    //확인용 alert // oldfile은 어디서 오는건가 
                    /* alert(form.bbsno); form 때문에 안되네 ㅋㅋㅋ 기본 방식이 post 방식이라 
                    alert(form.passwd);
                    alert(form.filename);
                    */
                    //비동기 요청 
                    $.ajax({
                        url : "./delete_Ajax",
                        type : "POST",
                        data : JSON.stringify(form), //form을 스트링으로 바꿈 
                        contentType : "application/json; charset=utf-8;",
                        dataType : "json",
                        success : function(data){ //json 객체 data 가 결과다?
                            
                                //alert(success); 이거 왜 안뜸 
                            $('#red').text('');
                            $('#red').text(data.str);
                        },
                        error : function(request, status, error){
                            alert("code = " + request.status + ",message = " + request.responseText + ",error = " + error);
                            //400에러 나는데  
                        }
                    });
                }); //버튼 이벤트 
            });	//페이지 로딩 되었을때 
        </script>

        <p id="red" class="col-sm-offset-2 col-sm-6" >삭제하면 복구할 수 없습니다</p>

2. myBatis
    2.1 pool : 가용할 수 있는 connection 을 만들어 놓고 사용자가 들어오면 할당
               - 바닷가에 튜브를 미리 만들어놓는다 
               - myBatis 는 그때 그때 pool을 미리 만들어놓음 
               - 유지보수성 높다 
               - JPA 
               - 비즈니스 로직 : service + dao 
               - SqlSession -> SqlSessionFactory -> SqlMapConfig.xml -> mapper.xml(쿼리문 정의) annotaion으로도 할 수 있음 
               - annotation으로는 안함? 웃기웃기웃기웃기웃웃기

    2.2 구조 : id, namespace, resultType(select 문에서 필수)
              insert 태그 : selectKey 로 primary key        
              mySql -> auto_increments
              resultMap : 상위 태그에 resultMap 이 선언되어 있어야 한다.
                        - 테이블 칼럼명과 매핑할 자바 객체의 필드명이 다를때도 사용  

                    <resultMap id="selectResult" type="board">
                        <result property="num" column = 'seq'>
                        <result property="title" column = 'subject'>
                        <result property="content" column = 'content'>
                        <result property="redate" column = 'redate'>
                    </resultMap>
                    <select id=”selectBoard” parameterType=”int” resultMap=”selectResult”>
                        SELECT * FROM board WHERE num = #{num}
                    </select>


              resultType : 결과물의 그냥 타입이고 

               <![CDATA[
              WHERE num <= #{num}
                ]]>
                --> 꺽쇠로 비교를 하는데 이를 바로잡아주기 위한 것 CDATA


    2.3 동적 sql - 저장 프로시저에서 분기하는 거랑 비교했을 때 뭐가 더 좋을까 

    (1) if
            
            <select id=”findActive”
                    parameterType=”Blog” resultType=”Blog”>
                    SELECT * FROM BLOG
                    WHERE state = ‘ACTIVE’
                    <if test=”title != null”>
                    AND title like #{title}
                    </if>
            </select>
            
            <select id=”findActiveBlogLike”
                =”Blog” resultType=”Blog”>
                    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
                    <if test=”title != null”>
                    AND title like #{title}
                    </if>
                    <if test=”author != null and author.name != null”>
                    AND author_name like #{author.name}
                    </if>
            </select>
            

            (2) choose, when, otherwise
            
            <select id=”findActiveBlogLike”
                    parameterType=”Blog” resultType=”Blog”>
                    SELECT * FROM BLOG WHERE state = ‘ACTIVE’
                    <choose>
                    <when test=”title != null”>
                            AND title like #{title}
                    </when>
                    <when test=”author != null and author.name != null”>
                            AND author_name like #{author.name}
                    </when>
                    <otherwise>
                            AND featured = 1
                    </otherwise>
                    </choose>
            </select>
            
            
            (3) where -> 주의 
            
            - where 요소는  단순히 “WHERE” 만을 추가한다.  “AND” 나 “OR” 로 시작한다면,
            그 “AND” 나 “OR”를 지워버린다.
            - 아래 예는 오류가 날 수 있다.
            
            <select id=”findActiveBlogLike”
                    parameterType=”Blog” resultType=”Blog”>
                    SELECT * FROM BLOG
                    WHERE
                    <if test=”state != null”>
                            state = #{state}
                    </if>
                    <if test=”title != null”>
                            AND title like #{title}
                    </if>
                    <if test=”author != null and author.name != null”>
                            AND author_name like #{author.name}
                    </if>
            </select>
            
            - 위의 구문이 어떤 조건에도 해당되지 않는다면 다음과 같은 SQL이
            만들어진다.

            (오류)
            SELECT * FROM BLOG
            WHERE
            
            (오류)
            SELECT * FROM BLOG
            WHERE
            AND title like ‘someTitle’
            
            - 수정된 코드는 아래와 같다.
            <select id=”findActiveBlogLike”
                    parameterType=”Blog” resultType=”Blog”>
                    SELECT * FROM BLOG
                    <where>
                        <if test=”state != null”>
                                state = #{state}
                        </if>
                        <if test=”title != null”>
                                AND title like #{title}
                        </if>
                        <if test=”author != null and author.name != null”>
                                AND author_name like #{author.name}
                        </if>
                    </where>
            </select>
            
            (4) set

            - 동적인 update 구문의 set 요소는 update 하고자 하는 칼럼을 동적으로
            포함시키기 위해 사용한다.
            - set 요소는 동적으로 SET 키워드를 붙히고 필요없는 콤마를 제거한다.
            
            <update id="updateAuthorIfNecessary"
                    parameterType="domain.blog.Author">
                    update Author
                    <set>
                        <if test="username != null">username=#{username},</if>
                        <if test="password != null">password=#{password},</if>
                        <if test="email != null">email=#{email},</if>
                        <if test="bio != null">bio=#{bio}</if>
                    </set>
                    where id=#{id}
            </update>
            
            (5) foreach
            - collection에 대해 반복처리한다. 종종 IN 조건을 사용한다.

            <select id="selectPostIn" resultType="domain.blog.Post">
                    SELECT *
                    FROM POST 
                    WHERE ID in 
                    <foreach item="item" index="index" collection="list"
                        open="(" separator="," close=")">
                                #{item}
                    </foreach>
            </select>

            item -> 전달받은 리스트 value 
            index -> 하나하나 꺼내올 때 



            추가
            <insert id="insertAuthor" parameterType="domain.blog.Author">
                <selectKey keyProperty="id" resultType="int" >
                        select board_seq.nextval as idfrom dual
                </selectKey>
                insert into Author (id,username,password,email,bio)
                values (#{id},#{username},#{password},#{email},#{bio})
            </insert> 
            ->selectKey primaryKey 값이 리턴 
            

            이렇게 바꿔도 됨 

            <insert id="insertAuthor" parameterType="domain.blog.Author">
                insert into Author (id,username,password,email,bio)
                values (board_seq.nextval,#{username},#{password},#{email},#{bio})
            </insert>
            -> 갱신된 수 리턴
    3. 33번 글 
        3.1 20210310_spring_test_myBatis
        3.2 pom.xml 에 myBatis 추가 
        3.3 root-context.xml1 -  p namespace 추가 
        3.4 Mapper.xml 추가 
            \src\main\resources\mybatis
        3.5 BbsMapper interface 추가 
            main\java\spring\model\bbs  
        3.6 Controller 에 injection           
            	@Autowired
	            private BbsMapper mapper;
        3.7 list 추가 
            - query 문만 Toggle Block Selection Mode 로 쿼리 가죠옴 
            - Mapper.xml 에 쿼리 넣음 
            - Controller 에 list 함수 
                //List<BbsDTO> list = dao.list(map); 이거 주석 후 이게 끝 
                List<BbsDTO> list = mapper.list(map); 이거 적용 
        3.10 create 추가
            - Mapping.xml 에 쿼리 추가 
            - BbsMapper.java 에 int create(BbsDTO bbsDTO) 추가 
            - BbsDAO.java 에서 create 함수 주석 처리 
            - BbsController.java 의 create 함수에서 boolean flag = dao.create(dto); 처리 
        3.11 read 추가 
             - Mapping.xml 에 쿼리 추가 
             - 

        3.12 update 추가 
              - Mapping.xml 에 쿼리 추가 
              - mapper interface 에 추가

[2021.03.11 목요일] 감정이 태도가 되어버리면 안된다

0. 20210310_review 이거 쓰기 
1. 답변달기 mybatis 로 교체작업 
    1.1 Mapping.xml 에 쿼리 넣는 작업 진행 
    1.2 BbsMapper interface 작성 
    1.3 DAO 삭제 
    1.4 controller 수정 
2. 다운로드 기능
    1.1 list.jsp - 링크걸기 

        <td>
            <c:choose >	
                <c:when test="${empty dto.filename }">파일 없음 다운로드 불가</c:when>
                <c:otherwise>
                    <a href="javascript:fileDown('${dto.filename }')">
                        ${dto.filename}
                    </a>
                </c:otherwise>	
            </c:choose>
        </td>

    1.2 controller 에 fileDown 추가 
              
        ServletContext ctx = request.getSession().getServletContext();
		//절대경로 두번째 방법 
		String dir = ctx.getRealPath(request.getParameter("dir"));
		String filename = request.getParameter("filename");
		
		//response -> 바이트 형으로 내보냄 
		byte[] files = FileUtils.readFileToByteArray(new File(dir, filename));
		//response.getOutputStream().write(files);
		
		
		//헤더 정의 
		response.setHeader("Content-disposition", "attachment; fileName=\""+ URLEncoder.encode(filename,"UTF-8")+"\";");
		//파일이 첨부되어 있으니 다운해랴 
		response.setHeader("Content-Transfer-Encoding", "binary");
	    /**
	     * Content-Disposition가 attachment와 함게 설정되었다면 
	     * 'Save As'로 파일을 제안하는지 여부에 따라 브라우저가 실행한다.
	     */
		response.setContentType("application/octet-stream");
	    response.setContentLength(files.length);
	    
	    response.getOutputStream().write(files);
	    response.getOutputStream().flush();
	    response.getOutputStream().close();

3. 웹에디터 CKEditor 와 CKFinder 
    3.1  : \src\main\webapp 에 붙여넣기 
    3.2 ckfinder_java_2.6.3\ckfinder/CKFinderJava-2.6.3.war 압축풀기
        ckfinder 폴더 \src\main\webapp 에 붙여넣기 
    3.3 ckfinder_java_2.6.3\ckfinder\CKFinderJava-2.6.3\WEB-INF\lib\
        ckFinder-2.6.3.jar & ckFinderPlugin-fileEditor.jar & IMageResize.jar & thumbnailator.jar 이거 복사
        spring/lib/에 넣기 
    3.4 ckfinder\CKFinderJava-2.6.3\WEB-INF\config.xml 파일 이름 변경 -> ckfinder-config.xml 
        spring/ 에 넣기 
    3.5  web.xml 에 넣기 

         <!-- *********************************************************************** -->
        <!-- CKFINDER start -->
        <!-- *********************************************************************** -->
        <servlet>
            <servlet-name>ConnectorServlet</servlet-name>
            <servlet-class>com.ckfinder.connector.ConnectorServlet</servlet-class>
            <init-param>
            <param-name>XMLConfig</param-name>
            <param-value>/WEB-INF/spring/ckfinder-config.xml</param-value>
            </init-param>
            <init-param>
            <param-name>debug</param-name>
            <param-value>false</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>
        
        <servlet-mapping>
            <servlet-name>ConnectorServlet</servlet-name>
            <url-pattern>
                        /ckfinder/core/connector/java/connector.java
                </url-pattern>
        </servlet-mapping>

    3.6 ckfinder-congif.xml 수정 
            -> enable true 로 바꾸기 
            -> baseDir 넣기
               C:\JCS\8.Spring\SpringWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\20210310_spring_test_myBatis\ckstorage
                역슬래시 
            -> baseURL 넣기
                /webtest/ckstorage 슬래시

    3.7 webapp/ckeditor/config.js 수정 

                    /*
            Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
            For licensing, see LICENSE.html or http://ckeditor.com/license
            */
            var myToolbar =  
                        [     
                            { name: 'document', items : [ 'Source','-','DocProps','Preview','Print','-','Templates' ]  },
                            { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
                            { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },
                            { name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','Iframe' ] },
                                    '/',
                            { name: 'styles', items : [ 'Font','FontSize' ] },
                            { name: 'colors', items : [ 'TextColor','BGColor' ] },
                            { name: 'basicstyles', items : [ 'Bold','Italic','Strike','-','RemoveFormat' ] },
                            { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock' ] },
                            { name: 'links', items : [ 'Link','Unlink' ] },
                            { name: 'tools', items : [ 'Maximize','-','About' ] }
                        ];
            
            CKEDITOR.editorConfig = function( config )
            {
            // Define changes to default configuration here. For example:
            // config.language = 'fr';
            // config.uiColor = '#AADC6E';
            config.height = 500;
            config.toolbar = myToolbar;
            //config.uiColor = '#9AB8F3';
            config.uiColor = '#D3D3D3';
            config.enterMode = CKEDITOR.ENTER_BR; //엔터키 태그 1:<p>, 2:<br>, 3:<div>
            config.font_defaultLabel = '굴림체'; //기본글씨
            config.font_names = '굴림체/Gulim;돋움체/Dotum;맑은 고딕/맑은 고딕;';
            
            config.filebrowserBrowseUrl = '../ckfinder/ckfinder.html';
                config.filebrowserImageBrowseUrl = '../ckfinder/ckfinder.html?type=Images';
                config.filebrowserFlashBrowseUrl = '../ckfinder/ckfinder.html?type=Flash';
                config.filebrowserUploadUrl = '../ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files';
                config.filebrowserImageUploadUrl = '../ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images';
                config.filebrowserFlashUploadUrl = '../ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash'; 
            };
    
    javascript 로 인식 못함...
    3.8 spring/appServlet/servlet-context.xml
    3.9 config.js 에서 에디터 화면 정의할 수 있음 

            <script type="text/javascript" src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
            <script type="text/JavaScript">
            window.onload=function(){
                CKEDITOR.replace('content');  // <TEXTAREA>태그 id 값
            };
             </script>

             추가

    3.10 content 예외처리 
        - createForm.jsp -> form -> onsubmit="return checkIn(this) 추가 : 폼이라는 객체를 가지고 가겠다
        - updateForm.jsp, replyForm.jsp 도 적용 
        - return 은 onsubmit 에 만 있음. true 일 때 진행, false 면 진행 안함 

            function checkIn(f){
            
            if(f.wname.value==""){
                alert("글쓴이를 입력하세요");
                f.wname.focus();
                
                return false;
            }
            
            if(f.title.value==""){
                alert("제목을 입력하세요");
                f.wname.focus();
                
                return false;
            }
            
            if(CKEDITOR.instances['content'].getData()==""){
                window.alert("내용을 입력하세요");
                f.wname.focus();
                CKEDITOR.instances['content'].focus();
                return false;
            }
            
            if(f.wname.value==""){
                alert("패스워드를 입력하세요");
                f.wname.focus();
                
                return false;
            }
        }

        CKEDITOR는 어디에 정의되어있지?
        server spring validation check -> boot 
        password check -> javascript 

4. RestController를 이용한 댓글 목록(ajax)
    4.1 Rest Controller
        - REST : Representational State Transfer 
        - HTTP URI 로 리소스를 정의 [webtest/bbs/update?bbsno=25&oldfile=(3)sidebar.PNG]
        - HTTP method로 리소스 정의 
        - POST  : create    
          GET   : read
          PUT   : update
          DELETE: delete
        - 기본 컨트롤러는 view 이름 ~.jsp return 
          이건 순수란 문자열 리턴 
    4.2 pom.xml 수정 - plugin/source 1.8/target 1.8
                        <dependency>
                        <groupId>com.fasterxml.jackson.core</groupId>
                        <artifactId>jackson-databind</artifactId>
                        <version>2.9.6</version>
                        </dependency>
                        
                        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-xml -->
                        <dependency>
                        <groupId>com.fasterxml.jackson.dataformat</groupId>
                        <artifactId>jackson-dataformat-xml</artifactId>
                        <version>2.9.6</version>
                        </dependency>
    4.3 >>> WEB-INF/reply.sql

            create table reply(
            rnum number not null,
            content varchar(500) not null,
            regdate date not null,
            id varchar(10) not null,
            bbsno number(7) not null,
            primary key(rnum),
            foreign key(bbsno)  references bbs(bbsno) -- BBS테이블에 값이 없으면 INSERT 안됨
            );

            insert into reply(rnum, content, regdate, id, bbsno)
            values((select nvl(max(rnum),0)+1 from reply),
            '의견입니다.',sysdate,'user1',1
            ) ;
            
            
            --list(목록)
            select rnum, content, to_char(regdate,'yyyy-mm-dd') regdate, id, bbsno, r
            FROM(
            select rnum, content, regdate, id, bbsno, rownum r
            FROM(
            select rnum, content, regdate, id, bbsno
            from REPLY
            where bbsno = 1
            order by rnum DESC
                )
            )WHERE r >= 1 and r <= 5;
            
            
            --total(목록)
            select count(*) from reply
            where bbsno = 1;

    4.4 DTO 작성 

            private int rnum;
            private String content;
            private String regdate;
            private String id;
            private int bbsno;

    4.5 Mapper.java 작성 

            >>> ReplyMapper.java

            package spring.model.reply;
            
            import java.util.List;
            import java.util.Map;
            
            public interface ReplyInter {
                int create(ReplyDTO replyDTO);
                List<ReplyDTO> list(Map map);
                ReplyDTO read(int rnum);
                int update(ReplyDTO replyDTO); 
                int delete(int rnum);
                int total(int bbsno);
            }
    4.6 REPLY.XML 작성 
                <?xml version="1.0" encoding="UTF-8" ?> 
            
            <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
            
            <mapper namespace="spring.model.reply.ReplyMapper">
                <insert id="create" parameterType="ReplyDTO">
                    
                    insert into reply(rnum
                                    ,content
                                    ,regdate
                                    ,id
                                    ,bbsno)
                    values((select nvl(max(#{rnum}),0)+1 from reply)
                        ,#{content}
                        ,sysdate
                        ,#{id}
                        ,#{bbsno}) 
                </insert>

                <!-- sno eno 떄문에 Map -->	
                <select id="list" parameterType="Map" resultType="ReplyDTO">
                
                    SELECT rnum
                        ,content
                        ,to_char(regdate,'yyyy-mm-dd') regdate
                        ,id
                        ,bbsno
                        ,r
                    FROM(
                            select rnum
                                ,content
                                ,regdate
                                ,id
                                ,bbsno
                                ,rownum r
                            FROM(
                                    select rnum
                                        ,content
                                        ,regdate
                                        ,id
                                        ,bbsno
                                    from REPLY
                                    where bbsno = #{bbsno}
                                    order by rnum DESC
                            )
                    <![CDATA[
                    )WHERE r >= #{sno} and r <= #{eno}
                    ]]>
                </select>
                
                <select id="total" resultType="int" parameterType="int">
                    select count(*) 
                    from reply
                    where bbsno=#{bbsno}
                </select>
                
                <!-- RESULT TYPE 넣어줘야 함 -->
                <select id="read" parameterType="int" resultType="ReplyDTO">
                    SELECT * 
                    FROM REPLY
                    WHERE RNUM = #{BBSNO}
                </select>
                <!-- 하나만 수정 -->
                <update id="update" parameterType="ReplyDTO">
                    UPDATE REPLY
                    SET CONTENT = #{CONTENT}
                    WHERE RNUM = #{RNUM}
                </update>
                
                <!-- 하나만 삭제 -->
                <delete id="delete" parameterType="int">
                    DELETE FROM REPLY
                    WHERE RNUM = #{RNUM}
                </delete>
            </mapper>


[2021.03.12 금요일]
1.JUnit 40번 글 
    - myBatis가 접근할 수 있는 mapper interface 만들었음.
    - 인터페이스는 룩업이 안되어서 자바 메인에서 테스트가 불가했다. 애노테이션? 
    - Junit 단위 테스트의 제공 기능
        . assertions : 테스트결과 값과 예상값이 같은지 비교하는 단정문
        . test fixture : 일관된 테스트 실행환경
        . test runner : 테스트 작업수행을 위한 테스����������� 실행 클래스
        . 단위 테스트 케이스와 단위 테스트 메소드 
    - 이전버전에서 사용했던 test라는 글자로 method 이름으로 시작해야 하는 제약 해소 test 메소드는 @Test 선언
    - 애노테이션 기반 픽스처
        @BeforeClass, @AfterClass, @Before, @After
    - 예외 테스트
        @Test(expected=NumberFormatException.class)
    - 시간 제한 테스트
        @Test(timeout=1000)
    - 테스트 무시
        @Ignore("this method isn't working yet")
    - 배열 지원
        assertArrayEquals([msg],예상값,실제값);
    - @RunWith(클래스이름.class)
        JUnit Test 클래스를 실행하기 위한 러너(Runner)를 명시적으로 지정한다.
        @RunWith는 junit.runner.Runner를 구현한 외부 클래스를 인자로 갖는다.
    - @SuiteClasses(Class[])
        보통 여러 개의 테스트 클래스를 수행하기 위해 쓰인다. @RunWith를 이용해 
        Suite.class를 러너로 사용한다.
    - 파라메터를 이용한 테스트
        @RunWith(Parameterized.class)
        @Parameters
        public static Collection data(){
        :
        }
    
    1.1 @Test 
        - JUnit 4는 @Test 애노테이션만 선언하면 테스트 메소드로 인식한다.
        - 이전버전 처럼 test로 메소드의 이름을 시작하지 않아도 된다.
        - JUnit 3에서 testDeposit 대신 testDepoist으로 타이핑 오타를 내면 실행이 안된다.
        그러나 JUnit4 @Test 선언하면 실행이 잘된다.
        
        BeforeClass -> Before -> Test1 -> After
                        Before -> Test2 -> After -> AfterClass 
        
        Test1 과 Test2만 사용자가 씀 

    1.2 예제 - main 함수 필요없음 

                public class Terminal {

                private String id;
                private String pw;
                private String msg;

                public void netConnect() {
                    
                    System.out.println("터미널 접속");
                }

                public void netDisConnect() {
                    
                    System.out.println("터미널 접속 해제");
                }

                public void logon(String id, String pw) {
                    
                    this.id = id;
                    this.pw = pw;
                    System.out.println("로그인");
                }

                public void logoff() {
                    
                    System.out.println("로그오프");
                }

                public boolean isLogon() {
                    
                    if (id != null && pw != null) {
                        return true;
                    } else {
                        return false;
                    }
                }

                public void sendMessage(String msg) {
                    this.msg = msg;

                }

                public Object getReturnMsg() {
                    return msg;
                }

            }
    1.3 테스트 클래스 생성 
        - 테스트 할 클래스 오른쪽 클릭 -> Junit Test Case 선택 -> method stubs 전체 선택 
            netConnect(), getReturnMsg() 이거만 선택 


        public class TerminalTest {
        //왜 이렇게 나누어서 하는건가요 -> 단위 테스트라서 
        private static Terminal term;

        //1
        @BeforeClass
        public static void setUpBeforeClass() throws Exception {
            term = new Terminal();
            term.netConnect();
        }

        @AfterClass
        public static void tearDownAfterClass() throws Exception {
            //다 끝났을 때 
            term.netDisConnect();
        }

        //2
        @Before
        public void setUp() throws Exception {
            term.logon("user1", "1234");
        }
        
        //4
        @After
        public void tearDown() throws Exception {
            //로그오프 
            
            term.logoff();
        }

        //3
        @Test
        public void terminalConnect() {
            //연결이 되었나 확인
            
            assertTrue(term.isLogon());
            System.out.println("==logon Test");
        }

        @Test
        public void getReturnMsg() {
            //
            term.sendMessage("hello");
            assertEquals("hello", term.getReturnMsg());
            System.out.println("== message test");
        }

        }
    
    Run as -> JUnit Test 선택 

2.ReplyMapper JUnit 테스트 적용 
    2.1 POM 수정
            <!-- https://mvnrepository.com/artifact/org.springframework/spring-test -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-test</artifactId>
                <version>${org.springframework-version}</version>
                <scope>test</scope>
            </dependency>

    2.2 oracle driver Project에 추가
        - STS --> Java Built Path -> Add External Jars...]에서 ojdbc.jar 추가

    2.3 Test class
        - @RunWith: JUnit에 내장된 Runner대신 테스트시에 Spring에서 확장 기능을 제공하는 SpringJUnit4ClassRunner라는 Runner 클래스를 설정
        - @ContextConfiguration:  테스트시에 필요한 애플리케이션 컨텍스트 파일 위치
        - @WebAppConfiguration : Servlet의 ServletContext를 이용(servlet-context.xml 인식)

        - @Autowired : 의존성 관리

        - @Before: @Test가 붙은 메소드 실행전에 실행한다.
        - @Test: JUnit 테스트가 작동하는 메소드로 테스트할 주요 기능을 명시한다.
        - @After: @Test가 붙은 메소드전에 실행한다.
             @Before --> @Test --> @After 순으로 실행하며 @Test가 5개이면 이런 과정을 5회 반복한다.

    2.4 spring.model.reply.ReplyMapper 의 testcase 생성 
        - 파일 오른쪽 클릭 -> new -> Junit -> Junit Test case 
    2.5 로깅 slf4j
        - private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    2.6
                        

        @WebAppConfiguration
        @RunWith(SpringJUnit4ClassRunner.class) //이거 없으면 안됨 
        @ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"
                                ,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"}) //절대경로 쓰면 안됨//ApplicationContext 

        public class ReplyMapperTest {

            private static final Logger logger = LoggerFactory.getLogger(ReplyMapperTest.class);//this 못씀
            
            @Autowired
            private ReplyMapper mapper;
            
        //	@BeforeClass
        //	public static void setUpBeforeClass() throws Exception {
        //	}
        //
        //	@AfterClass
        //	public static void tearDownAfterClass() throws Exception {
        //	}
        //
        //	@Before
        //	public void setUp() throws Exception {
        //	}
        //
        //	@After
        //	public void tearDown() throws Exception {
        //	}
            
            @Test
            //@Ignore //실행안되게 함 
            public void testMapper() {
                //연결 안되었음 
                logger.info("mapper : " + mapper.getClass().getName());
            }

            @Test
            @Ignore
            public void testCreate() {
                
                //이것도 autowired 로 해야 하는거 아닌가?
                //이거는 자동으로 할 순없다.? 
                ReplyDTO dto = new ReplyDTO();
                dto.setBbsno(2);
                dto.setContent("34");
                dto.setId("user1");
                //이거 왜 쓴다고?
                assertTrue( mapper.create(dto)>0 );
            }
            
            @Test
            @Ignore
            public void testList() {
                Map map = new HashMap();
                map.put("bbsno", 2);
                map.put("sno", 1);
                map.put("eno", 3);
                
                List<ReplyDTO> list = mapper.list(map);
                
                logger.info("list size : " + list.size());
                
                for(int i=0 ; i<list.size() ;i++) {
                    ReplyDTO dto = list.get(i);
                    System.out.println(dto);
                }
            }
            
            @Test
            //@Ignore
            public void testTotal() {
                int bbsno = 2;
                int cnt = mapper.total(bbsno);
                logger.info("total : " + cnt);
            }
            
            
            @Test
            //@Ignore
            public void testRead() {
                ReplyDTO dto = mapper.read(1);
                logger.info("Read dto :" + dto);
            }
            
            //testTotal testRead 를 둘다 @Ignore 주석했을 떄  Invocation of destroy method 'close' failed on bean with name 'sqlSession 이라는 오류가 나옴
        //	INFO : org.springframework.web.context.support.GenericWebApplicationContext - Closing org.springframework.web.context.support.GenericWebApplicationContext@68c72235: startup date [Fri Mar 12 12:36:00 KST 2021]; root of context hierarchy
        //	WARN : org.springframework.beans.factory.support.DisposableBeanAdapter - 
        //	       Invocation of destroy method 'close' failed on bean with name 'sqlSession': java.lang.UnsupportedOperationException: Manual close is not allowed over a Spring managed SqlSession
        //	INFO : org.apache.tiles.access.TilesAccess - Removing TilesContext for context: org.springframework.web.servlet.view.tiles2.SpringWildcardServletTilesApplicationContext

            
            
            @Test
            @Ignore
            public void testUpdate() {
                ReplyDTO dto = new ReplyDTO();
            }
            

            @Test
            public void testDelete() {
                fail("Not yet implemented");
            }
     2.7 SEVERE: 자식 컨테이너를 시작 중 실패했습니다. -> 서버 창에서 지웠다가 다시하니깐 됨 

[2021.03.15 월요일]
1.댓글 기능 ajax 비동기 
    1.1 controller 에서 collection 보낼 때 : model.addAllAttribute(map);
    1.2 ReplyController 추가 (restController)
    1.3 getList, getPage 함수 
    1.3 Utility 에 rpaging 이라는 함수 추가 
    1.4 현재까지 read 만 한 상태임 insert 는 안했음 
    
    아 이걸 이해해야 하는데 어렵다 

2. 댓글 생성, 조회 37번글 회원일 경우에만 
    2.1 js파일에 함수 추가
    2.2  update 추가 
    2.3 delete 추가 
    2.3.1 mybatis 에서 대소문자 구별함 .... 이거땜에 20분 갔음 

3. 댓글 갯수 추가 
    3.1 mybatis 에 댓글 갯수가지고 오는 쿼리 추가 
    3.2 replyDTO 추가
    3.3 BBScontroller list 함수에 추가 
    3.4 utility 에 함수 추가 rcount 
    3.5 list 이거 수정 . sl 문 입력 

4. 회원가입
   로그인 
   로그아웃 

   4.1 파일 추가 
   sql ->, jsp 파일 -> views 밑에 

   createForm.jsp // fnameMF 수정 

[2021.03.16 화요일]
1. 회원가입 기능 
    1.2. MemberMapper 추가 
    1.3. 타일즈 추가 - root-context.xml 
                    <value>/WEB-INF/spring/member.xml</value> 추가 
    1.4. member.xml 추가
                    <tiles-definitions>
                        <definition name="/member/agree" extends="main">
                        <put-attribute name="title" value="약관동의"></put-attribute>
                        <put-attribute name="body" value="/WEB-INF/views/member/agreement.jsp" />
                    </definition>
                    </tiles-definitions>
    
    1.5. views/member/파일 추가 
    1.6. top.jsp 수정 
    1.7. agreement.jsp 에서 회원가입 버튼 누르면 createForm.jsp 로 옴 
    1.8. createForm.jsp 에서 아이디 중복체크를 위한 idcheck함수를 Controller 에 추가 
    1.9. MemberMapper.xml select 문 추가 -> ID는 MemberMapper 의 함수명과 동일해야 함 
    1.10. 스타일 변경 - controller // map.put("color", "red");
                        javascript //  $.get(url, param, function(data, textStatus) {
                                            //var result = eval("("+data+")");
                                            // var result = JSON.parse(data);
                                            $("#idcheck").text(data.str);
                                            //스타일 색상 변경 
                                            $('#idcheck').css('color', data.color);
                                        })
    1.11 id getter 가 없다라는 오류가 뜨면 mybatis 쿼리를 살펴봐라 
    1.12 사진 업로드(create form ) - controller 추가 / member.xml sql 추가 
         / 이제 로그인 화면 설정 
         / session 에 값 저장 
    1.13 로그인 화면 가져오기 / controller, tile 설정
    1.14 loginForm 관련 저장 -> 스크립트 태그에서 el로 바꿔줌 
    1.15 로그아웃 기능 -> Invalid 한 ID면 오류 팝업 띄어줌 -> 이걸 그냥 로그인 화면에서 띄어주면 안됨? 
            쿠키 
            세션 

            혼자 다 할 수는 없는것이고 그렇다고 굳이 나서서 할 필요도 없는것이고 이거 참 고민이넼ㅋㅋ
            그냥 혼자 해? 근데 혼자하면 좋은 점수를 받을 수 없는거여 

            read.jsp, list.jsp 파일 가져옴 

    1.16 read.jsp 관련 controller read 추가 
        - mybatis, tiles 적용 
    1.17 read.jsp 이거 el로 바꿈 
[2021.03.17 수요일]
1. 댓글을 쓸려고 했을 때 로그인해야한다는 내용이 나오게 
    1.1 replyprocess.js 수정 - id="addReplyBtn" 내용 수정 
    1.2 bbs/read.jsp 에 추가 // var session_id = '${sessionScope.id}';

2. 개인 정보 수정

3. 관리자 목록

4. 개인 정보 수정 - member.xml 에 update 추가. controller 수정  


500은 서버단 오류 


    var modal = $(".modal");    //class == modal 

    //$(".modal").modal("show"); //달러 표시가 있어야 함 
	modal.modal("show");

    maven 기본 경로 
    users/정찬식/.m2

5.관리자 모드 
    http://localhost:8000/webtest/admin/list - 회원 목록 보임 
    controller list 함수 추가, mybatis 추가 total, list 

    5.1 top.jsp 수정 

            <c:choose>
            <c:when test="${not empty sessionScope.id && sessionScope.grade='A' }">
                <c:set var="str">기본 페이지입니다. 관리자 로그인</c:set>
            </c:when>
            <c:when test="${not empty sessionScope.id && sessionScope.grade!='A' }">
                <c:set var="str">기본 페이지입니다. ${sessionScope.id } 님</c:set>
            </c:when>
            <c:otherwise>
                <c:set var="str">기본 페이지입니다. </c:set>
            </c:otherwise>
            </c:choose>



            <c:if test="${not empty sessionScope.id && sessionScope.grade == 'A' }">
				<li><a href="${pageContext.request.contextPath }/admin/list">회원 목록</a></li>
			</c:if>



            javax.el.PropertyNotWritableException: Illegal Syntax for Set Operation
            이거 머임 ->> el 표현식에 = 이 2개여애 하는데 1개 였음...



           궁금한 것 : login 하는 controller 이거 하나로 합칠 수도 있지 않을까

    5.2 문제점 http://localhost:8000/webtest/admin/list
        이걸 입력하면 그냥 간다 막아줘야 하는데 
        -  필터 인터셉터 이걸로 막을 수 있다 
    
        5.2.1 필터 만들기 
        -> 상속받아서 구현 
        5.2.2 우린 인터셉터 만들거야 
        ->src\main\java\spring\interceptor\webtest 에  AdminInterceptor.java 추가
        5.2.3 servlet-context.xml 이거로 옴 

        namespace 탭 안보일 경우 -> marketplace 에서 sts 검색 

[2021.03.18 목요일]
1. 댓글있는 글 삭제하고 싶다 -> 트랜잭션 -> AOP 통해서 할 수 있다. 
2. AOP aspect oriented programming 관점지향 프로그래밍 
    2.1 공통 모듈을 분리하겠다 -> 횡단관심 core concern
    2.2 유지보수성 높아지고 재사용성 높임
    2.3 myBatis Mapper 를 이걸 이용할 것이다?
    2.4 주요 용어 
        - 조인 포인트 : 사용자가 요청하는 거에 대한 메소드 지칭 -> service 를 추가해줘야 함. 지금까진 controller 에서 바로 mapper 추가했었음
                       controller 와 dbo(mybatis) 사이에 service를 하나 만든다.
                       포인트 컷의 대상이 될 수 있다 
        - 포인트 컷 : 필터링 된(선택되어진 것) 조인 포인트 // 횡단 관심에 해당하는 공통기능을 수행시키기 위함
                     해당하는 함수만 쓰겠다, 포인트라는 걸 그냥 기존 함수라고 생각하면됨?? 
                     <aop:pointcut id="transactionPointcut" expression="execution(* spring.model.bbs.*Service.*(..))"/>
        - 어드바이스 : 공통 기능의 코드 -> 로그 기능 자체 
        - 위빙 : 스프링은 런타임 식. 비즈니스 메소드를 수정하지 않고도 횡단관심에 해당하는 기능 추가 및 변경가능
        - 애스팩트 : 포인트 컷과 어드바이스의 결합 
        - 프록시 : 위빙이 될 때 생성되는 객체 , 중간에 횡단관심이 실행 될 수 있도록 함 
    2.5 Spring의 AOP - 소스코드나 클래스 정보 변경 안함? 
                     - 프록시 통해서 aop 진행 
                     -service 패턴을 추가하여 트랜잭션 ㅈ
    2.6 AOP 
        2.6.1 - bbsService 추가 
              - reply.xml 에 delete 추가 
              - servlet-context.xml -> tx, aop namespace 추가 tx : 트랜잭션 설정을 위한 것
              - pom.xml ->          <!-- 인터페이스없이 transaction사용시 에러예방 -->
                                    <dependency>
                                            <groupId>cglib</groupId>
                                            <artifactId>cglib</artifactId>
                                            <version>3.1</version>
                                            <type>jar</type>
                                            <scope>compile</scope>
                                    </dependency>
                                    <!-- aspectjweaver -->
                                    <dependency>
                                            <groupId>org.aspectj</groupId>
                                            <artifactId>aspectjweaver</artifactId>
                                            <version>${org.aspectj-version}</version>
                                    </dependency>
             - bbscontroller 수정 
                	//aop를 위한 service 
                @Autowired
                private BbsService service;
                public String delete(int bbsno, String passwd, String oldfile, HttpServletRequest request) {
            - bbs.xml 수정 
            - 댓글 있는 거 삭제 시도 트랜잭션?

3.JPA - Help - install -  Latest Eclipse Release - https://download.eclipse.org/releases/latest
    - web.xml.java ee 선택 / jpa 선택 
    3.1 ORM : 객체와 테이블 매핑 -> ORM 객체 관계 매핑
            - 지금까지는 쿼리문으로 접근 
            - SQL 을 자동으로 생성 및 변경 -> Hibernate
    :JPA 는 ORM 을 연동하여 쓴다. 
    :JPA도 JDBC와 마찬가지로 애플리케이션을 구현할 때, JPA API(javax.persistence)
     를 이용하면 개발당시에는 Hibernate를 ORM 프레임워크로 사용하다가 실제
     서비스가 시작될 때는 TopLink로 변경할 수 있다.
     3.2 메이븐 기반 프로젝트 생성 - maven 의존성 관리만 되어있는 프로젝트 스프링은 없음 
        - file -> new -> maven project -> next -> filter 에 org.apache.maven.arche 쓴다
        -> artifact id = maven-archetype-quickstart

          Group Id : com.study.bbs
            artifact Id : JPAProject
            Version : 0.0.1-SNAPSHOT
            Package : com.study.bbs

        properties/project facet


    3.3 영속 클래스 생성 - JPA 생성기능 이용하여 만듦 -> 패키지 오른쪽 클릭 후 JPA Entity 선택
                       - Field 생성 
    3.4 persistence.xml 에 추가 
            <properties>
                <!-- 필수 속성 -->
                <property name="hibernate.dialect" value="org.hibernate.dialect.Oracle12cDialect"/>
                <property name="javax.persistence.jdbc.driver" value="oracle.jdbc.driver.OracleDriver"/>
                <property name="javax.persistence.jdbc.user" value="user1234"/>
                <property name="javax.persistence.jdbc.password" value="1234"/>
                <property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:XE"/>
                
                <!-- 옵션 -->
                <property name="hibernate.show_sql" value="true"/>
                <property name="hibernate.format_sql" value="true"/>
                <property name="hibernate.use_sql_comments" value="false"/>
                <property name="hibernate.id.new_generator_mappings" value="true"/>
                <property name="hibernate.show_sql" value="true"/>
                <property name="hibernate.hbm2ddl.auto" value="update"/>
         </properties>


    package com.study.bbs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BBSServiceClient {

	public static void main(String[] args) {
		// persistence.xml <persistence-unit name="JPAProject">
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAProject");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			// Transaction 시작
			tx.begin();

			BBS bbs = new BBS();
			bbs.setBbsno(1);
			bbs.setWname("홍길동");
			bbs.setTitle("jpa 제목");
			bbs.setContent("jpa 내용");
			bbs.setPasswd("1234");
			bbs.setWdate("2021-02-23");

			// 등록
			em.persist(bbs);

			// 글목록 조회
			String jpql = "select b from BBS b order by b.bbsno desc";
			List<BBS> list = em.createQuery(jpql, BBS.class).getResultList();
			for (BBS bs : list) {
				System.out.println("--->" + bs.toString());
			}

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
// Unable to build entity manager factory
}


4.JPA Entity
  - @Table -> table 이름과 vo 이름이 다를 경우 사용 

  - root-context.xml 추가 

    	<!-- 스프링과 JPA -->
	<bean id="jpaVendorAdapter"
		class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"></bean>

	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource"></property>
		<property name="jpaVendorAdapter" ref="jpaVendorAdapter"></property>
	</bean>

    - BbsService, BbsDAOJPA, BbsVO 추가 

    -쿼리가 자동생성돤다고 했는데 order by 나 like 나 이런 키워드들은 -> JPQL, HQL
