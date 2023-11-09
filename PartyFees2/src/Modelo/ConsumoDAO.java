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
public class ConsumoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int r;
    
    public int RegistrarConsumo(Consumo cons){
        String sql = "INSERT INTO consumo (idreserva, idproducto, cantidad, subtotal) VALUES (?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, cons.getIdreserva());
            ps.setInt(2, cons.getIdproducto());
            ps.setInt(3, cons.getCantidad());
            ps.setDouble(4, cons.getSubtotal());
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return r;
    }
    
    public List ListarConsumo(){
        List<Consumo> ListaCon = new ArrayList();
        String sql = "SELECT * FROM consumo";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Consumo cons = new Consumo();
                cons.setId(rs.getInt("id"));
                cons.setIdreserva(rs.getInt("idreserva"));
                cons.setIdproducto(rs.getInt("idproducto"));
                cons.setCantidad(rs.getInt("cantidad"));
                cons.setSubtotal(rs.getDouble("subtotal"));
                ListaCon.add(cons);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaCon;
    }
}
