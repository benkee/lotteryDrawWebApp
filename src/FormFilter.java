import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@WebFilter(filterName = "FormFilter")
public class FormFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        config.getServletContext().log("Filter Started");
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filtering");
        boolean invalid = false;
        Map params = request.getParameterMap();

        if(params != null){
            for (Object o : params.keySet()) {
                String key = (String) o;
                String[] values = (String[]) params.get(key);

                for (String value : values) {
                    if (checkChars(value)) {
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
                    chain.doFilter(request, response);
                }
            }
            }
        chain.doFilter(request,response);
        }



    public static boolean checkChars(String value) {
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
