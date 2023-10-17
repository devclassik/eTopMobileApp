package com.gbikna.sample.gbikna.util.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.gbikna.sample.gbikna.util.utilities.ProfileParser;
import com.gbikna.sample.gbikna.util.utilities.Utilities;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.APPVERSION;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.BRAND;
import static com.gbikna.sample.gbikna.util.utilities.ProfileParser.MODEL;
import static com.gbikna.sample.gbikna.util.utilities.Utils.TCMIP;
import static com.gbikna.sample.gbikna.util.utilities.Utils.TCMPORT;


public class UpgradeUtil {
    private static final String TAG = "UpgradeUtil";
    public static void getAppLastVersion(Context context,final String packageName, final QueryListener listener){//Context context
        /*String carrierNumber = getProp(context,"ro.tms.name","");
        String subCarrierNumber = getProp(context,"ro.tms.subname","");
        String hostName = getProp(context,"ro.tms.url","app.topwisesz.com");
        String hostPort = getProp(context,"ro.tms.port","9090");
        String ProductModel = getProp(context,"ro.product.model","");
        String SnNumber = getProp(context,"sys.pos.boot_sn","");
        if(TextUtils.isEmpty(SnNumber)){
            SnNumber = Build.SERIAL;
        }

        String url = "http://"+hostName+":"+hostPort+"/appmarket/app/findlastversion";
        if(TextUtils.isEmpty(carrierNumber)||TextUtils.isEmpty(subCarrierNumber)
                ||TextUtils.isEmpty(hostName)||TextUtils.isEmpty(hostPort)
                ||TextUtils.isEmpty(packageName)||TextUtils.isEmpty(SnNumber)
                ||TextUtils.isEmpty(ProductModel)){
            try {
                if (listener != null) listener.onFailure(packageName, "Params is empty");
            }catch (Exception e2){
                e2.printStackTrace();
            }
            return;
        }*/

        String url = "http://"+ TCMIP + ":" + TCMPORT + "/api/profile/download/" + ProfileParser.tid;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url)
                .addHeader("brand",BRAND)
                .addHeader("model",MODEL)
                .addHeader("serial", Utilities.getSerialNumber())
                .addHeader("appversion",APPVERSION)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    if (listener != null) listener.onFailure(packageName, e.getMessage());
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String info =  response.body().string();
                try {
                    if (listener != null) listener.onResponse(packageName,info);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private static String getProp(Context context, String key, String defValue) {
        String ret = "";
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[1];
            params[0] = new String(key);
            ret = (String) get.invoke(SystemProperties, params);
            if(TextUtils.isEmpty(ret)){
                ret = defValue;
            }
        } catch (Exception e) {
            ret = defValue;
        }
        return ret;
    }

    public static void downloadApk(Context context,String fromUrl, final String saveToPath, final DownloadListener listener){
        Log.i(TAG, "WISDOM: 1. " + fromUrl);
        Log.i(TAG, "WISDOM: 2. " + saveToPath);
        FileDownloader.setup(context);
        BaseDownloadTask downloadTask = FileDownloader.getImpl().create(fromUrl);
        downloadTask.setPath(saveToPath, false);
        downloadTask.setCallbackProgressTimes(300);//300
        downloadTask.setMinIntervalUpdateSpeed(400);
        downloadTask.setForceReDownload(true);
        downloadTask.setAutoRetryTimes(10);//Wasnt d before
        downloadTask.setListener(new FileDownloadSampleListener(){
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.pending(task, soFarBytes, totalBytes);
                Log.i(TAG, "WISDOM PENDING: " + soFarBytes + " - " + totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                Log.i(TAG, "WISDOM: CURRENT-BYTE - " + soFarBytes);
                Log.i(TAG, "WISDOM: TOTAL-BYTE - " + totalBytes);
                try {
                    int progress = (int)(soFarBytes*100.0/totalBytes);
                    if(listener!=null)listener.onProgress(saveToPath,progress,100);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i(TAG, "WISDOM EXCEPTION: " + e.getMessage());
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                Log.i(TAG, "WISDOM ERROR");
                try {
                    Log.i(TAG, "WISDOM EXCEPTION: " + e.getMessage());
                    e.printStackTrace();
                    if(listener!=null)listener.onFailure(saveToPath,e.getMessage());
                }catch (Exception e2){
                    e2.printStackTrace();
                    Log.i(TAG, "WISDOM EXCEPTION: " + e2.getMessage());
                }
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                Log.i(TAG, "WISDOM CONNECTED: " + isContinue + " - " + soFarBytes  + " - " + totalBytes);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                Log.i(TAG, "WISDOM PAUSED: " + soFarBytes + " - " + totalBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                Log.i(TAG, "WISDOM COMPLETED");
                try {
                    if(listener!=null)listener.onCompleted(saveToPath);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i(TAG, "WISDOM EXCEPTION: " + e.getMessage());
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                super.warn(task);
                Log.i(TAG, "WISDOM WARN");
            }
        });
        downloadTask.start();
    }
}
