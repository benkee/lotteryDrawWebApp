import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("UL doPost");
        HttpSession session = request.getSession();
        int attempts = trackSession(session);
        if (attempts < 3) {
            session.removeAttribute("firstname");
            session.removeAttribute("lastname");
            session.removeAttribute("username");
            session.removeAttribute("email");
            session.removeAttribute("hashedPassword");
            //access current http session
            String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            String USER = "user";
            String PASS = "password";

            // URLs to connect to database depending on your development approach
            // (NOTE: please change to option 1 when submitting)

            // 1. use this when running everything in Docker using docker-compose
            //String DB_URL = "jdbc:mysql://db:3306/lottery";

            // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
            String DB_URL = "jdbc:mysql://localhost:33333/lottery";

            // 3. use this when running tomcat and mysql database servers on your machine
            //String DB_URL = "jdbc:mysql://localhost:3306/lottery";

            try {
                // create database connection and statement
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();

                // query database and get results
                ResultSet rs = stmt.executeQuery("SELECT * FROM userAccounts");

                // add HTML table data using data from database
                ArrayList<String> Usernames = new ArrayList<String>();
                ArrayList<String> Passwords = new ArrayList<String>();
                ArrayList<String> Salts = new ArrayList<String>();
                ArrayList<String> Firstnames = new ArrayList<String>();
                ArrayList<String> Lastnames = new ArrayList<String>();
                ArrayList<String> Emails = new ArrayList<String>();
                ArrayList<String> Roles = new ArrayList<String>();

                while (rs.next()) {
                    Usernames.add(rs.getString("Username"));
                    Passwords.add(rs.getString("Pwd"));
                    Salts.add(rs.getString("Salt"));
                    Firstnames.add(rs.getString("Firstname"));
                    Lastnames.add(rs.getString("Lastname"));
                    Emails.add(rs.getString("Email"));
                    Roles.add(rs.getString("Role"));
                }

                String[] usernameLog = request.getParameterValues("usernameLog");
                String[] passwordLog = request.getParameterValues("passwordLog");
                boolean Access = false;
                for (int i = 0; i < Usernames.size(); i++) {
                    if (usernameLog[0].equals(Usernames.get(i))) {
                        Access = HashPassword.checkPassword(passwordLog[0], Passwords.get(i), Salts.get(i));
                        session.setAttribute("firstname", Firstnames.get(i));
                        session.setAttribute("lastname", Lastnames.get(i));
                        session.setAttribute("username", Usernames.get(i));
                        session.setAttribute("email", Emails.get(i));
                        session.setAttribute("hashedPassword", Passwords.get(i));
                        session.setAttribute("role",Roles.get(i));
                        break;
                    }
                }
                // close connection
                conn.close();
                // display output.jsp page with given content above if successful
                if (Access) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                    request.setAttribute("message", "Login Successful");
                    dispatcher.forward(request, response);
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                    request.setAttribute("message", "Unsuccessful Login Attempt, " + (3-attempts) + " remaining.");
                    dispatcher.forward(request, response);
                }

            } catch (Exception se) {
                se.printStackTrace();
                // display error.jsp page with given message if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message", "Database Error, Please try again");
                dispatcher.forward(request, response);
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                    se2.printStackTrace();
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }else{
            session.invalidate();
        }
}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    protected int trackSession(HttpSession session){
        int loginCount = 0;
        if(session.getAttribute("loginCount")==null){
            loginCount = 1;
            session.setAttribute("loginCount",loginCount);
        }else{
            loginCount = (Integer) session.getAttribute("loginCount");
            loginCount += 1;
            session.setAttribute("loginCount",loginCount);
        }return loginCount;
    }
}
