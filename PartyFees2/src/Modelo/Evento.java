/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;
import java.sql.Time;
 

/**
 *
 * @author dmesc
 */
public class Evento {
    private int id;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private Time hora_ini;
    private Time hora_fin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora_ini() {
        return hora_ini;
    }

    public void setHora_ini(Time hora_ini) {
        this.hora_ini = hora_ini;
    }

    public Time getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(Time hora_fin) {
        this.hora_fin = hora_fin;
    }

    public Evento(int id, String nombre, String descripcion, Date fecha, Time hora_ini, Time hora_fin) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora_ini = hora_ini;
        this.hora_fin = hora_fin;
    }

    public Evento() {
    }
}
