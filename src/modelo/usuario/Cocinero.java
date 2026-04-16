package modelo.usuario;
import modelo.cafe.Turno;

public class Cocinero extends Empleado{

	public Cocinero(int id, String nombre, String login, String password, Turno turno) {
		super(id, nombre, login, password, turno);
	}

}
