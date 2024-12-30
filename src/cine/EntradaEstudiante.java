
package cine;

public class EntradaEstudiante extends EntradaGeneral {
    public EntradaEstudiante() {
        super();
        this.tipo = "Estudiante";
    }

    @Override
    public int calcularPrecio() {
        return (int) (super.calcularPrecio() * 0.8); // 20% descuento para estudiantes
    }
}
