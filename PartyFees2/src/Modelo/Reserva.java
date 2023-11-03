/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author dmesc
 */
public class Reserva {
    private int id;
    private int idcliente;
    private int idevento;
    private Date fecha;
    private double precio;
    private int estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getIdevento() {
        return idevento;
    }

    public void setIdevento(int idevento) {
        this.idevento = idevento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Reserva(int id, int idcliente, int idevento, Date fecha, double precio, int estado) {
        this.id = id;
        this.idcliente = idcliente;
        this.idevento = idevento;
        this.fecha = fecha;
        this.precio = precio;
        this.estado = estado;
    }

    public Reserva() {
    }
}
