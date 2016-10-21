/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import mysqlpackage.OdpowiedzSQL;

/**
 *
 * @author turczyt
 */
public abstract class Komorka implements Comparable
{

   
    double powerToSet;
    
    static int KOMORKA_LTE=1;
    static int KOMORKA_UMTS=2;
    static int KOMORKA_GSM=3;
    //OdpowiedzSQL relacjeDoKomorki;
    
   

    @Override
    public int compareTo(Object o)
    {
        
        if(getPriorytet()==((Komorka)o).getPriorytet())
            return getId().compareTo(((Komorka)o).getId());
        else
        {
           return getPriorytet().compareTo(((Komorka)o).getPriorytet());
        }

    }

    @Override
    public boolean equals(Object obj)
    {
        if(this==null||obj==null)
            return super.equals(obj);
        else if(getPriorytet()==null||((Komorka)obj).getPriorytet()==null)
            return super.equals(obj);
        else if(getPriorytet()==((Komorka)obj).getPriorytet())
        {
            if(getId()!=null&&((Komorka)obj).getId()!=null&&getPosition()!=null&&((Komorka)obj).getPosition()!=null)
                return getId().equals(((Komorka)obj).getId())&&getPosition().equals(((Komorka)obj).getPosition());
            else
            {
                throw new java.lang.NullPointerException("getId()="+getId()+" ((Komorka)obj).getId()="+((Komorka)obj).getId()+" getPosition()="+getPosition()+"((Komorka)obj).getPosition()="+((Komorka)obj).getPosition());
            }
        }
        else
        {
           return getPriorytet().equals(((Komorka)obj).getPriorytet());
        }
    }
    
    

    abstract public String getId();
    abstract public void setId(String id);

    abstract public String getPosition();
    abstract public Integer getPriorytet();   
    
     abstract public void setNeName(String NeName);
    abstract public void  setKontrolerName( String kontrolerName);
    
    //abstract public double getActualPower();
    //abstract public void setActualPower(double value);
  
    abstract boolean isActualOnAir();
   // abstract void setActualOnAir(boolean value);
    


    @Override
    public String toString()
    {
        return "System="+getPriorytet()+" CellId="+getId();
    }
    
    
    
    public static double wat2miliDbm(double wat)
    {
        double dbm=10.0* Math.log10(1000.0*(wat));
        return dbm*10.0;//zamiana [dbm] na [0.1 dbm]
    }
    
    public static double wat2miliDbm(String wat)
    {
        
        return wat2miliDbm(Double.parseDouble(wat));
    }
    
    public static double miliDbm2Wat(double miliDbm)
    {
        double dBm=miliDbm/10;
        double wat=Math.pow(10.0, (dBm/10.0))/1000.0;
        wat=Math.round(wat*100)/100.0d;
        return wat;
    }
    public static int wat2rsInMiliDbm(double wat,double pb,int subcarierNum,int txNum)
    {
        double watToSet=wat/txNum;
        double pa=10.0*Math.log10(1/(1+pb));
        int rs=(int)((10.0*Math.log10(watToSet*1000/subcarierNum)-pa)*10);
        return rs;
        
    }
    public static double miliDbm2Wat(String miliDbm)
    {
        return miliDbm2Wat(Double.parseDouble(miliDbm));
    }

    public double getPowerToSet()
    {
        return powerToSet;
    }

   /* void setRelationToCell(OdpowiedzSQL relacje)
    {
        
        this.relacjeDoKomorki= relacje;
        
    }*/
 
   abstract void setRelationToCell(Object relacje);
    
    public void setPowerToSet(double powerToSet)
    {
        this.powerToSet = powerToSet;
    }
    abstract double getMaximumCellPower();        
    
    abstract String getBlkUblkMML_START();
    abstract String getBlkUblkMML_STOP();
    abstract String getSetPowMML_START();
    
    abstract String getConfigCheck_START();
    abstract String getSetPowMML_STOP();
    abstract String getSimulationMML_START();
    abstract String getSimulationMML_STOP();
   // abstract 
    abstract String getBlkUblkRealtion_START();
    abstract String getBlkUblkRealtion_STOP();
    abstract void setTxNum(int s);
    abstract String getName();
    abstract String getActDeaCell_START();
  //  abstract String getActDeaGcell_STOP();
    
    
    
}
