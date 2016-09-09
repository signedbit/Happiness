package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.user.action.TopicUpdateEvent;
import com.samczsun.skype4j.user.User;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

/**
 * Listens for changes of the topic and changes it back appropriately
 */
public class TopicChangeListener implements Listener {
    private final TopicChanger topic;
    private final Settings settings;

    public TopicChangeListener(final TopicChanger topic, final Settings settings) {
        this.topic = topic;
        this.settings = settings;
    }

    @EventHandler
    public void onTopicEvent(final TopicUpdateEvent e) {
        if (settings.isDisabled()) {
            // if the entire bot is off, then don't bother.
            return;
        }

        log(e); // = 1 ;-)

        final Chat c = e.getChat();

        if (!settings.getGroups().contains(c.getIdentity())) {
            // we aren't allowed to make this group happy :-(
            return;
        }

        if (!(c instanceof GroupChat)) {
            // this should never happen if we check the identity
            return;
        }

        if (settings.getTopics().contains(e.getNewTopic())) {
            // if the topic was changed to something positive, then don't change it again :-)
            // this also prevents of infinite loop when the bot accounts changes the topic
            return;
        }

        // we're free to change the topic
        topic.change((GroupChat) c);
    }

    private void log(final TopicUpdateEvent e) {
        final User user = e.getUser();

        System.out.printf(
                "Topic changed:%n\tBy: %s%n\tFrom: %s%n\tTo: %s%n",
                user.getUsername(),
                e.getOldTopic(),
                e.getNewTopic()
        );
    }
}
