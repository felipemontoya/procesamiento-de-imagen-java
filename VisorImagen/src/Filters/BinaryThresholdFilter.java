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
public class BinaryThresholdFilter extends FilterPipeObject{

    private String name;
    private int Level;

    public BinaryThresholdFilter(String name,int level) {
        super("GrayScaleFilter " + name);
        this.name = name;
        Level = level;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Binary))
        {
            Level = Math.abs(msg.iValue1);
        }
        return true;
    }

    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());

        byte max = generateByte(255);
       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){

                if(BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0])>=Level){


                for(int k = 0 ;k<3;k++){
                    data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + k] = max;
                }
                }else{
                   for(int k = 0 ;k<3;k++){
                    data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + k] = 0;
                }
                }

          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update GrayScale : " + name);
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
}