package modelo.producto;

public class Pasteleria extends itemVendible {

	private boolean esAlergeno;

	public Pasteleria(int idItem, String nombre, double precioBase, boolean esAlergeno) {
		super(idItem, nombre, precioBase);
		this.esAlergeno = esAlergeno;
	}

	@Override
	public double getPrecioFinal() {
		// TODO Auto-generated method stub
		return getPrecioBase();
	}

	public boolean isEsAlergeno() {
		return esAlergeno;
	}

	public void setEsAlergeno(boolean esAlergeno) {
		this.esAlergeno = esAlergeno;
	}

	
}
