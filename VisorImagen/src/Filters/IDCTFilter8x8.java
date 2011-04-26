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
public class IDCTFilter8x8 extends FilterPipeObject{

    private String name;
    private int channel;

    public IDCTFilter8x8(String name) {
        super("Inverse Discrete cosine transform 8x8 " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
        this.channel = channel;
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());


        int nChannels = data.getnCanales();

        int N = 8;
        int M = 8;

//        se divide la imagen en piezas de 8x8 y a cada una se le hace el proceso de la DCT como en el DCTFilter
        int[][] valuesY = new int[N][M];
        int[][] valuesI = new int[N][M];
        int[][] valuesQ = new int[N][M];
        int[][] q =  {{16,11,10,16,24,40,51,61},
                        {12,12,14,19,26,58,60,55},
                        {14,13,16,24,40,57,69,56},
                        {14,17,22,29,51,87,80,62},
                        {18,22,37,56,68,109,103,77},
                        {24,35,55,64,81,104,113,92},
                        {49,64,78,87,103,121,120,101},
                        {72,92,95,98,112,100,103,99}};
        double cv,cu,sumY,sumI,sumQ;
for (int offsetX = 0; offsetX < data.getWidth(); offsetX+=8){
for (int offsetY = 0; offsetY < data.getHeight(); offsetY+=8){

    for (int i = 0; i < N; i++){// sumatorias
          for (int j = 0; j < M; j++){
            valuesY[i][j]=(int)(data.bytesImage[((j + offsetY) * data.getWidthStep() + i +offsetX ) * nChannels + 0])*q[i][j];
            valuesI[i][j]=(int)(data.bytesImage[((j + offsetY) * data.getWidthStep() + i +offsetX ) * nChannels + 1])*q[i][j];
            valuesQ[i][j]=(int)(data.bytesImage[((j + offsetY) * data.getWidthStep() + i +offsetX ) * nChannels + 2])*q[i][j];
        }
    }

        for (int u = 0; u < N; u++) {
            for (int v = 0; v < M; v++){
               if(v==0){
                   cv=0.35355339059327376220042218105242;//  1/sqrt(2)
               }else{
                   cv=0.5;
               }

               if(u==0){
                   cu=0.35355339059327376220042218105242;//  1/sqrt(2)
               }else{
                   cu=0.5;
               }
               sumY=sumI=sumQ=0;

                for (int i = 0; i < N; i++){// sumatorias
                     for (int j = 0; j < M; j++){
                        sumY+=cv*cu*valuesY[i][j]*(Math.cos((Math.PI/8)*(i+0.5)*u))*(Math.cos((Math.PI/8)*(j+0.5)*v));
                        sumI+=cv*cu*valuesI[i][j]*(Math.cos((Math.PI/8)*(i+0.5)*u))*(Math.cos((Math.PI/8)*(j+0.5)*v));
                        sumQ+=cv*cu*valuesQ[i][j]*(Math.cos((Math.PI/8)*(i+0.5)*u))*(Math.cos((Math.PI/8)*(j+0.5)*v));
                     }
                }

               
               sumY+=128;
               sumI+=128;
               sumQ+=128;




               data.bytesImage[((v + offsetY) * data.getWidthStep() + u +offsetX ) * nChannels + 0] = generateByte((int)(sumY));
               data.bytesImage[((v + offsetY) * data.getWidthStep() + u +offsetX ) * nChannels + 1] = generateByte((int)(sumI));
               data.bytesImage[((v + offsetY) * data.getWidthStep() + u +offsetX ) * nChannels + 2] = generateByte((int)(sumQ));

            }
        }
    }
   }
        this.dataOut.setImageData(data);

        System.out.println("Internal update DCT : " + name);
        return  true;
    }

    public double Alpha(int i,int N){
        double temp = Math.sqrt(N);
        if (i==0)
            return 1./temp;
        else
            return 1.4142135623730950488016887242097/temp;
    }

    public double Lambda(int i){
        if (i==0)
            return 1./1.4142135623730950488016887242097;
        else
            return 1.;
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
}
