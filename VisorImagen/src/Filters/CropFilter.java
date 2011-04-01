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
public class CropFilter extends FilterPipeObject{

    private String name;
    public CropFilter(String name) {
        super("CropFilter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    private boolean isThereMessage = false;
    private int x0,y0,width,heigth;

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Crop))
        {
            isThereMessage = true;
            x0 = msg.iValue1;
            y0 = msg.iValue2;
            width = msg.iValue3;
            heigth = msg.iValue4;

        }
        return true;
    }
    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data;
        if(!isThereMessage)
            data = new ImageData(this.dataIn.getImageData());
        else
            data = new ImageData(this.dataIn.getImageData(),width,heigth);

        System.out.println("Filtro de crop por dentro " + x0 + " "+ y0);

        //Sigo trabajando 01/04/2011 . Felipe


        this.dataOut.setImageData(data);

//        System.out.println("Internal update Crop : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}