package modelo.producto;

import java.util.ArrayList;
import java.util.List;

public class Pasteleria {

    private String nombre;
    private double precio;
    private List<String> alergenos;

    public Pasteleria( String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.alergenos = new ArrayList<>();
    }

    public void agregarAlergeno(String alergeno) {
        alergenos.add(alergeno);
    }

    public boolean tieneAlergeno(String alergeno) {
        return alergenos.contains(alergeno);
    }

    public double getPrecio() {
        return precio;
    }
}