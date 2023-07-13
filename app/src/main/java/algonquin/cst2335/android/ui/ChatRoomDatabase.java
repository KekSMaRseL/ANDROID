package algonquin.cst2335.android.ui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class ChatRoomDatabase extends RoomDatabase {

    private static volatile ChatRoomDatabase instance;

    public abstract ChatMessageDAO chatMessageDao();

    public static synchronized ChatRoomDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ChatRoomDatabase.class, "chat_database")
                    .build();
        }
        return instance;
    }
}