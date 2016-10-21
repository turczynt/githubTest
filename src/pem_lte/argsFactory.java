/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

/**
 *
 * @author turczyt
 */
public class argsFactory
{
   String[] args;
   String helpText;
   
   private boolean allOk;
   private String cmdOutPut;
   
    private boolean start;
   private  boolean stop;
    private boolean generateOnly;
    private boolean skipSimCheck;
   private  boolean skipErrCheck;
   private  boolean help;
   private  boolean badParam;
   private  String siteName;
   

    public argsFactory(String[] args)
    {
        this.args=args;
        start=false;
        stop=false;
        generateOnly=false;
        skipSimCheck=false;
        skipErrCheck=false;
        help=false; 
        badParam=false;
        siteName=null;
        cmdOutPut="";
        prepareParams();
        this.allOk=checkParams();
    }

    private void prepareParams()
    {
        for(int a=0;a<args.length;a++)
        {
            if (args[a].equalsIgnoreCase("-start"))
            {
                start = true;
            }
            else if (args[a].equalsIgnoreCase("-stop"))
            {
                stop = true;
            }
            else if (args[a].equalsIgnoreCase("-generateFile"))
            {
                generateOnly = true;
            }
            else if (args[a].equalsIgnoreCase("-skipErrors"))
            {
                skipErrCheck = true;
                 generateOnly = true;
             }
             else if (args[a].equalsIgnoreCase("-skipSimCheck"))
             {
                    skipSimCheck = true;
                     generateOnly = true;
             }
             else if (args[a].equalsIgnoreCase("-h"))
             {
                 help=true;
             }
             else if(siteName==null&&args[a].matches("^[A-Za-z]{3}[0-9]{4}[A-Za-z]?"))
             {
                 siteName=args[a];
             }
             else
             {
                 badParam=true;
                 cmdOutPut=cmdOutPut+" param:"+args[a]+" not recognized\r\n";
             }            
        }
    }
    
    private boolean checkParams()
    {
        boolean argsOk=true;
        
        if(badParam)
        {
            argsOk=false;
            
        }
        if(siteName==null)
        {
            argsOk=false;
            cmdOutPut=cmdOutPut+"\tbrak siteName w wywolaniu aplikacji\r\n";
        }
        else if((start||stop)&&(skipSimCheck||skipErrCheck||help))
        {
            cmdOutPut=cmdOutPut+"\t Opcje -star oraz -stop nie moga byc laczone z innymi opcjami" ;
             argsOk=false;
        }
        else if(!(start||stop||generateOnly))
        {
            cmdOutPut=cmdOutPut+"\tWymagana jest jedna z opcji [start;stop;generateFile]" ;
             argsOk=false;
        }
        else if((start&&(stop||generateOnly))||(stop&&(start||generateOnly))||(generateOnly&&(start||stop)))
        {
            cmdOutPut=cmdOutPut+"\tOpcje [start;stop;generateFile]  nie moga byc laczone" ;
             argsOk=false;
        }
        return argsOk;
    }
    
    
    public String[] getArgs()
    {
        return args;
    }

    public String getHelpText()
    {
        return helpText;
    }

    public boolean isAllOk()
    {
        return allOk;
    }

    public boolean isStart()
    {
        return start;
    }

    public boolean isStop()
    {
        return stop;
    }

    public boolean isGenerateOnly()
    {
        return generateOnly;
    }

    public boolean isSkipSimCheck()
    {
        return skipSimCheck;
    }

    public boolean isSkipErrCheck()
    {
        return skipErrCheck;
    }

    public boolean isHelp()
    {
        return help;
    }

    public boolean isBadParam()
    {
        return badParam;
    }

    public String getSiteName()
    {
        return siteName;
    }
    
    public String getErrors()
    {
        return this.cmdOutPut;
    }
}
