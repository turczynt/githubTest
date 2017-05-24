/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbipackage;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

/**
 *
 * @author turczyt
 */
public class NBI
{
    String aktualneNe;
    String aktualnaKomenda;


    int timeOutCalkowity;
    int timeOutPomiedzyBitami;
    String serwer;
    String nbiPaswd;
    String nbiLogin;
    String lastNbiAns;
    Socket echoSocket ;
    PrintWriter out ;
    InputStream in;
    Logger loger;
    String endPattern="---    END";
    String conntinuePattern="To be continued...\r\n\r\n---    END";
    String conntinuePattern2="To be continued...\r\n---    END";

    public NBI(String serwer, String nbiLogin, String nbiPaswd,Logger loger)
    {
	this.serwer = serwer;
	this.nbiPaswd = nbiPaswd;
	this.nbiLogin = nbiLogin;
	this.loger=loger;
    }

    public String init()
    {
	String odp="";
	try
	{
	    echoSocket = new Socket(serwer, 31114);
	    //echoSocket.setSoTimeout(120000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = echoSocket.getInputStream();
	}
	catch (IOException ex)
	{
	    loger.log(Level.FINEST, odp, ex);
	    ex.printStackTrace();
	}
	    //echoSocket.setSoTimeout(300000);



	return odp;
    }

    public String readToEnd(InputStream inputStr)
    {
	String odp="";


	return odp;
    }

}
