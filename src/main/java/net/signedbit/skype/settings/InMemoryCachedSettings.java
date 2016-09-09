package net.signedbit.skype.settings;

import java.util.Set;

/**
 * This is a wrapper for settings that is useful when the reads are very slow such as over disk or network.
 * This will commit the changes immediately for storage.
 * It is assumed that reads are much more frequent than writes. Which makes sense given the domain.
 */
public class InMemoryCachedSettings implements Settings {
    private final Settings settings;
    private final Set<String> botMasters;
    private final Set<String> groups;
    private final Set<String> topics;
    private boolean disabled;
    private boolean tellingBotMasters;

    public InMemoryCachedSettings(final Settings settings) {
        this.settings = settings;

        this.botMasters = settings.getBotMasters();
        this.groups = settings.getGroups();
        this.topics = settings.getTopics();
        this.disabled = settings.isDisabled();
        this.tellingBotMasters = settings.isTellingBotMasters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getBotMasters() {
        return botMasters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addBotMaster(final String username) {
        final boolean success = settings.addBotMaster(username);
        if (success) {
            botMasters.add(username);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeBotMaster(final String username) {
        final boolean success = settings.removeBotMaster(username);
        if (success) {
            botMasters.remove(username);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getGroups() {
        return groups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addGroup(final String groupId) {
        final boolean success = settings.addGroup(groupId);
        if (success) {
            groups.add(groupId);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeGroup(final String groupId) {
        final boolean success = settings.removeGroup(groupId);
        if (success) {
            groups.remove(groupId);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getTopics() {
        return topics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTopic(final String topic) {
        final boolean success = settings.addTopic(topic);
        if (success) {
            topics.add(topic);
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeTopic(final String topic) {
        final boolean success = settings.removeTopic(topic);
        if (success) {
            topics.remove(topic);
            if (topics.size() == 0) {
                topics.addAll(settings.getTopics());
            }
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTopicLength() {
        return settings.getMaxTopicLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setDisabled(final boolean disabled) {
        final boolean success = settings.setDisabled(disabled);
        if (success) {
            this.disabled = disabled;
        }
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTellingBotMasters() {
        return tellingBotMasters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setTellingBotMasters(final boolean willTell) {
        final boolean success = settings.setTellingBotMasters(willTell);
        if (success) {
            this.tellingBotMasters = willTell;
        }
        return success;
    }
}
