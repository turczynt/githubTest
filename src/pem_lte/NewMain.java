/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

/**
 *
 * @author turczyt
 */
public class NewMain
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        System.out.println("430 dbm="+(Komorka.miliDbm2Wat("430"))+" Wat");
        System.out.println("430 dbm +0.1Wat="+(Komorka.miliDbm2Wat("430")+0.01)+" Wat");
         System.out.println("430 dbm +0.1Wat="+Komorka.wat2miliDbm(Komorka.miliDbm2Wat("430")+0.01)+" dbm");
        System.out.println("20.0WAT="+Komorka.wat2miliDbm(20.0)+" dbm");
        System.out.println("19.9WAT="+Komorka.wat2miliDbm(19.9)+" dbm,");
        System.out.println("40Wat"+Komorka.wat2rsInMiliDbm(40.0, 1, 600, 2));
    }
}
