package br.com.finalcraft.unesp.java.jogodamas.common;

public class SmartLogger {

    public static boolean DEBUG_RENDER = false;
    public static boolean DEBUG_LOGICAL = false;

    public static void info(String message){
        System.out.println(message);
    }

    public static void debugScreen(String message){
        if (DEBUG_RENDER) System.out.println(message);
    }

    public static void debugLogical(String message){
        if (DEBUG_LOGICAL) System.out.println(message);
    }
}
