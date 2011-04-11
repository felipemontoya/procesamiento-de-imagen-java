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
    public RotateFilter(String name) {
        super("RotateFilter " + name);
//        this.grados=grados;
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
        this.grados = 0;
    }

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Rotate))
        {
            grados += msg.dValue1;
        }
        return true;
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        int ancho =(int) Math.sqrt((dataIn.getImageData().getWidth()*dataIn.getImageData().getWidth()) + (dataIn.getImageData().getHeight()*dataIn.getImageData().getHeight()));
        ImageData data = new ImageData(this.dataIn.getImageData(),ancho,ancho);
        java.util.Arrays.fill(data.bytesImage, (byte)128);

        System.out.println("Filtro de rotación");
//        boolean RGB=true;;
//        switch(data.getEspacioColor()){
//        case ImageData.RGB:
//            RGB=true;
//            break;
//        case ImageData.BGR:
//            RGB=false;
//            break;
//      }

//        data.informacion();

        int channels = data.getnCanales();

        double y2,x2;
        int x1,y1;

        System.out.println("grados " + grados );
        

        int inWidth = dataIn.getImageData().getWidth();
        int inHeight = dataIn.getImageData().getHeight();
        int outWidth = data.getWidth();
        int outHeight = data.getHeight();

        int x0 = outWidth/2;
        int y0 = outHeight/2;
        byte pixel = 0;
        double cosG = Math.cos(grados);
        double sinG = Math.sin(grados);

        for(int j = 0;j < outHeight;j++){
            for(int i = 0;i < outWidth;i++){

                boolean isIn = false;

                int traI = i - x0;
                int traJ = j - y0;

                double rotI = traI*cosG - traJ*sinG;
                double rotJ = traI*sinG + traJ*cosG;

                double traI2 = rotI + x0;
                double traJ2 = rotJ + y0;

                x2 = traI2-x0;
                y2 = traJ2-y0;

               if(Math.abs(x2) < inWidth/2 && Math.abs(y2) < inHeight/2)
                   isIn = true;


//              interpolación por vecino más cercano
                x1 = (int) x2 + inWidth/2;
                y1 = (int) y2 + inHeight/2;
      
                try{
                if (isIn)
                    for (int k = 0; k <channels; k++)
                    {
                        pixel = dataIn.getImageData().bytesImage[(y1 * inWidth + x1 ) * channels  + k];
                        data.bytesImage[(j * outWidth + i) * channels  + k] = pixel;//dataIn.getImageData().bytesImage[(j * inWidth + i ) * channels  + 0];
                    }
                }
                 catch(Exception e){
                     System.out.println("El filtro rotate salio en alguna iteración de los límites");
                 }
          }
        }



        this.dataOut.setImageData(data);

//        System.out.println("Internal update Rotation filter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}