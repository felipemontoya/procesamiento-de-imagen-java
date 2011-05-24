/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Filters;

/**
 *
 * @author Jhon
 */
import Data.ImageData;
import PipeLine.*;
import java.awt.*;
import java.awt.image.*;



/**
 * http://users.ecs.soton.ac.uk/msn/book/new_demo/fourier/
 *
 * @author Jhon
 */
public class FFTFilter extends FilterPipeObject {

    private String name;
    boolean lowpass;
    int radius;
    Image fourier;
    public FFTFilter(String name, int r , boolean b) {
        super("FFTFilter " + name);
        lowpass=b;
        radius = r;
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    public FFT fft = new FFT();
    public InverseFFT inverse = new InverseFFT();





     @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.FFT))
        {
            radius = msg.iValue1;
            lowpass = msg.bValue1;
        }
        return true;
    }
    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
    public boolean InternalUpdate() {
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());
        int[] intdata = new int[data.getHeight() * data.getWidth()];
        int aux = 0;
        for (int i = 0; i < data.getHeight(); i++) {
            for (int j = 0; j < data.getWidth(); j++) {
                intdata[aux] = BytesToInt(data.bytesImage[(i * data.getWidthStep() + j) * data.getnCanales() + 0]);
                aux++;
            }
        }
        
        fft = new FFT(intdata,  data.getWidth(), data.getHeight());
        fft.intermediate = FreqFilter.filter(fft.intermediate,lowpass,radius);
        ComplexArray output = inverse.transform(fft.intermediate);


      int [] outdata = inverse.getPixels(output);
       //ImageData datau = new ImageData(this.dataIn.getImageData(),output.width,output.height);
        aux = 0;
        byte b;
        for (int i = 0; i < data.getHeight(); i++) {
            for (int j = 0; j < data.getWidth(); j++) {
                b = generateByte(outdata[i * output.width + j]);
                data.bytesImage[(i * data.getWidthStep() + j) * 3 + 0] = b;
                data.bytesImage[(i * data.getWidthStep() + j) * 3 + 1] = b;
                data.bytesImage[(i * data.getWidthStep() + j) * 3 + 2] = b;
                aux++;
            }
        }


        this.dataOut.setImageData(data);

        System.out.println("Internal update FFTFilter : " + name);
        return true;
    }

    public int BytesToInt(byte valor) {
        return (int) valor & 0xFF;

    }

    private byte generateByte(int integer) {
        byte[] byteStr = new byte[4];
        byteStr[0] = (byte) ((integer & 0xff000000) >>> 24);
        byteStr[1] = (byte) ((integer & 0x00ff0000) >>> 16);
        byteStr[2] = (byte) ((integer & 0x0000ff00) >>> 8);
        byteStr[3] = (byte) ((integer & 0x000000ff));
        return byteStr[3];
    }

    public Image furier(){
        return fourier;
    }

}
