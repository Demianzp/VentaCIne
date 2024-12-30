package cine;

public abstract class Entrada {
    protected String tipo;
    protected int precioBase = 1600;
    protected int numeroAsiento;

    public Entrada(String tipo) {
        this.tipo = tipo;
    }

    public abstract int calcularPrecio();

    public String getTipo() {
        return tipo;
    }

    public int getNumeroAsiento() {
        return numeroAsiento;
    }
}