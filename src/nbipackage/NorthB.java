package nbipackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

/**
 *
 * @author turczyt
 */
public class NorthB
{


    String serwer ;
    Socket echoSocket ;
    PrintWriter out ;
    InputStream in;
    boolean zapFlag;
    NewFile logi;
    String login;
    String pass;
    String flagaSave;
    String ostatniRegName;
    String lastRegOperation;
    long timeout=60000;
    long lastExecCommend;

    /**
     *
     * @param serwer Ip wybranego M2000
     * @param login login do M2000
     * @param pass  Haslo do M2000 w postaci jawnej
     * @param flagaSave true- tryb zapisujacy wszystkie operacje wraz z odpowiedziami  to pliku z logami, false -brak zapisu operacji wraz z odpowiedziami
     */

    public NorthB(String serwer,String login,String pass,String flagaSave)
    {
	System.runFinalizersOnExit(true);
        
        this.zapFlag=false;
        this.serwer=serwer;
	this.login=login;
	this.pass=pass;
	this.flagaSave=flagaSave;
	Systemowe syst=new Systemowe();
	logi=new NewFile("/usr/samba/utran/PP/WO/SCRIPTS/"+syst.user()+"/LOGI_TESTOWE_NORTHB.txt");
	ostatniRegName="";
        lastRegOperation="";
	init();
    }

    public NorthB(String serwer,String login,String pass,String logiPath,boolean flagaZapLogi)
    {
	System.runFinalizersOnExit(true);
        this.serwer=serwer;
	this.login=login;
	this.pass=pass;
        this.flagaSave="";
	Systemowe syst=new Systemowe();
        if(flagaZapLogi)
            this.zapFlag=true;
	logi=new NewFile(logiPath);
        this.
	ostatniRegName="";
        lastRegOperation="";
	init();
    }

    /**
     *
     * @return
     */

    public boolean init()
    {
        try
	{
            if(this!=null)
            {
            lastRegOperation="";
            echoSocket = new Socket(serwer, 31114);
	    //echoSocket.setSoTimeout(300000);
	    echoSocket.setSoTimeout(120000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = echoSocket.getInputStream();
            int im=13;
            int ij=10;
            //String userInput="LGI:OP=\"utranek\",PWD=\"12345678\""+";\r";
            String userInput="LGI:OP=\""+login+"\",PWD=\""+pass+"\""+";\r";
            out.println(userInput);
            String odp="";
            String zbior="";
            byte[] tmp=new byte[1024];
            while(true)
            {
                while(in.available()>0)
                {
                    int i=in.read(tmp,0,1024);
                    if(i<=0)
			break;
                    odp=odp+new String(tmp,0,i);
                    //logi.dopisz(odp);
		}
		//System.out.println("#"+odp+"#");
		if(!odp.trim().equals(""))
		{
                    zbior=zbior+odp;
                    if(zawiera(odp,new String[]{"To","be","continued"}))
			;
                    else
                    {
			break;
                    }
                    odp="";
		}
            }
	    int iloscPowtorzen=0;
	    while((!zawiera(zbior,new String[]{"RETCODE = 0"})||zawiera(zbior,new String[]{"RETCODE = 1  Login User Count Must"}))&&iloscPowtorzen<5)
	    {
		try
		{
		    Thread.sleep(5000);
		     System.out.println("##############################\n###\tLOGOWANIE nr"+iloscPowtorzen+"\n\n"+odp+"\n\n"+"#################\nPONOWIENIE POLACZENIA");
		    lastRegOperation="";
		    try
		    {
			in.close();
			out.close();
			echoSocket.close();
		    }
		    catch(Exception er)
		    {
			System.out.println("Proba zamkniecia polaczenia z M-ka nie powiodla sie");
			er.printStackTrace();
		    }
		    echoSocket = new Socket(serwer, 31114);
		    out = new PrintWriter(echoSocket.getOutputStream(), true);
		    in = echoSocket.getInputStream();
		    //String userInput="LGI:OP=\"utranek\",PWD=\"12345678\""+";\r";
		    out.println(userInput);
		    odp="";
		    zbior="";
		    tmp=new byte[1024];
		    while(true)
		    {
			while(in.available()>0)
			{
			    int i=in.read(tmp,0,1024);
			    if(i<=0)
				break;
			    odp=odp+new String(tmp,0,i);
			    //logi.dopisz(odp);
			}
			//System.out.println("#"+odp+"#");
			if(!odp.trim().equals(""))
			{
			    zbior=zbior+odp;
			    if(zawiera(odp,new String[]{"To","be","continued"}))
				;
			    else
			    {
				break;
			    }
			    odp="";
			}
		    }
		    System.out.println("##############################\n###\tLOGOWANIE nr2\n\n"+odp+"\n\n"+"#################\n");
		}
		catch(Exception ee)
		{
		    ee.printStackTrace();
		}
		finally
		{
		    iloscPowtorzen++;
		}
	    }

            if(this.zapFlag)
		logi.dopisz(odp+"\n");
            //	logi.dopisz(odp+"\n");
            if(this.zapFlag)
            	logi.dopisz("***************************|END|\n");
            if(zawiera(zbior,new String[]{"RETCODE = 0"}))
            {
                System.out.println("\nlog to server "+serwer);
                this.lastExecCommend=System.currentTimeMillis();
		return true;
            }
            else
            {
		System.out.println(zbior+"\nCouldn't get connection to: "+serwer);
		return false;
            }
            }
            else
                return false;
	}
	catch (UnknownHostException e)
	{
            System.err.println("Host "+serwer+" does not exist");
	    e.printStackTrace();
            return false;
	}
	catch (IOException e)
	{
	    System.err.println("Couldn't get I/O for "+ "the connection to: "+serwer);
	    e.printStackTrace();
            return false;
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	    return false;
	}
    }

    
    public String  make2(String polecenie,boolean reinit) throws NBIAnsException
    {
     /**
     *
     * @param polecenie przystosowywane do wyslania przez northb wyciananie na koncu \\r \\n oraz sprawdzenie czy polecenie zakonczone ";"
     * @return odpowiedz z M2000
     */
	StringBuffer calosc = new StringBuffer();
	try
	{
            if(polecenie.contains("REG NE:"))
            {
                this.ostatniRegName="";
            }
        long currentTime=System.currentTimeMillis();

        if((currentTime-this.lastExecCommend)>this.timeout)
        {
            this.init();

        }
        polecenie=polecenie.replaceAll(";","");
	//NewFile n=new NewFile();
	int puste=0;
	polecenie=obetnij(polecenie);
	StringBuilder str=new StringBuilder();
        String werPol=logi.getTokens(polecenie, "1", "1", ":");
	
            int im=13;
            int ij=10;
            String userInput=polecenie+";\r";
	   if(this.zapFlag)
		logi.dopisz("\r\n"+userInput);
            out.println(userInput);
	    if(this.zapFlag)
		logi.dopisz(" WYSLANO OK\r\n");
            String odp="";
            String pattern="---    END";
            String conntPattern="To be continued...\r\n\r\n---    END";
	    String conntPattern2="To be continued...\r\n---    END";
            String odpowiedz="";

            try
            {
                char lastChar = pattern.charAt(pattern.length() - 1);
                StringBuilder sb = new StringBuilder();
                boolean found = false;
		if(this.zapFlag)
		{
		    logi.dopisz("\r\n\r\nSTART CZYTANIA ODPOWIEDZI\r\n\r\n");
		    logi.dopisz("\r\n IN is shutDOWN="+echoSocket.isInputShutdown()+"\r\n");
		    //System.err.println("#START CZYTANIA ODPOWIEDZI");
		}
		try{
                char ch = (char) in.read();
		if(this.zapFlag)
		   logi.dopisz("ST="+Character.toString(ch));
                while (true)
                {
		    if(ch!=((char)-1))
                    sb.append(ch);
		    if(ch!=((char)-1))
                    calosc.append(ch);
                    if (ch == lastChar)
                    {
                        String tmp=sb.toString();
                        if (tmp.endsWith(pattern))
                        {
                            if (tmp.endsWith(conntPattern)||tmp.endsWith(conntPattern2))
                            {
                                sb.delete(0, sb.length());
                            }
                            else
                            {
				if(this.zapFlag)
				    logi.dopisz("\r\nCATCH BREAK\r\n");
                                break;
                            }
                        }
                    }

                    ch = (char) in.read();
		   // if(this.zapFlag)
			//logi.dopisz(Character.toString(ch));
                }
		}
		catch(SocketTimeoutException so)
		{
		    System.err.print("soEXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nSOEXCEPTION\r\n\r\n");
		   //so.printStackTrace();
		}
		catch(IOException io)
		{
		    System.err.print("IOEXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nIOEXCEPTION\r\n\r\n");
		}
		catch(Exception io)
		{
		    System.err.print("OTHER_EXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nOTHER_EXCEPTION\r\n\r\n");
		}

                this.lastExecCommend=System.currentTimeMillis();
		//System.out.println("KONIEC CZYTANIA ODP na:"+userInput);
		if(this.zapFlag)
		{
		    logi.dopisz("\r\n\r\nKONIEC CZYTANIA ODPOWIEDZI ");
		    logi.dopisz(""+calosc+"\r\n\r\n");
		}
		    
		if(calosc!=null&&calosc.length()>0)
		{
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\n"+calosc.toString()+"\r\n\r\n");
		    if(calosc.toString().contains(werPol)&&calosc.toString().contains("---    END"))
		    {


			    return calosc.toString();
		    }
		    else
		    {
			if(this.zapFlag)
		    	logi.dopisz("\r\nXXXXXXXXX\r\nTHOWS NBIANSEXCEPTION ZJEBANA WARTOSC\r\n\r\n");
			if(calosc.toString().contains("NE Disconnect Information")&& reinit)
			{
			    this.closeBuffor();
			    this.init();
			   String odpRe= this.make2(polecenie,false);
			   return odpRe;
			}
			else
			    throw new NBIAnsException(calosc.toString());
		    }
		}
		else
		{
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nTHOWS NBIANSEXCEPTION\r\n\r\n");
		    System.err.print("\n\n###########\nTHOWS NBIANSEXCEPTION\n######\n");
		    throw new NBIAnsException(calosc.toString());
		}
            }
            catch (Exception e)
            {
		System.err.println("WYwala read:");
		if(this.zapFlag)
		    logi.dopisz("\r\nZLAPANY WYJATEK E"+e.toString()+"\r\n");
		calosc.append(e.toString());
		
                
                throw new NBIAnsException(calosc.toString());
            }
	    finally
	    {
	       
	    }
        }
	catch(Exception z)
        {
	    //System.err.println("COS SIE JEBLO:");
            if(this.zapFlag)
		    logi.dopisz("\r\nZLAPANY WYJATEK Z"+z.toString()+"\r\n");
	    calosc.append(z.toString());
            throw new NBIAnsException(calosc.toString());
	}
        finally
        {
           if(this.zapFlag)
               logi.dopisz(calosc.toString()+"\n");
        }
    }
     public String  make2(String polecenie) throws NBIAnsException
    {
     /**
     *
     * @param polecenie przystosowywane do wyslania przez northb wyciananie na koncu \\r \\n oraz sprawdzenie czy polecenie zakonczone ";"
     * @return odpowiedz z M2000
     */
	StringBuffer calosc = new StringBuffer();
        
	try
	{
        long currentTime=System.currentTimeMillis();

        if((currentTime-this.lastExecCommend)>this.timeout)
        {
            this.init();

        }
        
        if(polecenie.contains("REG NE:"))
            {
                this.ostatniRegName="";
            }
        
        polecenie=polecenie.replaceAll(";","");
	//NewFile n=new NewFile();
	int puste=0;
	polecenie=obetnij(polecenie);
	StringBuilder str=new StringBuilder();
        String werPol=logi.getTokens(polecenie, "1", "1", ":");

            int im=13;
            int ij=10;
            String userInput=polecenie+";\r";
	   if(this.zapFlag)
		logi.dopisz("\r\n"+userInput);
            out.println(userInput);
	    if(this.zapFlag)
		logi.dopisz(" WYSLANO OK\r\n");
            String odp="";
            String pattern="---    END";
            String conntPattern="To be continued...\r\n\r\n---    END";
	    String conntPattern2="To be continued...\r\n---    END";
            String odpowiedz="";

            try
            {
                char lastChar = pattern.charAt(pattern.length() - 1);
                StringBuilder sb = new StringBuilder();
                boolean found = false;
		if(this.zapFlag)
		{
		    logi.dopisz("\r\n\r\nSTART CZYTANIA ODPOWIEDZI\r\n\r\n");
		    logi.dopisz("\r\n IN is shutDOWN="+echoSocket.isInputShutdown()+"\r\n");
		    //System.err.println("#START CZYTANIA ODPOWIEDZI");
		}
		try{
                char ch = (char) in.read();
		if(this.zapFlag)
		   logi.dopisz("ST="+Character.toString(ch));
                while (true)
                {
		    if(ch!=((char)-1))
		    {
			sb.append(ch);
		    //if(ch!=((char)-1))
			calosc.append(ch);
		    }
                    if (ch == lastChar)
                    {
                        String tmp=sb.toString();
                        if (tmp.endsWith(pattern))
                        {
                            if (tmp.endsWith(conntPattern)||tmp.endsWith(conntPattern2))
                            {
                                sb.delete(0, sb.length());
                            }
                            else
                            {
				if(this.zapFlag)
				    logi.dopisz("\r\nCATCH BREAK\r\n");
                                break;
                            }
                        }
                    }

                    ch = (char) in.read();
		   // if(this.zapFlag)
			//logi.dopisz(Character.toString(ch));
                }
		}
		catch(SocketTimeoutException so)
		{
		    System.err.print("soEXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nSOEXCEPTION\r\n\r\n");
		   //so.printStackTrace();
		}
		catch(IOException io)
		{
		    System.err.print("IOEXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nIOEXCEPTION\r\n\r\n");
		}
		catch(Exception io)
		{
		    System.err.print("OTHER_EXCEPTION");
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nOTHER_EXCEPTION\r\n\r\n");
		}

                this.lastExecCommend=System.currentTimeMillis();
		//System.out.println("KONIEC CZYTANIA ODP na:"+userInput);
		if(this.zapFlag)
		{
		    logi.dopisz("\r\n\r\nKONIEC CZYTANIA ODPOWIEDZI ");
		    logi.dopisz(""+calosc+"\r\n\r\n");
		}

		if(calosc!=null&&calosc.length()>0)
		{
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\n"+calosc.toString()+"\r\n\r\n");
		    if(calosc.toString().contains(werPol)&&calosc.toString().contains("---    END"))
		    {


			    return calosc.toString();
		    }
		    else
		    {
			if(this.zapFlag)
		    	logi.dopisz("\r\nXXXXXXXXX\r\nTHOWS NBIANSEXCEPTION ZJEBANA WARTOSC\r\n\r\n");
			
			    throw new NBIAnsException(calosc.toString());
		    }
		}
		else
		{
		    if(this.zapFlag)
			logi.dopisz("\r\nXXXXXXXXX\r\nTHOWS NBIANSEXCEPTION\r\n\r\n");
		    System.err.print("\n\n###########\nTHOWS NBIANSEXCEPTION\n######\n");
		    throw new NBIAnsException(calosc.toString());
		}
            }
            catch (Exception e)
            {
		System.err.println("WYwala read:");
		if(this.zapFlag)
		    logi.dopisz("\r\nZLAPANY WYJATEK E"+e.toString()+"\r\n");
		calosc.append(e.toString());


                throw new NBIAnsException(calosc.toString());
            }
	    finally
	    {

	    }
        }
	catch(Exception z)
        {
	    //System.err.println("COS SIE JEBLO:");
            if(this.zapFlag)
		    logi.dopisz("\r\nZLAPANY WYJATEK Z"+z.toString()+"\r\n");
	    calosc.append(z.toString());
            throw new NBIAnsException(calosc.toString());
	}
        finally
        {
           if(this.zapFlag)
               logi.dopisz(calosc.toString()+"\n");
        }
    }
    /**
     *
     * @param regName nazwa NE
     * @param polecenie {LST,DSP,ADD,MOD, ACT,DEA,BLK,UBL,SET,RMV}
     * @return Odpowiedz_NBI
     */

    public String make(String regName,String polecenie)  throws NBIAnsException
    {
        boolean zalogowanyNe=false;
        if(ostatniRegName!=null&&ostatniRegName.equals(regName))
        {
            zalogowanyNe=true;
        }
        else
        {
            zalogowanyNe=regOperation(regName);
        }
        if(zalogowanyNe)
        {
            String   odp=this.make2(polecenie,true);
	    if(this.zapFlag)
		logi.dopisz("MAKE2="+odp+"\n");
            if(this.zawiera(odp, "Login or Register needed")||this.zawiera(odp, "Login or Register needed"))
            {
                if(regOperation(regName))
                {
                    ostatniRegName=regName;
                    odp=this.make2(polecenie,true);
		    if(this.zapFlag)
	    		logi.dopisz("MAKE2="+odp+"\n");
                    return odp;
                }
                else
		{
		    if(this.zapFlag)
			logi.dopisz("lastRegOperation:MAKE2="+this.lastRegOperation+"\n");
                    return this.lastRegOperation;
		}
            }
            ostatniRegName=regName;
	    if(this.zapFlag)
		logi.dopisz("MAKE2="+odp+"\n");
            return odp;
        }
        else
        {
		    if(this.zapFlag)
			logi.dopisz("lastRegOperation:MAKE2="+this.lastRegOperation+"\n");
                    return this.lastRegOperation;
		}
    }

    public String MakeWithReg(String komenda_regName) throws NBIAnsException
    {


        if (komenda_regName != null && !komenda_regName.equals(""))
        {
            String komenda = logi.getTokens(komenda_regName, "1", "1", "[{]");
            komenda = komenda.replaceAll("[{]", "");
            String regname = logi.getTokens(komenda_regName, "2", "2", "[{]");
            regname = regname.replaceAll("[{]", "");
            regname = regname.replaceAll("[}]", "");
            return this.make(regname, komenda);
        }
        else
            return "";


    }

    private boolean regOperation(String regName) throws NBIAnsException
    {
        boolean regOk=false;
        lastRegOperation=this.make2("REG NE:NAME=\""+regName+"\"",true);
        regOk=this.zawiera(lastRegOperation, "RETCODE = 0  Success");
        if(regOk)
            this.ostatniRegName=regName;

        return regOk;
    }

    /**
     *
     * @return true -dla poprawnego zamkniecia bufforow, false- dla nieudanej operacji
     */

    public boolean closeBuffor()
    {
        try
        {
	    String unreg="UNREG NE:NAME=\""+ostatniRegName+"\";\r";
	    out.println(unreg);
	    String userInput="LGO:OP=\""+login+"\";\r";
            out.println(userInput);
            out.close();
            in.close();
            echoSocket.close();
	    //System.out.println("ZAMKNIETE");
            return true;
	}
        catch (UnknownHostException e)
	{
            System.err.println("Don't know about host: "+serwer);
            return false;
	}
	catch (IOException e)
	{
            System.err.println("Couldn't get I/O for "+ "the connection to: "+serwer);
            return false;
	}
        catch(Exception e)
        {
	    e.printStackTrace();
            return false;
        }
    }

    private boolean zawiera(String tekst,String szukany)
    {
	if(tekst!=null&&szukany!=null&&!tekst.equals("")&&!szukany.equals(""))
	{
            Pattern p = Pattern.compile(szukany);
            Matcher matcher= p.matcher(tekst);
            if(matcher.find())
		return true;
            return false;
	}
	return false;
    }

    private boolean zawiera(String tekst,String[] szukany)
    {
	if(tekst!=null&&szukany!=null&&!tekst.equals("")&&!szukany.equals(""))
    	{
            boolean flaga=true;
            for (int s=0;s<szukany.length ;s++ )
            {
                if(!zawiera(tekst,szukany[s]))
                    return false;
            }
            return true;
	}
	return false;
    }

    private String obetnij(String wej)
    {
	char[]tab=wej.toCharArray();
	String str="";
	for(int i=0;i<tab.length;i++)
	{
            if(tab[i]==10||tab[i]==13)
		;
            else
		str=str+tab[i];
	}
	return str;
    }

    @Override
    protected void finalize () throws Throwable
    {

	try{
            System.out.println("ZAMKNIECIE OK unreg="+this.ostatniRegName);
	    String unreg="UNREG NE:NAME=\""+this.ostatniRegName+"\";\r";
	    out.println(unreg);
	    String userInput="LGO:OP=\""+login+"\";\r";
            out.println(userInput);
            out.close();
            in.close();
            echoSocket.close();
	    
	}
	catch(Exception ee)
 	{

	    ee.printStackTrace();
	}
	finally
	{
	    super.finalize();
	}
    }

    public String getOstatniRegName()
    {
	return ostatniRegName;
    }

    public String getSerwer()
    {
	return serwer;
    }
    
}