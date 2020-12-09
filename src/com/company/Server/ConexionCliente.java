package com.company.Server;

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
            while(this.nombre == null){
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write("Inserte tu nombre de usuario: ");
                out.newLine();
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.nombre = in.readLine();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String linea;
            System.out.println("error -1");
            while(!(linea = br.readLine()).equals("Terminar")){
            //while((linea = br.readLine()) != "Terminar"){
                System.out.println("error 0");
                JSONParser parser = new JSONParser();
                System.out.println("error 1"+ linea);
                JSONObject msg = (JSONObject) parser.parse(linea);
                System.out.println("error 2 "+msg);
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
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }
}
