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
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println("GUN doPost");
        String numbers = "";
        for (int i = 1; i < 6; i++) {
            numbers += request.getParameter("nu" + String.valueOf(i)) + ",";
        }
        numbers += request.getParameter("nu6");
        String plaintext = numbers;
        try {
            EncryptDecrypt ED = new EncryptDecrypt();
            KeyPair kp =  null;
            if (session.getAttribute("KeyPair")==null){
                kp = ED.getKeyPair();
                session.setAttribute("KeyPair", kp);
            }else{
                kp = (KeyPair) session.getAttribute("KeyPair");
            }

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
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                request.setAttribute("message","Failed to create/read file");
                dispatcher.forward(request,response);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message","Successfully Submitted Draw");
        dispatcher.forward(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
