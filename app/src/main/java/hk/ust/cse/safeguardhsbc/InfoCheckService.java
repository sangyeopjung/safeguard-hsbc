package hk.ust.cse.safeguardhsbc;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class InfoCheckService extends Service {
    int DataNetworkType;
    int PhoneType;
    String SimOperator;
    String NetworkOperator;
    String NetworkOperatorName;

    String RegisteredAddress;
    String[] HourlyAddresses;

    public InfoCheckService() {
        HourlyAddresses = new String[14*24];
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //DataNetworkType = tm.getDataNetworkType();
        PhoneType = tm.getPhoneType();
        SimOperator = tm.getSimOperator();
        NetworkOperator = tm.getNetworkOperator();
        NetworkOperatorName = tm.getNetworkOperatorName();
        Log.d("Background Thread","Initiating");
        Log.d("Background Thread","Initial Info: (PhoneType: "+PhoneType+", SimOperator: "+SimOperator+", NetworkOperator: "+NetworkOperator+", NetworkOperatorName: "+NetworkOperatorName+")");

        new Thread() {
            public void run() {
                StartInfoCheck();
            }
        }.start();

        return START_STICKY;
    }

    public void StartInfoCheck() {
        while(true) {
            Log.d("Background Thread","Loop Running");
            Log.d("Background Thread","Initial Info: (PhoneType: "+PhoneType+", SimOperator: "+SimOperator+", NetworkOperator: "+NetworkOperator+", NetworkOperatorName: "+NetworkOperatorName+")");

            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            int newPhoneType = tm.getPhoneType();
            String newSimOperator = tm.getSimOperator();
            String newNetworkOperator = tm.getNetworkOperator();
            String newNetworkOperatorName = tm.getNetworkOperatorName();

            Log.d("Background Thread","New Info: (PhoneType: "+newPhoneType+", SimOperator: "+newSimOperator+", NetworkOperator: "+newNetworkOperator+", NetworkOperatorName: "+newNetworkOperatorName+")");


            if(PhoneType != newPhoneType ||
                    !SimOperator.equals(newSimOperator) ||
                    !NetworkOperator.equals(newNetworkOperator) ||
                    !NetworkOperatorName.equals(newNetworkOperatorName)) {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_hsbc_text)
                                .setContentTitle("Phone Number Change Detected")
                                .setContentText("Hello Sung Min, Have you changed your phone number?");

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());

                PhoneType = newPhoneType;
                SimOperator = newSimOperator;
                NetworkOperator = newNetworkOperator;
                NetworkOperatorName = newNetworkOperatorName;
            }
            else {
                // Testing Purpose
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_hsbc_text)
                                .setContentTitle("No Change Detected")
                                .setContentText("Hi there");

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());

            }

            try {
                    Thread.sleep(5*1000);
            }
            catch(Exception e) {
                Log.d("Background Service","Exception occured while running \"sleep\".");
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
