package cine;

public class EntradaGeneral extends Entrada {
    public EntradaGeneral() {
        super("General");
    }

    @Override
    public int calcularPrecio() {
        return precioBase;
    }

}
