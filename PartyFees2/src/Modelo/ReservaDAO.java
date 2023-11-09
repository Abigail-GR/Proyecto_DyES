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
    String sql = "INSERT INTO reserva (idcliente, idevento, precio, estado) VALUES (?,?,?,1)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, res.getIdcliente());
            ps.setInt(2, res.getIdevento());
            ps.setDouble(3, res.getPrecio());
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
}

