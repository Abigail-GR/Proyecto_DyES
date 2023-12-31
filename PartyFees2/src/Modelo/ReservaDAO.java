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
import javax.swing.JOptionPane;

/**
 *
 * @author dmesc
 */
public class ReservaDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int r;
    
    public boolean RegistrarReserva(Reserva res){
    String sql = "INSERT INTO reserva (idcliente, idevento, precio, precio_actual, estado) VALUES (?,?,?,?,1)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, res.getIdcliente());
            ps.setInt(2, res.getIdevento());
            ps.setDouble(3, res.getPrecio());
            ps.setDouble(4, res.getPrecio_actual());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    public int IdReserva(){
        int id = 0;
        String sql = "SELECT MAX(id) FROM reserva";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public List ListarReservas(){
        List<Reserva> ListaRes = new ArrayList();
        String sql = "SELECT * FROM reservainfo";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Reserva res = new Reserva();
                res.setId(rs.getInt("id"));
                res.setCliente(rs.getString("cliente"));
                res.setEvento(rs.getString("evento"));
                res.setDescripcion(rs.getString("descripcion"));
                res.setFecha_eve(rs.getDate("fecha"));
                res.setHora_ini(rs.getString("hora_ini"));
                res.setHora_fin(rs.getString("hora_fin"));
                res.setPrecio(rs.getDouble("precio"));
                res.setPrecio_actual(rs.getDouble("precio_actual"));
                res.setFecha(rs.getDate("fecha_res"));
                res.setEstadotxt(rs.getString("estado"));
                ListaRes.add(res);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaRes;
    }
    
    public void ActualizarEstadoFinalizado() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = cn.Conectar();
            
            // Actualizar el estado de la reserva si el precio actual es 0
            String sql = "UPDATE reserva SET estado = 0 WHERE precio_actual = 0";
            
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de excepciones, puedes personalizarlo según tus necesidades
        } finally {
            // Cerrar recursos (PreparedStatement y Connection) en el bloque finally
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo básico de excepciones, puedes personalizarlo según tus necesidades
            }
        }
    }
    
    public void ActualizarEstadoActivo() {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = cn.Conectar();
            
            // Actualizar el estado de la reserva si el precio actual es 0
            String sql = "UPDATE reserva SET estado = 1 WHERE precio_actual > 0";
            
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo básico de excepciones, puedes personalizarlo según tus necesidades
        } finally {
            // Cerrar recursos (PreparedStatement y Connection) en el bloque finally
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo básico de excepciones, puedes personalizarlo según tus necesidades
            }
        }
    }
}

