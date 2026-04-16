package modelo.usuario;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario{
	
	private double puntosFidelidad;
	private List<String> juegosFavoritos;
	
	
	public Cliente(int id, String nombre, String login, String password) {
		super(id, nombre, login, password);
		this.puntosFidelidad = 0;
		this.juegosFavoritos = new ArrayList<>();
		
	}
    public double getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void acumularPuntos(double valor) {
        this.puntosFidelidad += valor;
    }
    public void redimirPuntos(double valor) {
    	if (valor <= puntosFidelidad) {
    		puntosFidelidad -=valor;
    	}
    }
    public void agregarFavorito (String juego) {
    	juegosFavoritos.add(juego);
    }
    public List<String> getJuegosFavoritos (){
    	return juegosFavoritos;
    }

}
