/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Operator;

import Data.ImageData;

/**
 *
 * @author Jhon
 */
public class Histograma {
    private int color;
    private int[] hist;

    public Histograma(ImageData data,int color){
        hist =new int[256];
        int k;
        switch(data.getEspacioColor()){
        case ImageData.BGR:
            if(color==0)
                color=2;
            else if(color==2)
                color=0;
            break;
      }

        for(int i=0;i<256;i++){
            hist[i]=0;
        }

        for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                k = BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + color]);
                hist[k]++;
            }
        }
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }

    public int mayor(){
        int mayor = Integer.MIN_VALUE;
        for(int i=0;i<hist.length;i++){
            if(mayor<hist[i])
                mayor=hist[i];
        }
        return mayor;
    }

    /**
     * @return the hist
     */
    public int[] getHist() {
        return hist;
    }

    /**
     * @param hist the hist to set
     */
    public void setHist(int[] hist) {
        this.hist = hist;
    }


}
