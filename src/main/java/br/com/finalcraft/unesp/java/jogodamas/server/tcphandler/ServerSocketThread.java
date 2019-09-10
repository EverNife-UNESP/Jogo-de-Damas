package br.com.finalcraft.unesp.java.jogodamas.server.tcphandler;

import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.jogodamas.common.tcpmessage.TCPMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerSocketThread extends Thread{

    public String identifier;

    Socket clientConnecting;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public ServerSocketThread(Socket clientConnecting) throws Exception{
        this.clientConnecting = clientConnecting;
        this.objectInputStream = new ObjectInputStream(clientConnecting.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientConnecting.getOutputStream());
        this.identifier = "[" + clientConnecting.getInetAddress().getHostAddress() + " - " + clientConnecting.getLocalPort() + "]";
    }

    public void sendToClient(TCPMessage tcpMessage){
        try {
            info("Sending TCPMessage." + tcpMessage.getClass().getSimpleName() + " to client: " + tcpMessage.toString());
            objectOutputStream.flush();
            objectOutputStream.writeObject(tcpMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TCPMessage readFromClient() throws Exception{
        info("Waiting TCPMessage from Client!...");
        Object readObject = objectInputStream.readObject();
        if (readObject != null && readObject instanceof TCPMessage){
            TCPMessage tcpMessage = (TCPMessage) readObject;
            info("Received TCPMessage." + tcpMessage.getClass().getSimpleName() + ": " + tcpMessage.toString());
            return (TCPMessage) readObject;
        }
        throw new Exception("Esparava uma TCPMessage, mas recebi outro objeto...");
    }

    public void info(String message){
        SmartLogger.info("[Thread - " + clientConnecting.getInetAddress().getHostAddress() +":" + clientConnecting.getPort() + "]: " + message.toString());
    }

    @Override
    public void run() {
        while(true) {
            try {
                TCPMessage tcpMessage = readFromClient();
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
