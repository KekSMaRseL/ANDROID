package algonquin.cst2335.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.android.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyRowHolder> {
    private ArrayList<ChatMessage> messages;

    public ChatAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.send_message, parent, false);
        } else {
            view = inflater.inflate(R.layout.receive_message, parent, false);
        }
        return new MyRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        holder.messageText.setText(chatMessage.getMessage());
        holder.timeText.setText(chatMessage.getTimeSent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = messages.get(position);
        if (chatMessage.isSentButton()) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;

        public MyRowHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
