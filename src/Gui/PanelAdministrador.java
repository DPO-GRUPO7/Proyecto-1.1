package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import modelo.juego.Juego;
import modelo.torneo.Torneo;
import modelo.usuario.Administrador;

public class PanelAdministrador extends JPanel {

	private VentanaPrincipal ventana;
	private JTextArea areaInfo;

	public PanelAdministrador(VentanaPrincipal ventana) {
		this.ventana=ventana;
		construirUI();
	}

	private void construirUI() {
		setBackground(VentanaPrincipal.COLOR_FONDO);
		setLayout(new BorderLayout(20, 20));
		setBorder(new EmptyBorder(30, 40, 30, 40));

		JLabel titulo=new JLabel("Panel Administrador");
		titulo.setFont(VentanaPrincipal.FUENTE_TITULO);
		titulo.setForeground(VentanaPrincipal.COLOR_ACENTO);
		add(titulo, BorderLayout.NORTH);

		JPanel panelBotones=new JPanel(new GridLayout(0, 1, 10, 10));
		panelBotones.setBackground(VentanaPrincipal.COLOR_FONDO);

		JButton btnTorneos=crearBoton("Ver torneos");
		JButton btnAmistoso=crearBoton("Crear torneo amistoso");
		JButton btnCompetitivo=crearBoton("Crear torneo competitivo");
		JButton btnGraficas=crearBoton("Ver gráficas");
		JButton btnSalir=crearBoton("Cerrar sesión");

		panelBotones.add(btnTorneos);
		panelBotones.add(btnAmistoso);
		panelBotones.add(btnCompetitivo);
		panelBotones.add(btnGraficas);
		panelBotones.add(btnSalir);

		add(panelBotones, BorderLayout.WEST);

		areaInfo=new JTextArea();
		areaInfo.setEditable(false);
		areaInfo.setFont(VentanaPrincipal.FUENTE_NORMAL);
		areaInfo.setBackground(VentanaPrincipal.COLOR_PANEL);
		areaInfo.setForeground(VentanaPrincipal.COLOR_TEXTO);
		areaInfo.setBorder(new EmptyBorder(15, 15, 15, 15));

		JScrollPane scroll=new JScrollPane(areaInfo);
		scroll.setBorder(null);
		add(scroll, BorderLayout.CENTER);

		btnTorneos.addActionListener(e -> mostrarTorneos());
		btnAmistoso.addActionListener(e -> crearTorneoAmistoso());
		btnCompetitivo.addActionListener(e -> crearTorneoCompetitivo());
		btnGraficas.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_GRAFICAS));
		btnSalir.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_LOGIN));
	}

	private Administrador getAdministrador() {
		return (Administrador) ventana.getUsuarioActual();
	}

	private void mostrarTorneos() {
		areaInfo.setText("----- TORNEOS -----\n\n");

		if(ventana.getSistema().getTorneos().isEmpty()) {
			areaInfo.append("No hay torneos registrados.");
			return;
		}

		for(Torneo t : ventana.getSistema().getTorneos()) {
			areaInfo.append(t.toString() + "\n");
			areaInfo.append("Cupos disponibles: " + t.cuposDisponibles() + "\n");
			areaInfo.append("Cupos preferenciales disponibles: " + t.cuposPreferencialesDisponibles() + "\n\n");
		}
	}

	private void crearTorneoAmistoso() {
		String nombreJuego=JOptionPane.showInputDialog(this, "Nombre del juego:");
		if(nombreJuego==null) return;

		Juego juego=ventana.getSistema().buscarJuegoPorNombre(nombreJuego);

		if(juego==null) {
			JOptionPane.showMessageDialog(this, "Ese juego no existe en el inventario.");
			return;
		}

		String dia=JOptionPane.showInputDialog(this, "Día del torneo:");
		if(dia==null) return;

		String maxTexto=JOptionPane.showInputDialog(this, "Máximo de participantes:");
		if(maxTexto==null) return;

		String descuentoTexto=JOptionPane.showInputDialog(this, "Descuento. Ej: 0.15 para 15%:");
		if(descuentoTexto==null) return;

		try {
			int max=Integer.parseInt(maxTexto);
			double descuento=Double.parseDouble(descuentoTexto.replace(",", "."));

			String error=ventana.getSistema().crearTorneoAmistoso(
					juego,
					dia,
					max,
					getAdministrador(),
					descuento
			);

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Torneo amistoso creado correctamente.");
				mostrarTorneos();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese valores válidos.");
		}
	}

	private void crearTorneoCompetitivo() {
		String nombreJuego=JOptionPane.showInputDialog(this, "Nombre del juego:");
		if(nombreJuego==null) return;

		Juego juego=ventana.getSistema().buscarJuegoPorNombre(nombreJuego);

		if(juego==null) {
			JOptionPane.showMessageDialog(this, "Ese juego no existe en el inventario.");
			return;
		}

		String dia=JOptionPane.showInputDialog(this, "Día del torneo:");
		if(dia==null) return;

		String maxTexto=JOptionPane.showInputDialog(this, "Máximo de participantes:");
		if(maxTexto==null) return;

		String tarifaTexto=JOptionPane.showInputDialog(this, "Tarifa de inscripción:");
		if(tarifaTexto==null) return;

		try {
			int max=Integer.parseInt(maxTexto);
			double tarifa=Double.parseDouble(tarifaTexto.replace(",", "."));

			String error=ventana.getSistema().crearTorneoCompetitivo(
					juego,
					dia,
					max,
					getAdministrador(),
					tarifa
			);

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Torneo competitivo creado correctamente.");
				mostrarTorneos();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese valores válidos.");
		}
	}

	private JButton crearBoton(String texto) {
		JButton btn=new JButton(texto);
		btn.setFont(VentanaPrincipal.FUENTE_NORMAL);
		btn.setBackground(VentanaPrincipal.COLOR_ACENTO);
		btn.setForeground(Color.BLACK);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}
}