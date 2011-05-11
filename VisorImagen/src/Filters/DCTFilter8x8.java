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
public class DCTFilter8x8 extends FilterPipeObject{

    private String name;
    private int channel;

    public DCTFilter8x8(String name, int channel) {
        super("Discrete cosine transform 8x8 " + name);
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
        double[][] q =  {{16,11,10,16,24,40,51,61},
                        {12,12,14,19,26,58,60,55},
                        {14,13,16,24,40,57,69,56},
                        {14,17,22,29,51,87,80,62},
                        {18,22,37,56,68,109,103,77},
                        {24,35,55,64,81,104,113,92},
                        {49,64,78,87,103,121,120,101},
                        {72,92,95,98,112,100,103,99}};
        double cv,cu,sumY,sumI,sumQ;

        double raiz1n = Math.sqrt((float)(1.0/(float)N));
        double raiz2n = Math.sqrt((float)(2.0/(float)N));
        
        double outputR[][] = new double[N][N];
        double outputG[][] = new double[N][N];
        double outputB[][] = new double[N][N];
        float inputR[][] = new float[N][N];
        float inputG[][] = new float[N][N];
        float inputB[][] = new float[N][N];

        int i;
        int j;
for (int offsetX = 0; offsetX < data.getWidth(); offsetX+=8){
for (int offsetY = 0; offsetY < data.getHeight() ; offsetY+=8){

    for (i = 0; i < N; i++){// sumatorias
          for (j = 0; j < M; j++){
            inputR[i][j]=BytesToInt(data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 0]);
            inputG[i][j]=BytesToInt(data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 1]);
            inputB[i][j]=BytesToInt(data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 2]);
        }
    }
    outputR=forwardDCT(inputR);
     outputG=forwardDCT(inputG);
    outputB=forwardDCT(inputB);


    for (i = 0; i < N; i++){// sumatorias
          for (j = 0; j < M; j++){
            data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 0] = (byte)((int)Math.round(outputR[i][j]));
            data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 1] = (byte)((int)Math.round(outputG[i][j]));
            data.bytesImage[((j + offsetY) * data.getWidthStep() + i + offsetX ) * nChannels + 2] = (byte)((int)Math.round(outputB[i][j]));
        }
    }
        
    }
   }
        this.dataOut.setImageData(data);

        System.out.println("Internal update DCT : " + name);
        return  true;
    }

     public double[][] forwardDCT(float input[][]){
    double output[][] = new double[8][8];
    double tmp0, tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7;
    double tmp10, tmp11, tmp12, tmp13;
    double z1, z2, z3, z4, z5, z11, z13;
    int i;
    int j;

    // Subtracts 128 from the input values
    for (i = 0; i < 8; i++) {
      for(j = 0; j < 8; j++) {
        output[i][j] = (input[i][j] - 128.0);
        // input[i][j] -= 128;

      }
    }

    for (i = 0; i < 8; i++) {
      tmp0 = output[i][0] + output[i][7];
      tmp7 = output[i][0] - output[i][7];
      tmp1 = output[i][1] + output[i][6];
      tmp6 = output[i][1] - output[i][6];
      tmp2 = output[i][2] + output[i][5];
      tmp5 = output[i][2] - output[i][5];
      tmp3 = output[i][3] + output[i][4];
      tmp4 = output[i][3] - output[i][4];

      tmp10 = tmp0 + tmp3;
      tmp13 = tmp0 - tmp3;
      tmp11 = tmp1 + tmp2;
      tmp12 = tmp1 - tmp2;

      output[i][0] = tmp10 + tmp11;
      output[i][4] = tmp10 - tmp11;

      z1 = (tmp12 + tmp13) * 0.707106781;
      output[i][2] = tmp13 + z1;
      output[i][6] = tmp13 - z1;

      tmp10 = tmp4 + tmp5;
      tmp11 = tmp5 + tmp6;
      tmp12 = tmp6 + tmp7;

      z5 = (tmp10 - tmp12) * 0.382683433;
      z2 = 0.541196100 * tmp10 + z5;
      z4 = 1.306562965 * tmp12 + z5;
      z3 = tmp11 * 0.707106781;

      z11 = tmp7 + z3;
      z13 = tmp7 - z3;

      output[i][5] = z13 + z2;
      output[i][3] = z13 - z2;
      output[i][1] = z11 + z4;
      output[i][7] = z11 - z4;
    }

    for (i = 0; i < 8; i++) {
      tmp0 = output[0][i] + output[7][i];
      tmp7 = output[0][i] - output[7][i];
      tmp1 = output[1][i] + output[6][i];
      tmp6 = output[1][i] - output[6][i];
      tmp2 = output[2][i] + output[5][i];
      tmp5 = output[2][i] - output[5][i];
      tmp3 = output[3][i] + output[4][i];
      tmp4 = output[3][i] - output[4][i];

      tmp10 = tmp0 + tmp3;
      tmp13 = tmp0 - tmp3;
      tmp11 = tmp1 + tmp2;
      tmp12 = tmp1 - tmp2;

      output[0][i] = tmp10 + tmp11;
      output[4][i] = tmp10 - tmp11;

      z1 = (tmp12 + tmp13) * 0.707106781;
      output[2][i] = tmp13 + z1;
      output[6][i] = tmp13 - z1;

      tmp10 = tmp4 + tmp5;
      tmp11 = tmp5 + tmp6;
      tmp12 = tmp6 + tmp7;

      z5 = (tmp10 - tmp12) * 0.382683433;
      z2 = 0.541196100 * tmp10 + z5;
      z4 = 1.306562965 * tmp12 + z5;
      z3 = tmp11 * 0.707106781;

      z11 = tmp7 + z3;
      z13 = tmp7 - z3;

      output[5][i] = z13 + z2;
      output[3][i] = z13 - z2;
      output[1][i] = z11 + z4;
      output[7][i] = z11 - z4;
    }

    return output;
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
