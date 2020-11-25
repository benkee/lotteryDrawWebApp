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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {
    // called from a form/button in a jsp with action post to 'GetUserNumbers'
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
        HttpSession session = request.getSession();
        try {
            // here the key pair and the hashed password are called from the session object, then the data from the file
            // named as the first 20 characters of the hashed password is read. Then the bytes in the file are split
            // into blocks which can be decrypted using the keypair from the session. The decrypted draws from the file
            // are then passed through the request to the jsp file
            EncryptDecrypt ED = new EncryptDecrypt();
            KeyPair kp = (KeyPair) session.getAttribute("KeyPair");
            PrivateKey privKey = kp.getPrivate();
            Object hp = session.getAttribute("hashedPassword");
            String hpS = hp.toString();
            File dir = new File("CWLotteryWebApp");
            File file = new File(dir,hpS.substring(0, 19));
            byte[] ciphertext = Files.readAllBytes(file.toPath());

            List<byte[]> blocks = splitBytesArray(ciphertext);

            ArrayList<String> numbers = new ArrayList<>();

            for(byte[] block:blocks){
                String plaintext = new String(ED.decryptText(block, privKey));
                numbers.add(plaintext);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("draws", numbers);
            request.setAttribute("drawsTitle", "Draw: ");
            request.setAttribute("message","Successfully returned draw/s");
            dispatcher.forward(request,response);
        // if an error is thrown the user is redirected to the error page
        } catch (NoSuchAlgorithmException | NullPointerException | NoSuchFileException e) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
            request.setAttribute("message", "File Error, No draws");
            dispatcher.forward(request, response);
        }
    }
    private List<byte[]> splitBytesArray(byte[] fileBytes){
        // this function takes a byte and splits it into blocks of 256 bytes in a byte array
        List<byte[]> blocks = new ArrayList<byte[]>();
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
}