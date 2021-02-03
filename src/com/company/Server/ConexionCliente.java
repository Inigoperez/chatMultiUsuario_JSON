package com.company.Server;

import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;

public class ConexionCliente extends Thread{

    Socket socket;
    String nombre;
    BufferedWriter bw ;

    ConexionCliente(Socket socket){
        this.socket = socket;
    }

    public void enviarMensaje(String s,String nombre){
        s = nombre+":"+s;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
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
/*
    Este BufferedReadeer se encargará de recibir todos los mensajes de la conexión socket de este cliente/servidor.
 */
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            JSONParser parser = new JSONParser();
            boolean terminar = true;
/*
    Este while cada vez que el servidor conecta y crea un hilo CC (ConexiónCliente) y en este hilo no este definido el nombre de usuario
    enviará un mensaje pidiendo que insertes el nombre de usuario. Una vez detecte que ha recibido un mensaje este lo pondrá como nombre
    de usuario dentro de este hilo CC.
 */
            while(this.nombre == null){
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write("Inserte tu nombre de usuario: ");
                out.newLine();
                out.flush();
                this.nombre = br.readLine();
            }
/*
    En este caso ya no tiene que entrar en el bucle de arriba, ya que tenemos nombre de usuario ya dentro de la variable de este hilo.
 */
            while (terminar){
                JSONObject msg = (JSONObject) parser.parse(in.readObject().toString());
                if(msg.get("ms") != "Terminar"){
                    Server.controladorMSG(msg);
                }
                else{
                    terminar = false;
                }
            }
        } catch (IOException | ClassNotFoundException | ParseException exception) {
            exception.printStackTrace();
        }
    }
}
