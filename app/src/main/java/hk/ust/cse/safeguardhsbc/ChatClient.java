package hk.ust.cse.safeguardhsbc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class ChatClient extends AppCompatActivity implements View.OnClickListener {
    Button sendButton;
    EditText messageText;
    ListView messageList;
    MyAdapter myAdapter = null;
    ArrayList<MessageBubble> messageBubbles = null;
    int responseIndex = 0;

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
                    sendMessage();
                    messageBubble = null;
                    messageText.setText("");
                }
                break;

            default:
                break;
        }
    }

    public void sendMessage() {
//        String[] incoming = {"Response 1",
//                "Response 2",
//                "Response 3",
//                "Response 4",
//                "Response 5"};
//
//        if (responseIndex < incoming.length) {
//            MessageBubble messageBubble = new MessageBubble(incoming[responseIndex++], false,  new Date());
            MessageBubble messageBubble = new MessageBubble("Response " + Integer.toString(responseIndex++), false, new Date());
            messageBubbles.add(messageBubble);
            myAdapter.notifyDataSetChanged();
//        }
    }
}

