package consola;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import logica.SistemaCafe;
import modelo.usuario.*;
import modelo.juego.*;
import modelo.producto.*;
import modelo.cafe.*;
import modelo.venta.Venta;
import modelo.prestamo.Prestamo;

public class MainTest {


    private SistemaCafe sistema;
    private Cliente clientePrueba;
    private CopiaJuego copiaPrestamo;
    

    public void setup() {
        sistema = new SistemaCafe();
        //Crear cliente
        clientePrueba = new Cliente(1, "Andrés", "andres77", "clave");
        sistema.agregarCliente(clientePrueba);

        Juego catan = new Juego("Catan", 1995, "Devir", "Estrategia", 3, 4, 10, 3, "BUENO");
        copiaPrestamo = new CopiaJuego(catan, "BUENO", "PRESTAMO", 0);
        sistema.agregarJuegoInventario(copiaPrestamo);
    }
    

    @Test
    public void ventaGeneraPuntosTest() {
        setup(); 
        
        try {
        	// Comprobamos que los atributos y metodos funcionen correctamente para venta
            Venta nuevaVenta = sistema.crearVenta(clientePrueba);
            Bebida cafe = new Bebida(101, "Tinto", 2000, false, true); 
            nuevaVenta.agregarItem(cafe);
            
            sistema.registrarVenta(nuevaVenta);
            // Calculos correctos
            assertEquals(2360.0, nuevaVenta.calcularTotal(), "El total de la venta no es correcto");
            assertEquals(23.6, clientePrueba.getPuntosFidelidad(), "Los puntos no se sumaron correctamente");
            
        } catch (Exception e) {
            fail("No debió lanzar excepción al hacer una venta normal");
        }
    }
    
    @Test
    public void limitesMesaTest() {
        setup(); 
        
        try {
        	//Comprobamos que se cumplan los limites que establecimos para las mesas
            Mesa mesaJusta = new Mesa(1, 4); 
            mesaJusta.asignarPersonas(4, false); 
            
            Prestamo prestamoJusto = sistema.crearPrestamo(mesaJusta);
            prestamoJusto.agregarJuego(copiaPrestamo);
            
            assertTrue(sistema.iniciarPrestamo(prestamoJusto), "Debería permitir el préstamo a 4 personas en mesa de 4");
            
            Mesa mesaExcedida = new Mesa(2, 4);
            mesaExcedida.asignarPersonas(5, false);
            
            Prestamo prestamoExcedido = sistema.crearPrestamo(mesaExcedida);
            prestamoExcedido.agregarJuego(copiaPrestamo);
            
            assertFalse(sistema.iniciarPrestamo(prestamoExcedido), "Debería rechazar el préstamo a 5 personas en mesa de 4");
            
        } catch (Exception e) {
            fail("No debió lanzar excepción al validar las mesas");
        }
    }
    @Test
    public void operacionesEmpleadoTest() {
        setup(); 
        
        try {
            // Creamos el mesero que de base esta siempre disponible
            Mesero mesero = new Mesero(3, "Carlos", "carlosM", "1234", null);
            sistema.getUsuarios().add(mesero);
            
            assertTrue(mesero.estaDisponible(), "El mesero debería nacer con disponibilidad en true");
            
            mesero.ocupar();
            assertFalse(mesero.estaDisponible(), "El método ocupar() no cambió la disponibilidad a false");
            
            mesero.liberar();
            assertTrue(mesero.estaDisponible(), "El método liberar() no cambió la disponibilidad a true");
            
            SolicitudCambioTurno solicitud = mesero.solicitarCambioTurno(null);
            assertTrue(solicitud != null, "La solicitud de turno no se creó correctamente");
            
        } catch (Exception e) {
            fail("No debió lanzar excepción al operar con el Empleado");
        }
    }
    
    @Test
    public void operacionesAdministradorTest() {
        setup();
        
        try {
            Administrador admin = new Administrador(2, "Jefe", "admin1", "admin");
            sistema.getUsuarios().add(admin);
            //Creamos admin
            SolicitudCambioTurno solicitud = new SolicitudCambioTurno(null, null); 
            //recibir solicitud de cambioTurno
            admin.autorizarCambioTurno(solicitud, true);
            //autorizar la solicitud
            assertTrue(solicitud.esAprobada(), "El Administrador no pudo aprobar la solicitud de turno");
            
        } catch (Exception e) {
            fail("No debió lanzar excepción al autorizar el turno");
        }
    }
}