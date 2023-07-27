package algonquin.cst2335.android.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    // Other fields and methods...

    public void setId(long id) {
        this.id = id;
    }


    @ColumnInfo(name = "message")
    String message;

    @ColumnInfo(name = "timeSent")
    String timeSent;

    @ColumnInfo(name = "isSentButton")
    int isSentButton;


    // Placeholder fields for message selection status
    boolean selected;

    public ChatMessage(String message, String timeSent, int type) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = type;
    }

    public ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public int getIsSentButton() {
        return isSentButton;
    }

    public long getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}