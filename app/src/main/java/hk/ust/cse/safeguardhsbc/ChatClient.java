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
    String URL = "http://10.89.220.20:10007";

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
                    messageBubble = null;
                    messageText.setText("");
                }
                break;

            default:
                break;
        }
    }

    public class UpdatePhoneAsyncTask extends AsyncTask<String, Void, String> {
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
    }
}

