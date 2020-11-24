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
        String hashPass = generateHash(password, salt);
        String[] parts = hashPass.split(":");
        returnSalt = parts[0];
        hashedPassword = parts[1];
        // this function creates a salt then uses this to hash a given password (parameter) then sets the global class
        // variables to the returned salt and hashed password values.
    }
    // getters
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
        return toHex(salt) + ":" + toHex(hash);
        //this function takes 2 parameters, a password and a salt, then hashes the password using PBKDF2WithHmacSHA1
        // then returns the hex values of the calculated salt and hash
    }
    private static byte[] createSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
        //this creates 16 cryptographically secure characters for the salt
    }
    private static String toHex(byte[] array) {
        BigInteger bigInt = new BigInteger(1, array);
        String hex = bigInt.toString(16);
        int padLength = (array.length * 2) - hex.length();
        if(padLength > 0)
        {
            return String.format("%0"  +padLength + "d", 0) + hex;
        }else{
            return hex;
        }
        // this converts the given array (parameter) to a hex value
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
        // this function gets a string password, then the hex values for the hashed password and salt in the database
        // first it converts the hex to bytes, the it hashes the given string password with the stored byte, if the
        // new hashed password is the same as the hashed password in the database, the function returns true, else false
    }
    private static byte[] fromHex(String hex)
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
        // this function converts a hex string to a byte array
    }

}
