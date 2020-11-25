import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
    private Connection conn;
    private Statement stmt;
    // called from a form/button in a jsp with action post to 'GetUserNumbers'
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int attempts = trackSession(session);
        //gets the current session's login attempts, if they are less than 3, runs the code to login, if not the session
        //is invalidated so the user can no longer use the forms without restarting the session.
        if (attempts < 3) {
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

                // creates arrays for every column in the accounts database
                ArrayList<String> Usernames = new ArrayList<String>();
                ArrayList<String> Passwords = new ArrayList<String>();
                ArrayList<String> Salts = new ArrayList<String>();
                ArrayList<String> Firstnames = new ArrayList<String>();
                ArrayList<String> Lastnames = new ArrayList<String>();
                ArrayList<String> Emails = new ArrayList<String>();
                ArrayList<String> Roles = new ArrayList<String>();

                while (rs.next()) {
                    //adds the values for each row into their associated columns(values)
                    Usernames.add(rs.getString("Username"));
                    Passwords.add(rs.getString("Pwd"));
                    Salts.add(rs.getString("Salt"));
                    Firstnames.add(rs.getString("Firstname"));
                    Lastnames.add(rs.getString("Lastname"));
                    Emails.add(rs.getString("Email"));
                    Roles.add(rs.getString("Role"));
                }
                //gets the values from the login form for username login and compares them to every username and
                //password combination in the accounts database, if the hashed password entered in the login form
                //matches a hashed password in the database, access is granted and the session attributes are set
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
                // if access is true; resets the login counter due to successful login, if the role of the user is
                // admin, they are forwarded to the admin page, if they are public they are forwarded to their account
                // page, if access is not true, the login count is incremented and the user is sent back to the homepage
                if (Access) {
                    session.setAttribute("loginCount",null);
                    if(session.getAttribute("role").equals("public")){
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                        request.setAttribute("message", "Login Successful");
                        dispatcher.forward(request, response);}
                    if(session.getAttribute("role").equals("admin")) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/admin_home.jsp");
                        request.setAttribute("message", "Login Successful");
                        dispatcher.forward(request, response);}
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                    request.setAttribute("message", "Unsuccessful Login Attempt, " + (3-attempts) + " remaining.");
                    dispatcher.forward(request, response);
                }
                //if there are any errors with the database the user is returned to the home page
            } catch (Exception se) {
                se.printStackTrace();
                // display error.jsp page with given message if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message", "Database Error, Please try again");
                dispatcher.forward(request, response);
                //if there are statements ot connections which are open, they are closed here.
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
            System.out.println("Session Invalidated");
        }
}
    // this function created a login count if there is not one in the current session, however if there is, it is
    // incremented by 1
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
