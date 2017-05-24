
package nbipackage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;



public class NorthT
{
    String serwer ;
    Tunel t;
    String PlikSzpieg;
	
    Socket echoSocket ;
    PrintWriter out ;
    InputStream in;
    NewFile logi;

    String flagaSave;
    String ostatniRegName;
    NorthB north;
    String loginMka;
    String hasloMka;

    public NorthT(String serwer,String PlikSzpieg,String loginTool,String hasloTool,String loginM200,String hasloM2000)
    {
		this.serwer=serwer;
		t=new Tunel("172.16.5.38",loginTool,hasloTool);
		this.PlikSzpieg=PlikSzpieg;

		this.serwer=serwer;
		this.flagaSave=flagaSave;
		Systemowe syst=new Systemowe();

		ostatniRegName="";
                north=new NorthB(serwer,loginM200,hasloM2000,"");
		this.loginMka=loginM200;
		this.hasloMka=hasloM2000;
	//	System.out.println("Przed init");
		//init();
		//System.out.println("Po init");

	}

    public boolean init(String loginM200, String hasloM2000)
    {
	try
	{
	    this.echoSocket = new Socket(serwer, 31114);
	    this.echoSocket.setSoTimeout(20000);
	    this.out = new PrintWriter(echoSocket.getOutputStream(), true);
	    this.in = echoSocket.getInputStream();
	    int im = 13;
	    int ij = 10;
	    String userInput = "LGI:OP=\"" + loginM200 + "\",PWD=\"" + hasloM2000 + "\"" + ";\r";
	    out.println(userInput);
	    String odp = "";
	    String zbior = "";

	    byte[] tmp = new byte[1024];
	    while (true)
	    {
		while (in.available() > 0)
		{
		    int i = in.read(tmp, 0, 1024);
		    if (i <= 0)
		    {
			break;
		    }
		    odp = odp + new String(tmp, 0, i);
		    //logi.dopisz(odp);
		}
		//System.out.println("#"+odp+"#");
		if (!odp.trim().equals(""))
		{
		    //System.out.println("#"+odp+"#");
		    zbior = zbior + odp;

		    if (zawiera(odp, new String[]{"To", "be", "continued"}))
			;
		    else
		    {
			System.out.println("BREAK");
			break;
		    }
		    odp = "";
		}
	    }

	    if (zawiera(zbior, new String[]{"RETCODE = 0"}))
	    {
		System.out.println("\nlog to server " + serwer);
		return true;
	    }
	    else
	    {
		System.out.println("Couldn't get connection to: " + serwer);
		return false;
	    }
	}
	catch (UnknownHostException e)
	{
	    //logi.dopisz("\n"+e.toString()+"\n");
	    StackTraceElement[] traceElements = e.getStackTrace();
	    for (int ste = 0; ste < traceElements.length; ste++)
	    {
		System.out.println("\t" + traceElements[ste]);
	    }
	    return false;
	}
	catch (IOException e)
	{
	    //logi.dopisz("\n"+e.toString()+"\n");
	    StackTraceElement[] traceElements = e.getStackTrace();
	    for (int ste = 0; ste < traceElements.length; ste++)
	    {
		System.out.println("\t" + traceElements[ste]);
	    }
	    return false;
	}
	catch (Exception e)
	{
	    //logi.dopisz("\n"+e.toString()+"\n");
	    StackTraceElement[] traceElements = e.getStackTrace();
	    for (int ste = 0; ste < traceElements.length; ste++)
	    {
		System.out.println("\t" + traceElements[ste]);
	    }
	    return false;
	}
    }

    public NorthT()
    {
    }
	
    public void szpieg(String nodeName)
    {

		//String doZapisu="##########################################\n";
		dopiszDoPliku(this.PlikSzpieg,"##########################################");
		dopiszDoPliku(this.PlikSzpieg,"##########################################");
		
		dopiszDoPliku(this.PlikSzpieg,nodeName);//+" ";//+"\n";
		
		dopiszDoPliku(this.PlikSzpieg,System.getProperty("user.name"));//+" ";//+"\n";
		
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();

		try
		{
			InetAddress ownIP=InetAddress.getLocalHost();
		
			dopiszDoPliku(this.PlikSzpieg,"IP of system is = "+ownIP.getHostAddress());
		}
		catch (Exception e)
		{
			System.out.println("Exception caught ="+e.getMessage());
		}
		
		dopiszDoPliku(this.PlikSzpieg,dateFormat.format(date));
		dopiszDoPliku(this.PlikSzpieg,"##########################################");
	}

    public void chanegSzpFile(String PlikSzpieg)
    {
		this.PlikSzpieg=PlikSzpieg;
    }

    public void dopiszDoPliku(String plik,String tekst)
    {
		if(plik!=null&&!plik.equals(""))
		{

			tekst.replaceAll("'", "");
			String odpow=	t.execute("java -jar /usr/samba/utran/PP/Bin/WriteToFile.jar /export/home/turczyt/szpiegPem/"+plik+" '"+tekst+"'");
		}
    }

    public String make(String regName,String komenda,boolean saveOnTool)throws NBIAnsException
    {
            String odp="";
            odp=north.make(regName, komenda);
            if(saveOnTool)
                dopiszDoPliku(this.PlikSzpieg,odp+"\n");
            return odp;
        }

    public String make(String regName,String komenda)throws NBIAnsException
    {
            String odp=make(regName,komenda,true);
            return odp;
        }

    public boolean closeBuffor()
    {
		try
		{
		    if(this.north!=null)
			this.north.closeBuffor();
		    return true;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			StackTraceElement[] traceElements = e.getStackTrace();
			for(int ste=0;ste<traceElements.length;ste++)
			{
				System.out.println("\t"+traceElements[ste]);
			}
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
}