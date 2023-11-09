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
public class ClienteDAO {
    
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    public boolean RegistrarCliente(Cliente cl){
        String sql = "INSERT INTO cliente (nombre, ap_paterno, ap_materno, telefono, correo, direccion) VALUES (?,?,?,?,?,?)";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getAp_paterno());
            ps.setString(3, cl.getAp_materno());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getCorreo());
            ps.setString(6, cl.getDireccion());
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
    
    public List ListarCliente(){
        List<Cliente> ListaCl = new ArrayList();
        String sql = "SELECT * FROM cliente";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cl = new Cliente();
                cl.setId(rs.getInt("id"));
                cl.setNombre(rs.getString("nombre"));
                cl.setAp_paterno(rs.getString("ap_paterno"));
                cl.setAp_materno(rs.getString("ap_materno"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setCorreo(rs.getString("correo"));
                cl.setDireccion(rs.getString("direccion"));
                ListaCl.add(cl);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaCl;
    }
    
    public boolean EliminarCliente(int id){
        String sql = "DELETE FROM cliente WHERE id = ?";
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
    
    public boolean ModificarCliente(Cliente cl){
        String sql = "UPDATE cliente SET nombre=?, ap_paterno=?, ap_materno=?, telefono=?, correo=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getAp_paterno());
            ps.setString(3, cl.getAp_materno());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getCorreo());
            ps.setInt(6, cl.getId());
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
