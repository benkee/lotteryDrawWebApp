import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
            System.out.println(plaintext);
            byte[] ciphertext = ED.encryptText(plaintext);
            System.out.println(EncryptDecrypt.keyToNum(ciphertext).toString()+"'");
            String decrypttext = new String(ED.decryptText(ciphertext));
            System.out.println(decrypttext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
