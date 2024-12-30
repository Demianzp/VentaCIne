package cine;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class UtilidadCine {
    private static final int FILAS = 10;
    private static final int COLUMNAS = 10;
    private static final JButton[][] asientos = new JButton[FILAS][COLUMNAS];
    private static final Set<String> asientosOcupados = new HashSet<>();
    // Conjunto de asientos ocupados
    private static String asientoSeleccionado = null;
    // Almacenar el asiento seleccionado
    public static void inicializarAsientos(String tipoEntrada) {
        // Crear la pantalla
        JLabel lblPantalla = new JLabel("-------------PELICULA--------------", SwingConstants.CENTER);
        lblPantalla.setFont(new Font("Arial", Font.BOLD, 20));
        lblPantalla.setOpaque(true);
        lblPantalla.setBackground(Color.RED);
        lblPantalla.setForeground(Color.BLACK);
        lblPantalla.setPreferredSize(new Dimension(400, 40));

        // Crear el panel de asientos
        JPanel panelAsientos = new JPanel(new GridLayout(FILAS, COLUMNAS, 5, 5));
        panelAsientos.setBackground(Color.BLACK);  // Fondo negro
        panelAsientos.removeAll();

        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                JButton asiento = new JButton();
                asiento.setPreferredSize(new Dimension(60, 60));
                String asientoID = "" + (char) ('A' + fila) + (columna + 1);

                if (asientosOcupados.contains(asientoID)) {
                    asiento.setBackground(Color.RED);
                    asiento.setEnabled(false);
                } else {
                    if (tipoEntrada.equals("General") && esZonaGeneral(fila, columna)) {
                        asiento.setBackground(Color.GREEN);
                    } else if (tipoEntrada.equals("Estudiante") && esZonaEstudiantes(fila, columna)) {
                        asiento.setBackground(Color.CYAN);
                    } else if (tipoEntrada.equals("VIP") && esZonaVIP(fila, columna)) {
                        asiento.setBackground(Color.YELLOW);
                    } else {
                        asiento.setBackground(Color.GRAY);
                        asiento.setEnabled(false);
                    }
                }

                asiento.setText(asientoID);
                asiento.setFont(new Font("Arial", Font.PLAIN, 14));
                asiento.setForeground(Color.BLACK);

                int finalFila = fila;
                int finalColumna = columna;
                asiento.addActionListener(_ -> seleccionarAsiento(finalFila, finalColumna, asientoID));

                asientos[fila][columna] = asiento;
                panelAsientos.add(asiento);
            }
        }

        // Crear un panel vacío para agregar separación
        JPanel separacion = new JPanel();
        separacion.setPreferredSize(new Dimension(400, 20)); // Ajusta el tamaño de la separación

        // Contenedor principal
        JPanel contenedorPrincipal = new JPanel(new BorderLayout());
        contenedorPrincipal.add(lblPantalla, BorderLayout.NORTH);
        contenedorPrincipal.add(separacion, BorderLayout.CENTER); // Agrega el espacio entre la pantalla y los asientos
        contenedorPrincipal.add(panelAsientos, BorderLayout.SOUTH); // Los asientos estarán en la parte inferior

        // Mostrar el marco
        JFrame frame = new JFrame("Plano de Asientos - Cine");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(contenedorPrincipal);
        frame.pack();
        frame.setVisible(true);
    }

    public static void seleccionarAsiento(int fila, int columna, String asientoID) {
        JButton asiento = asientos[fila][columna];

        // Verificar si el asiento ya está ocupado
        if (asientosOcupados.contains(asientoID)) {
            JOptionPane.showMessageDialog(null, "El asiento " + asientoID + " ya está reservado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;  // No hacer nada si el asiento ya está reservado
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Desea seleccionar el asiento " + asientoID + "?", "Confirmar Asiento", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            asiento.setBackground(Color.ORANGE);  // Cambiar el color a naranja cuando se selecciona
            asiento.setEnabled(false);
            asientoSeleccionado = asientoID;  // Almacenar el asiento seleccionado
        } else {
            // Si el usuario cancela, liberar el asiento
            liberarAsiento(asientoID);
        }
    }

    public static void liberarAsiento(String asientoID) {
        // Recuperar el botón correspondiente al asiento
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                if (asientos[fila][columna].getText().equals(asientoID)) {
                    JButton asiento = asientos[fila][columna];
                    // Solo liberar el asiento si no está ocupado permanentemente
                    if (!asientosOcupados.contains(asientoID)) {
                        asiento.setBackground(Color.GRAY);  // Restaurar color de fondo original
                        asiento.setEnabled(true);  // Hacerlo habilitado para selección
                        asientoSeleccionado = null;  // Limpiar la selección
                        return;
                    }
                }
            }
        }
    }

    // Zona VIP: De derecha a izquierda según la disposición de filas y columnas
    private static boolean esZonaVIP(int fila, int columna) {
        // Ajustar las reglas según las zonas VIP
        if (fila == 2) return columna >= 1 && columna <= 8;
        if (fila == 3) return columna >= 1 && columna <= 8;
        if (fila == 4) return columna >= 2 && columna <= 7;
        if (fila == 5) return columna >= 2 && columna <= 7;
        if (fila == 6) return columna >= 3 && columna <= 6;
        if (fila == 7) return columna >= 3 && columna <= 6;
        if (fila == 8) return columna >= 4 && columna <= 5;

        return false;
    }

    // Zona Estudiantes: Las primeras dos filas completas (0 a 9)
    private static boolean esZonaEstudiantes(int fila, int ignoredColumna) {
        return fila < 2; // Las primeras dos filas completas (0 y 1)
    }

    // Zona General: Asientos restantes que no son ni Estudiantes ni VIP
    private static boolean esZonaGeneral(int fila, int columna) {
        return !esZonaVIP(fila, columna) && !esZonaEstudiantes(fila, columna);
    }

    // Obtener el asiento seleccionado
    public static String getAsientoSeleccionado() {
        return asientoSeleccionado;
    }

    // Marcar un asiento como ocupado
    public static void marcarAsientoComoOcupado(String asientoID) {
        asientosOcupados.add(asientoID);  // Marca el asiento como ocupado
    }

}
