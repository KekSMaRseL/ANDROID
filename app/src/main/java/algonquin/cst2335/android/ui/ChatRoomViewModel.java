package algonquin.cst2335.android.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    private MutableLiveData<ArrayList<ChatMessage>> messagesLiveData;

    public MutableLiveData<ArrayList<ChatMessage>> getMessages() {
        if (messagesLiveData == null) {
            messagesLiveData = new MutableLiveData<>();
            messagesLiveData.setValue(new ArrayList<>());
        }
        return messagesLiveData;
    }

    public void addMessage(ChatMessage message) {
        ArrayList<ChatMessage> messages = messagesLiveData.getValue();
        if (messages != null) {
            messages.add(message);
            messagesLiveData.setValue(messages);
        }
    }
}