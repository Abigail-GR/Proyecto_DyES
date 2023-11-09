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
    private String cliente;
    private int idevento;
    private String evento;
    private String descripcion;
    private Date fecha_eve;
    private String hora_ini;
    private String hora_fin;
    private Date fecha;
    private double precio;
    private int estado;
    private String estadotxt;

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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getIdevento() {
        return idevento;
    }

    public void setIdevento(int idevento) {
        this.idevento = idevento;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_eve() {
        return fecha_eve;
    }

    public void setFecha_eve(Date fecha_eve) {
        this.fecha_eve = fecha_eve;
    }

    public String getHora_ini() {
        return hora_ini;
    }

    public void setHora_ini(String hora_ini) {
        this.hora_ini = hora_ini;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
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

    public String getEstadotxt() {
        return estadotxt;
    }

    public void setEstadotxt(String estadotxt) {
        this.estadotxt = estadotxt;
    }

    public Reserva(int id, int idcliente, String cliente, int idevento, String evento, String descripcion, Date fecha_eve, String hora_ini, String hora_fin, Date fecha, double precio, int estado, String estadotxt) {
        this.id = id;
        this.idcliente = idcliente;
        this.cliente = cliente;
        this.idevento = idevento;
        this.evento = evento;
        this.descripcion = descripcion;
        this.fecha_eve = fecha_eve;
        this.hora_ini = hora_ini;
        this.hora_fin = hora_fin;
        this.fecha = fecha;
        this.precio = precio;
        this.estado = estado;
        this.estadotxt = estadotxt;
    }

    public Reserva() {
    }

}
