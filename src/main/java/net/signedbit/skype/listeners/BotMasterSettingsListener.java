package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

/**
 * These are the settings that control and display who the bot masters are.
 */
public class BotMasterSettingsListener extends BaseMessageListener {
    public BotMasterSettingsListener(final TopicChanger topic, final Settings settings) {
        super(topic, settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handle(final Chat c, final ChatMessage m) {
        final String username = m.getSender().getUsername();
        final String body = m.getContent().asPlaintext();

        if (TopicChanger.COMMAND_BOT_MASTER_LIST.equalsIgnoreCase(body)) {
            // send the list of bot masters, if possible ;-)
            if (settings.isTellingBotMasters() || topic.isBotMaster(username)) {
                final StringBuilder botMasters = new StringBuilder();
                topic.appendBotMasters(botMasters);
                topic.sendMessage(c, botMasters.toString());
            }
            return;
        }

        if (!topic.isBotMaster(username)) {
            // the rest are bot master commands
            return;
        }

        final int indexOfSpace = body.indexOf(' ');
        if (indexOfSpace != -1 && indexOfSpace != body.lastIndexOf(' ')) {
            // ensure there is exactly one space
            return;
        }

        final String botMaster = body.substring(indexOfSpace + 1, body.length());

        final String lower = body.toLowerCase();
        if (lower.startsWith(TopicChanger.COMMAND_BOT_MASTER_ADD)) {
            // promote the user to a bot master
            final boolean removed = settings.addBotMaster(botMaster);
            tellChat(c, removed, true, botMaster);
        } else if (lower.startsWith(TopicChanger.COMMAND_BOT_MASTER_REMOVE)) {
            // demote the user from a bot master
            final boolean added = settings.removeBotMaster(botMaster);
            tellChat(c, added, false, botMaster);
        } else if (TopicChanger.COMMAND_BOT_MASTER_TELLING_SHOW.equalsIgnoreCase(body)) {
            // say if non bot masters can request the list of bot masters
            final String enabledWord = settings.getEnabledWord(settings.isTellingBotMasters());
            topic.sendMessage(c, "Telling users who the bot masters are is currently " + enabledWord);
        } else if (TopicChanger.COMMAND_BOT_MASTER_TELLING_TOGGLE.equalsIgnoreCase(body)) {
            // toggle whether or not non bot masters can request the list of bot masters
            final boolean newTelling = !settings.isTellingBotMasters();
            final boolean success = settings.setTellingBotMasters(newTelling);

            final String successWord = settings.getSuccessWord(success);
            final String toggledWord = settings.getEnabledWord(newTelling);

            topic.sendMessage(c, String.format("%s %s ability for regular users to request the list of bot masters.", successWord, toggledWord));
        }
    }

    /**
     * Build and send a nice response for the command.
     *
     * @param c        the chat to send the response to
     * @param success  whether or not the command was applied successfully
     * @param added    if the operation attempted to add or remove the user from the bot masters
     * @param username username this operation was attempted on
     */
    private void tellChat(final Chat c, final boolean success, final boolean added, final String username) {
        final String successWord = settings.getSuccessWord(success);
        final String action = added ? "added" : "removed";
        final String where = added ? "to" : "from";
        topic.sendMessage(c, String.format("%s %s %s %s the list of bot masters.", successWord, action, username, where));
    }
}
