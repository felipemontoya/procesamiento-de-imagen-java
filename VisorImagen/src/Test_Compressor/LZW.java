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
        int i = 0;
        int k;
        System.out.println("COMPRESION: ");
        compress=String.valueOf(256);
        ImageUP(img);
        while(i<img.bytesImage.length){

            k=byteToInt(img.bytesImage[i]);
            if(isOnTable(omega+String.valueOf(k))){
                omega+=String.valueOf(k);
                
            }else{
                
                dict[iTable++]=omega+String.valueOf(k);
                System.out.println(codeFromString(omega));
                omega=String.valueOf(k);
            }
            i++;
        }
        System.out.println(codeFromString(omega));
        System.out.println(257);

        System.out.println("Diccionario");

        for(i=258;i<iTable;i++)
            System.out.println(i+ " - "+dict[i]);

        return compress;
    }


    private int byteToInt(byte by){
            int a=0;
              a |= by & 0xFF;

	     return  a ;

    }


    private boolean isOnTable(String op){
        boolean t = false;
        for(int i = 0;i<iTable;i++){
            if(dict[i].equals(op)){
                t=true;
                break;
            }
        }

        return t;
    }

    private int codeFromString(String op){
        int code = -4;
        for(int i = 0;i<iTable;i++){
            if(dict[i].equals(op)){
                code=i;
                break;
            }
        }
        return code;
    }

    private void ImageUP (ImageData image){
        byte aux,aux1,aux2;
        int widthStep = image.getWidth();
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
//Transformaciones de color...
//transformda discreta