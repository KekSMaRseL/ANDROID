package algonquin.cst2335.android.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        chatModel.getMessages().observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messages.clear();
                messages.addAll(chatMessages);
                chatAdapter.notifyDataSetChanged();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    ChatMessage chatMessage = new ChatMessage(message, currentDateAndTime, true);
                    chatModel.addMessage(chatMessage);
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
                    chatModel.addMessage(chatMessage);
                    editText.setText("");
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatAdapter = new ChatAdapter(messages, new ChatAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showDeleteConfirmationDialog(position);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteMessage(int position) {
        ChatMessage deletedMessage = messages.remove(position);
        chatAdapter.notifyItemRemoved(position);
        chatModel.deleteMessage(deletedMessage);
        showUndoSnackbar(deletedMessage, position);
    }

    private void showUndoSnackbar(final ChatMessage message, final int position) {
        Snackbar.make(recyclerView, "Message deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDelete(message, position);
                    }
                })
                .show();
    }

    private void undoDelete(ChatMessage message, int position) {
        messages.add(position, message);
        chatAdapter.notifyItemInserted(position);
        chatModel.addMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatModel.setMessages(messages);
    }
}