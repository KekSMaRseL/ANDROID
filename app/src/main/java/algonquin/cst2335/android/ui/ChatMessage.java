package algonquin.cst2335.android.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="Message")
    String message;

    @ColumnInfo(name="TimeSent")
    String timeSent;

    @ColumnInfo(name="IsSendButton")
    int isSendButton;

    public ChatMessage(String message, String timeSent, int type) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSendButton = type;
    }

    public ChatMessage() {}

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public int isSentButton() {
        return isSendButton;
    }
}