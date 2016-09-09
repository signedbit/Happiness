package net.signedbit.skype.listeners;

import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.events.chat.message.MessageSentEvent;
import net.signedbit.skype.TopicChanger;
import net.signedbit.skype.settings.Settings;

/**
 * A listener that listens for both message send and message receive events.
 * This is helpful for commands where they should be responded to by the bot account too.
 */
public abstract class BaseMessageListener implements Listener {
    protected final TopicChanger topic;
    protected final Settings settings;

    protected BaseMessageListener(TopicChanger topic, Settings settings) {
        this.topic = topic;
        this.settings = settings;
    }

    @EventHandler
    public void onMessageReceived(final MessageReceivedEvent e) {
        handle(e.getChat(), e.getMessage());
    }

    @EventHandler
    public void onMessageSent(final MessageSentEvent e) {
        handle(e.getChat(), e.getMessage());
    }

    /**
     * Actually handles the message event.
     *
     * @param c the chat this occurred in
     * @param m the actual message
     */
    protected abstract void handle(final Chat c, final ChatMessage m);
}
