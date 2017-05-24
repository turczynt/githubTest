/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbipackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author turczyt
 */
public class tescik {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
	//nbipackage.NBI nbi =new nbipackage.NBI("serwer", "login", "haslo", Logger.global);
	//nbi.init();
        try
        {            
           nbipackage.NorthB north=new nbipackage.NorthB("172.16.35.150", "U-boot", "utranek098", null);
            String lsteUnodeb=north.make("WAWRNC4", "LST UNODEB:;");
            

            System.out.println(lsteUnodeb);
            NPack npack=new nbipackage.NPack(lsteUnodeb, NPack.FORMAT_PIONOWY);
            
            
            
	    java.util.ArrayList<Paczka> nody=npack.getAllPacks();
            for(int n=0;n<nody.size();n++)
            {
                String NodeName=nody.get(n).getWartosc("Nodeb Name");
               // String lstLocell=north.make(NodeName, "LST ULOCELL:;");
                //System.out.println(lstLocell);
            }
            Thread.sleep(10000);
             north=new nbipackage.NorthB("172.16.35.170", "U-boot", "utranek098", null);
             System.out.println(north.make("GDARNC3", "LST UNODEB:;"));
            

           
             
            
            
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
    }
}