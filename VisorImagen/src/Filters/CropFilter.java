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
    private boolean markOnly = true;
    private int x0,y0,width,height;

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Crop))
        {
            isThereMessage = true;
            try{
                if(msg.bValue2)
                {
                    x0 = msg.iValue1;
                    if (x0 < 0)
                        x0 = 0;
                    y0 = msg.iValue2;
                    if (y0 < 0)
                        y0 = 0;
                    width = msg.iValue3;
                    if (width > (dataIn.getImageData().getWidth()-x0))
                        width = dataIn.getImageData().getWidth()-x0;
                    height = msg.iValue4;
                    if (height > (dataIn.getImageData().getHeight()-y0))
                        height = dataIn.getImageData().getHeight()-y0;
                }
                if(msg.bValue1)
                    markOnly = true;
                else
                    markOnly = markOnly^true;
            }
            catch(NumberFormatException e) {}
            
        }
        return true;
    }
    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data;
        if(!isThereMessage || markOnly)
        {
            data = new ImageData(this.dataIn.getImageData());
            System.arraycopy(dataIn.getImageData().bytesImage, 0, data.bytesImage, 0, dataIn.getImageData().bytesImage.length);
        }
        else
            data = new ImageData(this.dataIn.getImageData(),width,height);

//        System.out.println("Filtro de crop por dentro " + x0 + " "+ y0);

        int inWidth = dataIn.getImageData().getWidth();
        int inHeight = dataIn.getImageData().getHeight();
        //Sigo trabajando 01/04/2011 . Felipe
        int nChannels = dataIn.getImageData().getnCanales();

        

        if(isThereMessage)
        if (markOnly)
        {
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                    data.bytesImage[ ((i + (inHeight - y0 - height))*data.getWidthStep() + j + x0)*nChannels ] = (byte)255;
            }
        }
        else
        {
            for (int i = 0; i < height; i++)
            {
                System.arraycopy(   dataIn.getImageData().bytesImage,
                                    ((inHeight -y0 - i)*dataIn.getImageData().getWidthStep() + x0)*nChannels,
                                    data.bytesImage,
                                    (height - i -1) * data.getWidthStep() * nChannels,
                                    width*nChannels) ;
//                data.bytesImage[ ((i + (inHeight - y0 - height))*data.getWidthStep() + j + x0)*nChannels ] = (byte)255;
            }
        }
//            System.arraycopy(dataIn.getImageData().bytesImage,
//                                (( i +  y0) * dataIn.getImageData().getWidthStep() + x0) * nChannels,
//                                data.bytesImage,
//                                (i) * data.getWidthStep() * nChannels,
//                                width*nChannels);

        this.dataOut.setImageData(data);

//        System.out.println("Internal update Crop : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}