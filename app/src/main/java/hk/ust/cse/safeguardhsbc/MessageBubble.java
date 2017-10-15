package hk.ust.cse.safeguardhsbc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sang Yeop Jung on 15/10/17.
 */

public class MessageBubble {
    private String message;
    private boolean fromMe;
    private Date date;

    public MessageBubble(String message, boolean fromMe, Date date) {
        this.message = message;
        this.fromMe = fromMe;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean fromMe() {
        return fromMe;
    }

    public void setSelf(boolean fromMe) {
        this.fromMe = fromMe;
    }

    public void setDate(Date date) { this.date = date; }

    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return simpleDateFormat.format(date);
    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }

}