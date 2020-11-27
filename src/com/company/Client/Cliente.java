package com.company.Client;

import java.io.IOException;
import java.net.Socket;

public class Cliente{

    public static void main(String[] args){

        int port=1234;

        try {

            Socket clientSocket = new Socket("localhost", port);

            HiloEscucha hiloEscuchar = new HiloEscucha(clientSocket);
            HiloEscritura hiloEscritura = new HiloEscritura(clientSocket);
            hiloEscuchar.start();
            hiloEscritura.start();

        }catch (IOException e) {
            System.out.println(e);
        }
    }
}
