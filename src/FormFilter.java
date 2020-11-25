import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@WebFilter("/FormFilter")
public class FormFilter implements Filter {
    public void init(FilterConfig config) {
        config.getServletContext().log("Filter Started");
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // this filter checks if any user inputs in any form containing any bad characters by looping through
        // all the request parameters and calling 'checkChars', if this returns true, then the user is throw to the
        // error page
        boolean invalid = false;
        Map<String, String[]> params = request.getParameterMap();
        if(params != null){
            Iterator iter = params.keySet().iterator();
            while(iter.hasNext()){
                String key = (String) iter.next();
                String[] values = params.get(key);
                for(int i=0; i < values.length; i++){
                    if(checkChars(values[i])){
                        invalid = true;
                        break;
                    }
                }
                if (invalid) {
                    try {
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                        request.setAttribute("message", "Special characters / Keywords like <, >, drop, insert, " +
                                "script, alert, null, truncate, delete, xp_, ,<>, !, {, } are not allowed as input");
                        dispatcher.forward(request, response);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }chain.doFilter(request,response);
    }



    public static boolean checkChars(String value) {
        // this function check to see if a string contains any of the values in the string array 'badChars'
        boolean invalid = false;
        String[] badChars = { "<", ">", "script", "alert", "truncate", "delete",
                "insert", "drop", "null", "xp_", "<>", "!", "{", "}", "`",
                "input", "into", "where"};

        for (String badChar : badChars) {
            if (value.contains(badChar)) {
                invalid = true;
                break;
            }
        }
        return invalid;
    }

}
