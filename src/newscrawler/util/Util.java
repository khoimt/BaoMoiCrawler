package newscrawler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class Util {
    protected static MessageDigest md5Crypto;
    protected static Logger logger;
    static {
        logger = Logger.getLogger(Util.class);
        try {
            md5Crypto = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    public static String md5(String str) {
        return byteArrayToHex(md5Crypto.digest(str.getBytes()));
    }
    
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (byte b : a) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    
    public static byte[] long2ByteArray(long l) {
        byte[] array = new byte[8];
        int i, shift;
        for (i = 0, shift = 56; i < 8; i++, shift -= 8) {
            array[i] = (byte) (0xFF & (l >> shift));
        }
        return array;
    }

    public static byte[] int2ByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (3 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static void putIntInByteArray(int value, byte[] buf, int offset) {
        for (int i = 0; i < 4; i++) {
            int valueOffset = (3 - i) * 8;
            buf[offset + i] = (byte) ((value >>> valueOffset) & 0xFF);
        }
    }

    public static int byteArray2Int(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static long byteArray2Long(byte[] b) {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            int shift = (8 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    public static boolean hasBinaryContent(String contentType) {
        if (contentType != null) {
            String typeStr = contentType.toLowerCase();
            if (typeStr.contains("image") || typeStr.contains("audio") || typeStr.contains("video") || typeStr.contains("application")) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPlainTextContent(String contentType) {
        if (contentType != null) {
            String typeStr = contentType.toLowerCase();
            if (typeStr.contains("text/plain")) {
                return true;
            }
        }
        return false;
    }
}
