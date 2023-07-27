package algonquin.cst2335.android.ui;


import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.android.databinding.MessageDetailsLayoutBinding;


public class MessageDetailsFragment extends Fragment {

    ChatMessage thisMessage;

    public MessageDetailsFragment(ChatMessage toShow)
    {
        thisMessage = toShow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        MessageDetailsLayoutBinding binding = MessageDetailsLayoutBinding.inflate(inflater);

        binding.messageText.setText(thisMessage.message);
        binding.timeText.setText(thisMessage.timeSent);
        binding.idText.setText(Long.toString(thisMessage.id));


        return binding.getRoot();
    }
}

