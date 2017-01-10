/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import nbipackage.Paczka;

/**
 *
 * @author turczyt
 */
public class sektor
{
   static final double tolerancja=0.03; 
   boolean gsmStandAllone;
   String azymut;
   String pasmo;
   java.util.ArrayList<String> sektors_Id;
   java.util.ArrayList<Komorka> komorki;
   java.util.ArrayList<Paczka> rru;
   java.util.ArrayList<Paczka> rruOnlyGsm;
   java.util.ArrayList<Paczka> sectorEqDet;
   java.util.ArrayList<Paczka> dspRetSubUnit;
   String m2000Ip;
   String rncName;
   String bscName;
   String BtsName;
   String NeName;
   double mocAsOs;
   java.util.ArrayList<Integer> tiltToSet;
   java.util.ArrayList<String> tiltToSetTech;
   boolean valuOutOfRange;
   String errors;
   String tiltStart;
   String tiltStop;
   boolean onlyCheck;
   double mocPozostalaDoUstawienia;
   
   //Connection connection;
   //Statement testStatement;

    public sektor(String azymut, String pasmo,String bscName,String BtsName,String NeName,boolean onlyCheck)
    {
        this.azymut = azymut;
        this.pasmo = pasmo;
        this.bscName=bscName;
        this.BtsName=BtsName;
        this.NeName=NeName;
        
        this.mocAsOs=mocAsOs;
        this.valuOutOfRange=false;
        this.errors="";
        this.sektors_Id = new java.util.ArrayList<String>();
        this.komorki=new java.util.ArrayList<Komorka>();
        this.rru=new java.util.ArrayList<Paczka>();
        this.rruOnlyGsm=new java.util.ArrayList<Paczka>();
        this.sectorEqDet=new java.util.ArrayList<Paczka>();
        this.dspRetSubUnit=new java.util.ArrayList<Paczka> ();
        this.onlyCheck=onlyCheck;
        this.gsmStandAllone=false;
       // preparePowerToSet();
        
    }

    public boolean isGsmStandAllone()
    {
        return gsmStandAllone;
    }

    public void setGsmStandAllone(boolean gsmStandAllone)
    {
        this.gsmStandAllone = gsmStandAllone;
    }

    
    
    
    public ArrayList<Paczka> getSectorEqDet()
    {
        return sectorEqDet;
    }

    public void setSectorEqDet(ArrayList<Paczka> sectorEqDet)
    {
        this.sectorEqDet.addAll(sectorEqDet);
    }
    
    
    public void setSectorEqDet(Paczka sectorEqDet)
    {
        this.sectorEqDet.add(sectorEqDet);
    }

    public ArrayList<Paczka> getDspRetSubUnit()
    {
        return dspRetSubUnit;
    }

    public void addDspRetSubUnit(ArrayList<Paczka> dspRetSubUnit)
    {
        this.dspRetSubUnit.addAll(dspRetSubUnit);
    }
    public void addDspRetSubUnit(Paczka dspRetSubUnit)
    {
        this.dspRetSubUnit.add(dspRetSubUnit);
    }

    
    
    public ArrayList<Paczka> getRru()
    {
        return rru;
    }

    public void setRru(ArrayList<Paczka> rru)
    {
        this.rru = rru;
    }
    public void addRru(ArrayList<Paczka> rru)
    {
        this.rru.addAll(rru);
    }
     public void addRru(Paczka rru)
    {
        this.rru.add(rru);
    }
     
     
     public ArrayList<Paczka> getRruOnlyGsm()
    {
        return rruOnlyGsm;
    }

    public void setRruOnlyGsm(ArrayList<Paczka> rruOnlyGsm)
            
    {
        this.rruOnlyGsm = rruOnlyGsm;
    }
    public void addRruOnlyGsm(ArrayList<Paczka> rruOnlyGsm)
    {
        this.rruOnlyGsm.addAll(rruOnlyGsm);
    }
     public void addRruOnlyGsm(Paczka rruOnlyGsm)
    {
        this.rruOnlyGsm.add(rruOnlyGsm);
    }
     
     
    public ArrayList<Komorka> getKomorki()
    {
        return komorki;
    }

    public void setKomorki(ArrayList<Komorka> komorki)
    {
        this.komorki = komorki;
         this.posortujKomorki();
    }

    public void addKomorki(ArrayList<Komorka> komor)
    {
        for(int i=0;komor!=null&&i<komor.size();i++)
        {
            if(komor.get(i)!=null)
            addKom(komor.get(i));
        }
        this.posortujKomorki();
    }
    private void addKom(Komorka kom)
    {
        if(this.komorki!=null&&kom!=null&&!this.komorki.contains(kom))
            this.komorki.add(kom);
    }
    
    
    public String getAzymut()
    {
        return azymut;
    }

    public void setAzymut(String azymut)
    {
        this.azymut = azymut;
    }

    public String getPasmo()
    {
        return pasmo;
    }

    public void setPasmo(String pasmo)
    {
        this.pasmo = pasmo;
    }

    public java.util.ArrayList<String> getSektor_Id()
    {
        return sektors_Id;
    }

    public void setSektors_Id(java.util.ArrayList<String> sektor_Id)
    {
        this.sektors_Id = sektor_Id;
    }
    
    public void addSektors_Id(String sector_Id)
    {
        this.sektors_Id.add(sector_Id);
    }
    
    public void posortujKomorki()
    {
        Collections.sort(this.komorki);
    }
   
    public String toString()
    {
        String odp="";
        odp=    "Azymut="+(Integer.parseInt(this.azymut)/10)+"[stopni] Pasmo="+this.pasmo+"\r\n sektor ids="+this.sektors_Id.toString();
        odp=odp+"\\\\\\\\\\\\\\Komorki\\\\\\\\\\\\\r\n";
        boolean u900=false;
        boolean g900=false;
        boolean sharedBy2sys=false;
        for(int z=0;z<this.komorki.size();z++)
        {
            odp=odp+"\t"+komorki.get(z).toString()+"\r\n";
            if(komorki.get(z).getPriorytet()==Komorka.KOMORKA_GSM)
                g900=true;
            if(komorki.get(z).getPriorytet()==Komorka.KOMORKA_UMTS)
                u900=true;
        }
        odp=odp+"\\\\\\\\\\\\\\RRU\\\\\\\\\\\\\r\n";
        
        
        if(u900&&g900)
            sharedBy2sys=true;
        
        for(int r=0;r<this.rru.size();r++)
        {
            String RRUPOw=this.rru.get(r).getWartosc("Maximum output power on the TX channel(0.1dBm)");
            if(RRUPOw.equals("495"))
            {
                RRUPOw="490";
                if(sharedBy2sys&&onlyCheck)
                    RRUPOw="485";
            
            }
            odp=odp+"\tsrn="+this.rru.get(r).getWartosc("Subrack No.")+ " RX_num="+this.rru.get(r).getWartosc("Number of RX channels")+" maxPow="+Komorka.miliDbm2Wat(RRUPOw);
            
        }
        for(int r=0;r<this.rruOnlyGsm.size();r++)
        {
            String RRUPOw=this.rruOnlyGsm.get(r).getWartosc("Hardware Maximum Output Power of the TX Channel(Route A) (10mW)");
           
            odp=odp+"\t ONLY_GSM srn="+this.rruOnlyGsm.get(r).getWartosc("Subrack No.")+ " RX_num="+this.rruOnlyGsm.get(r).getWartosc("Number of RX channels")+" maxPow(one tx chanel)[10mW]="+RRUPOw;
            
        }
                
        return odp;
    }

    public double getMocAsOs()
    {
        return mocAsOs;
    }

    public void setMocAsOs(double mocAsOs)
    {
        this.mocAsOs = mocAsOs;
    }
           
    public java.util.ArrayList<String> getSRNs()
    {
        java.util.ArrayList<String> srns=new java.util.ArrayList<String>();
        if(this.rru!=null)
        {
            for(int r=0;r<this.rru.size();r++)
            srns.add(this.rru.get(r).getWartosc("Subrack No."));
        }
        return srns;
    }
    public java.util.ArrayList<String> getSRN2Gs()
    {
        java.util.ArrayList<String> srns=new java.util.ArrayList<String>();
        if(this.rruOnlyGsm!=null)
        {
            for(Paczka dspRR:rruOnlyGsm)
            srns.add(dspRR.getWartosc("Subrack No."));
        }
        return srns;
    }
    public double preparePowerToSetNormal(double mocPozostalaDoUstawienia)
    {
        //this.komorki
        for(int z=0;z<this.rru.size();z++)
        {
            //RF Unit Working Mode
            String srn=this.rru.get(z).getWartosc("Subrack No.");
            String workMode=this.rru.get(z).getWartosc("RF Unit Working Mode");
            if(workMode.contains("GSM")||workMode.contains("gsm"))
            {
                boolean existCell2G=false;
                for(int k=0;k<this.komorki.size();k++)
                {
                    if(this.komorki.get(k).getPriorytet()==Komorka.KOMORKA_GSM&&this.komorki.get(k).getPosition().equals(srn))
                        existCell2G=true;
                }
                if(!existCell2G)
                {
                    valuOutOfRange=true;
                    errors=errors+"Na stacji nie istnieje komorka 2G skorelowana z Azymut="+(Integer.parseInt(this.azymut)/10)+"[stopni] Pasmo="+this.pasmo+" SRN="+srn+"\r\n";
                }                
            }            
        }
        
        Hashtable<String,Double> maxPowerPerSrn=maxPowPerSrnPrepare();
        Hashtable<String,Double> PowerPerSrnTMP=(Hashtable<String,Double>)maxPowerPerSrn.clone();
        
        Hashtable<String,Double> maxPowerPerEq=maxPowPerEqPrepare(maxPowerPerSrn);
        Hashtable<String,Double> PowerPerEqTMP=(Hashtable<String,Double>)maxPowerPerEq.clone();
        Hashtable<String,Integer> TxPerEQ=txNumPerEqId();
        //for(int w=0;w<this.rru.size();w++)
          //  ;
       Enumeration<String> srny= maxPowerPerSrn.keys();
        //System.out.println("///////////////MAKSYMLNE MOCE PER SRN Azymut="+this.azymut+" Pasmo="+this.pasmo+" sektor ids="+this.sektors_Id.toString()+"  snry="+this.getSRNs()+"/////////");
        double maxAllRRU=0;
        while(srny.hasMoreElements())
        {
            String key=srny.nextElement();
          //  System.out.println("SRN"+key+" MOC MAKSYMALNA RRU="+maxPowerPerSrn.get(key));
            maxAllRRU=maxAllRRU+maxPowerPerSrn.get(key);
        }
         // System.out.println("///////////MAKSYMLNE MOCE PER SectorEq Azymut="+this.azymut+" Pasmo="+this.pasmo+" sektor ids="+this.sektors_Id.toString()+"/////////");
        Enumeration<String> eqipmenty= maxPowerPerEq.keys();
        double maxAllEq=0;
        while(eqipmenty.hasMoreElements())
        {
            String key=eqipmenty.nextElement();
           // System.out.println("SECEQID="+key+" MOC MAKSYMALNA="+maxPowerPerEq.get(key));
            maxAllEq=maxAllEq+maxPowerPerEq.get(key);
        }
        if(maxAllEq>maxAllRRU)
            maxAllEq=maxAllRRU;
        
  
        double sumaryPowToSet=mocPozostalaDoUstawienia;
        double sumaMocy=0.0;
       /* if((mocPozostalaDoUstawienia*(1.0-tolerancja))>maxAllRRU||(mocPozostalaDoUstawienia*(1.0-tolerancja))>maxAllEq)
        {
            //System.out.println("Moc do ustawienia="+this.mocAsOs+"("+(this.mocAsOs*(1.0-tolerancja))+") przekracza mozliwosci sprzetowe.Konf RRU maxPow="+maxAllRRU+", Konf SecEq maxPow="+maxAllEq);
           // valuOutOfRange=true;
            errors=errors+"Moc do ustawienia="+mocPozostalaDoUstawienia+"("+(mocPozostalaDoUstawienia*(1.0-tolerancja))+").Konfiguracja RRU umozliwia="+maxAllRRU+", Konfiguracja SektorEq umozliwia="+maxAllEq+"\r\n";
        }
        else*/
        {
            
            
            if((mocPozostalaDoUstawienia)>maxAllRRU)
                sumaryPowToSet=maxAllRRU;
            
            if((mocPozostalaDoUstawienia)>maxAllEq)
                sumaryPowToSet=maxAllEq;
            
                
            
            for(int a=0;a<this.komorki.size();a++)
            {
                String eqId=this.komorki.get(a).getPosition();       
                
                String srnId=null;
                boolean locGr=false;                
                for(int e=0;e<this.sectorEqDet.size()&&srnId==null;e++)
                    if(this.sectorEqDet.get(e).getWartosc("Sector Equipment ID").equals(eqId)&&this.sectorEqDet.get(e).getWartosc("TX Antenna Master/Slave Mode").equalsIgnoreCase("Master"))
                        srnId=this.sectorEqDet.get(e).getWartosc("Subrack No.");
                if(eqId!=null&&this.komorki.get(a)!=null&&this.komorki.get(a).getPriorytet()!=null&&this.komorki.get(a).getPriorytet()!=Komorka.KOMORKA_GSM)
                {
                    if(TxPerEQ.get(eqId)!=null)
                    this.komorki.get(a).setTxNum(TxPerEQ.get(eqId));
                }               
                if(this.komorki.get(a).getPriorytet()==Komorka.KOMORKA_GSM)
                {
                    srnId=this.komorki.get(a).getPosition();
                    eqId=this.komorki.get(a).getPosition()+";";
                    if(((KomorkaGsm)this.komorki.get(a)).isLocGr())
                        locGr=true;
                }        
               // System.out.println("KOMORKA="+this.komorki.get(a).getName()+" srnId="+srnId+" eqId="+eqId+" PowerPerSrnTMP.get(srnId)="+PowerPerSrnTMP.get(srnId)+" PowerPerEqTMP.get(eqId)="+PowerPerEqTMP.get(eqId));
                if(srnId!=null&&eqId!=null&&PowerPerSrnTMP.get(srnId)!=null&&PowerPerEqTMP.get(eqId)!=null)
                {
                    //PowerPerSrnTMP
                    //PowerPerEqTMP
                    double powToSetOnCell=-1;
                   if(sumaryPowToSet==0)
                   {
                       powToSetOnCell=0;                       
                   }
                   else if(PowerPerSrnTMP.get(srnId)>=sumaryPowToSet&&PowerPerEqTMP.get(eqId)>=sumaryPowToSet)//&&komorki.get(a).getMaximumCellPower()>=sumaryPowToSet)     
                   {
                       powToSetOnCell=sumaryPowToSet;
                       PowerPerSrnTMP.put(srnId, PowerPerSrnTMP.get(srnId)-powToSetOnCell);
                       PowerPerEqTMP.put(eqId,PowerPerEqTMP.get(eqId)-powToSetOnCell);
                       sumaryPowToSet=sumaryPowToSet-powToSetOnCell;
                   }
                   else
                   {
                       if(PowerPerSrnTMP.get(srnId)<sumaryPowToSet)
                       {
                           powToSetOnCell=PowerPerSrnTMP.get(srnId);
                           PowerPerEqTMP.put(eqId,0.0);
                           PowerPerSrnTMP.put(srnId,0.0);
                           sumaryPowToSet=sumaryPowToSet-powToSetOnCell;                           
                       }
                       else if(PowerPerEqTMP.get(eqId)<sumaryPowToSet)
                       {
                           powToSetOnCell=PowerPerEqTMP.get(eqId);
                           PowerPerEqTMP.put(eqId, PowerPerEqTMP.get(eqId)-powToSetOnCell);
                           PowerPerSrnTMP.put(srnId, PowerPerSrnTMP.get(srnId)-powToSetOnCell);
                           sumaryPowToSet=sumaryPowToSet-powToSetOnCell;
                       }
                   }
                   if(powToSetOnCell<mocPozostalaDoUstawienia*(tolerancja))
                   {
                       powToSetOnCell=0.0;
                       //sumaryPowToSet=0.0;
                   }
                  
                   if(powToSetOnCell==0.0)
                   {
                        if(locGr)
                            this.komorki.get(a).setPowerToSet(0.1);
                        else
                            this.komorki.get(a).setPowerToSet(powToSetOnCell);
                   }
                   else
                   {
                       this.komorki.get(a).setPowerToSet(powToSetOnCell);                 
                       sumaMocy=sumaMocy+powToSetOnCell;
                   
                   }
                }

            }
            if(sumaryPowToSet<mocPozostalaDoUstawienia*(tolerancja))
            {
                 sumaryPowToSet=0.0;
                       //sumaryPowToSet=0.0;
            }
            if(sumaryPowToSet>0.0)
            {
                if(this.komorki.size()==1&&this.rru.size()==2)
                {
                   if((sumaryPowToSet+this.komorki.get(0).getPowerToSet() )<=  maxAllEq)
                   {
                      // System.out.println("#################################ZWALONE MAIMO 4X NIE WIEM JAK ALE DZIALA###########################\r\n ");
                       this.komorki.get(0).setPowerToSet(this.komorki.get(0).getPowerToSet()+sumaryPowToSet);
                       sumaMocy=sumaMocy+this.komorki.get(0).getPowerToSet();
                       //sumaryPowToSet=sumaryPowToSet-this.komorki.get(0).getPowerToSet();
                       //return 0.0;
                   }
                   else
                   {
                       //valuOutOfRange=true;
                       errors=errors+"MIMO4x4: 2xRRU,1seqEQ,1 KomorkaMoc do ustawienia="+this.mocAsOs+"W przekracza mozliwosci sprzetowe.Po podziale mocy per komorki("+this.komorki.get(0).getPowerToSet()+") pozostalo="+sumaryPowToSet+"W .Konfiguracja RRU umozliwia="+maxAllRRU+"W, Konfiguracja SektorEq umozliwia="+maxAllEq+"W\r\n";
                
                   }
                
                }
               else
                {
                //valuOutOfRange=true;
                errors=errors+"Moc do ustawienia="+this.mocAsOs+"W przekracza mozliwosci sprzetowe.Po podziale mocy per komorki pozostalo="+sumaryPowToSet+"W .Konfiguracja RRU umozliwia="+maxAllRRU+"W, Konfiguracja SektorEq umozliwia="+maxAllEq+"W\r\n";
                }
            }
        }
       return mocPozostalaDoUstawienia-sumaMocy;
        
        
    }
    
    public void preparePowerToSet()
    {
        this.mocPozostalaDoUstawienia=this.getMocAsOs();
        //if(this.gsmStandAllone)
        //{
          //  System.out.println(this.azymut+" "+this.pasmo+" PROCEDURA GSMSTANDALLONE");
           
        //}
        //else
        //System.out.println("##################### podzial MOCY START##################");
        //System.out.println("before preparePowertTosetNormal="+mocPozostalaDoUstawienia);
          mocPozostalaDoUstawienia= preparePowerToSetNormal(mocPozostalaDoUstawienia); 
          //System.out.println("before preparePowertTosetGSMSTANDALLONE="+mocPozostalaDoUstawienia);
          if(mocPozostalaDoUstawienia>0.0)// && this.gsmStandAllone)
            mocPozostalaDoUstawienia=preparePowerToSetGsmStandAllone(mocPozostalaDoUstawienia);
           //System.out.println("after preparePowertTosetGSMSTANDALLONE="+mocPozostalaDoUstawienia+" przy mini dopuszczalnym="+(this.getMocAsOs()*(1.0-tolerancja)));
           
           
           
           
          if(mocPozostalaDoUstawienia>(this.getMocAsOs()*(tolerancja)))
          {   valuOutOfRange=true;
            errors=errors+"Moc do ustawienia="+this.mocAsOs+"("+(this.mocAsOs*(1.0-tolerancja))+") przekracza mozliwosci sprzetowe\r\n";
            for(int i=0;i<this.komorki.size();i++)
            {
                errors=errors+"\t komorka:"+this.komorki.get(i).getName()+"("+this.komorki.get(i).getId()+",)["+this.komorki.get(i).getPosition()+"] maksymalna dopuszczalna moc="+this.komorki.get(i).getPowerToSet()+"\r\n";
            }
          }
           
    }
    
    public double preparePowerToSetGsmStandAllone(double mocPozostalaDoUstawienia)
    {
        //this.komorki
        //CheckMax
       //System.out.println(this.azymut+" "+this.pasmo+" PROCEDURA GSMSTANDALLONE POZOSTALA MOC DO ROZDYSPONOWANIA="+mocPozostalaDoUstawienia );
        Hashtable<String,Double> maxPowerPerSrn=maxPowPerSrnGsmStandAlonePrepare();
        Hashtable<String,Double> PowerPerSrnTMP=(Hashtable<String,Double>)maxPowerPerSrn.clone();
        
        Hashtable<String,Double> maxPowerPerAntPass=maxPowPerAntPassPrepare(maxPowerPerSrn);
        Hashtable<String,Double> PowerPerAntPassTMP=(Hashtable<String,Double>)maxPowerPerAntPass.clone();
        Hashtable<String,Integer> TxPerAntPass=txNumPerAntPass();
        //for(int w=0;w<this.rru.size();w++)
          //  ;
       Enumeration<String> srny= maxPowerPerSrn.keys();
        //System.out.println("///////////////MAKSYMLNE MOCE PER SRN Azymut="+this.azymut+" Pasmo="+this.pasmo+" sektor ids="+this.sektors_Id.toString()+"  snry="+this.getSRNs()+"/////////");
        double maxAllRRU=0;
        while(srny.hasMoreElements())
        {
            String key=srny.nextElement();
          //  System.out.println("SRN"+key+" MOC MAKSYMALNA RRU="+maxPowerPerSrn.get(key));
            maxAllRRU=maxAllRRU+maxPowerPerSrn.get(key);
        }
         // System.out.println("///////////MAKSYMLNE MOCE PER SectorEq Azymut="+this.azymut+" Pasmo="+this.pasmo+" sektor ids="+this.sektors_Id.toString()+"/////////");
        Enumeration<String> eqipmenty= maxPowerPerAntPass.keys();
        double maxAllEq=0;
        while(eqipmenty.hasMoreElements())
        {
            String key=eqipmenty.nextElement();
           // System.out.println("SECEQID="+key+" MOC MAKSYMALNA="+maxAllEq);
            maxAllEq=maxAllEq+maxPowerPerAntPass.get(key);
        }
        if(maxAllEq>maxAllRRU)
            maxAllEq=maxAllRRU;
        
  
        //System.out.println("MAX SPRZETOWY="+(maxAllRRU)+" MAX KONFIGURACYJNY="+maxAllEq+" DO USTAWIENIA(3% tolerancji)="+(this.mocAsOs*(1.0-tolerancja)));
        /*if((this.mocAsOs*(1.0-tolerancja))>maxAllRRU||(this.mocAsOs*(1.0-tolerancja))>maxAllEq)
        {
            //System.out.println("Moc do ustawienia="+this.mocAsOs+"("+(this.mocAsOs*(1.0-tolerancja))+") przekracza mozliwosci sprzetowe.Konf RRU maxPow="+maxAllRRU+", Konf SecEq maxPow="+maxAllEq);
            valuOutOfRange=true;
            errors=errors+"Moc do ustawienia="+this.mocAsOs+"("+(this.mocAsOs*(1.0-tolerancja))+"). Mozliwosci hardware'owe="+maxAllRRU+", z wuzglednieniem aktualnej konfiguracji="+maxAllEq+"\r\n";
        }
        else*/
        double sumaryPowToSet=mocPozostalaDoUstawienia;
        {
            
            
            /*if((mocPozostalaDoUstawienia)>maxAllRRU)
                sumaryPowToSet=maxAllRRU;
            
            if((mocPozostalaDoUstawienia)>maxAllEq)
                sumaryPowToSet=maxAllEq;*/
            
                
            
            for(int a=0;a<this.komorki.size();a++)
            {
                
                String eqId=this.komorki.get(a).getPosition();               
                String srnId=null;
                boolean locGr=false;                
                               
                if(this.komorki.get(a).getPriorytet()==Komorka.KOMORKA_GSM)
                {
                    srnId=this.komorki.get(a).getPosition();
                
                    if(((KomorkaGsm)this.komorki.get(a)).isLocGr())
                                locGr=true;
                }
               // System.out.println("KOMORKA="+this.komorki.get(a).getName()+" srnId="+srnId+" eqId="+eqId+" PowerPerSrnTMP.get(srnId)="+PowerPerSrnTMP.get(srnId)+" PowerPerEqTMP.get(eqId)="+PowerPerEqTMP.get(eqId));
                if(srnId!=null&&PowerPerSrnTMP.get(srnId)!=null)
                {
                    //PowerPerSrnTMP
                    //PowerPerEqTMP
                    double powToSetOnCell=-1;
                   if(sumaryPowToSet==0)
                   {
                       powToSetOnCell=0;                       
                   }
                   else if(PowerPerSrnTMP.get(srnId)>=sumaryPowToSet)//&&komorki.get(a).getMaximumCellPower()>=sumaryPowToSet)     
                   {
                       powToSetOnCell=sumaryPowToSet;
                       PowerPerSrnTMP.put(srnId, PowerPerSrnTMP.get(srnId)-powToSetOnCell);
                       
                       sumaryPowToSet=sumaryPowToSet-powToSetOnCell;
                   }
                   else
                   {
                       if(PowerPerSrnTMP.get(srnId)<sumaryPowToSet)
                       {
                           powToSetOnCell=PowerPerSrnTMP.get(srnId);
                          
                           PowerPerSrnTMP.put(srnId,0.0);
                           sumaryPowToSet=sumaryPowToSet-powToSetOnCell;                           
                       }
                       
                   }
                   if(powToSetOnCell<mocPozostalaDoUstawienia*(tolerancja))
                   {
                       powToSetOnCell=0.0;
                       //sumaryPowToSet=0.0;
                   }
                  
                   if(powToSetOnCell==0.0)
                   {
                        if(locGr)
                            this.komorki.get(a).setPowerToSet(0.1);
                        else
                            this.komorki.get(a).setPowerToSet(powToSetOnCell);
                   }
                   else
                       this.komorki.get(a).setPowerToSet(powToSetOnCell);                 
                }

            }
            if(sumaryPowToSet<mocPozostalaDoUstawienia*(tolerancja))
            {
                 sumaryPowToSet=0.0;
                       //sumaryPowToSet=0.0;
            }
            if(sumaryPowToSet>0.0)
            {
               
                //valuOutOfRange=true;
                errors=errors+"Moc do ustawienia="+this.mocAsOs+"W przekracza mozliwosci sprzetowe.Po podziale mocy per komorki pozostalo="+sumaryPowToSet+"W .Mozliwosci sprzetowe="+maxAllRRU+"W, Aktualna konfiguracja umozliwia="+maxAllEq+"W\r\n";
               
            }
        }
       
        return sumaryPowToSet;
        
    }
    
    
    
    
    public String getErrors()
    {
        return errors;
    }
     public boolean isAllOk()
        {
            return !valuOutOfRange;
        }
    private Hashtable<String,Double> maxPowPerSrnPrepare()
    {
        Hashtable<String,Double> hash=new Hashtable();
        for(int z=0;z<this.rru.size();z++)
        {
            
            
            try{
            String srn=this.rru.get(z).getWartosc("Subrack No.");
           
             boolean u900=false;
        boolean g900=false;
        boolean sharedBy2sys=false;
        /*for(int z=0;z<this.komorki.size();z++)
        {
            odp=odp+"\t"+komorki.get(z).toString()+"\r\n";
            if(komorki.get(z).getPriorytet()==Komorka.KOMORKA_GSM)
                g900=true;
            if(komorki.get(z).getPriorytet()==Komorka.KOMORKA_UMTS)
                u900=true;
        }
        odp=odp+"\\\\\\\\\\\\\\RRU\\\\\\\\\\\\\r\n";
        
        
        if(u900&&g900)
            sharedBy2sys=true;
        */
             
             for(int a=0;a<this.komorki.size();a++)
            {
                
                if(this.komorki.get(a).getPriorytet()==Komorka.KOMORKA_GSM)
                {
                    String srnGSM=this.komorki.get(a).getPosition();
                    if(srn.equals(srnGSM))
                        g900=true;
                   
                }
                if(this.komorki.get(a).getPriorytet()==Komorka.KOMORKA_UMTS)
                {
                   u900=true;
                   
                }
            }
            if(u900&&g900)
                sharedBy2sys=true;
            
            String wholePow=this.rru.get(z).getWartosc("Maximum output power on the TX channel(0.1dBm)");
            if(wholePow.equals("495"))
            {
                //if(!exist2GonSrn)
                    wholePow="490";
                    if(this.onlyCheck&&sharedBy2sys)
                        wholePow="485";
                //else
                  //  wholePow="485";
            
            }
            
            //String maxTxNum=this.rru.get(z).getWartosc("Number of TX channels");
            double wholePowInWat=Komorka.miliDbm2Wat(wholePow);
            if(srn!=null)
                hash.put(srn, wholePowInWat);
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
            
        }
        return hash;
    }
    
    
    private Hashtable<String,Double> maxPowPerSrnGsmStandAlonePrepare()
    {
        Hashtable<String,Double> hash=new Hashtable();
        for(int z=0;z<this.rruOnlyGsm.size();z++)
        {
            try
            {
                String srn=this.rruOnlyGsm.get(z).getWartosc("Subrack No.");
                String wholePowA=this.rruOnlyGsm.get(z).getWartosc("Hardware Maximum Output Power of the TX Channel(Route A) (10mW)");
                String wholePowB=this.rruOnlyGsm.get(z).getWartosc("Hardware Maximum Output Power of the TX Channel(Route B) (10mW)");
                double wholePowInWat=Double.parseDouble(wholePowA)/100+Double.parseDouble(wholePowB)/100;
                if(srn!=null)
                    hash.put(srn, wholePowInWat);
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
            
        }
        return hash;
    }
    
    private Hashtable<String,Integer> txNumPerEqId()
    {
        Hashtable<String,Integer> hash=new Hashtable();//key=EqId value=suma mocy przypisana do sectorEqId;
        //Maximum output power on the TX channel(0.1dBm) /Number of TX channels
        for(int z=0;z<this.rru.size();z++)
        {
            String srn=this.rru.get(z).getWartosc("Subrack No.");
            
           
            for(int e=0;e<this.sectorEqDet.size();e++)
            {
                String srnFromEq=sectorEqDet.get(e).getWartosc("Subrack No.");
                if(srn.equals(srnFromEq))
                {
                    String seqEqId=sectorEqDet.get(e).getWartosc("Sector Equipment ID");
                    String antenaMode=sectorEqDet.get(e).getWartosc("Antenna RX/TX Mode");
                    if(antenaMode.contains("TX")||antenaMode.contains("tx"))
                    {
                       if(hash.containsKey(seqEqId))
                           hash.put(seqEqId, hash.get(seqEqId)+1);
                       else
                           hash.put(seqEqId, 1);
                    }
                }
            }
        }
        return hash;
    }
    
    private Hashtable<String,Integer> txNumPerAntPass()
    {
        Hashtable<String,Integer> hash=new Hashtable();//key=EqId value=suma mocy przypisana do sectorEqId;
        //Maximum output power on the TX channel(0.1dBm) /Number of TX channels
        for(int z=0;z<this.rru.size();z++)
        {
            String srn=this.rru.get(z).getWartosc("Subrack No.");
            
           
            for(int e=0;e<this.sectorEqDet.size();e++)
            {
                String srnFromEq=sectorEqDet.get(e).getWartosc("Subrack No.");
                if(srn.equals(srnFromEq))
                {
                    String seqEqId=sectorEqDet.get(e).getWartosc("Sector Equipment ID");
                    String antenaMode=sectorEqDet.get(e).getWartosc("Antenna RX/TX Mode");
                    if(antenaMode.contains("TX")||antenaMode.contains("tx"))
                    {
                       if(hash.containsKey(seqEqId))
                           hash.put(seqEqId, hash.get(seqEqId)+1);
                       else
                           hash.put(seqEqId, 1);
                    }
                }
            }
        }
        return hash;
    }
    private Hashtable<String,Double> maxPowPerAntPassPrepare(Hashtable<String,Double> maxPowerPerSrn)
    {
        Hashtable<String,Double> hash=new Hashtable();//key=srn;antPass value=maxMoc jednego AntenaPortu;
        //Maximum output power on the TX channel(0.1dBm) /Number of TX channels
        
        
        for(Paczka rru:this.rruOnlyGsm)
        {
            String srn=rru.getWartosc("Subrack No.");
            
            ////wylowienie komorek obslugiwanych przez danySubrack.
            java.util.ArrayList<KomorkaGsm> subCellList=new java.util.ArrayList<KomorkaGsm>();
            
            for(Komorka gcell:this.komorki)
            {
                if(gcell.getPriorytet()==Komorka.KOMORKA_GSM)
                if(((KomorkaGsm)gcell).getPosition().equals(srn))
                {
                    subCellList.add((KomorkaGsm)gcell);
                }
            }
            
            for(KomorkaGsm gcell:subCellList)
            {
                java.util.ArrayList<Paczka> trxy=gcell.getLstTrx();
                for(Paczka trx:trxy)
                {
                    String antPass=trx.getWartosc("Antenna Pass No");
                    String key=srn+";"+antPass;
                    
                    
                    Double maxOneTrxPow=null;
                    //maxOneTrxPow=20.0; odkomentowac jezeli moc pojedynczego trx ma byc nie wieksza niz
                    
                    String antPassPower=rru.getWartosc("Hardware Maximum Output Power of the TX Channel(Route "+antPass+") (10mW)");
                    //System.out.println("ANTPASS FROM TRX="+antPass);
                    //System.out.println(trx.getNazwy2());;
                    if(!hash.contains(key))
                    {
                        try{
                            
                             double wholePowInWat=0.0;
                             double antPow=Double.parseDouble(antPassPower)/100;
                            if(maxOneTrxPow==null)
                            {
                                wholePowInWat=antPow;
                            }
                            else
                            {
                                if(maxOneTrxPow<=antPow)
                                    wholePowInWat=maxOneTrxPow;
                                else
                                    wholePowInWat=antPow;
                            }
                            
                            
                            hash.put(key, wholePowInWat);
                        }
                        catch(Exception ee)
                        {
                            ee.printStackTrace();
                        }
                    }
                    else
                    {
                        Double usedPartOfPower=hash.get(key);
                        try
                        {
                            double wholePowInWat=0.0;
                              double antPow=Double.parseDouble(antPassPower)/100;
                            if(maxOneTrxPow==null)
                            {
                                wholePowInWat=antPow;
                            }
                            else
                            {
                                if(usedPartOfPower+maxOneTrxPow<=antPow)
                                    wholePowInWat=usedPartOfPower+maxOneTrxPow;
                                else
                                    wholePowInWat=antPow;
                            }
                            
                            
                            hash.put(key, wholePowInWat);
                        }
                        catch(Exception ee)
                        {
                            ee.printStackTrace();
                        }
                    }
                }
            }
        }
        
        return hash;
    }
    private Hashtable<String,Double> maxPowPerEqPrepare(Hashtable<String,Double> maxPowerPerSrn)
    {
        Hashtable<String,Double> hash=new Hashtable();//key=EqId value=suma mocy przypisana do sectorEqId;
        //Maximum output power on the TX channel(0.1dBm) /Number of TX channels
        for(int z=0;z<this.rru.size();z++)
        {
            String srn=this.rru.get(z).getWartosc("Subrack No.");
            double powPerTor=maxPowerPerSrn.get(srn)/(Double.parseDouble(this.rru.get(z).getWartosc("Number of TX channels")));
            for(int e=0;e<this.sectorEqDet.size();e++)
            {
                String srnFromEq=sectorEqDet.get(e).getWartosc("Subrack No.");
                if(srn.equals(srnFromEq))
                {
                    String seqEqId=sectorEqDet.get(e).getWartosc("Sector Equipment ID");
                    String antenaMode=sectorEqDet.get(e).getWartosc("Antenna RX/TX Mode");
                    if(antenaMode.contains("TX")||antenaMode.contains("tx"))
                    {
                        if(hash.containsKey(seqEqId))
                            hash.put(seqEqId, hash.get(seqEqId)+powPerTor);
                        else
                           hash.put(seqEqId, powPerTor);
                    }                    
                }
            }
            for(int w=0;w<this.komorki.size();w++ )
            {
                if(this.komorki.get(w).getPriorytet()==Komorka.KOMORKA_GSM&&((KomorkaGsm)this.komorki.get(w)).locGr==false)
                {
                     if(this.komorki.get(w).getPosition()!=null&&srn!=null&&this.komorki.get(w).getPosition().equals(srn))
                     {
                         java.util.ArrayList<Paczka> trxy=((KomorkaGsm)this.komorki.get(w)).getLstTrx();
                         
                         for(int t=0;t<trxy.size();t++)
                         {
                             String antenPort=trxy.get(t).getWartosc("Antenna Pass No");
                             String key=srn+";";//+antenPort;
                             if(hash.containsKey(key))
                             {
                                    hash.put(key, hash.get(key)+powPerTor);
                             }
                             else
                             {
                                 hash.put(key, powPerTor);
                             }
                         }
                     }
                }
                else if(this.komorki.get(w).getPriorytet()==Komorka.KOMORKA_GSM&&((KomorkaGsm)this.komorki.get(w)).locGr==true)
                {
                    if(this.komorki.get(w).getPosition()!=null&&srn!=null&&this.komorki.get(w).getPosition().equals(srn))
                     {
                      
                         
                        
                             String key=srn+";";//+antenPort;
                             if(hash.containsKey(key))
                             {
                                    hash.put(key, hash.get(key)+powPerTor);
                             }
                             else
                             {
                                 hash.put(key, powPerTor);
                             }
                         
                     }
                }
            }
           
            
            
        }
        return hash;
    }
   
    public void prepareTiltsComms()
    {
        tiltStart="";;
        tiltStop="";
        for(int z=0;z<this.dspRetSubUnit.size();z++)
        {
            try
            {
                
                String tiltAct=dspRetSubUnit.get(z).getWartosc("Actual Tilt(0.1degree)");
                int tiltMechAct=Integer.parseInt(dspRetSubUnit.get(z).getWartosc("Installed Mechanical Tilt(0.1degree)"));
                int maxAllowdElecticTilt=Integer.parseInt(dspRetSubUnit.get(z).getWartosc("Max Tilt(0.1degree)"));
                String DEVICENO=dspRetSubUnit.get(z).getWartosc("Device No.");//   0,SUBUNITNO=1
                String SUBUNITNO=dspRetSubUnit.get(z).getWartosc("Subunit No.");//   0,SUBUNITNO=1
                String devName=dspRetSubUnit.get(z).getWartosc("Device Name");//   0,SUBUNITNO=1
                String last_= devName.substring(  devName.lastIndexOf("_"));
                String devTech=null;
                
                if(last_.contains("U"))
                    devTech="UMTS";
                else if(last_.contains("L"))
                    devTech="LTE";
                else if(last_.contains("G"))
                    devTech="GSM";
                
                int indexVal=0;
                if(devTech!=null&&this.tiltToSetTech.indexOf(devTech)!=-1)
                {
                    indexVal=this.tiltToSetTech.indexOf(devTech);
                    
                }
                
                
                
                        
                if(indexVal>=this.tiltToSet.size()||this.tiltToSet.size()==1)
                    indexVal=this.tiltToSet.size()-1;
                 
                int tiltValToCheck=this.tiltToSet.get(indexVal);
                
                    
                
                int tiltElecToSet=(tiltValToCheck-tiltMechAct);
                if(tiltElecToSet>maxAllowdElecticTilt)
                {
                    valuOutOfRange=true;
                    errors=errors+"Tilt do ustawienia ="+tiltValToCheck+"(elektryczny="+tiltElecToSet+"), tilt elektryczny max="+maxAllowdElecticTilt+", tilt mech="+tiltMechAct+" DEVICENO="+DEVICENO+",SUBUNITNO="+SUBUNITNO+"\r\n";
                }
                else if(!tiltAct.matches("[0-9]+"))
                {
                        valuOutOfRange=true;
                        errors=errors+"Aktualna wartosc tiltu="+tiltAct+" jest nie poprawana DEVICENO="+DEVICENO+",SUBUNITNO="+SUBUNITNO+"\r\n";
                }
                else
                {
                    tiltStart=tiltStart+"MOD RETSUBUNIT:DEVICENO="+DEVICENO+",SUBUNITNO="+SUBUNITNO+",TILT="+tiltElecToSet+";{"+this.NeName+"}\r\n";
                    tiltStop=tiltStop+"MOD RETSUBUNIT:DEVICENO="+DEVICENO+",SUBUNITNO="+SUBUNITNO+",TILT="+tiltAct+";{"+this.NeName+"}\r\n";

                }
                
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
            
        }
    }

    public void setTiltToSet(String tiltToSet)
    {
        String[] tmp=tiltToSet.split(",");
        java.util.ArrayList<Integer>tmpVal=new java.util.ArrayList<Integer>();
        for(int g=0;g<tmp.length;g++)
        {
            tmpVal.add((int)( Double.parseDouble(tmp[g])*10));
        }
        this.tiltToSet =tmpVal;
    }
    
    
    public void setTiltToSetTech(String tiltToSetTech)
    {
        String[] tmp=tiltToSetTech.split(",");
        java.util.ArrayList<String>tmpVal=new java.util.ArrayList<String>();
        for(int g=0;g<tmp.length;g++)
        {
            tmpVal.add(tmp[g]);
        }
        this.tiltToSetTech =tmpVal;
    }
    
    
    public String getSetTiltMML_START()
    {
     
        return this.tiltStart;
    }
    public String getSetTiltMML_STOP()
    {
      return this.tiltStop;   
    }
}
    