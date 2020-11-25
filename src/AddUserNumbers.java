import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // creates a string of the draw from the request parameters that were forwarded and entered by the user
        String numbers = "";
        for (int i = 1; i < 6; i++) {
            numbers += request.getParameter("nu" + String.valueOf(i)) + ",";
        }
        numbers += request.getParameter("nu6");
        String plaintext = numbers;
        // creates an instance of the EncryptDecrypt class then creates a new key pair if there is not one as a current
        // session parameter
        try {
            EncryptDecrypt ED = new EncryptDecrypt();
            KeyPair kp =  null;
            if (session.getAttribute("KeyPair")==null){
                kp = ED.getKeyPair();
                session.setAttribute("KeyPair", kp);
            }else{
                kp = (KeyPair) session.getAttribute("KeyPair");
            }
            // the draw entered by the user is then encrypted and written to a file with the name as the first 20
            // characters of the users hashed password, the file is created in the directory 'CWLotteryWebApp', if
            // neither the file or directory exists, they are both created
            PublicKey pubKey = kp.getPublic();
            byte[] ciphertext = ED.encryptText(plaintext, pubKey);
            String hp = (String) session.getAttribute("hashedPassword");
            try {
                File dir = new File("CWLotteryWebApp");
                dir.mkdir();
                File file = new File(dir, hp.substring(0, 19));
                file.createNewFile();
                FileOutputStream stream = new FileOutputStream(new File(dir,hp.substring(0, 19)),true);
                stream.write(ciphertext);
                stream.flush();
                stream.close();
            } catch (IOException ex) {
                // if there is an error the user is thrown to the error page with the given message
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message","Failed to create/read file");
                dispatcher.forward(request,response);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // if the draw is correctly added to the file, the user is returned to the account page with the given message
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message","Successfully Submitted Draw");
        dispatcher.forward(request,response);
    }
}
