import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckDraws")
public class CheckDraws extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
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
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            // query database and get results
            ResultSet rs = stmt.executeQuery("SELECT * FROM winningDraw");
            String draw = null;
            while (rs.next()) {
                draw =  rs.getString("Draw");
            }
            // close connection
            conn.close();
            // display output.jsp page with given content above if successful
            EncryptDecrypt ED = new EncryptDecrypt();
            KeyPair kp = (KeyPair) session.getAttribute("KeyPair");
            PrivateKey privKey = kp.getPrivate();
            Object hp = session.getAttribute("hashedPassword");
            String hpS = hp.toString();
            File dir = new File("CWLotteryWebApp");
            File file = new File(dir,hpS.substring(0, 19));
            byte[] ciphertext = Files.readAllBytes(file.toPath());

            List<byte[]> blocks = splitBytesArray(ciphertext);

            ArrayList<String> numbers = new ArrayList<String>();

            for(byte[] block:blocks){
                String plaintext = new String(ED.decryptText(block, privKey));
                numbers.add(plaintext);
            }
            File deletingFile = new File(dir,hpS.substring(0, 19));
            deletingFile.delete();
            request.setAttribute("drawsTitle",null);
            for(String number:numbers) {
                if(number.equals(draw)){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                    request.setAttribute("winMessage", "Congratulations! You have won with the  draw: " + number);
                    request.setAttribute("message","Successfully checked draw/s");

                    dispatcher.forward(request,response);
                    break;
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("winMessage", "Unlucky, you have not won");
            request.setAttribute("message","Successfully checked draw/s");
            dispatcher.forward(request,response);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "File Error, Please try again");
            dispatcher.forward(request, response);

        }catch(NullPointerException e){
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "File Error, No draws");
            dispatcher.forward(request, response);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }catch(NoSuchFileException e){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("message", "File Error, No draws");
            dispatcher.forward(request, response);
        }
    }
    private List<byte[]> splitBytesArray(byte[] fileBytes){

        List<byte[]> blocks = new ArrayList<>();
        int offset = 0;
        int blockLength = 256;
        while(offset < fileBytes.length){
            byte[] byteBlock = new byte[blockLength];
            System.arraycopy(fileBytes,offset,byteBlock,0,blockLength);
            offset = offset + blockLength;
            blocks.add(byteBlock);
        }
        return blocks;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
