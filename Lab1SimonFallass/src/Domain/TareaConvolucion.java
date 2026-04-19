/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domain;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author laboratorios
 */
public class TareaConvolucion extends Thread {

    private BufferedImage imagenOriginal;
    private BufferedImage imagenResultado;
    private Convolucion convolucion;
    private int yInicio;
    private int yFin;

    public TareaConvolucion(BufferedImage imagenOriginal, BufferedImage imagenResultado, Convolucion convolucion, int yInicio, int yFin) {
        this.imagenOriginal = imagenOriginal;
        this.imagenResultado = imagenResultado;
        this.convolucion = convolucion;
        this.yInicio = yInicio;
        this.yFin = yFin;
    }

    public void run() {
        int ancho = this.imagenOriginal.getWidth();
        int alto = this.imagenOriginal.getHeight(); // Necesitamos el alto total
        float[][] kernel = this.convolucion.getKernel();
        float divisor = this.convolucion.getDivisor();
        for (int y = yInicio; y < yFin; y++) {
            for (int x = 1; x < ancho - 1; x++) {

                if (y > 0 && y < alto - 1) {
                    this.imagenResultado.setRGB(
                            x,
                            y,
                            aplicarKernel(x, y, kernel, divisor)
                    );
                }
            }
        }
    } // run subir

    private int aplicarKernel(int x, int y, float[][] kernel, float divisor) {
        float sumaR = 0, sumaG = 0, sumaB = 0;

        for (int fila = -1; fila <= 1; fila++) {
            for (int columna = -1; columna <= 1; columna++) {

                int rgb = this.imagenOriginal.getRGB(x + fila, y + columna);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                float valorKernel = kernel[fila + 1][columna + 1];

                sumaR += r * valorKernel;
                sumaG += g * valorKernel;
                sumaB += b * valorKernel;
            }
        }

        int red = Math.min(Math.max((int) (sumaR / divisor), 0), 255);
        int green = Math.min(Math.max((int) (sumaG / divisor), 0), 255);
        int blue = Math.min(Math.max((int) (sumaB / divisor), 0), 255);

        return (255 << 24) | (red << 16) | (green << 8) | blue;
    } //aplicarkernel

} // fin clases

