package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HiloEscucha extends Thread{

    Socket socket;
    BufferedReader in ;

    public HiloEscucha(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try{
            String linea;
            while((linea = in.readLine()) != null) {
                System.out.println(linea);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
