package algonquin.cst2335.android.ui;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert
    public long anyFunctionNameForInsertion(ChatMessage messageToInsert);

    @Query("Select * from ChatMessage")
    public List<ChatMessage> getAllMessages();

    @Delete
    public int deleteThisChatMessage(ChatMessage cm);

    @Delete
    void deleteMessage(ChatMessage chatMessage);
}