package ca.csf.mobile1.tp1.test.matcher;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormatMatcher extends TypeSafeMatcher<View> {

    @StringRes
    private final int stringResourceId;
    private final String expected;

    private TextFormatMatcher(@StringRes int stringResourceId, String expected) {
        super(View.class);
        this.stringResourceId = stringResourceId;
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof TextView)) {
            return false;
        }

        TextView textView = (TextView) target;

        String regex = target.getContext().getResources().getString(stringResourceId);
        regex = regex.replaceAll("\\/", "\\\\/").replaceAll("%s", "(.+?)");

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textView.getText().toString());
        if (matcher.matches()) {
            String actual = matcher.group(1);

            return expected.equals(actual);
        } else {
            return false;
        }

    }


    @Override
    public void describeTo(Description description) {
        description.appendText("with text : ");
        description.appendValue(expected);
    }

    public static org.hamcrest.Matcher<View> withTextInText(@StringRes int stringResourceId, String expected) {
        return new TextFormatMatcher(stringResourceId, expected);
    }

}