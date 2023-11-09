/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author dmesc
 */
public class Consumo {
    private int id;
    private int idreserva;
    private int idproducto;
    private String producto;
    private int cantidad;
    private double subtotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(int idreserva) {
        this.idreserva = idreserva;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Consumo(int id, int idreserva, int idproducto, int cantidad, double subtotal) {
        this.id = id;
        this.idreserva = idreserva;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public Consumo() {
    }
}
