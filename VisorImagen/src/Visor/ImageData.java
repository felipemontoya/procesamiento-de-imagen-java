/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;

/**
 *
 * @author Jhon
 */
public class ImageData {
private byte[] bits;//formato bigEndian y littleEndian
    private boolean formato;
double mayor, menor;


    public ImageData(int tamano, boolean formato){
        bits = new byte[tamano];
        this.formato=formato;
        mayor=0;
        menor=0;
   }



    public ImageData(){
        bits = null;
        this.formato=false;
        mayor=0;
        menor=0;
   }


    public void llenarByte(byte[] bits){
        this.setBits(bits);
    }



/* ejemplo bits[2]=2 (00000010) bits[3]=3 (00000011)
#
se aplica bits[2]<<8>
#
en total da 10 00000011 que es el numero 515 ,
#
este es un short de 16 bits, han entrado dos bytes en uno
#
(short[i]=contacenar byte[i]+byte[i+1])
#
los valores negativos estan en complemento a 2
#
*/

        public double[] convertirByteADouble(){
            double[] arrayDouble = new double[getBits().length/2];

            if (isFormato()==true){
            int temp = 0x00000000;
            for (int i = 0, j = 0; j < arrayDouble.length ; j++, temp = 0x00000000){
                temp=(int)getBits()[i++]<<8;
                temp |= (int) (0x000000FF & getBits()[i++]);
                arrayDouble[j]=(double)temp;
            }
            return arrayDouble;
            }

            if(isFormato()==false){ // si el formato es littleEndian
            int temp = 0x00000000;
            for (int i = 0, j = 0; j< arrayDouble.length ;j++, temp = 0x00000000){
                temp=(int)getBits()[i+1]<<8;
                temp |= (int) (0x000000FF & getBits()[(i)]);
                i=i+2;
                arrayDouble[j]=(double)temp;

                if(mayor<arrayDouble[j]){
                mayor=arrayDouble[j];}
                if(menor>arrayDouble[j]) {
                menor=arrayDouble[j];}
            }

            return arrayDouble;
            }else{
            System.out.println("orden de Bytes desconocido o no soportado");
            }
            return arrayDouble;
            }

    /**
     * @return the bits
     */
    public byte[] getBits() {
        return bits;
    }

    /**
     * @param bits the bits to set
     */
    public void setBits(byte[] bits) {
        this.bits = bits;
    }

    /**
     * @return the formato
     */
    public boolean isFormato() {
        return formato;
    }

    /**
     * @param formato the formato to set
     */
    public void setFormato(boolean formato) {
        this.formato = formato;
    }
}
