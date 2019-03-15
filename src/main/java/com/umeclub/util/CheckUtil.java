package com.umeclub.util;

public class CheckUtil {
    public static boolean isWastOrWasmFile(String fileName) {
        return fileName.endsWith("wast") || fileName.endsWith("wasm");
    }
}
