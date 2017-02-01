/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.util.ArrayList;
import mysqlpackage.OdpowiedzSQL;
import nbipackage.Paczka;

/**
 *
 * @author turczyt
 */
public class KomorkaLte extends Komorka
{
    final Integer  priorytet=Komorka.KOMORKA_LTE;
    String id;
    String locallCellId;
    String srn;
    
    OdpowiedzSQL relacjeDoKomorki;
    OdpowiedzSQL relacje2GDoKomorki;
            
    int subcarierNumber;
    double actualPowerWat;
    boolean unReserved;
    boolean unBlocked;
    double maximumCellPower;
    int txNum;
    
    Paczka lstCell;
    Paczka dspCell;
    Paczka cellOp;
    Paczka cellPdschcfg;
    Paczka cellDlPcpfschpa;
    
    String paAct;
    String pbAct;
    String rsAct;
    String rsMargAct;
    String rsToset;
            
    String NeName;
    String kontrolerName;
            
    String lteCellIndex;
    
    String name;
    String seqEqId;

    public String getLocallCellId()
    {
        return locallCellId;
    }

    public void setLocallCellId(String locallCellId)
    {
        this.locallCellId = locallCellId;
    }

    public Paczka getLstCell()
    {
        return lstCell;
    }

    public void setLstCell(Paczka lstCell)
    {
        this.lstCell = lstCell;
        this.name=this.lstCell.getWartosc("Cell Name");
    }

    public Paczka getDspCell()
    {
        return dspCell;
    }

    public OdpowiedzSQL getRelacje2GDoKomorki()
    {
        return relacje2GDoKomorki;
    }

    public void setRelacje2GDoKomorki(OdpowiedzSQL relacje2GDoKomorki)
    {
        this.relacje2GDoKomorki = relacje2GDoKomorki;
    }

    
    
    public void setDspCell(Paczka dspCell)
    {
        this.dspCell = dspCell;
        this.setMaximumCellPower(Komorka.miliDbm2Wat(Double.parseDouble(this.dspCell.getWartosc("Maximum transmit power(0.1dBm)"))));
        this.unBlocked=this.lstCell.getWartosc("Cell admin state").equalsIgnoreCase("Unblock");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
    @Override
    public Integer getPriorytet()
    {
        return this.priorytet;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public KomorkaLte(String id,int subcarierNumber)
    {
        this.id = id;
        this.subcarierNumber=subcarierNumber;
        this.relacjeDoKomorki=new OdpowiedzSQL();
        txNum=2;
        lteCellIndex=null;
    }

    public String getLteCellIndex()
    {
        return lteCellIndex;
    }

    public void setLteCellIndex(String lteCellIndex)
    {
        this.lteCellIndex = lteCellIndex;
    }


    public double getActualPower()
    {
        return this.getActualPowerWat();
    }

    @Override
    boolean isActualOnAir()
    {
        
        return unReserved&&unBlocked;
    }


    public void setActualPower(double actualPower)
    {
        this.actualPowerWat = actualPower;
    }



    public String getSeqEqId()
    {
        return seqEqId;
    }

    public void setSeqEqId(String seqEqId)
    {
        this.seqEqId = seqEqId;
    }


    public Paczka getCellOp()
    {
        return cellOp;
    }

    public void setCellOp(Paczka cellOp)
    {
        this.cellOp = cellOp;
        String cellReserv=this.cellOp.getWartosc("Cell reserved for operator");
        this.unReserved=cellReserv.equalsIgnoreCase("Not Reserved");
        
    }

    public Paczka getCellPdschcfg()
    {
        return cellPdschcfg;
    }

    public void setCellPdschcfg(Paczka cellPdschcfg)
    {
        this.cellPdschcfg = cellPdschcfg;
        this.rsAct=this.cellPdschcfg.getWartosc("Reference signal power(0.1dBm)");
        this.pbAct=this.cellPdschcfg.getWartosc("PB");
        
    }

    public Paczka getCellDlPcpfschpa()
    {
        return cellDlPcpfschpa;
    }

    public void setCellDlPcpfschpa(Paczka cellDlPcpfschpa)
    {
        this.cellDlPcpfschpa = cellDlPcpfschpa;
        
        try{
            String PA=this.cellDlPcpfschpa.getWartosc("PA for even power distribution(dB)");
            PA=PA.replaceAll("dB", "").trim();
            if(PA.contains("0"))
                this.paAct="DB0_P_A";
            if(PA.contains("-3"))
                this.paAct="DB_3_P_A";
            else if(PA.contains("-4"))
                this.paAct="DB_4DOT77_P_A";
            else if(PA.contains("-6"))
                this.paAct="DB_6_P_A";
            
        //double PA=Double.parseDouble();
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
            
        }
    }

    @Override
    void setTxNum(int s)
    {
       this.txNum=s;
    }
    
    
   
    public double getMaximumCellPower()
    {
       // return maximumCellPower;
        return Double.MAX_VALUE;
    }

    public void setMaximumCellPower(double maximumCellPower)
    {
        this.maximumCellPower = maximumCellPower;
    }

    public double getActualPowerWat()
    {
        return actualPowerWat;
    }

    public void setActualPowerWat(double actualPowerWat)
    {
        this.actualPowerWat = actualPowerWat;
    }

    public String getPaAct()
    {
        return paAct;
    }

    public void setPaAct(String paAct)
    {
        this.paAct = paAct;
    }

    public String getPbAct()
    {
        return pbAct;
    }

    public void setPbAct(String pbAct)
    {
        this.pbAct = pbAct;
    }

    public String getRsAct()
    {
        return rsAct;
    }

    public void setRsAct(String rsAct)
    {
        this.rsAct = rsAct;
    }

    public String getRsMargAct()
    {
        return rsMargAct;
    }

    public void setRsMargAct(String rsMargAct)
    {
        this.rsMargAct = rsMargAct;
    }

    public String getSrn()
    {
        return srn;
    }

    public void setSrn(String srn)
    {
        this.srn = srn;
    }

    public boolean isUnReserved()
    {
        return unReserved;
    }

    public void setUnReserved(boolean unReserved)
    {
        this.unReserved = unReserved;
    }

    public boolean isUnBlocked()
    {
        return unBlocked;
    }

    public void setUnBlocked(boolean unBlocked)
    {
        this.unBlocked = unBlocked;
    }
     //this.lstCell.getWartosc("Cell Name")
    public String toString()
    {
        String odp="";
        odp=odp+"System=LTE Name="+this.getName() +"CellId="+this.id+" LocallId="+this.locallCellId+" ActPow(PA,PB,RS)="+this.paAct+","+this.pbAct+","+this.rsAct+" OnAir="+this.isActualOnAir()+" EQID="+this.seqEqId+" MOC DO USTAW=(RS="+this.rsToset+",PB=1)";
        return odp;
    }

    public String getPosition()
    {
       return this.getSeqEqId();
    }

    public void setNeName(String NeName)
    {
        this.NeName = NeName;
    }

    public void setKontrolerName(String kontrolerName)
    {
        this.kontrolerName = kontrolerName;
    }



    
    
    
    
    //////generowanie komend MML
    @Override
    String getBlkUblkMML_START()
    {
        String odp="";
        if(this.powerToSet>0.0)
        {
            if(this.isUnReserved())
                odp=odp+"MOD CELLOP:LOCALCELLID="+this.locallCellId+",TRACKINGAREAID=0,CELLRESERVEDFOROP=CELL_RESERVED_FOR_OP;{"+this.NeName+"}\r\n";
            else
                odp=odp+"////MOD CELLOP:LOCALCELLID="+this.locallCellId+",TRACKINGAREAID=0,CELLRESERVEDFOROP=CELL_RESERVED_FOR_OP;{"+this.NeName+"}\r\n";
            
            if(this.isUnBlocked())
                odp=odp+"///UBL CELL:LOCALCELLID="+this.locallCellId+";{"+this.NeName+"}\r\n";
            else
                odp=odp+"UBL CELL:LOCALCELLID="+this.locallCellId+";{"+this.NeName+"}\r\n";
        }
        else
        {
          
            if(this.isUnBlocked())
                odp=odp+"BLK CELL:LOCALCELLID="+this.locallCellId+",CELLADMINSTATE=CELL_HIGH_BLOCK;{"+this.NeName+"}\r\n";
            else
                odp=odp+"///BLK CELL:LOCALCELLID="+this.locallCellId+",CELLADMINSTATE=CELL_HIGH_BLOCK;{"+this.NeName+"}\r\n";
        }
        return odp;
    }    

    @Override
    String getSetPowMML_START()
    {
        String odp="";
        if(this.powerToSet>0.0)
        {
           // odp=odp+"STR CFGCHK:;{"+this.NeName+"}\r\n";
            odp=odp+"MOD CELLDLPCPDSCHPA:LOCALCELLID="+this.locallCellId+",PAPCOFF=DB_3_P_A;{"+this.NeName+"}\r\n";
            odp=odp+"MOD PDSCHCFG:LOCALCELLID="+this.locallCellId+",REFERENCESIGNALPWR="+this.rsToset+",PB=1;{"+this.NeName+"}\r\n";
            
            //       MOD CELLDLPCPDSCHPA:LOCALCELLID=1,PAPCOFF=DB_3_P_A;
        }
        return odp;
    }
    
    
    String getConfigCheck_START()
    {
        String odp="";
        if(this.powerToSet>0.0)
        {
            odp=odp+"STR CFGCHK:;{"+this.NeName+"}\r\n";
          //  odp=odp+"MOD CELLDLPCPDSCHPA:LOCALCELLID="+this.locallCellId+",PAPCOFF=DB_3_P_A;{"+this.NeName+"}\r\n";
            //odp=odp+"MOD PDSCHCFG:LOCALCELLID="+this.locallCellId+",REFERENCESIGNALPWR="+this.rsToset+",PB=1;{"+this.NeName+"}\r\n";
            
            //       MOD CELLDLPCPDSCHPA:LOCALCELLID=1,PAPCOFF=DB_3_P_A;
        }
        return odp;
    }

    @Override
    String getSimulationMML_START()
    {
        if(this.powerToSet>0.0)
        {
            String kom="ADD CELLSIMULOAD:LOCALCELLID="+this.locallCellId+",SIMLOADCFGINDEX=9,SIMULOADTRANSMODE=TM"+this.txNum+"";
            if(this.txNum>2)
                kom=kom+",SIMULOADPMI="+this.txNum;
            return kom+";{"+this.NeName+"}\r\n";
        }
        else
             return "";
    }

    @Override
    String getActDeaCell_START()
    {
       return "";
    }

    
    
    
        @Override
    String getBlkUblkRealtion_START()
    {
        String odp="";
         if(this.powerToSet>0.0)
         {
             for(int r=0;r<relacjeDoKomorki.rowCount();r++)
             {
                 String typRelacji=relacjeDoKomorki.getValue("typRelacji", r);
                 String inter_intra=relacjeDoKomorki.getValue("inter_intra", r);
                 if(typRelacji.equals("LTE2LTE"))
                 {
                     if(inter_intra.equalsIgnoreCase("EutranIntraFreqNCell"))
                         odp=odp+"MOD EUTRANINTRAFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=FORBID_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                     else if(inter_intra.equalsIgnoreCase("EutranInterFreqNCell"))
                         odp=odp+"MOD EUTRANINTERFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=FORBID_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                     else
                         odp=odp+"//MOD EUTRANINTRAFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=FORBID_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                 }
             }
             if(this.lteCellIndex!=null)
                 odp=odp+"MOD ULTECELL:LTECELLINDEX="+this.lteCellIndex+",BLACKFLAG=true;{"+this.kontrolerName+"}\r\n";
             odp=odp+"//////////////////RELACJE   2G--->LTE ///////////////////////\r\n";
             for(int r=0;relacje2GDoKomorki!=null&&r<this.relacje2GDoKomorki.rowCount();r++)
             {
                  odp=odp+"MOD GLTENCELL:IDTYPE=BYID,SRCLTENCELLID="+relacje2GDoKomorki.getValue("SRCLTENCELLID", r)+",NBRLTENCELLID="+relacje2GDoKomorki.getValue("NBRLTENCELLID", r)+",SPTRESEL=UNSUPPORT;{"+relacje2GDoKomorki.getValue("Rnc_Bsc_Name", r)+"}\r\n";
             }
             
         }
         return odp;
    }
        
    @Override
    void setRelationToCell(Object relacje)
    {
        this.relacjeDoKomorki=(OdpowiedzSQL) relacje;
    }

    @Override
    String getBlkUblkRealtion_STOP()
    {
        String odp="";
         if(this.powerToSet>0.0)
         {
             for(int r=0;r<relacjeDoKomorki.rowCount();r++)
             {
                 String typRelacji=relacjeDoKomorki.getValue("typRelacji", r);
                   String inter_intra=relacjeDoKomorki.getValue("inter_intra", r);
                 //    odp=odp+"MOD EUTRANINTRAFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=PERMIT_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                   if(typRelacji.equals("LTE2LTE"))
                 {
                     if(inter_intra.equalsIgnoreCase("EutranIntraFreqNCell"))
                         odp=odp+"MOD EUTRANINTRAFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=PERMIT_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                     else if(inter_intra.equalsIgnoreCase("EutranInterFreqNCell"))
                         odp=odp+"MOD EUTRANINTERFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=PERMIT_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                     else
                         odp=odp+"//MOD EUTRANINTRAFREQNCELL:LOCALCELLID="+relacjeDoKomorki.getValue("locell_id_wychodzacy", r)+",MCC=\"260\",MNC=\"06\",ENODEBID="+relacjeDoKomorki.getValue("enodebId_stacji_PEM", r)+",CELLID="+relacjeDoKomorki.getValue("cellId_komorki_PEM", r)+",NOHOFLAG=PERMIT_HO_ENUM;{"+relacjeDoKomorki.getValue("ne_name", r)+"}\r\n";
                     
                 }
                  
             }
             if(this.lteCellIndex!=null)
                 odp=odp+"MOD ULTECELL:LTECELLINDEX="+this.lteCellIndex+",BlackFlag=false;{"+this.kontrolerName+"}\r\n";
             odp=odp+"//////////////////RELACJE   2G--->LTE ///////////////////////\r\n";
             for(int r=0;relacje2GDoKomorki!=null&&r<this.relacje2GDoKomorki.rowCount();r++)
             {
                  odp=odp+"MOD GLTENCELL:IDTYPE=BYID,SRCLTENCELLID="+relacje2GDoKomorki.getValue("SRCLTENCELLID", r)+",NBRLTENCELLID="+relacje2GDoKomorki.getValue("NBRLTENCELLID", r)+",SPTRESEL="+relacje2GDoKomorki.getValue("SPTRESEL", r)+";{"+relacje2GDoKomorki.getValue("Rnc_Bsc_Name", r)+"}\r\n";
             }
         }
         return odp;
    }   
    
    @Override
    String getSimulationMML_STOP()
    {
        if(this.powerToSet>0.0)
        {
            return "RMV CELLSIMULOAD:LOCALCELLID="+this.locallCellId+";{"+this.NeName+"}\r\n";
        }
        else
             return "";
    }
    
    
    
    
    
    String getBlkUblkMML_STOP()
    {
        String odp="";
           if(this.isUnReserved())
                odp=odp+"MOD CELLOP:LOCALCELLID="+this.locallCellId+",TRACKINGAREAID=0,CELLRESERVEDFOROP=CELL_NOT_RESERVED_FOR_OP;{"+this.NeName+"}\r\n";
            else
                odp=odp+"MOD CELLOP:LOCALCELLID="+this.locallCellId+",TRACKINGAREAID=0,CELLRESERVEDFOROP=CELL_RESERVED_FOR_OP;{"+this.NeName+"}\r\n";
            
            if(this.isUnBlocked())
                odp=odp+"UBL CELL:LOCALCELLID="+this.locallCellId+";{"+this.NeName+"}\r\n";
            else
                odp=odp+"BLK CELL:LOCALCELLID="+this.locallCellId+",CELLADMINSTATE=CELL_HIGH_BLOCK;{"+this.NeName+"}\r\n";
       
        return odp;
    }

    @Override
    String getSetPowMML_STOP()
    {
        String odp="";
        if(this.powerToSet>0.0)
        {
            odp=odp+"MOD CELLDLPCPDSCHPA:LOCALCELLID="+this.locallCellId+",PAPCOFF="+this.paAct+";{"+this.NeName+"}\r\n";
            odp=odp+"MOD PDSCHCFG:LOCALCELLID="+this.locallCellId+",REFERENCESIGNALPWR="+this.rsAct+",PB="+this.pbAct+";{"+this.NeName+"}\r\n";
            
            //       MOD CELLDLPCPDSCHPA:LOCALCELLID=1,PAPCOFF=DB_3_P_A;
        }
        return odp;
    }
    
    
    @Override
    public void setPowerToSet(double powerToSet)
    {
        super.setPowerToSet(powerToSet);
        //wat2rsInMiliDbm
        this.rsToset=""+Komorka.wat2rsInMiliDbm(powerToSet, Double.parseDouble("1"), subcarierNumber, txNum);
        
    }
    
    
}
