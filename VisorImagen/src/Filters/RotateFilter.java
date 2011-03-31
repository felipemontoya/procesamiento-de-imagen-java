/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Filters;
import Data.ImageData;
import PipeLine.*;


/**
 *
 * @author Jhon
 */
public class RotateFilter extends FilterPipeObject{

    private String name;
    private double grados;
    public RotateFilter(String name,double grados) {
        super("RotateFilter " + name);
        this.grados=grados;
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        int ancho =(int) Math.sqrt((dataIn.getImageData().getWidth()*dataIn.getImageData().getWidth()) + (dataIn.getImageData().getHeight()*dataIn.getImageData().getHeight()));
        ImageData data = new ImageData(this.dataIn.getImageData());//,ancho,ancho);

//        float[] a_channel = new float[data.getnCanales()];
//        for(int k = 0 ;k<data.getnCanales();k++){
//           a_channel[k]=0;
//        }
        System.out.println("Filtro de rotación");
        boolean RGB=true;;
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            RGB=true;
            break;
        case ImageData.BGR:
            RGB=false;
            break;
      }

//        data.informacion();

        int channels = data.getnCanales();
        grados*=(Math.PI/180);
        double y2,x2;


        java.util.Arrays.fill(data.bytesImage, (byte)128);

        int inWidth = dataIn.getImageData().getWidth();
        int inHeight = dataIn.getImageData().getHeight();
        int outWidth = data.getWidth();
        int outHeight = data.getHeight();

        int x0 = 0;//inWidth/2;
        int y0 = 0;//inHeight/2;

        for(int j = 0;j < inHeight;j++){
            for(int i = 0;i < inWidth;i++){
   
                x2 = Math.cos(grados)*(i-x0)-Math.sin(grados)*(j-y0)+x0;
                y2 = Math.sin(grados)*(i-x0)+Math.cos(grados)*(j-y0)+y0;

//                a+=dataIn.getImageData().getWidth()/2;
//                b+=dataIn.getImageData().getHeight()/2;
               if(x2<0 || y2<0 || x2>outWidth || y2>outHeight)
                   continue;


//          data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 0]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]
     try{   data.bytesImage[(int)(y2 * outWidth + x2 ) * channels  + 0] = dataIn.getImageData().bytesImage[(j * inWidth + i ) * channels  + 0];
                }
     catch(Exception e){
//         System.out.println("ij: " + i + " " + j + " ab: " + a + " " + b);
     }
//                System.out.println("llegue al filtro");
//                if(RGB){
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 0]=dataIn.getImageData().bytesImage[(j * data.getWidthStep() + i ) * channels + 0];
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 1]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1];
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 2]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2];
//                }else{
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 0]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2];
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 1]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1];
//                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 2]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0];
//                }
//
          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}