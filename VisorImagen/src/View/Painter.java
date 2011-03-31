/*
 * VisorMain.java
 *
 * Created on 10-feb-2011, 18:41:14
 */


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



package View;

import PipeLine.SinkPipeObject;
import java.awt.event.MouseListener;
import javax.swing.JInternalFrame;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;


/**
 *
 * @author Felipe
 */
public class Painter extends SinkPipeObject {


    public enum Type {PointToPoint, Texture};

    private String name;
    private Type type;
    private TextureGL texturePainter = null;
    private DrawGL pointPainter = null;

    public Painter(String name, Type type) {
        super(name);
        this.type = type;
        this.name = name;
        switch(this.type){
            case PointToPoint:
                pointPainter = new DrawGL();
                break;
            case Texture:
                texturePainter = new TextureGL();
                break;
        }
   }



    public JInternalFrame getInternalFrame(){
        if(pointPainter!= null){
            return pointPainter;
        }
        else
        if(texturePainter!=null){
            return texturePainter;
        }
        else
            return null;

    }

    @Override
    public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());


        switch(this.type){
            case PointToPoint:
//                pointPainter.DrawGLInit(this.dataIn.getImageData(), name,false);
                break;
            case Texture:
                texturePainter.TextureGLInit(this.dataIn.getImageData(), name);
                break;
        }

        System.out.println("Internal update Painter ->Name: " + name);
        return true;
    }



    /**
     * @param texturePainter the texturePainter to set
     */
    public void setTexturePainter(TextureGL texturePainter) {
        if (this.type.equals(Type.Texture))
            this.texturePainter = texturePainter;
        else
            System.out.println("Error: Un pintor de texturas solo puede recibir un TextureGL como argumento");
    }

    /**
     * @param pointPainter the pointPainter to set
     */
    public void setPointPainter(DrawGL pointPainter) {
        if (this.type.equals(Type.PointToPoint))
            this.pointPainter = pointPainter;
          else
        System.out.println("Error: Un pintor de puntos solo puede recibir un DrawGL como argumento");
    }

}
