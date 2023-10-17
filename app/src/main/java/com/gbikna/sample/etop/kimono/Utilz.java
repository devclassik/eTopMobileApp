package com.gbikna.sample.etop.kimono;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author taysayshaguy
 */
public class Utilz {
    
    
    public static void main(String[] args) {
        
        String name = "";//+System.currentTimeMillis();
        
        for (int i = 0; i < 10; i++) {
            
            name = ""+System.currentTimeMillis();
            //Log.info("i = " + name+"  "+name.length()+" : "+String.valueOf(System.currentTimeMillis()).substring(7));
            
        }
        
        /*
        String x = "1234567890ABCDEF";
        
        Utilz u = new Utilz();
        
        Log.info("u = " + u.retBinaryValue(x));
        //Log.info("u = " + ISOUtil.hexString(x.getBytes()));
        */
        
    }
    
    public String retBinaryValue (String val) {
        
        if(val == null) val = "";
        String retBinValue = "";
        if (val.equals("0"))
            retBinValue = "0000";

        else if (val.equals("1"))
            retBinValue = "0001";

        else if (val.equals("2"))
            retBinValue = "0010";

        else if (val.equals("3"))
            retBinValue = "0011";

        else if (val.equals("4"))
            retBinValue = "0100";

        else if (val.equals("5"))
            retBinValue = "0101";

        else if (val.equals("6"))
            retBinValue = "0110";

        else if (val.equals("7"))
            retBinValue = "0111";

        else if (val.equals("8"))
            retBinValue = "1000";

        else if (val.equals("9"))
            retBinValue = "1001";

        else if (val.equals("A"))
            retBinValue = "1010";

        else if (val.equals("B"))
            retBinValue = "1011";

        else if (val.equals("C"))
            retBinValue = "1100";

        else if (val.equals("D"))
            retBinValue = "1101";

        else if (val.equals("E"))
            retBinValue = "1110";

        else if (val.equals("F"))
            retBinValue = "1111";

        return retBinValue;
    }

      public String retHexVal (String val) {
        String retHexValue = "";
        if (val.equals("0000"))
            retHexValue = "0";

        else if (val.equals("0001"))
            retHexValue = "1";

        else if (val.equals("0010"))
            retHexValue = "2";

        else if (val.equals("0011"))
            retHexValue = "3";

        else if (val.equals("0100"))
            retHexValue = "4";

        else if (val.equals("0101"))
            retHexValue = "5";

        else if (val.equals("0110"))
            retHexValue = "6";

        else if (val.equals("0111"))
            retHexValue = "7";

        else if (val.equals("1000"))
            retHexValue = "8";

        else if (val.equals("1001"))
            retHexValue = "9";

        else if (val.equals("1010"))
            retHexValue = "A";

        else if (val.equals("1011"))
            retHexValue = "B";

        else if (val.equals("1100"))
            retHexValue = "C";

        else if (val.equals("1101"))
            retHexValue = "D";

        else if (val.equals("1110"))
            retHexValue = "E";

        else if (val.equals("1111"))
            retHexValue = "F";

        return retHexValue;
    }
    
}
