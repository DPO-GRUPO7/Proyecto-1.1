package modelo.venta;

import java.util.ArrayList;
import java.util.List;

import modelo.producto.Bebida;
import modelo.producto.Pasteleria;
import modelo.juego.CopiaJuego;
import modelo.usuario.Cliente;

public class Venta {

    private List<Object> items;
    private double propina; // porcentaje (0.1 = 10%)
    private double total;
    private Cliente cliente;

    public Venta(Cliente cliente) {
        this.items = new ArrayList<>();
        this.propina = 0.10; // sugerida
        this.cliente = cliente;
    }

    public void agregarItem(Object item) {
        items.add(item);
    }

    
    public double calcularSubtotal() {
        double subtotal = 0;

        for (Object item : items) {

            if (item instanceof Bebida) {
                subtotal += ((Bebida) item).getPrecio();
            } 
            else if (item instanceof Pasteleria) {
                subtotal += ((Pasteleria) item).getPrecio();
            } 
            else if (item instanceof CopiaJuego) {
                subtotal += ((CopiaJuego) item).getPrecioVenta();
            }
        }

        return subtotal;
    }

    // impuestos (solo café)
    public double calcularImpuestos() {
        double impuestos = 0;

        for (Object item : items) {

            if (item instanceof Bebida) {
                impuestos += ((Bebida) item).getPrecio() * 0.08;
            } 
            else if (item instanceof Pasteleria) {
                impuestos += ((Pasteleria) item).getPrecio() * 0.08;
            }
        }

        return impuestos;
    }

    // propina
    public double calcularPropina() {
        return calcularSubtotal() * propina;
    }

    // total
    public double calcularTotal() {
        total = calcularSubtotal() + calcularImpuestos() + calcularPropina();
        return total;
    }

    // puntos
    public void generarPuntos() {
        double puntos = calcularTotal() * 0.01;
        cliente.acumularPuntos(puntos);
    }

    // (por si el cliente la cambia)
    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getTotal() {
        return total;
    }
}