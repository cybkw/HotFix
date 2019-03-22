package com.scex.library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author bkw
 */
public class FileUtils {


    /**
     * @param soursFile  源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(final File soursFile, final File targetFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(soursFile);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = bufferedInputStream.read(b)) != -1) {
            bufferedOutputStream.write(b, 0, len);
        }

        bufferedOutputStream.flush();

        bufferedInputStream.close();
        bufferedOutputStream.close();
        outputStream.close();
        inputStream.close();
    }
}
