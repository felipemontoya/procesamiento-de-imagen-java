/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PipeLine;

/**
 *
 * @author Arzobispo
 */

//esta clase se usa para pasar mensajes a lo largo del pipeline
//las variables se pueden añadir, pero no retirar a menos que se
//garantice el corrento funcionamiento de todos los objetos del pipeline
public class PipeMessage {

    public PipeMessage() {
    }

    public PipeMessage(Receiver destination, String message) {
        this.destination = destination;
        this.message = message;
//        this.iValue1 = iValue1;
//        this.iValue2 = iValue2;
//        this.dValue1 = dValue1;
    }




    public enum Receiver{Rotate,Crop,Channel,Gamma,Binary};

    public Receiver destination;
    public String message;
    public int iValue1;
    public int iValue2;
    public int iValue3;
    public int iValue4;
    public boolean bValue1;
    public boolean bValue2;
    public double dValue1;



}
