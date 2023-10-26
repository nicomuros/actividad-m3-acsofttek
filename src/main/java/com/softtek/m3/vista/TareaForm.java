package com.softtek.m3.vista;

import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.servicio.TareaServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        agregarButton.addActionListener(e -> handleAgregarTarea());
        modificarButton.addActionListener(e -> {

        });
        eliminarButton.addActionListener(e -> {

        });
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

    private void handleAgregarTarea(){
        // Leer los valores del formulario
        String titulo = tituloUserInput.getText();
        String descripcion = descripcionUserInput.getText();
        try {
            if (titulo.isBlank() && descripcion.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título y una descripción para agregar la tarea. No se admiten espacios en blanco.");
            }
            if (titulo.isBlank()){
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }
            if (descripcion.isBlank()){
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }
            tareaServicio.agregarTarea(titulo, descripcion);
            listarTareas();
            manejarExito("Tarea agregada correctamente");
        } catch (Exception e){
            manejarExcepcion(e);
        }


    }

    private void manejarExcepcion(Exception e){
        resultadoLabel.setForeground(Color.red);
        resultadoLabel.setText(e.getMessage());
    }

    private void manejarExito(String mensaje){
        resultadoLabel.setForeground(Color.black);
        resultadoLabel.setText(mensaje);
    }
    private void createUIComponents() {
        iniciarTabla();
        listarTareas();
    }

    private void iniciarTabla(){
        // Configuramos los títulos de la tabla
        this.tablaModeloTareas = new DefaultTableModel(0, 3);
        String[] titulosColumnas = {"ID", "Titulo", "Descripcion"};
        this.tablaModeloTareas.setColumnIdentifiers(titulosColumnas);

        // Configurar ancho columnas


        // Instanciamos el objeto de JTable
        this.tablaTareas = new JTable(tablaModeloTareas);
        this.tablaTareas.getColumnModel().getColumn(0).setMaxWidth(20);
        this.tablaTareas.getColumnModel().getColumn(1).setMaxWidth(230);


    }

    private void listarTareas(){
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
