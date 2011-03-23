/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/***************************************************************************************************************/
/*    Especificaciones de la Programacion
 *    Patron
 *    version 1.0
 *
 *    General:
 *          - Toda clase debe tener la informacion de esta especificacion.
 *          - Todo procedimiento/variable programado sera debidamente comentado dependiento de su naturaleza.
 *          - Todas las variables, clases y metodos deberan ser nombradas en ingles y dependiendo de su funcion.
 *    Paquetes:
 *          - Toda clase estara incluida en un paquete.
 *          - El nombre del paquete debera ser acorde a las clases contenidas en el.
 *          - Si son paquetes de prueba deben estar identificados iniciando con "test_nombredelpaquete".
 *          - Si son paquetes que seran utilizados temporalmente para ser mas tarde eliminador iran identificados
 *            iniciando con "temp_nombredelpaquete".
 *
 *    Clases:
 *          - Toda clase iniciara con la primera letra mayuscula, si el nombre esta compuesto por mas palabras,
 *            las siguientes palabras deberan iniciar tambien con la primera letra en mayucula ej: ClaseEjemplo
 *          - El nombre de la clase debe identificar su funcionalidad.
 *          - Antes iniciar la clase debe ser comentado para que sirve dicha clase y como es su constructura.
 *          - Si son clases de prueba iran identificados como "cTest_NombreClase"
 *
 *    Metodos
 *          - Toda metodo iniciara con la primera letra mayuscula, si el nombre esta compuesto por mas palabras,
 *            las siguientes palabras deberan iniciar tambien con la primera letra en mayucula ej: MetodoEjemplo
 *          - El nombre de el metodo debe identificar su funcionalidad. si el metodo es un get o un set ira en minuscula
 *          - Antes iniciar el metodo debe ser comentado para que sirve dicha clase, sus entradas y salidas si tiene
 *
 *    Variables
 *          - Todo nombre de varible inicia en minusculas, si el nombre de la varible esta compuesta por mas palabras
 *            las siguientes palabras deberan iniciar con la primera letra en mayucula ej: variableEjemplo.
 *          - El nombre de la variable debe tener sentido acorde a su valor y su utilidad.
 *          - si son variables auxiliares o temporal deben ir identificados como a_nombreVariable o t_nombreVariable.
 *          - las variables globales ya estan plenamente identificadas con color verde.
 *          - Cuando se declara una variable debe comentarse una breve funcionalidad de dicha variable.
 *
 *   lease y cumplase
 */
/***************************************************************************************************************/


package Data;

/**
 *
 * @author Felipe
 */
/* Clase:ImageData
 * datos de la imagen leida... independiente de la extension
 */
public class ImageData {


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
 public byte[] bytesImage;



 //Metodos
    public ImageData(){
        
    }
 public ImageData(int width, int height, int origen, int nCanales, int depth, int alineacion, int espacioColor){
     boolean completado = false;
    if (this.setWidth(width))
    if (this.setHeight(height))
    if (this.setOrigen(origen))
    if (this.setnCanales(nCanales))
    if (this.setDepth(depth))
    if (this.setAlineacion(alineacion))
    if (this.setInterlineado(ImageData.INTERLINEADO_SI))
    if (this.setEspacioColor(espacioColor)){

        int bloques = this.getWidth() / this.getAlineacion();
        if ( this.getWidth() % this.getAlineacion() ==0){
            bloques--;
         }
        if (this.setWidthStep((bloques+1) * this.getAlineacion()))
        //if (this.setnSize(sizeof(this)); aún no esta implementado
        if (this.setdSize(this.getWidthStep()*this.getHeight()*this.getDepth())){


            bytesImage = new byte[this.getHeight()*this.getWidthStep()*this.nCanales];
            completado = true;
        }
     }

     if (!completado)
         System.out.println("Error creando la imagen");

     
 }
public ImageData(ImageData old){

 this.nSize = old.getnSize();
 this.dSize = old.getdSize();  // sizeof(datosImagen)
 this.nCanales = old.getnCanales();
 this.depth = old.getDepth();
 this.espacioColor = old.getEspacioColor();
 this.interlineado = old.getInterlineado();
 this.origen = old.getOrigen();
 this.alineacion = old.getAlineacion();
 this.width = old.getWidth();
 this.height = old.getHeight();
 this.widthStep = old.getWidthStep();

 this.bytesImage = new byte[old.bytesImage.length];
 System.arraycopy(old.bytesImage, 0, this.bytesImage, 0, old.bytesImage.length);

}
 public ImageData(int extencion,int width, int height, int byteimformacion,boolean formatoendian, int compresionbmp,int bpp,byte[][] datosImagen){
    /*if (this.setWidth(width))
    if (this.setHeight(height)){
    this.byteimformacion=byteimformacion;
    this.extencion=extencion;
    this.formatoendian=formatoendian;
    this.compresionbmp=compresionbmp;
    this.bpp=bpp;
    this.datosImagen=datosImagen;
    }*/

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
        if (depth == ImageData.PROF_F64 ||
            depth == ImageData.PROF_F32 ||
            depth == ImageData.PROF_S32 ||
            depth == ImageData.PROF_S16 ||
            depth == ImageData.PROF_U16 ||
            depth == ImageData.PROF_S8 ||
            depth == ImageData.PROF_U8 ){
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
//        if (espacioColor == ImageData.RGB ||
//            espacioColor == ImageData.RGBA ||
//            espacioColor == ImageData.CMYK ||
//            espacioColor == ImageData.HSL ||
//            espacioColor == ImageData.HSV ||
//            espacioColor == ImageData.YCBCR )
        {
            this.espacioColor = espacioColor;
            return true;
        }
//        else{
//            System.out.println("Error en la propiedad del espacio de color");
//            return false;
//        }
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
    private boolean setInterlineado(int interlineado) {
        if( interlineado == ImageData.INTERLINEADO_NO || interlineado == ImageData.INTERLINEADO_SI){
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
        if( origen == ImageData.ARRIBA_IZQ || origen == ImageData.ABAJO_IZQ){
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
        if( alineacion == ImageData.ALINEADO_4 || alineacion == ImageData.ALINEADO_8){
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

    public void informacion(){
        System.out.println("MegaImagen v1.0");
        System.out.println("\tAltura: \t\t" + this.getHeight());
        System.out.println("\tAnchura: \t\t" + this.getWidth());
        System.out.println("\tAncho de memoria: \t" + this.getWidthStep());
        System.out.println("\tNúmero de canales: \t" + this.getnCanales());
        System.out.println("\tProfundidad de color(bytes): \t" + this.getDepth());
        String temp;
        switch(this.getEspacioColor()){
            case RGB: temp = "RGB"; break;
            case RGBA: temp = "RGBA"; break;
            case CMYK: temp = "CMYK"; break;
            case HSV: temp = "HSV"; break;
            case HSL: temp = "HSL"; break;
            case YCBCR: temp = "YCbCr"; break;
            default: temp = "Desconocido o no implementado en este método";
        }
        System.out.println("\tEspacio de color: \t" + temp);

    }

     public void data(){
     for(int i = 0;i<getHeight();i++){
            for(int j = 0;j<getWidth();j++){
                for(int k = 0 ;k<nCanales;k++){
                    //[i][j][k]= i * widthStep + j * nChannels + colorBuscado
                   System.out.print(bytesImage[(i * widthStep + j ) * nCanales + k]+" ");
                }
            }
            System.out.println();
        }
     }


 //Constantes

 //Depth ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
 public static final int PROF_U8 = 1;
 public static final int PROF_S8 = 1;
 public static final int PROF_U16 = 2;
 public static final int PROF_S16 = 2;
 public static final int PROF_S32 = 4;
 public static final int PROF_F32 = 4;
 public static final int PROF_F64 = 8;

  //espacioColor
 public static final int RGB = 1;
 public static final int RGBA = 2;
 public static final int CMYK = 3;
 public static final int HSV = 4;
 public static final int HSL = 5;
 public static final int YCBCR  = 6;
 public static final int BGR  = 7;

 //Interlineado
 public static final int INTERLINEADO_SI = 0;
 public static final int INTERLINEADO_NO = 1;

 //Origen
 public static final int ARRIBA_IZQ = 0;
 public static final int ABAJO_IZQ = 1;

 //Alineación
 public static final int ALINEADO_8 = 8;
 public static final int ALINEADO_4 = 4;



//    /**
//     * @return the extencion
//     */
//    public int getExtencion() {
//        return extencion;
//    }
//
//    /**
//     * @param extencion the extencion to set
//     */
//    public void setExtencion(int extencion) {
//        this.extencion = extencion;
//    }
//
//    /**
//     * @return the formatoendian
//     */
//    public boolean isFormatoendian() {
//        return formatoendian;
//    }
//
//    /**
//     * @param formatoendian the formatoendian to set
//     */
//    public void setFormatoendian(boolean formatoendian) {
//        this.formatoendian = formatoendian;
//    }
//
//    /**
//     * @return the cabezera
//     */
//
//
//    /**
//     * @return the compresionbmp
//     */
//    public int getCompresionbmp() {
//        return compresionbmp;
//    }
//
//    /**
//     * @param compresionbmp the compresionbmp to set
//     */
//    public void setCompresionbmp(int compresionbmp) {
//        this.compresionbmp = compresionbmp;
//    }
//
//    /**
//     * @return the bpp
//     */
//    public int getBpp() {
//        return bpp;
//    }
//
//    /**
//     * @param bpp the bpp to set
//     */
//    public void setBpp(int bpp) {
//        this.bpp = bpp;
//    }
//
//    /**
//     * @return the byteimformacion
//     */
//    public int getByteimformacion() {
//        return byteimformacion;
//    }
//
//    /**
//     * @param byteimformacion the byteimformacion to set
//     */
//    public void setByteimformacion(int byteimformacion) {
//        this.byteimformacion = byteimformacion;
//    }


  

}
