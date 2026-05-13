package modelo.torneo;

import modelo.usuario.Usuario;

/**
 * Representa la inscripción de un usuario en un torneo.
 * Guarda cuántos cupos tomó y si usó cupos preferenciales (fanático).
 */
public class Inscripcion {

    private Usuario usuario;
    private int cuposTomados;         // 1 a 3
    private int cuposPreferenciales;  // cuántos de sus cupos son preferenciales

    public Inscripcion(Usuario usuario, int cuposTomados, int cuposPreferenciales) {
        this.usuario = usuario;
        this.cuposTomados = cuposTomados;
        this.cuposPreferenciales = cuposPreferenciales;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getCuposTomados() {
        return cuposTomados;
    }

    public int getCuposPreferenciales() {
        return cuposPreferenciales;
    }

    public int getCuposRegulares() {
        return cuposTomados - cuposPreferenciales;
    }
}
