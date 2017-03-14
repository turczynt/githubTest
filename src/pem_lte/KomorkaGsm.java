/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.util.ArrayList;
import nbipackage.Paczka;

/**
 *
 * @author turczyt
 */
public class KomorkaGsm extends Komorka
{

    final Integer priorytet = Komorka.KOMORKA_GSM;
    String id;
    String glocellId;
    boolean gsmStandAllone;
    String NeName;
    String kontrolerName;
    String cellName;
    double actualPower;
    boolean actualOnAir;
    boolean locGr;
    boolean mainLocGr;
    boolean active;
    boolean unBlocked;
    boolean unBarred;
    Paczka lstGcell;
    Paczka lstGcell_lsc;
    Paczka lstGcellIdle;
    java.util.ArrayList<Paczka> lstTrx;
    java.util.ArrayList<Paczka> lstGtrxDev;
    java.util.ArrayList<Paczka> lstBtsLocgr;
    java.util.ArrayList<Paczka> lstBindLocGr;
    
    boolean gulGcell;
    String srn;

    public String getGlocellId()
    {
        return glocellId;
    }

    public void setGlocellId(String glocellId)
    {
        this.glocellId = glocellId;
    }

    @Override
    public Integer getPriorytet()
    {
        return this.priorytet;
    }

    public boolean isGsmStandAllone()
    {
        return gsmStandAllone;
    }

    public void setGsmStandAllone(boolean gsmStandAllone)
    {
        this.gsmStandAllone = gsmStandAllone;
    }

    String getConfigCheck_START()
    {
        return "";

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public KomorkaGsm(String id)
    {
        this.id = id;
        this.lstTrx = new java.util.ArrayList<Paczka>();
        this.lstGtrxDev = new java.util.ArrayList<Paczka>();
        this.lstBindLocGr = new java.util.ArrayList<Paczka>();
        this.lstBtsLocgr = new java.util.ArrayList<Paczka>();
        this.locGr = false;
        this.mainLocGr = false;
        this.gsmStandAllone = false;
        this.gulGcell=false;
    }

    public void setNeName(String NeName)
    {
        this.NeName = NeName;
    }

    public void setKontrolerName(String kontrolerName)
    {
        this.kontrolerName = kontrolerName;
    }

    public double getActualPower()
    {
        return this.actualPower;
    }

    @Override
    boolean isActualOnAir()
    {
        return this.actualOnAir;
    }

    public void setActualPower(double actualPower)
    {
        this.actualPower = actualPower;
    }

    public void setActualOnAir(boolean actualOnAir)
    {
        this.actualOnAir = actualOnAir;
    }

    public String getSrn()
    {
        return srn;
    }

    public void setSrn(String srn)
    {
         if(srn!=null&&srn.matches("[0-9]+"))
         {
             System.out.println("PRZEDCellI="+this.cellName+" zmiana srn="+this.srn+" na "+srn+" getPost="+this.getPosition());
             this.srn = srn;
         }
         System.out.println("POCellI="+this.cellName+" zmiana srn="+this.srn+" na "+srn+" getPost="+this.getPosition());
    }

    public String getPosition()
    {
        return this.getSrn();
    }

    public double getMaximumCellPower()
    {
        return Double.MAX_VALUE;
    }

    public ArrayList<Paczka> getLstBtsLocgr()
    {
        return lstBtsLocgr;
    }

    public void setLstBtsLocgr(ArrayList<Paczka> lstBtsLocgr)
    {
        this.lstBtsLocgr.addAll(lstBtsLocgr);
        //  this.srn=lstBtsLocgr.get(0).getWartosc("Subrack No.");
        this.locGr = true;
    }

    public void setLstBtsLocgr(Paczka lstBtsLocgr)
    {
        this.lstBtsLocgr.add(lstBtsLocgr);
        this.locGr = true;

    }

    public ArrayList<Paczka> getLstBindLocGr()
    {
        return lstBindLocGr;
    }

    public void setLstBindLocGr(ArrayList<Paczka> lstBindLocGr)
    {
        this.lstBindLocGr.addAll(lstBindLocGr);
       if(this.lstBindLocGr.get(0).getWartosc("Subrack No.")!=null&&this.lstBindLocGr.get(0).getWartosc("Subrack No.").matches("[0-9]+"))
        this.srn = this.lstBindLocGr.get(0).getWartosc("Subrack No.");
        this.locGr = true;
    }

    public void setLstBindLocGr(Paczka lstBindLocGr)
    {
        this.lstBindLocGr.add(lstBindLocGr);
        if(this.lstBindLocGr.get(0).getWartosc("Subrack No.")!=null&&this.lstBindLocGr.get(0).getWartosc("Subrack No.").matches("[0-9]+"))
        this.srn = this.lstBindLocGr.get(0).getWartosc("Subrack No.");
        this.locGr = true;
    }

    public boolean isUnBlocked()
    {
        return unBlocked;
    }

    public void setUnBlocked(boolean unBlocked)
    {
        this.unBlocked = unBlocked;
    }

    public Paczka getLstGcell()
    {
        return lstGcell;
    }

    public void setLstGcell(Paczka lstGcell)
    {
        this.lstGcell = lstGcell;
        this.cellName = this.lstGcell.getWartosc("Cell Name");
        this.unBlocked = this.lstGcell.getWartosc("Administrative State").equals("Unlock");
        this.active = this.lstGcell.getWartosc("active status").equals("ACTIVATED");


    }

    public String getName()
    {
        return this.cellName;
    }

    public ArrayList<Paczka> getLstTrx()
    {
        return lstTrx;
    }

    public void setLstTrx(ArrayList<Paczka> lstTrx)
    {
        this.lstTrx.addAll(lstTrx);
       if(this.lstTrx.get(0).getWartosc("Subrack No.")!=null&&this.lstTrx.get(0).getWartosc("Subrack No.").matches("[0-9]+"))
        this.srn = this.lstTrx.get(0).getWartosc("Subrack No.");


    }

    public void setLstTrx(Paczka lstTrx)
    {
        this.lstTrx.add(lstTrx);
        if(this.lstTrx.get(0).getWartosc("Subrack No.")!=null&&this.lstTrx.get(0).getWartosc("Subrack No.").matches("[0-9]+"))
            this.srn = this.lstTrx.get(0).getWartosc("Subrack No.");
    }

    public ArrayList<Paczka> getLstGtrxDev()
    {
        return this.lstGtrxDev;
    }

    public void setLstGtrxDev(ArrayList<Paczka> lstGtrxDev)
    {
        this.lstGtrxDev.addAll(lstGtrxDev);



    }

    public void setLstGtrxDev(Paczka lstGtrxDev)
    {
        this.lstGtrxDev.add(lstGtrxDev);

    }

    public Paczka getLstGcell_lsc()
    {
        return lstGcell_lsc;
    }

    public void setLstGcell_lsc(Paczka lstGcell_lsc)
    {
        this.lstGcell_lsc = lstGcell_lsc;
    }

    public String getCellName()
    {
        return cellName;
    }

    public void setCellName(String cellName)
    {
        this.cellName = cellName;
    }

    public boolean isActive()
    {
        return active;
    }

    public void seActive(boolean active)
    {
        this.active = active;
    }

    @Override
    String getActDeaCell_START()
    {
        String odp = "";
        /* if(this.powerToSet>0.0)
         {
            
         }
         else
         {*/
        if (!this.locGr || this.isMainLocGr())
        {
            odp = odp + "DEA GCELL: IDTYPE=BYID, CELLID=" + this.id + ";{" + this.kontrolerName + "}\r\n";

            //odp=odp+"DSP GCELLSTAT:IDTYPE=BYID,CELLIDLST="+this.id+";{"+this.kontrolerName+"}\r\n";
            //odp=odp+"DSP GCELLSTAT:IDTYPE=BYID,CELLIDLST="+this.id+";{"+this.kontrolerName+"}\r\n";
            //odp=odp+"DSP GCELLSTAT:IDTYPE=BYID,CELLIDLST="+this.id+";{"+this.kontrolerName+"}\r\n";
            //odp=odp+"DSP GCELLSTAT:IDTYPE=BYID,CELLIDLST="+this.id+";{"+this.kontrolerName+"}\r\n";
            //odp=odp+"DSP GCELLSTAT:IDTYPE=BYID,CELLIDLST="+this.id+";{"+this.kontrolerName+"}\r\n";
        }

        /* }*/
        return odp;
    }

    @Override
    String getBlkUblkMML_START()
    {
        String odp = "";
        if (this.powerToSet > 0.0)
        {
            if (!this.locGr || this.isMainLocGr())
            {
                odp = odp + "ACT GCELL: IDTYPE=BYID, CELLID=" + this.id + ", TRXIDTYPE=BYID;{" + this.kontrolerName + "}\r\n";
                odp = odp + "SET GCELLADMSTAT:IDTYPE=BYID,CELLID=" + this.id + ",ADMSTAT=UNLOCK;{" + this.kontrolerName + "}\r\n";

            }
            //barowanie dziala po odkomentowaniu
            //odp=odp+"SET GCELLIDLEBASIC:IDTYPE=BYID,CELLID="+this.id+",CBA=YES;{"+this.kontrolerName+"}\r\n";

        }
        else
        {
            odp = odp + "ACT GCELL: IDTYPE=BYID, CELLID=" + this.id + ", TRXIDTYPE=BYID;{" + this.kontrolerName + "}\r\n";
            odp = odp + "SET GCELLADMSTAT:IDTYPE=BYID,CELLID=" + this.id + ",ADMSTAT=LOCK;{" + this.kontrolerName + "}\r\n";
        }
        return odp;
    }

    @Override
    String getBlkUblkMML_STOP()
    {
        String odp = "";

        if (this.isUnBlocked())
        {
            if (!this.locGr || this.mainLocGr)
                odp = odp + "SET GCELLADMSTAT:IDTYPE=BYID,CELLID=" + this.id + ",ADMSTAT=UNLOCK;{" + this.kontrolerName + "}\r\n";
        }
        else
        {
            if (!this.locGr || this.mainLocGr)
                odp = odp + "SET GCELLADMSTAT:IDTYPE=BYID,CELLID=" + this.id + ",ADMSTAT=LOCK;{" + this.kontrolerName + "}\r\n";

        }
        if (this.isActive())
            if (!this.locGr || this.mainLocGr)
                odp = odp + "ACT GCELL: IDTYPE=BYID, CELLID=" + this.id + ", TRXIDTYPE=BYID;{" + this.kontrolerName + "}\r\n";
            else if (!this.locGr || this.mainLocGr)
                odp = odp + "DEA GCELL: IDTYPE=BYID, CELLID=" + this.id + ", TRXIDTYPE=BYID;{" + this.kontrolerName + "}\r\n";
        //System.out.println("|| CellId="+this.id+" cell_barr_acces="+this.lstGcellIdle.getWartosc("Cell Bar Access")+" isUbared="+this.isUnBarred());
        if (this.isUnBarred())
        {
            //barowanie dziala po odkomentowaniu
            //odp=odp+"SET GCELLIDLEBASIC:IDTYPE=BYID,CELLID="+this.id+",CBA=NO;{"+this.kontrolerName+"}\r\n";
        }
        else//barowanie dziala po odkomentowaniu
              ;//odp=odp+"//SET GCELLIDLEBASIC:IDTYPE=BYID,CELLID="+this.id+",CBA=YES;{"+this.kontrolerName+"}\r\n";

        return odp;
    }

    @Override
    String getSimulationMML_START()
    {
        if (this.powerToSet > 0.0)
        {
            if (!this.locGr || this.mainLocGr)
            {
                if(this.gulGcell)
                    return "STR GTRXBURSTTST:GLOCELLID="+glocellId+",DURATIONHOUR=3;{"+this.NeName+"}\r\n";
                else
                    return "STR TRXBURSTTST:OBJECTTYPE=BYCELL,IDTYPE=BYID,CELLID=" + this.id + ",DURATH=3;{" + this.kontrolerName + "}\r\n";
            }
        }
        return "";


        //return "";//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getSimulationMML_STOP()
    {
        if (this.powerToSet > 0.0)
        {
            if (!this.locGr || this.mainLocGr)
            {
                if(this.gulGcell)
                    return "STP GTRXBURSTTST:GLOCELLID="+glocellId+";{"+this.NeName+"}\r\n";
                else
                    return "STP TRXBURSTTST:OBJECTTYPE=BYCELL,IDTYPE=BYID,CELLID=" + this.id + ";{" + this.kontrolerName + "}\r\n";
            }
        }

        return "";
    }

    @Override
    void setTxNum(int s)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getBlkUblkRealtion_START()
    {
        return "";// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getBlkUblkRealtion_STOP()
    {
        return "";// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void setRelationToCell(Object relacje)
    {
        ;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getSetPowMML_START()
    {
        String odp = "";
        if (this.powerToSet > 0.0)
        {

            double trxPow = this.powerToSet;// / this.lstTrx.size();
            if (this.lstTrx.size() > 0)
                trxPow = this.powerToSet / this.lstTrx.size();
            if (!this.locGr)
            {



                String[] wynik = KomorkaGsm.findOptimal(this.powerToSet, this.lstTrx.size());
                //double[] POWT
                
                String gulPowPerTrx=""+(int)Komorka.wat2miliDbm(trxPow);

                for (int t = 0; t < this.lstTrx.size(); t++)
                {
                    String powT = wynik[t].split(";")[0];
                    if (powT.contains(".0"))
                        powT = powT.replace(".0", "");
                    else if (powT.contains("."))
                    {
                        powT = powT.replaceAll("[.]", "_");
                    }
                    String powL = wynik[t].split(";")[1];
                    if(this.gulGcell)
                            odp = odp + "SET GTRXDEV:IDTYPE=BYID,TRXID=" + this.lstTrx.get(t).getWartosc("TRX ID")+ ",EGBTSPOWT=" + gulPowPerTrx+ ",POWTUNIT=0_1DBM;{" + this.kontrolerName + "}\r\n";                        
                    else
                         odp = odp + "SET GTRXDEV:IDTYPE=BYID,TRXID=" + this.lstTrx.get(t).getWartosc("TRX ID") + ",POWL=" + powL + ",POWT=" + powT + "W,POWTUNIT=W;{" + this.kontrolerName + "}\r\n";

                }
            }
            else
            {
                if (trxPow < 0.1)
                    trxPow = 0.1;
                if(this.gulGcell)
                {
                     odp = odp + "MOD BTSLOCGRPE:IDTYPE=BYNAME, BTSNAME=\"" + this.lstBtsLocgr.get(0).getWartosc("BTS Name") + "\",LOCGRPNO=" + this.lstBtsLocgr.get(0).getWartosc("Location Group No.") + ",OUTPUTPOWERUNIT=0_01W,OUTPUTPOWERW=" + ((int) (trxPow * 100)) + ";{" + this.kontrolerName + "}\r\n";
                     //MOD BTSLOCGRPE: IDTYPE=BYID,BTSID=415,LOCGRPNO=20,OUTPUTPOWERUNIT=0_01W,OUTPUTPOWERW=2000;%%
                }
                else
                    odp = odp + "MOD BTSLOCGRP:IDTYPE=BYNAME, BTSNAME=\"" + this.lstBtsLocgr.get(0).getWartosc("BTS Name") + "\",LOCGRPNO=" + this.lstBtsLocgr.get(0).getWartosc("Location Group No.") + ",OUTPUTPOWERUNIT=0_1W,OUTPUTPOWER=" + ((int) (trxPow * 10)) + ";{" + this.kontrolerName + "}\r\n";
            }
        }

        return odp;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getSetPowMML_STOP()
    {
        String odp = "";

        if (this.powerToSet > 0.0)
        {
            if (!this.locGr)
            {
                for (int t = 0; t < this.lstGtrxDev.size(); t++)
                {
                    //  odp=odp+"SET GTRXDEV:IDTYPE=BYID,TRXID="+this.lstTrx.get(t).getWartosc("TRX ID")+",POWL=0,POWT="+trxPow+"W,POWTUNIT=W;{"+this.kontrolerName+"}\r\n";
                    Paczka TRX = this.lstGtrxDev.get(t);
                    if (TRX != null)//&&checkIfCellHasAsOs(nameTrx)!=null)
                    {
                        if(this.gulGcell)
                            odp = odp + "SET GTRXDEV:IDTYPE=BYID,TRXID=" + TRX.getWartosc("TRX ID") + ",EGBTSPOWT=" + TRX.getWartosc("eGBTS Power Type(0.1dBm)") + ",POWTUNIT=0_1DBM;{" + this.kontrolerName + "}\r\n";
                        else
                            odp = odp + "SET GTRXDEV:IDTYPE=BYID,TRXID=" + TRX.getWartosc("TRX ID") + ",POWL=" + TRX.getWartosc("Power Level") + ",POWT=" + TRX.getWartosc("GBTS Power Type(w)") + ",POWTUNIT=W;{" + this.kontrolerName + "}\r\n";
                    }
                }
            }
            else
            {
                 if(this.gulGcell)
                     odp = odp + "MOD BTSLOCGRPE:IDTYPE=BYNAME, BTSNAME=\"" + this.lstBtsLocgr.get(0).getWartosc("BTS Name") + "\",LOCGRPNO=" + this.lstBtsLocgr.get(0).getWartosc("Location Group No.") + ",OUTPUTPOWERUNIT=0_01W,OUTPUTPOWERW=" + this.lstBtsLocgr.get(0).getWartosc("Output Power(0.01W)") + ";{" + this.kontrolerName + "}\r\n";
                 else
                    odp = odp + "MOD BTSLOCGRP:IDTYPE=BYNAME, BTSNAME=\"" + this.lstBtsLocgr.get(0).getWartosc("BTS Name") + "\",LOCGRPNO=" + this.lstBtsLocgr.get(0).getWartosc("Location Group No.") + ",OUTPUTPOWERUNIT=0_1W,OUTPUTPOWER=" + this.lstBtsLocgr.get(0).getWartosc("Output Power(0.1W)") + ";{" + this.kontrolerName + "}\r\n";
            }

        }
        return odp;
    }

    public String toString()
    {
        return "System=GSM/DCS Name=" + this.cellName + " CellId=" + this.id + "  active=" + this.isActive() + " UnBlocked=" + this.isUnBlocked() + "  SRN[?]=" + this.getPosition() + " QUASI=" + this.locGr + " Main=" + (this.locGr && this.mainLocGr) + " MOC_DO_USTAWIENIA" + this.powerToSet + "/" + this.lstTrx.size() + "(txNum) GUL="+this.gulGcell;
    }

    public boolean isLocGr()
    {
        return locGr;
    }

    public void setLocGr(boolean locGr)
    {
        this.locGr = locGr;
    }

    public boolean isMainLocGr()
    {
        return mainLocGr;
    }

    public void setMainLocGr(boolean mainLocGr)
    {
        this.mainLocGr = mainLocGr;
    }

    static String[] findOptimal(double powerToSet, int trxNum)
    {


        java.util.ArrayList<Double> dostTrxPow = new java.util.ArrayList<Double>();
        dostTrxPow.add(80.0);
        dostTrxPow.add(60.0);
        dostTrxPow.add(50.0);
        dostTrxPow.add(40.0);
        dostTrxPow.add(31.0);
        dostTrxPow.add(30.0);
        dostTrxPow.add(27.0);
        dostTrxPow.add(25.0);
        dostTrxPow.add(20.0);
        dostTrxPow.add(16.0);
        dostTrxPow.add(15.0);
        dostTrxPow.add(14.0);
        dostTrxPow.add(13.3);
        dostTrxPow.add(13.0);
        dostTrxPow.add(12.0);
        dostTrxPow.add(11.0);
        dostTrxPow.add(10.0);
        dostTrxPow.add(9.5);
        dostTrxPow.add(9.0);
        dostTrxPow.add(8.5);
        dostTrxPow.add(8.0);
        dostTrxPow.add(7.5);
        dostTrxPow.add(7.0);
        dostTrxPow.add(6.0);
        dostTrxPow.add(5.5);
        dostTrxPow.add(5.0);
        dostTrxPow.add(4.0);
        dostTrxPow.add(3.7);
        dostTrxPow.add(3.1);
        dostTrxPow.add(3.0);
        dostTrxPow.add(2.5);
        dostTrxPow.add(2.0);
        dostTrxPow.add(1.5);
        dostTrxPow.add(1.0);

        java.util.ArrayList<Double> dostTlumienia = new java.util.ArrayList<Double>();
        dostTlumienia.add(0.0);//powL =0 
        dostTlumienia.add(1.58);//powL =1
        dostTlumienia.add(2.51);//powL =2
        dostTlumienia.add(3.98);//powL =3
        dostTlumienia.add(6.31);//powL =4
        dostTlumienia.add(10.0);//powL =5


        double[] bestTrxPow = new double[trxNum];
        int[] bestTlumienie = new int[trxNum];
        double smalestDifFromToset = Double.MAX_VALUE;
        double bestSum = 0;

        double TRXPowNajl = -1;
        int powerLevelNajl = -1;
        String[] wynik = new String[trxNum];

        if (trxNum == 1)
        {
            for (int i = 0; i < dostTrxPow.size(); i++)
            {
                Double powVal = dostTrxPow.get(i);
                for (int g = 0; g < dostTlumienia.size(); g++)
                {
                    Double powLevVal = dostTlumienia.get(g);
                    Double sumarTmp = powVal - powLevVal;
                    if (powerToSet > sumarTmp)
                    {
                        double smalestDifFromTosetTmp = powerToSet - sumarTmp;
                        if (smalestDifFromTosetTmp < smalestDifFromToset)
                        {
                            TRXPowNajl = powVal;
                            powerLevelNajl = g;
                            smalestDifFromToset = smalestDifFromTosetTmp;
                            bestSum = sumarTmp;

                        }
                    }
                }
            }
            //System.out.println("MOC DO USTAWIENIA="+powerToSet+" NAJBLIZEJ="+bestSum+" dif="+smalestDifFromToset+"  POW="+TRXPowNajl+" PWL="+powerLevelNajl);
            wynik[0] = "" + TRXPowNajl + ";" + powerLevelNajl;

        }
        else if (trxNum == 2)
        {
            for (int i0 = 0; i0 < dostTrxPow.size(); i0++)
            {
                Double powVal0 = dostTrxPow.get(i0);
                for (int g0 = 0; g0 < dostTlumienia.size(); g0++)
                {
                    Double powLevVal0 = dostTlumienia.get(g0);

                    for (int i1 = 0; i1 < dostTrxPow.size(); i1++)
                    {
                        Double powVal1 = dostTrxPow.get(i1);
                        for (int g1 = 0; g1 < dostTlumienia.size(); g1++)
                        {
                            Double powLevVal1 = dostTlumienia.get(g1);
                            Double sumarTmp = (powVal0 - powLevVal0) + (powVal1 - powLevVal1);
                            if ((powVal0 - powLevVal0) > 0 && (powVal1 - powLevVal1) > 0)
                                if (powerToSet >= sumarTmp)
                                {
                                    double smalestDifFromTosetTmp = powerToSet - sumarTmp;
                                    double stosunekBestTrx = powVal0 / powVal1;
                                    if (stosunekBestTrx >= (1.0 / 5.0) && stosunekBestTrx <= (5.0))
                                        if (smalestDifFromToset > smalestDifFromTosetTmp)
                                        {
                                            //   TRXPowNajl=powVal;
                                            //  powerLevelNajl=g;
                                            bestTrxPow[0] = powVal0;
                                            bestTlumienie[0] = g0;
                                            bestTrxPow[1] = powVal1;
                                            bestTlumienie[1] = g1;
                                            bestSum = sumarTmp;
                                            smalestDifFromToset = smalestDifFromTosetTmp;

                                        }
                                }
                        }
                    }



                }
            }
            wynik[0] = "" + bestTrxPow[0] + ";" + bestTlumienie[0];
            wynik[1] = "" + bestTrxPow[1] + ";" + bestTlumienie[1];
            System.out.println("MOC DO USTAWIENIA=" + powerToSet + " NAJBLIZEJ=" + bestSum + " dif=" + smalestDifFromToset + "  POW_TX0=" + bestTrxPow[0] + " PWL_TX0=" + bestTlumienie[0] + "  POW_TX1=" + bestTrxPow[1] + " PWL_TX1=" + bestTlumienie[1]);
        }
        return wynik;
    }

    public Paczka getLstGcellIdle()
    {
        return lstGcellIdle;
    }

    public void setLstGcellIdle(Paczka lstGcellIdle)
    {
        this.lstGcellIdle = lstGcellIdle;

        this.setUnBarred(this.lstGcellIdle.getWartosc("Cell Bar Access").equalsIgnoreCase("NO"));

    }

    public boolean isUnBarred()
    {
        return unBarred;
    }

    public void setUnBarred(boolean unBarred)
    {
        this.unBarred = unBarred;
    }

    /**
     *
     * @param freqBand przyjmuje wartosc parametru "Freq. Band" na podstawie ktorego ustawia pasmo 900/1800
     */
    @Override
    public void setPasmo(String freqBand)
    {
        if(freqBand.contains("1800"))
            super.setPasmo(Komorka.PASMO_1800);
        else if(freqBand.contains("900"))
            super.setPasmo(Komorka.PASMO_900);
       
    }

    public boolean isGulGcell()
    {
        return gulGcell;
    }

    public void setGulGcell(boolean gulGcell)
    {
        this.gulGcell = gulGcell;
    }
    
    
    
}