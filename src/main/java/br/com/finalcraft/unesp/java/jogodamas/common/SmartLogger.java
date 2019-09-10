package br.com.finalcraft.unesp.java.jogodamas.common;

public class SmartLogger {

    public static boolean DEBUG_MODE_SCREEN = false;
    public static boolean DEBUG_MODE_LOGICAL = true;

    public static void info(String message){
        System.out.println(message);
    }

    public static void debugScreen(String message){
        if (DEBUG_MODE_SCREEN) System.out.println(message);
    }

    public static void debugLogical(String message){
        if (DEBUG_MODE_LOGICAL) System.out.println(message);
    }
}
