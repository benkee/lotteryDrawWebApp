import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashPassword  {
    public static String hashedPassword;
    public static String returnSalt;

    public static void hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = createSalt();
        String hashISaltPassword = generateHash(password, salt);
        String[] parts = hashISaltPassword.split(":");
        returnSalt = parts[1];
        hashedPassword = parts[2];
    }

    public static String getHashedPassword() {
        return hashedPassword;
    }

    public static String getSalt(){
        return returnSalt;
    }
    private static String generateHash(String password,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int i = 1000;
        char[] chars = password.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, i, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return i + ":" + toHex(salt) + ":" + toHex(hash);

    }
    private static byte[] createSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bigInt = new BigInteger(1, array);
        String hex = bigInt.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }

    }
    static boolean checkPassword(String originalPassword, String storedPassword, String storedSalt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        byte[] salt = fromHex(storedSalt);
        byte[] hash = fromHex(storedPassword);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

}
