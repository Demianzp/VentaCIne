package cine;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InterfazCineApp extends JFrame {
    // Componentes de la interfaz
    private final JTextField txtNombre, txtApellido, txtEdad;
    private final JComboBox<String> cbTipoEntrada;
    private final JLabel lblPrecio, lblAsiento, lblResumen;
    private final JButton btnCalcular;
    private final JButton btnConfirmar;
    private final JButton btnNuevaCompra;
    private final JButton btnConsultarCompras;
    //-----------------------------------------------------------------
    public InterfazCineApp() {
        // Configuración de la ventana
        setTitle("Venta de Entradas - Cine");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10, 4, 8, 8));

        // Configuración de colores básicos
        Color fondo = new Color(211, 211, 211); // Gris claro
        getContentPane().setBackground(fondo);

        // Componentes principales
        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        add(txtApellido);

        add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        add(txtEdad);
        add(new JLabel("Tipo de Entrada:"));
        cbTipoEntrada = new JComboBox<>(new String[]{"Elige una Opción", "General", "VIP", "Estudiante"});
        cbTipoEntrada.addActionListener(_ -> aplicarColorFondo());

        add(cbTipoEntrada);

        // Botón para calcular el precio
        btnCalcular = new JButton("Calcular Precio");
        btnCalcular.addActionListener(_ -> calcularPrecio());
        btnCalcular.setBackground(Color.GRAY);
        btnCalcular.setForeground(Color.BLACK); //Aplica color al texto

        add(btnCalcular);

        lblPrecio = new JLabel("Precio: $0");
        add(lblPrecio);

        // Botón para elegir asiento
        JButton btnElegirAsiento = new JButton("Elegir Asiento");
        btnElegirAsiento.addActionListener(_ -> elegirAsiento());
        btnElegirAsiento.setBackground(Color.decode("#4682B4"));
        btnElegirAsiento.setForeground(Color.BLACK);
        add(btnElegirAsiento);

        // Establecer el color de texto negro para botones deshabilitados
        UIManager.put("Button.disabledText", Color.BLACK);

        btnConfirmar = new JButton("Confirmar Compra");
        btnConfirmar.setEnabled(false); // Deshabilitar el botón al inicio
        btnConfirmar.addActionListener(_ -> confirmarCompra());
        btnConfirmar.setBackground(Color.decode("#FF8C00")); // Color de fondo naranja
        btnConfirmar.setForeground(Color.BLACK); // Asegurarse de que el texto sea negro cuando el botón está habilitado
        add(btnConfirmar);



        lblAsiento = new JLabel("Asiento: -");
        add(lblAsiento);

        // Botón para iniciar una nueva compra
        btnNuevaCompra = new JButton("Nueva Compra");
        btnNuevaCompra.addActionListener(_ -> nuevaCompra());
        btnNuevaCompra.setBackground(Color.decode("#4682B4"));
        btnNuevaCompra.setForeground(Color.BLACK);
        add(btnNuevaCompra);

        lblResumen = new JLabel("Resumen: -");
        add(lblResumen);

        // Botón para consultar las compras
        btnConsultarCompras = new JButton("Consultar Compras");
        btnConsultarCompras.setBackground(Color.GRAY);
        btnConsultarCompras.setForeground(Color.BLACK);
        add(btnConsultarCompras);
        Validaciones.aplicarValidaciones(txtNombre, txtApellido, txtEdad);
    }

    // Método para cambiar el color de fondo según el tipo de entrada
    public void aplicarColorFondo() {
        Object selectedItem = cbTipoEntrada.getSelectedItem();
        //--cbTipoEntrada.getSelectedItem(): Obtiene el objeto seleccionado del combo box cbTipoEntrada.
        String tipo = (selectedItem != null) ? selectedItem.toString() : "";
        //--Objects.requireNonNull(): Verifica que el valor obtenido no sea null.
        // Si lo es, lanza una excepción NullPointerException.
        Color color = switch (tipo) {
            case "General" -> new Color(144, 238, 144); // Verde
            case "VIP" -> new Color(255, 255, 102); // Amarillo
            case "Estudiante" -> new Color(173, 216, 230); // Celeste
            default -> new Color(240, 240, 240); // Fondo básico
        };

        getContentPane().setBackground(color);
        repaint();
    }

    // Método para calcular el precio (simulado)
    public void calcularPrecio() {
        String tipoEntrada = Objects.requireNonNull(cbTipoEntrada.getSelectedItem()).toString();
        try {
            switch (tipoEntrada) {
                case "General" -> new EntradaGeneral();
                case "VIP" ->  new EntradaVIP();
                case "Estudiante" -> new EntradaEstudiante();
                default -> throw new IllegalArgumentException("Tipo de entrada no válido");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Método para elegir asiento
    private void elegirAsiento() {
        String tipoEntrada = Objects.requireNonNull(cbTipoEntrada.getSelectedItem()).toString();
        if (tipoEntrada.equals("Elige una Opción")) {
            JOptionPane.showMessageDialog(this, "Por favor, elija un tipo de entrada primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Abrir el plano de asientos
        UtilidadCine.inicializarAsientos(tipoEntrada);
        // Habilitar el botón de confirmar compra después de seleccionar un asiento
        btnConfirmar.setEnabled(true);
    }

    // Método para confirmar la compra
    private void confirmarCompra() {
        String asientoSeleccionado = UtilidadCine.getAsientoSeleccionado();
        if (asientoSeleccionado != null) {
            lblAsiento.setText("Asiento: " + asientoSeleccionado);
            lblResumen.setText("Compra Confirmada! " + asientoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, elija un asiento primero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void nuevaCompra() {
        //Llama al método estático de Validaciones
        Validaciones.nuevaCompra(this);
    }
    // Metodo para Mostrar la ventana
    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
    // Getters para los componentes
    public JTextField getTxtNombre() {
        return txtNombre;
    }
    public JTextField getTxtApellido() {
        return txtApellido;
    }
    public JTextField getTxtEdad() {
        return txtEdad;
    }
    public JComboBox<String> getCbTipoEntrada() {
        return cbTipoEntrada;
    }
    public JLabel getLblPrecio() {
        return lblPrecio;
    }
    public JLabel getLblAsiento() {
        return lblAsiento;
    }
    public JLabel getLblResumen() {
        return lblResumen;
    }
    public JButton getBtnCalcular() {
        return btnCalcular;
    }
    public JButton getBtnConfirmar() {
        return btnConfirmar;
    }
    public JButton getBtnNuevaCompra() {
        return btnNuevaCompra;
    }
    public JButton getBtnConsultarCompras() {
        return btnConsultarCompras;
    }
}