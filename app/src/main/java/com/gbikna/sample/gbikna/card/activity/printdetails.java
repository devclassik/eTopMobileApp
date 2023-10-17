package com.gbikna.sample.gbikna.card.activity;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.gbikna.sample.MyApplication;
import com.gbikna.sample.R;
import com.gbikna.sample.utils.CombBitmap;
import com.gbikna.sample.utils.GenerateBitmap;
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener;
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter;
import com.horizonpay.smartpossdk.data.PrinterConst;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.Utils.TCMIP;
import static com.gbikna.sample.gbikna.util.utilities.Utils.TCMPORT;


public class printdetails {
    //private AlertDialog.Builder mAlertDialog;
    private Reports mConsumeSuccessActivity;
    private Context mContext;

    IAidlPrinter printer;

    public printdetails(Reports consumeSuccessActivity) {
        mConsumeSuccessActivity = consumeSuccessActivity;
        mContext = consumeSuccessActivity;
        try {
            printer = MyApplication.getINSTANCE().getDevice().getPrinter();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void print() {
        getConsumePrintResponse();
    }

    private void getConsumePrintResponse() {
        PrintDataOk();
    }

    private void PrintDataOk() {
        try {
            printer.printBmp(true, false, generateBitmap(), 0, new AidlPrinterListener.Stub() {
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
        return image;
    }

    private Bitmap generateBitmap() {
        CombBitmap combBitmap = new CombBitmap();
        File fi = new File(mContext.getFilesDir(), "logo.bmp");
        Log.i("PRINTDETAILS", "2. LOGO PRINTING: " + ProfileParser.rptshowlogo);
        Log.i("PRINTDETAILS", "2. LOGO EXIST: " + fi.exists());
        if (ProfileParser.rptshowlogo && fi.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
            combBitmap.addBitmap(bitmap);
        }

        // print one line
        combBitmap.addBitmap(GenerateBitmap.generateLine(1));

        //Content
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TERMINAL DETAILS", 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.bnkname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantname, 24, GenerateBitmap.AlignEnum.CENTER, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap(ProfileParser.merchantaddress, 24, GenerateBitmap.AlignEnum.CENTER, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TERMINAL ID:", ProfileParser.tid, 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("MERCHANT ID:", ProfileParser.mid, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("APP VERSION:", APPVERSION, 24, true, false));


        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("COMMS TYPE:", String.valueOf(ProfileParser.comcommstype), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("APN:", String.valueOf(ProfileParser.comapn), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("USERNAME:", String.valueOf(ProfileParser.comgateway), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PASSWORD:", String.valueOf(ProfileParser.compassword), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TCM IP:", String.valueOf(TCMIP), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("TCM PORT:", String.valueOf(TCMPORT), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST NAME 1:", String.valueOf(ProfileParser.hostidname), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST 1 IP:", String.valueOf(ProfileParser.hostip), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST 1 PORT:", String.valueOf(ProfileParser.hostport), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST 1 SSL:", String.valueOf(ProfileParser.hostssl), 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST 2 PORT", String.valueOf(ProfileParser.host2port), 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("HOST 2 SSL:", String.valueOf(ProfileParser.host2ssl), 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("BARCODE:", String.valueOf(ProfileParser.rptshowbarcode), 24, true, false));


        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("CALLHOME INT.:", String.valueOf(ProfileParser.chinterval), 24, true, false));
        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("CALLHOME TIME:", ProfileParser.chremotedownloadtime, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("CUSTOMER TAB:", ProfileParser.rptcustomercopylabel, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("MERCHANT TAB:", ProfileParser.rptmerchantcopylabel, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("FOOTER TAB:", ProfileParser.rptfootertext, 24, true, false));

        combBitmap.addBitmap(GenerateBitmap.str2Bitmap("PTSP:", ProfileParser.ptspname, 24, true, false));


        combBitmap.addBitmap(GenerateBitmap.generateGap(60)); // print row gap

        Bitmap bp = combBitmap.getCombBitmap();

        return bp;
    }








    
}
