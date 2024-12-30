package cine;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CineApp {
    private final InterfazCineApp interfaz;
    private Entrada entrada;
    private final List<String> historialCompras = new ArrayList<>(); // Historial de compras

    public CineApp() {
        interfaz = new InterfazCineApp();
        // Configurar acciones de botones
        interfaz.getBtnCalcular().addActionListener(this::calcularPrecio);
        interfaz.getBtnConfirmar().addActionListener(this::confirmarCompra);
        interfaz.getBtnNuevaCompra().addActionListener(this::nuevaCompra);
        interfaz.getBtnConsultarCompras().addActionListener(this::consultarCompras);
    }
    private void confirmarCompra(ActionEvent e) {
        // Validar que los campos estén llenos
        if (!Validaciones.validarCamposLlenos(interfaz)) {
            return;
        }

        try {
            String nombre = interfaz.getTxtNombre().getText();
            String apellido = interfaz.getTxtApellido().getText();
            int edad = Integer.parseInt(interfaz.getTxtEdad().getText());
            Persona cliente = new Persona(nombre, apellido, edad) {}; // Instanciación

            // Obtener el asiento seleccionado
            String asiento = UtilidadCine.getAsientoSeleccionado();
            if (asiento == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un asiento antes de confirmar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear el resumen de la compra
            String resumen = "Cliente: " + cliente.getNombre() + " " + cliente.getApellido() +
                    "\nEntrada: " + entrada.getTipo() +
                    "\nPrecio: $" + entrada.calcularPrecio() +
                    "\nAsiento: " + asiento;

            // Mostrar resumen al usuario para confirmación
            if (!Validaciones.mostrarResumenCompra(resumen)) {
                // Si el usuario cancela la compra, liberar el asiento
                UtilidadCine.liberarAsiento(asiento);
                return; // No se confirma la compra
            }

            // Registrar la compra
            historialCompras.add(resumen);
            UtilidadCine.marcarAsientoComoOcupado(asiento);  // Marca el asiento como ocupado permanentemente

            interfaz.getLblResumen().setText("Resumen: Compra registrada.");

            JOptionPane.showMessageDialog(null, resumen, "Compra Confirmada", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Edad inválida. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarCompras(ActionEvent e) {
        if (historialCompras.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay compras registradas.", "Historial Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crear un resumen de todas las compras
        StringBuilder resumenCompras = new StringBuilder("Historial de Compras:\n\n");
        for (int i = 0; i < historialCompras.size(); i++) {
            resumenCompras.append("Compra ").append(i + 1).append(":\n");
            resumenCompras.append(historialCompras.get(i)).append("\n");
            // No agregar número de asiento de nuevo, ya está en el resumen
            resumenCompras.append("\n");
        }

        // Mostrar el resumen en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, resumenCompras.toString(), "Historial de Compras", JOptionPane.INFORMATION_MESSAGE);
    }

    private void calcularPrecio(ActionEvent e) {
        try {
            // Crear un mapa para manejar los tipos de entradas
            Map<String, Supplier<Entrada>> entradaMap = new HashMap<>();
            entradaMap.put("General", EntradaGeneral::new);
            entradaMap.put("VIP", EntradaVIP::new);
            entradaMap.put("Estudiante", EntradaEstudiante::new);

            // Obtener el tipo seleccionado, manejando posibles valores null
            Object selectedItem = interfaz.getCbTipoEntrada().getSelectedItem();
            String tipo = (selectedItem != null) ? selectedItem.toString() : "";

            // Crear la entrada usando el mapa
            entrada = entradaMap.getOrDefault(tipo, () -> {
                throw new IllegalArgumentException("Tipo de entrada no válido: " + tipo);
            }).get();

            // Aquí ya puedes calcular el precio
            interfaz.getLblPrecio().setText("Precio: $" + entrada.calcularPrecio());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al calcular precio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void nuevaCompra(ActionEvent e) {
        Validaciones.nuevaCompra(interfaz);
        entrada = null; // Reinicia la entrada
    }

    public static void main(String[] args) {
        //Aca se llama al metodo mostrarVentana
        SwingUtilities.invokeLater(() -> new CineApp().interfaz.mostrarVentana());
    }

}

