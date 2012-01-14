package com.iBank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Functions for Streams
 * @author steffengy
 *
 */
public class StreamUtils {
    public static String inputStreamToString(InputStream in){
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    try {
		while ((line = bufferedReader.readLine()) != null) {
		stringBuilder.append(line + "\n");
		}

		bufferedReader.close();
	} catch (IOException e) {
		return "";
	}
    return stringBuilder.toString();
    }
    public static boolean copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
