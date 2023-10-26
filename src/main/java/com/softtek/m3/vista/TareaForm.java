package com.softtek.m3.vista;

import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.servicio.TareaServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

public class TareaForm extends JFrame {
    TareaServicio tareaServicio;
    private JPanel panelPrincipal;
    private JTable tablaTareas;
    private JScrollPane scrollPane;
    private JTextField tituloUserInput;
    private JTextField descripcionUserInput;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JLabel resultadoLabel;
    private DefaultTableModel tablaModeloTareas;

    public TareaForm(TareaServicio tareaServicio){
        this.tareaServicio = tareaServicio;
        iniciarForm();
    }

    private void iniciarForm(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 900);

        // Para obtener las dimensiones de la ventana y ubicarlo en el centro
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Dimension screenSize = toolkit.getScreenSize();
        int x = (getWidth()/2);
        int y = (screenSize.height - getHeight())/2;
        setLocation(x, y);
    }


    private void createUIComponents() {
        // Configuramos los títulos de la tabla
        this.tablaModeloTareas = new DefaultTableModel(0, 3);
        String[] titulosColumnas = {"ID", "Titulo", "Descripcion"};
        this.tablaModeloTareas.setColumnIdentifiers(titulosColumnas);

        // Instanciamos el objeto de JTable
        this.tablaTareas = new JTable(tablaModeloTareas);

        listarLibros();
    }

    private void listarLibros(){
        // Limpiar la tabla
        tablaModeloTareas.setRowCount(0);

        // Obtener los libros de la base de datos
        List<Tarea> listaDeTareas = tareaServicio.obtenerTareas();

        listaDeTareas.forEach((tarea) -> {
            Object[] renglonTarea = {
                    tarea.getId(),
                    tarea.getTitulo(),
                    tarea.getDescripcion()
            };
            this.tablaModeloTareas.addRow(renglonTarea);
        });

    }
}
