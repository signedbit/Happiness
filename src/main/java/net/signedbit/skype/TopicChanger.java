package net.signedbit.skype;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import net.signedbit.skype.listeners.*;
import net.signedbit.skype.settings.InMemoryCachedSettings;
import net.signedbit.skype.settings.PreferencesSettings;
import net.signedbit.skype.settings.Settings;

import java.util.Random;
import java.util.Set;
import java.util.prefs.BackingStoreException;

public class TopicChanger {

    // groups commands
    public static final String COMMAND_GROUP_TOGGLE_MASTER = "./toggleall";
    public static final String COMMAND_GROUP_SHOW_MASTER = "./showall";
    public static final String COMMAND_GROUP_TOGGLE_GROUP = "./togglegroup";
    public static final String COMMAND_GROUP_SHOW_GROUP = "./showgroup";

    // bot master commands
    public static final String COMMAND_BOT_MASTER_ADD = "./addbotmaster";
    public static final String COMMAND_BOT_MASTER_REMOVE = "./removebotmaster";
    public static final String COMMAND_BOT_MASTER_LIST = "./listbotmasters";
    public static final String COMMAND_BOT_MASTER_TELLING_TOGGLE = "./tellbotmasters";
    public static final String COMMAND_BOT_MASTER_TELLING_SHOW = "./istellingbotmasters";

    // topic commands
    public static final String COMMAND_TOPIC_ADD = "./addtopic";
    public static final String COMMAND_TOPIC_REMOVE = "./removetopic";
    public static final String COMMAND_TOPIC_LIST = "./listtopics";
    public static final String COMMAND_TOPIC_CHANGE = "./change";

    // general commands
    public static final String COMMAND_GENERAL_PING = "./ping";
    public static final String COMMAND_GENERAL_STATUS = "./status";
    public static final String COMMAND_GENERAL_HELP = "./help";

    private static final Random RANDOM = new Random();

    private final Settings settings;
    private final String username;

    public TopicChanger(final Settings settings, final String username) {
        this.settings = settings;
        this.username = username;
    }

    public static void main(String[] args) throws ConnectionException, NotParticipatingException, InvalidCredentialsException, BackingStoreException {
        if (args.length != 2) {
            System.err.println("usage: java -jar TopicChanger.jar <username> <password>");
            return;
        }

        final String username = args[0];
        final String password = args[1];

        final Settings settings = new InMemoryCachedSettings(new PreferencesSettings());
        final TopicChanger topic = new TopicChanger(settings, username);

        final Skype skype = new SkypeBuilder(username, password).withAllResources().build();
        skype.login();

        skype.getEventDispatcher().registerListener(new TopicChangeListener(topic, settings));
        skype.getEventDispatcher().registerListener(new BotMasterSettingsListener(topic, settings));
        skype.getEventDispatcher().registerListener(new GroupSettingsListener(topic, settings));
        skype.getEventDispatcher().registerListener(new TopicSettingsListener(topic, settings));
        skype.getEventDispatcher().registerListener(new GeneralCommandsListener(topic, settings));
        skype.subscribe();

        topic.updateTopics(skype);

        System.out.println("Loaded.");
    }

    /**
     * Try to change the topics when the bot starts.
     *
     * @throws ConnectionException since this is in the initialization of the bot, we propagate this up
     */
    private void updateTopics(final Skype skype) throws ConnectionException {
        if (settings.isDisabled()) {
            return;
        }

        final Set<String> topics = settings.getTopics();

        for (final String groupId : settings.getGroups()) {
            if (!settings.getGroups().contains(groupId)) {
                // we aren't allowed to make this group happy :-(
                return;
            }

            try {
                final Chat c = skype.getOrLoadChat(groupId);
                if (!(c instanceof GroupChat)) {
                    // this should never happen
                    return;
                }
                if (!topics.contains(((GroupChat) c).getTopic())) {
                    change((GroupChat) c);
                }
            } catch (ChatNotFoundException e) {
                // should only happen if the chat is deleted/left by the user
                e.printStackTrace();
            }
        }
    }

    /**
     * Change the topic of the given group chat and surpress any (hopefully temporary) connection error
     *
     * @param c the group chat to change
     */
    public void change(final GroupChat c) {
        final String newTopic = getRandomTopic();

        System.out.println("Changing it to: " + newTopic);
        System.out.println();

        try {
            c.setTopic(newTopic);
        } catch (ConnectionException e) {
            // this probably won't happen very often since this is only called when the topic is changed.
            // ... we wouldn't know that the topic was changed if we weren't online.
            // perhaps we should propagate this upwards anyway
            e.printStackTrace();
        }
    }

    /**
     * Gives a random positive topic.
     *
     * @return a positive topic :-)
     */
    private String getRandomTopic() {
        final Set<String> t = settings.getTopics();
        final String[] topics = t.toArray(t.toArray(new String[t.size()]));
        return topics[RANDOM.nextInt(topics.length)];
    }

    /**
     * Sends a plaintext message to the given chat and surpresses any (hopefully temporary) connect error.
     *
     * @param c       the chat
     * @param message the message
     */
    public void sendMessage(final Chat c, final String message) {
        try {
            c.sendMessage(message);
        } catch (final ConnectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * The list of bot masters are the bot itself and the custom bot masters.
     *
     * @param user the username to check
     * @return true if the user is allowed to use the 'privileged' commands.
     */
    public boolean isBotMaster(final String user) {
        return username.equalsIgnoreCase(user) ||
                settings.getBotMasters().contains(user.toLowerCase());
    }

    /**
     * Just construct the list of bot masters in a cute (shoutout to Nora) way. :-)
     */
    public void appendBotMasters(final StringBuilder sb) {
        sb.append("Bot masters:\n\t");
        final Set<String> botMasters = settings.getBotMasters();
        if (!botMasters.isEmpty()) {
            sb.append(String.join("<3\n\t", botMasters));
            sb.append("<3");
        } else {
            sb.append("(none)");
        }
    }
}
