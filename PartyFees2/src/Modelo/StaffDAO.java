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
public class StaffDAO {
    
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    public boolean RegistrarStaff(Staff st){
        String sql = "INSERT INTO trabajador (nombre, ap_paterno, ap_materno, telefono, posicion, sueldo) VALUES (?,?,?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, st.getNombre());
            ps.setString(2, st.getAp_paterno());
            ps.setString(3, st.getAp_materno());
            ps.setString(4, st.getTelefono());
            ps.setString(5, st.getPosicion());
            ps.setDouble(6, st.getSueldo());
            ps.execute();
             return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    
    public List ListarStaff(){
        List<Staff> ListaSt = new ArrayList();
        String sql = "SELECT * FROM trabajador";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Staff st = new Staff();
                st.setId(rs.getInt("id"));
                st.setNombre(rs.getString("nombre"));
                st.setAp_paterno(rs.getString("ap_paterno"));
                st.setAp_materno(rs.getString("ap_materno"));
                st.setTelefono(rs.getString("telefono"));
                st.setPosicion(rs.getString("posicion"));
                st.setSueldo(rs.getDouble("sueldo"));
                ListaSt.add(st);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaSt;
    }
    
    public boolean EliminarStaff(int id){
        String sql = "DELETE FROM trabajador WHERE id = ?";
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
    
    public boolean ModificarStaff(Staff st){
        String sql = "UPDATE trabajador SET nombre=?, ap_paterno=?, ap_materno=?, telefono=?, sueldo=?, posicion=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, st.getNombre());
            ps.setString(2, st.getAp_paterno());
            ps.setString(3, st.getAp_materno());
            ps.setString(4, st.getTelefono());
            ps.setDouble(5, st.getSueldo());
            ps.setString(6, st.getPosicion());
            ps.setInt(7, st.getId());
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
    
    public int BuscarIdCli(String nombreCliente) {
        int id = 0;
        String sql = "SELECT id FROM cliente WHERE nombre = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreCliente);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
                System.out.println("ID Cliente encontrado: " + id);
            } else {
                System.out.println("No se encontró un cliente con el nombre: " + nombreCliente);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public int BuscarIdEve(String nombreEvento){
        int id = 0;
        String sql = "SELECT id FROM evento WHERE nombre = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreEvento);
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
