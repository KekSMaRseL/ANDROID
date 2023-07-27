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

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
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

    protected EditText theEditText;

    MessageDatabase myDB;
    ChatMessageDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
//                //even
//                if (position % 2 == 0)
//                    return 1;
//                else //odd
//                    return 2;
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                //updates the widgets
                ChatMessage atThisRow = theWords.get(position);

                //puts the string in position at theWords TextView
                holder.messageText.setText(atThisRow.message);

                holder.timeText.setText(atThisRow.timeSent);
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


               /* AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question:")
                .setPositiveButton("YES", (dlg, which) -> {


                //what is the index?
                int index = getAbsoluteAdapterPosition();
                ChatMessage toDelete = theWords.get(index);


                Executor thread1 = Executors.newSingleThreadExecutor();
                thread1.execute(() -> {
                    myDAO.deleteThisChatMessage(toDelete);
                    theWords.remove(index); // remove from our array list
                    runOnUiThread(() -> { myAdapter.notifyDataSetChanged();});


                    Snackbar.make (messageText , "Deleted your message #" +index, Snackbar.LENGTH_LONG)
                            .setAction("UNDO", clk-> {
                                Executor myThread = Executors.newSingleThreadExecutor();
                                myThread.execute(()-> {
                                    myDAO.anyFunctionNameForInsertion(toDelete);
                                    theWords.add(index, toDelete);
                                    runOnUiThread(() -> myAdapter.notifyDataSetChanged());
                                });

                            })
                            .show();
                });

                })
                .setNegativeButton("NO", (dl, wh) -> { })
                .create().show();
                */
            });

            // THis holds the message Text:
            messageText = itemView.findViewById(R.id.messageText);

            //This holds the time text
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
