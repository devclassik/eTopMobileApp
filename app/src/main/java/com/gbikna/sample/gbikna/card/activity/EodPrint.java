package com.gbikna.sample.gbikna.card.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.gbikna.util.database.DATABASEHANDLER;
import com.gbikna.sample.gbikna.util.database.EOD;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class EodPrint {

    int i = 0 ;

    IAidlPrinter printer;
    //private AlertDialog.Builder mAlertDialog;
    private Reports mConsumeSuccessActivity;
    private Context mContext;
    private static final String TAG = EodPrint.class.getSimpleName();
    public EodPrint(Reports consumeSuccessActivity) {
        mConsumeSuccessActivity = consumeSuccessActivity;
        mContext = consumeSuccessActivity;
        ButterKnife.bind(consumeSuccessActivity);
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void Today()
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        td = f.format(new Date());
        DATABASEHANDLER db = new DATABASEHANDLER(mConsumeSuccessActivity);
        eod = db.getByDate(td);
        if(eod.isEmpty())
        {
            Toast.makeText(mConsumeSuccessActivity, "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
        }else
            processPrint(1);
    }

    String start = "";
    String end = "";
    String td = "";
    int chmode = 0;

    public void All(int tpy)
    {
        DATABASEHANDLER db = new DATABASEHANDLER(mConsumeSuccessActivity);
        eod = db.getAllEod();
        if(eod.isEmpty())
        {
            Toast.makeText(mConsumeSuccessActivity, "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
        }else
            processPrint(tpy);
    }
    public void StartDate(String mode, String st, String ed)
    {
        start = st;
        end = ed;
        DATABASEHANDLER db = new DATABASEHANDLER(mConsumeSuccessActivity);
        eod = db.getByDateRange(st.substring(0, 8), ed.substring(0, 8));
        if(eod.isEmpty())
        {
            Toast.makeText(mConsumeSuccessActivity, "NO DATA AVAILABLE", Toast.LENGTH_SHORT).show();
        }else {
            if(mode.equals("CARD"))
            {
                chmode = 1;
            }else if(mode.equals("QR"))
            {
                chmode = 2;
            }else if(mode.equals("USSD"))
            {
                chmode = 3;
            }else
            {
                chmode = 0;
            }
            processPrint(3);
        }
    }

    private List<EOD> eod;

    void processPrint(int typ)
    {
        printDetail(typ);
    }
    
    private void printDetail(int tpy) {
        getConsumePrintResponse(tpy);
        Log.i(TAG, "startPrint ");
    }

    Double totalAmt = 0.0;
    int TotalTxn = 0;
    int TotalFailed = 0;
    int TotalPassed = 0;

    String getStatus(String resp, String amount)
    {
        TotalTxn = TotalTxn + 1;
        if(resp == null)
        {
            TotalFailed = TotalFailed + 1;
            return "D";
        }

        if(resp.equals("00"))
        {
            TotalPassed = TotalPassed + 1;
            Float amt = Float.parseFloat(amount);
            totalAmt = totalAmt + amt;
            return "A";
        }else
        {
            TotalFailed = TotalFailed + 1;
            return "D";
        }
    }

    private String getDate(String dat)
    {
        String db = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
                + "/" + dat.substring(0, 2) + "/" + dat.substring(2, 4) + " "
                + dat.substring(4, 6) + ":" + dat.substring(6, 8) + ":"
                + dat.substring(8, 10);
        return db;
    }

    String getTime(String dat)
    {
        Log.i(TAG, "OLUBAYO REPRINT: " + dat);
        String sDate1 = getDate(dat);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dt = date.toString();
        String result = dt.substring(0, 10) + " " + dt.substring(30, dt.length());
        String time = dt.substring(11, 19);
        return time;
    }
    
    String getLastFive(String val)
    {
        String str = val;
        String substr = "";
        substr = str.substring(str.length() - 5, str.length());
        return substr;
    }

    String getFirstLetter(String val)
    {
        String str = val;
        String substr = "";
        substr = str.substring(0, 2);
        return substr;
    }
    
    String getTotal(String amount)
    {
        return amount;
    }
    
    
    private void PrintDataOk(int tpy) {
        try {
            printer.printBmp(true, false, generateBitmap(tpy), 0, new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    switch (i) {
                        case PrinterConst.RetCode.ERROR_PRINT_NOPAPER:
                            Toast.makeText(mContext, mContext.getString(R.string.msg_print_paper), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV:
                            Toast.makeText(mContext, mContext.getString(R.string.msg_print_device), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV_IS_BUSY:
                            Toast.makeText(mContext, mContext.getString(R.string.msg_print_busy), Toast.LENGTH_LONG).show();
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
                            Toast.makeText(mContext, mContext.getString(R.string.msg_print_other), Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                @Override
                public void onPrintSuccess() throws RemoteException {
                    Log.i(TAG,"onPrintSuccess: ");
                    //Toast.makeText(mContext, mContext.getString(R.string.msg_print_succ), Toast.LENGTH_LONG).show();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    String pntDate = "";
    int chP = 1;
    private Bitmap generateBitmap(int tpy) {
        CombBitmap combBitmap = new CombBitmap();
        File fi = new File(mContext.getFilesDir(), "logo.bmp");
        Log.i(TAG, "LOGO PRINTING: " + ProfileParser.rptshowlogo);
        Log.i(TAG, "LOGO EXIST: " + fi.exists());
        if (ProfileParser.rptshowlogo && fi.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
            combBitmap.addBitmap(bitmap);
        }

        // print one line
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("END OF DAY", 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.bnkname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        
        
        String print = "";
        if(tpy == 1)
        {
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DATE:", String.valueOf(td), 24, true, false));
        }else if(tpy == 2)
        {
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DATE:", String.valueOf("ALL"), 24, true, false));
        }else if(tpy == 3)
        {
            pntDate = start.substring(4, 8);
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("START DATE:", String.valueOf(start), 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("END DATE:", String.valueOf(end), 24, true, false));
        }

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TERMINAL ID:", ProfileParser.tid, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("MERCHANT ID:", ProfileParser.mid, 24, true, false));

        print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", "  TIME  ", "TT", " PAN ", "   AMOUNT   ", "S");
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));

        Log.i(TAG, "START DATE WISDOM: " + pntDate);
        if(tpy == 3)
        {
            String p = start.substring(0, 4) + "/" + pntDate.substring(0, 2) + "/"
                    + pntDate.substring(2, 4);
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(p, 24, GenerateBitmap.AlignEnum.LEFT, true, false));
            chP = 0;
        }

        for (EOD cn : eod) {
            if(tpy == 3)
            {
                Log.i(TAG, "CURRENT START DATE WISDOM: " + cn.getLocaldatetime().substring(0, 4));
                if(chmode == 1)
                {
                    //card
                    if(cn.getTxntype().contains("Qr Code") == false
                    && cn.getTxntype().contains("Ussd") == false)
                    {
                        if(!pntDate.equals(cn.getLocaldatetime().substring(0, 4)))
                        {
                            String p = start.substring(0, 4) + "/" + cn.getLocaldatetime().substring(0, 2) + "/"
                                    + cn.getLocaldatetime().substring(2, 4);
                            pntDate = cn.getLocaldatetime().substring(0, 4);
                            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(p, 24, GenerateBitmap.AlignEnum.LEFT, true, false));
                        }

                        print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", getTime(cn.getLocaldatetime()), getFirstLetter(cn.getTxntype()),
                                getLastFive(cn.getMaskedpan()), getTotal(cn.getAmount()), getStatus(cn.getRespcode(), cn.getAmount()));
                        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
                    }
                }else if(chmode == 2)
                {
                    //qr code
                    if(cn.getTxntype().contains("Qr Code"))
                    {
                        if(!pntDate.equals(cn.getLocaldatetime().substring(0, 4)))
                        {
                            String p = start.substring(0, 4) + "/" + cn.getLocaldatetime().substring(0, 2) + "/"
                                    + cn.getLocaldatetime().substring(2, 4);
                            pntDate = cn.getLocaldatetime().substring(0, 4);
                            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(p, 24, GenerateBitmap.AlignEnum.LEFT, true, false));
                        }
                        print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", getTime(cn.getLocaldatetime()), getFirstLetter(cn.getTxntype()),
                                getLastFive(cn.getMaskedpan()), getTotal(cn.getAmount()), getStatus(cn.getRespcode(), cn.getAmount()));
                        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
                    }
                }else if(chmode == 3)
                {
                    //ussd
                    if(cn.getTxntype().contains("Ussd"))
                    {
                        if(!pntDate.equals(cn.getLocaldatetime().substring(0, 4)))
                        {
                            String p = start.substring(0, 4) + "/" + cn.getLocaldatetime().substring(0, 2) + "/"
                                    + cn.getLocaldatetime().substring(2, 4);
                            pntDate = cn.getLocaldatetime().substring(0, 4);
                            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(p, 24, GenerateBitmap.AlignEnum.LEFT, true, false));
                        }

                        print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", getTime(cn.getLocaldatetime()), getFirstLetter(cn.getTxntype()),
                                getLastFive(cn.getMaskedpan()), getTotal(cn.getAmount()), getStatus(cn.getRespcode(), cn.getAmount()));
                        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
                    }
                }else
                {
                    if(!pntDate.equals(cn.getLocaldatetime().substring(0, 4)))
                    {
                        String p = start.substring(0, 4) + "/" + cn.getLocaldatetime().substring(0, 2) + "/"
                                + cn.getLocaldatetime().substring(2, 4);
                        pntDate = cn.getLocaldatetime().substring(0, 4);
                        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(p, 24, GenerateBitmap.AlignEnum.LEFT, true, false));
                    }

                    print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", getTime(cn.getLocaldatetime()), getFirstLetter(cn.getTxntype()),
                            getLastFive(cn.getMaskedpan()), getTotal(cn.getAmount()), getStatus(cn.getRespcode(), cn.getAmount()));
                    combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
                }
            }else
            {
                print = String.format("%-8.8s %-2.2s %-5.5s %-12.12s %1.1s", getTime(cn.getLocaldatetime()), getFirstLetter(cn.getTxntype()),
                        getLastFive(cn.getMaskedpan()), getTotal(cn.getAmount()), getStatus(cn.getRespcode(), cn.getAmount()));
                combBitmap.addBitmap(GenerateBitmap.str2Bitmap(print, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            }
        }

        combBitmap.addBitmap(GenerateBitmap.generateLine(1));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("SUMMARY", 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));


        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TOTAL APPROVED:", String.valueOf(TotalPassed), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TOTAL DECLINED:", String.valueOf(TotalFailed), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TOTAL TRANSACTION:", String.valueOf(TotalTxn), 24, true, false));

        DecimalFormat df = new DecimalFormat("0.00");
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("APPR. AMOUNT:", ProfileParser.curabbreviation + " " + String.valueOf(df.format(totalAmt)), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap

        Bitmap bp = combBitmap.getCombBitmap();

        return bp;
    }

    private void getConsumePrintResponse(int tpy) {
        PrintDataOk(tpy);
    }

}
