/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Filters;
import Data.ImageData;
import Operator.Histograma;
import PipeLine.*;
import View.Lienzo;

/**
 *
 * @author Jhon
 */
public class EqualizationFilter extends FilterPipeObject{

    private String name;
    private Histograma G;
    private Histograma R;
    private Histograma B;
    private Lienzo acumulado;
    private Lienzo EqualR;
    private Lienzo EqualG;
    private Lienzo EqualB;

    public EqualizationFilter(String name,Histograma r,Histograma g, Histograma b) {
        super("Equalization " + name);
        G=g;
        R=r;
        B=b;
        acumulado = new Lienzo();
        EqualR = new Lienzo();
        EqualG = new Lienzo();
        EqualB = new Lienzo();
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());
        int[] hist_R_acumulado = new int[256];
        int[] hist_G_acumulado = new int[256];
        int[] hist_B_acumulado = new int[256];
        int[] histR=R.getHist();
        int[] histG=G.getHist();
        int[] histB=B.getHist();
        int[] valorR = new int[256];
        int[] valorG = new int[256];
        int[] valorB = new int[256];

        for(int i=0;i<256;i++){
            hist_R_acumulado[i]=0;
            hist_G_acumulado[i]=0;
            hist_B_acumulado[i]=0;
            valorR[i]=0;
            valorG[i]=0;
            valorB[i]=0;
        }
        hist_R_acumulado[0]=histR[0];
        hist_G_acumulado[0]=histG[0];
        hist_B_acumulado[0]=histB[0];

        for(int i=1;i<256;i++){
             hist_R_acumulado[i]=hist_R_acumulado[i-1]+histR[i];
             hist_G_acumulado[i]=hist_G_acumulado[i-1]+histG[i];
             hist_B_acumulado[i]=hist_B_acumulado[i-1]+histB[i];
        }




        for(int i=0;i<256;i++){
            valorR[i]=Math.round(((float)((float)hist_R_acumulado[i]-(float)R.menor())/((float)(data.getWidth()*data.getHeight())-R.menor()))*255);
            valorG[i]=Math.round(((float)((float)hist_G_acumulado[i]-(float)G.menor())/((float)(data.getWidth()*data.getHeight())-G.menor()))*255);
            valorB[i]=Math.round(((float)((float)hist_B_acumulado[i]-(float)B.menor())/((float)(data.getWidth()*data.getHeight())-B.menor()))*255);
        }

        boolean RGB=true;
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            RGB=true;
            break;
        case ImageData.BGR:
            RGB=false;
            break;
      }

        for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                if(RGB){
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]= generateByte(valorR[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0])]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]= generateByte(valorG[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1])]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]= generateByte(valorB[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2])]);
                }else{
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]= generateByte(valorR[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2])]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]= generateByte(valorG[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1])]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]= generateByte(valorB[BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0])]);
                }



          }
        }

        int[] vacio = new int[256];
        for(int i=0;i<256;i++)
            vacio[i]=0;

        Histograma h = new Histograma(data,0);
        Histograma h1 = new Histograma(data,1);
        Histograma h2 = new Histograma(data,2);
        EqualR.color(0);
        EqualR.setMayor(25);
        EqualR.getDatos(h.getHist());
        

        EqualG.color(1);
        EqualG.setMayor(25);
        EqualG.getDatos(h1.getHist());
        
        
        EqualB.color(2);
        EqualB.setMayor(25);
        EqualB.getDatos(h2.getHist());
        

        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }


    private byte generateByte(int integer) {
        byte[] byteStr = new byte[4];
        byteStr[0]=(byte)((integer & 0xff000000)>>>24);
        byteStr[1]=(byte)((integer & 0x00ff0000)>>>16);
        byteStr[2]=(byte)((integer & 0x0000ff00)>>>8);
        byteStr[3]=(byte)((integer & 0x000000ff));
        return byteStr[3];
        }

    public Lienzo getAcumulado(){
        return acumulado;
    }

    /**
     * @return the EqualR
     */
    public Lienzo getEqualR() {
        return EqualR;
    }

    /**
     * @return the EqualG
     */
    public Lienzo getEqualG() {
        return EqualG;
    }

    /**
     * @return the EqualB
     */
    public Lienzo getEqualB() {
        return EqualB;
    }
}
