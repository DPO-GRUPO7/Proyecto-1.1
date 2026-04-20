package logica;

import java.util.ArrayList;
import java.util.List;

import modelo.usuario.Usuario;
import modelo.venta.Venta;
import persistencia.Persistencia;
import modelo.prestamo.Prestamo;
import modelo.juego.CopiaJuego;
import modelo.cafe.Mesa;

public class SistemaCafe {

    private List<Usuario> Usuarios;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<CopiaJuego> inventarioJuegos;

    public SistemaCafe() {
        Usuarios = new ArrayList<>();
        ventas = new ArrayList<>();
        prestamos = new ArrayList<>();
        inventarioJuegos = new ArrayList<>();
    }
    public List<Usuario> getUsuarios() {
        return this.Usuarios;
        
       
    }
    public void setUsuarios(List<Usuario> usuariosRecuperados) {
        this.Usuarios = usuariosRecuperados;
    }
    public void agregarCliente(Usuario cliente) {
        Usuarios.add(cliente);
    }

    public Usuario buscarCliente(String login) {
        for (Usuario c : Usuarios) {
            if (c.getLogin().equals(login)) {
                return c;
            }
        }
        return null;
    }


    public Venta crearVenta(Usuario usuario) {
        return new Venta(usuario);
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
    
        Persistencia p = new Persistencia("data/", "usuarios.json", "juegos.json", "ventas.json");
        p.guardarEstadoCompleto(this);
    }

    public void cargarDatos() {
   
        Persistencia p = new Persistencia("data/", "usuarios.json", "juegos.json", "ventas.json");
        p.cargarEstadoCompleto(this);
    }
}