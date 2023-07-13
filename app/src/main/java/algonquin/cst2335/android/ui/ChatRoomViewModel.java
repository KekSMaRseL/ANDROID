package algonquin.cst2335.android.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomViewModel extends AndroidViewModel {

    private MutableLiveData<List<ChatMessage>> messagesLiveData;

    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        messagesLiveData = new MutableLiveData<>();
        messagesLiveData.setValue(new ArrayList<>());
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messagesLiveData;
    }

    public void setMessages(List<ChatMessage> messages) {
        messagesLiveData.setValue(messages);
    }

    public void addMessage(ChatMessage message) {
        List<ChatMessage> messages = messagesLiveData.getValue();
        if (messages != null) {
            messages.add(message);
            messagesLiveData.setValue(messages);
        }
    }

    public void deleteMessage(ChatMessage message) {
        List<ChatMessage> messages = messagesLiveData.getValue();
        if (messages != null) {
            messages.remove(message);
            messagesLiveData.setValue(messages);
        }
    }
}