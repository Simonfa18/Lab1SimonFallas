/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author laboratorios
 */
public class ManejoImagen {

   
    public static BufferedImage cargarImagen(File archivo) throws IOException {
        return ImageIO.read(archivo);
    }

  
    public static void guardarImagen(BufferedImage imagen, File archivo, String formato) throws IOException {
        ImageIO.write(imagen, formato, archivo);
    }

  
    public static boolean formatoValido(File archivo) {
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".png");
    }
}

