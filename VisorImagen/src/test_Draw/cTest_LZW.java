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
    byte[] table;

    public cTest_LZW(byte[] byteArray) {
        this.inByteArray = byteArray;

    }

    public static void main(String args[]){
        try {

            System.out.println("empezando");
            writeBitStream();
            readBitStream();
            System.out.println("terminado");


        } catch (IOException ex) {
            Logger.getLogger(cTest_LZW.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public void populateTable(){
        table = new byte[65536];
        java.util.Arrays.fill(table, (byte)0x0000);
    }

    public static void writeBitStream() throws IOException{

        BitOutputStream os = new BitOutputStream("bitOutputStream.txt");

        int code = 99999;

        os.writeBits(8,code) ;

        os.flush();

//        BitInputStream is = new BitInputStream("bitOutputStream.txt");
//
//        int returnedCode = is.readBits(8);
//
//        System.out.println("Leido: " + returnedCode);
    }

    public static void readBitStream() throws IOException{
        
        byte[] array = new byte[200];
        java.util.Arrays.fill(array, (byte)0xFFFF);

        ByteArrayInputStream bais = new ByteArrayInputStream(array);

        BitInputStream is2 = new BitInputStream(bais);

        int returnedCode2 = is2.readBits(12);

        System.out.println("Leido byteArray: " + returnedCode2);

    }




}
