package modelo.venta;

import java.util.ArrayList;
import java.util.List;


import modelo.juego.CopiaJuego;
import modelo.usuario.Cliente;
import modelo.usuario.Usuario;
import modelo.producto.itemVendible;


public class Venta {

    private List<itemVendible> items;
    private List<CopiaJuego> juegosVendidos;
    private double propina; // porcentaje (0.1 = 10%)
    private double total;
    private Usuario cliente;

    public Venta(Usuario cliente) {
        this.items = new ArrayList<>();
        this.juegosVendidos = new ArrayList<>();
        this.propina = 0.10; // sugerida
        this.cliente = cliente;
    }
    
    public List<itemVendible> getItems() {
		return items;
	}

	public void setItems(List<itemVendible> items) {
		this.items = items;
	}

	public List<CopiaJuego> getJuegosVendidos() {
		return juegosVendidos;
	}

	public void setJuegosVendidos(List<CopiaJuego> juegosVendidos) {
		this.juegosVendidos = juegosVendidos;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public double getPropina() {
		return propina;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void agregarItem(itemVendible item) {
        items.add(item);
    }
    // Agregamos juegos
    public void agregarJuego(CopiaJuego juego) {
       
        if (juego.aptoParaVenta()) {
            juegosVendidos.add(juego);
        } else {
            System.out.println("Este juego es solo para préstamos en mesa.");
        }
    }

    
    public double calcularSubtotal() {
        double subtotal = 0;
        
        // Sumamos los productos de la cafetería
        for (itemVendible item : items) {
            subtotal += item.getPrecioBase(); 
        }
        
        // Sumamos los juegos físicos (usando el precioVenta de tu UML)
        for (CopiaJuego juego : juegosVendidos) {
            subtotal += juego.getPrecioventa(); 
        }
        
        return subtotal;
    }

    // impuestos (solo café)
    public double calcularImpuestos() {
        double impuestos = 0;

        for (itemVendible item : items) {
        	impuestos += ((itemVendible) item).getPrecioFinal() * 0.08;
          
        }
        for (CopiaJuego juego : juegosVendidos) {
            impuestos += juego.getPrecioventa() * 0.19;
        }
        return impuestos;
    }


    // propina
    public double calcularPropina() {
        return calcularSubtotal() * propina;
    }

    // total
    public double calcularTotal() {
        total = calcularSubtotal() + calcularImpuestos() + calcularPropina();
        return total;
    }

    // puntos
    public void generarPuntos() {
       
        if (this.cliente instanceof Cliente) {
            double puntos = calcularTotal() * 0.01;
            ((Cliente) this.cliente).acumularPuntos(puntos);
        }
    }
    // (por si el cliente la cambia)
    public void setPropina(double propina) {
        this.propina = propina;
    }

    public double getTotal() {
        return total;
    }
}