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
public class KomorkaUmts extends Komorka
{
    final Integer  priorytet=Komorka.KOMORKA_UMTS;
    String id;
    String locellId;
    String  actualPowerRncSite;
    String actualPowerNodebSite;
    
    boolean unReserved;
    boolean unBlocked;
    boolean activetd;
    double maxCellPower;
    String srn;
    
    String NeName;
    String kontrolerName;
    
    String name;
    Paczka lstCell;
    Paczka lstUcellOnRnc;
    Paczka lstAccesStrict;
    String seqEqId;
    java.util.ArrayList<String> relacje;
    OdpowiedzSQL relacjeLte2Umts;

    public Paczka getLstCell()
    {
        return lstCell;
    }

    public void setLstCell(Paczka lstCell)
    {
        this.lstCell = lstCell;
        this.actualPowerNodebSite=this.lstCell.getWartosc("Max Output Power(0.1dBm)");
        
    }

    KomorkaUmts(String id)
    {
       this.id = id;
       
    }

    public boolean isActivetd()
    {
        return activetd;
    }

    public void setActivetd(boolean activetd)
    {
        this.activetd = activetd;
    }
    
    
    
    @Override
    public Integer getPriorytet()
    {
        return this.priorytet;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    
    public String getActualPowerRncSite()
    {
        return actualPowerRncSite;
    }

    @Override
    boolean isActualOnAir()
    {
        return this.unBlocked&&this.unReserved;
     
    }

    public void setNeName(String NeName)
    {
        this.NeName = NeName;
    }

    public void setKontrolerName(String kontrolerName)
    {
        this.kontrolerName = kontrolerName;
    }

    public void setActualPowerRncSite(String  actualPowerRncSite)
    {
        this.actualPowerRncSite = actualPowerRncSite;
    }

      public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocellId()
    {
        return locellId;
    }

    public void setLocellId(String locellId)
    {
        this.locellId = locellId;
    }

    public Paczka getLstAccesStrict()
    {
        return lstAccesStrict;
    }

    public void setLstAccesStrict(Paczka lstAccesStrict)
    {
        this.lstAccesStrict = lstAccesStrict;
        this.setName(this.lstAccesStrict.getWartosc("Cell Name"));
        String reserv=this.lstAccesStrict.getWartosc("Cell reserved for operator use");
        this.unReserved=  reserv.equalsIgnoreCase("NOT_RESERVED");
    }

    public double getMaxCellPower()
    {
        return maxCellPower;
    }

    public void setMaxCellPower(double maxCellPower)
    {
        this.maxCellPower = maxCellPower;
    }
    
     public String toString()
    {
        String odp="";
        odp=odp+"System=UMTS Name="+this.name +"CellId="+this.id+" LocallId="+this.locellId+" ActPowRncSide="+this.getActualPowerRncSite()+" ActPowNodebSide="+this.getActualPowerNodebSite()+" OnAir="+this.isActualOnAir()+" EQID="+this.seqEqId+" MOC DO USTAW="+this.powerToSet;
        return odp;
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

    public Paczka getLstUcellOnRnc()
    {
        return lstUcellOnRnc;
    }

    public void setLstUcellOnRnc(Paczka lstUcellOnRnc)
    {
        this.lstUcellOnRnc = lstUcellOnRnc;
        this.unBlocked=this.lstUcellOnRnc.getWartosc("Cell administrative state").equalsIgnoreCase("UNBLOCKED");
        this.actualPowerRncSite=this.lstUcellOnRnc.getWartosc("Max Transmit Power of Cell");
        this.activetd=this.lstUcellOnRnc.getWartosc("Validation indication").equalsIgnoreCase("ACTIVATED");
    }

    public String getActualPowerNodebSite()
    {
        return actualPowerNodebSite;
    }

    public void setActualPowerNodebSite(String actualPowerNodebSite)
    {
        this.actualPowerNodebSite = actualPowerNodebSite;
    }

    public String getSrn()
    {
        return srn;
    }

    public void setSrn(String srn)
    {
        this.srn = srn;
    }

    public String getSeqEqId()
    {
        return seqEqId;
    }

    public void setSeqEqId(String seqEqId)
    {
        this.seqEqId = seqEqId;
    }

    @Override
    public String getPosition()
    {
       return this.getSeqEqId();
    }
     
    public double getMaximumCellPower()
    {
        return Double.MAX_VALUE;
    }

   
    @Override
    String getBlkUblkRealtion_START()
    {
        String out="";
        if(this.powerToSet>0)
        for(int i=0;relacje!=null&&i<relacje.size();i++)
        {
            String typRel=relacje.get(i).split(":")[0];
            typRel=typRel.replace("ADD","");
            String parametry=relacje.get(i).split(":")[1];
            String[] param=parametry.split(",");
            String CELLID="";
            String rncId="";
            String nrncId="";
            
            for(int p=0;p<param.length;p++)
            {
                String[] nameVal=param[p].split("=");
                if(nameVal[0].trim().equalsIgnoreCase("CELLID"))
                {
                    CELLID=nameVal[1].trim();
                }
                if(nameVal[0].trim().equalsIgnoreCase("NCELLRNCID"))
                {
                    nrncId=nameVal[1].trim();
                }
                if(nameVal[0].trim().equalsIgnoreCase("RNCID"))
                {
                    rncId=nameVal[1].trim();
                }
            }
                    out=out+"RMV "+typRel+":RNCID="+rncId+", CELLID="+CELLID+", NCELLRNCID="+nrncId+", NCELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
                   
        }
        
        for(int z=0;z<this.relacjeLte2Umts.rowCount();z++)
        {
            if(relacjeLte2Umts.getValue("ne_name", z)!=null&&!relacjeLte2Umts.getValue("ne_name", z).equals(""))
                out=out+"MOD UTRANNCELL:LOCALCELLID="+relacjeLte2Umts.getValue("source_elocell_id", z)+",MCC=\""+relacjeLte2Umts.getValue("Mcc", z)+"\",MNC=\"0"+relacjeLte2Umts.getValue("Mnc", z)+"\",RNCID="+relacjeLte2Umts.getValue("ext_rnc_Id", z)+",CELLID="+relacjeLte2Umts.getValue("ext_ucell_id", z)+",NOHOFLAG=FORBID_HO_ENUM;{"+relacjeLte2Umts.getValue("ne_name", z)+"}\r\n" ;
           
           
        }
         return out;
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OdpowiedzSQL getRelacjeLte2Umts()
    {
        return relacjeLte2Umts;
    }

    public void setRelacjeLte2Umts(OdpowiedzSQL relacjeLte2Umts)
    {
        this.relacjeLte2Umts = relacjeLte2Umts;
    }

   
    @Override
    String getBlkUblkRealtion_STOP()
    {
        String out="";
        if(this.powerToSet>0)
        for(int i=0;relacje!=null&&i<relacje.size();i++)
        {
            out=out+relacje.get(i)+"{"+this.kontrolerName+"}\r\n";
        }
        
        for(int z=0;z<this.relacjeLte2Umts.rowCount();z++)
        {
            if(relacjeLte2Umts.getValue("ne_name", z)!=null&&!relacjeLte2Umts.getValue("ne_name", z).equals(""))
                out=out+"MOD UTRANNCELL:LOCALCELLID="+relacjeLte2Umts.getValue("source_elocell_id", z)+",MCC=\""+relacjeLte2Umts.getValue("Mcc", z)+"\",MNC=\"0"+relacjeLte2Umts.getValue("Mnc", z)+"\",RNCID="+relacjeLte2Umts.getValue("ext_rnc_Id", z)+",CELLID="+relacjeLte2Umts.getValue("ext_ucell_id", z)+",NOHOFLAG=PERMIT_HO_ENUM;{"+relacjeLte2Umts.getValue("ne_name", z)+"}\r\n" ;
           
           
        }
        
        
         return out;
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 @Override
    String getActDeaCell_START()
    {
       return  "DEA UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    String getBlkUblkMML_START()
    {
        String odp="";
        if(this.powerToSet>0)
        {
           odp=odp+"DEA UCELLHSDPA:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
           odp=odp+"MOD UCELLACCESSSTRICT:CELLID="+this.id+",CELLRESERVEDFOROPERATORUSE=RESERVED;{"+this.kontrolerName+"}\r\n";
           odp=odp+"ACT UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
           odp=odp+"UBL UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";

        }
        else
        {
            odp=odp+"ACT UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
            odp=odp+"BLK UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        }
        return odp;
    }

    
    @Override
    String getBlkUblkMML_STOP()
    {
        String odp="";
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        if(this.powerToSet>0.0)
        {
            odp=odp+"ACT UCELLHSDPA:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        }
        if(this.isUnReserved())
            odp=odp+"MOD UCELLACCESSSTRICT:CELLID="+this.id+",CELLRESERVEDFOROPERATORUSE=NOT_RESERVED;{"+this.kontrolerName+"}\r\n";
        else
            odp=odp+"MOD UCELLACCESSSTRICT:CELLID="+this.id+",CELLRESERVEDFOROPERATORUSE=RESERVED;{"+this.kontrolerName+"}\r\n";
        
        if(this.isUnBlocked())
            odp=odp+"UBL UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        else
            odp=odp+"BLK UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        
        if(this.isActivetd())
            odp=odp+"ACT UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        else
            odp=odp+"DEA UCELL:CELLID="+this.id+";{"+this.kontrolerName+"}\r\n";
        
        return odp;    
    }
    
    @Override
    String getSetPowMML_START()
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String odp="";
        if(this.powerToSet>0)
        {
            odp=odp+"MOD UCELL:CELLID="+this.id+",MAXTXPOWER="+(int)Math.round(Komorka.wat2miliDbm(this.powerToSet))+";{"+this.kontrolerName+"}\r\n";
            odp=odp+"MOD ULOCELL:ULOCELLID="+this.locellId+",LOCELLTYPE=NORMAL_CELL,MAXPWR="+(int)Math.round(Komorka.wat2miliDbm(this.powerToSet))+";{"+this.NeName+"}\r\n";
        
        }
        return odp;
    }

    @Override
    String getSimulationMML_START()
    {
        if(this.powerToSet>0)
            return "STR DLSIM:ULOCELLID="+this.locellId+",LR=99;{"+this.NeName+"}\r\n";
        else
            return "";
    }

    String getConfigCheck_START()
    {
        return "";
        
    }
    
    @Override
    void setTxNum(int s)
    {
        ;
    }
    
    @Override
    String getSimulationMML_STOP()
    {
         if(this.powerToSet>0)
            return "STP DLSIM:ULOCELLID="+this.locellId+";{"+this.NeName+"}\r\n";
         else
             return "";
    }
    
    @Override
    String getSetPowMML_STOP()
    {
        String odp="";
       if(this.powerToSet>0)
        {
            odp=odp+"MOD UCELL:CELLID="+this.id+",MAXTXPOWER="+this.actualPowerRncSite+";{"+this.kontrolerName+"}\r\n";
            odp=odp+"MOD ULOCELL:ULOCELLID="+this.locellId+",LOCELLTYPE=NORMAL_CELL,MAXPWR="+this.actualPowerNodebSite+";{"+this.NeName+"}\r\n";
        
        }
       return odp;
    }

    @Override
    void setRelationToCell(Object relacje)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.relacje=(java.util.ArrayList<String>)relacje;
    }
    
     
    
    
    
    
    
    
    
}
