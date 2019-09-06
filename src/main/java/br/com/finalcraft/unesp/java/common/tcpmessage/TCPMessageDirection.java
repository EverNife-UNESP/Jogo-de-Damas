package br.com.finalcraft.unesp.java.common.tcpmessage;

import java.io.Serializable;

public enum TCPMessageDirection implements Serializable {
    CLIENT_TO_SERVER,
    SERVER_TO_CLIENT;
}
