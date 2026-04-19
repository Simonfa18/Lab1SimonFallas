/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Data.ManejoImagen;
import Domain.ConvolucionBorde;
import Domain.ProcesarImagen;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author laboratorios
 */
public class ExploradorArchivos extends JFrame {

    private JLabel etiquetaImagen;
    private BufferedImage imagenCargada;
    private File archivoOriginal;

    public ExploradorArchivos() {
        super("Procesamiento de Imágenes");

        JButton botonAbrir = new JButton("Abrir Imagen");
        JButton botonGuardar = new JButton("Guardar Imagen");

        etiquetaImagen = new JLabel("No hay imagen cargada", SwingConstants.CENTER);

        botonAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser selectorArchivos = new JFileChooser();
                selectorArchivos.setAcceptAllFileFilterUsed(true);

                int opcion = selectorArchivos.showOpenDialog(ExploradorArchivos.this);
                if (opcion == JFileChooser.APPROVE_OPTION) {
                    File archivo = selectorArchivos.getSelectedFile();
                    try {
                        if (!ManejoImagen.formatoValido(archivo)) {
                            JOptionPane.showMessageDialog(ExploradorArchivos.this,
                                    "Formato no permitido. Solo JPG o PNG.");
                            return;
                        }
                        BufferedImage img = ManejoImagen.cargarImagen(archivo);
                        if (img == null) {
                            JOptionPane.showMessageDialog(ExploradorArchivos.this,
                                    "El formato de la imagen no es soportado.");
                            return;
                        }
                        imagenCargada = img;
                        archivoOriginal = archivo;

                        int ancho = etiquetaImagen.getWidth() > 0 ? etiquetaImagen.getWidth() : 600;
                        int alto = etiquetaImagen.getHeight() > 0 ? etiquetaImagen.getHeight() : 400;

                        Image imagenEscalada = imagenCargada.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                        etiquetaImagen.setIcon(new ImageIcon(imagenEscalada));
                        etiquetaImagen.setText("");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ExploradorArchivos.this,
                                "Error al cargar la imagen: " + ex.getMessage());
                    }
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imagenCargada != null && archivoOriginal != null) {
                    try {
                        String formato = archivoOriginal.getName().toLowerCase().endsWith(".png") ? "png" : "jpg";
                        ManejoImagen.guardarImagen(imagenCargada, archivoOriginal, formato);
                        JOptionPane.showMessageDialog(ExploradorArchivos.this,
                                "Imagen guardada correctamente.");
                        mostrarImagenEnNuevaVentana(imagenCargada);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ExploradorArchivos.this,
                                "Error al guardar la imagen: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(ExploradorArchivos.this,
                            "No hay imagen cargada para guardar.");
                }
            }
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonAbrir);
        panelBotones.add(botonGuardar);

        this.setLayout(new BorderLayout());
        this.add(panelBotones, BorderLayout.NORTH);
        this.add(new JScrollPane(etiquetaImagen), BorderLayout.CENTER);

        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void mostrarImagenEnNuevaVentana(BufferedImage imagen) {
        JFrame ventanaImagen = new JFrame("Imagen Guardada");
        ventanaImagen.setSize(800, 600);
        ventanaImagen.setLocationRelativeTo(null);
        ventanaImagen.setLayout(new BorderLayout());

        int ancho = ventanaImagen.getWidth();
        int alto = ventanaImagen.getHeight();
        Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        JLabel etiqueta = new JLabel(new ImageIcon(imagenEscalada));
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

       
        JPanel panelBotones = new JPanel();
        JButton botonProcesar = new JButton("Procesar");
        JButton botonVolver = new JButton("Volver");

        panelBotones.add(botonProcesar);
        panelBotones.add(botonVolver);

        ventanaImagen.add(panelBotones, BorderLayout.NORTH);
        ventanaImagen.add(new JScrollPane(etiqueta), BorderLayout.CENTER);

        botonProcesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    ProcesarImagen procesador = new ProcesarImagen(archivoOriginal.getAbsolutePath(), 4);

                    BufferedImage procesada = procesador.procesarYMarcar(new ConvolucionBorde());

                    BufferedImage marcada = procesador.marcarObjeto(procesada);

                    Image imgEscalada = marcada.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                    etiqueta.setIcon(new ImageIcon(imgEscalada));

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ventanaImagen,
                            "Error al procesar la imagen: " + ex.getMessage());
                }
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventanaImagen.dispose(); // cierra la ventana secundaria
            }
        });

        ventanaImagen.setVisible(true);
    }
}
