package algonquin.cst2335.android.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import algonquin.cst2335.android.R;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests whether the application correctly identifies a password missing uppercase, lowercase, and special characters.
     */
    @Test
    public void testMissingUppercaseLowercaseAndSpecialCharacter() {
        executePasswordCheck("12345", "You shall not pass!");
    }

    /**
     * Tests whether the application correctly identifies a password missing an uppercase character.
     */
    @Test
    public void testMissingUppercase() {
        executePasswordCheck("abcdefg1#", "You shall not pass!");
    }

    /**
     * Tests whether the application correctly identifies a password missing a lowercase character.
     */
    @Test
    public void testMissingLowercase() {
        executePasswordCheck("ABCDEFG1#", "You shall not pass!");
    }

    /**
     * Tests whether the application correctly identifies a password missing a numeric character.
     */
    @Test
    public void testMissingNumeric() {
        executePasswordCheck("Abcdefg#", "You shall not pass!");
    }

    /**
     * Tests whether the application correctly identifies a password missing a special character.
     */
    @Test
    public void testMissingSpecialCharacter() {
        executePasswordCheck("Abcdefg1", "You shall not pass!");
    }

    /**
     * Tests whether the application correctly identifies a password that has all requirements.
     */
    @Test
    public void testHasAllRequirements() {
        executePasswordCheck("Abcdefg1#", "Your password meets the requirements");
    }

    /**
     * Executes the password check with the provided password and expected result.
     *
     * @param password The password to check.
     * @param expectedText The expected result text.
     */
    private void executePasswordCheck(String password, String expectedText) {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.theEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText(password), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.button), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView), withText(expectedText),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText(expectedText)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}