package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

/**
 * These are the settings that control and display the topics that the bot will use to spread happiness.
 */
public class TopicSettingsListener extends BaseMessageListener {
    public TopicSettingsListener(final TopicChanger topic, final Settings settings) {
        super(topic, settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handle(final Chat c, final ChatMessage m) {
        final String username = m.getSender().getUsername();

        if (!topic.isBotMaster(username)) {
            // all the commands here are bot masters only
            return;
        }

        final String body = m.getContent().asPlaintext();

        if (TopicChanger.COMMAND_TOPIC_LIST.equalsIgnoreCase(body)) {
            // send the topic pool
            topic.sendMessage(c, "Topics:\n\n" + String.join("\n", settings.getTopics()));
            return;
        }

        final int indexOfSpace = body.indexOf(' ');
        if (indexOfSpace == -1) {
            // ensure there is at least one space
            return;
        }

        final String newTopic = body.substring(indexOfSpace + 1, body.length());

        final String lower = body.toLowerCase();
        final boolean add = lower.startsWith(TopicChanger.COMMAND_TOPIC_ADD);
        final boolean remove = lower.startsWith(TopicChanger.COMMAND_TOPIC_REMOVE);

        if (!add && !remove) {
            return;
        }

        // attempt to add a topic to the pool
        if (newTopic.length() > settings.getMaxTopicLength()) {
            topic.sendMessage(c, "That topic is too long. :-( The topic must be less than " + settings.getMaxTopicLength() + " characters.");
            return;
        }

        final boolean success = add ? settings.addTopic(newTopic) : settings.removeTopic(newTopic);
        tellChat(c, success, add, newTopic);
    }

    /**
     * Build and send a nice response for the command.
     *
     * @param c       the chat to send the response to
     * @param success whether or not the command was applied successfully
     * @param added   if the operation attempted to add or remove the user from the bot masters
     * @param topic   topic this operation was attempted on
     */
    private void tellChat(final Chat c, final boolean success, final boolean added, final String topic) {
        final String successWord = settings.getSuccessWord(success);
        final String action = added ? "added" : "removed";
        final String where = added ? "to" : "from";
        this.topic.sendMessage(c, String.format("%s %s %s %s the list of topics.", successWord, action, topic, where));
    }
}
