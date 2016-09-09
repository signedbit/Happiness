package net.signedbit.skype.settings;

import com.samczsun.skype4j.chat.Chat;
import net.signedbit.skype.TopicChanger;

import java.util.Set;

/**
 * Manage settings storage.
 * The addXYZ and removeXYZ return booleans to indicate if the setting was saved.
 * They might return false because the underlying storage is unavailable.
 */
public interface Settings {

    // ----------------------- bot masters -----------------------

    /**
     * Bot masters are those who can control the bot.
     *
     * @return the set of bot masters
     */
    Set<String> getBotMasters();

    /**
     * Adds a bot master.
     *
     * @param username username of the new bot master
     * @return true if the user is a bot master now, false otherwise
     */
    boolean addBotMaster(final String username);

    /**
     * Removes a bot master.
     *
     * @param username username of the old bot master
     * @return true if the user is not a bot master anymore, false otherwise
     */
    boolean removeBotMaster(final String username);

    // ----------------------- groups -----------------------

    /**
     * The set of groups that the bot is enabled in and will spread happiness.
     *
     * @return the group IDs returned from {@link Chat#getIdentity()}
     */
    Set<String> getGroups();

    /**
     * Allows the bot to spread happiness in the group.
     *
     * @param groupId the group ID returned from {@link Chat#getIdentity()}
     * @return true if the bot will now be able to spread happiness in the group, false otherwise
     */
    boolean addGroup(final String groupId);

    /**
     * Disallows the bot to spread happiness in the group.
     *
     * @param groupId the group ID returned from {@link Chat#getIdentity()}
     * @return true if the bot will NOT be able to spread happiness in the group, false otherwise
     */
    boolean removeGroup(final String groupId);

    // ----------------------- topics -----------------------

    /**
     * Gets the topics that we can change the group topic to. This is just for convenience.
     *
     * @return the positive topics
     */
    Set<String> getTopics();

    /**
     * Attempts to save the topic for later happiness spreading.
     * The topic cannot be longer than {@link Settings#getMaxTopicLength()} or this will return false.
     *
     * @param topic Topic to save
     * @return true if the topic can potentially be used later, false otherwise
     */
    boolean addTopic(String topic);

    /**
     * Attempts to remove the topic for later happiness spreading.
     * The topic cannot be longer than {@link Settings#getMaxTopicLength()} or this will return false.
     *
     * @param topic Topic to remove
     * @return true if the topic can NOT potentially be used later, false otherwise
     */
    boolean removeTopic(String topic);

    /**
     * This is to ensure the topics aren't terribly long, both for persistence reasons and UX reasons on Skype.
     *
     * @return the arbitrary maximum topic length
     */
    int getMaxTopicLength();

    // ----------------------- miscellaneous -----------------------

    /**
     * This determines if the bot is completely disabled for all groups
     *
     * @return true if the bot is disabled for all groups
     */
    boolean isDisabled();

    /**
     * Either enables or disables the bot from spreading happiness in all the groups.
     *
     * @param disabled true to disable, false to enable
     * @return true if the setting was successfully updated
     */
    boolean setDisabled(boolean disabled);

    /**
     * This setting determines if normal users can see who the bot masters are by using the
     * {@link TopicChanger#COMMAND_BOT_MASTER_LIST list} command.
     * <p>
     * It is true by default
     *
     * @return if normal users can see who the bot masters are
     */
    boolean isTellingBotMasters();

    /**
     * This setting either enables or disables non-bot masters from using the command to see who the bot masters are.
     *
     * @param willTell true if non bot masters may use the {@link TopicChanger#COMMAND_BOT_MASTER_LIST} command.
     * @return true if the setting will take effect
     */
    boolean setTellingBotMasters(boolean willTell);

    /**
     * Gets the English word to describe the success condition.
     *
     * @param success true for success, false otherwise
     * @return An English word representing the success condition
     */
    default String getSuccessWord(final boolean success) {
        return success ? "Successfully" : "Unsuccessfully";
    }

    /**
     * Gets the English word to describe the enabled condition.
     *
     * @param enabled true for enabled, false otherwise
     * @return An English word representing the enabled condition
     */
    default String getEnabledWord(final boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }
}
