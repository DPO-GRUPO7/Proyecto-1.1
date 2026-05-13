package modelo.torneo;

import java.util.ArrayList;
import java.util.List;

import modelo.juego.Juego;
import modelo.usuario.Administrador;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

/**
 * Clase abstracta que representa un torneo en el Board Game Café.
 * Gestiona cupos, inscripciones y validaciones comunes a ambos tipos de torneo.
 */
public abstract class Torneo {

    private static int contadorId = 1;

    private int idTorneo;
    private Juego juego;
    private String dia;
    private int maxParticipantes;
    private Administrador organizador;
    private List<Inscripcion> inscripciones;

    public Torneo(Juego juego, String dia, int maxParticipantes, Administrador organizador) {
        this.idTorneo = contadorId++;
        this.juego = juego;
        this.dia = dia;
        this.maxParticipantes = maxParticipantes;
        this.organizador = organizador;
        this.inscripciones = new ArrayList<>();
    }

    // --- Cálculo de cupos ---

    // 20% redondeado hacia arriba
    public int calcularCuposPreferenciales() {
        return (int) Math.ceil(maxParticipantes * 0.20);
    }

    public int cuposTotalesOcupados() {
        int total = 0;
        for (Inscripcion i : inscripciones) {
            total += i.getCuposTomados();
        }
        return total;
    }

    public int cuposPreferencialesOcupados() {
        int total = 0;
        for (Inscripcion i : inscripciones) {
            total += i.getCuposPreferenciales();
        }
        return total;
    }

    public int cuposDisponibles() {
        return maxParticipantes - cuposTotalesOcupados();
    }

    public int cuposPreferencialesDisponibles() {
        return calcularCuposPreferenciales() - cuposPreferencialesOcupados();
    }

    // --- Consultas de fanático ---

    public boolean esFanatico(Usuario usuario) {
        if (usuario instanceof Cliente) {
            return ((Cliente) usuario).esFanaticoDe(juego);
        }
        return false;
    }

    // --- Buscar inscripción existente de un usuario ---

    public Inscripcion buscarInscripcion(Usuario usuario) {
        for (Inscripcion i : inscripciones) {
            if (i.getUsuario().getId() == usuario.getId()) {
                return i;
            }
        }
        return null;
    }

    // --- Validaciones previas a inscribir ---

    /**
     * Valida si el usuario puede inscribirse con la cantidad de cupos pedida.
     * Devuelve un mensaje de error, o null si todo está bien.
     */
    public String validarInscripcion(Usuario usuario, int cuposPedidos) {
        if (cuposPedidos < 1 || cuposPedidos > 3) {
            return "Solo puede inscribir entre 1 y 3 participantes.";
        }
        if (buscarInscripcion(usuario) != null) {
            return "El usuario ya está inscrito en este torneo.";
        }
        if (cuposDisponibles() < cuposPedidos) {
            return "No hay suficientes cupos disponibles. Disponibles: " + cuposDisponibles();
        }
        // Validación específica para empleados: no pueden tener turno ese día
        if (usuario instanceof Empleado) {
            Empleado emp = (Empleado) usuario;
            if (emp.getTurno() != null && emp.getTurno().getDia().equalsIgnoreCase(this.dia)) {
                return "El empleado tiene turno el día del torneo (" + this.dia + ") y no puede inscribirse.";
            }
        }
        return null; // sin errores
    }

    /**
     * Inscribe al usuario en el torneo.
     * Distribuye cupos: primero preferenciales (si es fanático y hay disponibles),
     * luego regulares.
     * Retorna null si tuvo éxito, o un mensaje de error si falló.
     */
    public String inscribir(Usuario usuario, int cuposPedidos) {
        String error = validarInscripcion(usuario, cuposPedidos);
        if (error != null) {
            return error;
        }

        int cuposPreferencialesTomados = 0;

        if (esFanatico(usuario)) {
            // Toma cupos preferenciales disponibles primero
            int prefDisponibles = cuposPreferencialesDisponibles();
            cuposPreferencialesTomados = Math.min(cuposPedidos, prefDisponibles);
            // Si los preferenciales se acabaron, el resto va a cupos regulares (ya validado arriba)
        }

        inscripciones.add(new Inscripcion(usuario, cuposPedidos, cuposPreferencialesTomados));
        return null; // éxito
    }

    /**
     * Desinscribe al usuario eliminando TODOS sus cupos.
     * Retorna true si estaba inscrito, false si no.
     */
    public boolean desinscribir(Usuario usuario) {
        Inscripcion inscripcion = buscarInscripcion(usuario);
        if (inscripcion == null) {
            return false;
        }
        inscripciones.remove(inscripcion);
        return true;
    }

    // --- Getters ---

    public int getIdTorneo() {
        return idTorneo;
    }

    public Juego getJuego() {
        return juego;
    }

    public String getDia() {
        return dia;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public Administrador getOrganizador() {
        return organizador;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    // Devuelve una descripción del tipo de torneo (implementado por subclases)
    public abstract String getTipo();

    @Override
    public String toString() {
        return "[" + idTorneo + "] Torneo " + getTipo() + " | Juego: " + juego.getNombre()
                + " | Día: " + dia + " | Cupos: " + cuposTotalesOcupados() + "/" + maxParticipantes;
    }
}
