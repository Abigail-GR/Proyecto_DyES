/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dmesc
 */
public class Conexion {
    public String db="base_reserva";
    public String url="jdbc:mysql://127.0.0.1/"+db;
    public String user="root";
    public String pass="";
    
    public Conexion(){
        
    }
    
    public Connection Conectar(){
        Connection link=null;
    
        try{
            Class.forName("org.gjt.mm.mysql.Driver");
            link=DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showConfirmDialog(null, e);
        }
        
    return link;
    }
}
