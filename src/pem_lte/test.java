/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

/**
 *
 * @author turczyt
 */
public class test
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        // TODO code application logic here
      String[]  Enodeb_Name=new String[]{
"14_GRJ3301A_1_PIOTRASKARGI_10",
"14_KIE1019G_1_DOLOMITOWA_1",
"14_WAR2074D_1_WAL_MIEDZES_604",
"14_WAR3071B_1_OLAWSKA_1",
"24_JAW0005B_1_LICEALNA_6",
"24_JAW0008A_1_DWORNICKIEGO_2",
"24_JRS3301C_1_PRZEMYSLOWA_2",
"24_KAT0022C_1_ZELAZNA_9",
"24_KAT0084B_1_JANKEGO_276",
"24_KRA0013D_1_GROTTGERA_1",
"24_KRA0092G_1_POWSTANCOW_50",
"24_KRA0146B_1_KLIMECKIEGO_24",
"24_KRA0159B_1_CZARNOGORSKA_14",
"24_KRA0172B_1_BULGARSKA_38",
"24_KRA0188J_1_PL_NA_STAWACH_1",
"24_KRA0195F_1_BOBRZYNSKIEGO_1",
"24_KRA0200E_1_KRAKOWSKA_229",
"24_LIM2001A_1_DZ_445",
"24_MIE3303A_1_WOJSKA_POLSKIEG",
"24_MIE3304C_1_PARTYZANTOW_11",
"24_MSL2002A_1_KASPROWICZA_13",
"24_NWS2015H_1_LIMANOWSKIEGO_1",
"24_NWT2002A_1_JADWIGI_17",
"24_NYS2004A_1_PILSUDSKIEGO_40",
"24_OLU2901B_1_TYSIACLECIA_15",
"24_OPO1520A_1_PIOTRKOWSKA",
"24_PRZ3301A_1_PRZEMYSLAWA_33",
"24_RZE1001E_1_BRONIEWSKIEGO_1",
"24_RZE1013A_1_DABROWSKIEGO_71",
"24_RZE1014I_1_SLOWACKIEGO_24",
"24_STW3301A_1_OKULICKIEGO_1B",
"24_TRB3301A_1_GO_MAJA",
"24_ZYW2901A_1_FOLWARK_14",
"34_GOR1021B_1_GORCZYN",
"34_KAL3002A_1_PEC",
"34_KAL3011A_1_CMENTARZ",
"34_KLO3003A_1_STARA_WIEZA",
"34_KSN3001D_1_FABRYKA",
"34_OBO3021A_1_KOMIN",
"34_POZ0027C_1_PRZYBYSZEW",
"34_POZ0040E_1_SIERADZKA",
"34_POZ0046C_1_GORECKA",
"34_POZ0061D_1_OPOLSKA",
"34_POZ0062D_1_SWIERCZEWO",
"34_POZ0071E_1_SZ_SZEREGOW",
"34_POZ0072D_1_STRZESZYNSKA",
"34_POZ0087C_1_TYSIACLECIA",
"34_POZ0127D_1_WINOGRADY",
"34_POZ0157D_1_LUBON",
"34_POZ0168B_1_SWARZCENTER",
"34_POZ0171A_1_MOSINA_KRAS",
"34_POZ0179A_1_LANGIEWICZA",
"34_POZ0200B_1_WSK",
"34_POZ0238A_1_GRONOWA",
"34_POZ3061A_1_GADKI",
"34_SRM3001B_1_SZPITAL",
"34_WRO1174A_1_ZELAZNA",
"44_GDA0024A_1_GDANSKA21",
"44_GDA0060D_1_SWIETOKRZYSKA77",
"44_GDA0075H_1_BARNIEWICKA",
"44_GDA0095A_1_BIEGANSKIEGO_8",
"44_GDA0098D_1_LUBCZYKOWA",
"44_GDY0063A_1_KOLLATAJA1",
"44_KET0201A_1_WOLNOSCI_5",
"44_NDG0001E_1_NOWY_DWOR_GD",
"44_OLS1002E_1_WOJ_POL_83",
"44_OSR0101B_1_BEMA_14"};
      
      
      Idb baza;
            baza = new Idb(1433, "SqlQuery", "SqlQuery1234", "172.16.35.119",null);//, "UTRAN3.IDB2");
            baza.connectMS();

            for (int i = 0; i < Enodeb_Name.length; i++)
        {
            String propName=Enodeb_Name[i].split("_")[1];
            System.out.println(i+"/"+Enodeb_Name.length+" "+propName);
            String[][] pobierzWnioski = baza.pobierzWnioski(propName);
           System.out.println( baza.wnioksi.toString());
            
        }
//            String[][] aspemOs =null;
            
            
            /* FAKEOWY WNIOSEK ASPEMOS
            
            String[][] aspemOs =new String[][]{
             new String[]{ "LAN6002A","0","49.0","10.0","2100","LTE,UMTS"},
 new String[]{ "LAN6002A","0","52.010145582133724","12.0,12.0","900","GSM,UMTS,GSM"},
 new String[]{ "LAN6002A","1600","49.0","10.0","2100","LTE,UMTS"},
 new String[]{ "LAN6002A","1600","52.010145582133724","12.0,12.0","900","GSM,UMTS,GSM"},
 new String[]{ "LAN6002A","2600","49.0","12.0","2100","LTE,UMTS"},
 new String[]{ "LAN6002A","2600","52.010145582133724","12.0,12.0","900","GSM,UMTS,GSM"}};
            
            */
                

    }
}
