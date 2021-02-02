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
        /*try{
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }*/
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
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
            JSONParser parser = new JSONParser();
            JSONObject msg = (JSONObject) parser.parse(in.readObject().toString());
            if(!(msg.get("ms")).equals("Terminar")){
                Server.controladorMSG(msg);
            }

            /*BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String linea;
            while((linea = br.readLine()) != null){
                String[] datos = linea.split("\\|");
                JSONObject msg = new JSONObject();
                    if(datos.length ==1) {
                        msg.put("from",this.nombre);
                        msg.put("to","all");
                        msg.put("ms",datos[0]);
                    }else if(datos[0] == "all"){
                        msg.put("from",this.nombre);
                        msg.put("to",datos[0]);
                        msg.put("ms",datos[1]);
                    }else{
                        msg.put("from",this.nombre);
                        msg.put("to",datos[0]);
                        msg.put("ms",datos[1]);
                    }

                BufferedWriter bw_json = new BufferedWriter(new FileWriter("MSG.json"));
                bw_json.write(msg.toJSONString());
                bw_json.close();

                Server.controladorMSG(msg);
            }*/
        } catch (IOException | ClassNotFoundException | ParseException exception) {
            exception.printStackTrace();
        }
    }
}
