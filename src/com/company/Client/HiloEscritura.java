package com.company.Client;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class HiloEscritura extends Thread{
    Socket socket;
    BufferedWriter outWrite ;
    ObjectOutputStream outObj;
    String nombre;
    Scanner sc = new Scanner(System.in);

    public HiloEscritura(Socket socket) throws IOException {
        this.socket = socket;
        this.outWrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.outObj = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        String linea;

        while (!(linea = sc.nextLine()).equals("terminar")) {
            if(this.nombre == null){

                this.nombre = linea;

                try {
                    outWrite.write(linea);
                    outWrite.newLine();
                    outWrite.flush();
                    System.out.println("Tu usuario es : "+this.nombre);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }else {

                String[] datos = linea.split("\\|");
                JSONObject msg = new JSONObject();
                if (datos.length == 1) {
                    msg.put("from", this.nombre);
                    msg.put("to", "all");
                    msg.put("ms", datos[0]);
                } else if (datos[0].equals("all")) {
                    msg.put("from", this.nombre);
                    msg.put("to", datos[0]);
                    msg.put("ms", datos[1]);
                } else {
                    msg.put("from", this.nombre);
                    msg.put("to", datos[0]);
                    msg.put("ms", datos[1]);
                }
                try {
                    outObj.writeObject(msg.toJSONString());
                    System.out.println(msg.toJSONString());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}