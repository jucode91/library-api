package com.jay.apis.libraryapi.util;

public class LibraryApiUtils {
    public static boolean doesStringValueExist(String str) {
        if(str != null && str.trim().length()>0){
            return true;
        }
        return false;
    }
}
