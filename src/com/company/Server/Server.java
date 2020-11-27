package com.company.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<ConexionCliente> clientesConectados = new ArrayList();

    public static void broadcast(String s,String nombre,Socket socket){
        for(ConexionCliente cliente : clientesConectados){
            if(cliente.socket != socket) {
                cliente.enviarMensaje(s,nombre);
            }
        }
    }


    public static void main(String[] args) {

        int puerto = 1234;
        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(puerto);

            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Conexion realizada");

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write("Inserte tu nombre de usuario: ");
                out.newLine();
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String usuario = in.readLine();

                ConexionCliente cc = new ConexionCliente(socket,usuario);
                cc.start();
                clientesConectados.add(cc);
            }

        } catch (IOException ex) {
            System.out.println(ex);
        } finally{
            try {
                servidor.close();
            } catch (IOException ex) {
               System.out.println(ex);
            }
        }
    }
}