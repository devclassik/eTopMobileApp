package com.gbikna.sample.gbikna.util.utilities;

import android.util.Log;
import org.jpos.iso.ISOUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PackIso {
    private static final String TAG = PackIso.class.getSimpleName();
    public static byte[] formIso()
    {
        ISO8583 packISO8583  = new ISO8583();
        packISO8583.setMit(ProfileParser.field0);
        packISO8583.clearBit();
        if(ProfileParser.field2 != null)
        {
            byte[] field2 = ProfileParser.field2.getBytes();
            packISO8583.setBit(2, field2, field2.length);
        }
        if(ProfileParser.field3 != null)
        {
            byte[] field3 = ProfileParser.field3.getBytes();
            packISO8583.setBit(3, field3, field3.length);
        }
        if(ProfileParser.field4 != null)
        {
            byte[] field4 = ProfileParser.field4.getBytes();
            packISO8583.setBit(4, field4, field4.length);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
        String datetime = simpleDateFormat.format(new Date());
        ProfileParser.rfield7 = datetime;
        byte[] field7 = datetime.getBytes();
        packISO8583.setBit(7, field7, field7.length);
        simpleDateFormat = new SimpleDateFormat("hhmmss");
        String stan = simpleDateFormat.format(new Date());
        byte[] field11 = stan.getBytes();
        packISO8583.setBit(11, field11, field11.length);
        byte[] field12 = stan.getBytes();
        packISO8583.setBit(12, field12, field12.length);
        simpleDateFormat = new SimpleDateFormat("MMdd");
        String date = simpleDateFormat.format(new Date());
        byte[] field13 = date.getBytes();
        packISO8583.setBit(13, field13, field13.length);
        if(ProfileParser.field14 != null)
        {
            byte[] field14 = ProfileParser.field14.getBytes();
            packISO8583.setBit(14, field14, field14.length);
            ProfileParser.rfield14 = ProfileParser.field14;
        }

        if(ProfileParser.umcc.length() < 4)
        {
            byte[] field18 = "6012".getBytes();
            packISO8583.setBit(18, field18, field18.length);
        }else
        {
            byte[] field18 = ProfileParser.umcc.getBytes();
            packISO8583.setBit(18, field18, field18.length);
        }


        if(ProfileParser.field22 != null)
        {
            byte[] field22 = ProfileParser.field22.getBytes();
            packISO8583.setBit(22, field22, field22.length);
        }
        if(ProfileParser.field23 != null)
        {
            byte[] field23 = ProfileParser.field23.getBytes();
            packISO8583.setBit(23, field23, field23.length);
        }
        if(ProfileParser.field25 != null)
        {
            byte[] field25 = ProfileParser.field25.getBytes();
            packISO8583.setBit(25, field25, field25.length);
        }
        if(ProfileParser.field26 != null)
        {
            byte[] field26 = ProfileParser.field26.getBytes();
            packISO8583.setBit(26, field26, field26.length);
        }
        if(ProfileParser.field28 != null)
        {
            byte[] field28 = ProfileParser.field28.getBytes();
            packISO8583.setBit(28, field28, field28.length);
        }
        if(ProfileParser.field32 != null)
        {
            byte[] field32 = ProfileParser.field32.getBytes();
            packISO8583.setBit(32, field32, field32.length);
        }
        if(ProfileParser.field35 != null)
        {
            byte[] field35 = ProfileParser.field35.getBytes();
            packISO8583.setBit(35, field35, field35.length);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String pre = dateFormat.format(new Date());
        String rrn = pre.substring(2);
        byte[] field37 = rrn.getBytes();
        packISO8583.setBit(37, field37, field37.length);

        if (Integer.parseInt(ProfileParser.txnNumber) == 7) {
            field37 = ProfileParser.field37.getBytes();
            packISO8583.setBit(37, field37, field37.length);
        }else
            ProfileParser.field37 = rrn;

        if(ProfileParser.field38 != null)
        {
            byte[] field38 = ProfileParser.field38.getBytes();
            packISO8583.setBit(38, field38, field38.length);
        }

        if(ProfileParser.field40 != null)
        {
            byte[] field40 = ProfileParser.field40.getBytes();
            packISO8583.setBit(40, field40, field40.length);
        }
        byte[] field41 = ProfileParser.tid.getBytes();
        packISO8583.setBit(41, field41, field41.length);

        byte[] field42 = ProfileParser.umid.getBytes();
        packISO8583.setBit(42, field42, field42.length);

        byte[] field43 = ProfileParser.umnl.getBytes();
        packISO8583.setBit(43, field43, field43.length);

        if(String.valueOf(ProfileParser.curcode) != null) {
            byte[] field49 = String.valueOf(ProfileParser.curcode).getBytes();
            packISO8583.setBit(49, field49, field49.length);
        }else
        {
            ProfileParser.curcode = 566;
            byte[] field49 = String.valueOf(ProfileParser.curcode).getBytes();
            packISO8583.setBit(49, field49, field49.length);
        }

        if(ProfileParser.field52 != null && ProfileParser.field52.length() > 4)
        {
            byte[] field52 = ProfileParser.field52.getBytes();
            packISO8583.setBit(52, field52, field52.length);
        }
        if(ProfileParser.field55 != null)
        {
            byte[] field55 = ProfileParser.field55.getBytes();
            packISO8583.setBit(55, field55, field55.length);
        }
        if(ProfileParser.field56 != null)
        {
            byte[] field56 = ProfileParser.field56.getBytes();
            packISO8583.setBit(56, field56, field56.length);
        }

        if(ProfileParser.field59 != null)
        {
            if(Integer.parseInt(ProfileParser.txnNumber) == 2)
            {
                int i = ProfileParser.field59.indexOf("Meter Number=12^") + 16;
                Log.i(TAG, "INTEGER: " + i);
                if(i <= 15)
                {
                    byte[] field59 = ProfileParser.field59.getBytes();
                    packISO8583.setBit(59, field59, field59.length);
                }else
                {
                    String fin = ProfileParser.field59.substring(0, i) + ProfileParser.field62 + "^" + ProfileParser.field59.substring(i);
                    ProfileParser.field59 = fin;
                    byte[] field59 = ProfileParser.field59.getBytes();
                    packISO8583.setBit(59, field59, field59.length);
                }
            }else
            {
                byte[] field59 = ProfileParser.field59.getBytes();
                packISO8583.setBit(59, field59, field59.length);
            }
        }

        if(ProfileParser.field60 != null)
        {
            byte[] field60 = ProfileParser.field60.getBytes();
            packISO8583.setBit(60, field60, field60.length);
        }

        if(ProfileParser.field62 != null)
        {
            byte[] field62 = ProfileParser.field62.getBytes();
            packISO8583.setBit(62, field62, field62.length);
        }
        byte[] field123 = ProfileParser.field123.getBytes();
        packISO8583.setBit(123, field123, field123.length);
        byte use = 0x0;
        char ch = (char)use;
        byte[] field128 = Character.toString(ch).getBytes();
        packISO8583.setBit(128, field128, field128.length);
        ISO8583.sec = true;


        byte[] preUnmac = packISO8583.getMacIso();
        byte[] unMac = new byte[preUnmac.length - 64];
        System.arraycopy(preUnmac, 0, unMac, 0, preUnmac.length - 64);

        //byte[] unMac =  packISO8583.getMacIso();
        Log.i(TAG, "ISO BEFORE MAC: " + new String(unMac));
        EncDec enc = new EncDec();
        String gotten = null;
        try {
            Log.i(TAG, "CLEAR SESSION KEY USED: " + ProfileParser.fhostclrsk);
            gotten = enc.getMacNibss(ProfileParser.fhostclrsk, unMac);
            Log.i(TAG, "MAC: " + gotten);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProfileParser.field128 = gotten;
        field128 = gotten.getBytes();
        packISO8583.setBit(128, field128, field128.length);
        ISO8583.sec = true;
        byte[] packData =  packISO8583.isotostr();


        Log.i(TAG, "ISO TO HOST: " + ISOUtil.hexString(packData));


        Log.i(TAG, "IP: " + ProfileParser.fhostip);
        Log.i(TAG, "PORT: " + ProfileParser.fhostport);
        Log.i(TAG, "SSL: " + ProfileParser.fhostssl);

        Log.i(TAG, "Storing for database sake");
        byte[] getSending = new byte[packData.length - 2];
        System.arraycopy(packData, 2, getSending, 0, packData.length - 2);
        ISO8583.sec = true;
        ISO8583 unpackISO8583  = new ISO8583();
        unpackISO8583.strtoiso(getSending);
        ProfileParser.sending = new String[128];
        Utilities.logISOMsgMute(unpackISO8583, ProfileParser.sending);
        return packData;
    }
}

