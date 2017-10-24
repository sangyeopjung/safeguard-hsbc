package hk.ust.cse.safeguardhsbc;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import hk.ust.cse.safeguardhsbc.HttpUtility.PutUtility;

public class ChatClient extends AppCompatActivity implements View.OnClickListener {
    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyAdapter myAdapter = null;
    ArrayList<MessageBubble> messageBubbles = null;

    //private final String URL = "http://10.89.220.20:10007";
    private final String URL = "http://192.168.0.199:10007";
    private String ID;
    private State state;

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

        // TODO: Should be carried over from login intent
        ID = "543210";
        state = State.MAIN;
        sendMessage("Welcome to HSBC!");
        sendMessage(state);
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

                    state = update(state, message);
                    // Update customer information
                    sendMessage(state);

                    //Reset
                    messageBubble = null;
                    messageText.setText("");
                }
                break;

            default:
                break;
        }
    }

    private State update(State st, String msg) {
        switch(st) {
            case PHONE:
                updatePhone(msg);
                break;
            case ADDRESS:
                updateAddress(msg);
                break;
            case EMAIL:
                updateEmail(msg);
                break;
            default:
                try {
                    return State.values()[Integer.parseInt(msg)];
                } catch (Exception e) {
                    sendMessage("Invalid operation. Please re-enter.");
                }
                break;
        }
        return State.MAIN;
    }

    private void sendMessage(State st) {
        String msg;
        switch(st) {
            case MAIN:
                msg = "Please select an operation:\n" +
                        "1. Update phone number\n" +
                        "2. Update residential address\n" +
                        "3. Update email address";
                break;
            case PHONE:
                msg = "Please input your new phone number";
                break;
            case ADDRESS:
                msg = "Please input your new residential address";
                break;
            case EMAIL:
                msg = "Please input your new email address";
                break;
            default:
                msg = "Invalid operation. Please re-enter.";
                break;
        }
        sendMessage(msg);
    }

    private void sendMessage(String msg) {
        MessageBubble messageBubble = new MessageBubble(msg, false, new Date());
        messageBubbles.add(messageBubble);
        myAdapter.notifyDataSetChanged();
    }

    private class UpdateAsyncTask extends AsyncTask<String, Void, String> {
        String dataToUpdate;
        public UpdateAsyncTask(String dataToUpdate) {
            this.dataToUpdate = dataToUpdate;
        }

        @Override
        protected String doInBackground(String... params) {
            PutUtility putUtility;
            String response = "";
            try {
                putUtility = new PutUtility(URL + "/api/account/updateAccount");
                putUtility.setContentType("application/json");
                putUtility.setRequestBody("{ \"id\" : \"" + ID + "\", \"" + dataToUpdate + "\" : \""+ params[0] + "\" }");
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

    public void updatePhone(String phone){
        String response;
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask("phoneNumber");
        try {
            response = updateAsyncTask.execute(phone).get();
        } catch (Exception e) {
            response = "Failed to update your phone number";
            e.printStackTrace();
        }

        sendMessage(response);
    }

    public void updateAddress(String address) {
        String response;
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask("residentialAddress");
        try {
            response = updateAsyncTask.execute(address).get();
        } catch (Exception e) {
            response = "Failed to update your residential address";
            e.printStackTrace();
        }

        sendMessage(response);
    }

    public void updateEmail(String email) {
        String response;
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask("email");
        try {
            response = updateAsyncTask.execute(email).get();
        } catch (Exception e) {
            response = "Failed to update your email address";
            e.printStackTrace();
        }

        sendMessage(response);
    }
}

