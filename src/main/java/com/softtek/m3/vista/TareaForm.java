package com.softtek.m3.vista;

import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.servicio.TareaServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class TareaForm extends JFrame {

    // Se declara la dependencia que será inyectada
    TareaServicio tareaServicio;

    private JPanel panelPrincipal;
    private JTable tablaTareas;
    private JScrollPane scrollPane;
    private JTextField tituloUserInput;
    private JTextField descripcionUserInput;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JLabel idSelected;
    private JLabel resultadoLabel;
    private DefaultTableModel tablaModeloTareas;

    public TareaForm(TareaServicio tareaServicio) {

        // Se instancia la dependendia del servicio.
        this.tareaServicio = tareaServicio;

        // Se inicia el formulario
        iniciarForm();

        // Configuración de botones
        agregarButton.addActionListener(e -> manejarAgregarTarea());
        modificarButton.addActionListener(e -> manejarModificarTarea());
        eliminarButton.addActionListener(e -> manejarEliminarTarea());
        tablaTareas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                manejarSeleccionTarea();
            }
        });
    }

    private void createUIComponents() {
        this.resultadoLabel = new JLabel();
        resultadoLabel.setText("Aguardando acción...");
        iniciarTabla();
        listarTareas();
    }

    private void iniciarForm() {

        // Configuración del panel
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 900);

        // Ubicar el form en el centro de la pantalla
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (getWidth() / 2);
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

    }

    private void iniciarTabla() {

        // Configuración de los títulos de la tabla
        this.tablaModeloTareas = new DefaultTableModel(0, 3) {
            // Configuración para que no se puedan modificar las celdas desde la interfaz
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Titulo de las columnas
        String[] titulosColumnas = {"ID", "Titulo", "Descripcion"};
        this.tablaModeloTareas.setColumnIdentifiers(titulosColumnas);

        // Instancia objeto de JTable
        this.tablaTareas = new JTable(tablaModeloTareas);

        // Configurar ancho columnas
        this.tablaTareas.getColumnModel().getColumn(0).setMaxWidth(20);
        this.tablaTareas.getColumnModel().getColumn(1).setMaxWidth(230);

        // Configurar imposibilidad de seleccionar más de un registro
        this.tablaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void manejarAgregarTarea() {

        // Leer los valores del formulario
        String titulo = tituloUserInput.getText();
        String descripcion = descripcionUserInput.getText();

        try {

            // Validaciones
            if (titulo.isBlank() && descripcion.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título y una descripción para agregar la tarea.");
            }
            if (titulo.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }
            if (descripcion.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }

            // Se solicita al repositorio agregar la nueva tarea
            Tarea tarea = new Tarea(titulo, descripcion);
            tareaServicio.agregarTarea(tarea);
            manejarExito("Tarea agregada correctamente.");

        } catch (Exception e) {

            // Se muestra mensaje de error
            manejarExcepcion(e);
        } finally {

            // Actualización de tabla, y limpieza de campos
            listarTareas();
            limpiarCampos();
        }
    }

    private void manejarSeleccionTarea() {

        // Se obtiene el número de fila que seleccionó
        int filaSeleccionada = tablaTareas.getSelectedRow();

        // Se comprueba que se haya seleccionado una fila.
        if (filaSeleccionada != -1) {

            // Recuperación de datos de la tabla
            String tareaIdString = tablaTareas.getModel()
                    .getValueAt(filaSeleccionada, 0)
                    .toString();

            String titulo = tablaTareas.getModel().getValueAt(filaSeleccionada, 1).toString();
            String descripcion = tablaTareas.getModel().getValueAt(filaSeleccionada, 2).toString();

            // Se muestran los datos en los fieldtext
            idSelected.setText(tareaIdString);
            tituloUserInput.setText(titulo);
            descripcionUserInput.setText(descripcion);
        }
    }

    private void manejarModificarTarea() {

        // Se recuperan los datos de los fieldset
        String tareaIdString = idSelected.getText();
        String titulo = tituloUserInput.getText();
        String descripcion = descripcionUserInput.getText();

        try {

            // Validaciones
            if (tareaIdString.isBlank())
                throw new DatosInvalidosException("Error: No seleccionó ninguna tarea para modificar.");
            if (titulo.isBlank() && descripcion.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título y una descripción para modificar la tarea.");
            }
            if (titulo.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }
            if (descripcion.isBlank()) {
                throw new DatosInvalidosException("Error: Debe introducir un título. No se admiten espacios en blanco.");
            }

            // Se obtiene el ID de la tarea
            Integer tareaId = Integer.parseInt(tareaIdString);

            // Se crea un objeto Tarea con los datos modificados
            Tarea tareaModificada = new Tarea();
            tareaModificada.setId(tareaId);
            tareaModificada.setTitulo(titulo);
            tareaModificada.setDescripcion(descripcion);

            // Solicitud al servicio de modificar la tarea
            tareaServicio.modificarTarea(tareaModificada);
            manejarExito("Tarea modificada exitosamente.");

        } catch (Exception e) {

            // Se muestra mensaje de error
            manejarExcepcion(e);
        } finally {

            // Actualización de tabla, y limpieza de campos
            listarTareas();
            limpiarCampos();
        }
    }

    private void manejarEliminarTarea() {
        String tareaIdString = idSelected.getText();
        try {
            if (tareaIdString.isBlank())
                throw new DatosInvalidosException("Error: No seleccionó ninguna tarea para eliminar.");

            Integer tareaId = Integer.parseInt(tareaIdString);
            tareaServicio.eliminarTarea(tareaId);

            manejarExito("Tarea eliminada exitosamente.");

        } catch (Exception e) {
            manejarExcepcion(e);
        } finally {
            listarTareas();
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        idSelected.setText("");
        tituloUserInput.setText("");
        descripcionUserInput.setText("");
    }

    private void manejarExcepcion(Exception e) {
        // Configurar color del label en rojo
        resultadoLabel.setForeground(Color.RED);
        resultadoLabel.setText(e.getMessage());
    }

    private void manejarExito(String mensaje) {
        // Configurar color del label en verde
        resultadoLabel.setForeground(Color.GREEN);
        resultadoLabel.setText(mensaje);
    }

    private void listarTareas() {
        // Limpiar la tabla
        tablaModeloTareas.setRowCount(0);
        List<Tarea> listaDeTareas = new ArrayList<>();
        // Se almacenan las tareas provistas por el servicio
        try {
            listaDeTareas = tareaServicio.obtenerTareas();
        } catch (Exception e) {
            manejarExcepcion(e);
        }
        // Se rellena la tabla
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
