package com.example.priyakarambelkar.generalformerrors;

import android.annotation.SuppressLint;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class LoginFlowErrorUtils {

    public static final int NO_ERROR = -1;

    private int minPasswordLength = 6;
    private final int maxPasswordLength = 31;

    private String patternLetterAndNumber = "^(?=.*[a-zA-Z])(?=.*\\d)";
    private String patternAllowedCharacters = "^[a-zA-Z0-9!#@{~}$_^-]+$";
    private String patternWhiteSpace = "\\s";
    private String wordPassword = "password";
    private int minimumPasswordErrorStringId = R.string.error_password_length_too_short,
            maximumPasswordErrorStringId = R.string.error_password_length_too_long,
            passwordWordForPasswordErrorStringId = R.string.error_password_is_word_password,
            letterAndNumberMissingErrorStringId = R.string.error_password_do_not_contain_one_letter_and_one_number,
            forbiddenCharactersPasswordErrorStringId = R.string.error_password_contain_disallowed_characters;

    public int getMinimumPasswordErrorStringId() {
        return minimumPasswordErrorStringId;
    }

    public void setMinimumPasswordErrorStringId(int minimumPasswordErrorStringId) {
        this.minimumPasswordErrorStringId = minimumPasswordErrorStringId;
    }

    public int getMaximumPasswordErrorStringId() {
        return maximumPasswordErrorStringId;
    }

    public void setMaximumPasswordErrorStringId(int maximumPasswordErrorStringId) {
        this.maximumPasswordErrorStringId = maximumPasswordErrorStringId;
    }

    public int getPasswordWordForPasswordErrorStringId() {
        return passwordWordForPasswordErrorStringId;
    }

    public void setPasswordWordForPasswordErrorStringId(int passwordWordForPasswordErrorStringId) {
        this.passwordWordForPasswordErrorStringId = passwordWordForPasswordErrorStringId;
    }

    public int getLetterAndNumberMissingErrorStringId() {
        return letterAndNumberMissingErrorStringId;
    }

    public void setLetterAndNumberMissingErrorStringId(int letterAndNumberMissingErrorStringId) {
        this.letterAndNumberMissingErrorStringId = letterAndNumberMissingErrorStringId;
    }

    public int getForbiddenCharactersPasswordErrorStringId() {
        return forbiddenCharactersPasswordErrorStringId;
    }

    public void setForbiddenCharactersPasswordErrorStringId(int forbiddenCharactersPasswordErrorStringId) {
        this.forbiddenCharactersPasswordErrorStringId = forbiddenCharactersPasswordErrorStringId;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }

    public int getMaxPasswordLength() {
        return maxPasswordLength;
    }

    public String getPatternLetterAndNumber() {
        return patternLetterAndNumber;
    }

    public void setPatternLetterAndNumber(String patternLetterAndNumber) {
        this.patternLetterAndNumber = patternLetterAndNumber;
    }

    public String getPatternAllowedCharacters() {
        return patternAllowedCharacters;
    }

    public void setPatternAllowedCharacters(String patternAllowedCharacters) {
        this.patternAllowedCharacters = patternAllowedCharacters;
    }

    public String getPatternWhiteSpace() {
        return patternWhiteSpace;
    }

    public void setPatternWhiteSpace(String patternWhiteSpace) {
        this.patternWhiteSpace = patternWhiteSpace;
    }

    public String getWordPassword() {
        return wordPassword;
    }

    public void setWordPassword(String wordPassword) {
        this.wordPassword = wordPassword;
    }

    /**
     * Removes link underlines in a string replacing them with non-underlined links.
     *
     * @param text A Spannable object
     */
    public static void removeUnderlines(Spannable text) {
        URLSpan[] spans = text.getSpans(0, text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            text.setSpan(span, start, end, 0);
        }
    }

    public static String getReadableValue(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

    @SuppressLint("ParcelCreator")
    private static class URLSpanNoUnderline extends URLSpan {

        URLSpanNoUnderline(String url) {
            super(url);
        }

        public void updateDrawState(TextPaint drawState) {
            super.updateDrawState(drawState);
            drawState.setUnderlineText(false);
        }
    }

    public static boolean isValidEmail(final String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static int getEmailError(final String target, @StringRes int error) {
        return isValidEmail(target) ? NO_ERROR : error;
    }

    public static boolean isValidPhoneNumber(final String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.PHONE.matcher(target).matches();
    }

    public static int getPhoneNumberError(final String target, @StringRes int error) {
        return isValidPhoneNumber(target) ? NO_ERROR : error;
    }

    public int getPasswordError(final String target) {
        if (!isLongerThanMinLength(target)) {
            return minimumPasswordErrorStringId;
        } else if (!isShorterThanMaxLength(target)) {
            return maximumPasswordErrorStringId;
        } else if (isTheWordPassword(target)) {
            return passwordWordForPasswordErrorStringId;
        } else if (!isContainingOneLetterAndOneNumber(target)) {
            return letterAndNumberMissingErrorStringId;
        } else if (!isContainingOnlyAllowedCharacters(target)) {
            return forbiddenCharactersPasswordErrorStringId;
        } else {
            return NO_ERROR;
        }
    }

    private boolean isLongerThanMinLength(String target) {
        return !TextUtils.isEmpty(target) && target.length() >= minPasswordLength;
    }

    private boolean isShorterThanMaxLength(String target) {
        return target.length() <= maxPasswordLength;
    }

    private boolean isTheWordPassword(String target) {
        return target.toLowerCase().equals(wordPassword);
    }

    private boolean isContainingOneLetterAndOneNumber(String target) {
        return Pattern.compile(patternLetterAndNumber).matcher(target).find();
    }

    private boolean isContainingOnlyAllowedCharacters(String target) {
        return Pattern.compile(patternAllowedCharacters).matcher(target).matches();
    }

    private boolean isContainingWhiteSpace(String target) {
        return Pattern.compile(patternWhiteSpace).matcher(target).find();
    }

    public static int getDobError(String dob, @StringRes int error) throws ParseException {
        if (isDobValid(dob)) {
            return NO_ERROR;
        } else {
            return error;
        }
    }

    public static int getMembershipExpirationError(String expirationDate, @StringRes int error) throws ParseException {
        if (isExpirationDateValid(expirationDate)) {
            return NO_ERROR;
        } else {
            return error;
        }
    }

    private static boolean isDobValid(String dob) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dob);
        return new Date().before(date);
    }

    private static boolean isExpirationMonthYearValid(String expirationDate) throws ParseException {
        Date date = new SimpleDateFormat("MM/yy").parse(expirationDate);
        return new Date().after(date);
    }

    public static boolean isExpirationDateValid(String expirationDate) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(expirationDate);
        return new Date().after(date);
    }
}
