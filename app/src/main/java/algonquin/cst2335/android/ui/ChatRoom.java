package algonquin.cst2335.android.ui;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.android.R;
import algonquin.cst2335.android.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.android.databinding.ReceiveMessageBinding;
import algonquin.cst2335.android.databinding.SendMessageBinding;
import algonquin.cst2335.android.ui.ChatMessage;
import algonquin.cst2335.android.ui.ChatMessageDAO;
import algonquin.cst2335.android.ui.ChatRoomViewModel;
import algonquin.cst2335.android.ui.MessageDatabase;
import algonquin.cst2335.android.ui.MessageDetailsFragment;
import algonquin.cst2335.android.R;
public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> theWords;
    ChatRoomViewModel chatModel;

    private RecyclerView.Adapter myAdapter;
    protected EditText theEditText;
    MessageDatabase myDB;
    ChatMessageDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the database
        myDB = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        myDAO = myDB.cmDAO();

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        chatModel.selectedMessage.observe(this, newMessage -> {
            //newMessage is what is posted to the value
            MessageDetailsFragment detailsFragment = new MessageDetailsFragment(newMessage);

            //show the fragment on screen
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.addToBackStack("Doesn't matter which string");
            tx.replace(R.id.fragmentLocation, detailsFragment);
            tx.commit(); //go and do it
        });

        theWords = chatModel.theWords.getValue();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<ChatMessage> allMessages = myDAO.getAllMessages();
            theWords.addAll(allMessages);
        });

        //add all previous messages from database;
        if (theWords == null) {
            chatModel.theWords.postValue(theWords = new ArrayList<>());
        }

        binding.isSendButton.setOnClickListener(click -> {
            String input = binding.editText.getText().toString();
            if (!input.isEmpty()) {
                int type = 1;
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateAndTime = sdf.format(new Date());
                ChatMessage newMessage = new ChatMessage(input, currentDateAndTime, type);
                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    newMessage.id = myDAO.anyFunctionNameForInsertion(newMessage);
                });
                theWords.add(newMessage);
                myAdapter.notifyDataSetChanged();
                binding.editText.setText("");
            }
        });

        binding.receiveButton.setOnClickListener(click -> {
            String input = binding.editText.getText().toString();
            if (!input.isEmpty()) {
                int type = 2;
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateAndTime = sdf.format(new Date());
                ChatMessage newMessage = new ChatMessage(input, currentDateAndTime, type);
                Executor thread2 = Executors.newSingleThreadExecutor();
                thread2.execute(() -> {
                    newMessage.id = myDAO.anyFunctionNameForInsertion(newMessage);
                });
                theWords.add(newMessage);
                myAdapter.notifyDataSetChanged();
                binding.editText.setText("");
            }
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    SendMessageBinding binding = SendMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public int getItemViewType(int position) {
                ChatMessage atThisRow = theWords.get(position);
                return atThisRow.getIsSentButton() == 1 ? 1 : 2;
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage atThisRow = theWords.get(position);
                holder.messageText.setText(atThisRow.getMessage());
                holder.timeText.setText(atThisRow.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return theWords.size();
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(binding.myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this message?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                // Perform the delete operation here
                int selectedItemPosition = -1;
                long messageIdToDelete = -1;
                for (int i = 0; i < theWords.size(); i++) {
                    if (theWords.get(i).isSelected()) {
                        selectedItemPosition = i;
                        messageIdToDelete = theWords.get(i).getId();
                        break;
                    }
                }

                if (selectedItemPosition != -1) {
                    theWords.remove(selectedItemPosition);
                    myAdapter.notifyItemRemoved(selectedItemPosition);

                    // Delete the message from the database
                    ChatMessage messageToDelete = new ChatMessage(); // Create a temporary ChatMessage object
                    messageToDelete.setId(messageIdToDelete); // Set the message id
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        myDAO.deleteMessage(messageToDelete); // Pass the temporary object to the deleteMessage method
                    });
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
            return true;
        } else if (itemId == R.id.action_about) {
            // Show "About" toast message
            Toast.makeText(this, "Version 1.0, created by YourName", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        //This is a row:
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int index = getAbsoluteAdapterPosition();
                chatModel.selectedMessage.postValue(theWords.get(index));
            });

            // This holds the message Text:
            messageText = itemView.findViewById(R.id.messageText);

            // This holds the time text
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}