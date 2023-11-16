/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author dmesc
 */
public class Configuracion implements Serializable {
    private double renta;

    public double getRenta() {
        return renta;
    }

    public void setRenta(double renta) {
        this.renta = renta;
    }

    // Método para guardar la configuración en un archivo
    public void guardarConfiguracion() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("configuracion.ser"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar la configuración desde un archivo
    public static Configuracion cargarConfiguracion() {
        Configuracion configuracion = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("configuracion.ser"))) {
            configuracion = (Configuracion) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Si ocurre algún error al cargar, simplemente crea una nueva instancia
            configuracion = new Configuracion();
        }
        return configuracion;
    }
}