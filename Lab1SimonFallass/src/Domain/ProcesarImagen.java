/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.imageio.ImageIO;

public class ProcesarImagen {

    private BufferedImage imagenOriginal;
    private int cantidadHilos;

    public ProcesarImagen(String rutaArchivo, int hilos) throws IOException {
        this.imagenOriginal = ImageIO.read(new File(rutaArchivo));
        this.cantidadHilos = hilos;
    }

    public BufferedImage procesarYMarcar(Convolucion filtro) {
        int ancho = imagenOriginal.getWidth();
        int alto = imagenOriginal.getHeight();
        BufferedImage resultadoProcesado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        TareaConvolucion[] misHilos = new TareaConvolucion[this.cantidadHilos];
        int tamañoFranja = alto / this.cantidadHilos;

        for (int i = 0; i < this.cantidadHilos; i++) {
            int filaInicio = i * tamañoFranja;
            int filaFin = (i == this.cantidadHilos - 1) ? alto : (i + 1) * tamañoFranja;

            misHilos[i] = new TareaConvolucion(imagenOriginal, resultadoProcesado, filtro, filaInicio, filaFin);
            misHilos[i].start();
        }

        for (TareaConvolucion hilo : misHilos) {
            try {
                hilo.join();
            } catch (Exception error) {
                error.printStackTrace();
            }
        }

        return marcarObjeto(resultadoProcesado);
    } //procesar y marcar

    public BufferedImage marcarObjeto(BufferedImage imagenLimpia) {
        int ancho = imagenLimpia.getWidth();
        int alto = imagenLimpia.getHeight();
        
        BufferedImage imagenFinal = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagenFinal.createGraphics();
        g.drawImage(imagenLimpia, 0, 0, null);
        
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));

        boolean[][] revisado = new boolean[alto][ancho];

        for (int fila = 0; fila < alto; fila++) {
            for (int columna = 0; columna < ancho; columna++) {
                
                if (!revisado[fila][columna] && esColorValido(imagenLimpia, columna, fila)) {
                    
                    int[] bordes = buscarBordes(imagenLimpia, revisado, columna, fila);
                    
                    int izquierda = bordes[0];
                    int arriba = bordes[1];
                    int derecha = bordes[2];
                    int abajo = bordes[3];

                    int anchoObjeto = derecha - izquierda;
                    int altoObjeto = abajo - arriba;

                    if (anchoObjeto > 15 && altoObjeto > 15) {
                        g.drawRect(izquierda, arriba, anchoObjeto, altoObjeto);
                    }
                }
            }
        }

        g.dispose();
        return imagenFinal;
    } //marcar

    private int[] buscarBordes(BufferedImage imagen, boolean[][] revisado, int columnaInicio, int filaInicio) {
        int limiteAncho = imagen.getWidth();
        int limiteAlto = imagen.getHeight();

        int masIzquierda = columnaInicio;
        int masArriba = filaInicio;
        int masDerecha = columnaInicio;
        int masAbajo = filaInicio;

        Stack<Point> pendientes = new Stack<>();
        pendientes.push(new Point(columnaInicio, filaInicio));
        revisado[filaInicio][columnaInicio] = true;

        int distanciaDeSalto = 10; 

        while (!pendientes.isEmpty()) {
            Point puntoActual = pendientes.pop();

            if (puntoActual.x < masIzquierda) masIzquierda = puntoActual.x;
            if (puntoActual.y < masArriba) masArriba = puntoActual.y;
            if (puntoActual.x > masDerecha) masDerecha = puntoActual.x;
            if (puntoActual.y > masAbajo) masAbajo = puntoActual.y;

            for (int moverFila = -distanciaDeSalto; moverFila <= distanciaDeSalto; moverFila++) {
                for (int moverColumna = -distanciaDeSalto; moverColumna <= distanciaDeSalto; moverColumna++) {
                    
                    int nuevaColumna = puntoActual.x + moverColumna;
                    int nuevaFila = puntoActual.y + moverFila;

                    if (nuevaColumna >= 0 && nuevaColumna < limiteAncho && nuevaFila >= 0 && nuevaFila < limiteAlto 
                        && !revisado[nuevaFila][nuevaColumna]) {
                        
                        if (esColorValido(imagen, nuevaColumna, nuevaFila)) {
                            revisado[nuevaFila][nuevaColumna] = true;
                            pendientes.push(new Point(nuevaColumna, nuevaFila));
                        }
                    }
                }
            }
        }
        return new int[]{masIzquierda, masArriba, masDerecha, masAbajo};
    } //buscabordes

    private boolean esColorValido(BufferedImage imagen, int columna, int fila) {
        int colorRgb = imagen.getRGB(columna, fila);
        int rojo = (colorRgb >> 16) & 0xFF;
        int verde = (colorRgb >> 8) & 0xFF;
        int azul = colorRgb & 0xFF;
        
        return (rojo > 50 || verde > 50 || azul > 50);
    }//es color valido
    
}// fin de clase