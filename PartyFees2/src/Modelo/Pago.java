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
public class Pago {
    private int id;
    private int idreserva;
    private String evento;
    private String tipo_pago;
    private double cant_pagada;
    private double total_pago;
    private Date fecha;

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

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getTipo_pago() {
        return tipo_pago;
    }

    public void setTipo_pago(String tipo_pago) {
        this.tipo_pago = tipo_pago;
    }

    public double getCant_pagada() {
        return cant_pagada;
    }

    public void setCant_pagada(double cant_pagada) {
        this.cant_pagada = cant_pagada;
    }

    public double getTotal_pago() {
        return total_pago;
    }

    public void setTotal_pago(double total_pago) {
        this.total_pago = total_pago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Pago() {
    }

    public Pago(int id, int idreserva, String evento, String tipo_pago, double cant_pagada, double total_pago, Date fecha) {
        this.id = id;
        this.idreserva = idreserva;
        this.evento = evento;
        this.tipo_pago = tipo_pago;
        this.cant_pagada = cant_pagada;
        this.total_pago = total_pago;
        this.fecha = fecha;
    }

}
