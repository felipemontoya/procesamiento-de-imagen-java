/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;

/**
 *
 * @author Felipe
 */
public class MegaImagen {


    //Atributos importantes

 private int  nSize;  // sizeof(MegaImagen)
 private int  dSize;  // sizeof(datosImagen)
 private int  nCanales;
 private int  depth; // tamaño del pixel
 private int  espacioColor;
 private int  interlineado;
 private int  origen;
 private int  alineacion;
 private int  width;
 private int  height;
 private int  widthStep;
    //roi;
    @SuppressWarnings("PublicField")
 public byte datosImagen[][];



 //Metodos
 public MegaImagen(int width, int height, int origen, int nCanales, int depth, int alineacion, int espacioColor){
     boolean completado = false;
    if (this.setWidth(width))
    if (this.setHeight(height))
    if (this.setOrigen(origen))
    if (this.setnCanales(nCanales))
    if (this.setDepth(depth))
    if (this.setAlineacion(alineacion))
    if (this.setEspacioColor(espacioColor)){

        int bloques = this.getWidth() / this.getAlineacion();
        if ( this.getWidth() % this.getAlineacion() ==0){
            bloques--;
         }
        if (this.setWidthStep((bloques+1) * this.getAlineacion()))
        //if (this.setnSize(sizeof(this)); aún no esta implementado
        if (this.setdSize(this.getWidthStep()*this.getHeight()*this.getDepth())){


            datosImagen = new byte[this.getWidthStep()][this.getHeight()];
            completado = true;
        }
     }

     if (!completado)
         System.out.println("Error creando la imagen");

     
 }

    /**
     * @return the nSize
     */
    public int getnSize() {
        return nSize;
    }

    /**
     * @param nSize the nSize to set
     */
    public boolean setnSize(int nSize) {
        this.nSize = nSize;
        return true;
    }

    /**
     * @return the dSize
     */
    public int getdSize() {
        return dSize;
    }

    /**
     * @param dSize the dSize to set
     */
    public boolean setdSize(int dSize) {
        if (dSize >= 0){
            this.dSize = dSize;
            return true;
        }
        else
            return false;
    }

    /**
     * @return the nCanales
     */
    public int getnCanales() {
            return nCanales;
    }

    /**
     * @param nCanales the nCanales to set
     */
    private boolean setnCanales(int nCanales) {
        if (nCanales < 5 || nCanales > 0){
            this.nCanales = nCanales;
            return true;
        }
        else
            return false;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @param depth the depth to set
     */
    private boolean setDepth(int depth) {
        if (depth == MegaImagen.PROF_F64 ||
            depth == MegaImagen.PROF_F32 ||
            depth == MegaImagen.PROF_S32 ||
            depth == MegaImagen.PROF_S16 ||
            depth == MegaImagen.PROF_U16 ||
            depth == MegaImagen.PROF_S8 ||
            depth == MegaImagen.PROF_U8 ){
            this.depth = depth;
            return true;
        }
        else{
            System.out.println("Error en la propiedad de la profundidad");
            return false;
        }


    }

    /**
     * @return the espacioColor
     */
    public int getEspacioColor() {
        return espacioColor;
    }

    /**
     * @param espacioColor the espacioColor to set
     */
    private boolean setEspacioColor(int espacioColor) {
        if (espacioColor == MegaImagen.RGB ||
            espacioColor == MegaImagen.RGBA ||
            espacioColor == MegaImagen.CMYK ||
            espacioColor == MegaImagen.HSL ||
            espacioColor == MegaImagen.HSV ){
            this.espacioColor = espacioColor;
            return true;
        }
        else{
            System.out.println("Error en la propiedad del espacio de color");
            return false;
        }
    }

    /**
     * @return the interlineado
     */
    public int getInterlineado() {
        return interlineado;
    }

    /**
     * @param interlineado the interlineado to set
     */
    public boolean setInterlineado(int interlineado) {
        if( interlineado == MegaImagen.INTERLINEADO_NO || interlineado == MegaImagen.INTERLINEADO_SI){
            this.interlineado = interlineado;
            return true;
        }
         else{
             System.out.println("Error en la propiedad del interlineado");
             return false;
        }
    }

    /**
     * @return the origen
     */
    public int getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    private boolean setOrigen(int origen) {
        if( origen == MegaImagen.ARRIBA_IZQ || origen == MegaImagen.ABAJO_IZQ){
            this.origen = origen;
            return true;
        }
         else{
             System.out.println("Error en la propiedad del origen");
             return false;
        }
    }

    /**
     * @return the alineacion
     */
    public int getAlineacion() {
        return alineacion;
    }

    /**
     * @param alineacion the alineacion to set
     */
    private boolean setAlineacion(int alineacion) {
        if( alineacion == MegaImagen.ALINEADO_4 || alineacion == MegaImagen.ALINEADO_8){
            this.alineacion = alineacion;
            return true;
        }
         else{
             System.out.println("Error en la propiedad de la alineación");
             return false;
        }
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    private boolean setWidth(int width) {
        if (width >= 0){
            this.width = width;
            return true;
        }
        else
            return false;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    private boolean setHeight(int height) {
        if (height >= 0){
            this.height = height;
            return true;
        }
        else
            return false;
    }

    /**
     * @return the widthStep
     */
    public int getWidthStep() {
        return widthStep;
    }

    /**
     * @param widthStep the widthStep to set
     */
    public boolean setWidthStep(int widthStep) {
        if (widthStep >= 0){
            this.widthStep = widthStep;
            return true;
        }
        else
            return false;
    }


 //Constantes

 //Depth
 public static final int PROF_U8 = 1;
 public static final int PROF_S8 = 1;
 public static final int PROF_U16 = 2;
 public static final int PROF_S16 = 2;
 public static final int PROF_S32 = 4;
 public static final int PROF_F32 = 4;
 public static final int PROF_F64 = 64;

  //espacioColor
 public static final int RGB = 1;
 public static final int RGBA = 2;
 public static final int CMYK = 3;
 public static final int HSV = 4;
 public static final int HSL = 5;

 //Interlineado
 public static final int INTERLINEADO_SI = 0;
 public static final int INTERLINEADO_NO = 1;

 //Origen
 public static final int ARRIBA_IZQ = 0;
 public static final int ABAJO_IZQ = 0;

 //Alineación
 public static final int ALINEADO_8 = 8;
 public static final int ALINEADO_4 = 4;



}
