/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
package nbipackage;
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;


public class Tunel
{
	String toolHost;
	String toolUser;
	String toolUserPass;

	public Tunel(String toolHost,String toolUser,String toolUserPass)
	{
		this.toolHost=toolHost;
		this.toolUser=toolUser;
		this.toolUserPass=toolUserPass;
	}
	public String execute(String komenda)
	{
		String odp="";
		try
		{
			JSch jsch=new JSch();
			String host=this.toolHost;
			String user=this.toolUser;
			
			Session session=jsch.getSession(user,host,22);
			session.setPassword(this.toolUserPass);
			session.setConfig("StrictHostKeyChecking","no");
			//session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
			session.setConfig("PreferredAuthentications","password");

			
			session.connect();
			String command=komenda;
			
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec)channel).setErrStream(System.err);
			InputStream in=channel.getInputStream();
			InputStream in2=channel.getExtInputStream();
			channel.connect();
			/*
			 * Session session=jsch.getSession(user,host,22);
				//UserInfo ui=new MyUserInfo();
				//session.setUserInfo(ui);
				session.setConfig("StrictHostKeyChecking","no");
				session.setTimeout(15000);
				session.setPassword(this.toolUserPass);
				session.connect();
				String command=komenda;//JOptionPane.showInputDialog("Enter command","set|grep SSH");
				Channel channel=session.openChannel("exec");
				((ChannelExec)channel).setCommand(command);
				channel.setInputStream(null);
				((ChannelExec)channel).setErrStream(System.err);
				InputStream in=channel.getInputStream();
			 */


			byte[] tmp=new byte[1024];
			while(true)
			{
				while(in.available()>0)
				{
					int i=in.read(tmp,0,1024);
					if(i<0)
						break;
					odp=odp+new String(tmp,0,i);

				}
				while(in2.available()>0)
				{
					int i=in2.read(tmp,0,1024);
					if(i<0)
						break;
					odp=odp+new String(tmp,0,i);

				}
				odp=odp+";"+"exit-status: "+channel.getExitStatus();
				if(channel.isClosed())
				{
					System.out.println("exit-status: "+channel.getExitStatus());
					break;
				}
				try
				{
					Thread.sleep(100);
				}
				catch(Exception ee)
				{
					return "RETCODE = X transport error";
				}

			}
			channel.disconnect();
			session.disconnect();
			return odp;
		}
		catch(Exception e)
		{
		    e.printStackTrace();
			return "RETCODE = X transport error";
		}
	}
	public boolean copyFromServ(String passRemote,String passLocal,boolean hide)
	{

		java.util.ArrayList<String> listaOdp=new java.util.ArrayList<String>();

		try {
			JSch jsch=new JSch();
			String host=this.toolHost;
			String user=this.toolUser;

			Session session=jsch.getSession(user,host,22);
			System.out.println("Create sftp session successfull");
			session.setConfig("StrictHostKeyChecking","no");
			//session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
			session.setConfig("PreferredAuthentications","password");
			session.setPassword(this.toolUserPass);

			session.connect();
			System.out.println("Connect with remote serwer success");

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			System.out.println("Open sftpChannel OK");
			System.out.println("start download "+passRemote+" file");
			sftpChannel.get(passRemote, passLocal);
			sftpChannel.exit();
			System.out.println("Close sftpChannel OK");
			session.disconnect();
			if(hide)
			{

				Runtime.getRuntime().exec("attrib +H "+passLocal);
			}

		}
		catch (JSchException e)
		{

			e.printStackTrace();
                        return false;
		}
		catch (SftpException e)
		{
			e.printStackTrace();
                        return false;
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
                        return false;
		}
		return true;
	}
}




