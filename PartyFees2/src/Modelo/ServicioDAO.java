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
public class ServicioDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarServicio(Servicio ser){
    String sql = "INSERT INTO servicio (nombre, descripcion, costo, precio) VALUES (?,?,?,?)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, ser.getNombre());
            ps.setString(2, ser.getDescripcion());
            ps.setDouble(3, ser.getCosto());
            ps.setDouble(4, ser.getPrecio());
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
    
    public boolean RegistrarServicios(Servicios serv){
    String sql = "INSERT INTO servicios_res (idreserva, idservicio) VALUES (?,?)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, serv.getIdreserva());
            ps.setInt(2, serv.getIdservicio());
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
    
    public List ListarServicio(){
        List<Servicio> ListaServicio = new ArrayList();
        String sql = "SELECT * FROM servicio";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Servicio ser = new Servicio();
                ser.setId(rs.getInt("id"));
                ser.setNombre(rs.getString("nombre"));
                ser.setDescripcion(rs.getString("descripcion"));
                ser.setCosto(rs.getDouble("costo"));
                ser.setPrecio(rs.getDouble("precio"));
                ListaServicio.add(ser);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaServicio;
    }
     
     public boolean ModificarServicio(Servicio ser){
        String sql = "UPDATE servicio SET nombre=?, descripcion=?, costo=?, precio=? WHERE id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, ser.getNombre());
            ps.setString(2, ser.getDescripcion());
            ps.setDouble(3, ser.getCosto());
            ps.setDouble(4, ser.getPrecio());
            ps.setInt(5, ser.getId());
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
    
    public boolean EliminarServicio(int id){
        String sql = "DELETE FROM servicio WHERE id = ?";
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
    
    public double ObtenerPrecioDeProducto(String nombreProducto) {
    String sql = "SELECT * FROM producto WHERE nombre = ?";
    double precio = 0.00;
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreProducto); // Configura el parámetro id_proveedor
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
    
    public int BuscarIdSer(String nombreServicio){
        int id = 0;
        String sql = "SELECT id FROM servicio WHERE nombre = ?";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreServicio);
            rs = ps.executeQuery();
            if (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public List ListarServicios(){
        List<Servicio> ListaServicios = new ArrayList();
        String sql = "SELECT * FROM serviciosinfo";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Servicio ser = new Servicio();
                ser.setIdreserva(rs.getInt("id"));
                ser.setNombre(rs.getString("servicio"));
                ser.setDescripcion(rs.getString("descripcion"));
                ser.setCosto(rs.getDouble("costo"));
                ser.setPrecio(rs.getDouble("precio"));
                ListaServicios.add(ser);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaServicios;
    }
}
