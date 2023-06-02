package algonquin.cst2335.android.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import algonquin.cst2335.android.data.MainViewModel;
import algonquin.cst2335.android.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.myCheckbox.setOnCheckedChangeListener((a, b) -> {
            model.isCheckboxChecked.postValue(b);
        });

        variableBinding.mySwitch.setOnCheckedChangeListener((a, b) -> {
            model.isSwitchChecked.postValue(b);
        });

        variableBinding.myRadioButton.setOnCheckedChangeListener((a, b) -> {
            model.isRadioChecked.postValue(b);
        });

        model.isCheckboxChecked.observe(this, aBoolean -> {
            variableBinding.myCheckbox.setChecked(aBoolean);
            Toast.makeText(MainActivity.this, "Checkbox value is now: " + aBoolean, Toast.LENGTH_SHORT).show();
        });

        model.isSwitchChecked.observe(this, aBoolean -> {
            variableBinding.mySwitch.setChecked(aBoolean);
            Toast.makeText(MainActivity.this, "Switch value is now: " + aBoolean, Toast.LENGTH_SHORT).show();
        });

        model.isRadioChecked.observe(this, aBoolean -> {
            variableBinding.myRadioButton.setChecked(aBoolean);
            Toast.makeText(MainActivity.this, "Radio value is now: " + aBoolean, Toast.LENGTH_SHORT).show();
        });

        model.theText.observe(this, s -> {
            variableBinding.theText.setText(s);
        });

        variableBinding.theButton.setOnClickListener(click -> {
            model.theText.postValue(variableBinding.theEditText.getText().toString());
        });

        variableBinding.myImageButton.setOnClickListener(v -> {
            int width = v.getWidth();
            int height = v.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}