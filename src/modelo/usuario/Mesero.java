package modelo.usuario;
import modelo.cafe.Turno;

public class Mesero extends Empleado{
	
	private boolean disponible;
	
	public Mesero(int id, String nombre, String login, String password, Turno turno) {
		super(id, nombre, login, password, turno);
		this.disponible = true;
	}
	public boolean estaDisponible() {
        return disponible;
    }
	public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
	public void ocupar() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }
}
