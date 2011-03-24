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
public class RGBtoHSLFilter extends FilterPipeObject{

    private String name;

    public RGBtoHSLFilter(String name) {
        super("YUVFilter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());

        float[] a_channel = new float[data.getnCanales()];
        for(int k = 0 ;k<data.getnCanales();k++){
           a_channel[k]=0;
        }
        //identifica el espacio de canal y asi las operacion R=0.3 G= 0.59 B=0.11
        boolean RGB=true;;
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            RGB=true;
            break;
        case ImageData.BGR:
            RGB=false;
            break;
      }
        byte[] hsl=new byte[3];
       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){

                if(RGB){
                  hsl= toHSL(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],hsl);

                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]=hsl[0];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]=hsl[1];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]=hsl[2];
                }else{
                  hsl= toHSL(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],hsl);

                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]=hsl[0];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]=hsl[1];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]=hsl[2];
                }



          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }


    private byte[] toHSL(byte R,byte G, byte B,byte[] hsl){
        int[] rgb = new int[3];
        float aux;
        int max=Integer.MIN_VALUE,min=Integer.MAX_VALUE;

//        fuente : http://en.literateprograms.org/RGB_to_HSV_color_space_conversion_(C)#chunk def:compute value
        rgb[0]=BytesToInt(R);
        rgb[1]=BytesToInt(G);
        rgb[2]=BytesToInt(B);
        for(int i:rgb){
            if(i>max)
                max=i;

            if(i<min)
                min=i;

        }
        hsl[2]=generateByte((max+min)/2);

        if(max==0 || max==min){
            hsl[0]=hsl[1]=0x0;
            return hsl;
        }

        hsl[1] =generateByte((int)(255*(max - min)/((max+min)/2)));
        if (hsl[1] == 0x0) {
            hsl[0] = 0x0;
            return hsl;
        }

        if (max == rgb[0]) {
            hsl[0] =generateByte( (int)(0 + 43*(rgb[1] - rgb[2])/(max - min)));
        } else if (max == rgb[1]) {
            hsl[0] =generateByte( (int)(85 + 43*(rgb[2] - rgb[0])/(max - min)));
        } else /* rgb_max == rgb.b */ {
            hsl[0] =generateByte( (int)(171 + 43*(rgb[0] - rgb[1])/(max - min)));
        }



        return hsl;
    }


    private byte generateByte(int integer) {
        byte[] byteStr = new byte[4];
        byteStr[0]=(byte)((integer & 0xff000000)>>>24);
        byteStr[1]=(byte)((integer & 0x00ff0000)>>>16);
        byteStr[2]=(byte)((integer & 0x0000ff00)>>>8);
        byteStr[3]=(byte)((integer & 0x000000ff));
        return byteStr[3];
        }
}
