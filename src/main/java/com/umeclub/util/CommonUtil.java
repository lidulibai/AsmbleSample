package com.umeclub.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CommonUtil {
    public static String getNameByWastFile(String fileName) {
        File f = new File(fileName);
        String tempFile = f.getName();
        String className = tempFile.substring(0, tempFile.indexOf("."));
        className = new StringBuilder().append(Character.toUpperCase(className.charAt(0)))
            .append(className.substring(1)).toString();
        return className;
    }

    public static void importBasicLib(String fileName) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        String str = br.readLine();
        boolean first = true;
        while (str != null) {
            if (!str.contains("import")) {
                sb.append(str + "\n");
            } else {
                if (first) {
                    first = false;
                    sb.append(" (import \"env\" \"print\" (func $print (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"printNum\" (func $printNum (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"println\" (func $println (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"printlnNum\" (func $printlnNum (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"allowed\" (func $allowed (param i32 i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"getAdress\" (func $getAdress (result i32)))\n");
                    sb.append(" (import \"env\" \"getMyBalance\" (func $getMyBalance (result i32)))\n");
                    sb.append(" (import \"env\" \"getOtherBalance\" (func $getOtherBalance (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"initToken\" (func $initToken (param i32 i32 i32 i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"setAllowed\" (func $setAllowed (param i32 i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"setBalance\" (func $setBalance (param i32) (result i32)))\n");
                    sb.append(" (import \"env\" \"setOtherBalance\" (func $setOtherBalance (param i32 i32) (result i32)))\n");
                }
            }
            str = br.readLine();
        }
        br.close();
        fr.close();
        FileWriter fw = new FileWriter(fileName);
        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }
}
