package net.chmilevfa.telegram.bots.currency.service;

/**
 * Localization service for storing strings. Contains all strings visible to user.
 *
 * @author chmilevfa
 * @since 09.07.18
 */
public class StringService {

    /* Buttons */
    public static final String CURRENT_RATE = "Current rate";
    public static final String SETTINGS = "Settings";
    public static final String FEEDBACK = "Feedback/Bug report";
    public static final String GO_TO_MAIN_MENU = "Go to main menu";

    /* Replies */
    public static final String HELLO_MESSAGE = "I can show you currency rate.\nPlease choose one option.";
    public static final String CHOOSE_FIRST_CURRENCY = "Please choose first currency";
    public static final String CHOOSE_SECOND_CURRENCY = "Please choose second currency";
    public static final String CURRENCY_RATE = "Current rate for **%s** is **%s**";
    public static final String WRITE_FEEDBACK = "Please write feedback.\nI will send it to my developer.";
    public static final String THANKS_FEEDBACK = "Thank you for a feedback! I already sent it to my developer\n\n";
    public static final String FEEDBACK_FOR_DEVELOPER = "Feedback from @%s\n";
}
