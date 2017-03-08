/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pem_lte;

import java.util.regex.Pattern;

/**
 *
 * @author turczyt
 */
public class NewMain1
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        String dspGtrxOnNode="+++    14_BEL1009A_1_OKRZEI        2017-03-01 12:55:09\n" +
"O&M    #269484109\n" +
"%%/*77872521*/DSP GTRX:;%%\n" +
"RETCODE = 0  Operation succeeded.\n" +
"\n" +
"Display GBTS TRX\n" +
"----------------\n" +
"Local Cell ID  Cell Index  Cell No.  TRX Index  TRX No.  TRX Group ID  Is BCCH TRX  Whether Primary TRX or Not  License Authorized  Effective Status  Sector ID  Sector Equipment ID  Baseband Board Cabinet No.  Baseband Board Subrack No.  Baseband Board Slot No.\n" +
"\n" +
"0              819         80        2414       31       2             Yes          NULL                        Licensed            Effective         10         65535                0                           0                           3                      \n" +
"0              819         80        2415       32       4             No           NULL                        Licensed            Effective         10         65534                0                           0                           3                      \n" +
"1              1098        120       2416       33       15            Yes          NULL                        Licensed            Effective         12         15                   0                           0                           3                      \n" +
"1              1098        120       2423       34       17            No           NULL                        Licensed            Effective         12         17                   0                           0                           3                      \n" +
"2              1099        98        2424       35       28            Yes          NULL                        Licensed            Effective         14         28                   0                           0                           3                      \n" +
"2              1099        98        2425       36       30            No           NULL                        Licensed            Effective         14         65533                0                           0                           3                      \n" +
"20             1690        45        3178       1        200           Yes          NULL                        Licensed            Effective         20         200                  0                           0                           3                      \n" +
"21             1691        54        3179       3        201           Yes          NULL                        Licensed            Effective         22         201                  0                           0                           3                      \n" +
"22             1692        109       3180       5        202           Yes          NULL                        Licensed            Effective         24         202                  0                           0                           3                      \n" +
"(Number of results =  0    )\n" +
"\n" +
"\n" +
"---    END";
        if(dspGtrxOnNode.contains("RETCODE = 0")&&Pattern.compile("[Number of results =]([1-9]|([1-9][0-9]))\\s*[)]").matcher(dspGtrxOnNode).find())
        {
       System.out.println("check="+ Pattern.compile("[Number of results = ]\\s*([1-9]|([1-9][0-9]))\\s*[)]").matcher(dspGtrxOnNode).find());
        }
        
        
        String tt=" 9";
        
        if(tt==null||!tt.matches("[0-9]+"))
        {
            System.out.println("Do zmian,null lub nie liczbowy");
        }
    }
}
