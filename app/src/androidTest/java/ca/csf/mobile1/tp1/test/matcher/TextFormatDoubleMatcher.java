package ca.csf.mobile1.tp1.test.matcher;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormatDoubleMatcher extends TypeSafeMatcher<View> {

    @StringRes
    private final int stringResourceId;
    private final double expected;
    private final double delta;

    private TextFormatDoubleMatcher(@StringRes int stringResourceId, double expected, double delta) {
        super(View.class);
        this.stringResourceId = stringResourceId;
        this.expected = expected;
        this.delta = delta;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof TextView)) {
            return false;
        }

        TextView textView = (TextView) target;

        String regex = target.getContext().getResources().getString(stringResourceId);
        regex = regex.replaceAll("\\/", "\\\\/").replaceAll("%s", ".+?").replaceAll("%f", "(\\\\d+[\\\\.,]?\\\\d*)");

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textView.getText().toString());
        if (matcher.matches()) {
            double actual = Double.valueOf(matcher.group(1).replace(",", "."));

            return Math.abs(actual - expected) <= delta;
        } else {
            return false;
        }

    }


    @Override
    public void describeTo(Description description) {
        description.appendText("with double : ");
        description.appendValue(expected);
    }

    public static org.hamcrest.Matcher<View> withDoubleInText(@StringRes int stringResourceId, double expected, double delta) {
        return new TextFormatDoubleMatcher(stringResourceId, expected, delta);
    }

}