package com.softtek.m3.vista;

import com.softtek.m3.excepciones.DatosInvalidosException;
import com.softtek.m3.modelo.Tarea;
import com.softtek.m3.servicio.TareaServicio;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Locale;

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
    private JLabel resultadoLabel;
    private JLabel idSelected;
    private DefaultTableModel tablaModeloTareas;

    public TareaForm(TareaServicio tareaServicio) {

        // Se instancia la dependendia del servicio.
        this.tareaServicio = tareaServicio;

        // Se inicia el formulario
        $$$setupUI$$$();
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
                throw new DatosInvalidosException("Error: Debe introducir un título y una descripción para agregar la tarea. No se admiten espacios en blanco.");
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
                throw new DatosInvalidosException("Error: Debe introducir un título y una descripción para modificar la tarea. No se admiten espacios en blanco.");
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
                throw new DatosInvalidosException("Error: No seleccionó ninguna tarea para modificar.");

            Integer tareaId = Integer.parseInt(tareaIdString);
            tareaServicio.eliminarTarea(tareaId);

            manejarExito("Tarea modificada exitosamente.");

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
        resultadoLabel.setForeground(Color.red);
        resultadoLabel.setText(e.getMessage());
    }

    private void manejarExito(String mensaje) {
        resultadoLabel.setForeground(Color.black);
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        panelPrincipal.setName("panelPrincipal");
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Ubuntu", Font.BOLD, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setText("Lista de tareas");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelPrincipal.add(label1, gbc);
        scrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(scrollPane, gbc);
        scrollPane.setViewportView(tablaTareas);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMinimumSize(new Dimension(500, 100));
        panel1.setPreferredSize(new Dimension(500, 100));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setPreferredSize(new Dimension(500, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setInheritsPopupMenu(true);
        label2.setMaximumSize(new Dimension(100, 20));
        label2.setMinimumSize(new Dimension(100, 20));
        label2.setPreferredSize(new Dimension(100, 20));
        label2.setText("Id:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel3.add(label2, gbc);
        idSelected = new JLabel();
        idSelected.setMaximumSize(new Dimension(8, 20));
        idSelected.setMinimumSize(new Dimension(8, 20));
        idSelected.setPreferredSize(new Dimension(30, 20));
        idSelected.setText("  ");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 10;
        panel2.add(idSelected, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        panel4.setMinimumSize(new Dimension(500, 30));
        panel4.setPreferredSize(new Dimension(515, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel4, gbc);
        tituloUserInput = new JTextField();
        tituloUserInput.setMaximumSize(new Dimension(300, 17));
        tituloUserInput.setMinimumSize(new Dimension(300, 17));
        tituloUserInput.setPreferredSize(new Dimension(450, 20));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(tituloUserInput, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(panel5, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setHorizontalAlignment(4);
        label3.setMaximumSize(new Dimension(100, 17));
        label3.setMinimumSize(new Dimension(100, 17));
        label3.setPreferredSize(new Dimension(100, 20));
        label3.setText("Título:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        panel5.add(label3, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer2, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        panel6.setMinimumSize(new Dimension(500, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel6, gbc);
        descripcionUserInput = new JTextField();
        descripcionUserInput.setMaximumSize(new Dimension(400, 20));
        descripcionUserInput.setMinimumSize(new Dimension(400, 25));
        descripcionUserInput.setPreferredSize(new Dimension(450, 20));
        descripcionUserInput.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 500.0;
        gbc.weighty = 30.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(descripcionUserInput, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        panel7.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel7, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, -1, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setHorizontalAlignment(4);
        label4.setPreferredSize(new Dimension(100, 20));
        label4.setText("Descripción:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        panel7.add(label4, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(spacer3, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(panel8, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, -1, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Resultado:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label5, gbc);
        resultadoLabel = new JLabel();
        resultadoLabel.setAlignmentX(0.0f);
        Font resultadoLabelFont = this.$$$getFont$$$(null, Font.BOLD, -1, resultadoLabel.getFont());
        if (resultadoLabelFont != null) resultadoLabel.setFont(resultadoLabelFont);
        resultadoLabel.setText("Aguardando accion del usuario");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(resultadoLabel, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panel8.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(spacer5, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        panel9.setMinimumSize(new Dimension(500, 30));
        panel9.setPreferredSize(new Dimension(500, 50));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTH;
        panelPrincipal.add(panel9, gbc);
        agregarButton = new JButton();
        agregarButton.setPreferredSize(new Dimension(140, 30));
        agregarButton.setText("Agregar");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel9.add(agregarButton, gbc);
        modificarButton = new JButton();
        modificarButton.setPreferredSize(new Dimension(140, 30));
        modificarButton.setText("Modificar");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel9.add(modificarButton, gbc);
        eliminarButton = new JButton();
        eliminarButton.setPreferredSize(new Dimension(140, 30));
        eliminarButton.setText("Eliminar");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel9.add(eliminarButton, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(spacer7, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelPrincipal;
    }

}
