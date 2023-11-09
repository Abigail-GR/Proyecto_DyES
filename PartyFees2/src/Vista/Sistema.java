/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Combo;
import Modelo.ComboServicio;
import Modelo.Conexion;
import Modelo.Consumo;
import Modelo.ConsumoDAO;
import Modelo.Evento;
import Modelo.EventoDAO;
import java.awt.CardLayout;
import Modelo.Producto;
import Modelo.ProductoDAO;
import Modelo.Reserva;
import Modelo.ReservaDAO;
import Modelo.Servicio;
import Modelo.ServicioDAO;
import Modelo.Servicios;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractSpinnerModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author dmesc
 */
public class Sistema extends javax.swing.JFrame {
    
    int item;
    Consumo con = new Consumo();
    ConsumoDAO consumo = new ConsumoDAO();
    Reserva res = new Reserva();
    ReservaDAO reserva = new ReservaDAO();
    Cliente cl = new Cliente();
    ClienteDAO cliente = new ClienteDAO();
    Evento ev = new Evento();
    EventoDAO evento = new EventoDAO();
    Producto pro = new Producto();
    ProductoDAO producto = new ProductoDAO();
    Servicio ser = new Servicio();
    Servicios serv = new Servicios();
    ServicioDAO servicio = new ServicioDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel tmp = new DefaultTableModel();
    /**
     * Creates new form Sistema
     */
    CardLayout cardlayout;
    public Sistema() {
        initComponents();
        txtServicioID.setVisible(false);
        txtServicioID.setEditable(false);
        txtCantidadConsumo.setEditable(false);
        txtPID.setEditable(false);
        txtPID.show(false);
        cardlayout=(CardLayout) jPanel2.getLayout();
        ((AbstractDocument) txtHInicio.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        ((AbstractDocument) txtMinInicio.getDocument()).setDocumentFilter(new NumberDocumentFilter());
    }
    
    private int horasExtraAnterior = 0; // Variable para rastrear el valor anterior de txtHorasExtra
    
    public void LlenarProductos() {
        List<Producto> lista = producto.ListarProducto();

        // Limpia el JComboBox antes de volver a llenarlo
        comboProductos.removeAllItems();

        // Agrega un elemento especial para "Seleccione los productos..."
        comboProductos.addItem(new Combo(-1, "Seleccione los productos..."));

        for (int i = 0; i < lista.size(); i++) {
            int id = lista.get(i).getId();
            String nombre = lista.get(i).getNombre();
            comboProductos.addItem(new Combo(id, nombre));
        }
    }
    
    public void LlenarServicios() {
        List<Servicio> listaServicios = servicio.ListarServicio();
        
        // Limpia el JComboBox antes de volver a llenarlo
        comboServicios.removeAllItems();

        // Agrega un elemento especial para "Seleccione los productos..."
        comboServicios.addItem(new Servicio(-1, "Seleccione los servicios...", "-", 0, 0));
        
        // Agrega los servicios al comboServicios
        for (Servicio servicio : listaServicios) {
            comboServicios.addItem(servicio); // Usará el nombre del servicio
        }
    }

   
    public void LimpiarTabla(){
        for (int i = 0; i < modelo.getRowCount(); i++){
            modelo.removeRow(i);
            i = i-1;
        }
    }
    
    public void LimpiarTablas(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Elimina todas las filas
    }
    
    private void LimpiarProducto(){
        txtNProducto.setText("");
        txtPDescripcion.setText("");
        txtUnidadM.setText("");
        txtPCosto.setText("");
        txtPPrecio.setText("");
    }
    
    private void LimpiarConsumo(){
        txtCantidadConsumo.setText("");
        lblPrecioUConsumo.setText("---");
        comboProductos.setSelectedItem(comboProductos.getItemAt(0));
        lblSubtotalConsumo.setText("---");
    }
    
    private void LimpiarServicios(){
        comboServicios.setSelectedItem(comboServicios.getItemAt(0));
        lblSubtotalConsumo.setText("---");
    }
    
    private void LimpiarCliente(){
        txtNombreCliente.setText("");
        txtApellidoPCliente.setText("");
        txtApellidoMCliente.setText("");
        txtTelefonoCliente.setText("");
        txtCorreoCliente.setText("");
        txtDireccionCliente.setText("");
    }
    
    private void LimpiarEvento(){
        txtNombreEvento.setText("");
        txtDescripcionEvento.setText("");
        fechaEvento.setDate(null);
        txtHInicio.setText("");
        txtHFin.setText("");
        txtMinInicio.setText("");
        txtMinFin.setText("");
        txtHorasExtra.setText("");
        lblPrecioHoras.setText("0.00");
    }
    
    private void LimpiarServicio(){
        txtServicio.setText("");
        txtDescripcionServicio.setText("");
        txtCostoServicio.setText("");
        txtPrecioServicio.setText("");
    }
    
    private void LimpiarNuevaReserva(){
        LimpiarCliente();
        LimpiarEvento();
        LimpiarConsumo();
        LimpiarServicios();
    }
    
    private void TotalPagar(){
        double TotalP = 0.00;
        int numFila = tblConsumo.getRowCount();
        for (int i = 0; i < numFila; i++){
            double cal = Double.parseDouble(String.valueOf(tblConsumo.getModel().getValueAt(i, 3)));
            TotalP = TotalP + cal;
        }
        lblTotalConsumo.setText(String.format("%.2f", TotalP));
        if (TotalP == 0){
            lblTotalConsumo.setText("---");
        }
    }
    
    private void TotalServicios() {
    double total = 0.0;
    DefaultTableModel model = (DefaultTableModel) tblServicios.getModel();
    
    for (int row = 0; row < model.getRowCount(); row++) {
        double precio = (double) model.getValueAt(row, 3); // Obtén el precio de la columna 4
        total += precio; // Suma el precio al total
    }
    
    lblTotalServicio.setText(String.format("%.2f", total));
    if (total == 0.0) {
        lblTotalServicio.setText("---");
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_nreserva = new javax.swing.JButton();
        btn_reservas = new javax.swing.JButton();
        btn_clientes = new javax.swing.JButton();
        btn_staff = new javax.swing.JButton();
        btn_productos = new javax.swing.JButton();
        btn_gastos = new javax.swing.JButton();
        btn_pagos = new javax.swing.JButton();
        btn_servicios = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        ClienteEvento = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        PEvento = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreEvento = new javax.swing.JTextField();
        fechaEvento = new com.toedter.calendar.JDateChooser();
        txtHInicio = new javax.swing.JTextField();
        jScrollPane14 = new javax.swing.JScrollPane();
        txtDescripcionEvento = new javax.swing.JTextArea();
        txtMinInicio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtHFin = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtMinFin = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtHorasExtra = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        lblPrecioHoras = new javax.swing.JLabel();
        PCliente = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        txtApellidoPCliente = new javax.swing.JTextField();
        txtApellidoMCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtCorreoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        btnNReservaSig1 = new javax.swing.JButton();
        Consumo = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblSubtotalConsumo = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCantidadConsumo = new javax.swing.JTextField();
        lblPrecioUConsumo = new javax.swing.JLabel();
        btnAgregarConsumo = new javax.swing.JButton();
        btnEliminarConsumo = new javax.swing.JButton();
        comboProductos = new javax.swing.JComboBox<>();
        Titulo1 = new javax.swing.JLabel();
        btnNReservaSig2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConsumo = new javax.swing.JTable();
        btnNReservaAnt1 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        lblTotalConsumo = new javax.swing.JLabel();
        Servicios = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        btnAgregarServicios = new javax.swing.JButton();
        btnEliminarServicios = new javax.swing.JButton();
        comboServicios = new javax.swing.JComboBox<>();
        btnNReservaFinalizar = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblServicios = new javax.swing.JTable();
        Titulo9 = new javax.swing.JLabel();
        btnNReservaAnt2 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        lblTotalServicio = new javax.swing.JLabel();
        Reservas = new javax.swing.JPanel();
        Titulo8 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblReservas = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblServicios_Res = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblConsumoXReserva = new javax.swing.JTable();
        Clientes = new javax.swing.JPanel();
        Titulo4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        Pagos = new javax.swing.JPanel();
        Titulo5 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtUnidadM1 = new javax.swing.JTextField();
        txtPCosto1 = new javax.swing.JTextField();
        btn_PAgregar1 = new javax.swing.JButton();
        btn_PModificar1 = new javax.swing.JButton();
        btn_PEliminar1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblProductos1 = new javax.swing.JTable();
        Productos = new javax.swing.JPanel();
        Titulo2 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtNProducto = new javax.swing.JTextField();
        txtPDescripcion = new javax.swing.JTextField();
        txtUnidadM = new javax.swing.JTextField();
        txtPCosto = new javax.swing.JTextField();
        txtPPrecio = new javax.swing.JTextField();
        btnGuardarProducto = new javax.swing.JButton();
        btnModificarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        txtPID = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        Servicio = new javax.swing.JPanel();
        Titulo10 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        txtServicio = new javax.swing.JTextField();
        txtCostoServicio = new javax.swing.JTextField();
        txtPrecioServicio = new javax.swing.JTextField();
        btnGuardarServicio = new javax.swing.JButton();
        btnModificarServicio = new javax.swing.JButton();
        btnEliminarServicio = new javax.swing.JButton();
        txtServicioID = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtDescripcionServicio = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblServicio = new javax.swing.JTable();
        Staff = new javax.swing.JPanel();
        Titulo6 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblStaff = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtNombreStaff = new javax.swing.JTextField();
        txtApellidoPStaff = new javax.swing.JTextField();
        txtApellidoMStaff = new javax.swing.JTextField();
        txtPosicionStaff = new javax.swing.JTextField();
        txtSueldoStaff = new javax.swing.JTextField();
        txtTelefonoStaff = new javax.swing.JTextField();
        btn_AgregarStaff = new javax.swing.JButton();
        btn_ModificarStaff = new javax.swing.JButton();
        btn_EliminarStaff = new javax.swing.JButton();
        btn_EliminarStaff1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Gastos = new javax.swing.JPanel();
        Titulo7 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtGasto = new javax.swing.JTextField();
        txtDescripcionGasto = new javax.swing.JTextField();
        txtPCosto2 = new javax.swing.JTextField();
        btn_AgregarGasto = new javax.swing.JButton();
        btn_ModificarGasto = new javax.swing.JButton();
        btn_EliminarGasto = new javax.swing.JButton();
        txtGastoID = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblGastos = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Serviciosold = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtServicio1 = new javax.swing.JTextField();
        txtDescripcionServicio1 = new javax.swing.JTextField();
        txtCostoServicio1 = new javax.swing.JTextField();
        txtPrecioServicio1 = new javax.swing.JTextField();
        btnAgregarServicio2 = new javax.swing.JButton();
        btnEliminarServicio2 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblServicio2 = new javax.swing.JTable();
        Titulo3 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        lblTotalServicio2 = new javax.swing.JLabel();
        Logo = new javax.swing.JLabel();
        Fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_nreserva.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_nreserva.setText("Nueva Reserva");
        btn_nreserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nreservaActionPerformed(evt);
            }
        });
        getContentPane().add(btn_nreserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 160, 40));

        btn_reservas.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_reservas.setText("Reservas");
        btn_reservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reservasActionPerformed(evt);
            }
        });
        getContentPane().add(btn_reservas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 160, 40));

        btn_clientes.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_clientes.setText("Clientes");
        btn_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clientesActionPerformed(evt);
            }
        });
        getContentPane().add(btn_clientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 160, 40));

        btn_staff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_staff.setText("Staff");
        btn_staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_staffActionPerformed(evt);
            }
        });
        getContentPane().add(btn_staff, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 160, 40));

        btn_productos.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_productos.setText("Productos");
        btn_productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_productosActionPerformed(evt);
            }
        });
        getContentPane().add(btn_productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 160, 40));

        btn_gastos.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_gastos.setText("Gastos");
        btn_gastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gastosActionPerformed(evt);
            }
        });
        getContentPane().add(btn_gastos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 160, 40));

        btn_pagos.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_pagos.setText("Pagos");
        btn_pagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pagosActionPerformed(evt);
            }
        });
        getContentPane().add(btn_pagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 160, 40));

        btn_servicios.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btn_servicios.setText("Servicios");
        btn_servicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_serviciosActionPerformed(evt);
            }
        });
        getContentPane().add(btn_servicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 160, 40));

        jPanel2.setLayout(new java.awt.CardLayout());

        Titulo.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo.setText("Nueva Reserva");

        PEvento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Evento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Franklin Gothic Book", 0, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel1.setText("Nombre:");

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel2.setText("Descripción:");

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel3.setText("Fecha:");

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel5.setText("Hora de inicio:");

        jLabel6.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel6.setText("Hora de fin:");

        txtNombreEvento.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtNombreEvento.setPreferredSize(new java.awt.Dimension(200, 20));
        txtNombreEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEventoActionPerformed(evt);
            }
        });

        txtHInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHInicioActionPerformed(evt);
            }
        });
        txtHInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHInicioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHInicioKeyTyped(evt);
            }
        });

        txtDescripcionEvento.setColumns(20);
        txtDescripcionEvento.setLineWrap(true);
        txtDescripcionEvento.setRows(5);
        txtDescripcionEvento.setWrapStyleWord(true);
        jScrollPane14.setViewportView(txtDescripcionEvento);

        txtMinInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMinInicioActionPerformed(evt);
            }
        });
        txtMinInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMinInicioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMinInicioKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText(":");

        txtHFin.setEditable(false);
        txtHFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHFinActionPerformed(evt);
            }
        });
        txtHFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHFinKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText(":");

        txtMinFin.setEditable(false);
        txtMinFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMinFinActionPerformed(evt);
            }
        });
        txtMinFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMinFinKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel12.setText("Horas extras:");

        txtHorasExtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHorasExtraActionPerformed(evt);
            }
        });
        txtHorasExtra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHorasExtraKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHorasExtraKeyTyped(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel29.setText("Precio:");

        lblPrecioHoras.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        lblPrecioHoras.setText("0.00");

        javax.swing.GroupLayout PEventoLayout = new javax.swing.GroupLayout(PEvento);
        PEvento.setLayout(PEventoLayout);
        PEventoLayout.setHorizontalGroup(
            PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PEventoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14)
                    .addComponent(txtNombreEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PEventoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PEventoLayout.createSequentialGroup()
                                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PEventoLayout.createSequentialGroup()
                                        .addComponent(txtHInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMinInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PEventoLayout.createSequentialGroup()
                                        .addComponent(txtHFin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMinFin, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(53, 53, 53))
                            .addGroup(PEventoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHorasExtra, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(lblPrecioHoras))
                    .addGroup(PEventoLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PEventoLayout.setVerticalGroup(
            PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PEventoLayout.createSequentialGroup()
                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaEvento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtNombreEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PEventoLayout.createSequentialGroup()
                        .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(PEventoLayout.createSequentialGroup()
                                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtHInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMinInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtHFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMinFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtHorasExtra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(lblPrecioHoras))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PEventoLayout.createSequentialGroup()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        PCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Franklin Gothic Book", 0, 18))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel13.setText("Nombre(s):");

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel14.setText("Apellido paterno:");

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel15.setText("Apellido materno:");

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel16.setText("Teléfono:");

        jLabel17.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel17.setText("Correo:");

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel18.setText("Dirección:");

        txtNombreCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtNombreCliente.setPreferredSize(new java.awt.Dimension(200, 20));
        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });

        txtApellidoPCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtApellidoPCliente.setPreferredSize(new java.awt.Dimension(200, 20));

        txtApellidoMCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtApellidoMCliente.setPreferredSize(new java.awt.Dimension(200, 20));

        txtTelefonoCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtTelefonoCliente.setPreferredSize(new java.awt.Dimension(200, 20));

        txtCorreoCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtCorreoCliente.setPreferredSize(new java.awt.Dimension(200, 20));

        txtDireccionCliente.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtDireccionCliente.setPreferredSize(new java.awt.Dimension(200, 20));
        txtDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PClienteLayout = new javax.swing.GroupLayout(PCliente);
        PCliente.setLayout(PClienteLayout);
        PClienteLayout.setHorizontalGroup(
            PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtApellidoPCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtApellidoMCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCorreoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PClienteLayout.setVerticalGroup(
            PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PClienteLayout.createSequentialGroup()
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel17)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorreoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PClienteLayout.createSequentialGroup()
                        .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel18)
                            .addComponent(txtApellidoPCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(txtApellidoMCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        btnNReservaSig1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btnNReservaSig1.setText("Siguiente");
        btnNReservaSig1.setActionCommand("");
        btnNReservaSig1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNReservaSig1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ClienteEventoLayout = new javax.swing.GroupLayout(ClienteEvento);
        ClienteEvento.setLayout(ClienteEventoLayout);
        ClienteEventoLayout.setHorizontalGroup(
            ClienteEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteEventoLayout.createSequentialGroup()
                .addGroup(ClienteEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ClienteEventoLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(ClienteEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Titulo)
                            .addComponent(PCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(ClienteEventoLayout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(btnNReservaSig1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ClienteEventoLayout.setVerticalGroup(
            ClienteEventoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClienteEventoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo)
                .addGap(18, 18, 18)
                .addComponent(PCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(PEvento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnNReservaSig1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        jPanel2.add(ClienteEvento, "c1");

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consumo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Franklin Gothic Book", 0, 18))); // NOI18N

        jLabel19.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel19.setText("Nombre del producto:");

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel20.setText("Precio unitario:");

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel21.setText("Subtotal:");

        lblSubtotalConsumo.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        lblSubtotalConsumo.setText("---");

        jLabel22.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel22.setText("Cantidad:");

        txtCantidadConsumo.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtCantidadConsumo.setPreferredSize(new java.awt.Dimension(200, 20));
        txtCantidadConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadConsumoActionPerformed(evt);
            }
        });
        txtCantidadConsumo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadConsumoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadConsumoKeyTyped(evt);
            }
        });

        lblPrecioUConsumo.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        lblPrecioUConsumo.setText("---");

        btnAgregarConsumo.setText("Agregar");
        btnAgregarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarConsumoActionPerformed(evt);
            }
        });

        btnEliminarConsumo.setText("Eliminar");
        btnEliminarConsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarConsumoActionPerformed(evt);
            }
        });

        comboProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboProductosActionPerformed(evt);
            }
        });
        comboProductos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboProductosKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(lblPrecioUConsumo)
                                .addGap(188, 188, 188))
                            .addComponent(comboProductos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidadConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(168, 168, 168)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btnAgregarConsumo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarConsumo))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSubtotalConsumo)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(comboProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(lblSubtotalConsumo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblPrecioUConsumo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtCantidadConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        Titulo1.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo1.setText("Nueva Reserva");

        btnNReservaSig2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btnNReservaSig2.setText("Siguiente");
        btnNReservaSig2.setActionCommand("");
        btnNReservaSig2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNReservaSig2ActionPerformed(evt);
            }
        });

        tblConsumo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio C/U", "Subtotal"
            }
        ));
        tblConsumo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConsumoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblConsumo);
        if (tblConsumo.getColumnModel().getColumnCount() > 0) {
            tblConsumo.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblConsumo.getColumnModel().getColumn(1).setPreferredWidth(20);
            tblConsumo.getColumnModel().getColumn(2).setPreferredWidth(30);
            tblConsumo.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        btnNReservaAnt1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btnNReservaAnt1.setText("Anterior");
        btnNReservaAnt1.setActionCommand("");
        btnNReservaAnt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNReservaAnt1ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setText("Total a pagar:");

        lblTotalConsumo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalConsumo.setText("---");

        javax.swing.GroupLayout ConsumoLayout = new javax.swing.GroupLayout(Consumo);
        Consumo.setLayout(ConsumoLayout);
        ConsumoLayout.setHorizontalGroup(
            ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConsumoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNReservaAnt1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnNReservaSig2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
            .addGroup(ConsumoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ConsumoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel32)
                        .addGap(43, 43, 43)
                        .addComponent(lblTotalConsumo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ConsumoLayout.createSequentialGroup()
                        .addGroup(ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ConsumoLayout.createSequentialGroup()
                                .addComponent(Titulo1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addGap(22, 22, 22))))
        );
        ConsumoLayout.setVerticalGroup(
            ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsumoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo1)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(lblTotalConsumo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ConsumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNReservaSig2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNReservaAnt1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.add(Consumo, "c2");

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Servicios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Franklin Gothic Book", 0, 18))); // NOI18N

        jLabel52.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel52.setText("Nombre del servicio:");

        btnAgregarServicios.setText("Agregar");
        btnAgregarServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarServiciosActionPerformed(evt);
            }
        });

        btnEliminarServicios.setText("Eliminar");
        btnEliminarServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarServiciosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboServicios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(243, 243, 243)
                        .addComponent(btnAgregarServicios)
                        .addGap(86, 86, 86)
                        .addComponent(btnEliminarServicios)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(comboServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminarServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnNReservaFinalizar.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btnNReservaFinalizar.setText("Finalizar");
        btnNReservaFinalizar.setActionCommand("");
        btnNReservaFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNReservaFinalizarActionPerformed(evt);
            }
        });

        tblServicios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Servicio", "Descripcion", "Costo", "Precio"
            }
        ));
        tblServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblServiciosMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblServicios);
        if (tblServicios.getColumnModel().getColumnCount() > 0) {
            tblServicios.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblServicios.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblServicios.getColumnModel().getColumn(2).setPreferredWidth(20);
            tblServicios.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        Titulo9.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo9.setText("Nueva Reserva");

        btnNReservaAnt2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        btnNReservaAnt2.setText("Anterior");
        btnNReservaAnt2.setActionCommand("");
        btnNReservaAnt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNReservaAnt2ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Total a pagar:");

        lblTotalServicio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalServicio.setText("---");

        javax.swing.GroupLayout ServiciosLayout = new javax.swing.GroupLayout(Servicios);
        Servicios.setLayout(ServiciosLayout);
        ServiciosLayout.setHorizontalGroup(
            ServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServiciosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ServiciosLayout.createSequentialGroup()
                        .addComponent(Titulo9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane11)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServiciosLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(btnNReservaAnt2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(btnNReservaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
            .addGroup(ServiciosLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel34)
                .addGap(43, 43, 43)
                .addComponent(lblTotalServicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ServiciosLayout.setVerticalGroup(
            ServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServiciosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(lblTotalServicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ServiciosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNReservaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNReservaAnt2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(Servicios, "c3");

        Titulo8.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo8.setText("Reservas");

        tblReservas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Cliente", "Evento", "Descripcion", "Fecha", "Precio", "Hora inicio", "Hora final", "Estado"
            }
        ));
        tblReservas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblReservas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReservasMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblReservas);
        if (tblReservas.getColumnModel().getColumnCount() > 0) {
            tblReservas.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblReservas.getColumnModel().getColumn(1).setPreferredWidth(160);
            tblReservas.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblReservas.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblReservas.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblReservas.getColumnModel().getColumn(5).setPreferredWidth(80);
            tblReservas.getColumnModel().getColumn(8).setPreferredWidth(70);
        }

        tblServicios_Res.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Reserva", "Servicio", "Descripcion", "Costo", "Precio"
            }
        ));
        tblServicios_Res.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblServicios_Res.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblServicios_ResMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(tblServicios_Res);
        if (tblServicios_Res.getColumnModel().getColumnCount() > 0) {
            tblServicios_Res.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblServicios_Res.getColumnModel().getColumn(0).setMaxWidth(60);
            tblServicios_Res.getColumnModel().getColumn(1).setPreferredWidth(50);
            tblServicios_Res.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblServicios_Res.getColumnModel().getColumn(3).setMaxWidth(80);
            tblServicios_Res.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblServicios_Res.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        tblConsumoXReserva.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Bolsa de Papitas", "5", "1", "2", "10"},
                {"1", "Mini Hot Dogs", "5", "1.5", "3", "15"}
            },
            new String [] {
                "ID", "Producto", "Cantidad", "Costo", "Precio", "Subtotal"
            }
        ));
        tblConsumoXReserva.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblConsumoXReserva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConsumoXReservaMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tblConsumoXReserva);
        if (tblConsumoXReserva.getColumnModel().getColumnCount() > 0) {
            tblConsumoXReserva.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblConsumoXReserva.getColumnModel().getColumn(1).setPreferredWidth(160);
            tblConsumoXReserva.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblConsumoXReserva.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblConsumoXReserva.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblConsumoXReserva.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        javax.swing.GroupLayout ReservasLayout = new javax.swing.GroupLayout(Reservas);
        Reservas.setLayout(ReservasLayout);
        ReservasLayout.setHorizontalGroup(
            ReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReservasLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Titulo8)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        ReservasLayout.setVerticalGroup(
            ReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReservasLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel2.add(Reservas, "c4");

        Titulo4.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo4.setText("Clientes");

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Fernando", "Gomez", "Morin", "8112345678", "fergom@gmail.com", "Av Leones 123"}
            },
            new String [] {
                "ID", "Nombre(s)", "Ap. Paterno", "Ap. Materno", "Teléfono", "Correo", "Dirección"
            }
        ));
        tblClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblClientes.getColumnModel().getColumn(1).setPreferredWidth(160);
            tblClientes.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblClientes.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblClientes.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblClientes.getColumnModel().getColumn(5).setPreferredWidth(70);
            tblClientes.getColumnModel().getColumn(6).setPreferredWidth(150);
        }

        javax.swing.GroupLayout ClientesLayout = new javax.swing.GroupLayout(Clientes);
        Clientes.setLayout(ClientesLayout);
        ClientesLayout.setHorizontalGroup(
            ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClientesLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Titulo4)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        ClientesLayout.setVerticalGroup(
            ClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClientesLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel2.add(Clientes, "c5");

        Titulo5.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo5.setText("Pagos");

        jLabel42.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel42.setText("Reserva:");

        jLabel43.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel43.setText("Tipo de pago:");

        jLabel44.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel44.setText("Cantidad pagada:");

        jLabel45.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel45.setText("Total a pagar:");

        txtUnidadM1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtUnidadM1.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPCosto1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPCosto1.setPreferredSize(new java.awt.Dimension(200, 20));

        btn_PAgregar1.setText("Agregar");
        btn_PAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PAgregar1ActionPerformed(evt);
            }
        });

        btn_PModificar1.setText("Modificar");
        btn_PModificar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PModificar1ActionPerformed(evt);
            }
        });

        btn_PEliminar1.setText("Eliminar");
        btn_PEliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PEliminar1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione la reserva..." }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione el tipo de pago..." }));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_PAgregar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_PModificar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_PEliminar1)
                .addGap(185, 185, 185))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel44))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel45)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPCosto1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnidadM1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtUnidadM1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel45)
                                    .addComponent(txtPCosto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel43))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_PAgregar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_PModificar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_PEliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblProductos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Reserva", "Tipo de pago", "Cantidad pagada", "Total a pagar", "Fecha"
            }
        ));
        tblProductos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductos1MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblProductos1);
        if (tblProductos1.getColumnModel().getColumnCount() > 0) {
            tblProductos1.getColumnModel().getColumn(0).setMaxWidth(30);
            tblProductos1.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductos1.getColumnModel().getColumn(2).setPreferredWidth(70);
            tblProductos1.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblProductos1.getColumnModel().getColumn(4).setPreferredWidth(20);
            tblProductos1.getColumnModel().getColumn(5).setPreferredWidth(20);
        }

        javax.swing.GroupLayout PagosLayout = new javax.swing.GroupLayout(Pagos);
        Pagos.setLayout(PagosLayout);
        PagosLayout.setHorizontalGroup(
            PagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PagosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(PagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Titulo5)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        PagosLayout.setVerticalGroup(
            PagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PagosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo5)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel2.add(Pagos, "c6");

        Titulo2.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo2.setText("Productos");

        jLabel37.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel37.setText("Nombre del producto:");

        jLabel38.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel38.setText("Descripcion:");

        jLabel39.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel39.setText("Unidad de medida:");

        jLabel40.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel40.setText("Costo:");

        jLabel41.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel41.setText("Precio:");

        txtNProducto.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtNProducto.setPreferredSize(new java.awt.Dimension(200, 20));
        txtNProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        txtPDescripcion.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPDescripcion.setPreferredSize(new java.awt.Dimension(200, 20));

        txtUnidadM.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtUnidadM.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPCosto.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPCosto.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPPrecio.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPPrecio.setPreferredSize(new java.awt.Dimension(200, 20));

        btnGuardarProducto.setText("Guardar");
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnModificarProducto.setText("Modificar");
        btnModificarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(txtPID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPDescripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                            .addComponent(txtNProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40)
                            .addComponent(jLabel39)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUnidadM, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(btnGuardarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarProducto)
                        .addGap(185, 185, 185))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(txtUnidadM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(txtPCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addComponent(txtPPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(txtNProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(txtPID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Descripcion", "Unidad de medida", "Costo", "Precio"
            }
        ));
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProductos);
        if (tblProductos.getColumnModel().getColumnCount() > 0) {
            tblProductos.getColumnModel().getColumn(0).setMaxWidth(30);
            tblProductos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblProductos.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblProductos.getColumnModel().getColumn(3).setPreferredWidth(20);
            tblProductos.getColumnModel().getColumn(4).setPreferredWidth(20);
            tblProductos.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblProductos.getColumnModel().getColumn(5).setHeaderValue("Precio");
        }

        javax.swing.GroupLayout ProductosLayout = new javax.swing.GroupLayout(Productos);
        Productos.setLayout(ProductosLayout);
        ProductosLayout.setHorizontalGroup(
            ProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProductosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Titulo2)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        ProductosLayout.setVerticalGroup(
            ProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProductosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo2)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.add(Productos, "c7");

        Titulo10.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo10.setText("Servicios");

        jLabel54.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel54.setText("Nombre del servicio:");

        jLabel55.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel55.setText("Descripción:");

        jLabel57.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel57.setText("Costo:");

        jLabel58.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel58.setText("Precio:");

        txtServicio.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtServicio.setPreferredSize(new java.awt.Dimension(200, 20));
        txtServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtServiciojTextField19ActionPerformed(evt);
            }
        });

        txtCostoServicio.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtCostoServicio.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPrecioServicio.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPrecioServicio.setPreferredSize(new java.awt.Dimension(200, 20));

        btnGuardarServicio.setText("Guardar");
        btnGuardarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarServicioActionPerformed(evt);
            }
        });

        btnModificarServicio.setText("Modificar");
        btnModificarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarServicioActionPerformed(evt);
            }
        });

        btnEliminarServicio.setText("Eliminar");
        btnEliminarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarServicioActionPerformed(evt);
            }
        });

        txtDescripcionServicio.setColumns(20);
        txtDescripcionServicio.setLineWrap(true);
        txtDescripcionServicio.setRows(5);
        txtDescripcionServicio.setWrapStyleWord(true);
        jScrollPane13.setViewportView(txtDescripcionServicio);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarServicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificarServicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminarServicio)
                .addGap(185, 185, 185))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel54)
                        .addComponent(jLabel55))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(txtServicioID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtServicio, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(jScrollPane13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCostoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(txtPrecioServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(txtServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addGap(26, 26, 26)
                                .addComponent(txtServicioID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel57)
                            .addComponent(txtCostoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58)
                            .addComponent(txtPrecioServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblServicio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Servicio", "Descripcion", "Costo", "Precio"
            }
        ));
        tblServicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblServicioMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblServicio);
        if (tblServicio.getColumnModel().getColumnCount() > 0) {
            tblServicio.getColumnModel().getColumn(0).setMaxWidth(30);
            tblServicio.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblServicio.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblServicio.getColumnModel().getColumn(3).setPreferredWidth(20);
            tblServicio.getColumnModel().getColumn(4).setPreferredWidth(20);
        }

        javax.swing.GroupLayout ServicioLayout = new javax.swing.GroupLayout(Servicio);
        Servicio.setLayout(ServicioLayout);
        ServicioLayout.setHorizontalGroup(
            ServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServicioLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Titulo10)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        ServicioLayout.setVerticalGroup(
            ServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServicioLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo10)
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.add(Servicio, "c8");

        Titulo6.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo6.setText("Staff");

        tblStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Daniel Eduardo", "Mesias", "Cortina", "Gerente", "4000", "8129342940"},
                {"2", "Ángel Adolfo", "Casas", "Valero", "Coordinador", "2000", "8158203740"}
            },
            new String [] {
                "ID", "Nombre(s)", "Ap. paterno", "Ap. materno", "Posición", "Sueldo", "Teléfono"
            }
        ));
        tblStaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStaffMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblStaff);
        if (tblStaff.getColumnModel().getColumnCount() > 0) {
            tblStaff.getColumnModel().getColumn(0).setMaxWidth(30);
            tblStaff.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblStaff.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblStaff.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblStaff.getColumnModel().getColumn(4).setPreferredWidth(50);
            tblStaff.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblStaff.getColumnModel().getColumn(6).setPreferredWidth(60);
        }

        jLabel23.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel23.setText("Nombre(s):");

        jLabel24.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel24.setText("Apellido paterno:");

        jLabel25.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel25.setText("Apellido materno:");

        jLabel26.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel26.setText("Posición:");

        jLabel27.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel27.setText("Sueldo:");

        jLabel28.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel28.setText("Teléfono:");

        txtNombreStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtNombreStaff.setPreferredSize(new java.awt.Dimension(200, 20));
        txtNombreStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreStaffActionPerformed(evt);
            }
        });

        txtApellidoPStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtApellidoPStaff.setPreferredSize(new java.awt.Dimension(200, 20));

        txtApellidoMStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtApellidoMStaff.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPosicionStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPosicionStaff.setPreferredSize(new java.awt.Dimension(200, 20));

        txtSueldoStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtSueldoStaff.setPreferredSize(new java.awt.Dimension(200, 20));

        txtTelefonoStaff.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtTelefonoStaff.setPreferredSize(new java.awt.Dimension(200, 20));
        txtTelefonoStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoStaffActionPerformed(evt);
            }
        });

        btn_AgregarStaff.setText("Agregar");
        btn_AgregarStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarStaffActionPerformed(evt);
            }
        });

        btn_ModificarStaff.setText("Modificar");
        btn_ModificarStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarStaffActionPerformed(evt);
            }
        });

        btn_EliminarStaff.setText("Eliminar");
        btn_EliminarStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarStaffActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtApellidoPStaff, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtApellidoMStaff, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombreStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTelefonoStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSueldoStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPosicionStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_AgregarStaff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ModificarStaff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_EliminarStaff)
                .addGap(175, 175, 175))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtNombreStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtPosicionStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtApellidoPStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtApellidoMStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtSueldoStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28)
                            .addComponent(txtTelefonoStaff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_AgregarStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ModificarStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_EliminarStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        btn_EliminarStaff1.setText("Agregar a gastos fijos");
        btn_EliminarStaff1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarStaff1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Total sueldos:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("6,000.00");

        javax.swing.GroupLayout StaffLayout = new javax.swing.GroupLayout(Staff);
        Staff.setLayout(StaffLayout);
        StaffLayout.setHorizontalGroup(
            StaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StaffLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(StaffLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(StaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(StaffLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addGap(53, 53, 53)
                        .addComponent(btn_EliminarStaff1))
                    .addGroup(StaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Titulo6)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        StaffLayout.setVerticalGroup(
            StaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StaffLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(StaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(btn_EliminarStaff1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2.add(Staff, "c9");

        Titulo7.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo7.setText("Gastos");

        jLabel46.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel46.setText("Nombre del gasto:");

        jLabel47.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel47.setText("Descripcion:");

        jLabel53.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel53.setText("Costo:");

        txtGasto.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtGasto.setPreferredSize(new java.awt.Dimension(200, 20));
        txtGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGastojTextField19ActionPerformed(evt);
            }
        });

        txtDescripcionGasto.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtDescripcionGasto.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPCosto2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPCosto2.setPreferredSize(new java.awt.Dimension(200, 20));

        btn_AgregarGasto.setText("Agregar");
        btn_AgregarGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarGastoActionPerformed(evt);
            }
        });

        btn_ModificarGasto.setText("Modificar");
        btn_ModificarGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarGastoActionPerformed(evt);
            }
        });

        btn_EliminarGasto.setText("Eliminar");
        btn_EliminarGasto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarGastoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(171, Short.MAX_VALUE)
                .addComponent(btn_AgregarGasto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ModificarGasto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_EliminarGasto)
                .addGap(185, 185, 185))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel46)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDescripcionGasto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtGasto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGastoID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(txtGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(txtPCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDescripcionGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47)
                    .addComponent(txtGastoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_AgregarGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ModificarGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_EliminarGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "Renta", "Renta del establecimiento de salón de fiestas por el tiempo de 4h", "8000"},
                {"2", "Productos de limpieza", "Productos utilizados para la limpieza del lugar antes y durante la reservación", "1200"},
                {"3", "Luz", "Costo de recibo promedio de luz", "600"},
                {"4", "Agua", "Costo de recibo promedio de agua", "400"},
                {"5", "Gas", "Costo de recibo promedio de gas", "400"},
                {"6", "Sueldos", "", "6000"}
            },
            new String [] {
                "ID", "Gasto", "Descripcion", "Costo"
            }
        ));
        tblGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGastosMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblGastos);
        if (tblGastos.getColumnModel().getColumnCount() > 0) {
            tblGastos.getColumnModel().getColumn(0).setMaxWidth(30);
            tblGastos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblGastos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblGastos.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Total gastos fijos:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("16,600.00");

        javax.swing.GroupLayout GastosLayout = new javax.swing.GroupLayout(Gastos);
        Gastos.setLayout(GastosLayout);
        GastosLayout.setHorizontalGroup(
            GastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GastosLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(GastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Titulo7)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GastosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(29, 29, 29))
        );
        GastosLayout.setVerticalGroup(
            GastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, GastosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(GastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.add(Gastos, "c10");

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Servicios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Franklin Gothic Book", 0, 18))); // NOI18N

        jLabel48.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel48.setText("Nombre del servicio:");

        jLabel49.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel49.setText("Descripcion:");

        jLabel50.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel50.setText("Costo:");

        jLabel51.setFont(new java.awt.Font("Franklin Gothic Book", 0, 14)); // NOI18N
        jLabel51.setText("Precio:");

        txtServicio1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtServicio1.setPreferredSize(new java.awt.Dimension(200, 20));
        txtServicio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtServicio1ActionPerformed(evt);
            }
        });

        txtDescripcionServicio1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtDescripcionServicio1.setPreferredSize(new java.awt.Dimension(200, 20));

        txtCostoServicio1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtCostoServicio1.setPreferredSize(new java.awt.Dimension(200, 20));

        txtPrecioServicio1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        txtPrecioServicio1.setPreferredSize(new java.awt.Dimension(200, 20));

        btnAgregarServicio2.setText("Agregar");
        btnAgregarServicio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarServicio2ActionPerformed(evt);
            }
        });

        btnEliminarServicio2.setText("Eliminar");
        btnEliminarServicio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarServicio2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel49)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDescripcionServicio1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                    .addComponent(txtServicio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel51)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCostoServicio1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(txtPrecioServicio1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                        .addGap(31, 31, 31))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEliminarServicio2)
                            .addComponent(btnAgregarServicio2))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txtServicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(txtDescripcionServicio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(txtCostoServicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(txtPrecioServicio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarServicio2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminarServicio2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jButton14.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jButton14.setText("Finalizar");
        jButton14.setActionCommand("");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        tblServicio2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Servicio", "Descripcion", "Costo", "Precio"
            }
        ));
        jScrollPane3.setViewportView(tblServicio2);
        if (tblServicio2.getColumnModel().getColumnCount() > 0) {
            tblServicio2.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblServicio2.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblServicio2.getColumnModel().getColumn(2).setPreferredWidth(20);
            tblServicio2.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        Titulo3.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 24)); // NOI18N
        Titulo3.setText("Nueva Reserva");

        jButton15.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jButton15.setText("Anterior");
        jButton15.setActionCommand("");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setText("Total a pagar:");

        lblTotalServicio2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalServicio2.setText("---");

        javax.swing.GroupLayout ServiciosoldLayout = new javax.swing.GroupLayout(Serviciosold);
        Serviciosold.setLayout(ServiciosoldLayout);
        ServiciosoldLayout.setHorizontalGroup(
            ServiciosoldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServiciosoldLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(ServiciosoldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ServiciosoldLayout.createSequentialGroup()
                        .addComponent(Titulo3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServiciosoldLayout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130))
            .addGroup(ServiciosoldLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel33)
                .addGap(43, 43, 43)
                .addComponent(lblTotalServicio2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ServiciosoldLayout.setVerticalGroup(
            ServiciosoldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServiciosoldLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Titulo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ServiciosoldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(lblTotalServicio2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ServiciosoldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.add(Serviciosold, "");

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 660, 500));

        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Logo PartyFees (1).png"))); // NOI18N
        Logo.setToolTipText("");
        getContentPane().add(Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -10, -1, -1));

        Fondo.setFont(new java.awt.Font("Franklin Gothic Demi", 0, 12)); // NOI18N
        Fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Fondo PartyFees.png"))); // NOI18N
        Fondo.setToolTipText("");
        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEventoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEventoActionPerformed

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void txtDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteActionPerformed

    private void btn_reservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reservasActionPerformed
        ListarReservas();
        ListarServicios();
        cardlayout.show(jPanel2, "c4");
    }//GEN-LAST:event_btn_reservasActionPerformed

    private void btn_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clientesActionPerformed
        cardlayout.show(jPanel2, "c5");
    }//GEN-LAST:event_btn_clientesActionPerformed

    private void btn_staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_staffActionPerformed
        cardlayout.show(jPanel2, "c9");
    }//GEN-LAST:event_btn_staffActionPerformed

    private void btn_gastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gastosActionPerformed
        cardlayout.show(jPanel2, "c10");
    }//GEN-LAST:event_btn_gastosActionPerformed

    private void btn_nreservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nreservaActionPerformed
        cardlayout.show(jPanel2, "c1");
    }//GEN-LAST:event_btn_nreservaActionPerformed

    private void btnNReservaSig1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNReservaSig1ActionPerformed
        if (validarCamposCliente() & validarCamposEvento()) {
            cardlayout.show(jPanel2, "c2");
            LlenarProductos();
        }
    }//GEN-LAST:event_btnNReservaSig1ActionPerformed

    private void btn_productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_productosActionPerformed
        cardlayout.show(jPanel2, "c7");
        LimpiarTabla();
        LimpiarProducto();
    }//GEN-LAST:event_btn_productosActionPerformed

    private void btnEliminarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarConsumoActionPerformed
        int selectedRow = tblConsumo.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tblConsumo.getModel();

            // Elimina la fila seleccionada del modelo de la tabla
            model.removeRow(selectedRow);

            // Actualiza el modelo de la tabla
            tblConsumo.setModel(model);
        }
        LimpiarConsumo();
        TotalPagar();
        // Deselecciona la fila
        tblConsumo.clearSelection();
    }//GEN-LAST:event_btnEliminarConsumoActionPerformed

    private void btnNReservaSig2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNReservaSig2ActionPerformed
        if (tblConsumo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Falta seleccionar productos de consumo");
        } else {
            cardlayout.show(jPanel2, "c3");
            tblConsumo.clearSelection();
            LlenarServicios();
        }
    }//GEN-LAST:event_btnNReservaSig2ActionPerformed

    private void btnNReservaAnt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNReservaAnt1ActionPerformed
        cardlayout.show(jPanel2, "c1");
        // Deselecciona la fila
        tblConsumo.clearSelection();
    }//GEN-LAST:event_btnNReservaAnt1ActionPerformed

    private void btnModificarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarProductoActionPerformed
        if ("".equals(txtPID.getText())){
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else{
            pro.setId(Integer.parseInt(txtPID.getText()));
            pro.setNombre(txtNProducto.getText());
            pro.setDescripcion(txtPDescripcion.getText());
            pro.setUnidad_medida(txtUnidadM.getText());
            pro.setCosto(Double.parseDouble(txtPCosto.getText()));
            pro.setPrecio(Double.parseDouble(txtPPrecio.getText()));
            if(!"".equals(txtPID.getText()) || !"".equals(txtNProducto.getText()) || !"".equals(txtPDescripcion.getText()) || !"".equals(txtUnidadM.getText()) || !"".equals(txtPCosto.getText()) || !"".equals(txtPPrecio.getText())){
                producto.ModificarProducto(pro);
                LimpiarTabla();
                LimpiarProducto();
                ListarProducto();
            }
        }
    }//GEN-LAST:event_btnModificarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        if ("".equals(txtPID.getText())){
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else{
            if (!"".equals(txtPID.getText())){
                int pregunta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?");
                if (pregunta == 0){
                    int id = Integer.parseInt(txtPID.getText());
                    producto.EliminarProducto(id);
                    LimpiarTabla();
                    LimpiarProducto();
                    ListarProducto();
                }
            }
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        if (!"".equals(txtNProducto.getText()) || !"".equals(txtPDescripcion.getText()) || !"".equals(txtUnidadM.getText()) || !"".equals(txtPCosto.getText()) || !"".equals(txtPPrecio.getText())){
            pro.setNombre(txtNProducto.getText());
            pro.setDescripcion(txtPDescripcion.getText());
            pro.setUnidad_medida(txtUnidadM.getText());
            pro.setCosto(Double.parseDouble(txtPCosto.getText()));
            pro.setPrecio(Double.parseDouble(txtPPrecio.getText()));
            producto.RegistrarProducto(pro);
            LimpiarTabla();
            LimpiarProducto();
            ListarProducto();
            JOptionPane.showMessageDialog(null, "Producto registrado");
        }else{
            JOptionPane.showMessageDialog(null, "Los campos estan vacios");
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosMouseClicked
        int fila = tblProductos.rowAtPoint(evt.getPoint());
        txtPID.setText(tblProductos.getValueAt(fila, 0).toString());
        txtNProducto.setText(tblProductos.getValueAt(fila, 1).toString());
        txtPDescripcion.setText(tblProductos.getValueAt(fila, 2).toString());
        txtUnidadM.setText(tblProductos.getValueAt(fila, 3).toString());
        txtPCosto.setText(tblProductos.getValueAt(fila, 4).toString());
        txtPPrecio.setText(tblProductos.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tblProductosMouseClicked

    private void txtServicio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtServicio1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServicio1ActionPerformed

    private void btnEliminarServicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarServicio2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarServicio2ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
       JOptionPane.showMessageDialog(null, "Reserva registrada");
       LimpiarCliente();
       LimpiarEvento();
       cardlayout.show(jPanel2, "c1");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        cardlayout.show(jPanel2, "c2");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void txtHInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHInicioActionPerformed

    private void txtHInicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHInicioKeyTyped
        char c = evt.getKeyChar();
                if (!(Character.isDigit(c) && txtHInicio.getText().length() < 5 || c == ':' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    evt.consume(); // Evita que el carácter se agregue al JTextField
                }
    }//GEN-LAST:event_txtHInicioKeyTyped

    private void txtCantidadConsumoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadConsumoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); // Si la tecla presionada no es un dígito, la consumimos (ignoramos la entrada)
        }
    }//GEN-LAST:event_txtCantidadConsumoKeyTyped

    private void txtCantidadConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadConsumoActionPerformed
        
    }//GEN-LAST:event_txtCantidadConsumoActionPerformed

    private void btnAgregarConsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarConsumoActionPerformed
            if (!"".equals(txtCantidadConsumo.getText())) {
            String cantidadText = txtCantidadConsumo.getText();
            Object selectedItem = comboProductos.getSelectedItem();

            if (selectedItem != null && selectedItem instanceof Combo) {
                Combo selectedProducto = (Combo) selectedItem;
                String nombreProducto = selectedProducto.getNombre();
                int cantidadIngresada = Integer.parseInt(cantidadText); // Convierte la cantidad ingresada a un entero
                double precioUnitario = Double.parseDouble(lblPrecioUConsumo.getText());
                double subtotal = cantidadIngresada * precioUnitario;
                item = item + 1;
                DefaultTableModel tmp = (DefaultTableModel) tblConsumo.getModel();

                for (int i = 0; i < tblConsumo.getRowCount(); i++) {
                    if (tblConsumo.getValueAt(i, 0).equals(nombreProducto)) {
                        JOptionPane.showMessageDialog(null, "El producto ya está registrado");
                        LimpiarConsumo();
                        return;
                    }
                }

                ArrayList<Object> lista = new ArrayList<Object>();
                lista.add(item);
                lista.add(nombreProducto);
                lista.add(cantidadIngresada);
                lista.add(precioUnitario);
                lista.add(subtotal);

                Object[] O = new Object[4];
                O[0] = lista.get(1);
                O[1] = lista.get(2); 
                O[2] = lista.get(3); 
                O[3] = lista.get(4); 

                tmp.addRow(O);
                tblConsumo.setModel(tmp);
                TotalPagar();
                LimpiarConsumo();
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto válido");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
        }
    }//GEN-LAST:event_btnAgregarConsumoActionPerformed

    private void tblConsumoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConsumoMouseClicked
        if (tblConsumo.getSelectedRow() != -1) {
            comboProductos.setEditable(false);
            DefaultTableModel model = (DefaultTableModel) tblConsumo.getModel();
            int selectedRow = tblConsumo.getSelectedRow();
            Object producto = model.getValueAt(selectedRow, 0);

            // Autocompleta el campo txtCantidadConsumo
            Object cantidad = model.getValueAt(selectedRow, 1);
            txtCantidadConsumo.setText(cantidad.toString());

            // Encuentra y selecciona el producto correcto en comboProductos
            for (int i = 0; i < comboProductos.getItemCount(); i++) {
                Combo comboItem = (Combo) comboProductos.getItemAt(i);
                if (comboItem.getNombre().equals(producto.toString())) {
                    comboProductos.setSelectedItem(comboItem);
                    break;
                }
            }

            // Llama manualmente al evento KeyReleased
            txtCantidadConsumoKeyReleased(null);
        }   
    }//GEN-LAST:event_tblConsumoMouseClicked

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btn_pagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pagosActionPerformed
        cardlayout.show(jPanel2, "c6");
    }//GEN-LAST:event_btn_pagosActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void btn_PAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PAgregar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_PAgregar1ActionPerformed

    private void btn_PModificar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PModificar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_PModificar1ActionPerformed

    private void btn_PEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PEliminar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_PEliminar1ActionPerformed

    private void tblProductos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductos1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductos1MouseClicked

    private void tblStaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStaffMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblStaffMouseClicked

    private void txtNombreStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreStaffActionPerformed

    private void txtTelefonoStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoStaffActionPerformed

    private void btn_AgregarStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_AgregarStaffActionPerformed

    private void btn_ModificarStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ModificarStaffActionPerformed

    private void btn_EliminarStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarStaffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_EliminarStaffActionPerformed

    private void txtGastojTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGastojTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGastojTextField19ActionPerformed

    private void btn_AgregarGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarGastoActionPerformed
        JOptionPane.showMessageDialog(null, "Registro agregado con éxito");
    }//GEN-LAST:event_btn_AgregarGastoActionPerformed

    private void btn_ModificarGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarGastoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_ModificarGastoActionPerformed

    private void btn_EliminarGastoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarGastoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_EliminarGastoActionPerformed

    private void tblGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGastosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGastosMouseClicked

    private void tblReservasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReservasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblReservasMouseClicked

    private void tblServicios_ResMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblServicios_ResMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblServicios_ResMouseClicked

    private void tblConsumoXReservaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConsumoXReservaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblConsumoXReservaMouseClicked

    private void btn_EliminarStaff1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarStaff1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_EliminarStaff1ActionPerformed

    private void comboProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProductosActionPerformed
        Combo selectedProducto = (Combo) comboProductos.getSelectedItem();
        if (selectedProducto.getId() != -1){
            txtCantidadConsumo.setEditable(true);
            double precioProducto = producto.ObtenerPrecioDeProducto(selectedProducto.getNombre()); // Obtener el precio
            lblPrecioUConsumo.setText("" + precioProducto); // Actualizar el JLabel con el precio
        }
        else{
            lblPrecioUConsumo.setText("---");
        }
    }//GEN-LAST:event_comboProductosActionPerformed

    private void comboProductosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboProductosKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_comboProductosKeyTyped

    private void txtCantidadConsumoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadConsumoKeyReleased
        String cantidadText = txtCantidadConsumo.getText();
    
        if (!cantidadText.isEmpty()) {
            int cantidadIngresada = Integer.parseInt(cantidadText);
            double precioUnitario = Double.parseDouble(lblPrecioUConsumo.getText());
            double subtotal = cantidadIngresada * precioUnitario;
            lblSubtotalConsumo.setText(String.valueOf(subtotal));
        } else {
            // Puedes establecer un valor predeterminado o un mensaje de error si el campo está vacío
            lblSubtotalConsumo.setText("---");
        }
    }//GEN-LAST:event_txtCantidadConsumoKeyReleased

    private void btnAgregarServicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarServicio2ActionPerformed
        
    }//GEN-LAST:event_btnAgregarServicio2ActionPerformed

    private void btnEliminarServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarServiciosActionPerformed
        int selectedRow = tblServicios.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) tblServicios.getModel();

            // Elimina la fila seleccionada del modelo de la tabla
            model.removeRow(selectedRow);

            // Actualiza el modelo de la tabla
            tblServicios.setModel(model);

            // Recalcula el total de los servicios
            TotalServicios();
        }
    }//GEN-LAST:event_btnEliminarServiciosActionPerformed

    private void btnNReservaFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNReservaFinalizarActionPerformed
            if (tblServicios.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Falta seleccionar servicio a reservar");
            } else {
                RegistrarCliente();
                RegistrarEvento();
                RegistrarReserva();
                RegistrarConsumo();
                RegistrarServicios();
                JOptionPane.showMessageDialog(null, "Reserva registrada");
                LimpiarCliente();
                LimpiarEvento();
                LimpiarConsumo();
                LimpiarServicios();
                LimpiarTablas(tblConsumo);
                LimpiarTablas(tblServicios);
                cardlayout.show(jPanel2, "c1");
                tblConsumo.clearSelection();
                tblServicios.clearSelection();
            }
    }//GEN-LAST:event_btnNReservaFinalizarActionPerformed

    private void btnNReservaAnt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNReservaAnt2ActionPerformed
        cardlayout.show(jPanel2, "c2");
    }//GEN-LAST:event_btnNReservaAnt2ActionPerformed

    private void btnAgregarServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarServiciosActionPerformed
        Servicio selectedServicio = (Servicio) comboServicios.getSelectedItem(); // Obtén el servicio seleccionado
        String nombreServicio = selectedServicio.getNombre();
    
        // Verifica si se ha seleccionado un servicio
        if (selectedServicio != null) {
            DefaultTableModel model = (DefaultTableModel) tblServicios.getModel();
            if (selectedServicio.getId() == -1) {
                // No hacer nada si se selecciona un servicio con ID -1
                return;
            }
            // Verificar si el servicio ya existe en la tabla
            for (int i = 0; i < tblServicios.getRowCount(); i++) {
                if (tblServicios.getValueAt(i, 0).equals(nombreServicio)) {
                    JOptionPane.showMessageDialog(null, "El servicio ya está registrado");
                    LimpiarConsumo();
                    return;
                }
            }
            
            // Agrega el servicio seleccionado a la tabla tblServicios
            model.addRow(new Object[] {
                selectedServicio.getNombre(),
                selectedServicio.getDescripcion(),
                selectedServicio.getCosto(),
                selectedServicio.getPrecio()
            });

            // Actualiza el modelo de la tabla
            tblServicios.setModel(model);
        }
        TotalServicios();
    }//GEN-LAST:event_btnAgregarServiciosActionPerformed

    private void btn_serviciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_serviciosActionPerformed
        cardlayout.show(jPanel2, "c8");
        LimpiarTabla();
        LimpiarServicio();
        ListarServicio();
    }//GEN-LAST:event_btn_serviciosActionPerformed

    private void txtServiciojTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtServiciojTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServiciojTextField19ActionPerformed

    private void btnGuardarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarServicioActionPerformed
        if (!"".equals(txtServicio.getText()) || !"".equals(txtDescripcionServicio.getText()) || !"".equals(txtCostoServicio.getText()) || !"".equals(txtPrecioServicio.getText())){
            ser.setNombre(txtServicio.getText());
            ser.setDescripcion(txtDescripcionServicio.getText());
            ser.setCosto(Double.parseDouble(txtCostoServicio.getText()));
            ser.setPrecio(Double.parseDouble(txtPrecioServicio.getText()));
            servicio.RegistrarServicio(ser);
            LimpiarTabla();
            LimpiarServicio();
            ListarServicio();
            JOptionPane.showMessageDialog(null, "Servicio registrado");
        }else{
            JOptionPane.showMessageDialog(null, "Los campos están vacíos");
        }
    }//GEN-LAST:event_btnGuardarServicioActionPerformed

    private void btnModificarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarServicioActionPerformed
        if ("".equals(txtServicioID.getText())){
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else{
            ser.setId(Integer.parseInt(txtServicioID.getText()));
            ser.setNombre(txtServicio.getText());
            ser.setDescripcion(txtDescripcionServicio.getText());
            ser.setCosto(Double.parseDouble(txtCostoServicio.getText()));
            ser.setPrecio(Double.parseDouble(txtPrecioServicio.getText()));
            if(!"".equals(txtServicioID.getText()) || !"".equals(txtServicio.getText()) || !"".equals(txtDescripcionServicio.getText()) || !"".equals(txtCostoServicio.getText()) || !"".equals(txtPrecioServicio.getText())){
                servicio.ModificarServicio(ser);
                LimpiarTabla();
                LimpiarServicio();
                ListarServicio();
            }
        }
    }//GEN-LAST:event_btnModificarServicioActionPerformed

    private void btnEliminarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarServicioActionPerformed
        if ("".equals(txtServicioID.getText())){
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else{
            if (!"".equals(txtServicioID.getText())){
                int pregunta = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar el registro?");
                if (pregunta == 0){
                    int id = Integer.parseInt(txtServicioID.getText());
                    servicio.EliminarServicio(id);
                    LimpiarTabla();
                    LimpiarServicio();
                    ListarServicio();
                }
            }
        }
    }//GEN-LAST:event_btnEliminarServicioActionPerformed

    private void tblServicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblServicioMouseClicked
        int fila = tblServicio.rowAtPoint(evt.getPoint());
        txtServicioID.setText(tblServicio.getValueAt(fila, 0).toString());
        txtServicio.setText(tblServicio.getValueAt(fila, 1).toString());
        txtDescripcionServicio.setText(tblServicio.getValueAt(fila, 2).toString());
        txtCostoServicio.setText(tblServicio.getValueAt(fila, 3).toString());
        txtPrecioServicio.setText(tblServicio.getValueAt(fila, 4).toString());
    }//GEN-LAST:event_tblServicioMouseClicked

    private void tblServiciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblServiciosMouseClicked
        if (tblServicios.getSelectedRow() != -1) {
            DefaultTableModel model = (DefaultTableModel) tblServicios.getModel();
            int selectedRow = tblServicios.getSelectedRow();
            Object producto = model.getValueAt(selectedRow, 0);

            // Encuentra y selecciona el producto correcto en comboServicios
            for (int i = 0; i < comboServicios.getItemCount(); i++) {
                Servicio comboItem = (Servicio) comboServicios.getItemAt(i);
                if (comboItem.getNombre().equals(producto.toString())) {
                    comboServicios.setSelectedItem(comboItem);
                    break;
                }
            }
        }   
    }//GEN-LAST:event_tblServiciosMouseClicked

    private void txtMinInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMinInicioActionPerformed

    private void txtMinInicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinInicioKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMinInicioKeyTyped

    private void txtHFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHFinActionPerformed

    private void txtHFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHFinKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHFinKeyTyped

    private void txtMinFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMinFinActionPerformed

    private void txtMinFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinFinKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMinFinKeyTyped

    private void txtHInicioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHInicioKeyReleased
         // Verifica que el campo de texto no esté vacío
        if (!txtHInicio.getText().isEmpty()) {
            try {
                // Obtén el valor actual en txtHInicio
                int valorHInicio = Integer.parseInt(txtHInicio.getText());

                // Suma 5 al valor de HInicio
                int valorHFin = valorHInicio + 5;

                if (valorHFin > 23) {
                    // El valor excede 23, muestra un mensaje y limpia ambos campos
                    JOptionPane.showMessageDialog(this, "Número de horas excedido");
                    txtHInicio.setText("");
                    txtHFin.setText("");
                } else {
                    // Formatea el nuevo valor para asegurar que tenga dos dígitos
                    String nuevoValor = String.format("%02d", valorHFin);

                    // Muestra el valor actualizado en txtHFin
                    txtHFin.setText(nuevoValor);
                }
            } catch (NumberFormatException e) {
                // Maneja una excepción en caso de que no se pueda convertir a número
                txtHFin.setText("Error");
            }
        } else {
            // Si txtHInicio está vacío, asegúrate de que txtHFin también esté vacío
            txtHFin.setText("");
        }
    }//GEN-LAST:event_txtHInicioKeyReleased

    private void txtMinInicioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinInicioKeyReleased
        // Verifica si el campo de texto txtMinIni está vacío
        if (txtMinInicio.getText().isEmpty()) {
            // Si está vacío, vacía también el campo txtMinFin
            txtMinFin.setText("");
        } else {
            // Si no está vacío, copia el valor de MinIni a MinFin
            txtMinFin.setText(txtMinInicio.getText());
        }
    }//GEN-LAST:event_txtMinInicioKeyReleased

    private void txtHorasExtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHorasExtraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorasExtraActionPerformed

    private void txtHorasExtraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorasExtraKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHorasExtraKeyTyped

    private void txtHorasExtraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHorasExtraKeyReleased
        String horasExtraText = txtHorasExtra.getText();

        if (!horasExtraText.isEmpty()) {
            int horasExtra = Integer.parseInt(horasExtraText);
            int horasFin = Integer.parseInt(txtHFin.getText());

            int nuevoHorasFin = horasFin + horasExtra - horasExtraAnterior;
            double precioHoras = horasExtra * 200.0;

            txtHFin.setText(String.valueOf(nuevoHorasFin));
            lblPrecioHoras.setText(String.format("%.2f", precioHoras));

            // Actualiza el valor anterior de horasExtra
            horasExtraAnterior = horasExtra;
        } else {
            // Si txtHorasExtra está vacío, resetea el valor de lblPrecioHoras y resta las horas al txtHFin
            txtHFin.setText(String.valueOf(Integer.parseInt(txtHFin.getText()) - horasExtraAnterior));
            lblPrecioHoras.setText("0.00");

            // Restablece el valor anterior de horasExtra
            horasExtraAnterior = 0;
        }
    }//GEN-LAST:event_txtHorasExtraKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ClienteEvento;
    private javax.swing.JPanel Clientes;
    private javax.swing.JPanel Consumo;
    private javax.swing.JLabel Fondo;
    private javax.swing.JPanel Gastos;
    private javax.swing.JLabel Logo;
    private javax.swing.JPanel PCliente;
    private javax.swing.JPanel PEvento;
    private javax.swing.JPanel Pagos;
    private javax.swing.JPanel Productos;
    private javax.swing.JPanel Reservas;
    private javax.swing.JPanel Servicio;
    private javax.swing.JPanel Servicios;
    private javax.swing.JPanel Serviciosold;
    private javax.swing.JPanel Staff;
    private javax.swing.JLabel Titulo;
    private javax.swing.JLabel Titulo1;
    private javax.swing.JLabel Titulo10;
    private javax.swing.JLabel Titulo2;
    private javax.swing.JLabel Titulo3;
    private javax.swing.JLabel Titulo4;
    private javax.swing.JLabel Titulo5;
    private javax.swing.JLabel Titulo6;
    private javax.swing.JLabel Titulo7;
    private javax.swing.JLabel Titulo8;
    private javax.swing.JLabel Titulo9;
    private javax.swing.JButton btnAgregarConsumo;
    private javax.swing.JButton btnAgregarServicio2;
    private javax.swing.JButton btnAgregarServicios;
    private javax.swing.JButton btnEliminarConsumo;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarServicio;
    private javax.swing.JButton btnEliminarServicio2;
    private javax.swing.JButton btnEliminarServicios;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnGuardarServicio;
    private javax.swing.JButton btnModificarProducto;
    private javax.swing.JButton btnModificarServicio;
    private javax.swing.JButton btnNReservaAnt1;
    private javax.swing.JButton btnNReservaAnt2;
    private javax.swing.JButton btnNReservaFinalizar;
    private javax.swing.JButton btnNReservaSig1;
    private javax.swing.JButton btnNReservaSig2;
    private javax.swing.JButton btn_AgregarGasto;
    private javax.swing.JButton btn_AgregarStaff;
    private javax.swing.JButton btn_EliminarGasto;
    private javax.swing.JButton btn_EliminarStaff;
    private javax.swing.JButton btn_EliminarStaff1;
    private javax.swing.JButton btn_ModificarGasto;
    private javax.swing.JButton btn_ModificarStaff;
    private javax.swing.JButton btn_PAgregar1;
    private javax.swing.JButton btn_PEliminar1;
    private javax.swing.JButton btn_PModificar1;
    private javax.swing.JButton btn_clientes;
    private javax.swing.JButton btn_gastos;
    private javax.swing.JButton btn_nreserva;
    private javax.swing.JButton btn_pagos;
    private javax.swing.JButton btn_productos;
    private javax.swing.JButton btn_reservas;
    private javax.swing.JButton btn_servicios;
    private javax.swing.JButton btn_staff;
    public javax.swing.JComboBox<Object> comboProductos;
    private javax.swing.JComboBox<Servicio> comboServicios;
    private com.toedter.calendar.JDateChooser fechaEvento;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel lblPrecioHoras;
    private javax.swing.JLabel lblPrecioUConsumo;
    private javax.swing.JLabel lblSubtotalConsumo;
    private javax.swing.JLabel lblTotalConsumo;
    private javax.swing.JLabel lblTotalServicio;
    private javax.swing.JLabel lblTotalServicio2;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblConsumo;
    private javax.swing.JTable tblConsumoXReserva;
    private javax.swing.JTable tblGastos;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTable tblProductos1;
    private javax.swing.JTable tblReservas;
    private javax.swing.JTable tblServicio;
    private javax.swing.JTable tblServicio2;
    private javax.swing.JTable tblServicios;
    private javax.swing.JTable tblServicios_Res;
    private javax.swing.JTable tblStaff;
    private javax.swing.JTextField txtApellidoMCliente;
    private javax.swing.JTextField txtApellidoMStaff;
    private javax.swing.JTextField txtApellidoPCliente;
    private javax.swing.JTextField txtApellidoPStaff;
    private javax.swing.JTextField txtCantidadConsumo;
    private javax.swing.JTextField txtCorreoCliente;
    private javax.swing.JTextField txtCostoServicio;
    private javax.swing.JTextField txtCostoServicio1;
    private javax.swing.JTextArea txtDescripcionEvento;
    private javax.swing.JTextField txtDescripcionGasto;
    private javax.swing.JTextArea txtDescripcionServicio;
    private javax.swing.JTextField txtDescripcionServicio1;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtGasto;
    private javax.swing.JTextField txtGastoID;
    private javax.swing.JTextField txtHFin;
    private javax.swing.JTextField txtHInicio;
    private javax.swing.JTextField txtHorasExtra;
    private javax.swing.JTextField txtMinFin;
    private javax.swing.JTextField txtMinInicio;
    private javax.swing.JTextField txtNProducto;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreEvento;
    private javax.swing.JTextField txtNombreStaff;
    private javax.swing.JTextField txtPCosto;
    private javax.swing.JTextField txtPCosto1;
    private javax.swing.JTextField txtPCosto2;
    private javax.swing.JTextField txtPDescripcion;
    private javax.swing.JTextField txtPID;
    private javax.swing.JTextField txtPPrecio;
    private javax.swing.JTextField txtPosicionStaff;
    private javax.swing.JTextField txtPrecioServicio;
    private javax.swing.JTextField txtPrecioServicio1;
    private javax.swing.JTextField txtServicio;
    private javax.swing.JTextField txtServicio1;
    private javax.swing.JTextField txtServicioID;
    private javax.swing.JTextField txtSueldoStaff;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoStaff;
    private javax.swing.JTextField txtUnidadM;
    private javax.swing.JTextField txtUnidadM1;
    // End of variables declaration//GEN-END:variables

    public void ListarProducto(){
        List<Producto> ListarPro = producto.ListarProducto();
        modelo = (DefaultTableModel) tblProductos.getModel();
        Object[] ob = new Object[6];
        for (int i = 0; i < ListarPro.size(); i++) {
            ob[0] = ListarPro.get(i).getId();
            ob[1] = ListarPro.get(i).getNombre();
            ob[2] = ListarPro.get(i).getDescripcion();
            ob[3] = ListarPro.get(i).getUnidad_medida();
            ob[4] = ListarPro.get(i).getCosto();
            ob[5] = ListarPro.get(i).getPrecio();
            modelo.addRow(ob);
        }
        tblProductos.setModel(modelo);
    }
    
    public void ListarServicio(){
        List<Servicio> ListarServ = servicio.ListarServicio();
        modelo = (DefaultTableModel) tblServicio.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListarServ.size(); i++) {
            ob[0] = ListarServ.get(i).getId();
            ob[1] = ListarServ.get(i).getNombre();
            ob[2] = ListarServ.get(i).getDescripcion();
            ob[3] = ListarServ.get(i).getCosto();
            ob[4] = ListarServ.get(i).getPrecio();
            modelo.addRow(ob);
        }
        tblServicio.setModel(modelo);
    }
    
    public void ListarConsumo(){
        List<Consumo> ListaConsumo = consumo.ListarConsumo();
        modelo = (DefaultTableModel) tblConsumoXReserva.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListaConsumo.size(); i++) {
            ob[0] = ListaConsumo.get(i).getIdreserva();
            ob[1] = ListaConsumo.get(i).getProducto();
            ob[2] = ListaConsumo.get(i).getDescripcion();
            ob[3] = ListaConsumo.get(i).getCosto();
            ob[4] = ListaConsumo.get(i).getPrecio();
            modelo.addRow(ob);
        }
        tblConsumoXReserva.setModel(modelo);
    }
    
    public void ListarServicios(){
        List<Servicio> ListarServs = servicio.ListarServicios();
        modelo = (DefaultTableModel) tblServicios_Res.getModel();
        Object[] ob = new Object[5];
        for (int i = 0; i < ListarServs.size(); i++) {
            ob[0] = ListarServs.get(i).getIdreserva();
            ob[1] = ListarServs.get(i).getNombre();
            ob[2] = ListarServs.get(i).getDescripcion();
            ob[3] = ListarServs.get(i).getCosto();
            ob[4] = ListarServs.get(i).getPrecio();
            modelo.addRow(ob);
        }
        tblServicios_Res.setModel(modelo);
    }
    
    public void ListarReservas(){
        List<Reserva> ListarRes = reserva.ListarReservas();
        modelo = (DefaultTableModel) tblReservas.getModel();
        Object[] ob = new Object[9];
        for (int i = 0; i < ListarRes.size(); i++) {
            ob[0] = ListarRes.get(i).getId();
            ob[1] = ListarRes.get(i).getCliente();
            ob[2] = ListarRes.get(i).getEvento();
            ob[3] = ListarRes.get(i).getDescripcion();
            ob[4] = ListarRes.get(i).getFecha_eve();
            ob[5] = ListarRes.get(i).getHora_ini();
            ob[6] = ListarRes.get(i).getHora_fin();
            ob[7] = ListarRes.get(i).getFecha();
            ob[8] = ListarRes.get(i).getEstadotxt();
            modelo.addRow(ob);
        }
        tblReservas.setModel(modelo);
    }
    
    public void RegistrarReserva(){
        String nombreCliente = txtNombreCliente.getText();
        int id_c = cliente.BuscarIdCli(nombreCliente);
        res.setIdcliente(id_c);
        
        String nombreEvento = txtNombreEvento.getText();
        int id_e = evento.BuscarIdEve(nombreEvento);
        res.setIdevento(id_e);

        String precioHExtrastxt = lblPrecioHoras.getText();
        String totalConsumotxt = lblTotalConsumo.getText();
        String totalServiciostxt = lblTotalServicio.getText();
        
        double precioHExtrasint = Double.parseDouble(precioHExtrastxt);
        double totalConsumoint = Double.parseDouble(totalConsumotxt);
        double totalServiciosint = Double.parseDouble(totalServiciostxt);
        
        double precioReservaTotal = precioHExtrasint + totalConsumoint + totalServiciosint;
        
        res.setPrecio(precioReservaTotal);
        reserva.RegistrarReserva(res);
    }
    
    public void RegistrarCliente(){
        if (!txtNombreCliente.getText().isEmpty() &&
        !txtApellidoPCliente.getText().isEmpty() &&
        !txtApellidoMCliente.getText().isEmpty() &&
        !txtApellidoMCliente.getText().isEmpty() &&
        !txtTelefonoCliente.getText().isEmpty() &&
        !txtCorreoCliente.getText().isEmpty() &&
        !txtDireccionCliente.getText().isEmpty()) {

        cl.setNombre(txtNombreCliente.getText());
        cl.setAp_paterno(txtApellidoPCliente.getText());
        cl.setAp_materno(txtApellidoMCliente.getText());
        cl.setTelefono(txtTelefonoCliente.getText());
        cl.setCorreo(txtCorreoCliente.getText());
        cl.setDireccion(txtDireccionCliente.getText());
        cliente.RegistrarCliente(cl);
    } else {
        JOptionPane.showMessageDialog(null, "Faltan campos de cliente por llenar");
        }
    }
    
    public void RegistrarEvento() {
        // Realiza el registro del evento si los campos son válidos
        if (validarCamposEvento()) {
            String horaInicioBD = txtHInicio.getText() + ":" + txtMinInicio.getText();
            String horaFinBD = txtHFin.getText() + ":" + txtMinFin.getText();

            ev.setNombre(txtNombreEvento.getText());
            ev.setDescripcion(txtDescripcionEvento.getText());
            // Convertir la fecha de java.util.Date a java.sql.Date
            java.util.Date fechaUtil = fechaEvento.getDate();
            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());
            ev.setFecha(fechaSql);
            ev.setHora_ini(horaInicioBD);
            ev.setHora_fin(horaFinBD);

            evento.RegistrarEvento(ev);
        }
    }
    
    public void RegistrarConsumo(){
        int id_r = reserva.IdReserva();
        for (int i = 0; i < tblConsumo.getRowCount(); i++) {
            String nombreProducto = tblConsumo.getValueAt(i, 0).toString();
            int id_p = producto.BuscarIdPro(nombreProducto);
            int cant = Integer.parseInt(tblConsumo.getValueAt(i, 1).toString());
            double precio = Double.parseDouble(tblConsumo.getValueAt(i, 2).toString());
            double subtotal = cant * precio;
            con.setIdreserva(id_r);
            con.setIdproducto(id_p);
            con.setCantidad(cant);
            con.setSubtotal(subtotal);
            consumo.RegistrarConsumo(con);
        }
    }
    
    public void RegistrarServicios(){
        int id_r = reserva.IdReserva();
        for (int i = 0; i < tblServicios.getRowCount(); i++) {
            String nombreServicio = tblServicios.getValueAt(i, 0).toString();
            int id_s = servicio.BuscarIdSer(nombreServicio);
            serv.setIdreserva(id_r);
            serv.setIdservicio(id_s);
            servicio.RegistrarServicios(serv);
        }
    }
    
    public class NumberDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;

        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String resultingText = currentText.substring(0, offset) + string + currentText.substring(offset);

        if (resultingText.matches("\\d{0,2}")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;

        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String resultingText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        if (resultingText.matches("\\d{0,2}")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
    
    private boolean validarCamposCliente() {
        // Verifica los campos del cliente y retorna true si son válidos
        if (!txtNombreCliente.getText().isEmpty() &&
            !txtApellidoPCliente.getText().isEmpty() &&
            !txtApellidoMCliente.getText().isEmpty() &&
            !txtTelefonoCliente.getText().isEmpty() &&
            !txtCorreoCliente.getText().isEmpty() &&
            !txtDireccionCliente.getText().isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Faltan campos de cliente por llenar");
            return false;
        }
    }

    private boolean validarCamposEvento() {
        // Verifica si los campos del evento están llenos
        if (txtNombreEvento.getText().isEmpty() ||
            txtDescripcionEvento.getText().isEmpty() ||
            fechaEvento.getDate() == null ||
            txtHInicio.getText().isEmpty() ||
            txtMinInicio.getText().isEmpty() ||
            txtHFin.getText().isEmpty() ||
            txtMinFin.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan campos de evento por llenar");
            return false;
        }

        // Verifica las horas del evento
        String horaInicio = txtHInicio.getText();
        String minInicio = txtMinInicio.getText();
        String horaFinal = txtHFin.getText();
        String minFinal = txtMinFin.getText();

        if (horaInicio.matches("\\d{2}") && minInicio.matches("\\d{2}") && horaFinal.matches("\\d{2}") && minFinal.matches("\\d{2}")) {
            String horaInicioBD = horaInicio + ":" + minInicio;
            String horaFinBD = horaFinal + ":" + minFinal;

            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Favor de corregir campos de hora (dos dígitos)");
            return false;
        }
    }

    private String corregirLongitud(String numero) {
        // Asegura que el número tenga al menos dos dígitos
        return String.format("%02d", Integer.parseInt(numero));
    }
}
