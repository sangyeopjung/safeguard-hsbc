package hk.ust.cse.safeguardhsbc;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import hk.ust.cse.safeguardhsbc.HttpUtility.PutUtility;

public class ChatClient extends AppCompatActivity implements View.OnClickListener {
    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyAdapter myAdapter = null;
    ArrayList<MessageBubble> messageBubbles = null;
    String URL = "http://10.89.220.20:10010";
    String ID = "A123456(7)";
    int responseIndex = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);

        messageText = (EditText) findViewById(R.id.messageText);

        // Get the reference to the ListView on the UI
        messageList = (ListView) findViewById(R.id.messageList);

        // Create a new ArrayList of MessageBubble objects
        messageBubbles = new ArrayList<MessageBubble>();

        // Create a new custom ArrayAdapter
        myAdapter = new MyAdapter(this, messageBubbles);

        // Set the ListView's adapter to be the adapter that we just constructed.
        messageList.setAdapter((ListAdapter) myAdapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //sendMessage("Hi Stitt! Have you changed your phone number?\n\t\t1. Yes\n\t\t2. No");
        //sendMessage("\t\t1. Yes\n\t\t2. No");

        sendMessage("Hi, David! How shall I help you?");
        sendMessage("1. Update Information \n2. Check Information");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendButton:
                String message = messageText.getText().toString();
                // If the message is not empty string
                if (!message.equals("")) {
                    // Create a new MessageBubble object and initialize it with the information
                    MessageBubble messageBubble = new MessageBubble(message, true, new Date());

                    // Add the MessageBubble object to the ArrayList
                    messageBubbles.add(messageBubble);

                    // Notify the adapter that the data has changed due to the addition
                    // of a new messageBubble object. This triggers an update of the ListView
                    myAdapter.notifyDataSetChanged();
                    updatePhoneNum(messageText.getText().toString());
                    switch(responseIndex++){

                        case 0:
                            sendMessage("Which type of information do you want to update?\n\t\t1. Correspondence Address \n" +
                                    "\t\t2. Residential Address \n" +
                                    "\t\t3. Phone number \n" +
                                    "\t\t4. E-mail");
                            break;
                        case 1:
                            sendMessage("Your current address is " + getAddress(ID));
                                    //123S, UG Hall 7, HKUST, Clear Water Bay, Kowloon, HK"
                            sendMessage(
    "Please type your new address following the below format.\nRoom, Flat, Floor, Building, Street, District, Region");

    break;
    case 2:
    sendMessage("Your updated address is now:\n" + getAddress(ID));
    sendMessage("Do you want to update other information? \n\t\t1. Yes 2. No");
    break;
    case 3:
    sendMessage("Which type of information do you want to update?\n\t\t1. Correspondence Address \n" +
                        "\t\t2. Residential Address \n" +
                        "\t\t3. Phone number \n" +
                        "\t\t4. E-mail");
    break;
    case 4:
    sendMessage("Your current phone number is 22333000");
    sendMessage(
            "Please type your new phone number without '-'.");

    break;
    case 5:
    sendMessage("Your updated phone number is now 88888888");
    sendMessage("Do you want to update other information? \n\t\t1. Yes 2. No");
    break;
    case 6:
    sendMessage("Thank you for playing your part in the fight against financial crime.");
    break;

}
messageBubble = null;
        messageText.setText("");
        }
        break;

default:
        break;
        }
        }

private class GetAddressAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        PutUtility putUtility;
        String response = "";
        try {
            putUtility = new PutUtility(URL + "/api/account/getAccount");
            String requestBody = "{\"id\" : \"" + ID + "\"}";
            putUtility.setContentType("application/json");
            putUtility.setRequestBody(requestBody);
            response = putUtility.getResponse();
            JSONObject jsonObj = new JSONObject(response);
            response = jsonObj.optString("residentialAddress");
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}

    public String getAddress(String ID) {
        String response = "";
        GetAddressAsyncTask getAddressAsyncTask = new GetAddressAsyncTask();
        try {
            response = getAddressAsyncTask.execute(ID).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ("".equals(response))
            sendMessage("Wrong ID number!!");

        return response;
    }

private class UpdatePhoneAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        PutUtility putUtility;
        String response = "";
        try {
            putUtility = new PutUtility(URL + "/api/account/updateAccount");
            putUtility.setContentType("application/json");
            putUtility.setRequestBody("{ \"id\" : \"543210\", \"phoneNumber\" : \"43214321\" }");
            response = putUtility.getResponse();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}

    public void updatePhoneNum(String pNum){
        String response = "";
        UpdatePhoneAsyncTask updatePhoneAsyncTask = new UpdatePhoneAsyncTask();
        try {
            response = updatePhoneAsyncTask.execute("asd").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(! "".equals(response))
            sendMessage(response);
        else
            sendMessage("Failed to update your phone number");
    }

    public void updateAddress(String address) {

    }

    public void updateEmail(String email) {

    }

    public void sendMessage(String msg) {
        MessageBubble messageBubble = new MessageBubble(msg, false, new Date());
        messageBubbles.add(messageBubble);
        myAdapter.notifyDataSetChanged();
/*
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentTitle("Phone Number Change Detected")
                        .setContentText("Hi Stitt! Have you changed your phone number?");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

        */
    }

   /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ChatClient Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

