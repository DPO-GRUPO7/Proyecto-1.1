package modelo.producto;

public abstract class itemVendible {
	private int idItem;
	private String nombre;
	private double precioBase;
	
	
	public itemVendible(int idItem, String nombre, double precioBase) {
		super();
		this.idItem = idItem;
		this.nombre = nombre;
		this.precioBase = precioBase;
	}

	

	public abstract double getPrecioFinal();



	public int getIdItem() {
		return idItem;
	}



	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public double getPrecioBase() {
		return precioBase;
	}



	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	
}
