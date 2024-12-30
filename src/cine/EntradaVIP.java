
package cine;

public class EntradaVIP extends Entrada {
    public EntradaVIP() {
        super("VIP");
    }

    @Override
    public int calcularPrecio() {
        return (int) (precioBase * 1.5); // 50% extra por ser VIP
    }

}
