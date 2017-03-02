package ca.csf.mobile1.tp1.activity;

import android.support.annotation.StringRes;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ca.csf.mobile1.tp1.R;
import ca.csf.mobile1.tp1.test.action.OrientationChangeAction;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.csf.mobile1.tp1.test.matcher.TextFormatDoubleMatcher.withDoubleInText;
import static ca.csf.mobile1.tp1.test.matcher.TextFormatMatcher.withTextInText;


@LargeTest
public class MainActivityTest {

    //<editor-fold desc="Unit Tests Preparation">

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    //</editor-fold>

    @Test
    public void askForChemicalElementAtStart() throws Exception {
        assertMessage(R.string.hint_chemical_formula);
    }

    @Test
    public void canComputeChemicalCompoundWeight1() throws Exception {
        writeTextToApplication("H2O");

        assertOutput(18.01528D);
    }

    @Test
    public void canComputeChemicalCompoundWeight2() throws Exception {
        writeTextToApplication("NaCl");

        assertOutput(58.4428D);
    }

    @Test
    public void canComputeChemicalCompoundWeight3() throws Exception {
        writeTextToApplication("(NaCl)4");

        assertOutput(233.7711D);
    }

    @Test
    public void canComputeChemicalCompoundWeight4() throws Exception {
        writeTextToApplication("(H2SO4(Be)3(H2O))2");

        assertOutput(286.2606D);
    }

    @Test
    public void cannotComputeEmptyChemicalCompoundFormula() throws Exception {
        writeTextToApplication("");

        assertError(R.string.text_empty_formula);
    }

    @Test
    public void cannotComputeChemicalCompoundFormulaWithOnlyWhitespaces() throws Exception {
        writeTextToApplication("      ");

        assertError(R.string.text_empty_formula);
    }

    @Test
    public void cannotComputeChemicalCompoundFormulaWithOnlyTabs() throws Exception {
        writeTextToApplication("\t\t");

        assertError(R.string.text_empty_formula);
    }

    @Test
    public void cannotComputeChemicalCompoundWithIllegalCharacter() throws Exception {
        writeTextToApplication("H2$");

        assertError(R.string.text_illegal_character, "$");
    }

    @Test
    public void cannotComputeChemicalCompoundWithMisplacedExponent() throws Exception {
        writeTextToApplication("2H2O");

        assertError(R.string.text_misplaced_exponent);
    }

    @Test
    public void cannotComputeChemicalCompoundWithEmptyParenthesis() throws Exception {
        writeTextToApplication("H2()O");

        assertError(R.string.text_empty_parenthesis);
    }

    @Test
    public void cannotComputeChemicalCompoundWithUnknownChemicalElement() throws Exception {
        writeTextToApplication("Hu2");

        assertError(R.string.text_unknown_chemical_element, "Hu");
    }

    @Test
    public void cannotComputeChemicalCompoundWithIllegalClosingParenthesis() throws Exception {
        writeTextToApplication("H2O)");

        assertError(R.string.text_illegal_closing_parenthesis);
    }

    @Test
    public void cannotComputeChemicalCompoundWithMissingClosingParenthesis() throws Exception {
        writeTextToApplication("(H2O");

        assertError(R.string.text_missing_closing_parenthesis);
    }

    @Test
    public void orientationChangeDoesNotResetInputAndOutput() throws Exception {
        writeTextToApplication("H2O");

        assertInput("H2O");
        assertOutput(18.01528D);

        changeToOrientationLandscape();

        assertInput("H2O");
        assertOutput(18.01528D);

        changeToOrientationPortrait();

        assertInput("H2O");
        assertOutput(18.01528D);
    }

    //<editor-fold desc="Unit Tests Tools">

    private void writeTextToApplication(String text) {
        onView(withId(R.id.inputEditText)).perform(typeText(text));
        closeSoftKeyboard();
        onView(withId(R.id.computeButton)).perform(click());
    }

    private void assertMessage(@StringRes int stringResourceId) throws Exception {
        onView(ViewMatchers.withId(R.id.inputEditText)).check(matches(withHint(stringResourceId)));
    }

    private void assertInput(String text) {
        onView(withId(R.id.inputEditText)).check(matches(withText(text)));
    }

    private void assertOutput(double expected) {
        onView(withId(R.id.outputTextView)).check(matches(withDoubleInText(R.string.text_output, expected, 0.01D)));
    }

    private void assertError(@StringRes int stringResourceId) {
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText(stringResourceId)));
    }

    private void assertError(@StringRes int stringResourceId, String expected) {
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withTextInText(stringResourceId, expected)));
    }

    private void changeToOrientationLandscape() {
        onView(withId(R.id.rootView)).perform(OrientationChangeAction.changeToOrientationLandscape());
    }

    private void changeToOrientationPortrait() {
        onView(withId(R.id.rootView)).perform(OrientationChangeAction.changeToOrientationPortrait());
    }

    //</editor-fold>

}
