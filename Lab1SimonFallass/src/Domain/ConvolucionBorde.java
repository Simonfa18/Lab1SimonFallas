/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Domain;

/**
 *
 * @author laboratorios
 */
public class ConvolucionBorde extends Convolucion {

    @Override
    public float[][] getKernel() {
        return new float[][]{
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
        };
    }

    @Override
    public float getDivisor() {
        return 1.0f;
    } // subir

}
