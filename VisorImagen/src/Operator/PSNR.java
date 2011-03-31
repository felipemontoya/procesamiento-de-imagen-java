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
public class PSNR {
    private ImageData I;
    private ImageData K;
    private float MSE;
    private int MAX;
    public PSNR(ImageData i, ImageData k){
        I=i;
        K=k;
        MAX=255;

    }

    public boolean isDimentionEquals(){
        if(I.getHeight()==K.getHeight() && I.getWidth()==K.getWidth() && I.bytesImage.length==K.bytesImage.length){
            return true;
        }else
            return false;
    }

    private void calc(){
        MSE=0;
        int im,km;
        for(int i=0;i<I.bytesImage.length;i++){
            im=byteToInt(I.bytesImage[i]);
            km=byteToInt(K.bytesImage[i]);
            MSE+=(im-km)*(im-km);
        }
        MSE/=(I.getHeight()*K.getWidth());
    }

    private int byteToInt(byte by){
            int a=0;
              a |= by & 0xFF;

	     return  a ;

    }

    public double result(){
        calc();
        return 20*Math.log10(MAX/MSE);
    }



}
