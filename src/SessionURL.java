import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


// This class will handle the sessions url during the searches
public class SessionURL {
    public static void rememberSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String temp = request.getQueryString().toString();
        System.out.println(".html?" + temp);
        session.setAttribute("sessionURL", request.getRequestURL().toString() + ".html?" + request.getQueryString().toString());
    }

    public static void printPreviousSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        System.out.println("checking: sessionURL="+ sessionURL);
    }

    public static String returnSavedSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        if (sessionURL == null){
            sessionURL = "index.html";
        }
        return sessionURL;
    }

    public static void printCurrentSession(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        String sessionURL = (String) session.getAttribute("sessionURL");
        System.out.println("current sessionURL= "+ sessionURL);
    }
}
