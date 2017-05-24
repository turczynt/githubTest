/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbipackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author turczyt
 */
public class NBILogger
{
    String systemUser;
    String[] args;
    private String login;
    private String passwd;
    String logInfDir;

    public static int FROM_ARGS=0;
    public static int FROM_FILE=1;
    public static int FROM_CMD=2;




   /* public NBILogger(String logInfDir,String systemUser, String[] args)
    {
	this.logInfDir=logInfDir;
	this.systemUser = systemUser;
	this.args = args;
	this.login=null;
	this.passwd=null;
	for(int i=0;i<args.length;i++)
	{
	    
	    if(args[i].equals("-u")&&args.length>(i+1))
	    {
		this.login=args[i+1];
	    }
	    if(args[i].equals("-p")&&args.length>(i+1))
	    {
		this.passwd=args[i+1];
	    }
	}
	if(login!=null&&passwd!=null)
	{
	    
	    NewFile logInfo=new NewFile(logInfDir+systemUser+"_NBIlogInfo.conf");
	    logInfo.setReadOnlyForOwner();
	    if(logInfo.istnieje())
	    {
		String loginFile=logInfo.getParamValue("login");
		String passwdFile=logInfo.getParamValue("passwd");
		if(loginFile==null||passwdFile==null)
		{
		    logInfo.clear();
		    logInfo.dopisz("login="+login+"\n");
		    logInfo.dopisz("passwd="+passwd+"\n");
		}
	    }
	    else
	    {
	
		    logInfo=new NewFile(logInfDir+systemUser+"_NBIlogInfo.conf");
		    logInfo.setReadOnlyForOwner();
		    logInfo.dopisz("login="+login+"\n");
		    logInfo.dopisz("passwd="+passwd+"\n");

	    }
	}
	if(login==null||passwd==null)
	{
	    NewFile logInfo=new NewFile(logInfDir+systemUser+"_NBIlogInfo.conf");
	    logInfo.setReadOnlyForOwner();
	    if(logInfo.istnieje())
	    {
		this.login=logInfo.getParamValue("login");
		this.passwd=logInfo.getParamValue("passwd");
	    }
	}
    }*/


    public NBILogger(String logInfDir,String systemUser,String[] args,Integer[] getInfoFrom)
    {
	
	this.logInfDir=logInfDir;
	this.systemUser = systemUser;
	this.args = args;
	this.login=null;
	this.passwd=null;
	boolean infoGetOk=false;
	for(int i=0;i<getInfoFrom.length;i++)
	{
	    if(!infoGetOk&&getInfoFrom[i]==FROM_ARGS)
	    {
		infoGetOk=getLogInfoFromArgs();
	    }
	    if(!infoGetOk&&getInfoFrom[i]==FROM_FILE)
	    {
		infoGetOk=getLogInfoFromConfFile();
	    }
	    if(!infoGetOk&&getInfoFrom[i]==FROM_CMD)
	    {
		infoGetOk=getLogInfoFromCmd();
	    }
	}
	if(infoGetOk)
	{
	    rewriteLogInfoFile();
	}
    }

    public boolean getLogInfoFromArgs()
    {
	if(args==null||args.length==0)
	    return false;
	for(int i=0;i<args.length;i++)
	{

	    if(args[i].equals("-u")&&args.length>(i+1))
	    {
		this.login=args[i+1];
	    }
	    if(args[i].equals("-p")&&args.length>(i+1))
	    {
		this.passwd=args[i+1];
	    }
	}
	if(this.login!=null&&this.login.length()!=0&&this.passwd!=null&&this.passwd.length()!=0)
	    return true;
	else
	    return false;
    }
    public boolean getLogInfoFromConfFile()
    {
	Properties prop = new Properties();

    	try {
               //load a properties file

	    File ff=new File(logInfDir+systemUser+"_NBIlogInfo.conf");
	    if(ff.exists())
	    {
    		prop.load(new FileInputStream(logInfDir+systemUser+"_NBIlogInfo.conf"));

               //get the property value and print it out
		this.login=prop.getProperty("login");
		this.passwd=prop.getProperty("passwd");
		if(this.login!=null&&this.login.length()!=0&&this.passwd!=null&&this.passwd.length()!=0)
		    return true;
		else
		    return false;
	    }
	    return false;
    	}
	catch (IOException ex)
	{
    		ex.printStackTrace();
		return false;
        }
    }

    public boolean getLogInfoFromCmd()
    {
	System.out.println("Podaj nazwe uzytkownika M2000:");
	String loginTmp=NewFile.readFromConsol();
	System.out.println("Podaj haslo:");
	String hasloTmp=NewFile.readFromConsol();
	if(loginTmp!=null&&loginTmp.length()!=0&&hasloTmp!=null&&hasloTmp.length()!=0)
	{
	    this.login=loginTmp;
	    this.passwd=hasloTmp;
	    return true;
	}
	else
	    return false;
    }

    public boolean rewriteLogInfoFile()
    {
	try
	{
	    File ff=new File(logInfDir+systemUser+"_NBIlogInfo.conf");
	    if(!ff.exists())
		ff.createNewFile();
	    
	    FileOutputStream fos = new FileOutputStream(logInfDir+systemUser+"_NBIlogInfo.conf");
	    Properties prop = new Properties();
	    prop.put("login",this.login);
	    prop.put("passwd",this.passwd);
	    prop.store(fos, "Loggin and passwd used by NorthB interface to connect with M2000\r\n");
	    fos.flush();
	    fos.close();
	    NewFile logInfo=new NewFile(logInfDir+systemUser+"_NBIlogInfo.conf");
	    logInfo.setReadOnlyForOwner();
	    return true;
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	    return false;
	}
    }



    public String getLogin()
    {
	return login;
    }
    public String getPasswd()
    {
	return passwd;
    }
}