import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class RSAKeyPairGen {
    private static KeyPair keyPair;
    private static PrivateKey privKey;
    private static PublicKey pubKey;

    public RSAKeyPairGen() throws NoSuchAlgorithmException {
        KeyPairGenerator kpgen = KeyPairGenerator.getInstance("RSA");
        kpgen.initialize(1024);
        keyPair = kpgen.generateKeyPair();
        this.privKey = keyPair.getPrivate();
        this.pubKey = keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey() {
        return privKey;
    }

    public static PublicKey getPublicKey() {
        return pubKey;
    }

    public static KeyPair getKeyPair() {
        return keyPair;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        RSAKeyPairGen keyPairGenerator = new RSAKeyPairGen();

    }
}
