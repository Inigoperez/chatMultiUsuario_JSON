package com.company.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HiloEscritura extends Thread{
    Socket socket;
    BufferedWriter out ;
    Scanner sc = new Scanner(System.in);

    public HiloEscritura(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String linea;
        while((linea = sc.nextLine()) != "terminar") {
            try {
                out.write(linea);
                out.newLine();
                out.flush();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}