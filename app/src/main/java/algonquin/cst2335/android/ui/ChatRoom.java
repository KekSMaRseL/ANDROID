package algonquin.cst2335.android.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.android.R;

public class ChatRoom extends AppCompatActivity {

    private ArrayList<ChatMessage> messages;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText editText;
    private Button sendButton;
    private Button receiveButton;
    private ChatRoomViewModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);

        messages = new ArrayList<>();
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        ArrayList<ChatMessage> savedMessages = chatModel.getMessages().getValue();
        if (savedMessages != null) {
            messages.addAll(savedMessages);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(message, currentDateAndTime, true);
                    messages.add(chatMessage);
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    editText.setText("");
                }
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(message, currentDateAndTime, false);
                    messages.add(chatMessage);
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    editText.setText("");
                }
            }
        });

        chatModel.getMessages().setValue(messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatModel.getMessages().setValue(messages);
    }
}
