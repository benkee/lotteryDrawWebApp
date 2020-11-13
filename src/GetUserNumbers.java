import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.*;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
        HttpSession session = request.getSession();
        try {
            EncryptDecrypt ED = new EncryptDecrypt();
            KeyPair kp = (KeyPair) session.getAttribute("KeyPair");
            PrivateKey privKey = kp.getPrivate();
            Object hp = session.getAttribute("hashedPassword");
            String hpS = hp.toString();
            File dir = new File("CWLotteryWebApp");
            File file = new File(dir,hpS.substring(0, 19));

            byte[] ciphertext = Files.readAllBytes(file.toPath());
            System.out.println(Arrays.toString(ciphertext));
            List<byte[]> blocks = splitBytesArray(ciphertext);
            ArrayList<String> numbers = new ArrayList<String>();
            for(byte[] block:blocks){
                String plaintext = new String(ED.decryptText(block, privKey));
                System.out.println(plaintext);
                numbers.add(plaintext);
            }
            System.out.println(numbers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("draws", numbers);
            request.setAttribute("title", "Draw: ");
            request.setAttribute("message","Successfully returned draw/s");
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
        }
    }
    private List<byte[]> splitBytesArray(byte[] fileBytes){

        List<byte[]> blocks = new ArrayList<byte[]>();
        int offset = 0;
        int blockLength = 256;

        while(offset < fileBytes.length){
            byte[] byteBlock = new byte[blockLength];
            System.arraycopy(fileBytes,offset,byteBlock,0,blockLength);
            offset += blockLength;
            blocks.add(byteBlock);
        }
        return blocks;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
