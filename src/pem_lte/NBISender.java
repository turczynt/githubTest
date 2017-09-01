/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import nbipackage.NewFile;

/**
 *
 * @author turczyt
 */
public class NBISender
{
  private   String ip;
    String login;
    String passwd;
    NewFile logs;
    java.util.ArrayList<String> commands;
    boolean allCommandSucceded;
    String errorsInfo;
    private nbipackage.NorthB north;
    int licznikBledow;

    public NBISender(String ip, String login,String passwd, NewFile logs, ArrayList<String> commands)
    {
        this.ip = ip;
        this.passwd = passwd;
        this.logs = logs;
        this.login=login;
        this.commands = commands;
        this.allCommandSucceded=true;
        this.licznikBledow=0;
    }
    public boolean executeCommands()
    {
        FileOutputStream stream=null;
        PrintWriter printWriter=null;
        try
        {
            logs.dopisz("\r\n\r\n///////////////////////////////////NBI SENDER/////////////\r\n\r\n");
            
            stream=new FileOutputStream(logs.pass(),true);
            printWriter=new PrintWriter(stream);
            
            this.north=new nbipackage.NorthB(ip, login, passwd, null);
            for(int c=0;c<this.commands.size();c++)
            {
                String out="";
                try
                {
                    if(commands.get(c)!=null&&!commands.get(c).trim().equals("")&&!commands.get(c).startsWith("/////"))
                    {
                        System.out.print(c+"/"+this.commands.size()+" "+this.commands.get(c));
                        if(commands.get(c).contains("//SLEEP"))//   //SLEEP=30 czas podany w sekundach
                        {
                            int timeInS=Integer.parseInt(commands.get(c).split("=")[1]);
                            Thread.sleep(timeInS*1000);
                            System.out.println();
                        }
                        else
                        {
                            out= this.north.MakeWithReg(this.commands.get(c));
                           
                            printWriter.println(out+"\r\n");
                            if(out.contains("RETCODE = 0"))
                            {
                                System.out.println("RETCODE = 0 [succeeded]");
                            }
                            else
                            {
                                System.out.println(out+"\r\nSLEEP 10s and try again\r\n");
                                printWriter.println(out+"\r\nSLEEP 10s and try again\r\n");
                                Thread.sleep(10000);
                                out= this.north.MakeWithReg(this.commands.get(c));
                           
                                printWriter.println(out+"\r\n");
                                if(out.contains("RETCODE = 0"))
                                {
                                    System.out.println("RETCODE = 0 [succeeded]");
                                }
                                else
                                {

                                    this.licznikBledow++;
                                    System.out.println("\r\n"+out);
                                }
                                
                            }
                            
                        }
                         
                    }
                }
                catch(Exception ene)
                {
                    this.allCommandSucceded=false;
                    this.licznikBledow++;
                    errorsInfo=errorsInfo+"\r\nline["+(c+1)+"]:"+this.commands.get(c)+"\r\n";//+out;
                           
                    ene.printStackTrace(printWriter);
                }
            }
            
        }
        catch(Exception ee)
        {
            ee.printStackTrace(printWriter);
           
            this.allCommandSucceded=false;
           
        }
        finally
        {
            try
            {
               printWriter.close();
               stream.close();
               this.north.closeBuffor();
            }
            catch(Exception eze)
            {
                
            }
            this.logs.dopisz("\r\n\r\n#######################LICZBA BLEDOW="+this.licznikBledow);
        }    
        
        return this.allCommandSucceded;
    }

    public boolean isAllCommandSucceded()
    {
        return allCommandSucceded;
    }

    public String getErrorsInfo()
    {
        return errorsInfo;
    }
    
    
    public int getlicznikBledow()
    {
        return this.licznikBledow;
    }       
}
