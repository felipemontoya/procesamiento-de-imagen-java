/***************************************************************************************************************/
/*    Especificaciones de la Programación
 *    Patrón
 *    Versión 1.1
 *
 *    General:
 *          - Toda clase debe tener la información de esta especificación.
 *          - Todo procedimiento/variable programado será debidamente comentado dependiendo de su naturaleza.
 *          - Todas las variables, clases y métodos deberán ser nombradas en ingles y dependiendo de su función.
 *    Paquetes:
 *          - Toda clase estará incluida en un paquete.
 *          - El nombre del paquete deberá ser acorde a las clases contenidas en el.
 *          - Si son paquetes de prueba deben estar identificados iniciando con "test_nombredelpaquete".
 *          - Si son paquetes que serán utilizados temporalmente para ser mas tarde eliminador irán identificados
 *            iniciando con "temp_nombredelpaquete".
 *
 *    Clases:
 *          - Toda clase iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: ClaseEjemplo
 *          - El nombre de la clase debe identificar su funcionalidad.
 *          - Antes iniciar la clase debe ser comentado para que sirve dicha clase y como es su constructor.
 *          - Si son clases de prueba irán identificados como "cTest_NombreClase"
 *
 *    Métodos
 *          - Toda método iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: MetodoEjemplo
 *          - El nombre de el método debe identificar su funcionalidad. Si el método es un get o un set ira en minúscula
 *          - Antes iniciar el método debe ser comentado para que sirve dicha clase, sus entradas y salidas si tiene
 *
 *    Variables
 *          - Todo nombre de variable inicia en minúsculas, si el nombre de la variable está compuesta por mas palabras
 *            Las siguientes palabras deberán iniciar con la primera letra en mayúscula ej.: variableEjemplo.
 *          - El nombre de la variable debe tener sentido acorde a su valor y su utilidad.
 *          - si son variables auxiliares o temporal deben ir identificados como a_nombreVariable o t_nombreVariable.
 *          - las variables globales ya están plenamente identificadas con color verde.
 *          - Cuando se declara una variable debe comentarse una breve funcionalidad de dicha variable.
 *
 *   léase y cúmplase
 */
/***************************************************************************************************************/

package Filters;
import Data.ImageData;
import PipeLine.*;

/**
 *
 * @author Jhon
 */
public class InterpolateFilter extends FilterPipeObject{

    private String name;
    int dimX;
    int dimY;

    public InterpolateFilter(String name, int x,int y) {
        super("InterpolateFilter " + name);
        dimX = x;
        dimY = y;
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData(),dimX,dimY);
        int ii;
        int jj;
        byte r,g,b;
        System.out.print("Nuevo Array: "+data.bytesImage.length);
       for(int i = 0;i<dataIn.getImageData().getHeight();i++){
            for(int j = 0;j<dataIn.getImageData().getWidth();j++){
            ii = Math.round((float)i * (float) (dimY-1) / (float) dataIn.getImageData().getHeight());
            jj = Math.round((float)j * (float) (dimX-1) / (float) dataIn.getImageData().getWidthStep());
            r =dataIn.getImageData().bytesImage[(i * dataIn.getImageData().getWidth() + j ) * dataIn.getImageData().getnCanales() + 0];
            data.bytesImage[(ii * data.getWidthStep() + jj ) * data.getnCanales() + 0] = r;
            g =dataIn.getImageData().bytesImage[(i * dataIn.getImageData().getWidth() + j ) * dataIn.getImageData().getnCanales() + 1];
            data.bytesImage[(ii * data.getWidthStep() + jj ) * data.getnCanales() + 1] = g;
            b =dataIn.getImageData().bytesImage[(i * dataIn.getImageData().getWidth() + j ) * dataIn.getImageData().getnCanales() + 2];
            data.bytesImage[(ii * data.getWidthStep() + jj ) * data.getnCanales() + 2] = b;
            }
            
       }

        int[] v1,v2,v3,v4;
        int[] p = new int[3];
        for(int i = 1;i<data.getHeight()-1;i++){
            for(int j = 1;j<data.getWidth()-1;j++){
             v1= buscarArribaIzquierda(data,i,j);
             v2= buscarAbajoIzquierda(data,i,j);
             v3= buscarArribaDerecha(data,i,j);
             v4= buscarAbajoDerecha(data,i,j);
             p[0] = (v1[0]+v2[0]+v3[0]+v4[0])/4;
             p[1] = (v1[1]+v2[1]+v3[1]+v4[1])/4;
             p[2] = (v1[2]+v2[2]+v3[2]+v4[2])/4;
             data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]=generateByte(p[0]);
             data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]=generateByte(p[1]);
             data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]=generateByte(p[2]);
            }
        }

        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int[] buscarArribaIzquierda(ImageData data,int i,int j){
        boolean v = false;
        int[] s = new int[3];
        for(int k = j-1;k>0;k--){
            if(BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }

        /*while(!v){
            i++;
            for(int k = j-1;k>0;k--){
            if(BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }
        }*/
        return s;

    }

    public int[] buscarAbajoIzquierda(ImageData data,int i,int j){
        boolean v = false;
        int[] s = new int[3];
        for(int k = j-1;k>0;k--){
            if(BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }

        /*while(!v){
            i--;
            for(int k = j-1;k>0;k--){
            if(BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }
        }*/

        return s;

    }



    public int[] buscarArribaDerecha(ImageData data,int i,int j){
        boolean v = false;
        int[] s = new int[3];
        for(int k = j+1;k<data.getWidth();k++){
            if(BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }

        /*while(!v){
            i++;
            for(int k = j+1;k<data.getWidth();k++){
            if(BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i+1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }
        }*/
        return s;

    }

    public int[] buscarAbajoDerecha(ImageData data,int i,int j){
        boolean v = false;
        int[] s = new int[3];
        for(int k = j+1;k<data.getWidth();k++){
            if(BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }

        /*while(!v){
            i--;
            for(int k = j+1;k<data.getWidth();k++){
            if(BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1])!=1 && BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2])!=1 ){
                s[0]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 0]);
                s[1]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 1]);
                s[2]=BytesToInt(data.bytesImage[((i-1) * data.getWidthStep() + (k) ) * data.getnCanales() + 2]);
                v=true;
                break;
            }

        }
        }*/
        return s;

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