package com.company.Server;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<ConexionCliente> clientesConectados = new ArrayList();

    public static void controladorMSG(JSONObject msg){
        if(msg.get("to")=="all"){
            for(ConexionCliente cliente : clientesConectados){
                if(msg.get("from")!=cliente.nombre){
                    if(cliente.nombre != null){
                        cliente.enviarMensaje(msg.get("ms").toString(),"/all-"+msg.get("from").toString());
                    }
                }
            }
        }else{
            for(ConexionCliente cliente : clientesConectados){
                System.out.println("From:"+msg.get("from"));
                System.out.println("To:"+msg.get("to"));
                System.out.println("Msg:"+msg.get("ms"));
                if(msg.get("from") !=cliente.nombre & msg.get("to").equals(cliente.nombre)){
                    cliente.enviarMensaje(msg.get("ms").toString(),msg.get("from").toString());
                }
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

                ConexionCliente cc = new ConexionCliente(socket);
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