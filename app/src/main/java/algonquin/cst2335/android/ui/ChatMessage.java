package algonquin.cst2335.android.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_messages")
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time_sent")
    private String timeSent;

    @ColumnInfo(name = "is_sent_button")
    private boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}