import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import entidades.Documento;
import servicios.Arbol;
import servicios.Nodo;
import servicios.ServicioDocumento;
import servicios.Util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FrmOrdenamiento extends JFrame {

    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JToolBar tbOrdenamiento;
    private JComboBox cmbCriterio;
    private JTextField txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private JTable tblDocumentos;

    public FrmOrdenamiento() {

        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();
        cmbCriterio = new JComboBox();
        txtTiempo = new JTextField();

        btnBuscar = new JButton();
        txtBuscar = new JTextField();

        tblDocumentos = new JTable();

        setSize(600, 400);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnOrdenarBurbuja.setIcon(new ImageIcon(getClass().getResource("/iconos/Ordenar.png")));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarBurbujaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarRapido.png")));
        btnOrdenarRapido.setToolTipText("Ordenar Rapido");
        btnOrdenarRapido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarRapidoClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarInsercion.png")));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarInsercionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarInsercion);

        cmbCriterio.setModel(new DefaultComboBoxModel(
                new String[] { "Nombre Completo, Tipo de Documento", "Tipo de Documento, Nombre Completo" }));
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);

        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/iconos/Buscar.png")));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnBuscar(evt);
            }
        });
        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(txtBuscar);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Datos.csv";

        //ServicioDocumento.desdeArchivo(nombreArchivo);
        //ServicioDocumento.mostrar(tblDocumentos);

        Arbol arbol = new Arbol();
        arbol.desdeArchivo(nombreArchivo);
        //arbol.recorrerInOrden();
        arbol.mostrar(tblDocumentos);
    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarBurbuja(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        } else {
            JOptionPane.showMessageDialog(null, "Elija el criterio de ordenamiento");
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarRapido(0, ServicioDocumento.getTamaño() - 1, cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        } else {
            JOptionPane.showMessageDialog(null, "Elija el criterio de ordenamiento");
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {

    }

    private void btnBuscar(ActionEvent evt) {
        String textoBusqueda = txtBuscar.getText().trim();
        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un criterio de búsqueda", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Arbol arbol = new Arbol();
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Datos.csv";
        arbol.desdeArchivo(nombreArchivo);
        
        List<Nodo> resultados = arbol.buscarUsuarios(textoBusqueda);
        
        if (!resultados.isEmpty()) {
            String[][] datos = new String[resultados.size()][ServicioDocumento.getEncabezados().length];
            
            for (int i = 0; i < resultados.size(); i++) {
                Nodo nodo = resultados.get(i);
                datos[i][0] = nodo.getDocumento().getApellido1();
                datos[i][1] = nodo.getDocumento().getApellido2();
                datos[i][2] = nodo.getDocumento().getNombre();
                datos[i][3] = nodo.getDocumento().getDocumento();
            }
            
            DefaultTableModel dtm = new DefaultTableModel(datos, ServicioDocumento.getEncabezados());
            tblDocumentos.setModel(dtm);
            
            JOptionPane.showMessageDialog(this, 
                "Se encontraron " + resultados.size() + " resultados exactos", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron resultados exactos para: " + textoBusqueda, 
                "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}