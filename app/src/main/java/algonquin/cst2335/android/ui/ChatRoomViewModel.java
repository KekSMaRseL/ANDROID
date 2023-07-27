package algonquin.cst2335.android.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.android.ui.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> theWords = new MutableLiveData< >();

    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData< >();

}