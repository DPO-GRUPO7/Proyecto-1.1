package modelo.venta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.juego.CopiaJuego;
import modelo.usuario.Cliente;
import modelo.usuario.Usuario;
import modelo.producto.itemVendible;

public class Venta {

    private List<itemVendible> items;
    private List<CopiaJuego> juegosVendidos;
    private double propina;
    private double total;
    private Usuario comprador;

    // NUEVO
    private LocalDate fecha;

    public Venta(Usuario comprador) {
        this.items = new ArrayList<>();
        this.juegosVendidos = new ArrayList<>();
        this.propina = 0.10;
        this.comprador = comprador;

        // NUEVO
        this.fecha = LocalDate.now();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public List<itemVendible> getItems() {
        return items;
    }

    public void setItems(List<itemVendible> items) {
        this.items = items;
    }

    public List<CopiaJuego> getJuegosVendidos() {
        return juegosVendidos;
    }

    public void setJuegosVendidos(List<CopiaJuego> juegosVendidos) {
        this.juegosVendidos = juegosVendidos;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public double getPropina() {
        return propina;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void agregarItem(itemVendible item) {
        items.add(item);
    }

    public void agregarJuego(CopiaJuego juego) {
        if (juego.aptoParaVenta()) {
            juegosVendidos.add(juego);
        } else {
            System.out.println("Este juego es solo para préstamos en mesa.");
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;

        for (itemVendible item : items) {
            subtotal += item.getPrecioBase();
        }

        for (CopiaJuego juego : juegosVendidos) {
            subtotal += juego.getPrecioventa();
        }

        return subtotal;
    }

    public double calcularImpuestos() {
        double impuestos = 0;

        for (itemVendible item : items) {
            impuestos += item.getPrecioFinal() * 0.08;
        }

        for (CopiaJuego juego : juegosVendidos) {
            impuestos += juego.getPrecioventa() * 0.19;
        }

        return impuestos;
    }

    public double calcularPropina() {
        return calcularSubtotal() * propina;
    }

    public double calcularTotal() {
        total = calcularSubtotal() + calcularImpuestos() + calcularPropina();
        return total;
    }

    public void generarPuntos() {
        if (this.comprador instanceof Cliente) {
            double puntos = calcularTotal() * 0.01;
            ((Cliente) this.comprador).acumularPuntos(puntos);
        }
    }

    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getTotal() {
        return total;
    }
}