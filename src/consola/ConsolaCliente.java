package consola;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logica.SistemaCafe;
import modelo.juego.CopiaJuego;
import modelo.juego.Juego;
import modelo.producto.Bebida;
import modelo.producto.Pasteleria;
import modelo.torneo.Inscripcion;
import modelo.torneo.Torneo;
import modelo.torneo.TorneoCompetitivo;
import modelo.usuario.Cliente;
import modelo.venta.Venta;

public class ConsolaCliente{

    private SistemaCafe sistema;
    private Scanner scanner;

    public ConsolaCliente(SistemaCafe sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public void ejecutar(Cliente cliente) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== MENÚ CLIENTE — " + cliente.getNombre() + " =====");
            if (cliente.tieneBono()) {
                System.out.println("  * Tienes un bono activo del "
                        + (int)(cliente.getBonoDescuento() * 100) + "%");
            }
            System.out.println("1. Ver torneos disponibles");
            System.out.println("2. Inscribirme en un torneo");
            System.out.println("3. Desinscribirme de un torneo");
            System.out.println("4. Mis inscripciones");
            System.out.println("5. Mis juegos favoritos");
            System.out.println("6. Realizar una compra");
            System.out.println("7. Mi perfil");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");

            switch (leerEntero()) {
                case 1: verTorneos(cliente); break;
                case 2: inscribirse(cliente); break;
                case 3: desinscribirse(cliente); break;
                case 4: misInscripciones(cliente); break;
                case 5: gestionarFavoritos(cliente); break;
                case 6: realizarCompra(cliente); break;
                case 7: verPerfil(cliente); break;
                case 0: salir = true; break;
                default: System.out.println("Opción inválida.");
            }
        }
    }

 

    private void verTorneos(Cliente cliente) {
        if (sistema.getTorneos().isEmpty()) {
            System.out.println("No hay torneos disponibles.");
            return;
        }
        System.out.println("\n-- Torneos disponibles --");
        for (Torneo t : sistema.getTorneos()) {
            System.out.println(t);
            System.out.println("  Cupos preferenciales libres: "
                    + t.cuposPreferencialesDisponibles()
                    + "/" + t.calcularCuposPreferenciales());
            if (cliente.esFanaticoDe(t.getJuego())) {
                System.out.println("  * Eres fanático de este juego (tienes prioridad de cupo)");
            }
            if (t instanceof TorneoCompetitivo) {
                System.out.println("  Tarifa: $"
                        + String.format("%.2f", ((TorneoCompetitivo) t).getTarifaInscripcion())
                        + " por cupo");
            }
        }
    }

    private void inscribirse(Cliente cliente) {
        verTorneos(cliente);
        if (sistema.getTorneos().isEmpty()) return;

        System.out.print("ID del torneo: ");
        int id = leerEntero();
        Torneo t = sistema.buscarTorneo(id);
        if (t == null) { System.out.println("Torneo no encontrado."); return; }

        System.out.print("¿Cuántos cupos desea tomar? (1-3): ");
        int cupos = leerEntero(1, 3);


        if (t instanceof TorneoCompetitivo) {
            double tarifa = ((TorneoCompetitivo) t).getTarifaInscripcion();
            double total = tarifa * cupos;
            System.out.println("Tarifa total a pagar: $" + String.format("%.2f", total));
            if (!leerSiNo("¿Confirma la inscripción?")) return;
        }

        String error = sistema.inscribirEnTorneo(id, cliente, cupos);
        if (error == null) {
            System.out.println("Inscripción exitosa con " + cupos + " cupo(s).");
            if (cliente.esFanaticoDe(t.getJuego())) {
                System.out.println("Se usaron cupos preferenciales por ser fanático del juego.");
            }
        } else {
            System.out.println("No fue posible inscribirse: " + error);
        }
    }

    private void desinscribirse(Cliente cliente) {
        misInscripciones(cliente);
        System.out.print("ID del torneo del que desea salir: ");
        int id = leerEntero();
        String error = sistema.desinscribirDeTorneo(id, cliente);
        if (error == null) {
            System.out.println("Desinscripción exitosa. Se liberaron todos sus cupos.");
        } else {
            System.out.println("Error: " + error);
        }
    }

    private void misInscripciones(Cliente cliente) {
        System.out.println("\n-- Mis inscripciones --");
        boolean tiene = false;
        for (Torneo t : sistema.getTorneos()) {
            Inscripcion ins = t.buscarInscripcion(cliente);
            if (ins != null) {
                tiene = true;
                System.out.println("  [" + t.getIdTorneo() + "] "
                        + t.getJuego().getNombre() + " (" + t.getTipo() + ")"
                        + " — " + t.getDia()
                        + " | Cupos: " + ins.getCuposTomados());
            }
        }
        if (!tiene) System.out.println("No estás inscrito en ningún torneo.");
    }

    // ── FAVORITOS ─────────────────────────────────────────────────────────

    private void gestionarFavoritos(Cliente cliente) {
        System.out.println("\n-- Mis juegos favoritos --");
        if (cliente.getJuegosFavoritos().isEmpty()) {
            System.out.println("No tienes juegos favoritos aún.");
        } else {
            for (Juego j : cliente.getJuegosFavoritos()) {
                System.out.println("  * " + j.getNombre());
            }
        }

        if (!leerSiNo("¿Deseas agregar un favorito?")) return;

        List<Juego> disponibles = juegosSinRepetir();
        if (disponibles.isEmpty()) {
            System.out.println("No hay juegos en el inventario.");
            return;
        }
        System.out.println("Juegos en inventario:");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + disponibles.get(i).getNombre());
        }
        System.out.print("Seleccione número: ");
        int idx = leerEntero(1, disponibles.size());
        cliente.agregarFavorito(disponibles.get(idx - 1));
        System.out.println("Juego agregado a favoritos.");
    }

   

    private void realizarCompra(Cliente cliente) {
        System.out.println("\n-- Realizar compra --");
        Venta venta = sistema.crearVenta(cliente);

        boolean agregar = true;
        while (agregar) {
            System.out.println("1. Bebida   2. Pastelería   0. Terminar");
            System.out.print("Opción: ");
            int tipo = leerEntero(0, 2);
            if (tipo == 0) break;

            System.out.print("Nombre del producto: ");
            String nombre = leerTextoNoVacio();
            System.out.print("Precio base: $");
            double precio = leerDouble();
            int idItem = (int)(Math.random() * 9000) + 1000;

            if (tipo == 1) {
                boolean alcoholica = leerSiNo("¿Es alcohólica?");
                boolean caliente   = leerSiNo("¿Es caliente?");
                venta.agregarItem(new Bebida(idItem, nombre, precio, alcoholica, caliente));
            } else {
                boolean alergeno = leerSiNo("¿Contiene alérgenos?");
                venta.agregarItem(new Pasteleria(idItem, nombre, precio, alergeno));
            }
            System.out.println("Producto agregado.");
            agregar = leerSiNo("¿Agregar otro producto?");
        }

        if (venta.getItems().isEmpty()) {
            System.out.println("Compra cancelada (sin productos).");
            return;
        }

        // Aplicar bono si tiene
        double descuento = 0;
        if (cliente.tieneBono()) {
            if (leerSiNo("Tienes un bono del "
                    + (int)(cliente.getBonoDescuento() * 100) + "%. ¿Aplicarlo?")) {
                descuento = cliente.usarBono();
                System.out.println("Bono aplicado.");
            }
        }

        double subtotal   = venta.calcularSubtotal();
        double impuestos  = venta.calcularImpuestos();
        double propina    = venta.calcularPropina();
        double total      = venta.calcularTotal() * (1 - descuento);

        System.out.println("\nSubtotal : $" + String.format("%.2f", subtotal));
        System.out.println("Impuestos: $" + String.format("%.2f", impuestos));
        System.out.println("Propina  : $" + String.format("%.2f", propina));
        if (descuento > 0)
            System.out.println("Descuento: -" + (int)(descuento * 100) + "%");
        System.out.println("TOTAL    : $" + String.format("%.2f", total));

        if (leerSiNo("¿Confirmar compra?")) {
            sistema.registrarVenta(venta);
            System.out.println("Compra registrada. Puntos acumulados: "
                    + String.format("%.2f", cliente.getPuntosFidelidad()));
        } else {
            System.out.println("Compra cancelada.");
        }
    }

  
    private void verPerfil(Cliente cliente) {
        System.out.println("\n-- Mi perfil --");
        System.out.println("Nombre   : " + cliente.getNombre());
        System.out.println("Login    : " + cliente.getLogin());
        System.out.println("Puntos   : " + String.format("%.2f", cliente.getPuntosFidelidad()));
        System.out.println("Bono     : " + (cliente.tieneBono()
                ? (int)(cliente.getBonoDescuento() * 100) + "%" : "Sin bono activo"));
        System.out.println("Favoritos: " + cliente.getJuegosFavoritos().size() + " juego(s)");
    }



    private List<Juego> juegosSinRepetir() {
        List<Juego> lista = new ArrayList<>();
        for (CopiaJuego c : sistema.getInventarioJuegos()) {
            if (c.getJuego() == null) continue;
            boolean visto = lista.stream()
                    .anyMatch(j -> j.getNombre().equalsIgnoreCase(c.getJuego().getNombre()));
            if (!visto) lista.add(c.getJuego());
        }
        return lista;
    }
    private int leerEntero() {
    	while(true) {
    		try {
    			return Integer.parseInt(scanner.nextLine().trim());
    		}
    		catch(Exception e) {
    			System.out.print("Ingrese un número válido: ");
    		}
    	}
    }

    private int leerEntero(int min, int max) {
    	while(true) {
    		int valor=leerEntero();
    		if(valor>=min && valor<=max) {
    			return valor;
    		}
    		System.out.print("Ingrese un número entre " + min + " y " + max + ": ");
    	}
    }

    private double leerDouble() {
    	while(true) {
    		try {
    			return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
    		}
    		catch(Exception e) {
    			System.out.print("Ingrese un número válido: ");
    		}
    	}
    }

    private String leerTextoNoVacio() {
    	while(true) {
    		String texto=scanner.nextLine().trim();
    		if(!texto.isEmpty()) {
    			return texto;
    		}
    		System.out.print("El campo no puede estar vacío: ");
    	}
    }

    private boolean leerSiNo(String mensaje) {
    	while(true) {
    		System.out.print(mensaje + " (s/n): ");
    		String respuesta=scanner.nextLine().trim().toLowerCase();

    		if(respuesta.equals("s")) {
    			return true;
    		}
    		else if(respuesta.equals("n")) {
    			return false;
    		}

    		System.out.println("Responda s o n.");
    	}
    }
}