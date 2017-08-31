package pem_lte;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author turczyt
 * Klasa odpowiedzialna za komunikacje z baza danych
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import mysqlpackage.OdpowiedzSQL;
import nbipackage.NewFile;

public class Idb
{

    String login;
    String paswd;
    String ip;
    String URL;
    Connection con;
    String catalogName;
    Statement stmt;
    boolean connectionSucced;
    int port;
OdpowiedzSQL wnioksi;
    public Idb(int port, String login, String paswd, String ip, String CatalogName)
    {
	this.login = login;
	this.paswd = paswd;
	this.ip = ip;
	this.port = port;
	//URL="jdbc:sybase:Tds:"+ip+":4100";
	//jdbc:sqlserver://
        //jdbc:mysql://172.16.5.52:3306
	

	this.catalogName = CatalogName;

    }

    public Idb()
    {
	this.login = null;
	this.paswd = null;
	this.ip = null;
	this.port = -1;
	//URL="jdbc:sybase:Tds:"+ip+":4100";
	this.URL = null;

	this.catalogName = null;

    }

    public boolean connectMS() throws ClassNotFoundException, InstantiationException, IllegalAccessException
     {
	connectionSucced = false;
        this.URL="jdbc:sqlserver://"+ ip + ":" + port;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
	try
	{
	
	    this.con = DriverManager.getConnection(URL, login, paswd);
	    //conn = DriverManager.getConnection("jdbc:mysql://172.16.5.38:3306/oncall?user=" + login + "&password=" + pass + "&useUnicode=yes&characterEncoding=UTF-8");
	    if (catalogName != null)
	    {
		this.con.setCatalog(catalogName);
	    }
	    this.stmt = con.createStatement();
	    connectionSucced = true;
	}
	catch (Exception ee)
	{
	    ee.printStackTrace();
	}
	return connectionSucced;
    }
    public boolean connectMy() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        this.URL = "jdbc:mysql://" + ip + ":" + port;
	connectionSucced = false;
          Class.forName("org.gjt.mm.mysql.Driver").newInstance();
	try
	{
	    //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
	  
	    this.con = DriverManager.getConnection(URL, login, paswd);
	    //conn = DriverManager.getConnection("jdbc:mysql://172.16.5.38:3306/oncall?user=" + login + "&password=" + pass + "&useUnicode=yes&characterEncoding=UTF-8");
	    if (catalogName != null)
	    {
		this.con.setCatalog(catalogName);
	    }
	    this.stmt = con.createStatement();
	    connectionSucced = true;
	}
	catch (Exception ee)
	{
	    ee.printStackTrace();
	}
	return connectionSucced;
    }

    public boolean disconnect() throws SQLException
    {
        this.con.close();
	    //conn = DriverManager.getConnection("jdbc:mysql://172.16.5.38:3306/oncall?user=" + login + "&password=" + pass + "&useUnicode=yes&characterEncoding=UTF-8");

	return connectionSucced;
    }

    public OdpowiedzSQL wykonajZapytanie(String zapytanie)
    {
	//System.out.println(zapytanie);
	java.sql.ResultSet rs = this.executeRequest(zapytanie);
	return this.createAnswer(rs);
    }

    public java.sql.ResultSet executeRequest(String request)
    {
	try
	{
	    //con.setCatalog("logdb");
	    String[] kolejne = request.split(";");
	    ResultSet rs = null;
	    for (int z = 0; z < kolejne.length; z++)
	    {
		rs = stmt.executeQuery(kolejne[z]);
	    }

	    return rs;
	}
	catch (Exception ee)
	{
	    ee.printStackTrace();
	}
	return null;
    }

    public boolean executeQuery(String query)
    {
	try
	{
	    stmt.execute(query);
	    if (stmt != null && stmt.getWarnings() != null)
	    {
		stmt.getWarnings().printStackTrace(System.out);
	    }
	    return true;
	}
	catch (Exception ee)
	{
	    //System.out.println(ee.toString());
	    ee.printStackTrace();
	}
	return false;
    }

    public static OdpowiedzSQL createAnswer(java.sql.ResultSet input)
    {
//        System.out.println("Obrabianie odpowiedzi na zapytanie");
	String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);

	java.util.ArrayList<String> nazwyKolumn = new java.util.ArrayList<String>();
	java.util.ArrayList<java.util.ArrayList<String>> rekordy = new java.util.ArrayList<java.util.ArrayList<String>>();
	try
	{
	    java.sql.ResultSetMetaData MetaData = input.getMetaData();
	    int numberOfColumns = MetaData.getColumnCount();

	    for (int i = 1; i < numberOfColumns + 1; i++)
	    {
		String columnName = MetaData.getColumnLabel(i);
		nazwyKolumn.add(columnName);

	    }
	    while (input.next())
	    {
		try
		{
		    java.util.ArrayList<String> rekTmp = new java.util.ArrayList<String>();
		    for (int i = 1; i < numberOfColumns + 1; i++)
		    {

			String valTMp = input.getString(i);
			if (valTMp != null)
			{
			    if (MetaData.getColumnName(i).equalsIgnoreCase("beginDate"))
			    {
				Long ll = new java.lang.Long(valTMp + "000");
				java.sql.Time tst = new java.sql.Time(ll);
				String tmpData = sdf.format(tst);
				rekTmp.add(tmpData);
			    }
			    else
			    {
				rekTmp.add(valTMp);
			    }
			}
			else
			{
			    rekTmp.add("");
			}
		    }
		    rekordy.add(rekTmp);
		}
		catch (Exception ewewe)
		{
		    //                  System.out.println(ewewe.toString());
		    ewewe.printStackTrace();
		}
	    }
	}
	catch (Exception ee)
	{
	    ee.printStackTrace();
	}
	OdpowiedzSQL odp = new OdpowiedzSQL(input, nazwyKolumn, rekordy);
	//  System.out.println("zakonczenie tworzenia odp");
	return odp;
    }

    private OdpowiedzSQL pobierzWnioskiZAsPemOs(String siteShortName)
    {
        
        String req="Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMax,PemTiltMax,BandId,tech from UTRAN3.idb2.data.PEMZleconyMerge where PropertyName like '%"+siteShortName+"%'";
       // OdpowiedzSQL wnioski=this.wykonajZapytanie("Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMax,PemTiltMax,BandId from idb2.data.PEMZlecony where PropertyName like '%"+siteShortName+"%'");
        
        OdpowiedzSQL wnioski=this.wykonajZapytanie(req);
        System.out.println(wnioski);
        java.util.ArrayList<java.util.ArrayList<String>> daneTmp= new java.util.ArrayList<java.util.ArrayList<String>>();
        java.util.ArrayList<String> usedValue=new java.util.ArrayList<String>();//KLUCZ:  azimut+";"+BandInd
        for(int i=0;i<wnioski.rowCount();i++)
        {
            String klucz=wnioski.getValue("Azymut", i)+";"+wnioski.getValue("BandId", i);
            int bb=-1;
            java.util.ArrayList<String> rekord=null;
                
            if(usedValue.contains(klucz))
            {
                for(int a=0;a<daneTmp.size()&&rekord==null;a++)
                {
                    if(daneTmp.get(a).get(1).equals(wnioski.getValue("Azymut", i))&&daneTmp.get(a).get(4).equals(wnioski.getValue("BandId", i)))
                    {
                       
                        bb=a;
                    }
                }                
            }
            else
            {
                rekord=new java.util.ArrayList<String>();
            }
            usedValue.add(klucz);
            if(bb==-1)
            {
                daneTmp.add(wnioski.getRekord(i));
            }
            else
            {
                rekord=daneTmp.get(bb);
                String newVal=""+ (Komorka.wat2miliDbm(Komorka.miliDbm2Wat(Double.parseDouble(wnioski.getValue("PemTxPowerMax", i).replaceAll(",", ".")) * 10)+Komorka.miliDbm2Wat(Double.parseDouble(rekord.get(2).replaceAll(",", ".")) * 10))/10);
                rekord.set(2, newVal); // suma mocy
                rekord.set(3, rekord.get(3)+","+wnioski.getValue("PemTiltMax", i)); // suma tiltVal
                rekord.set(5, rekord.get(5)+","+wnioski.getValue("tech", i)); // suma tiltVal
                daneTmp.set(bb, rekord);
            }
        }
        wnioski.setRekordy(daneTmp);
        
        //System.out.println("Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMax,PemTiltMax,BandId from idb2.data.PEMZlecony where PropertyName like '%"+siteShortName+"%'");
        
        
        
        System.out.println(wnioski);
        return wnioski;
    }
    
    private OdpowiedzSQL pobierzWnioskiZAsPemOsChangePower(String siteShortName)
    {
        
        String req="Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMaxMax as PemTxPowerMax,PemTiltMax,BandId,tech from UTRAN3.idb2.data.PEMZleconyMerge where PropertyName like '%"+siteShortName+"%'";
       // OdpowiedzSQL wnioski=this.wykonajZapytanie("Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMax,PemTiltMax,BandId from idb2.data.PEMZlecony where PropertyName like '%"+siteShortName+"%'");
        
        OdpowiedzSQL wnioski=this.wykonajZapytanie(req);
        System.out.println(wnioski);
        java.util.ArrayList<java.util.ArrayList<String>> daneTmp= new java.util.ArrayList<java.util.ArrayList<String>>();
        java.util.ArrayList<String> usedValue=new java.util.ArrayList<String>();//KLUCZ:  azimut+";"+BandInd
        for(int i=0;i<wnioski.rowCount();i++)
        {
            String klucz=wnioski.getValue("Azymut", i)+";"+wnioski.getValue("BandId", i);
            int bb=-1;
            java.util.ArrayList<String> rekord=null;
                
            if(usedValue.contains(klucz))
            {
                for(int a=0;a<daneTmp.size()&&rekord==null;a++)
                {
                    if(daneTmp.get(a).get(1).equals(wnioski.getValue("Azymut", i))&&daneTmp.get(a).get(4).equals(wnioski.getValue("BandId", i)))
                    {
                       
                        bb=a;
                    }
                }                
            }
            else
            {
                rekord=new java.util.ArrayList<String>();
            }
            usedValue.add(klucz);
            if(bb==-1)
            {
                daneTmp.add(wnioski.getRekord(i));
            }
            else
            {
                rekord=daneTmp.get(bb);
                String newVal=""+ (Komorka.wat2miliDbm(Komorka.miliDbm2Wat(Double.parseDouble(wnioski.getValue("PemTxPowerMax", i).replaceAll(",", ".")) * 10)+Komorka.miliDbm2Wat(Double.parseDouble(rekord.get(2).replaceAll(",", ".")) * 10))/10);
                rekord.set(2, newVal); // suma mocy
                rekord.set(3, rekord.get(3)+","+wnioski.getValue("PemTiltMax", i)); // suma tiltVal
                rekord.set(5, rekord.get(5)+","+wnioski.getValue("tech", i)); // suma tiltVal
                daneTmp.set(bb, rekord);
            }
        }
        wnioski.setRekordy(daneTmp);
        
        //System.out.println("Select PropertyName,(azimuth*10) as Azymut,PemTxPowerMax,PemTiltMax,BandId from idb2.data.PEMZlecony where PropertyName like '%"+siteShortName+"%'");
        
        
        
        System.out.println(wnioski);
        return wnioski;
    }
    
    public String[][] pobierzWnioski(String siteShortName)           
    {
        this.wnioksi=pobierzWnioskiZAsPemOs(siteShortName);
         String[][] ww=new String[wnioksi.rowCount()][wnioksi.kolumnCount()];
         for(int i=0;i<wnioksi.rowCount();i++)
         {
            ww[i]=wnioksi.getRekord(i).toArray(ww[i]);
            if(ww[i][1].matches("[-][0-9]+"))
            {
                Integer tmp=3600+Integer.parseInt(ww[i][1]);
                ww[i][1]=""+tmp;
            }
         }
         return ww;
    }

    public String[][] pobierzWnioskiChangePower(String siteShortName)           
    {
        this.wnioksi=pobierzWnioskiZAsPemOsChangePower(siteShortName);
         String[][] ww=new String[wnioksi.rowCount()][wnioksi.kolumnCount()];
         for(int i=0;i<wnioksi.rowCount();i++)
         {
            ww[i]=wnioksi.getRekord(i).toArray(ww[i]);
            if(ww[i][1].matches("[-][0-9]+"))
            {
                Integer tmp=3600+Integer.parseInt(ww[i][1]);
                ww[i][1]=""+tmp;
            }
         }
         return ww;
    }

    public OdpowiedzSQL getWnioski()
    {
        return wnioksi;
    }
    
}
