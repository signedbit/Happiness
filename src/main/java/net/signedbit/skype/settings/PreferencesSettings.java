package net.signedbit.skype.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * An implementation of {@link Settings} backed by the Java Preferences API.
 */
public class PreferencesSettings implements Settings {
    // pref keys
    private static final String NODE_KEY_TELLING_BOT_MASTERS = "tellingBotmasters";
    private static final String NODE_KEY_DISABLED = "disabled";

    // pref subnode keys
    private static final String NODE_CHILD_MASTERS = "masters";
    private static final String NODE_CHILD_GROUPS = "groups";
    private static final String NODE_CHILD_TOPICS = "topics";

    private final Preferences root = Preferences.userNodeForPackage(getClass());

    private final Preferences masters;
    private final Preferences groups;
    private final Preferences topics;

    public PreferencesSettings() throws BackingStoreException {
        masters = root.node(NODE_CHILD_MASTERS);
        groups = root.node(NODE_CHILD_GROUPS);
        topics = root.node(NODE_CHILD_TOPICS);

        initTopics(); // helpful for the first run :-) could also create a canary key
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getBotMasters() {
        return new HashSet<>(Arrays.asList(getKeys(masters)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addBotMaster(final String username) {
        return addKey(masters, username.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeBotMaster(final String username) {
        return removeKey(masters, username.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getGroups() {
        return new HashSet<>(Arrays.asList(getKeys(groups)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addGroup(final String groupId) {
        return addKey(groups, groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeGroup(final String groupId) {
        return removeKey(groups, groupId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getTopics() {
        return new HashSet<>(Arrays.asList(getKeys(topics)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTopic(final String topic) {
        if (topic.length() > getMaxTopicLength()) {
            return false;
        }
        return addKey(topics, topic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeTopic(final String topic) {
        if (topic.length() > getMaxTopicLength()) {
            return false;
        }
        final boolean success = removeKey(topics, topic);
        initTopics(); // don't let there be zero topics to choose from :-)
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxTopicLength() {
        return Preferences.MAX_KEY_LENGTH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return root.getBoolean(NODE_KEY_DISABLED, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setDisabled(final boolean disabled) {
        root.putBoolean(NODE_KEY_DISABLED, disabled);
        flush(root);
        return root.getBoolean(NODE_KEY_DISABLED, !disabled) == disabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTellingBotMasters() {
        return root.getBoolean(NODE_KEY_TELLING_BOT_MASTERS, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setTellingBotMasters(final boolean willTell) {
        root.putBoolean(NODE_KEY_TELLING_BOT_MASTERS, willTell);
        flush(root);
        return root.getBoolean(NODE_KEY_TELLING_BOT_MASTERS, !willTell) == willTell;
    }

    /**
     * Adds the default topics to the pool.
     */
    private void initTopics() {
        if (getTopics().size() == 0) {
            // add default topics for first run or invalid state

            final String[] defaultTopics = {
                    "You're amazing<3",
                    "Everyone here is great<3",
                    "You're loved<3",
                    "You're wonderful<3",
                    "I love you<3",
                    "Smile because you're worth it<3",
                    "I hope your day is as great as you are<3",
                    "Believe in yourself<3",
                    "Ignore the haters<3",
                    "You're extraordinarily beautiful<3",
                    "Don't succumb to peer pressure<3",
                    "You're the best<3"
            };

            for (final String topic : defaultTopics) {
                addTopic(topic);
            }
        }
    }

    /**
     * Gets the keys associated with the given preferences.
     * The key names are used to store the groups, bot masters, and topics. The values are empty strings.
     *
     * @param node the preference node we want the keys of
     * @return the keys or an empty array in the case that the backing store is unavailable
     */
    private String[] getKeys(final Preferences node) {
        try {
            return node.keys();
        } catch (BackingStoreException e) {
            // if the settings object was created successfully that means the store was backed
            // let's just hope that this is a temporary problem and return an empty array
            e.printStackTrace();
            return new String[]{};
        }
    }

    /**
     * Adds the given key to the preferences node
     *
     * @param node the preferences node to add the key to
     * @param key  the key to add
     * @return true if the key is able to be retrieved later
     */
    private boolean addKey(final Preferences node, final String key) {
        node.put(key, "");
        flush(node);
        return node.get(key, null) != null;
    }

    /**
     * Removes the given key to the preferences node
     *
     * @param node the preferences node to remove the key from
     * @param key  the key to remove
     * @return true if the key is NOT able to be retrieved later
     */
    private boolean removeKey(final Preferences node, final String key) {
        node.remove(key);
        flush(node);
        return node.get(key, null) == null;
    }

    /**
     * When we update the preferences, we must ensure the changes are written successfully.
     * This doesn't propagate up any errors from writing because in order to start the bot,
     * the backing store needs to be available. It's probably just be temporarily unavailable.
     * Let's not panic.
     *
     * @param node the preferences node to ensure changes are committed.
     */
    private void flush(final Preferences node) {
        try {
            node.flush();
        } catch (BackingStoreException e) {
            // if the settings object was created successfully that means the store was backed
            // let's just hope that this is a temporary problem and do nothing for now
            e.printStackTrace();
        }
    }
}
