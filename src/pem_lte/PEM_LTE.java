/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import mysqlpackage.Baza;
import mysqlpackage.DataSource;
import mysqlpackage.OdpowiedzSQL;
import nbipackage.NPack;
import nbipackage.NewFile;
import nbipackage.NorthB;
import nbipackage.Paczka;

/**
 *
 * @author turczyt
 */
public class PEM_LTE
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, IOException
    {
        boolean onlyCheck=false;
        boolean powerChange=false;
        boolean powerChangeNbi=false;
        java.util.ArrayList<String> changePowerCommands=new java.util.ArrayList<String>();
        try
        {
           String siteName = args[0].toUpperCase();//"PLN4460";
            String oryginalSiteName=args[0];
            if(siteName.contains("_"))
            {
                if(siteName.split("_").length>1)
                    siteName=siteName.split("_")[1];
            }
            boolean generateOnlyIfOk = true;
            boolean skipStartSimulationCheck = false;
            
            for (int i = 0; i < args.length; i++)
            {
                if (args[i].equalsIgnoreCase("-skipErrors"))
                {
                    generateOnlyIfOk = false;
                }
                else if (args[i].equalsIgnoreCase("-skipSimCheck"))
                {
                    skipStartSimulationCheck = true;
                }
                else if (args[i].equalsIgnoreCase("-checkOnly"))
                {
                    onlyCheck = true;
                    skipStartSimulationCheck = true;
                }
                else if(args[i].equals("-powerChange"))
                {
                    powerChange=true;
                    if(args.length>(i+1))
                    {
                        if(args[i+1].equalsIgnoreCase("-nbi"))
                        {
                            i++;
                            powerChangeNbi=true;
                        }
                    }
                }
                else if (args[i].equalsIgnoreCase("-h"))
                {
                    
                    System.out.println("\r\n########################################################################################\r\n\t\tURUCHOMIENIE\r\n########################################################################################\r\n");
                    if(powerChange)
                    {
                         System.out.println("java -jar PEM_LTE_ChangePowerNBI.jar siteName");
                    System.out.println("\r\n#### DODATKOWE(opcjonalne) PARAMETRY WYWOLANIA: ####\r\n");
                    System.out.println("\t -nbi\tGenerowanie komend oraz wykonanie ich przy pomocy interfejsu NBI");
                    System.out.println("\t -h\t\tWyswietlenie dostepnych opcji");
                    System.out.println("\r\n");
                    }
                    else
                    {
                    System.out.println("java -jar PEM_LTE.jar siteName");
                    System.out.println("\r\n#### DODATKOWE(opcjonalne) PARAMETRY WYWOLANIA: ####\r\n");
                    System.out.println("\t -skipErrors\tWygeneruj komendy dla \"Pasmo/Azymut\" ktore nie zawieraja bledow");
                    System.out.println("\t -skipSimCheck\tWygeneruj komendy pomimo aktualnie trwajacych pomiarow  na danej stacji");
                   
                    System.out.println("\t -checkOnly\tSprawdzenie poprawnosci stacji, bez generowania komend");
                    System.out.println("\t -h\t\tWyswietlenie dostepnych opcji");
                    System.out.println("\r\n");
                    }
                    System.exit(0);
                }
            }
            if(onlyCheck)
            {
                skipStartSimulationCheck=true;
            }
            Idb baza;
            baza = new Idb(1433, "SqlQuery", "SqlQuery1234", "172.16.35.119",null);//, "UTRAN3.IDB2");
            baza.connectMS();

           String[][] aspemOs =null;
            
            
            /* FAKEOWY WNIOSEK ASPEMOS
            
            String[][] aspemOs =new String[][]{
            
             new String[]{ "TOM3301","300","49.0","10.0","900","GSM"},
             new String[]{ "TOM3301","300","52.0","10.0","1800","LTE,UMTS"},
                        
             new String[]{ "TOM3301","1200","49.0","10.0","900","GSM,UMTS,GSM"},
             new String[]{ "TOM3301","1200","49.0","10.0","1800","LTE,UMTS"},
            
             new String[]{ "TOM3301","2400","49.0","10.0","900","GSM,UMTS,GSM"},
             new String[]{ "TOM3301","2400","49.0","10.0","1800","LTE,UMTS"},
              
            
             new String[]{ "TOM3301","3300","49.0","10.0","900","GSM,UMTS,GSM"},
             new String[]{ "TOM3301","3300","49.0","10.0","1800","LTE,UMTS"}
            
              };*/
            
         
            
            
           if(powerChange)
                aspemOs=baza.pobierzWnioskiChangePower(siteName);
            else
                aspemOs=baza.pobierzWnioski(siteName);

             baza.disconnect();

            if (aspemOs.length == 0)
            {
                System.out.println("BRAK WNIOSKOW W BAZIE IDB PASUJACYCH DO WZORCA=*" + siteName + "*");
                System.exit(0);
            }
           

            boolean brakDanych = false;
            for (int a = 0; a < aspemOs.length; a++)
                for (int z = 0; z < aspemOs[a].length; z++)
                {
                    if (aspemOs[a][z].equals(""))
                    {
                        System.out.println("NIEPOPRAWNE DANE W IDB2 CO NAJMNIEJ JEDNA Z WYMAGANYCH WARTOSCI JEST PUSTA");
                        System.exit(0);
                    }
                }

            String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss";
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);
            java.util.Date DataDzisiaj = java.util.Calendar.getInstance().getTime();
            String obecnyDzienCzas = sdf.format(DataDzisiaj);
        
            Idb bazaOncall = new Idb(3306, "turczyt", "turczyt123", "172.16.5.52", "oncall");
            bazaOncall.connectMy();
            try
            {
                String req3G = "select m.M2000_Ip,r.Rnc_Bsc_Id,r.Rnc_Bsc_Name,nt.NodebName as NeName, nb.Nodeb_Name as Name_on_RncSite,nb.Nodeb_Id,nt.NodebType from oncall.konfiguracja_aktualna_NodebType nt left join oncall.konfiguracja_aktualna_nodeb nb on(nt.Ne_index=nb.Nodeb_Index)  left join oncall.konfiguracja_aktualna_rnc_bsc r on (nb.Rnc_Bsc_Index=r.Rnc_Bsc_Index) left join oncall.konfiguracja_aktualna_m2000 m on(m.M2000_Index=r.M2000_Index) where nt.NodebName like '%" + oryginalSiteName + "%';";
                OdpowiedzSQL nodebparams=bazaOncall.wykonajZapytanie(req3G);

                String req4G = "select m.M2000_Ip,e.Enodeb_Id,e.Enodeb_Name from oncall.konfiguracja_aktualna_enodeb e left join oncall.konfiguracja_aktualna_m2000 m on(e.Enodeb_Index=m.M2000_Index) where e.Enodeb_Name like '%" + oryginalSiteName + "%';";
                OdpowiedzSQL enodebparams =bazaOncall.wykonajZapytanie(req4G);

                String req2G = "select m.M2000_Name,m.M2000_Ip,r.Rnc_Bsc_Id,r.Rnc_Bsc_Name,b.Bts_Id,b.Bts_Name from oncall.konfiguracja_aktualna_bts b left join oncall.konfiguracja_aktualna_rnc_bsc r on(b.Rnc_Bsc_Index=r.Rnc_Bsc_Index) left join oncall.konfiguracja_aktualna_m2000 m on(r.M2000_Index=m.M2000_Index) where b.Bts_Name like '%" + siteName + "%';";
                OdpowiedzSQL btsbparams =bazaOncall.wykonajZapytanie(req2G);

                String m2000Ip = null;
                String rncName = null;
                String rncId = null;
                String bscName = null;
                String BtsName = null;
                java.util.ArrayList<String> NeName  =new java.util.ArrayList<String>();
                String nodebName=null;

                if (nodebparams.rowCount() > 0)
                {
                    m2000Ip = nodebparams.getValue("M2000_Ip", 0);
                    rncName = nodebparams.getValue("Rnc_Bsc_Name", 0);
                    rncId = nodebparams.getValue("Rnc_Bsc_Id", 0);
                    for(int z=0;z<nodebparams.rowCount();z++)
                    {
                       String  NeNameTmp = nodebparams.getValue("NeName", z);
                       if(!NeName.contains(NeNameTmp))
                           NeName.add(NeNameTmp);
                       
                    }
                    nodebName=nodebparams.getValue("Name_on_RncSite", 0);
                    System.out.println(nodebparams.toString());
                }

                if (enodebparams.rowCount() > 0)
                {
                    if (m2000Ip == null || m2000Ip.equals(""))
                        m2000Ip = enodebparams.getValue("M2000_Ip", 0);
                   // if (NeName == null || NeName.equals(""))
                    for(int z=0;z<enodebparams.rowCount();z++)
                    {
                       String  NeNameTmp = enodebparams.getValue("Enodeb_Name", z);
                       if(!NeName.contains(NeNameTmp))
                           NeName.add(NeNameTmp);
                       
                    }
                    System.out.println(enodebparams.toString());
                }
                if (btsbparams.rowCount() > 0)
                {
                    if (m2000Ip == null || m2000Ip.equals(""))
                        m2000Ip = btsbparams.getValue("M2000_Ip", 0);
                    bscName = btsbparams.getValue("Rnc_Bsc_Name", 0);
                    BtsName = btsbparams.getValue("Bts_Name", 0);
                    System.out.println(btsbparams.toString());
                }
                
                 String pathBegin = "/usr/samba/utran/PP/WO/SCRIPTS/PEM/" + System.getProperty("user.name") + "/";
                 //String pathBegin = "C:\\TOOL_PROJECTS\\WO\\" + System.getProperty("user.name") + "\\";
                if(onlyCheck)
                    pathBegin="";
                File katalog = new File(pathBegin + siteName);
                if (!katalog.exists())
                    katalog.mkdirs();
                
                String listingsPath=katalog.getAbsolutePath() + "/"+siteName+"_NorthBListings_" + obecnyDzienCzas + ".txt";
                if(powerChange)
                    listingsPath=katalog.getAbsolutePath() + "/"+siteName+"_ChangePower_NorthBListings_" + obecnyDzienCzas + ".txt";
                
                NewFile FileStart = new NewFile(katalog.getAbsolutePath() + "/"+siteName+"_START_" + obecnyDzienCzas + ".txt");
                NewFile FileStop = new NewFile(katalog.getAbsolutePath() + "/"+siteName+"_STOP_" + obecnyDzienCzas + ".txt");
                NewFile listF = new NewFile(listingsPath);
                NewFile changePower= new NewFile(katalog.getAbsolutePath() + "/"+siteName+"_ChangePowerMML_" + obecnyDzienCzas + ".txt");
         //    listF.dopisz(baza.getWnioski().toString() + "\r\n");
                
                
                
                 NorthB north=null;
                if(m2000Ip==null||nodebName==null||NeName.isEmpty()||bscName==null||BtsName==null||rncName==null)
                {
                    String mkiReq = "select * from oncall.konfiguracja_aktualna_m2000;";
                     OdpowiedzSQL Mki =bazaOncall.wykonajZapytanie(mkiReq);
                     boolean found=false;
                     
                     if(m2000Ip==null)
                     {
                        for(int m=0;m2000Ip==null&&Mki.rowCount()>m;m++)
                        {
                            north= new NorthB(Mki.getValue("M2000_Ip", m), "U-boot", "utranek098", null);
                            String lstNe=north.make2("LST NEBYOMC:");
                            //String[] getLinia(String[] szukany,String tekst)
                            String[] NeNameLine= listF.getLinia(new String[]{oryginalSiteName}, lstNe);
                            if(NeNameLine!=null)
                            {
                                for(int g=0;g<NeNameLine.length;g++)
                                {
                                    //    System.out.println("FROM NORTH:"+NeNameLine[g]);
                                    m2000Ip=Mki.getValue("M2000_Ip", m);
                                    String NeN=NeNameLine[g].split("    ")[1];
                                    if(!NeName.contains(NeN))
                                    {
                                        NeName.add(NeN);
                                        System.out.println("FROM NORTH:"+NeN);
                                    }
                                    String nodebF=north.make(NeN, "LST NODEBFUNCTION:");
                                    //  System.out.println(nodebF);
                                    NPack nnn=new NPack(nodebF,NPack.FORMAT_PIONOWY);
                                    if(nnn.getAllPacks().size()>0)
                                        nodebName=nnn.getAllPacks().get(0).getWartosc("NodeB Function Name");
                                    ////POZOSTAJE Przeszukac RNC/BSC, brakuje rncName, bscName, BtsName   w petli na podstawie konfiguracja_aktualna_rncbsc
                                    found=true;
                                }
                           }
                           else
                               north.closeBuffor();
                        }
                     }
                     else
                     {
                         north= new NorthB(m2000Ip, "U-boot", "utranek098", null);
                     }
                     if(m2000Ip!=null&&north!=null)
                     {
                        if(rncName==null||nodebName==null||NeName.isEmpty())
                        {
                           String lstNe=north.make2("LST NEBYOMC:");
                           
                           String[] NeNameLine= listF.getLinia(new String[]{oryginalSiteName}, lstNe);
                           
                           if(NeNameLine!=null)
                           {
                               for(int g=0;g<NeNameLine.length;g++)
                               {
                               //m2000Ip=Mki.getValue("M2000_Ip", m);
                               String NeN=NeNameLine[g].split("    ")[1];
                               if(!NeName.contains(NeN))
                                   NeName.add(NeN);
                               NPack nnn=new NPack(north.make(NeN, "LST NODEBFUNCTION:"),NPack.FORMAT_PIONOWY);
                               if(nnn.getAllPacks().size()>0)
                                   nodebName=nnn.getAllPacks().get(0).getWartosc("NodeB Function Name");
                                ////POZOSTAJE Przeszukac RNC/BSC, brakuje rncName, bscName, BtsName   w petli na podstawie konfiguracja_aktualna_rncbsc
                               found=true;
                               }
     
                               
                               
                               
                               String rncReq = "select * from oncall.konfiguracja_aktualna_rnc_bsc r where r.M2000_Index=(select m.M2000_Index from oncall.konfiguracja_aktualna_m2000 m where m.M2000_Ip='"+m2000Ip+"') and r.Rnc_Bsc_Name like '%RNC%';";
                                OdpowiedzSQL rncs =bazaOncall.wykonajZapytanie(rncReq);
                               for(int r=0;rncName==null&&r<rncs.rowCount();r++) 
                               {
                                   String odpRe=north.make(rncs.getValue("Rnc_Bsc_Name", r), "LST UNODEB:LSTTYPE=BYNODEBNAME, NODEBNAME=\""+nodebName+"\"");
                                  
                                   if(odpRe.contains("RETCODE = 0")&&odpRe.contains("Number of results"))
                                   {
                                       rncName=rncs.getValue("Rnc_Bsc_Name", r);
                                       rncId=rncs.getValue("Rnc_Bsc_Id", r);
                                       System.out.println("ZNALEZIONO: "+rncName+" "+rncId);
                                   }
                               }
                                
                                ////POZOSTAJE Przeszukac RNC/BSC, brakuje rncName, bscName, BtsName   w petli na podstawie konfiguracja_aktualna_rncbsc
                               
                           }
                        }
                        if(bscName==null||BtsName==null)
                        {
                                String bscReq = "select * from oncall.konfiguracja_aktualna_rnc_bsc r where r.M2000_Index=(select m.M2000_Index from oncall.konfiguracja_aktualna_m2000 m where m.M2000_Ip='"+m2000Ip+"') and r.Rnc_Bsc_Name like '%BSC%';";
                                OdpowiedzSQL bscs =bazaOncall.wykonajZapytanie(bscReq);
                               for(int r=0;bscName==null&&r<bscs.rowCount();r++) 
                               {
                                   String odpRe=north.make(bscs.getValue("Rnc_Bsc_Name", r), "LST BTS:");
                                   
                                   if(odpRe.contains("RETCODE = 0")&&odpRe.contains(oryginalSiteName))
                                   {
                                       bscName=bscs.getValue("Rnc_Bsc_Name", r);
                                       NPack btsy=new NPack(odpRe,NPack.FORMAT_POZIOMY);
                                       java.util.ArrayList<Paczka> lst=btsy.getAllPacks();
                                       for(int b=0;BtsName==null&&b<lst.size();b++)
                                       {
                                           if(lst.get(b).getWartosc("BTS Name").contains(oryginalSiteName))
                                           {
                                               BtsName=lst.get(b).getWartosc("BTS Name");
                                                System.out.println("ZNALEZIONO: "+bscName+" "+BtsName);
                                           }
                                       }
                                   }
                               }
                        }
                     }
                     else
                     {
                         System.out.println("NIE ZNALEZIONO(NORTHB) STACJI PASUJACEJ DO WZORCA=*" + siteName + "*");
                         listF.dopisz("BRAK DANYCH W BAZIE UTRAN PASUJACYCH DO WZORCA=*" + siteName + "*");
                         System.exit(0);
                     }
                }
                else
                    north= new NorthB(m2000Ip, "U-boot", "utranek098", null);
                
                            

                String START = "";
                String STOP = "";
                String ERROR = "";
                
                
                
                if (m2000Ip != null && NeName != null)
                {
                    if (!skipStartSimulationCheck)
                    {
                        
                        
                         boolean simAct = false;
                         String output = "";
                        for(int i=0;i<NeName.size();i++)
                        {
                            String simUmts = north.make(NeName.get(i), "DSP DLSIM:");
                            String simLte = north.make(NeName.get(i), "LST CELLSIMULOAD:");
                            if (simUmts.contains("Successful Local Cell List"))
                            {
                                simAct = true;
                                output = output + "\r\n" + simUmts;
                            }
                            if (simLte.contains("List Configuration of Cell Simulated Load"))
                            {
                                simAct = true;
                                output = output + "\r\n" + simLte;
                            }
                        }
                        System.out.println(output+"\r\n\r\nsimAct="+simAct+" powerChange="+powerChange);
                        north.closeBuffor();
                        
                        if ((simAct&&!powerChange))
                        {
                            output = output + "\r\n\r\n\r\n##########################################################\r\n\r\n\t\tDZIALANIE APLIKACJI PRZERWANE;NA STACJI AKTUALNIE SA WYKONYWANE PEM\r\n\r\n##########################################################\r\n\r\n";
                            System.out.println(output);
                            listF.dopisz(output);
                            System.exit(0);
                        }
                        else if(powerChange&&!simAct)
                        {
                             output = output + "\r\n\r\n\r\n##########################################################\r\n\r\n\t\tDZIALANIE APLIKACJI PRZERWANE;NA STACJI NIE SA AKTUALNIE WYKONYWANE PEM(brak aktywnych symulacji)\r\n\r\n##########################################################\r\n\r\n";
                            System.out.println(output);
                            listF.dopisz(output);
                            System.exit(0);
                        }
                            
                    }
                }
                else
                {
                    System.out.println("BRAK DANYCH W BAZIE UTRAN PASUJACYCH DO WZORCA=*" + siteName + "*");
                    listF.dopisz("BRAK DANYCH W BAZIE UTRAN PASUJACYCH DO WZORCA=*" + siteName + "*");
                    System.exit(0);
                }
                java.util.ArrayList<sektor> wnioski=new java.util.ArrayList<sektor>();
                
                for(int n=0;n<NeName.size();n++)
                {
                    System.out.println("Pobieranie danych dla:"+NeName.get(n)+ " "+n+"/"+NeName.size());
                    SectorFactory factory = new SectorFactory(m2000Ip, rncName,rncId,nodebName, bscName, BtsName, NeName.get(n), bazaOncall, listF,onlyCheck,powerChange);
                    java.util.ArrayList<sektor> wnioskiTmp = factory.getSektoryNaStacji();
                   
                    if(factory.retProblem)
                    {
                        ERROR = ERROR + "\r\n//######### BLEDNE WARTOSCI SECTOR_ID W DSP RETSUBUNIT: #################\r\n"+factory.badRet+"\r\n"; 
                    }
                    
                    for(int t=0;t<wnioskiTmp.size();t++)
                    {
                        String azymutTmp=wnioskiTmp.get(t).getAzymut();
                        String pasmoTmp=wnioskiTmp.get(t).getPasmo();
                        boolean notExist=true;
                        for(int w=0;w<wnioski.size();w++)
                        {
                             String azymutW=wnioski.get(w).getAzymut();
                             String pasmoW=wnioski.get(w).getPasmo();
                             
                             if(azymutTmp.equals(azymutW)&&pasmoTmp.equals(pasmoW))
                             {
                                 notExist=false;
                                 if(! wnioski.get(w).isAllOk())
                                 {
                                     wnioski.set(w, wnioskiTmp.get(t));
                                 }
                             }
                        }                        
                        if(notExist)
                        {
                            wnioski.add(wnioskiTmp.get(t));
                        }
                    }                    
                }
                
                

                boolean Ok = true;
                String bledy = "";
                for (int w = 0; w < wnioski.size(); w++)
                {
                    if (!wnioski.get(w).isAllOk())
                    {
                        ERROR = ERROR + "######AZYMUT=" + (Double.parseDouble(wnioski.get(w).getAzymut())/10.0) + "[stopni] PASMO=" + wnioski.get(w).getPasmo() + "######\r\n" + wnioski.get(w).getErrors() + "\r\n\r\n##########################################################\r\n";
                        Ok = false;
                    }
                }
                String RELACJESTART = "";
                for (int a = 0; a < aspemOs.length; a++)
                {
                    int azymutAsOs = Integer.parseInt(aspemOs[a][1]);
                    String mocAsOs = "" + Komorka.miliDbm2Wat(Double.parseDouble(aspemOs[a][2].replaceAll(",", ".")) * 10);
                    String tiltAsOs = aspemOs[a][3];
                    String bandAsOs = aspemOs[a][4];
                    String tiltToSetTech=aspemOs[a][5];
                    if(powerChange)
                    {
                        //changePower.dopisz("\r\n\r\n//////////////azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + " mocAsOs=" + mocAsOs + " tiltAsOs=" + tiltAsOs + "////////////\r\n");
                        changePowerCommands.add("//////////////azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + " mocAsOs=" + mocAsOs + " tiltAsOs=" + tiltAsOs + "////////////");
                    }
                    else
                    {
                        START = START + "\r\n\r\n//////////////azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + " mocAsOs=" + mocAsOs + " tiltAsOs=" + tiltAsOs + "////////////\r\n";
                        STOP  = STOP  + "\r\n\r\n//////////////azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + " mocAsOs=" + mocAsOs + " tiltAsOs=" + tiltAsOs + "////////////\r\n";
                    }
                    boolean found = false;

                    for (int w = 0; w < wnioski.size(); w++)
                    {
                        if (Integer.parseInt(wnioski.get(w).getAzymut()) == azymutAsOs && wnioski.get(w).getPasmo().equals(bandAsOs))
                        {
                            found = true;
                            wnioski.get(w).setMocAsOs(Double.parseDouble(mocAsOs));
                            wnioski.get(w).preparePowerToSet();
                            wnioski.get(w).setTiltToSet(tiltAsOs);
                            wnioski.get(w).setTiltToSetTech(tiltToSetTech);
                            wnioski.get(w).prepareTiltsComms();
                            if(wnioski.get(w).isAllOk())
                            {
                                if(!onlyCheck)
                                {
                                    java.util.ArrayList<Komorka> cells = wnioski.get(w).getKomorki();
                                    if(!powerChange)
                                    {
                                        START = START + wnioski.get(w).getSetTiltMML_START();
                                    }
                                    for (int c = 0; c < cells.size(); c++)
                                    {
                                        if(powerChange)
                                        {
                                            changePowerCommands.addAll(Arrays.asList(cells.get(c).getSimulationMML_STOP().split("\r\n")));
                                           // changePower.dopisz(cells.get(c).getSimulationMML_STOP()+"\r\n");
                                        }
                                        else
                                        {
                                            STOP = STOP + cells.get(c).getSimulationMML_STOP();
                                            START = START + cells.get(c).getActDeaCell_START();
                                        }
                                    }
                                    if(powerChange)
                                    {
                                        //changePower.dopisz(wnioski.get(w).getSetTiltMML_START()+"\r\n");
                                        changePowerCommands.addAll(Arrays.asList(wnioski.get(w).getSetTiltMML_START().split("\r\n")));
                                    }
                                    if(!powerChange)
                                        STOP = STOP + wnioski.get(w).getSetTiltMML_STOP();
                                    for (int c = 0; c < cells.size(); c++)
                                    {
                                        if(powerChange)
                                        {
                                            changePowerCommands.addAll(Arrays.asList(cells.get(c).getSetPowMML_START().split("\r\n")));
                                            //changePower.dopisz(cells.get(c).getSetPowMML_START()+"\r\n");
                                        }
                                        else
                                        {
                                            START=START +cells.get(c).getConfigCheck_START();
                                            START = START + cells.get(c).getSetPowMML_START();
                                            START = START + cells.get(c).getBlkUblkMML_START();
                                            STOP = STOP + cells.get(c).getSetPowMML_STOP();
                                            STOP = STOP + cells.get(c).getBlkUblkMML_STOP() + "\r\n";
                                        }
                                    }
                                    if(!powerChange)
                                    {
                                    for (int c = 0; c < cells.size(); c++)
                                    {
                                        RELACJESTART = RELACJESTART + cells.get(c).getBlkUblkRealtion_START() + "\r\n";
                                        STOP = STOP + cells.get(c).getBlkUblkRealtion_STOP() + "\r\n";
                                    }
                                    }
                                }
                            }
                            else
                            {
                                ERROR = ERROR + "\r\n//##########################AZYMUT=" + (Double.parseDouble(wnioski.get(w).getAzymut())/10.0) + "[stopni] PASMO=" + wnioski.get(w).getPasmo() + "########################\r\n//###\t" + wnioski.get(w).getErrors() + "\r\n//######################\r\n";

                            }
                            listF.dopisz("\r\n\r\n///////////////azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + " mocAsOs=" + mocAsOs + " tiltAsOs=" + tiltAsOs + "////////////\r\n");
                            listF.dopisz(wnioski.get(w).toString() + "\r\n");
                        }
                    }
                    if (!found)
                    {
                        ERROR = ERROR + "\r\n//##########################################################\r\n//###\tNie istnieje na stacji:azymutAsOs=" + (azymutAsOs/10) + "[stopni] bandAsOs=" + bandAsOs + "\r\n//##########################################################\r\n";

                    }
                }

                String SYMULACJE = "";
                for (int a = 0; a < aspemOs.length; a++)
                {
                    int azymutAsOs = Integer.parseInt(aspemOs[a][1]);
                    String mocAsOs = "" + Komorka.miliDbm2Wat(Double.parseDouble(aspemOs[a][2].replaceAll(",", ".")) * 10);
                    String tiltAsOs = aspemOs[a][3];
                    String bandAsOs = aspemOs[a][4];
                    boolean found = false;
                    for (int w = 0; w < wnioski.size(); w++)
                    {
                        if (Integer.parseInt(wnioski.get(w).getAzymut()) == azymutAsOs && wnioski.get(w).getPasmo().equals(bandAsOs))
                        {
                            found = true;

                            if (wnioski.get(w).isAllOk())
                            {
                                java.util.ArrayList<Komorka> cells = wnioski.get(w).getKomorki();

                                for (int c = 0; c < cells.size(); c++)
                                {
                                    
                                    if(powerChange)
                                    {
                                        changePowerCommands.addAll(Arrays.asList(cells.get(c).getSimulationMML_START().split("\r\n")));
                                        //changePower.dopisz(cells.get(c).getSimulationMML_START()+"\r\n");
                                    }
                                    else
                                        SYMULACJE = SYMULACJE + cells.get(c).getSimulationMML_START() + "\r\n";
                                }
                            }
                        }
                    }
                }
                START = START + RELACJESTART + SYMULACJE;


                if(!onlyCheck)
                {
                    
                    if (!ERROR.equals(""))
                    {
                        System.out.println("BLEDY:\r\n\r\n" + ERROR);
                        listF.dopisz(ERROR);
                    }
                    if(!powerChange)
                    {
                        if (generateOnlyIfOk == false || ERROR.equals(""))
                        {
                            FileStart.dopisz(START);
                            FileStop.dopisz(STOP);
                            System.out.println("GENEROWANIE KOMEND ZAKONCZONE (" + aspemOs.length + "):\r\n\t-" + FileStart.pass() + "\r\n\t-" + FileStop.pass() + "\r\n\t-" + listF.pass());
                        }
                    }
                    else
                    {
                        changePower.dopisz(changePowerCommands);
                       if(powerChangeNbi)
                       {
                           NBISender sender=new NBISender(m2000Ip, "U-boot","utranek098", listF, changePowerCommands);
                           sender.executeCommands();
                           if(sender.isAllCommandSucceded())
                           {
                               System.out.println("\r\r\r\n#################################\r\n");
                               System.out.println(" ZMIANA MOCY ZAKONCZONA");
                               System.out.println(" SCZEGOLY OPERACJI:\r\n\t-" + changePower.pass() + "\r\n\t-" + listF.pass());
                               System.out.println("\r\r\r\n#################################\r\n");
                           }
                           else
                           {
                               System.out.println("\r\r\r\n#################################\r\n");
                               System.out.println(" ZMIANA MOCY ZAKONCZONA,NIE UDANYCH KOMEND:"+sender.getlicznikBledow());
                               System.out.println(sender.getErrorsInfo());
                               System.out.println(" SCZEGOLY OPERACJI:\r\n\t-" + changePower.pass() + "\r\n\t-" + listF.pass());
                               System.out.println("\r\r\r\n#################################\r\n");
                               
                           }
                       }
                       else
                       {
                           System.out.println("GENEROWANIE KOMEND ZAKONCZONE (" + aspemOs.length + "):\r\n\t-" + changePower.pass() + "\r\n\t-" + listF.pass());
                       }
                    }
                }
                else
                {
                    String odp="\r\r\r\n###########  TEST POPRAWNOSCI DANYCH :"+siteName+"  ###########\r\n";
                    if(ERROR.equals(""))
                    {
                        odp=odp+"\t\t DANE POPRAWNE\r\n\r\n";
                    }
                    else
                    {
                       odp=odp+"\t\tBLEDY:\r\n"+ERROR+"\r\n\r\n";
                    }
                    odp=odp+"#############################################################";
                    
                    listF.dopisz("\r\n"+odp);
                    System.out.println(odp);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                   bazaOncall.disconnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception ee)
        {
            ee.printStackTrace();
        }
    }
}
