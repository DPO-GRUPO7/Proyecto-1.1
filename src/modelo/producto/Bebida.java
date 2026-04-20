package modelo.producto;

public class Bebida extends itemVendible{
	private boolean esAlcoholica;
	private boolean esCaliente;
	public Bebida(int idItem, String nombre, double precioBase, boolean esAlcoholica, boolean esCaliente) {
		super(idItem, nombre, precioBase);
		this.esAlcoholica = esAlcoholica;
		this.esCaliente = esCaliente;
	}
	@Override
	public double getPrecioFinal() {
		// TODO Auto-generated method stub
		return getPrecioBase();
	}
	public boolean isEsAlcoholica() {
		return esAlcoholica;
	}
	public void setEsAlcoholica(boolean esAlcoholica) {
		this.esAlcoholica = esAlcoholica;
	}
	public boolean isEsCaliente() {
		return esCaliente;
	}
	public void setEsCaliente(boolean esCaliente) {
		this.esCaliente = esCaliente;
	}
	
	
	
}
