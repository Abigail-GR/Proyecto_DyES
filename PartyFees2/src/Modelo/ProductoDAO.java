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
public class ProductoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarProducto(Producto pro){
    String sql = "INSERT INTO producto (nombre, descripcion, unidad_medida, costo, precio) VALUES (?,?,?,?,?)";
    try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getNombre());
            ps.setString(2, pro.getDescripcion());
            ps.setString(3, pro.getUnidad_medida());
            ps.setDouble(4, pro.getCosto());
            ps.setDouble(5, pro.getPrecio());
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
    
    public List ListarProducto(){
        List<Producto> ListaPro = new ArrayList();
        String sql = "SELECT * FROM producto";
        try {
            con = cn.Conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto pro = new Producto();
                pro.setId(rs.getInt("id"));
                pro.setNombre(rs.getString("nombre"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setUnidad_medida(rs.getString("unidad_medida"));
                pro.setCosto(rs.getDouble("costo"));
                pro.setPrecio(rs.getDouble("precio"));
                ListaPro.add(pro);       
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        return ListaPro;
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
    
    public boolean EliminarProducto(int id){
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
}
