package br.com.finalcraft.unesp.java.client.tcp;

import br.com.finalcraft.unesp.java.common.tcpmessage.TCPMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSideTCP extends Thread {

    public static ClientSideTCP clientSide;

    public static boolean isConnected(){
        return (clientSide == null || clientSide.serverSocket == null) ? false : clientSide.serverSocket.isConnected();
    }

    private final String ip;
    private final int port;

    public ClientSideTCP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void initialize(String ip, int port){
        if (isConnected()) System.out.println("Você já está conectado!");

        System.out.println("Iniciando CLIENT_SIDE_TCP (Targeting \"" + ip + ":" + port + "\")");
        clientSide = new ClientSideTCP(ip,port);
        clientSide.run();
        //clientSide.start(); Não quero mais fazer a conexão assincrona, bora deixar tudo na main thread memo....
    }

    public static void sendToServer(TCPMessage tcpMessage){
        System.out.println("Sending TCPMessage." + tcpMessage.getClass().getSimpleName() + " to server: " + tcpMessage.toString());
        try {
            clientSide.objectOutputStream.flush();
            clientSide.objectOutputStream.writeObject(tcpMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static TCPMessage readFromServer(){
        try {
            System.out.println("Waiting TCPMessage from server...");
            Object readObject = clientSide.objectInputStream.readObject();
            if (readObject instanceof TCPMessage){
                System.out.println("Received TCPMessage." + readObject.getClass().getSimpleName() + " from Server: " + readObject.toString());
                return (TCPMessage) readObject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket serverSocket;

    @Override
    public void run() {
        try {
            // Inicia conexão com o servidor
            serverSocket = new Socket(ip, port);
            objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(serverSocket.getInputStream());

            System.out.println("Conectando ao servidor [" + serverSocket.getInetAddress().getHostAddress() + "]");
            System.out.println("\n\n");
        }catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
