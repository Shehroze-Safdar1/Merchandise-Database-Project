
package ViewLogin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    public Security() {
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        byte[] var4 = b;
        int var5 = b.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte b1 = var4[var6];
            sb.append(Integer.toHexString(b1 & 255).toString());
        }

        return sb.toString();
    }
}
