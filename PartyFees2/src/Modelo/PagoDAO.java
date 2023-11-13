/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dmesc
 */
public class PagoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarPago(Pago pg){
    String sql = "INSERT INTO pago (idreserva, tipo_pago, cant_pagada, total_pago) VALUES (?,?,?,?)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, pg.getIdreserva());
            ps.setString(2, pg.getTipo_pago());
            ps.setDouble(3, pg.getCant_pagada());
            ps.setDouble(4, pg.getTotal_pago());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public List ListarPago(){
        List<Pago> ListaPago = new ArrayList();
        String sql = "SELECT * FROM pagosinfo";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Pago pg = new Pago();
                pg.setId(rs.getInt("id"));
                pg.setEvento(rs.getString("evento"));
                pg.setTipo_pago(rs.getString("tipo_pago"));
                pg.setCant_pagada(rs.getDouble("cant_pagada"));
                pg.setTotal_pago(rs.getDouble("total_pago"));
                pg.setFecha(rs.getDate("fecha"));
                ListaPago.add(pg);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaPago;
    }
     
    public boolean ModificarProducto(Producto pro){
        String sql = "UPDATE producto SET nombre=?, descripcion=?, unidad_medida=?, costo=?, precio=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getDescripcion());
            ps.setString(3, pro.getUnidad_medida());
            ps.setDouble(4, pro.getCosto());
            ps.setDouble(5, pro.getPrecio());
            ps.setInt(6, pro.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public boolean EliminarPago(int id){
        String sql = "DELETE FROM producto WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }  
        }
    }
    
    public double ObtenerPrecioDeReserva(int idReserva) {
    String sql = "SELECT * FROM reserva WHERE id = ?";
    double precio = 0.00;
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva); // Configura el parámetro id_proveedor
            rs = ps.executeQuery();

            // Verifica si se encontró un resultado y obtén el precio
            if (rs.next()) {
                precio = rs.getDouble("precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores aquí
        } finally {
            // Cierra los recursos
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                // Manejo de errores aquí
                
            }
        }
        return precio;
    }
    
    public boolean ActualizarPrecioReserva(int idReserva, double NuevoTotal_Pago) {
    String sql = "UPDATE reserva SET precio_actual=? WHERE id=?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, NuevoTotal_Pago);
            ps.setInt(2, idReserva);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
            
    public boolean RevisionStock(int productoId, int cantidadIngresada){
        boolean suficienteStock = false;
            String sql = "SELECT stock FROM producto WHERE id = ?";
            try {
                con = cn.Conectar();
                ps = con.prepareStatement(sql);
                ps.setInt(1, productoId);
                ps.execute();
                if (rs.next()) {
                        int varStock = rs.getInt("stock");
                        if (varStock >= cantidadIngresada) {
                            suficienteStock = true;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Manejo de errores aquí
                } finally {
                    // Cierra los recursos
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Manejo de errores aquí
                    }
                }
                return suficienteStock;
            }
    
    public int BuscarIdPro(String nombreProducto){
        int id = 0;
        String sql = "SELECT id FROM producto WHERE nombre = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto);
            rs = ps.executeQuery();
            if (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
}
