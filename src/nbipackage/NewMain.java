/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbipackage;

/**
 *
 * @author turczyt
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {	
	NBILogger loger=new NBILogger("/usr/samba/utran/PP/NorthBLogInfo/",Systemowe.user.toUpperCase(),args,new Integer[]{NBILogger.FROM_ARGS,NBILogger.FROM_FILE,NBILogger.FROM_CMD});
	System.out.println("login="+loger.getLogin()+" haslo="+loger.getPasswd());
    }
}
