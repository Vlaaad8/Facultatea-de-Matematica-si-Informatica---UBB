package org.example.servers;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    public AbstractConcurrentServer(int port){
        super(port);
    }
    @Override
    public void processRequest(Socket client) {
        System.out.println("Am inceput sa lucrez la procesarea requestului");
        Thread threadWorker=createWorker(client);
        threadWorker.start();
    }
    public abstract Thread createWorker(Socket client);
}
