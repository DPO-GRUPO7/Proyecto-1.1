package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.SistemaCafe;
import modelo.juego.CopiaJuego;
import modelo.usuario.*;
import modelo.venta.Venta;

public class Persistencia {
    private String rutaDirectorioExterna;
    private String archivoUsuarios;
    private String archivoJuegos;
    private String archivoVentas;

    public Persistencia(String rutaDirectorioExterna, String archivoUsuarios, String archivoJuegos, String archivoVentas) {
        this.rutaDirectorioExterna = rutaDirectorioExterna;
        this.archivoUsuarios = archivoUsuarios;
        this.archivoJuegos = archivoJuegos;
        this.archivoVentas = archivoVentas;
    }
    
    

    // --- GUARDAR ---
    public void guardarEstadoCompleto(SistemaCafe sistema) {
        String base = this.rutaDirectorioExterna;

        escribirArchivo(base + archivoUsuarios, serializarUsuarios(sistema.getUsuarios()).toString(4));

        escribirArchivo(base + archivoJuegos, serializarJuegos(sistema.getInventarioJuegos()).toString(4));

        escribirArchivo(base + archivoVentas, serializarVentas(sistema.consultarVentas()).toString(4));
        
        System.out.println("¡Sistema guardado por completo en la carpeta " + base + "!");
    }

    private JSONArray serializarUsuarios(List<Usuario> usuarios) {
        JSONArray listaJson = new JSONArray();
        for (Usuario user : usuarios) {
            JSONObject objJson = new JSONObject();
            objJson.put("id", user.getId());
            objJson.put("nombre", user.getNombre());
            objJson.put("login", user.getLogin());
            objJson.put("password", user.getPassword());

            if (user instanceof Cliente) {
                objJson.put("rol", "Cliente");
                objJson.put("puntos", ((Cliente) user).getPuntosFidelidad());
            } else if (user instanceof Mesero) {
                objJson.put("rol", "Mesero");
                objJson.put("disponible", ((Mesero) user).estaDisponible());
            } else if (user instanceof Administrador) {
                objJson.put("rol", "Administrador");
            } else if (user instanceof Cocinero) {
                objJson.put("rol", "Cocinero");
            }
            listaJson.put(objJson);
        }
        return listaJson;
    }

    
 
    public void cargarEstadoCompleto(SistemaCafe sistema) {
        String rutaFinal = this.rutaDirectorioExterna + this.archivoUsuarios;
        String contenido = leerArchivo(rutaFinal);

        if (!contenido.isEmpty()) {
            JSONArray jsonArray = new JSONArray(contenido);
            List<Usuario> recuperados = deserializarUsuarios(jsonArray);
            sistema.setUsuarios(recuperados);
        }
    }
    private JSONArray serializarJuegos(List<CopiaJuego> inventario) {
        JSONArray listaJson = new JSONArray();
        for (CopiaJuego copia : inventario) {
            JSONObject obj = new JSONObject();
            obj.put("estado", copia.estaDisponible() ? "BUENO" : "MALO");
            obj.put("uso", copia.getUso());
            obj.put("precioVenta", copia.getPrecioventa());

            JSONObject j = new JSONObject();
            j.put("nombre", copia.getJuego().getNombre());
            j.put("edadMinima", copia.getJuego().getEdadMinima());
            
            obj.put("juegoBase", j);
            listaJson.put(obj);
        }
        return listaJson;
    }
    private JSONArray serializarVentas(List<Venta> ventas) {
        JSONArray listaJson = new JSONArray();
        for (Venta v : ventas) {
            JSONObject obj = new JSONObject();
            obj.put("total", v.getTotal());
            
            obj.put("idCliente", v.getCliente().getId()); 
            obj.put("numItems", v.getItems().size());
            obj.put("numJuegos", v.getJuegosVendidos().size());

            listaJson.put(obj);
        }
        return listaJson;
    }

    private List<Usuario> deserializarUsuarios(JSONArray jsonArray) {
        List<Usuario> lista = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String rol = obj.getString("rol");
            
         
            int id = obj.getInt("id");
            String nom = obj.getString("nombre");
            String log = obj.getString("login");
            String pass = obj.getString("password");

            if (rol.equals("Cliente")) {
                Cliente c = new Cliente(id, nom, log, pass);
                c.acumularPuntos(obj.getDouble("puntos"));
                lista.add(c);
            } else if (rol.equals("Mesero")) {
                
                Mesero m = new Mesero(id, nom, log, pass, null);
                m.setDisponible(obj.getBoolean("disponible"));
                lista.add(m);
            } else if (rol.equals("Administrador")) {
                lista.add(new Administrador(id, nom, log, pass));
            } else if (rol.equals("Cocinero")) {
                lista.add(new Cocinero(id, nom, log, pass, null));
            }
        }
        return lista;
    }


    private void escribirArchivo(String ruta, String contenido) {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write(contenido);
        } catch (IOException e) {
            System.err.println("Error al escribir: " + e.getMessage());
        }
    }

    private String leerArchivo(String ruta) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
        } catch (IOException e) {
            System.out.println("Archivo no encontrado, se creará uno nuevo al guardar.");
        }
        return sb.toString();
    }
}