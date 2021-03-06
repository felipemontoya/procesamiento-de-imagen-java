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
package PipeLine;

/**
 *
 * @author Felipe
 */
public abstract class PipeObject {

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }


public enum Type {sink, filter, source};

private String name;
private Type type;
private PipeObject lastElement;
//public PipeObject nextElement;

public PipeObject(String name, Type type) {
    this.name = name;
    this.type = type;
    lastElement = null;
//    nextElement = null;

}

protected abstract void setDataIn(DataPackage DataIn);
public abstract DataPackage getDataOut();

public boolean Update(){

//    System.out.println("Update PipeObject -> nombre: " + name);
    boolean isPipelineReady = false;

    if (!type.equals(Type.source)){
        if (lastElement != null)
            isPipelineReady = this.getLastElement().Update();
        else{
            System.out.println("El pipeline se encuentra roto en :" + name);
            return false;
        }
            
    }
    else
        isPipelineReady = true;


    if (isPipelineReady){
        
        return this.InternalUpdate();
    }
    else
        return false;
}

public boolean PassMessage(PipeMessage msg){

//    primero pasa el mensaje de lado a lado
    boolean isPipelineReady = false;

    if (!type.equals(Type.source)){
        if (lastElement != null)
            isPipelineReady = this.getLastElement().PassMessage(msg);
        else{
            System.out.println("Pasando un mensaje el pipeline se encuentra roto en :" + name);
            return false;
        }

    }
    else
        isPipelineReady = true;

//cuando el mensaje llegó al final cada filtro lee el mensaje
    if (isPipelineReady){

        return this.ReadMessage(msg);
    }
    else
        return false;
}

    /**
     * @return the lastElement
     */
    public PipeObject getLastElement() {
        return lastElement;
    }

    /**
     * @param lastElement the lastElement to set
     */
    public void setLastElement(PipeObject lastElement) {
        if (!type.equals(Type.source)){
            this.lastElement = lastElement;

        }
        else
            System.out.println("No se puede definir un objeto anterior a un source");
    }



public abstract boolean InternalUpdate();
public abstract boolean ReadMessage(PipeMessage msg);

}
