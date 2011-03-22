/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test_Compressor;

import Data.ImageData;

/**
 *
 * @author Jhon
 */
public class LZW {
    private String[] dict;
    private int iTable;
    private ImageData img;
    public LZW(ImageData i){
        img = new ImageData(i);
        dict = new String[65536];
        for (iTable = 0; iTable<256;iTable++)
             dict[iTable]=String.valueOf(iTable);
        dict[iTable++]="";
        dict[iTable++]="";
    }


    public String CompressionLZW(){
        String compress="";
        String omega="";
        int i = 0,k;

        compress=String.valueOf(256);
        ImageUP(img);
        while(i<img.bytesImage.length){
            k=img.bytesImage[i];
            
            i++;
        }
        return compress;
    }


    private boolean isOnTable(String op){
        boolean t = false;
        for(String b:dict){
            if(op.equals(op)){
                t=true;
                break;
            }
        }

        return t;
    }

    private void ImageUP (ImageData image){
        byte aux,aux1,aux2;
        int widthStep = image.getWidthStep();
        int heigh = image.getHeight();
        for(int i = 0;i<heigh/2;i++){
                    for(int j = 0;j<image.getWidth();j++){
                        aux=image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 0];
                        aux1=image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 1];
                        aux2=image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 2];
                        image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 0]= image.bytesImage[(i * widthStep + j ) * 3 + 0];
                        image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 1]=image.bytesImage[(i * widthStep + j ) * 3 + 1];
                        image.bytesImage[((heigh-1-i) * (widthStep-2) + j ) * 3 + 2]=image.bytesImage[(i * widthStep + j ) * 3 + 2];
                        image.bytesImage[(i * widthStep + j ) * 3 + 0]=aux;
                        image.bytesImage[(i * widthStep + j ) * 3 + 1]=aux1;
                        image.bytesImage[(i * widthStep + j ) * 3 + 2]=aux2;
                    }
                }

       }



}
