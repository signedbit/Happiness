package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

/**
 * These are the settings that control and display the groups that the bot will be allowed to spread happiness in.
 */
public class GroupSettingsListener extends BaseMessageListener {
    public GroupSettingsListener(final TopicChanger topic, final Settings settings) {
        super(topic, settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handle(final Chat c, final ChatMessage m) {
        final String username = m.getSender().getUsername();
        final boolean botMaster = topic.isBotMaster(username);

        final String body = m.getContent().asPlaintext();
        if (botMaster && TopicChanger.COMMAND_GROUP_TOGGLE_MASTER.equalsIgnoreCase(body)) {
            // turn the bot on or off for all groups, admin only
            final boolean disabled = settings.isDisabled();
            final boolean success = settings.setDisabled(!disabled);

            final String successWord = settings.getSuccessWord(success);
            final String enabledWord = settings.getEnabledWord(disabled);

            topic.sendMessage(c, String.format("%s %s the bot for all groups.", successWord, enabledWord));
            return;
        } else if (TopicChanger.COMMAND_GROUP_SHOW_MASTER.equalsIgnoreCase(body)) {
            // let 'em know if the bot is on or off for this specific group
            final boolean enabled = !settings.isDisabled();
            topic.sendMessage(c, String.format("This bot %s spreading happiness in any group.", enabled ? "is" : "is NOT"));
            return;
        }

        if (!(c instanceof GroupChat)) {
            // the rest of the commands only work in a group
            return;
        }

        final String groupId = c.getIdentity();
        if (botMaster && TopicChanger.COMMAND_GROUP_TOGGLE_GROUP.equalsIgnoreCase(body)) {
            // turn the bot on or off for just this groups, admin only
            if (settings.getGroups().contains(groupId)) {
                final boolean removed = settings.removeGroup(groupId);
                tellChat(c, removed, false);
            } else {
                final boolean added = settings.addGroup(groupId);
                tellChat(c, added, true);
            }
        } else if (TopicChanger.COMMAND_GROUP_SHOW_GROUP.equalsIgnoreCase(body)) {
            // let 'em know if the bot is on or off for this specific group
            final boolean enabled = settings.getGroups().contains(groupId);
            final String predicate = enabled ? "is" : "is NOT";
            topic.sendMessage(c, String.format("This bot %s spreading happiness in this group", predicate));
        }
    }

    /**
     * Build and send a nice response for the command.
     *
     * @param c       the chat to send the response to
     * @param success whether or not the command was applied successfully
     * @param added   if the operation attempted to add or remove the user from the bot masters
     */
    private void tellChat(final Chat c, final boolean success, final boolean added) {
        final String successWord = settings.getSuccessWord(success);
        final String action = added ? "added" : "removed";
        final String where = added ? "to" : "from";
        topic.sendMessage(c, String.format("%s %s this group %s the list of happy groups.", successWord, action, where));
    }
}
