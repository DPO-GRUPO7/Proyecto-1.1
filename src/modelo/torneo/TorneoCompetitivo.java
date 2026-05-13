package modelo.torneo;

import modelo.juego.Juego;
import modelo.usuario.Administrador;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

/**
 * Torneo con tarifa de inscripción y premio monetario.
 * Los empleados participan gratis pero NO reciben el premio en metálico.
 */
public class TorneoCompetitivo extends Torneo {

    private double tarifaInscripcion;

    public TorneoCompetitivo(Juego juego, String dia, int maxParticipantes,
                              Administrador organizador, double tarifaInscripcion) {
        super(juego, dia, maxParticipantes, organizador);
        this.tarifaInscripcion = tarifaInscripcion;
    }

    /**
     * Los empleados participan gratis.
     */
    public double getTarifaParaUsuario(Usuario usuario) {
        if (usuario instanceof Empleado) {
            return 0.0;
        }
        return tarifaInscripcion;
    }

    /**
     * El premio se calcula con base en lo recaudado solo de clientes
     * (los empleados no pagan, por tanto no aportan al premio).
     */
    public double calcularPremio() {
        double recaudado = 0.0;
        for (Inscripcion i : getInscripciones()) {
            recaudado += getTarifaParaUsuario(i.getUsuario()) * i.getCuposTomados();
        }
        return recaudado;
    }

    /**
     * Verifica si el ganador puede recibir el premio monetario.
     * Los empleados NO pueden recibirlo.
     */
    public boolean puedeRecibirPremio(Usuario ganador) {
        return !(ganador instanceof Empleado);
    }

    public double getTarifaInscripcion() {
        return tarifaInscripcion;
    }

    @Override
    public String getTipo() {
        return "Competitivo";
    }

    @Override
    public String toString() {
        return super.toString() + " | Tarifa: $" + tarifaInscripcion
                + " | Premio acumulado: $" + calcularPremio();
    }
}
