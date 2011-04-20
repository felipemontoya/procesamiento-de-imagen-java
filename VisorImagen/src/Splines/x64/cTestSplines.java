/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Splines.x64;

/**
 *
 * @author Arzobispo
 */
public class cTestSplines {
    public native String funct1();
    public native void functSplines();

    public static void main(String ar[])
    {
        String os = System.getProperty("os.arch");
        String path = System.getProperty("user.dir");

        System.out.println("" + path);


//        System.out.println("Os " + os);
        if (os.equals("amd64"))
        {
            System.load(path + "\\src\\Splines\\x64\\jniWrapper.dll");
            System.load(path + "\\src\\Splines\\x64\\Splines.dll");
        }
        else
        {
            System.load(path + "\\src\\Splines\\x64\\jniWrapper.dll");
            System.load(path + "\\src\\Splines\\x64\\Splines.dll");
        }
        cTestSplines jht = new cTestSplines();
        //System.out.println("hello java");
        System.out.println(jht.funct1());
//        jht.functSplines();
//        System.exit(0);
    }
    
}
