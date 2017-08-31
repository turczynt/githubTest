/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import mysqlpackage.Baza;
import mysqlpackage.OdpowiedzSQL;
import nbipackage.NBIAnsException;
import nbipackage.NPack;
import nbipackage.NewFile;
import nbipackage.NorthB;
import nbipackage.Paczka;

/**
 *
 * @author turczyt
 */
public class SectorFactory
{

    String m2000Ip;
    boolean onlyCheck;
    NewFile listF;
    String rncName;
    String rncId;
    String bscName;
    String BtsName;
    String NeName;
    NorthB north;
    Idb testStatement;
    String nodebName;
    String badRet;
    boolean retProblem;
    boolean changePower;
    private java.util.ArrayList<sektor> sektoryNaStacji;

    public SectorFactory(String m2000Ip, String rncName, String rncId, String nodebName, String bscName, String BtsName, String NeName, Idb testStatement, NewFile listF, boolean onlyCheck, boolean changePower) throws SQLException
    {
        try
        {
            this.changePower = changePower;
            badRet = "";
            retProblem = false;
            this.rncId = rncId;
            this.listF = listF;
            this.m2000Ip = m2000Ip;
            this.rncName = rncName;
            this.bscName = bscName;
            this.BtsName = BtsName;
            this.NeName = NeName;
            this.onlyCheck = onlyCheck;
            this.nodebName = nodebName;
            this.testStatement = testStatement;
            this.sektoryNaStacji = new java.util.ArrayList<sektor>();
            getActualSiteInf();

        }
        catch (NBIAnsException ex)
        {
            Logger.getLogger(SectorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getActualSiteInf() throws NBIAnsException, SQLException
    {
        north = new NorthB(this.m2000Ip, "U-boot", "utranek098", null);

        String lstGcelllcs = north.make(this.bscName, "LST GCELLLCS: IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");

        String lstGcell = north.make(this.bscName, "LST GCELL:IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        java.util.ArrayList<Paczka> gcell_lcs = (new NPack(lstGcelllcs, NPack.FORMAT_POZIOMY)).getAllPacks();
        java.util.ArrayList<Paczka> gcell = (new NPack(lstGcell, NPack.FORMAT_POZIOMY)).getAllPacks();

        listF.dopisz(lstGcelllcs + "\r\n");
        listF.dopisz(lstGcell + "\r\n");


        String trx = north.make(this.bscName, "LST GTRX:IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        listF.dopisz(trx + "\r\n");
        NPack trxPack = new NPack(trx, NPack.FORMAT_POZIOMY);
        java.util.ArrayList<Paczka> trxLst = trxPack.getAllPacks();

        String bindLocGrp = north.make(this.bscName, "LST BTSBINDLOCGRP: IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        listF.dopisz(bindLocGrp + "\r\n");
        java.util.ArrayList<Paczka> bindLocGrpLst = (new NPack(bindLocGrp, NPack.FORMAT_POZIOMY)).getAllPacks();

        String BtsLocGrp = north.make(this.bscName, "LST BTSLOCGRP: IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        listF.dopisz(BtsLocGrp + "\r\n");
        java.util.ArrayList<Paczka> BtsLocGrpLst = (new NPack(BtsLocGrp, NPack.FORMAT_POZIOMY)).getAllPacks();

        String GULBtsLocGrpStr = north.make(this.bscName, "LST BTSLOCGRPE: IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        listF.dopisz(GULBtsLocGrpStr + "\r\n");
        java.util.ArrayList<Paczka> GULBtsLocGrp = (new NPack(GULBtsLocGrpStr, NPack.FORMAT_POZIOMY)).getAllPacks();


        String dspGtrxOnNode = north.make(this.NeName, "DSP GTRX:");
        listF.dopisz(dspGtrxOnNode + "\r\n");
        java.util.ArrayList<Paczka> dspGtrxListOnNode = null;
        if (dspGtrxOnNode.contains("RETCODE = 0") && Pattern.compile("[Number of results =]([1-9]|([1-9][0-9]))\\s*[)]").matcher(dspGtrxOnNode).find())
        {
            dspGtrxListOnNode = (new NPack(dspGtrxOnNode, NPack.FORMAT_POZIOMY)).getAllPacks();
         
            //System.exit(0);
        }


        /*GUL section commands
         ##################################################
         DSP GTRX:;
         Display GBTS TRX
         ----------------
         Local Cell ID  Cell Index  Cell No.  TRX Index  TRX No.  TRX Group ID  Is BCCH TRX  Whether Primary TRX or Not  License Authorized  Effective Status  Sector ID  Sector Equipment ID  Baseband Board Cabinet No.  Baseband Board Subrack No.  Baseband Board Slot No.
         ##################################################
                   
         LST GTRX:;%%
         List GBTS TRX
         -------------
         Local Cell ID  Cell Index  Cell No.  TRX Index  TRX No.
         
         */



        String lstSec = north.make(this.NeName, "LST SECTOR:;");

        NPack secPack = new NPack(lstSec, NPack.FORMAT_POZIOMY);
        if (lstSec.contains("(Number of results = 1)"))
            secPack = new NPack(lstSec, NPack.FORMAT_PIONOWY);
        java.util.ArrayList<Paczka> sectorLst = secPack.getAllPacks();

        String lstRet = north.make(this.NeName, "LST RET:;");
        listF.dopisz(lstRet + "\r\n");
        java.util.ArrayList<Paczka> rety = (new NPack(lstRet, NPack.FORMAT_POZIOMY)).getAllPacks();
        java.util.ArrayList<Paczka> retsubUnit = (new NPack(lstRet, NPack.FORMAT_POZIOMY)).getAllPacks();

        String cellOnRnc = north.make(this.rncName, "LST UCELL: NODEBNAME=\"" + this.nodebName + "\",LSTFORMAT=HORIZONTAL");
        listF.dopisz(cellOnRnc + "\r\n");
        java.util.ArrayList<Paczka> ucellOnRncSite = (new NPack(cellOnRnc, NPack.FORMAT_POZIOMY)).getAllPacks();

        String rru = north.make(this.NeName, "LST RRU:");

        listF.dopisz(rru + "\r\n");
        java.util.ArrayList<Paczka> rruL = new java.util.ArrayList<Paczka>();
        java.util.ArrayList<String> usedSRN = new java.util.ArrayList<String>();
        if (rru.contains("RETCODE = 0"))
        {
            NPack np = new NPack(rru);
            rruL = np.getAllPacks();
            if (rru.contains("(Number of results = 1)"))
            {
                np = new NPack(rru.split("\n"), new String[]
                {
                    "LST RRU:"
                }, new String[]
                {
                    "-----"
                }, new String[]
                {
                    "---    END"
                });
                rruL = np.getAllPacks();

            }
            for (Paczka singRRU : rruL)
            {
                // System.out.println("USED SRN="+singRRU.getWartosc("Subrack No."));
                String workMode = singRRU.getWartosc("RF Unit Working Mode");
                if (workMode.contains("LTE") || workMode.contains("UMTS"))
                    usedSRN.add(singRRU.getWartosc("Subrack No."));
            }
        }
        java.util.ArrayList<Paczka> rruOnBts = new java.util.ArrayList<Paczka>();
        String rruBts = north.make(this.bscName, "DSP BTSBRD: INFOTYPE=INPOSBRD,IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\"");
        listF.dopisz(rruBts + "\r\n");

        if (rruBts.contains("RETCODE = 0"))
        {
            NPack np = new NPack(rruBts);//.split("\n"),new String[]{"DSP BTSBRD:"},new String[]{"-----"},new String[]{"---    END"});
            java.util.ArrayList<Paczka> btssRRU = np.getAllPacks();
            for (int rr = 0; rr < btssRRU.size(); rr++)
            {
                //
                String brdType = btssRRU.get(rr).getWartosc("Physical Board Type");
                String srn = btssRRU.get(rr).getWartosc("Subrack No.");
                if (brdType.toUpperCase().contains("RRU") && !usedSRN.contains(srn))
                {
                    String cn = btssRRU.get(rr).getWartosc("Cabinet No.");

                    String sn = btssRRU.get(rr).getWartosc("Slot No.");

                    String rruBtsDet = north.make(this.bscName, "DSP BTSBRD: INFOTYPE=RUNPARA,IDTYPE=BYNAME,BTSNAME=\"" + this.BtsName + "\",BRDTYPE=RXU,RXUIDTYPE=SRNSN,RXUCN=" + cn + ",RXUSRN=" + srn + ",RXUSN=" + sn);
                    listF.dopisz(rruBtsDet + "\r\n");
                    NPack rruDetNp = new NPack(rruBtsDet.split("\n"), new String[]
                    {
                        "DSP BTSBRD:"
                    }, new String[]
                    {
                        "-----"
                    }, new String[]
                    {
                        "---    END"
                    });
                    java.util.ArrayList<Paczka> RRUSbTS = rruDetNp.getAllPacks();
                    for (Paczka RRU : RRUSbTS)
                    {
                        RRU.dodaj("Subrack No.", srn);
                        rruOnBts.add(RRU);
                    }
                    //DSP BTSBRD: INFOTYPE=RUNPARA,IDTYPE=BYNAME,BTSNAME="34_SLP3101A_38597G_SLUBICEBLOK",BRDTYPE=RXU,RXUIDTYPE=SRNSN,RXUCN=0,RXUSRN=76,RXUSN=0;
                }
            }

        }


        for (int g = 0; g < rety.size(); g++)
        {

            String devNo = rety.get(g).getWartosc("Device No.");
            String sebNo = "1";//rety.get(g).getWartosc("RET Subunit Number");
            /////DSP RETSUBUNIT:DEVICENO=6,SUBUNITNO=1
            String dspRetSub = north.make(this.NeName, "DSP RETSUBUNIT:DEVICENO=" + devNo + ",SUBUNITNO=" + sebNo + ";");

            java.util.ArrayList<Paczka> tmpSub = (new NPack(dspRetSub, NPack.FORMAT_PIONOWY)).getAllPacks();
            if (tmpSub != null && tmpSub.size() > 0)
            {
                Paczka subU = tmpSub.get(0);
                //if(subU.getWartosc(""))
                String tiltAct = subU.getWartosc("Actual Tilt(0.1degree)");
                if (!tiltAct.matches("[0-9]+"))
                {
                    dspRetSub = north.make(this.NeName, "DSP RETSUBUNIT:DEVICENO=" + devNo + ",SUBUNITNO=" + sebNo + ";");

                    tmpSub = (new NPack(dspRetSub, NPack.FORMAT_PIONOWY)).getAllPacks();
                }
            }


            String devData = north.make(this.NeName, "DSP RETDEVICEDATA:DEVICENO=" + devNo + ",SUBUNITNO=" + sebNo + ";");
            listF.dopisz(dspRetSub + "\r\n" + devData + "\r\n");
            java.util.ArrayList<Paczka> devTmp = (new NPack(devData, NPack.FORMAT_PIONOWY)).getAllPacks();
            if (devTmp != null && devTmp.size() > 0)
            {
                Paczka devDataP = devTmp.get(0);

                //java.util.ArrayList<Paczka> tmpSub = (new NPack(dspRetSub, NPack.FORMAT_PIONOWY)).getAllPacks();
                if (tmpSub != null && tmpSub.size() > 0)
                {
                    Paczka subU = tmpSub.get(0);
                    subU.dodaj("Max Tilt(0.1degree)", devDataP.getWartosc("Max Tilt(0.1degree)"));
                    subU.dodaj("Min Tilt(0.1degree)", devDataP.getWartosc("Min Tilt(0.1degree)"));
                    subU.dodaj("Installed Mechanical Tilt(0.1degree)", devDataP.getWartosc("Installed Mechanical Tilt(0.1degree)"));
                    String retSub_SectorID = subU.getWartosc("Actual Sector ID");
                    String devName = subU.getWartosc("Device Name");
                    if (retSub_SectorID != null && retSub_SectorID.length() > 0 && (retSub_SectorID.equalsIgnoreCase("NULL")))
                    {
                        badRet = badRet + "DEVICENO=" + devNo + " SUBUNITNO=" + sebNo + " DEVNAME=" + devName + " Sector_ID=" + retSub_SectorID + "\r\n";
                        retProblem = true;
                    }

                    //sek.addDspRetSubUnit(subU);
                    retsubUnit.add(subU);



                }
            }

        }

        listF.dopisz(lstSec + "\r\n");
        // System.out.println("Liczba sektorow="+sectorLst.size());
        String lstSecEq = north.make(this.NeName, "LST SECTOREQM:;");

        java.util.ArrayList<Paczka> sectorEqLst = (new NPack(lstSecEq, NPack.FORMAT_POZIOMY)).getAllPacks();
        listF.dopisz(lstSecEq + "\r\n");
        // System.out.println("liczba sectorEq="+sectorEqLst.size());
        java.util.ArrayList<String> used_azymutPasmo=new java.util.ArrayList<String>();//zawiera wszystkie pary azymut;pasmo  (bez powtorzen)
        for(int z=0;z<sectorLst.size();z++)
        {
            String azymut=sectorLst.get(z).getWartosc("Antenna Azimuth(0.1degree)");
            String sektorId=sectorLst.get(z).getWartosc("Sector ID");
            String pasmo=getPasmo(sektorId,sectorEqLst,north);
            if(pasmo!=null)
            {
                // System.out.println("AZ="+azymut+" sektorId="+sektorId+" pasmo="+pasmo);
            if(!used_azymutPasmo.contains(azymut+";"+pasmo))
                {
                used_azymutPasmo.add(azymut+";"+pasmo);
                sektor sek=new pem_lte.sektor(azymut, pasmo,bscName,BtsName,NeName,onlyCheck);
                    sek.addSektors_Id(sektorId);
                sek.addKomorki(this.getCellsOnSectorId(sektorId, sectorEqLst, north,pasmo,ucellOnRncSite));




                java.util.ArrayList<Paczka> sectorsEqDet=getSectEqLst(sektorId, sectorEqLst, north, pasmo);
                    sek.setSectorEqDet(sectorsEqDet);
                java.util.ArrayList<Paczka> rrus=this.gestDspRruLst(sektorId, sectorsEqDet,rruL, north, pasmo);
                    sek.addRru(rrus);

                for(int re=0;re<retsubUnit.size();re++)
                    {
                           String sekId=retsubUnit.get(re).getWartosc("Actual Sector ID");
                           if(sekId.equals(sektorId))
                        {
                            sek.addDspRetSubUnit(retsubUnit.get(re));
                        }
                    }


                    this.sektoryNaStacji.add(sek);

                }
                else
                {
                for(int w=0;w<this.sektoryNaStacji.size();w++)
                    {

                    if(this.sektoryNaStacji.get(w).getAzymut().equals(azymut)&&this.sektoryNaStacji.get(w).getPasmo().equals(pasmo))
                        {
                            this.sektoryNaStacji.get(w).addSektors_Id(sektorId);
                       this.sektoryNaStacji.get(w).addKomorki(this.getCellsOnSectorId(sektorId, sectorEqLst, north,pasmo,ucellOnRncSite));
                       java.util.ArrayList<Paczka> sectorsEqDet=getSectEqLst(sektorId, sectorEqLst, north, pasmo);
                            this.sektoryNaStacji.get(w).setSectorEqDet(sectorsEqDet);
                       java.util.ArrayList<Paczka> rrus=this.gestDspRruLst(sektorId, sectorsEqDet,rruL, north, pasmo);
                            this.sektoryNaStacji.get(w).addRru(rrus);
                       for(int re=0;re<retsubUnit.size();re++)
                            {
                           String sekId=retsubUnit.get(re).getWartosc("Actual Sector ID");
                           if(sekId.equals(sektorId))
                                {
                                    this.sektoryNaStacji.get(w).addDspRetSubUnit(retsubUnit.get(re));
                                }
                            }
                            //

                        }

                    }
                }

            }
        }
        for(int w=0;w<this.sektoryNaStacji.size();w++)
        {

            sektoryNaStacji.get(w).addKomorki(getGcell(sektoryNaStacji.get(w).azymut, sektoryNaStacji.get(w).pasmo, gcell_lcs, gcell, trxLst, bindLocGrpLst, BtsLocGrpLst, GULBtsLocGrp, sektoryNaStacji.get(w).getSRNs(), sektoryNaStacji.get(w).getSRN2Gs(), north, dspGtrxListOnNode, sektoryNaStacji.get(w).getSectorEqDet(),sectorLst));
        }
        //used_azymutPasmo
        //java.util.ArrayList<Paczka> gcell_lcs=(new NPack(lstGcelllcs,NPack.FORMAT_POZIOMY)).getAllPacks();
        //java.util.ArrayList<Paczka> gcell=(new NPack(lstGcell,NPack.FORMAT_POZIOMY)).getAllPacks();
        //azymut [0.1 stopni] pasmo=[800,900,1800,2600]
        //used_azymutPasmo.add(azymut+";"+pasmo);




        /*
         * 
         * PROBA OGARNIECIA GSM STAND ALONE
         */
        for(int l=0;l<gcell_lcs.size();l++)
        {
            String azymutFromCell=gcell_lcs.get(l).getWartosc("Antenna Azimuth Angle");
            if(!azymutFromCell.equals("0"))
                azymutFromCell=azymutFromCell+"0";

                String cellInd=gcell_lcs.get(l).getWartosc("Cell Index");
                String pasmo=null;

                for(int g=0;pasmo==null&&g<gcell.size();g++)
            {

                    if(gcell.get(g).getWartosc("Cell Index").equals(cellInd))
                {
                        String band=gcell.get(g).getWartosc("Freq. Band");

                        if(band.contains("1800"))
                            pasmo="1800";
                        else if(band.contains("900"))
                            pasmo="900";
                }
            }
                if(pasmo!=null)
            {
                // System.out.println("STAND ALONE GSM TEST "+azymutFromCell + ";" + pasmo);
                if (!used_azymutPasmo.contains(azymutFromCell + ";" + pasmo))
                {
                    used_azymutPasmo.add(azymutFromCell + ";" + pasmo);
                    sektor sek = new pem_lte.sektor(azymutFromCell, pasmo, bscName, BtsName, NeName, onlyCheck);
                     java.util.ArrayList<Paczka> sectorsEqDet=getSectEqLst(null, sectorEqLst, north, pasmo);
                    sek.setGsmStandAllone(true);
                        java.util.ArrayList<Paczka> selectedRRU = findStandAloneRruFitsToBandAzymuth(pasmo, azymutFromCell, rruOnBts, trxLst, gcell, gcell_lcs,null,null,null);
                    sek.setRruOnlyGsm(selectedRRU); 
                    sek.addKomorki(getGcell(azymutFromCell, pasmo, gcell_lcs, gcell, trxLst, bindLocGrpLst, BtsLocGrpLst, GULBtsLocGrp, sek.getSRNs(), sek.getSRN2Gs(), north, dspGtrxListOnNode, sectorsEqDet,sectorLst));
                    //   System.out.println("GSM STAND ALONE:" + azymutFromCell + ";" + pasmo);
                    this.sektoryNaStacji.add(sek);


                }
                else
                {
                    //System.out.println("AZYMUT ISTNIEJE W 2G/3G: TEST SRN");
                        for(int w=0;w<this.sektoryNaStacji.size();w++)
                    {
                            if(this.sektoryNaStacji.get(w).getAzymut().equals(azymutFromCell)&&this.sektoryNaStacji.get(w).getPasmo().equals(pasmo))
                        {

                                java.util.ArrayList<String> not2GsrnsUsed=this.sektoryNaStacji.get(w).getSRNs();
                            // System.out.println("ZNALEZIONY AZYMUT/PASMO test SRN:"+not2GsrnsUsed.toString()+" "+not2GsrnsUsed.toArray());
                                java.util.ArrayList<Paczka> selectedRRU = findStandAloneRruFitsToBandAzymuth(pasmo, azymutFromCell, rruOnBts, trxLst, gcell, gcell_lcs,null,null,null);
                                boolean onlyRRU=false;
                                for(Paczka rruGsm:selectedRRU)
                            {
                                //    System.out.println("CHECK:SRN="+rruGsm.getWartosc("Subrack No."));
                                    if(!not2GsrnsUsed.contains(rruGsm.getWartosc("Subrack No.")))
                                {
                                    this.sektoryNaStacji.get(w).addRruOnlyGsm(rruGsm);
                                    //      System.out.println("ADD:SRN="+rruGsm.getWartosc("Subrack No."));
                                        onlyRRU=true;
                                }
                            }
                                if(onlyRRU)
                            {
                                this.sektoryNaStacji.get(w).setGsmStandAllone(true);
                                this.sektoryNaStacji.get(w).setRruOnlyGsm(selectedRRU);
                                this.sektoryNaStacji.get(w).addKomorki(getGcell(azymutFromCell, pasmo, gcell_lcs, gcell, trxLst, bindLocGrpLst, BtsLocGrpLst, GULBtsLocGrp, this.sektoryNaStacji.get(w).getSRNs(), this.sektoryNaStacji.get(w).getSRN2Gs(), north, dspGtrxListOnNode, sektoryNaStacji.get(w).getSectorEqDet(),sectorLst));
                                //  System.out.println("GSM STAND ALONE:" + azymutFromCell + ";" + pasmo); 
                            }
                        }
                    }
                }



            }
        }


        north.closeBuffor();
        //north.make("", rncName)



    }

    public java.util.ArrayList<Komorka> getGcell(String azymut, String pasmo, java.util.ArrayList<Paczka> lstGcelllcs, java.util.ArrayList<Paczka> lstGcell, java.util.ArrayList<Paczka> lstTrx, java.util.ArrayList<Paczka> bindLocGrpLst, java.util.ArrayList<Paczka> BtsLocGrpLst, java.util.ArrayList<Paczka> GulBtsLocGrpLst, java.util.ArrayList<String> srn3G, java.util.ArrayList<String> srnOnlyBts, NorthB north, java.util.ArrayList<Paczka> dspGtrxOnNode, java.util.ArrayList<Paczka> SectorEqDet,java.util.ArrayList<Paczka> sectorLst) throws NBIAnsException
    {
        boolean egbts = false;

        if (dspGtrxOnNode != null && dspGtrxOnNode.size() > 0)
        {
            System.out.println("azymut="+azymut+" pasmo="+pasmo+" GCELL GUL PROCESSING");
            return getGcellGul(azymut, pasmo, lstGcelllcs, lstGcell, lstTrx, bindLocGrpLst, BtsLocGrpLst, GulBtsLocGrpLst, srn3G, srnOnlyBts, north, dspGtrxOnNode, SectorEqDet,sectorLst);
        }
        else
        {
            System.out.println("azymut="+azymut+" pasmo="+pasmo+" GCELL NOT_GUL PROCESSING");
            return getGcellNotGul(azymut, pasmo, lstGcelllcs, lstGcell, lstTrx, bindLocGrpLst, BtsLocGrpLst, srn3G, srnOnlyBts, north);
        }
        //                 
    }

    public java.util.ArrayList<Komorka> getGcellNotGul(String azymut, String pasmo, java.util.ArrayList<Paczka> lstGcelllcs, java.util.ArrayList<Paczka> lstGcell, java.util.ArrayList<Paczka> lstTrx, java.util.ArrayList<Paczka> bindLocGrpLst, java.util.ArrayList<Paczka> BtsLocGrpLst, java.util.ArrayList<String> srn3G, java.util.ArrayList<String> srnOnlyBts, NorthB north) throws NBIAnsException
    {
        java.util.ArrayList<Komorka> kom=new java.util.ArrayList<Komorka>();
        
        // Antenna Azimuth Angle //LCS
        //Cell Index //LCS//GCELL
        //Freq. Band  //GCELL
        //Administrative State  //GCELL
        //active status         //GCELL
        boolean found=false;
        for(int l=0;l<lstGcelllcs.size();l++)
        {
            String azymutFromCell=lstGcelllcs.get(l).getWartosc("Antenna Azimuth Angle");
           if(!azymutFromCell.equals("0"))
                azymutFromCell=azymutFromCell+"0";
           
           String cellInd=lstGcelllcs.get(l).getWartosc("Cell Index");
           String gtr=north.make(this.bscName, "LST GTRXDEV: IDTYPE=BYID,CELLID="+cellInd+",TRXIDTYPE=BYID");
           java.util.ArrayList<Paczka> lstGTrxDev=(new NPack(gtr,NPack.FORMAT_POZIOMY)).getAllPacks();
           listF.dopisz(gtr+"\r\n");
            
            
           String idle=north.make(this.bscName, "LST GCELLIDLEBASIC: IDTYPE=BYID,CELLID="+cellInd);
           java.util.ArrayList<Paczka> lstGidle=(new NPack(idle,NPack.FORMAT_POZIOMY)).getAllPacks();
           listF.dopisz(idle+"\r\n");
            //System.out.println("%%%%%"+lstGcelllcs.get(l).getWartosc("Cell Name")+" azymutFromCell="+azymutFromCell+" AZfromAS="+azymut+"\r\n"+gtr);
            
            if(azymut.equals(azymutFromCell))
            {
              
                
                for(int g=0;g<lstGcell.size();g++)
                {
                    
                    if(lstGcell.get(g).getWartosc("Cell Index").equals(cellInd))
                    {
                        String band=lstGcell.get(g).getWartosc("Freq. Band");
                        String pasmoTmp="";
                        if(band.contains("1800"))
                            pasmoTmp="1800";
                        else if(band.contains("900"))
                            pasmoTmp="900";
                        if(pasmo.equals(pasmoTmp))
                        {
                            KomorkaGsm gcell=new pem_lte.KomorkaGsm(cellInd);
                            gcell.setPasmo(band);
                            gcell.setKontrolerName(this.bscName);
                            gcell.setNeName(this.BtsName);
                            gcell.setLstGcell(lstGcell.get(g));
                            gcell.setLstGcell_lsc(lstGcelllcs.get(l));
                            if(lstGidle!=null&&lstGidle.size()>0)
                                gcell.setLstGcellIdle(lstGidle.get(0));
                            
                            for(int t=0;t<lstTrx.size();t++)
                            {
                                String Cell_IndexFromTRX=lstTrx.get(t).getWartosc("Cell Index");
                                if(cellInd.equals(Cell_IndexFromTRX))
                                {
                                    gcell.setLstTrx(lstTrx.get(t));
                                }
                            }                           
                            gcell.setLstGtrxDev(lstGTrxDev);
                            found=true;
                            for (int bb = 0; bb < BtsLocGrpLst.size(); bb++)
                            {
                                String btsLocIndex=BtsLocGrpLst.get(bb).getWartosc("Cell Index");
                                //System.out.println("*********LOC_GR=" + BtsLocGrpLst.get(bb).getWartosc("Location Group No.") + " IS_MAIN=" + BtsLocGrpLst.get(bb).getWartosc("Is Main Local Group"));
                                boolean main = BtsLocGrpLst.get(bb).getWartosc("Is Main Local Group").equalsIgnoreCase("YES");
                                if (btsLocIndex.equalsIgnoreCase(cellInd))
                                {
                                    gcell.setLstBtsLocgr(BtsLocGrpLst.get(bb));

                                    gcell.setLocGr(true);
                                    gcell.setMainLocGr(main);
                                   // System.out.println("*********ZBINDOWANIE NIEZBIDNOWANEJ\r\n"+gcell.toString());
                                     found=false;                                    
                                }                               
                            }
                            if(found)
                                kom.add(gcell);
                           
                            //System.out.println("###########\r\n###################\r\n###############\r\n"+gcell);
                        }
                    }
                }
            }
            
            ///
        }
        boolean LocGrEx=false;
       // if(!found)
        {
            LocGrEx=true;
            for(int b=0;b<BtsLocGrpLst.size();b++)
            {
                String cellInd=BtsLocGrpLst.get(b).getWartosc("Cell Index");
                String LocGr=BtsLocGrpLst.get(b).getWartosc("Location Group No.");
                boolean main = BtsLocGrpLst.get(b).getWartosc("Is Main Local Group").equalsIgnoreCase("YES");
                //System.out.println("cellInd="+cellInd+" LocGr="+LocGr+" mainLocGr="+main);
                boolean firstMatch=false;
                
                Paczka lstGcellSelected=null;
                
                
                String idle=north.make(this.bscName, "LST GCELLIDLEBASIC: IDTYPE=BYID,CELLID="+cellInd);
                java.util.ArrayList<Paczka> lstGidle=(new NPack(idle,NPack.FORMAT_POZIOMY)).getAllPacks();
                listF.dopisz(idle+"\r\n");
                for(int g=0;g<lstGcell.size();g++)
                {
                    
                    if(lstGcell.get(g).getWartosc("Cell Index").equals(cellInd))
                    {
                        String band=lstGcell.get(g).getWartosc("Freq. Band");
                        String pasmoTmp="";
                        if(band.contains("1800"))
                            pasmoTmp="1800";
                        else if(band.contains("900"))
                            pasmoTmp="900";
                        if(pasmo.equals(pasmoTmp))
                        {
                            lstGcellSelected=lstGcell.get(g);
                        }
                    }
                }
                     
                
                for(int z=0;z<bindLocGrpLst.size()&&!firstMatch;z++)
                {
                    String BindLocGr=bindLocGrpLst.get(z).getWartosc("Sub-Location Group No.");
                  //  System.out.println("BindLocGrr="+BindLocGr);
                    if(BindLocGr.equals(LocGr))
                    {
                        String srn=bindLocGrpLst.get(z).getWartosc("Subrack No.");
                    //     System.out.println("srn="+srn+"  srny3G="+srn3G.toString());
                        if(srn3G.contains(srn))
                        {
                            KomorkaGsm gcell=new pem_lte.KomorkaGsm(cellInd);
                            
                            if(lstGcellSelected!=null)
                            {  gcell.setLstGcell(lstGcellSelected);
                                gcell.setPasmo(lstGcellSelected.getWartosc("Freq. Band"));
                            }
                             for(int t=0;t<lstTrx.size();t++)
                            {
                                String Cell_IndexFromTRX=lstTrx.get(t).getWartosc("Cell Index");
                                if(cellInd.equals(Cell_IndexFromTRX))
                                {
                                    gcell.setLstTrx(lstTrx.get(t));
                                    
                                }
                            }
                             
                             
                             if(lstGidle!=null&&lstGidle.size()>0)
                                gcell.setLstGcellIdle(lstGidle.get(0));
                            
                            gcell.setLstBindLocGr(bindLocGrpLst.get(z));
                            gcell.setLstBtsLocgr(BtsLocGrpLst.get(b));
                            gcell.setKontrolerName(this.bscName);
                            gcell.setNeName(this.BtsName);
                           
                            gcell.setMainLocGr(main);
                            firstMatch=true;
                            kom.add(gcell);
                            //System.out.println("gcell_LocG="+BindLocGr);
                           
                        }
                    }
                }
                //if(!firstMatch)
                {
                    String srn=null;
                    KomorkaGsm gcell=new pem_lte.KomorkaGsm(cellInd);
                     if(lstGcellSelected!=null)
                     {        gcell.setLstGcell(lstGcellSelected);
                      gcell.setPasmo(lstGcellSelected.getWartosc("Freq. Band"));
                     }
                           
                     for(int t=0;t<lstTrx.size();t++)
                     {
                         String Cell_IndexFromTRX=lstTrx.get(t).getWartosc("Cell Index");
                         //System.out.println("Cell_IndexFromTRX="+Cell_IndexFromTRX+" cellInd="+cellInd);
                         if(cellInd.equals(Cell_IndexFromTRX))
                         {
                             
                             if(srn3G.contains(lstTrx.get(t).getWartosc("Subrack No.")))
                             {
                                 srn=lstTrx.get(t).getWartosc("Subrack No.");
                                 gcell.setLstTrx(lstTrx.get(t));
                                 
                                 
                             }
                             if(srnOnlyBts.contains(lstTrx.get(t).getWartosc("Subrack No.")))
                             {
                                 srn=lstTrx.get(t).getWartosc("Subrack No.");
                                 gcell.setLstTrx(lstTrx.get(t));
                                 gcell.setGsmStandAllone(true);
                                 
                                 
                             }
                         }
                      }
                     if(srn!=null)
                     {
                         gcell.setSrn(srn);
                         gcell.setLstBtsLocgr(BtsLocGrpLst.get(b));
                         gcell.setKontrolerName(this.bscName);
                         gcell.setNeName(this.BtsName);
                         gcell.setMainLocGr(main);
                         firstMatch=true;
                         kom.add(gcell);
                     }
                }
            }
        }
        
        return kom;
    }

    public java.util.ArrayList<Komorka> getGcellGul(String azymut, String pasmo, java.util.ArrayList<Paczka> lstGcelllcs, java.util.ArrayList<Paczka> lstGcell, java.util.ArrayList<Paczka> lstTrx, java.util.ArrayList<Paczka> bindLocGrpLst, java.util.ArrayList<Paczka> BtsLocGrpLst, java.util.ArrayList<Paczka> GulBtsLocGrpLst, java.util.ArrayList<String> srn3G, java.util.ArrayList<String> srnOnlyBts, NorthB north, java.util.ArrayList<Paczka> dspGtrxOnNode, java.util.ArrayList<Paczka> SectorEqDet,java.util.ArrayList<Paczka> sectorLst) throws NBIAnsException
    {
        java.util.ArrayList<Komorka> kom = new java.util.ArrayList<Komorka>();
        boolean found = false;



        for (int l = 0; l < lstGcelllcs.size(); l++)
        {
            String azymutFromCell = lstGcelllcs.get(l).getWartosc("Antenna Azimuth Angle");
            if (!azymutFromCell.equals("0"))
                azymutFromCell = azymutFromCell + "0";

            String cellInd = lstGcelllcs.get(l).getWartosc("Cell Index");
       

            boolean quasi = false;

            if (GulBtsLocGrpLst != null && GulBtsLocGrpLst.size() > 0)
            {
                for(int k=0;k<GulBtsLocGrpLst.size();k++)
                {
                    if(GulBtsLocGrpLst.get(k).getWartosc("Cell Index").equals(cellInd))
                    quasi = true;
                }
            }
            for (int qu = 0; (!quasi && qu == 0) || (quasi && qu < GulBtsLocGrpLst.size()); qu++)
            {

                String gulEq = null;
                String gulSrn = null;
            
                String SektorFromLOC=null;
                        
                   if(quasi)
                   {
                       SektorFromLOC=GulBtsLocGrpLst.get(qu).getWartosc("Sector ID");
                       for (int i = 0; i < sectorLst.size(); i++)
                       {
                           if(SektorFromLOC.equalsIgnoreCase(sectorLst.get(i).getWartosc("Sector ID")))
                           azymutFromCell=sectorLst.get(i).getWartosc("Antenna Azimuth(0.1degree)");
                       }
                   }
                   
             
                String glocell=null;
                for (int i = 0; gulEq == null && gulSrn == null && i < dspGtrxOnNode.size(); i++)
                {
                   
                    if (cellInd.equals(dspGtrxOnNode.get(i).getWartosc("Cell Index")))
                    {
                        glocell=dspGtrxOnNode.get(i).getWartosc("Local Cell ID");
                        if(!quasi||SektorFromLOC.equals(dspGtrxOnNode.get(i).getWartosc("Sector ID")))
                        {
                            gulEq = dspGtrxOnNode.get(i).getWartosc("Sector Equipment ID");
                            for (int q = 0; gulEq != null && gulSrn == null &&(SectorEqDet!=null&& q < SectorEqDet.size()); q++)
                            {
                         
                                if (gulEq.equals(SectorEqDet.get(q).getWartosc("Sector Equipment ID")))
                                {
                                    if (!SectorEqDet.get(q).getWartosc("Subrack No.").equals(""))
                                        gulSrn = SectorEqDet.get(q).getWartosc("Subrack No.");

                                }
                            }
                        }
                    }
                }

                //wykonywane jezeli nie znaleziono gulSrn w podstawowywch danych, dodatkowe listowanie sectorEq
                if (gulSrn == null && gulEq != null)
                {
                    java.util.ArrayList<Paczka> temp;
                    String odpSS = north.make(this.NeName, "LST SECTOREQM:SECTOREQMID=" + gulEq);
                    boolean oneAntenaPort = odpSS.indexOf("Number of results = 1", odpSS.indexOf("List Sector Equipment Antenna Configuration")) > 0;
                    if (oneAntenaPort)
                        temp = (new NPack(odpSS, NPack.FORMAT_PIONOWY)).getAllPacks();
                    else
                        temp = (new NPack(odpSS, NPack.FORMAT_POZIOMY)).getAllPacks();

                    listF.dopisz(odpSS + "\r\n");
                    String srnV = null;
                    String snV = null;
                    String cnV = null;
                    for (int s = 0; s < temp.size(); s++)
                    {
                        if (!temp.get(s).getWartosc("Subrack No.").equals(""))
                        {
                            srnV = temp.get(s).getWartosc("Subrack No.");
                            gulSrn = srnV;
                        }
                        if (!temp.get(s).getWartosc("Slot No.").equals(""))
                            snV = temp.get(s).getWartosc("Slot No.");
                        if (!temp.get(s).getWartosc("Cabinet No.").equals(""))
                            cnV = temp.get(s).getWartosc("Cabinet No.");
                        if (srnV != null && snV != null && cnV != null)
                        {
                            Paczka tt = temp.get(s);
                            tt.dodaj("Sector Equipment ID", gulEq);
                            if(quasi)
                            tt.dodaj("Sector ID",SektorFromLOC);
                                    
                            SectorEqDet.add(tt);
                        }
                    }
                }
                

                String gtr = north.make(this.bscName, "LST GTRXDEV: IDTYPE=BYID,CELLID=" + cellInd + ",TRXIDTYPE=BYID");
                java.util.ArrayList<Paczka> lstGTrxDev = (new NPack(gtr, NPack.FORMAT_POZIOMY)).getAllPacks();
                listF.dopisz(gtr + "\r\n");


                String idle = north.make(this.bscName, "LST GCELLIDLEBASIC: IDTYPE=BYID,CELLID=" + cellInd);
                java.util.ArrayList<Paczka> lstGidle = (new NPack(idle, NPack.FORMAT_POZIOMY)).getAllPacks();
                listF.dopisz(idle + "\r\n");
               

                if (azymut.equals(azymutFromCell))
                {


                    for (int g = 0; g < lstGcell.size(); g++)
                    {
                      
                        if (lstGcell.get(g).getWartosc("Cell Index").equals(cellInd))
                        {
                            String band = lstGcell.get(g).getWartosc("Freq. Band");
                           
                            String pasmoTmp = "";
                            if (band.contains("1800"))
                                pasmoTmp = "1800";
                            else if (band.contains("900"))
                                pasmoTmp = "900";
                           
                            if (pasmo.equals(pasmoTmp))
                            {
                                KomorkaGsm gcell = new pem_lte.KomorkaGsm(cellInd);
                                gcell.setPasmo(band);
                                gcell.setGlocellId(glocell);
                                gcell.setKontrolerName(this.bscName);
                                gcell.setNeName(this.BtsName);
                                gcell.setLstGcell(lstGcell.get(g));
                                gcell.setLstGcell_lsc(lstGcelllcs.get(l));
                                gcell.setGulGcell(true);
                               
                                if (lstGidle != null && lstGidle.size() > 0)
                                    gcell.setLstGcellIdle(lstGidle.get(0));

                                for (int t = 0; t < lstTrx.size(); t++)
                                {
                                    String Cell_IndexFromTRX = lstTrx.get(t).getWartosc("Cell Index");
                                    if (cellInd.equals(Cell_IndexFromTRX))
                                    {
                                        gcell.setLstTrx(lstTrx.get(t));
                                    }
                                }
                                gcell.setLstGtrxDev(lstGTrxDev);
                                if(quasi)
                                {
                                    
                                     gcell.setLstBtsLocgr(GulBtsLocGrpLst.get(qu));

                                    gcell.setLocGr(true);
                                    gcell.setMainLocGr(qu==0);
                                }
                            
                                if(gulSrn==null||gulSrn.equals(""))
                                {
                                    System.out.println("Nie znaleziono SRN dla GUL CELLI="+gcell.getId()+" glocell="+gcell.getGlocellId()+" gulEq="+gulEq+" GULSektorFromLOC="+SektorFromLOC+" azymutFromCell="+azymutFromCell+" azymut from wniosek="+azymut);
                                }
                               else
                                {
                                gcell.setSrn(gulSrn);
                                System.out.println("srn="+gulSrn+" \r\n"+gcell.toString());

                                kom.add(gcell);
                                }
                  
                            
                            }
                        }
                    }
                }

                ///
            }
        }

        return kom;
    }

    private java.util.ArrayList<Komorka> getCellsOnSectorId(String sektorId, java.util.ArrayList<Paczka> sectorEqLst, NorthB north, String pasmo, java.util.ArrayList<Paczka> ucellOnRncSite) throws NBIAnsException, SQLException
    {
        java.util.ArrayList<Komorka> allKom = new java.util.ArrayList<Komorka>();
        int subcarier = 0;
        if (pasmo.equals("2100") || pasmo.equals("800"))
            subcarier = 300;
        else if (pasmo.equals("1800"))
            subcarier = 600;
        else if (pasmo.equals("2600"))
            subcarier = 1200;

        for (int e = 0; e < sectorEqLst.size(); e++)
        {
            String secIdFromEq = sectorEqLst.get(e).getWartosc("Sector ID");
            if (secIdFromEq.equals(sektorId))
            {
                String secEqId = sectorEqLst.get(e).getWartosc("Sector Equipment ID");
                allKom.addAll(getECellsOnSectorId(sektorId, secEqId, north, subcarier));
                java.util.ArrayList<Komorka> tmpLi = getUCellsOnSectorId(sektorId, secEqId, north, ucellOnRncSite);
                if (tmpLi != null)
                    allKom.addAll(tmpLi);
                //allKom.addAll(getGCellsOnSectorId(sektorId,secEqId,north));
            }
        }

        return allKom;
    }

    private java.util.ArrayList<Paczka> gestDspRruLst(String sektorId,java.util.ArrayList<Paczka> sectorEqLst,java.util.ArrayList<Paczka> lstRRU,NorthB north,String pasmo) throws NBIAnsException
    {
        java.util.ArrayList<Paczka> rru=new java.util.ArrayList<Paczka>();
        java.util.ArrayList<String> srn=new java.util.ArrayList<String>();


                    for(int s=0;s<sectorEqLst.size();s++)
        {

                    String srnV=null;
                    String snV=null;
                    String cnV=null;
                        if(!sectorEqLst.get(s).getWartosc("Subrack No.").equals(""))
                            srnV=sectorEqLst.get(s).getWartosc("Subrack No.");
                        if(!sectorEqLst.get(s).getWartosc("Slot No.").equals(""))
                            snV=sectorEqLst.get(s).getWartosc("Slot No.");
                        if(!sectorEqLst.get(s).getWartosc("Cabinet No.").equals(""))
                            cnV=sectorEqLst.get(s).getWartosc("Cabinet No.");

            //System.out.println("$$$$$$$srnV="+srnV+" snV="+snV+" cnV="+cnV);
                    if(!srn.contains(srnV))
            {
                srn.add(srnV);
                        String odp=north.make(this.NeName, "DSP RRU:SN="+snV+",SRN="+srnV+",CN="+cnV);
                        listF.dopisz(odp+"\r\n");
                        java.util.ArrayList<Paczka> listaTmp=null;

                        if(odp.contains("AARU does not support this function"))
                {
                            odp=north.make(this.NeName, "DSP AARU:SN="+snV+",SRN="+srnV+",CN="+cnV);
                            listF.dopisz(odp+"\r\n");
                }
                            listaTmp=(new NPack(odp,NPack.FORMAT_PIONOWY)).getAllPacks();

                        String workMode=null;
                        for(int g=0;g<lstRRU.size();g++)
                {
                            if(lstRRU.get(g).getWartosc("Subrack No.").equals(srnV))
                            workMode=lstRRU.get(g).getWartosc("RF Unit Working Mode");
                }
                        if(workMode!=null)
                {
                            for(int g=0;g<listaTmp.size();g++)
                    {
                        listaTmp.get(g).dodaj("RF Unit Working Mode", workMode);
                    }
                }

                rru.addAll(listaTmp);



            }
        }

        return rru;

    }

    private java.util.ArrayList<Paczka> findStandAloneRruFitsToBandAzymuth(String pasmoFromCell, String azymutFromCell, java.util.ArrayList<Paczka> lstRRU, java.util.ArrayList<Paczka> trxy, java.util.ArrayList<Paczka> gcell, java.util.ArrayList<Paczka> gcell_lcs,java.util.ArrayList<Paczka>dspGtrxOnNode,java.util.ArrayList<Paczka>SectorEqDet,java.util.ArrayList<Paczka> sectorList)
    {
        java.util.ArrayList<Paczka> rruSubList=new java.util.ArrayList<Paczka>();
        //  if(!azymutFromCell.equals("0"))
        //    {    azymutFromCell=azymutFromCell+"0";
        
        java.util.AbstractList<String> usedSubracks=new java.util.ArrayList<String>();
        for(int l=0;l<gcell_lcs.size();l++)
        {
                String cellInd=gcell_lcs.get(l).getWartosc("Cell Index");

               String azymut=gcell_lcs.get(l).getWartosc("Antenna Azimuth Angle");
               if(!azymut.equals("0"))
                    azymut=azymut+"0";
               if(azymut.equals(azymutFromCell))
            {
                    for(int g=0;g<gcell.size();g++)
                {

                        if(gcell.get(g).getWartosc("Cell Index").equals(cellInd))
                    {
                            String band=gcell.get(g).getWartosc("Freq. Band");
                             String pasmo="";
                            if(band.contains("1800"))
                                pasmo="1800";
                            else if(band.contains("900"))
                                pasmo="900";

                            if(pasmo.equals(pasmoFromCell))
                        {
                                for(Paczka trx:trxy)
                            {
                                    String trxCellIndex=trx.getWartosc("Cell Index");
                                    if(trxCellIndex.equals(cellInd))
                                {
                                        if(!usedSubracks.contains(trx.getWartosc("Subrack No.")))
                                        usedSubracks.add(trx.getWartosc("Subrack No."));
                                }
                            }
                        }
                    }
                }
            }
            //  }
        }
         
        
        
       
        
           //stand allone gul rru 
        if (dspGtrxOnNode != null && dspGtrxOnNode.size() > 0)
        {
            for (int i = 0; i < dspGtrxOnNode.size(); i++)
            {
               
                
                String sectorId = dspGtrxOnNode.get(i).getWartosc("Sector ID");
                String sectorEq = dspGtrxOnNode.get(i).getWartosc("Sector Equipment ID");
                String cellInd=dspGtrxOnNode.get(i).getWartosc("Cell Index");
                String pasmo=null;
                if(sectorId!=null&&!sectorId.equals("")&&sectorEq!=null&&!sectorEq.equals(""))
                {
                for (int g = 0;pasmo==null&& g < gcell.size(); g++)//sprawdzenie pasma
                {

                    if (gcell.get(g).getWartosc("Cell Index").equals(cellInd))
                    {
                        String band = gcell.get(g).getWartosc("Freq. Band");
                      //  String pasmo = "";
                        if (band.contains("1800"))
                            pasmo = "1800";
                        else if (band.contains("900"))
                            pasmo = "900";

                    }
                }
                String azymutFromSectorLst=null;
                for (int j = 0;azymutFromSectorLst==null&& j <sectorList.size(); j++)
                {
                    if(sectorList.get(j).getWartosc("Sector ID").equals(sectorId))
                    {
                        azymutFromSectorLst=sectorList.get(j).getWartosc("Antenna Azimuth(0.1degree)");

                    }
                }
                
                
                
                if(pasmo!=null&&pasmo.equalsIgnoreCase(pasmoFromCell)&&azymutFromSectorLst!=null&&azymutFromSectorLst.equals(azymutFromCell))//podmienic warunek aby sprawdzal azymut i pasmo
                {
                for (int q = 0; q < SectorEqDet.size(); q++)
                {
                   
                    if (sectorEq.equals(SectorEqDet.get(q).getWartosc("Sector Equipment ID")))
                    {
                        if (!SectorEqDet.get(q).getWartosc("Subrack No.").equals(""))
                        {
                          String  gulSrn = SectorEqDet.get(q).getWartosc("Subrack No.");
                            if (!usedSubracks.contains(gulSrn))
                            {
                                usedSubracks.add(gulSrn);
                            }
                        }

                    }
                }
                }
            }
            }
        }

         for (Paczka RRU : lstRRU)
        {
            if (usedSubracks.contains(RRU.getWartosc("Subrack No.")))
            {
                rruSubList.add(RRU);
            }
        }

        
        return rruSubList;
    }

    private java.util.ArrayList<Paczka> getSectEqLst(String sektorId,java.util.ArrayList<Paczka> sectorEqLst,NorthB north,String pasmo) throws NBIAnsException
    {
        java.util.ArrayList<Paczka> secDet=new java.util.ArrayList<Paczka>();
        for(int e=0;e<sectorEqLst.size();e++)
        {
            String secIdFromEq=sectorEqLst.get(e).getWartosc("Sector ID");
            if(sektorId==null||secIdFromEq.equals(sektorId))
            {
                String odpSS = north.make(this.NeName, "LST SECTOREQM:SECTOREQMID=" + sectorEqLst.get(e).getWartosc("Sector Equipment ID"));
                java.util.ArrayList<Paczka> temp;
                boolean oneAntenaPort = odpSS.indexOf("Number of results = 1", odpSS.indexOf("List Sector Equipment Antenna Configuration")) > 0;
                if (oneAntenaPort)
                    temp = (new NPack(odpSS, NPack.FORMAT_PIONOWY)).getAllPacks();
                else
                    temp = (new NPack(odpSS, NPack.FORMAT_POZIOMY)).getAllPacks();

                listF.dopisz(odpSS + "\r\n");
                String srnV = null;
                String snV = null;
                String cnV = null;
                for (int s = 0; s < temp.size(); s++)
                {
                    if (!temp.get(s).getWartosc("Subrack No.").equals(""))
                        srnV = temp.get(s).getWartosc("Subrack No.");
                    if (!temp.get(s).getWartosc("Slot No.").equals(""))
                        snV = temp.get(s).getWartosc("Slot No.");
                    if (!temp.get(s).getWartosc("Cabinet No.").equals(""))
                        cnV = temp.get(s).getWartosc("Cabinet No.");
                    if (srnV != null && snV != null && cnV != null)
                    {
                        Paczka tt = temp.get(s);
                        tt.dodaj("Sector Equipment ID", sectorEqLst.get(e).getWartosc("Sector Equipment ID"));
                       // if(sektorId!=null)
                        tt.dodaj("Sector ID", secIdFromEq);
                        secDet.add(tt);
                    }
                }
            }
        }
        return secDet;

    }

    private java.util.ArrayList<Komorka> getECellsOnSectorId(String sektorId, String sektorEq, NorthB north, int subcarierNumber) throws NBIAnsException, SQLException
    {
        java.util.ArrayList<Komorka> kom = new java.util.ArrayList<Komorka>();
        String lstEcellPerEQ = north.make(this.NeName, "LST EUCELLSECTOREQM:SECTOREQMID=" + sektorEq);
        listF.dopisz(lstEcellPerEQ + "\r\n");
        java.util.ArrayList<Paczka> ecellS = new java.util.ArrayList<Paczka>();
        if (lstEcellPerEQ.contains("Number of results = 1)"))//listowanie pionowe
        {
            ecellS = (new NPack(lstEcellPerEQ, NPack.FORMAT_PIONOWY)).getAllPacks();
        }
        else if (lstEcellPerEQ.contains("Number of results ="))//listowanie poziome
        {
            ecellS = (new NPack(lstEcellPerEQ, NPack.FORMAT_POZIOMY)).getAllPacks();
        }
        for (int uc = 0; uc < ecellS.size(); uc++)
        {
            String locellId = ecellS.get(uc).getWartosc("Local cell ID");
            String ecellDetails = north.make(this.NeName, "LST CELL:LOCALCELLID=" + locellId);
            listF.dopisz(ecellDetails + "\r\n");
            java.util.ArrayList<Paczka> ecellSDet = (new NPack(ecellDetails, NPack.FORMAT_PIONOWY)).getAllPacks();

            String dSPecellDetails = north.make(this.NeName, "DSP CELL:LOCALCELLID=" + locellId);
            java.util.ArrayList<Paczka> dspCell = (new NPack(dSPecellDetails, NPack.FORMAT_PIONOWY)).getAllPacks();
            listF.dopisz(dSPecellDetails + "\r\n");

            String cellOpDet = north.make(this.NeName, "LST CELLOP:LOCALCELLID=" + locellId);
            java.util.ArrayList<Paczka> cellOp = (new NPack(cellOpDet, NPack.FORMAT_PIONOWY)).getAllPacks();
            listF.dopisz(cellOpDet + "\r\n");

            String cellPds = north.make(this.NeName, "LST PDSCHCFG:LOCALCELLID=" + locellId);
            java.util.ArrayList<Paczka> PDSCHCFG = (new NPack(cellPds, NPack.FORMAT_PIONOWY)).getAllPacks();
            listF.dopisz(cellPds + "\r\n");

            String cellDLPds = north.make(this.NeName, "LST CELLDLPCPDSCHPA:LOCALCELLID=" + locellId);
            java.util.ArrayList<Paczka> CELLDLPCPDSCHPA = (new NPack(cellDLPds, NPack.FORMAT_PIONOWY)).getAllPacks();
            listF.dopisz(cellDLPds + "\r\n");
            //ADD ULTECELL:LTECELLINDEX=22, LTECELLNAME="4166552L_SLU0004B_O_PONIATOWSKI27", MCC="260", MNC="06", TAC=49, CNOPGRPINDEX=0, CELLPHYID=99, LTEBAND=3, LTEARFCN=1449, SUPPPSHOFLAG=Support, EUTRANCELLID=4263720, BLACKFLAG=False, U2LRIMCNOPERATORRTINDEX=255;
            for (int e = 0; e < ecellSDet.size(); e++)
            {
                try
                {
                    // subcarierNumber=Integer.parseInt(ecellSDet.get(e).getWartosc("Downlink bandwidth").replace("M", ""))*5*12;
                    subcarierNumber = ((int) Double.parseDouble(ecellSDet.get(e).getWartosc("Downlink bandwidth").replace("M", ""))) * 5 * 12;
                }
                catch (Exception exc)
                {
                    exc.printStackTrace();
                }
                KomorkaLte ecell = new pem_lte.KomorkaLte(ecellSDet.get(e).getWartosc("Cell ID"), subcarierNumber);
                //findRelations;
                //.getLinia(new String[]{"ADD ULTECELL:","["+"]"});
                ecell.setPasmo(ecellSDet.get(e).getWartosc("Frequency band"));
                ecell.setNeName(NeName);
                ecell.setKontrolerName(rncName);
                ecell.setLocallCellId(locellId);
                ecell.setLstCell(ecellSDet.get(e));
                ecell.setSeqEqId(sektorEq);
                String findRel = "select  'LTE2LTE' as typRelacji, e.ne_name,p.locell_id as locell_id_wychodzacy,rel.cell_name_wychodzacy,rel.enodebId_stacji_PEM,cellId_komorki_PEM,rel.ext_ecell_name as cellName_komorki_PEM,rel.inter_intra from (select    source_enodeb_ident as EnodebIdent_wychodzacy,source_ecell_name as cell_name_wychodzacy,source_ecell_ident ,ext_enode_Id as enodebId_stacji_PEM,ext_ecell_Id as cellId_komorki_pem , ext_ecell_name,typ as inter_intra from  raport_konfiguracja_aktualna.xml_eu_ncell_ident  where NoHoFlag=0 and  ext_ecell_ident=(select ecell_ident from raport_konfiguracja_aktualna.xml_ecell_ident  where enodeb_ident=(select enodeb_ident from raport_konfiguracja_aktualna.xml_enodeb_ident  where ne_name='" + this.NeName + "') and ecell_id='" + ecellSDet.get(e).getWartosc("Cell ID") + "'))as rel left join raport_konfiguracja_aktualna.xml_enodeb_ident e on(e.enodeb_ident=rel.EnodebIdent_wychodzacy) left join raport_konfiguracja_aktualna.xml_ecell_param p on(p.ecell_ident=rel.source_ecell_ident);";
                String relacje2G = "select l.*,r.Rnc_Bsc_Name from  (select   e.EXTLTECELLID,e.bsc_index from raport_konfiguracja_aktualna.GEXTLTECELL_perBsc e where e.EXTLTECELLNAME like '%" + ecell.getName() + "%') as externale left join raport_konfiguracja_aktualna.GLTENCELL_perBSC l on(externale.EXTLTECELLID=l.NBRLTENCELLID and externale.bsc_index=l.bsc_index) left join oncall.konfiguracja_aktualna_rnc_bsc r on(l.bsc_index=r.Rnc_Bsc_Index) where r.Rnc_Bsc_Name is not null";
                if (!this.onlyCheck && !changePower)
                {
                    OdpowiedzSQL rel2G = testStatement.wykonajZapytanie(relacje2G);
                    listF.dopisz(rel2G.toString() + "\r\n");
                    ecell.setRelacje2GDoKomorki(rel2G);
                    NewFile mml = new NewFile("/usr/samba/utran/PP/TMP/Printouts/" + this.rncName + ".mml");
                    //String findRel="select  'LTE2LTE' as typRelacji, e.ne_name,p.locell_id as locell_id_wychodzacy,rel.cell_name_wychodzacy,rel.enodebId_stacji_PEM,cellId_komorki_PEM,rel.ext_ecell_name as cellName_komorki_PEM,rel.inter_intra from (select    source_enodeb_ident as EnodebIdent_wychodzacy,source_ecell_name as cell_name_wychodzacy,source_ecell_ident ,ext_enode_Id as enodebId_stacji_PEM,ext_ecell_Id as cellId_komorki_pem , ext_ecell_name,typ as inter_intra from  raport_konfiguracja_aktualna.xml_eu_ncell_ident  where NoHoFlag=0 and  ext_ecell_ident=(select ecell_ident from raport_konfiguracja_aktualna.xml_ecell_ident  where enodeb_ident=(select enodeb_ident from raport_konfiguracja_aktualna.xml_enodeb_ident  where ne_name='"+this.NeName+"') and ecell_id='"+ecellSDet.get(e).getWartosc("Cell ID")+"'))as rel left join raport_konfiguracja_aktualna.xml_enodeb_ident e on(e.enodeb_ident=rel.EnodebIdent_wychodzacy) left join raport_konfiguracja_aktualna.xml_ecell_param p on(p.ecell_ident=rel.source_ecell_ident);";
                    //ResultSet res=testStatement.executeQuery(findRel);
                    //OdpowiedzSQL relacje=Baza.createAnswer(res);
                    OdpowiedzSQL relacje = testStatement.wykonajZapytanie(findRel);
                    // System.out.println(relacje); 
                    listF.dopisz(relacje.toString() + "\r\n");
                    ecell.setRelationToCell(relacje);
                    //WAR2074
                    String externaleLteNaRnc = mml.getFirstLine(new String[]
                    {
                        "ADD ULTECELL:", ecell.getName()
                    }, new String[]
                    {
                    });
                    if (externaleLteNaRnc.length() > 0 && externaleLteNaRnc.contains("LTECELLINDEX"))
                    {
                        //ADD ULTECELL:LTECELLINDEX=22, LTECELLNAME="4166552L_SLU0004B_O_PONIATOWSKI27", MCC="260", MNC="06", TAC=49, CNOPGRPINDEX=0, CELLPHYID=99, LTEBAND=3, LTEARFCN=1449, SUPPPSHOFLAG=Support, EUTRANCELLID=4263720, BLACKFLAG=False, U2LRIMCNOPERATORRTINDEX=255;
                        listF.dopisz(externaleLteNaRnc.toString() + "\r\n");
                        String lteIndex = externaleLteNaRnc.substring(externaleLteNaRnc.indexOf("LTECELLINDEX=") + "LTECELLINDEX=".length(), externaleLteNaRnc.indexOf(",", externaleLteNaRnc.indexOf("LTECELLINDEX=")));
                        ecell.setLteCellIndex(lteIndex);
                    }
                }

                if (dspCell.size() > 0)
                    ecell.setDspCell(dspCell.get(0));

                if (cellOp.size() > 0)
                    ecell.setCellOp(cellOp.get(0));

                if (PDSCHCFG.size() > 0)
                    ecell.setCellPdschcfg(PDSCHCFG.get(0));

                if (CELLDLPCPDSCHPA.size() > 0)
                    ecell.setCellDlPcpfschpa(CELLDLPCPDSCHPA.get(0));
                kom.add(ecell);
            }
        }


        return kom;
    }

    private java.util.ArrayList<String> getRelacjeUmt2Umts(String cellId)
    {
        NewFile mml = new NewFile("/usr/samba/utran/PP/TMP/Printouts/" + this.rncName + ".mml");
        java.util.ArrayList<String> relacje = new java.util.ArrayList<String>();

        String[] relacjeIntRACell = mml.getLinia(new String[]
        {
            "ADD UINTRAFREQNCELL:", "NCELLID=" + cellId + ","
        });
        //  System.out.println(" CELLID="+cellId+" INTRA SIZE="+relacjeIntRACell.length);
        for (int r = 0; r < relacjeIntRACell.length; r++)
        {
            if (!relacje.contains(relacjeIntRACell[r]))
            {
                relacje.add(relacjeIntRACell[r]);
            }
        }

        String[] relacjeIntERCell = mml.getLinia(new String[]
        {
            "ADD UINTERFREQNCELL:", "NCELLID=" + cellId + ","
        });
        //System.out.println(" CELLID="+cellId+" INTER SIZE="+relacjeIntRACell.length);
        for (int r = 0; r < relacjeIntERCell.length; r++)
        {
            if (!relacje.contains(relacjeIntERCell[r]))
            {
                relacje.add(relacjeIntERCell[r]);
            }
            // RMV UINTERFREQNCELL:RNCID=43,CELLID=123,NCELLRNCID=43,NCELLID=456;
        }
        return relacje;
    }

    private OdpowiedzSQL getRelacjeLte2Umts(String ExterUcellId, String exetrRncId)
    {
        String findRel = "select ei.ne_name,ee.source_ecell_name,ee.source_elocell_id,ee.Mcc,ee.Mnc ,ee.ext_rnc_Id,ee.ext_ucell_id from raport_konfiguracja_aktualna.xml_u_ncell_ident ee left join raport_konfiguracja_aktualna.xml_enodeb_ident ei on(ee.source_enodeb_ident=ei.enodeb_ident) left join raport_konfiguracja_aktualna.xml_ecell_param p on(ee.source_ecell_ident=p.ecell_ident) where ee.ext_rnc_Id=" + exetrRncId + " and ee.ext_ucell_id=" + ExterUcellId;

        //ResultSet res=testStatement.executeQuery(findRel);
        //OdpowiedzSQL relacje=Baza.createAnswer(res);
        OdpowiedzSQL relacje = testStatement.wykonajZapytanie(findRel);
        // System.out.println(relacje);
        listF.dopisz(relacje.toString() + "\r\n");


        return relacje;
    }

    private java.util.ArrayList<Komorka> getUCellsOnSectorId(String sektorId, String sektorEq, NorthB north, java.util.ArrayList<Paczka> ucellOnRncSite) throws NBIAnsException
    {
        java.util.ArrayList<Komorka> kom = new java.util.ArrayList<Komorka>();
        String lstUcellPerEQ = north.make(this.NeName, "LST ULOCELL:MODE=SECTOREQM,SECTOREQMID=" + sektorEq);
        listF.dopisz(lstUcellPerEQ + "\r\n");
        if (!lstUcellPerEQ.contains("No matching result is found"))
        {
            kom = new java.util.ArrayList<Komorka>();
            // System.out.println(lstUcellPerEQ);
            java.util.ArrayList<Paczka> ucellS = new java.util.ArrayList<Paczka>();
            if (lstUcellPerEQ.contains("Number of results = 1)"))//listowanie pionowe
            {
                ucellS = (new NPack(lstUcellPerEQ, NPack.FORMAT_PIONOWY)).getAllPacks();
            }
            else//listowanie poziome
            {
                ucellS = (new NPack(lstUcellPerEQ, NPack.FORMAT_POZIOMY)).getAllPacks();
            }
            for (int uc = 0; uc < ucellS.size(); uc++)
            {
                String locellId = ucellS.get(uc).getWartosc("Local Cell ID");
                String ucellDetails = north.make(this.NeName, "LST ULOCELL:MODE=LOCALCELL,ULOCELLID=" + locellId);
                listF.dopisz(ucellDetails + "\r\n");
                java.util.ArrayList<Paczka> ucellSDet = (new NPack(ucellDetails, NPack.FORMAT_PIONOWY)).getAllPacks();

//                for()
                String cellId = null;
                for (int wq = 0; wq < ucellOnRncSite.size() && cellId == null; wq++)
                {
                    if (ucellOnRncSite.get(wq).getWartosc("Local Cell ID").equals(locellId))
                        cellId = ucellOnRncSite.get(wq).getWartosc("Cell ID");
                }

                if (ucellSDet.size() > 0)
                {
                    //String cellId=ucellSDet.get(0).getWartosc("Cell ID");
                    KomorkaUmts ucell = new pem_lte.KomorkaUmts(cellId);
                    ucell.setLocellId(locellId);
                    ucell.setLstCell(ucellSDet.get(0));
                    ucell.setPasmo(ucellSDet.get(0).getWartosc("UL Frequency Channel Number"));
                    if (!this.onlyCheck && !changePower)
                    {
                        ucell.setRelationToCell(getRelacjeUmt2Umts(cellId));
                        ucell.setRelacjeLte2Umts(getRelacjeLte2Umts(cellId, this.rncId));
                    }
                    String lstAcc = north.make(this.rncName, "LST UCELLACCESSSTRICT: CELLID=" + cellId + ";");
                    java.util.ArrayList<Paczka> ucellAcc = (new NPack(lstAcc, NPack.FORMAT_PIONOWY)).getAllPacks();
                    listF.dopisz(lstAcc + "\r\n");

                    String lstUcell = north.make(this.rncName, "LST UCELL:LSTTYPE=BYCELLID,CELLID=" + cellId);
                    listF.dopisz(lstUcell + "\r\n");
                    java.util.ArrayList<Paczka> cellOnRncSide = (new NPack(lstUcell, NPack.FORMAT_POZIOMY)).getAllPacks();
                    if (cellOnRncSide.size() > 0)
                        ucell.setLstUcellOnRnc(cellOnRncSide.get(0));

                    if (ucellAcc.size() > 0)
                        ucell.setLstAccesStrict(ucellAcc.get(0));

                    ucell.setSeqEqId(sektorEq);
                    ucell.setNeName(NeName);
                    ucell.setKontrolerName(rncName);
                    kom.add(ucell);
                }
            }
        }
        return kom;
    }

    private String getPasmo(String sektorId, java.util.ArrayList<Paczka> sectorEqLst, NorthB north) throws NBIAnsException
    {
        String pasmo = null;
        for (int e = 0; e < sectorEqLst.size() && pasmo == null; e++)
        {
            String secIdFromEq = sectorEqLst.get(e).getWartosc("Sector ID");
            if (secIdFromEq.equals(sektorId))
            {
                String secEqId = sectorEqLst.get(e).getWartosc("Sector Equipment ID");
                String lstUcellPerEQ = north.make(this.NeName, "LST ULOCELL:MODE=SECTOREQM,SECTOREQMID=" + secEqId);
                listF.dopisz(lstUcellPerEQ + "\r\n");
                if (!lstUcellPerEQ.contains("No matching result is found") && !lstUcellPerEQ.contains("Invalid command,it is inexecutable"))
                {
                    java.util.ArrayList<Paczka> ucellS = new java.util.ArrayList<Paczka>();
                    if (lstUcellPerEQ.contains("Number of results = 1)"))//listowanie pionowe
                    {
                        ucellS = (new NPack(lstUcellPerEQ, NPack.FORMAT_PIONOWY)).getAllPacks();
                    }
                    else//listowanie poziome
                    {
                        ucellS = (new NPack(lstUcellPerEQ, NPack.FORMAT_POZIOMY)).getAllPacks();
                    }
                    for (int uc = 0; uc < ucellS.size() && pasmo == null; uc++)
                    {
                        String locellId = ucellS.get(uc).getWartosc("Local Cell ID");

                        String ucellDetails = north.make(this.NeName, "LST ULOCELL:MODE=LOCALCELL,ULOCELLID=" + locellId);
                        listF.dopisz(ucellDetails + "\r\n");
                        java.util.ArrayList<Paczka> ucellSDet = (new NPack(ucellDetails, NPack.FORMAT_PIONOWY)).getAllPacks();
                        for (int d = 0; d < ucellSDet.size() && pasmo == null; d++)
                        {
                            String freq = ucellSDet.get(d).getWartosc("UL Frequency Channel Number");
                            if (freq != null && !freq.trim().equals(""))
                            {
                                int freqI = Integer.parseInt(freq);
                                if (freqI >= 9837 && freqI <= 9886)
                                {
                                    pasmo = "2100";
                                }
                                else if (freqI >= 2713 && freqI <= 2938)
                                {
                                    pasmo = "900";
                                }
                            }
                        }
                    }
                }
                else///BRAK UCELL przejscie do LISTOWANIA ECELL
                {
                    String lstEcellPerEQ = north.make(this.NeName, "LST EUCELLSECTOREQM:SECTOREQMID=" + secEqId);
                    listF.dopisz(lstEcellPerEQ + "\r\n");
                    java.util.ArrayList<Paczka> ecellS = new java.util.ArrayList<>();
                    if (lstEcellPerEQ.contains("Number of results = 1)"))//listowanie pionowe
                    {
                        ecellS = (new NPack(lstEcellPerEQ, NPack.FORMAT_PIONOWY)).getAllPacks();
                    }
                    else if (lstEcellPerEQ.contains("Number of results ="))//listowanie poziome
                    {
                        ecellS = (new NPack(lstEcellPerEQ, NPack.FORMAT_POZIOMY)).getAllPacks();
                    }

                    for (int uc = 0; uc < ecellS.size() && pasmo == null; uc++)
                    {
                        String locellId = ecellS.get(uc).getWartosc("Local cell ID");

                        String ecellDetails = north.make(this.NeName, "LST CELL:LOCALCELLID=" + locellId);
                        listF.dopisz(ecellDetails + "\r\n");
                        java.util.ArrayList<Paczka> ecellSDet = (new NPack(ecellDetails, NPack.FORMAT_PIONOWY)).getAllPacks();
                        for (int d = 0; d < ecellSDet.size() && pasmo == null; d++)
                        {
                            String freq = ecellSDet.get(d).getWartosc("Frequency band");
                            if (freq != null && !freq.trim().equals(""))
                            {

                                if (freq.equals("3"))
                                {
                                    pasmo = "1800";//
                                }
                                else if (freq.equals("1"))
                                {
                                    pasmo = "2100";//
                                }
                                else if (freq.equals("20"))
                                {
                                    pasmo = "800";//
                                }
                                else if (freq.equals("7"))
                                {
                                    pasmo = "2600";//
                                }
                            }
                        }
                    }
                }
            }
        }
        return pasmo;
    }

    public ArrayList<sektor> getSektoryNaStacji()
    {
        return sektoryNaStacji;
    }
}