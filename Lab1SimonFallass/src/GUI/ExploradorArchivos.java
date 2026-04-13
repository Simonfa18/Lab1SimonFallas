/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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
                        BufferedImage img = ImageIO.read(archivo);
                        if (img == null) {
                            JOptionPane.showMessageDialog(ExploradorArchivos.this, "El formato de la imagen no es soportado.");
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
                        JOptionPane.showMessageDialog(ExploradorArchivos.this, "Error al cargar la imagen: " + ex.getMessage());
                    }
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imagenCargada != null && archivoOriginal != null) {
                    String nombreArchivo = archivoOriginal.getName().toLowerCase();
                    if (!(nombreArchivo.endsWith(".png") || nombreArchivo.endsWith(".jpg"))) {
                        JOptionPane.showMessageDialog(ExploradorArchivos.this, "Formato no permitido. Solo se puede guardar como PNG o JPG.");
                        return;
                    }
                    try {
                        String formato = nombreArchivo.endsWith(".png") ? "png" : "jpg";
                        ImageIO.write(imagenCargada, formato, archivoOriginal);
                        JOptionPane.showMessageDialog(ExploradorArchivos.this, "Imagen guardada correctamente.");
                        mostrarImagenEnNuevaVentana(imagenCargada);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ExploradorArchivos.this, "Error al guardar la imagen: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(ExploradorArchivos.this, "No hay imagen cargada para guardar.");
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

        int ancho = ventanaImagen.getWidth();
        int alto = ventanaImagen.getHeight();
        Image imagenEscalada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        JLabel etiqueta = new JLabel(new ImageIcon(imagenEscalada));
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        ventanaImagen.add(new JScrollPane(etiqueta));
        ventanaImagen.setVisible(true);
    }
}