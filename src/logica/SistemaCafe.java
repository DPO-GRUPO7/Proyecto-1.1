package logica;

import java.util.ArrayList;
import java.util.List;

import modelo.cafe.Mesa;
import modelo.juego.CopiaJuego;
import modelo.juego.Juego;
import modelo.prestamo.Prestamo;
import modelo.torneo.Inscripcion;
import modelo.torneo.Torneo;
import modelo.torneo.TorneoAmistoso;
import modelo.torneo.TorneoCompetitivo;
import modelo.usuario.Administrador;
import modelo.usuario.Usuario;
import modelo.venta.Venta;
import persistencia.Persistencia;

public class SistemaCafe {

    private List<Usuario> Usuarios;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<CopiaJuego> inventarioJuegos;
    private List<Torneo> torneos; 

    public SistemaCafe() {
        Usuarios = new ArrayList<>();
        ventas = new ArrayList<>();
        prestamos = new ArrayList<>();
        inventarioJuegos = new ArrayList<>();
        torneos = new ArrayList<>();
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

    public String crearTorneoAmistoso(Juego juego, String dia, int maxParticipantes,
                                      Administrador organizador, double porcentajeDescuento) {
        String error = validarCapacidadTorneo(juego, maxParticipantes);
        if (error != null) return error;

        TorneoAmistoso torneo = new TorneoAmistoso(juego, dia, maxParticipantes,
                organizador, porcentajeDescuento);
        torneos.add(torneo);
        return null; 
    }

    public String crearTorneoCompetitivo(Juego juego, String dia, int maxParticipantes,
                                         Administrador organizador, double tarifaInscripcion) {
        String error = validarCapacidadTorneo(juego, maxParticipantes);
        if (error != null) return error;

        TorneoCompetitivo torneo = new TorneoCompetitivo(juego, dia, maxParticipantes,
                organizador, tarifaInscripcion);
        torneos.add(torneo);
        return null; 
    }

    
    private String validarCapacidadTorneo(Juego juego, int maxParticipantes) {
        int copiasDisponibles = 0;
        for (CopiaJuego copia : inventarioJuegos) {
            if (copia.getJuego() != null
                    && copia.getJuego().getNombre().equalsIgnoreCase(juego.getNombre())
                    && copia.estaDisponible()) {
                copiasDisponibles++;
            }
        }
        int capacidadMaxima = copiasDisponibles * juego.getMaxJugadores();
        if (maxParticipantes > capacidadMaxima) {
            return "No hay suficientes copias del juego '" + juego.getNombre()
                    + "' para soportar " + maxParticipantes + " participantes. "
                    + "Capacidad máxima con inventario actual: " + capacidadMaxima;
        }
        return null;
    }

    public String inscribirEnTorneo(int idTorneo, Usuario usuario, int cuposPedidos) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) {
            return "No existe un torneo con id " + idTorneo;
        }
        return torneo.inscribir(usuario, cuposPedidos);
    }
    public String desinscribirDeTorneo(int idTorneo, Usuario usuario) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) {
            return "No existe un torneo con id " + idTorneo;
        }
        boolean eliminado = torneo.desinscribir(usuario);
        if (!eliminado) {
            return "El usuario no estaba inscrito en ese torneo.";
        }
        return null; 
    }

    public String declararGanadorAmistoso(int idTorneo, Usuario ganador) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) return "No existe un torneo con id " + idTorneo;
        if (!(torneo instanceof TorneoAmistoso)) return "El torneo no es amistoso.";
        if (torneo.buscarInscripcion(ganador) == null) return "El ganador no estaba inscrito.";

        TorneoAmistoso amistoso = (TorneoAmistoso) torneo;
        boolean asignado = amistoso.otorgarBono(ganador);
        if (!asignado) {
            return "El usuario ya tiene un bono activo. No se asignó uno nuevo (no acumulable).";
        }
        return null; 
    }

    
    public double declararGanadorCompetitivo(int idTorneo, Usuario ganador) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null || !(torneo instanceof TorneoCompetitivo)) return -1.0;
        if (torneo.buscarInscripcion(ganador) == null) return -1.0;

        TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;
        if (!competitivo.puedeRecibirPremio(ganador)) {
            return 0.0; // empleado participa pero no recibe premio
        }
        return competitivo.calcularPremio();
    }

    public Torneo buscarTorneo(int idTorneo) {
        for (Torneo t : torneos) {
            if (t.getIdTorneo() == idTorneo) {
                return t;
            }
        }
        return null;
    }

    public List<Torneo> getTorneos() {
        return torneos;
    }

    public void setTorneos(List<Torneo> torneos) {
        this.torneos = torneos;
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
