package cine;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.Objects;


public class Validaciones {
    // Verifica que todos los campos estén llenos y que el tipo de entrada sea válido
    public static boolean validarCamposLlenos(InterfazCineApp interfaz) {
        if (interfaz.getTxtNombre().getText().trim().isEmpty() ||
                interfaz.getTxtApellido().getText().trim().isEmpty() ||
                interfaz.getTxtEdad().getText().trim().isEmpty() ||
                Objects.requireNonNull(interfaz.getCbTipoEntrada().getSelectedItem()).equals("Elige una Opción") ||
                UtilidadCine.getAsientoSeleccionado() == null) {  // Verificación del asiento
            JOptionPane.showMessageDialog(null,
                    "Todos los campos deben estar llenos, debe seleccionar un tipo de entrada válido y un asiento.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    // Muestra el resumen de compra antes de confirmar
    public static boolean mostrarResumenCompra(String resumen) {
        int confirmacion = JOptionPane.showConfirmDialog(null,
                resumen,
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        return confirmacion == JOptionPane.YES_OPTION; // Devuelve true si el usuario acepta
    }
    public static void aplicarValidaciones(JTextField txtNombre, JTextField txtApellido, JTextField txtEdad) {
        // Solo letras para Nombre y Apellido
        DocumentFilter soloLetras = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text.matches("[a-zA-Z\\sáéíóúÁÉÍÓÚ]+")) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
                if (text.matches("[a-zA-Z\\sáéíóúÁÉÍÓÚ]+")) {
                    super.replace(fb, offset, length, text, attr);
                }
            }
        };
        DocumentFilter soloNumeros = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text.matches("\\d+")) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
                if (text.matches("\\d+")) {
                    super.replace(fb, offset, length, text, attr);
                }
            }
        };

        // Restablecer el filtro cuando los valores se reinician
        ((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(null);
        ((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(null);
        ((AbstractDocument) txtEdad.getDocument()).setDocumentFilter(null);

        // Restablecer los campos
        txtNombre.setText("");
        txtApellido.setText("");
        txtEdad.setText("");

        // Aplicar los filtros nuevamente
        ((AbstractDocument) txtNombre.getDocument()).setDocumentFilter(soloLetras);
        ((AbstractDocument) txtApellido.getDocument()).setDocumentFilter(soloLetras);
        ((AbstractDocument) txtEdad.getDocument()).setDocumentFilter(soloNumeros);
    }

    public static void nuevaCompra(InterfazCineApp interfaz) {
        // Restablecer los campos de texto
        aplicarValidaciones(interfaz.getTxtNombre(), interfaz.getTxtApellido(), interfaz.getTxtEdad());

        // Restablecer otros componentes
        interfaz.getCbTipoEntrada().setSelectedIndex(0);
        interfaz.getLblPrecio().setText("Precio: $0");
        interfaz.getLblAsiento().setText("Asiento: -");
        interfaz.aplicarColorFondo();

        // Lógica adicional
        interfaz.getBtnConfirmar().setEnabled(false); // Desactivar el botón de confirmación
        // Mostrar asientos ocupados si corresponde
        String tipoEntrada = Objects.requireNonNull(interfaz.getCbTipoEntrada().getSelectedItem()).toString();
        if (!tipoEntrada.equals("Elige una Opción")) {
            UtilidadCine.inicializarAsientos(tipoEntrada);
        }
    }

}
