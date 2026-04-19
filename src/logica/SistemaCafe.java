package logica;

import java.util.ArrayList;
import java.util.List;

import modelo.usuario.Cliente;
import modelo.venta.Venta;
import modelo.prestamo.Prestamo;
import modelo.juego.CopiaJuego;
import modelo.cafe.Mesa;

public class SistemaCafe {

    private List<Cliente> clientes;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<CopiaJuego> inventarioJuegos;

    public SistemaCafe() {
        clientes = new ArrayList<>();
        ventas = new ArrayList<>();
        prestamos = new ArrayList<>();
        inventarioJuegos = new ArrayList<>();
    }

    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public Cliente buscarCliente(String login) {
        for (Cliente c : clientes) {
            if (c.getLogin().equals(login)) {
                return c;
            }
        }
        return null;
    }


    public Venta crearVenta(Cliente cliente) {
        return new Venta(cliente);
    }

    public void registrarVenta(Venta venta) {
        venta.calcularTotal();
        venta.generarPuntos();
        ventas.add(venta);
    }

    public List<Venta> consultarVentas() {
        return ventas;
    }


    public Prestamo crearPrestamo(Mesa mesa) {
        return new Prestamo(mesa);
    }

    public boolean iniciarPrestamo(Prestamo prestamo) {
        if (prestamo.validarRestricciones()) {
            prestamo.iniciarPrestamo();
            prestamos.add(prestamo);
            return true;
        }
        return false;
    }


    public void agregarJuegoInventario(CopiaJuego juego) {
        inventarioJuegos.add(juego);
    }

    public List<CopiaJuego> getInventarioJuegos() {
        return inventarioJuegos;
    }

    public boolean hayDisponibles() {
        for (CopiaJuego juego : inventarioJuegos) {
            if (juego.estaDisponible()) {
                return true;
            }
        }
        return false;
    }


    public void guardarDatos() {
        
    }

    public void cargarDatos() {
        
    }
}