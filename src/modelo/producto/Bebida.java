package modelo.producto;

public class Bebida {
    private String nombre;
    private double precio;
    private boolean alcoholica;
    private boolean caliente;

    public Bebida( String nombre, double precio, boolean alcoholica, boolean caliente) {
        this.nombre = nombre;
        this.precio = precio;
        this.alcoholica = alcoholica;
        this.caliente = caliente;
    }

    public boolean esAlcoholica() {
        return alcoholica;
    }

    public boolean esCaliente() {
        return caliente;
    }

    public double getPrecio() {
        return precio;
    }

}
