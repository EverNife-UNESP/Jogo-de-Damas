package br.com.finalcraft.unesp.java.jogodamas.server.tcphandler;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerSideTCP extends Thread {

    private static ServerSideTCP serverSide;
    private static ServerSocketThread serverSocketThread;

    public static boolean isConnected(){
        return serverSocketThread == null ? false : serverSocketThread.isAlive();
    }

    public static ServerSocketThread getClient(){
        return serverSocketThread;
    }

    public static void initialize(int porta){

        try {
            if (serverSide != null){
                System.out.println("Encerrando servidor ouvindo na porta [" + serverSide.porta + "]");
                serverSide.interrupt();
            }
            if (serverSocketThread != null) serverSocketThread.interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }

        serverSide = new ServerSideTCP(porta);
        serverSide.start();
        System.out.println("\n\nIniciando SERVER_SIDE (TCP)");
    }

    public ServerSideTCP(int porta) {
        this.porta = porta;
    }

    int porta;

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            // Instancia o ServerSocket ouvindo a porta ...
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("----------------------------------\n\nServidor ouvindo a porta " + servidor.getLocalPort());

            while (true){
                try {
                    Socket clientConnecting = servidor.accept();
                    if (isConnected()){
                        System.out.println("Alguem tentou se conectar ao servidor, contudo, você já está em jogo!");
                        clientConnecting.close();
                    }
                    serverSocketThread = new ServerSocketThread(clientConnecting);
                    serverSocketThread.start();
                }catch (Exception e){
                    System.out.println("Erro ao tentar conectar com possivel cliente...");
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
