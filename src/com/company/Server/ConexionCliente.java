package com.company.Server;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ConexionCliente extends Thread{

    Socket socket;
    String nombre;
    BufferedWriter bw ;

    ConexionCliente(Socket socket,String nombre){
        this.socket = socket;
        this.nombre = nombre;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void enviarMensaje(String s,String nombre){
        s = nombre+":"+s;
        try{
            bw.write(s);
            bw.newLine();
            bw.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void run() {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String linea;
            while((linea = br.readLine()) != null){
                String[] datos = linea.split("|");

                JSONObject msg = new JSONObject();
                    msg.put("from",this.nombre);
                    msg.put("to",datos[0]);
                    msg.put("ms",datos[1]);

                try (FileWriter file = new FileWriter("MSG.json")) {
                    file.write(msg.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Server.broadcast(linea,this.nombre,socket);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
