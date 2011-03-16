/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test_Draw;

import java.lang.reflect.Array;
import java.util.Arrays.*;
import java.io.* ;
import Utilities.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arzobispo
 */
public class cTest_LZW {

    byte[] inByteArray;
    int[] table;

    public cTest_LZW() {
        try {
            
            // generar un inputSteam
            String array = "the/rain/in/Spain/falls/mainly/on/the/plain";
            ByteArrayInputStream bais = new ByteArrayInputStream(array.getBytes());
            BitInputStream inputStream = new BitInputStream(bais);

//            Ejemplo para generar un bitInputStream desde un array
//            byte[] array = new byte[100];
//            ByteArrayInputStream bais = new ByteArrayInputStream(array);
//            BitInputStream is = new BitInputStream(bais);

            // codificarlo
            BitOutputStream oStream = encode(inputStream, array.length());

            // decodificarlo
//            String array2 = "@hP/ aѮ";
//            ByteArrayInputStream bais2 = new ByteArrayInputStream(array2.getBytes());
//            BitInputStream iStream = new BitInputStream(bais2);
            BitInputStream iStream = new BitInputStream("bitOutputStream.txt");
            decode(iStream);



//            readBitStream();

        } catch (IOException ex) {
            Logger.getLogger(cTest_LZW.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String args[]){
        new cTest_LZW();
    }

 
    public BitOutputStream encode(BitInputStream inputStream, int length)throws IOException{
        // Primero se crea un diccionario y se llenan los primeros 256 valores
        String[] dict = new String[65536];
        int iTable;
        for (iTable = 0; iTable<256;iTable++)
            dict[iTable]=Character.toString((char)iTable);


        // Se crea una variable de salida y otras variables temporales del algoritmo
        // Los nombres son consistentes con la explicación de http://www.dspguide.com/ch27/5.htm
        char k;
        String w = "";
        String wk;
        int[] output = new int[65536];
        int iOutput = 0;

        //Por compatibilidad con el método de descompresión se crea un BitOutputStream
        BitOutputStream outputStream = new BitOutputStream("bitOutputStream.txt");

//        algoritmo de compresión
        for (int i = 0; i <= length; i++){

            // primero se obtiene el valor actual y a su vez se concatena con el
            // último valor de w que el algoritmo tenga
            k = (char)inputStream.readBits(8);
            wk = w.concat(Character.toString(k));


            // Busca a wk en el diccionario, si lo encuentra retorna verdadero en isOnDict
            boolean isOnDict = false;
            for (int j = 0; j < iTable; j++){
                if(dict[j].equals(wk)){
                    isOnDict = true;
                    break;
                }
            }


            // De acuerdo a la presencia de wk en el diccionario: si esta, lo asigna a w y termina
            // si no, lo añade al diccionario, y retorna el código de w
            if (isOnDict){
               w = wk;
            }
            else
            {
                // añadir wk
                dict[iTable++] = wk;

                //buscar el código de w
                int codeW = 0;
                for (int j = 0; j < iTable; j++){
                    if(dict[j].equals(w)){
                        codeW = j;
                        break;
                    }
                }

                // retornar el código de w como un array de enteros
                output[iOutput] = codeW;

                int writeBits;
                if (iOutput < 4096)
                    writeBits = 12;
                else
                    if (iOutput < 8192)
                        writeBits = 13;
                    else
                        if (iOutput < 16384)
                            writeBits = 14;
                        else
                            if (iOutput < 32768)
                                writeBits = 15;
                            else
                                if (iOutput < 65536)
                                    writeBits = 16;
                                else
                                    throw new IOException("La tabla de codificación superó su máximo tamaño");

                outputStream.writeBits(writeBits, codeW);
                iOutput++;

                
//                // Opcional: Imprimir en pantalla
                if (codeW < 256)
                    System.out.println("output " + (char)codeW);
                else
                    System.out.println("output " + codeW);

                // Asigna el valor actual a w para continuar el siguiente ciclo
                w = Character.toString(k);
            }
      }
        outputStream.flush();
        return outputStream;
    }

    public void decode(BitInputStream inputStream)throws IOException{

//        inputStream = new BitInputStream("bitOutputStream.txt");

        // Primero se crea un diccionario y se llenan los primeros 256 valores
        String[] dict = new String[65536];
        int iTable;
        for (iTable = 0; iTable<256;iTable++)
            dict[iTable]=Character.toString((char)iTable);


        // Se crea una variable de salida y otras variables temporales del algoritmo
        // Los nombres son consistentes con la explicación de http://www.dspguide.com/ch27/5.htm
        int intK;
        String w = "";


        // algoritmo de compresión
        // Primer valor
        intK = inputStream.readBits(12);
        System.out.println("leido " + dict[intK]);  // salida
        w=dict[intK];
      
        while(true){

            intK = inputStream.readBits(12);
            if (intK == -1){
                System.out.println("Lectura terminada");
                break;
            }
 
            System.out.println("leido " + dict[intK]); // salida
            w = w.concat(dict[intK]);
            dict[iTable++] = w;

            w = dict[intK];

        }


    }

   




}
