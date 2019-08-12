package com.longxw.starter.updater.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FileTool {

    public static String readText(File file) {
        try{
            return new String(readBytes(file), "UTF-8");
        }catch (IOException e){
            return "";
        }
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
