package algonquin.cst2335.android.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import algonquin.cst2335.android.R;

/**
 * This is MainActivity class for a simple password checker app.
 *
 * @author Nikita
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This is a TextView that will display the password status.
     */
    protected TextView theText;

    /**
     * This is a button to trigger password check.
     */
    protected Button myButton;

    /**
     * This is an EditText where the user can type the password.
     */
    protected EditText theEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theText = findViewById(R.id.textView);
        myButton = findViewById(R.id.button);
        theEditText = findViewById(R.id.theEditText);

        myButton.setOnClickListener(click -> {
            String password = theEditText.getText().toString();

            if (checkPasswordComplexity(password)) {
                theText.setText("Your password meets the requirements");
            } else {
                theText.setText("You shall not pass!");
            }
        });
    }

    /**
     * This method checks if the provided password string is complex enough.
     * A complex password must contain at least an upper case letter, a lower case letter, a number, and a special character.
     *
     * @param password the password string to be checked.
     * @return true if the password meets the complexity requirements, false otherwise.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase = false, foundLowerCase = false, foundNumber = false, foundSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) foundUpperCase = true;
            else if (Character.isLowerCase(c)) foundLowerCase = true;
            else if (Character.isDigit(c)) foundNumber = true;
            else if (isSpecialCharacter(c)) foundSpecial = true;

            if (foundUpperCase && foundLowerCase && foundNumber && foundSpecial) return true;
        }

        if (!foundUpperCase)
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
        else if (!foundLowerCase)
            Toast.makeText(this, "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
        else if (!foundNumber)
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
        else if (!foundSpecial)
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();

        return false;
    }

    /**
     * This method checks if the provided character is a special symbol.
     * The special symbols are #$%^&*!@?.
     *
     * @param c the character to be checked.
     * @return true if the character is a special symbol, false otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}