/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbipackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author turczyt
 */
public class TEST
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException
    {
        // TODO code application logic here
            String serwer="172.16.35.150" ;
    Socket echoSocket ;
    PrintWriter out ;
    InputStream in;
        echoSocket = new Socket(serwer, 31114);
        echoSocket.setKeepAlive(false);
        
	    //echoSocket.setSoTimeout(300000);
      
	  
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = echoSocket.getInputStream();
            
            int im=13;
            int ij=10;
            //String userInput="LGI:OP=\"utranek\",PWD=\"12345678\""+";\r";
            String userInput="LGI:OP=\"U-boot\",PWD=\"utranek098\""+";";
              String userInputEND="LGO:OP=\"U-boot\";";
            out.println(userInput);
            StringBuilder stt=new StringBuilder();
            
            
             /*   while(in.available()>0)
                {
                    
                    int i=in.read(tmp,0,1024);
                    if(i<=0)
			break;
                    odp=odp+new String(tmp,0,i);
                    //logi.dopisz(odp);
		}*/
            int i;
            while((i=in.read())!=-1)
            {
                stt.append((char)i);
                System.out.println(stt.toString().toCharArray());
                if(stt.toString().contains("---    END"))
                    break;
            }
            System.out.println("##############");
            
            for(int z=0;z<30;z++)
            {
//                Thread.sleep(10000);
                System.out.println("##############"+z+"/"+20);
            }
            
            out.println(userInputEND);
            stt=new StringBuilder();
            while((i=in.read())!=-1)
            {
                stt.append((char)i);
                System.out.println(stt.toString().toCharArray());
                if(stt.toString().contains("---    END"))
                    break;
            }
    }
}
