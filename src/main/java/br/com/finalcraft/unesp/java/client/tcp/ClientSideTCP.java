package br.com.finalcraft.unesp.java.client.tcp;

import br.com.finalcraft.unesp.java.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.common.tcpmessage.TCPMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
        if (clientSide.connect()){
            clientSide.start();
        }
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

    public static TCPMessage readFromServer() throws Exception{
        System.out.println("Waiting TCPMessage from server...");
        Object readObject = clientSide.objectInputStream.readObject();
        if (readObject instanceof TCPMessage){
            System.out.println("Received TCPMessage." + readObject.getClass().getSimpleName() + " from Server: " + readObject.toString());
            return (TCPMessage) readObject;
        }
        return null;
    }


    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket serverSocket;

    public boolean connect(){
        try {
            // Inicia conexão com o servidor
            serverSocket = new Socket(ip, port);
            objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(serverSocket.getInputStream());

            System.out.println("Conectando ao servidor [" + serverSocket.getInetAddress().getHostAddress() + "]");
            System.out.println("\n\n");
            return true;
        }catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while(true) {
                try {
                    TCPMessage tcpMessage = readFromServer();
                    handleTCPMessage(tcpMessage);
                } catch(SocketException e) {
                    System.out.println("Erro: ");
                    e.printStackTrace();
                    break;  //Fechar conexão (no caso, finalizar a thread)
                }
                catch(Exception e) {
                    System.out.println("Erro: ");
                    e.printStackTrace();
                }
            }
        }catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


    public void handleTCPMessage(TCPMessage tcpMessage) throws Exception{

        if (tcpMessage instanceof TCPMessage.CheckersTable){
            handleTCPMessageCheckersTable((TCPMessage.CheckersTable) tcpMessage);
        }

    }

    public void handleTCPMessageCheckersTable(TCPMessage.CheckersTable tcpMessage){
        CheckersTheGame.updateNewInstance(tcpMessage.getTheTable());
    }
}
