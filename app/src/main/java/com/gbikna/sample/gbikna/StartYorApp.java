package com.gbikna.sample.gbikna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartYorApp extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent arg1)
    {
        /*try {
            System.out.println("test1");
            if (Intent.ACTION_BOOT_COMPLETED.equals(arg1.getAction())) {
                System.out.println("test2");
                Intent i = new Intent(context, Login.class);
                context.startActivity(i);
                System.out.println("test3/..................");
            }
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            //Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }
}