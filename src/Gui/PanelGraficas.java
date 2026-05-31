package Gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import logica.SistemaCafe;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PanelGraficas extends JPanel {

    private SistemaCafe sistema;
    private JComboBox<String> comboJuegos;
    private ChartPanel panelGraficaPastel;

    public PanelGraficas(SistemaCafe sistema) {

        this.sistema = sistema;

        setLayout(new GridLayout(1, 3));

   
        // GRÁFICA 1 - PASTEL
  

        JPanel panelPastel = new JPanel(new BorderLayout());

        comboJuegos = new JComboBox<>();

        // Si no carga persistencia, uno temporal
        if (sistema.getNombresJuegos().isEmpty()) {
            comboJuegos.addItem("Catan");
        } else {
            for (String nombre : sistema.getNombresJuegos()) {
                comboJuegos.addItem(nombre);
            }
        }

        panelPastel.add(comboJuegos, BorderLayout.NORTH);

        String juegoSeleccionado =
                (String) comboJuegos.getSelectedItem();

        panelGraficaPastel =
                crearGraficaPastel(juegoSeleccionado);

        panelPastel.add(panelGraficaPastel,
                BorderLayout.CENTER);

        add(panelPastel);

        comboJuegos.addActionListener(
                e -> actualizarGrafica(panelPastel)
        );

       
        // GRÁFICA 2 - BARRAS
       

        add(crearGraficaBarras());


        // GRÁFICA 3 - LÍNEAS


        add(crearGraficaLineas());
    }

  
    // GRÁFICA PASTEL


    private ChartPanel crearGraficaPastel(String nombreJuego) {

        int[] datos =
                sistema.getDistribucionCopias(nombreJuego);

        DefaultPieDataset dataset =
                new DefaultPieDataset();

        dataset.setValue("Venta", datos[0]);
        dataset.setValue("Préstamo", datos[1]);

        JFreeChart chart =
                ChartFactory.createPieChart(
                        "Distribución de Copias\n" + nombreJuego,
                        dataset,
                        true,
                        true,
                        false
                );

        return new ChartPanel(chart);
    }

   
    // GRÁFICA DE BARRAS
   

    private ChartPanel crearGraficaBarras() {

        LocalDate fechaInicio =
                LocalDate.now().minusDays(4);

        Map<String, double[]> datos =
                sistema.getVentasPorRango(fechaInicio);

        String[] dias =
                sistema.getEtiquetasDias(fechaInicio);

        double[] cafeteria =
                datos.get("cafeteria");

        double[] juegos =
                datos.get("juegos");

        // Datos demo si no hay ventas
        boolean vacio = true;

        for (double v : cafeteria) {
            if (v > 0) {
                vacio = false;
            }
        }

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        for (int i = 0; i < 5; i++) {

            dataset.addValue(
                    cafeteria[i],
                    "Cafetería",
                    dias[i]
            );

            dataset.addValue(
                    juegos[i],
                    "Juegos",
                    dias[i]
            );
        }

        JFreeChart chart =
                ChartFactory.createBarChart(
                        "Ventas últimos 5 días",
                        "Día",
                        "Ventas ($)",
                        dataset
                );

        return new ChartPanel(chart);

    }


    // GRÁFICA DE LÍNEAS


    private ChartPanel crearGraficaLineas() {

        int[] reservas =
                sistema.getReservasPorSemana();

        String[] dias = {
                "Lun", "Mar", "Mié",
                "Jue", "Vie", "Sáb", "Dom"
        };

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        boolean todasCero = true;

        for (int r : reservas) {
            if (r > 0) {
                todasCero = false;
                break;
            }
        }

        if (todasCero) {
            reservas[0] = 1;
        }

        JFreeChart chart =
                ChartFactory.createLineChart(
                        "Reservas de la Semana",
                        "Día",
                        "Cantidad",
                        dataset
                );

        return new ChartPanel(chart);
    }

 
    // ACTUALIZAR PASTEL


    private void actualizarGrafica(
            JPanel panelPastel) {

        panelPastel.remove(panelGraficaPastel);

        String juegoSeleccionado =
                (String) comboJuegos
                        .getSelectedItem();

        panelGraficaPastel =
                crearGraficaPastel(
                        juegoSeleccionado
                );

        panelPastel.add(
                panelGraficaPastel,
                BorderLayout.CENTER
        );

        panelPastel.revalidate();
        panelPastel.repaint();
    }
}