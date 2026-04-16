package modelo.usuario;
import modelo.cafe.SolicitudCambioTurno;
import modelo.cafe.SugerenciaPlatillo;

public class Administrador extends Usuario{
	
	public Administrador(int id, String nombre, String login, String password) {
		super(id, nombre, login, password);
	}
	
	public void evaluarSugerencia(SugerenciaPlatillo sugerencia, boolean aprobar) {
        if (aprobar) {
            sugerencia.aprobar();
        } else {
            sugerencia.rechazar();
        }
    }
	public void autorizarCambioTurno(SolicitudCambioTurno solicitud, boolean aprobar) {
        if (aprobar) {
            solicitud.cambiarEstado("APROBADO");
        } else {
            solicitud.cambiarEstado("RECHAZADO");
        }
    }
}
