package hk.ust.cse.safeguardhsbc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sang Yeop Jung on 15/10/17.
 */
public class MyAdapter extends ArrayAdapter<MessageBubble> {
    private final Context context;
    private final ArrayList<MessageBubble> messageBubbles;

    public MyAdapter(Context context, ArrayList<MessageBubble> messageBubbles) {
        super(context, R.layout.botmessage, messageBubbles);
        this.context = context;
        this.messageBubbles = messageBubbles;
    }

    // Constructs the ListItem's view from the data obtained from the MessageBubble object
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View messageView = null;

        // Get a reference to the LayoutInflater
        LayoutInflater inflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Change the layout based on who the message is from
        if (messageBubbles.get(position).fromMe()) {
            messageView = inflater.inflate(R.layout.mymessage, parent, false);

            TextView timeView = (TextView) messageView.findViewById(R.id.mytimeTextView);
            timeView.setText(messageBubbles.get(position).getTime());
            TextView msgView = (TextView) messageView.findViewById(R.id.mymessageTextView);
            msgView.setText(messageBubbles.get(position).getMessage());
        } else {
            messageView = inflater.inflate(R.layout.botmessage, parent, false);

            TextView timeView = (TextView) messageView.findViewById(R.id.bottimeTextView);
            timeView.setText(messageBubbles.get(position).getTime());
            TextView msgView = (TextView) messageView.findViewById(R.id.botmessageTextView);
            msgView.setText(messageBubbles.get(position).getMessage());
        }

        return messageView;
    }
}