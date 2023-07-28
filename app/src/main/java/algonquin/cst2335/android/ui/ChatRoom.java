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
import androidx.appcompat.widget.Toolbar;
import android.inputmethodservice.Keyboard;
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
import algonquin.cst2335.android.ui.ChatRoomViewModel;

public class ChatRoom extends AppCompatActivity {


    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> theWords;
    ChatRoomViewModel chatModel;

    //ArrayList<ChatMessage> theWords;

    private RecyclerView.Adapter myAdapter;
    private ChatMessage selectedMessage = null;
    protected EditText theEditText;

    MessageDatabase myDB;
    ChatMessageDAO myDAO;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            if (selectedMessage != null) {
                new AlertDialog.Builder(ChatRoom.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete the selected chat message?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                myDAO.deleteThisChatMessage(selectedMessage);
                                theWords.remove(selectedMessage);
                                runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                            });
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            } else {
                Toast.makeText(ChatRoom.this, "No chat message is selected to delete.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        if (id == R.id.action_about) {
            Toast.makeText(ChatRoom.this, "This is the About Toast Message", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);
        
        myDB = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        myDAO = myDB.cmDAO(); // the only function in my MessageDatabase


        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        chatModel.selectedMessage.observe(this, newMessage -> {
            //newMessage is what is posted to the value

            {


                MessageDetailsFragment detailsFragment = new MessageDetailsFragment(newMessage);

                //show the fragment on screen
                FragmentManager fMgr = getSupportFragmentManager();


                FragmentTransaction tx = fMgr.beginTransaction();
                tx.addToBackStack("Doesn't matter which string");
                tx.replace(R.id.fragmentLocation, detailsFragment);
                tx.commit(); //go and do it
            }
        });



        theWords = chatModel.theWords.getValue();

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<ChatMessage> allMessages = myDAO.getAllMessages();
            theWords.addAll(allMessages);
        });


        //add all previous messages from database;

        if (theWords == null) {
            chatModel.theWords.postValue(theWords = new ArrayList<ChatMessage>());


        }

        //theWords = chatModel.theWords;

        binding.isSendButton.setOnClickListener(click -> {
            String input = binding.editText.getText().toString();

            if (!input.isEmpty()) {

                int type = 1;

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateAndTime = sdf.format(new Date());

                //messages.add(binding.textInput.getText().toString());
                //myAdapter.notifyItemInserted( messages.size()-1 );


                ChatMessage newMessage = new ChatMessage(input, currentDateAndTime, type);


                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    newMessage.id = myDAO.anyFunctionNameForInsertion(newMessage);
                });
                //this runs in another thread

                //add to database

                //insert into ArrayList
                theWords.add(newMessage);

                //updates the row
                myAdapter.notifyDataSetChanged();

                //clear the previous text
                binding.editText.setText("");
            }

        });

        binding.receiveButton.setOnClickListener(click -> {
            String input = binding.editText.getText().toString();

            if (!input.isEmpty()) {

                int type = 2;

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                String currentDateAndTime = sdf.format(new Date());

                //messages.add(binding.textInput.getText().toString());
                //myAdapter.notifyItemInserted( messages.size()-1 );

                ChatMessage newMessage = new ChatMessage(input, currentDateAndTime, type);
                //insert into ArrayList

                Executor thread2 = Executors.newSingleThreadExecutor();
                thread2.execute(() -> {
                    newMessage.id = myDAO.anyFunctionNameForInsertion(newMessage);
                });
                theWords.add(newMessage);

                //updates the row
                myAdapter.notifyDataSetChanged();

                //clear the previous text
                binding.editText.setText("");
            }

        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //this inflates the row layout

                //int viewType is what layout to load
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
                if (atThisRow.isSentButton() == 1)
                    return 1;
                else
                    return 2;

            }




            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                //updates the widgets
                ChatMessage atThisRow = theWords.get(position);

                //puts the string in position at theWords TextView
                holder.messageText.setText(atThisRow.message);

                holder.timeText.setText(atThisRow.timeSent);

                // Add this
                holder.itemView.setOnClickListener(v -> {
                    if (selectedMessage == atThisRow) {
                        selectedMessage = null; // deselect the message
                    } else {
                        selectedMessage = atThisRow; // select the message
                    }
                });
            }

            @Override
            public int getItemCount() {
                return theWords.size();
            }

        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }


    protected class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        //This is a row:
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {

                int index = getAbsoluteAdapterPosition();
                chatModel.selectedMessage.postValue( theWords.get(index) );


            });

            // THis holds the message Text:
            messageText = itemView.findViewById(R.id.messageText);

            //This holds the time text
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}