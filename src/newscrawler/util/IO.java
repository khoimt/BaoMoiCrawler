package newscrawler.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class IO {

    public static boolean deleteFolder(File folder) {
        return deleteFolderContents(folder) && folder.delete();
    }

    public static boolean deleteFolderContents(File folder) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                if (!deleteFolder(file)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void writeBytesToFile(byte[] bytes, String destination) {
        try {
            FileChannel fc = new FileOutputStream(destination).getChannel();
            fc.write(ByteBuffer.wrap(bytes));
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
