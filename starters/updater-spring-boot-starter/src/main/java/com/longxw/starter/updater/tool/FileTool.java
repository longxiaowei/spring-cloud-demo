package com.longxw.starter.updater.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileTool {

    private static Charset defaultCharset = Charset.forName("UTF-8");

    public static List<String> readLines(File file) throws IOException{
        return readLines(file.toPath());
    }

    public static List<String> readLines(Path path) throws IOException{
        return Files.readAllLines(path,defaultCharset);
    }

    public static String readText(Path path) throws IOException{
        StringBuilder sb = new StringBuilder();
        readLines(path).forEach( line -> sb.append(line));
        return sb.toString();
    }

    public static String readText(File file) throws IOException{
        return new String(readBytes(file), defaultCharset);
    }

    public static byte[] readBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        int remaining = new Long(file.length()).intValue();
        if(remaining == 0){
            return new byte[0];
        }
        int offset = 0;
        byte[] result = new byte[remaining];
        while (remaining > 0){
            int read = is.read(result,offset,remaining);
            if(read < 0 )
                break;
            remaining -= read;
            offset += read;
        }

        return Arrays.copyOf(result, offset);
    }

}
