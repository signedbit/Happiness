package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

import static net.signedbit.skype.TopicChanger.*;

/**
 * Responds to general help and information commands.
 */
public class GeneralCommandsListener extends BaseMessageListener {
    public GeneralCommandsListener(final TopicChanger topic, final Settings settings) {
        super(topic, settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handle(final Chat c, final ChatMessage m) {
        final String username = m.getSender().getUsername();
        final String body = m.getContent().asPlaintext();

        if (COMMAND_GENERAL_PING.equalsIgnoreCase(body)) {
            // ping/pong to check if the bot is even online :-)
            topic.sendMessage(c, "pong");
            return;
        } else if (COMMAND_GENERAL_STATUS.equalsIgnoreCase(body)) {
            // send 'em a status report with the current settings
            topic.sendMessage(c, getStatus(c));
            return;
        } else if (COMMAND_GENERAL_HELP.equalsIgnoreCase(body)) {
            // send 'em the help message ;-) It's a long one.
            topic.sendMessage(c, getHelp());
            return;
        }

        final boolean botMaster = topic.isBotMaster(username);
        if (!botMaster || !(c instanceof GroupChat)) {
            // the rest of the commands are only for the bot masters in a group
            return;
        }

        if (COMMAND_TOPIC_CHANGE.equalsIgnoreCase(body)) {
            // change the topic just for funs :-)
            topic.change((GroupChat) c);
        }
    }

    /**
     * Construct and return the help message
     *
     * @return the long ol' help message.
     */
    private String getHelp() {
        final StringBuilder sb = new StringBuilder();

        appendCredits(sb);

        sb.append("Help:\n\n");

        // no enum because this would be the only place it's used
        // groups commands
        appendCommand(sb, COMMAND_GROUP_TOGGLE_MASTER, null, "Toggles me on or off for ALL groups", true);
        appendCommand(sb, COMMAND_GROUP_SHOW_MASTER, null, "Determines whether or not any positivity is being spread", false);
        appendCommand(sb, COMMAND_GROUP_TOGGLE_GROUP, null, "Toggles me on or off for this group", true);
        appendCommand(sb, COMMAND_GROUP_SHOW_GROUP, null, "Determines whether or not this group is spreading positivity", false);
        sb.append("\n");

        // bot master commands
        appendCommand(sb, COMMAND_BOT_MASTER_ADD, "<username>", "Allow a user to control me", true);
        appendCommand(sb, COMMAND_BOT_MASTER_REMOVE, "<username>", "Remove the ability for a user to control me", true);
        appendCommand(sb, COMMAND_BOT_MASTER_LIST, null, "Send a list of those who have control over me", true);
        appendCommand(sb, COMMAND_BOT_MASTER_TELLING_TOGGLE, null, "Toggles the ability for regular users to see who the bot masters are", true);
        appendCommand(sb, COMMAND_BOT_MASTER_TELLING_SHOW, null, "Determine whether regular users can see who the bot masters are", true);
        sb.append("\n");

        // topic commands
        appendCommand(sb, COMMAND_TOPIC_ADD, "<topic>", "Add a topic to the set of possible positive topics, the topic must be less than " + settings.getMaxTopicLength() + " characters", true);
        appendCommand(sb, COMMAND_TOPIC_REMOVE, "<topic>", "Remove the given topic from the set of possible positive topics", true);
        appendCommand(sb, COMMAND_TOPIC_LIST, null, "Lists all the (hopefully positive) topics that might be randomly selected", true);
        appendCommand(sb, COMMAND_TOPIC_CHANGE, null, "Changes the topic to a random positive topic", true);
        sb.append("\n");

        // general commands
        appendCommand(sb, COMMAND_GENERAL_PING, null, "Responds with a 'pong' as a quick and easy way to see if the bot is running", false);
        appendCommand(sb, COMMAND_GENERAL_STATUS, null, "Responds with my status and current settings", false);
        appendCommand(sb, COMMAND_GENERAL_HELP, null, "Responds with this message", false);

        return sb.toString();
    }

    /**
     * Compose each command in the same format
     *
     * @param command           the actual command
     * @param usage             the optional usage parameters, null for none
     * @param description       a brief description of what the command does
     * @param requiresBotMaster true if only bot masters can use the command
     */
    private void appendCommand(final StringBuilder sb,
                               final String command,
                               final String usage,
                               final String description,
                               final boolean requiresBotMaster) {
        sb.append(command).append(' ');
        if (usage != null) {
            sb.append(usage).append(' ');
        }
        sb.append("- ");
        sb.append(description);
        if (requiresBotMaster) {
            sb.append(" (requires bot master)");
        }
        sb.append(" :-)\n");
    }

    /**
     * Compose the credits.
     */
    private void appendCredits(final StringBuilder sb) {
        sb.append("Skype Topic Positivity Bot\n");
        sb.append("By Signedbit\n");
        sb.append("https://github.com/signedbit/happiness");
        sb.append("\n\n");
    }

    /**
     * Construct and return a nice status message. :-)
     *
     * @param c an optional chat. If it's a group chat,
     *          this will also say whether the bot is enabled in said group chat.
     * @return a nice status message describing the current settings and such :-)
     */
    private String getStatus(final Chat c) {
        final StringBuilder sb = new StringBuilder();

        appendCredits(sb);

        sb.append("Status:\n\nBot is: ");
        sb.append(settings.isDisabled() ? "OFF :-(" : "ON :-)");
        sb.append("\n");
        if (c instanceof GroupChat) {
            sb.append("Is this group spreading positivity: ");
            sb.append(settings.getGroups().contains(c.getIdentity()) ? "YES :-)" : "NO :-(");
            sb.append('\n');
        }
        sb.append("Max topic length in characters: ");
        sb.append(settings.getMaxTopicLength());
        sb.append("\n\n");

        topic.appendBotMasters(sb);

        sb.append("\n\nTopics:\n\t");
        sb.append(String.join("\n\t", settings.getTopics()));
        return sb.toString();
    }
}
