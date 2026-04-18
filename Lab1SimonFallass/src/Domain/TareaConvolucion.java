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
public class TareaConvolucion {
    
    private BufferedImage  imagenOriginal;
    private BufferedImage imagenResuldado;
    
    private Convolucion convolucion;
    private int yInicio;
    private int yFin;

    public TareaConvolucion(BufferedImage imagenOriginal, BufferedImage imagenResuldado, Convolucion convolucion, int yInicio, int yFin) {
        this.imagenOriginal = imagenOriginal;
        this.imagenResuldado = imagenResuldado;
        this.convolucion = convolucion;
        this.yInicio = yInicio;
        this.yFin = yFin;
    } // constructor
    
    
    
    public void run(){
        
        int w=this.imagenOriginal.getWidth();
        float[][] kernel=this.convolucion.getKernel();
        float divisor=this.convolucion.getDivisor();
        
        for (int y = yInicio; y < yFin; y++) {
            for (int x = 1; x < w-1; x++) {
                this.imagenResuldado.setRGB(
                        x,
                        y,
                        aplicarKernel(x, y, kernel, divisor)
                );
            } // for x
        } // for y
        
    } // run
    
    private int aplicarKernel(int x, int y,
            float[][] kernel, float divisor){
    
        float r=0, g=0, b=0;
        
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Color c=new Color(this.imagenOriginal.getRGB(x+i, y+j));
                r+=c.getRed()*kernel[i+1][j+1];
                g+=c.getGreen()*kernel[i+1][j+1];
                b+=c.getBlue()*kernel[i+1][j+1];
            } // for j 
        } // for i
        
        // Normalizar
        
        int red=Math.min(Math.max((int)(r/divisor), 0), 255);
        int green=Math.min(Math.max((int)(g/divisor), 0), 255);
        int blue=Math.min(Math.max((int)(b/divisor), 0), 255);
        
        return new Color(red, green, blue).getRGB();
        
    } // aplicarKernel
    
   
} // fin clases


