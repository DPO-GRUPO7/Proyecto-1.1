package modelo.usuario;

import modelo.cafe.Turno;
import modelo.cafe.SolicitudCambioTurno;

public abstract class Empleado extends Usuario{

	protected Turno turno;
	
	public Empleado(int id, String nombre, String login, String password, Turno turno) {
		super(id, nombre, login, password);
		this.turno = turno;
	}
	public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Turno consultarTurno() {
        return turno;
    }

    public SolicitudCambioTurno solicitarCambioTurno(Turno nuevoTurno) {
        return new SolicitudCambioTurno(this, nuevoTurno);
    }
}


