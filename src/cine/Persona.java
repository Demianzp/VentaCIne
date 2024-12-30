
package cine;

public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected int edad;

    public Persona(String nombre, String apellido, int edad) {
        if (edad < 18) {
            throw new IllegalArgumentException("La edad debe ser mayor o igual a 18 años.");
        }
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;

    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
