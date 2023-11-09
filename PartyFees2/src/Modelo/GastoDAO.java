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
public class GastoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int r;
    
    public boolean RegistrarGasto(Gasto gas){
    String sql = "INSERT INTO gastos_fijos (nombre, descripcion, costo) VALUES (?,?,?)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, gas.getNombre());
            ps.setString(2, gas.getDescripcion());
            ps.setDouble(3, gas.getCosto());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    public List ListarGastos(){
        List<Gasto> ListaGas = new ArrayList();
        String sql = "SELECT * FROM gastos_fijos";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Gasto gas = new Gasto();
                gas.setId(rs.getInt("id"));
                gas.setNombre(rs.getString("nombre"));
                gas.setDescripcion(rs.getString("descripcion"));
                gas.setCosto(rs.getDouble("costo"));
                ListaGas.add(gas);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaGas;
    }
    
    public boolean ModificarGasto(Gasto gas){
        String sql = "UPDATE gastos_fijos SET nombre=?, descripcion=?, costo=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, gas.getNombre());
            ps.setString(2, gas.getDescripcion());
            ps.setDouble(3, gas.getCosto());
            ps.setInt(4, gas.getId());
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
    
    public boolean EliminarGasto(int id){
        String sql = "DELETE FROM gastos_fijos WHERE id = ?";
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
}
