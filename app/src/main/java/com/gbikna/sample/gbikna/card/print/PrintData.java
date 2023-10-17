package com.gbikna.sample.gbikna.card.print;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;


import com.gbikna.sample.MyApplication;
import com.gbikna.sample.gbikna.util.utilities.Utilities;
import com.gbikna.sample.etop.EodSummary;
import com.gbikna.sample.etop.util.SignupVr;
import com.gbikna.sample.utils.AppLog;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;
import com.gbikna.sample.gbikna.TransactionDetails;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import butterknife.ButterKnife;


public class PrintData {
    private static final String TAG = PrintData.class.getSimpleName();

    private TransactionDetails mConsumeSuccessActivity;
    private EodSummary mConsumeActivity;
    private Context mContext;
    IAidlPrinter printer;

    public PrintData(TransactionDetails consumeSuccessActivity) {
        mConsumeSuccessActivity = consumeSuccessActivity;
        mContext = consumeSuccessActivity;
        ButterKnife.bind(consumeSuccessActivity);
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public PrintData(EodSummary mConsumeActivity) {
        mConsumeActivity = mConsumeActivity;
        mContext = mConsumeActivity;
        ButterKnife.bind(mConsumeActivity);
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void printBarCode(String rrn) {

    }

    public void printDetail(String header) {
        getConsumePrintResponse(header);
    }

    private String getDate(String dat)
    {
        String db = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
                + "/" + dat.substring(0, 2) + "/" + dat.substring(2, 4) + " "
                + dat.substring(4, 6) + ":" + dat.substring(6, 8) + ":"
                + dat.substring(8, 10);
        return db;
    }

    private String formatExp(String dat)
    {
        if(dat == null)
            return "**/**";
        if(dat.length() < 1)
            return "**/**";
        String db = dat.substring(2, 4) + "/" + dat.substring(0, 2);
        return db;
    }

    private String printInAmountFormat(String cur, String number)
    {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String fin;
        if(formatter.format(amount).length() < 4)
            fin = cur + " 0" + formatter.format(amount);
        else
            fin = cur + " " + formatter.format(amount);
        return fin;
    }


    private void PrintDataOk(String header) {
        try {
            printer.printBmp(true, false, generateBitmap(header), 0, new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    switch (i) {
                        case PrinterConst.RetCode.ERROR_PRINT_NOPAPER:
                            //Toast.makeText(mContext, mContext.getString(R.string.msg_print_paper), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV:
                            //Toast.makeText(mContext, mContext.getString(R.string.msg_print_device), Toast.LENGTH_LONG).show();
                            break;
                        case PrinterConst.RetCode.ERROR_DEV_IS_BUSY:
                            //Toast.makeText(mContext, mContext.getString(R.string.msg_print_busy), Toast.LENGTH_LONG).show();
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
                            //Toast.makeText(mContext, mContext.getString(R.string.msg_print_other), Toast.LENGTH_LONG).show();
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


    private Bitmap generateEodSummary(String dateRange,
                                      String purhasecount, String purchaseamount, String purchaseagent,
                                      String purchasefee, String purchase9rapoint,
                                      String cashoutcount, String cashoutamount, String cashoutagent, String cashoutfee,
                                      String cashout9rapoint, String transfercount, String transferamount, String transferagent,
                                      String transferfee, String transfer9rapoint,
                                      String billscount, String billsamount, String billsagent, String billsfee, String bills9rapoint) {

        Log.i(TAG, "WISDOM PRINT MERCOPY 3: " + ProfileParser.merCopy);
        Log.i(TAG, "WISDOM PRINT CURCOPY 3: " + ProfileParser.cusCopy);
        Log.i(TAG, "getConsumePrintResponse()");

        CombBitmap combBitmap = new CombBitmap();
        File fi = new File(mContext.getFilesDir(), "logo.bmp");
        Log.i(TAG, "LOGO PRINTING: " + ProfileParser.rptshowlogo);
        Log.i(TAG, "LOGO EXIST: " + fi.exists());
        if (ProfileParser.rptshowlogo && fi.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
            combBitmap.addBitmap(bitmap);
        }

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        // print one line
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));


        Log.i(TAG, "getConsumePrintResponse()");
        //Content
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DATE RANGE:", dateRange, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PURCHASE COUNT:", purhasecount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PURCHASE AMOUNT:", purchaseamount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AGENT FEE:", "₦ 0.00", 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("9RA POINT FEE:", purchasefee, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("CASHOUT COUNT:", cashoutcount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("CASHOUT AMOUNT:", cashoutamount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TO WALLET:", cashoutagent, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AGENT FEE:", "₦ 0.00", 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("9RA POINT FEE:", cashoutfee, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TRANSFER COUNT:", transfercount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TRANSFER AMOUNT:", transferamount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TO WALLET:", "₦ 0.00", 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AGENT FEE:", "₦ 0.00", 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("9RA POINT FEE:", transferfee, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BILLS COUNT:", billscount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BILLS AMOUNT:", billsamount, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("FROM WALLET:", billsagent, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AGENT FEE:", "₦ 0.00", 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("9RA POINT FEE:", billsfee, 24, true, false));


        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap
        Bitmap bp = combBitmap.getCombBitmap();
        return bp;
    }

    public void PrintEodSummary(String dateRange,
                                String purhasecount, String purchaseamount, String purchaseagent,
                                String purchasefee, String purchase9rapoint,
                                String cashoutcount, String cashoutamount, String cashoutagent, String cashoutfee,
                                String cashout9rapoint, String transfercount, String transferamount, String transferagent,
                                String transferfee, String transfer9rapoint,
                                String billscount, String billsamount, String billsagent, String billsfee, String bills9rapoint) {
        try {
            printer.printBmp(true, false, generateEodSummary(dateRange,
                    purhasecount, purchaseamount, purchaseagent,
                    purchasefee, purchase9rapoint,
                    cashoutcount, cashoutamount, cashoutagent, cashoutfee,
                    cashout9rapoint, transfercount, transferamount, transferagent,
                    transferfee, transfer9rapoint,
                    billscount, billsamount, billsagent, billsfee, bills9rapoint), 0, new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    switch (i) {
                        case PrinterConst.RetCode.ERROR_PRINT_NOPAPER:
                            break;
                        case PrinterConst.RetCode.ERROR_DEV:
                            break;
                        case PrinterConst.RetCode.ERROR_DEV_IS_BUSY:
                            break;
                        default:
                        case PrinterConst.RetCode.ERROR_OTHER:
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

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppLog.d(TAG, "bitMap  =" + image);
        return image;
    }

    private Bitmap generateBitmap(String header) {

        Log.i(TAG, "WISDOM PRINT MERCOPY 3: " + ProfileParser.merCopy);
        Log.i(TAG, "WISDOM PRINT CURCOPY 3: " + ProfileParser.cusCopy);
        Log.i(TAG, "getConsumePrintResponse()");

        CombBitmap combBitmap = new CombBitmap();
        File fi = new File(mContext.getFilesDir(), "logo.bmp");
        Log.i(TAG, "LOGO PRINTING: " + ProfileParser.rptshowlogo);
        Log.i(TAG, "LOGO EXIST: " + fi.exists());
        if (ProfileParser.rptshowlogo && fi.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
            combBitmap.addBitmap(bitmap);
        }

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.bnkname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));

        //        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
//        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(SignupVr.businessaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        // print one line
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));


        Log.i(TAG, "getConsumePrintResponse()");
        //Content
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(header, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RECEIPT NO:", String.valueOf(ProfileParser.receiptNum), 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.txnName, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TERMINAL:",  ProfileParser.tid, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("SERIAL NUMBER:", Utilities.getSerialNumber(), 24, true, false));
        String sDate1 = getDate(ProfileParser.sending[7]);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dt = date.toString();
        String result = dt.substring(0, 10) + " " + dt.substring(30, dt.length());
        String time = dt.substring(11, 19);

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DATE:",  result, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TIME:",  time, 24, true, false));
        final String amount = ProfileParser.Amount;
        final String stamp = ProfileParser.Fee;
        String total = ProfileParser.totalAmount;
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AMOUNT:", printInAmountFormat(ProfileParser.curabbreviation, amount), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.getResponseDetails(ProfileParser.receiving[39]).toUpperCase(), 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RESPONSE CODE:", ProfileParser.receiving[39], 24, true, false));

        String print = "";
        if (ProfileParser.receiving[38] == null)
            print = "NA";
        else
            print = ProfileParser.receiving[38];
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("AUTHCODE:",  print, 24, true, false));
        if(Integer.valueOf(ProfileParser.txnNumber) == 1 || Integer.valueOf(ProfileParser.txnNumber) == 2)
        {
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("STAN:",  ProfileParser.sending[11], 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("RRN:",  ProfileParser.sending[37], 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("EXPIRY DATE:",  formatExp(ProfileParser.sending[14]), 24, true, false));
            String cardNo = ProfileParser.sending[2];
            String firCardNo = null;
            String mid = null;
            String lastCardNo = null;
            if (cardNo != null) {
                int cardLength = cardNo.length();
                firCardNo = cardNo.substring(0, 6);
                lastCardNo = cardNo.substring(cardLength - 4);
                mid = "******";
                cardNo = firCardNo + mid + lastCardNo;
            }
            final String finalCardNo = cardNo;
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PAN:",  finalCardNo, 24, true, false));

        }else if(Integer.valueOf(ProfileParser.txnNumber) == 3)
        {
            //combBitmap.addBitmap(GenerateBitmap.str2Bitmap("",  ProfileParser.sending[11], 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("",  ProfileParser.sending[37], 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BANK NAME:", ProfileParser.accountbank, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("ACCOUNT NUMBER:", ProfileParser.destination, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BENEFICIARY:", ProfileParser.receivername, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DESCRIPTION:", ProfileParser.description, 24, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BANKCODE:", ProfileParser.bankcode, 24, true, false));
        }else if(Integer.valueOf(ProfileParser.txnNumber) == 4)
        {
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.sending[37], 24, GenerateBitmap.AlignEnum.CENTER, true, false));
            combBitmap.addBitmap(GenerateBitmap.str2Bitmap("DESTINATION", ProfileParser.destination, 24, true, false));
            Log.i(TAG, "OTHERS: " + ProfileParser.sending[62]);
            try {
                JSONObject obj = new JSONObject(ProfileParser.sending[62]);
                Iterator<String> keys = obj.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    Log.i(TAG, "KEY: " + key);
                    combBitmap.addBitmap(GenerateBitmap.str2Bitmap(obj.getString(key), 24, GenerateBitmap.AlignEnum.CENTER, true, false));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("Powered By Etop", 24, GenerateBitmap.AlignEnum.CENTER, true, false));

        combBitmap.addBitmap(GenerateBitmap.generateLine(1)); // print one line
        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap
        Bitmap bp = combBitmap.getCombBitmap();
        return bp;
    }

    private void getConsumePrintResponse(String header) {
        PrintDataOk(header);
    }

}

