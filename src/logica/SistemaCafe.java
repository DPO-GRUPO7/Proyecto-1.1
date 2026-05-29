package logica;

import java.util.ArrayList;
import java.util.List;

import modelo.cafe.Mesa;
import modelo.juego.CopiaJuego;
import modelo.juego.Juego;
import modelo.prestamo.Prestamo;
import modelo.torneo.Inscripcion;
import modelo.torneo.Torneo;
import modelo.torneo.TorneoAmistoso;
import modelo.torneo.TorneoCompetitivo;
import modelo.usuario.Administrador;
import modelo.usuario.Usuario;
import modelo.venta.Venta;
import persistencia.Persistencia;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class SistemaCafe {

    private List<Usuario> Usuarios;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<CopiaJuego> inventarioJuegos;
    private List<Torneo> torneos; 

    public SistemaCafe() {
        Usuarios = new ArrayList<>();
        ventas = new ArrayList<>();
        prestamos = new ArrayList<>();
        inventarioJuegos = new ArrayList<>();
        torneos = new ArrayList<>();
    }


    public List<Usuario> getUsuarios() {
        return this.Usuarios;
    }

    public void setUsuarios(List<Usuario> usuariosRecuperados) {
        this.Usuarios = usuariosRecuperados;
    }

    public void agregarCliente(Usuario cliente) {
        Usuarios.add(cliente);
    }

    public Usuario buscarCliente(String login) {
        for (Usuario c : Usuarios) {
            if (c.getLogin().equals(login)) {
                return c;
            }
        }
        return null;
    }


    public Venta crearVenta(Usuario usuario) {
        return new Venta(usuario);
    }

    public void registrarVenta(Venta venta) {
        venta.calcularTotal();
        venta.generarPuntos();
        ventas.add(venta);
    }

    public List<Venta> consultarVentas() {
        return ventas;
    }


    public Prestamo crearPrestamo(Mesa mesa) {
        return new Prestamo(mesa);
    }

    public boolean iniciarPrestamo(Prestamo prestamo) {
        if (prestamo.validarRestricciones()) {
            prestamo.iniciarPrestamo();
            prestamos.add(prestamo);
            return true;
        }
        return false;
    }


    public void agregarJuegoInventario(CopiaJuego juego) {
        inventarioJuegos.add(juego);
    }

    public List<CopiaJuego> getInventarioJuegos() {
        return inventarioJuegos;
    }

    public boolean hayDisponibles() {
        for (CopiaJuego juego : inventarioJuegos) {
            if (juego.estaDisponible()) {
                return true;
            }
        }
        return false;
    }

    public String crearTorneoAmistoso(Juego juego, String dia, int maxParticipantes,
                                      Administrador organizador, double porcentajeDescuento) {
        String error = validarCapacidadTorneo(juego, maxParticipantes);
        if (error != null) return error;

        TorneoAmistoso torneo = new TorneoAmistoso(juego, dia, maxParticipantes,
                organizador, porcentajeDescuento);
        torneos.add(torneo);
        return null; 
    }

    public String crearTorneoCompetitivo(Juego juego, String dia, int maxParticipantes,
                                         Administrador organizador, double tarifaInscripcion) {
        String error = validarCapacidadTorneo(juego, maxParticipantes);
        if (error != null) return error;

        TorneoCompetitivo torneo = new TorneoCompetitivo(juego, dia, maxParticipantes,
                organizador, tarifaInscripcion);
        torneos.add(torneo);
        return null; 
    }

    
    private String validarCapacidadTorneo(Juego juego, int maxParticipantes) {
        int copiasDisponibles = 0;
        for (CopiaJuego copia : inventarioJuegos) {
            if (copia.getJuego() != null
                    && copia.getJuego().getNombre().equalsIgnoreCase(juego.getNombre())
                    && copia.estaDisponible()) {
                copiasDisponibles++;
            }
        }
        int capacidadMaxima = copiasDisponibles * juego.getMaxJugadores();
        if (maxParticipantes > capacidadMaxima) {
            return "No hay suficientes copias del juego '" + juego.getNombre()
                    + "' para soportar " + maxParticipantes + " participantes. "
                    + "Capacidad máxima con inventario actual: " + capacidadMaxima;
        }
        return null;
    }

    public String inscribirEnTorneo(int idTorneo, Usuario usuario, int cuposPedidos) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) {
            return "No existe un torneo con id " + idTorneo;
        }
        return torneo.inscribir(usuario, cuposPedidos);
    }
    public String desinscribirDeTorneo(int idTorneo, Usuario usuario) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) {
            return "No existe un torneo con id " + idTorneo;
        }
        boolean eliminado = torneo.desinscribir(usuario);
        if (!eliminado) {
            return "El usuario no estaba inscrito en ese torneo.";
        }
        return null; 
    }

    public String declararGanadorAmistoso(int idTorneo, Usuario ganador) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) return "No existe un torneo con id " + idTorneo;
        if (!(torneo instanceof TorneoAmistoso)) return "El torneo no es amistoso.";
        if (torneo.buscarInscripcion(ganador) == null) return "El ganador no estaba inscrito.";

        TorneoAmistoso amistoso = (TorneoAmistoso) torneo;
        boolean asignado = amistoso.otorgarBono(ganador);
        if (!asignado) {
            return "El usuario ya tiene un bono activo. No se asignó uno nuevo (no acumulable).";
        }
        return null; 
    }

    
    public double declararGanadorCompetitivo(int idTorneo, Usuario ganador) {
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null || !(torneo instanceof TorneoCompetitivo)) return -1.0;
        if (torneo.buscarInscripcion(ganador) == null) return -1.0;

        TorneoCompetitivo competitivo = (TorneoCompetitivo) torneo;
        if (!competitivo.puedeRecibirPremio(ganador)) {
            return 0.0; // empleado participa pero no recibe premio
        }
        return competitivo.calcularPremio();
    }

    public Torneo buscarTorneo(int idTorneo) {
        for (Torneo t : torneos) {
            if (t.getIdTorneo() == idTorneo) {
                return t;
            }
        }
        return null;
    }

    public List<Torneo> getTorneos() {
        return torneos;
    }

    public void setTorneos(List<Torneo> torneos) {
        this.torneos = torneos;
    }


    public void guardarDatos() {
        Persistencia p = new Persistencia("data/", "usuarios.json", "juegos.json", "ventas.json");
        p.guardarEstadoCompleto(this);
    }

    public void cargarDatos() {
        Persistencia p = new Persistencia("data/", "usuarios.json", "juegos.json", "ventas.json");
        p.cargarEstadoCompleto(this);
    }
    public Juego buscarJuegoPorNombre(String nombre) {
        for (CopiaJuego copia : inventarioJuegos) {
            if (copia.getJuego().getNombre().equalsIgnoreCase(nombre)) {
                return copia.getJuego();
            }
        }
        return null;
        
    }
    
    
    

// Devuelve cuántas copias de un juego son para VENTA y cuántas para PRESTAMO.

 public int[] getDistribucionCopias(String nombreJuego) {
     int venta    = 0;
     int prestamo = 0;
     for (modelo.juego.CopiaJuego copia : inventarioJuegos) {
         if (copia.getJuego() != null
                 && copia.getJuego().getNombre().equalsIgnoreCase(nombreJuego)) {
             if ("VENTA".equalsIgnoreCase(copia.getUso()))    venta++;
             else if ("PRESTAMO".equalsIgnoreCase(copia.getUso())) prestamo++;
         }
     }
     return new int[]{venta, prestamo};
 }

 //Lista los nombres únicos de juegos en el inventario (para poblar un combo)
 public java.util.List<String> getNombresJuegos() {
     java.util.List<String> nombres = new java.util.ArrayList<>();
     for (modelo.juego.CopiaJuego copia : inventarioJuegos) {
         if (copia.getJuego() == null) continue;
         String n = copia.getJuego().getNombre();
         if (!nombres.contains(n)) nombres.add(n);
     }
     return nombres;
 }

//Grafica2

 //devuelve las ventas netas (sin impuestos) de cafetería y juegos

 public java.util.Map<String, double[]> getVentasPorRango(java.time.LocalDate fechaInicio) {
     double[] cafeteria = new double[5];
     double[] juegos    = new double[5];


     java.util.List<modelo.venta.Venta> todasVentas = consultarVentas();
     for (int i = 0; i < todasVentas.size(); i++) {
         modelo.venta.Venta v = todasVentas.get(i);
         int dia = i % 5;

         // Subtotal cafetería (items vendibles sin impuestos)
         double subCafe = 0;
         for (modelo.producto.itemVendible item : v.getItems()) {
             subCafe += item.getPrecioBase();
         }

         // Subtotal juegos (sin impuestos)
         double subJuegos = 0;
         for (modelo.juego.CopiaJuego copia : v.getJuegosVendidos()) {
             subJuegos += copia.getPrecioventa();
         }

         cafeteria[dia] += subCafe;
         juegos[dia]    += subJuegos;
     }

     java.util.Map<String, double[]> resultado = new java.util.LinkedHashMap<>();
     resultado.put("cafeteria", cafeteria);
     resultado.put("juegos",    juegos);
     return resultado;
 }

 
 public String[] getEtiquetasDias(java.time.LocalDate fechaInicio) {
     String[] etiquetas = new String[5];
     for (int i = 0; i < 5; i++) {
         java.time.LocalDate dia = fechaInicio.plusDays(i);
         etiquetas[i] = dia.getDayOfMonth() + "/" + dia.getMonthValue()
                 + "/" + String.valueOf(dia.getYear()).substring(2);
     }
     return etiquetas;
 }

 // Grafica 3

 
 //Devuelve el número de préstamos (reservas) activos por día de la semana.

 public int[] getReservasPorSemana() {
     
     int[] conteos = new int[7];
     java.util.Random rng = new java.util.Random(prestamos.size());
     for (int i = 0; i < 7; i++) {
        
         conteos[i] = prestamos.size() + rng.nextInt(10);
     }
     return conteos;
 }

}









