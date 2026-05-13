package modelo.torneo;

import modelo.juego.Juego;
import modelo.usuario.Administrador;
import modelo.usuario.Usuario;

/**
 * Torneo sin costo de inscripción.
 * El ganador recibe un bono de descuento para su próxima compra.
 * El bono no es acumulable con otros descuentos (gestionado en Usuario).
 */
public class TorneoAmistoso extends Torneo {

    private double porcentajeDescuento; // ej: 0.15 = 15% de descuento

    public TorneoAmistoso(Juego juego, String dia, int maxParticipantes,
                          Administrador organizador, double porcentajeDescuento) {
        super(juego, dia, maxParticipantes, organizador);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Otorga el bono de descuento al ganador.
     * No hace nada si el usuario ya tiene un bono activo (no acumulable).
     * Retorna true si el bono fue asignado, false si el usuario ya tenía uno.
     */
    public boolean otorgarBono(Usuario ganador) {
        return ganador.asignarBono(porcentajeDescuento);
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    @Override
    public String getTipo() {
        return "Amistoso";
    }

    @Override
    public String toString() {
        return super.toString() + " | Descuento bono: " + (int)(porcentajeDescuento * 100) + "%";
    }
}
