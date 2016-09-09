# Skype Group Topic Positivity :-)

A bot to automatically change Skype group topics back to a random positive message for subconsciously making people happier. :-)

## What?

All this does is change the Skype topic back to a random positive message whenever it is changed. It can also be used to keep the group topic from being changed to anything other than a predefined message. For example, to have a [MotD](https://en.wikipedia.org/wiki/Motd_(Unix)) that others users can't interfere with. Another use is cycling between a number of messages. Note: it only reacts to changing the topic, it doesn't simply change in a fixed period.

## Why?

[Depression](http://www.nami.org/Learn-More/Mental-Health-By-the-Numbers) [is](http://www.healthline.com/health/depression/facts-statistics-infographic) [widely](http://www.nimh.nih.gov/health/statistics/prevalence/major-depression-among-adults.shtml) [considered](http://www.dbsalliance.org/site/PageServer?pagename=education_statistics_depression) [the](https://www.adaa.org/about-adaa/press-room/facts-statistics) [common](http://www.who.int/mediacentre/factsheets/fs369/en/) [cold](https://www.cdc.gov/mentalhealth/data_stats/depression.htm) [of](http://www.newsweek.com/nearly-1-5-americans-suffer-mental-illness-each-year-230608) [mental](http://www.huffingtonpost.com/2014/12/01/mental-illness-statistics_n_6193660.html) [health](http://jama.jamanetwork.com/article.aspx?articleid=196765). Even those without clinical problems or recorded issues seem to not be very happy. From those who dread going to school to those who dread going to work, we don't seem to be very happy. [The Internet and social media amplifies this problem](http://www.newyorker.com/tech/elements/how-facebook-makes-us-unhappy) [because we see only the best of others](http://www.independent.co.uk/voices/social-media-is-making-us-depressed-lets-learn-to-turn-it-off-a6974526.html) [and fail to realize they have problems of their own](http://www.bbc.co.uk/bbcthree/item/65c9fe04-4b3d-461b-a3ab-10d0d5f6d9b5).

In my personal experience, simply being kind to someone is interpreted as affection for them. Are we so unhappy that _simply being nice_ to someone means you want something more from them? On the other hand, when someone is being nice to us, we often think there is a more devious and sinister plot lurking beneath the surface, or they're trying to butter us up. Why is [paying it forward](https://en.wikipedia.org/wiki/Pay_it_forward) and random acts of kindness considered the exception instead of the norm?

tl;dr: I want to make people happier. We need more positivity in the world.

## How?

I believe that the human subconscious is an _incredibly_ powerful thing. Doing things like repeating to yourself "I'm beautiful", seeing someone smile (perhaps on a billboard every day on your way to work), etc will affect you without you realizing it. This is abused by [marketers, subliminal messaging, TV](https://www.youtube.com/watch?v=2xPvYgTvr8I), etc constantly. [The best marketing](https://www.ducttapemarketing.com/blog/outbound-marketing-impact/) [doesn't](http://blog.hubspot.com/marketing/good-marketing-brands-that-get-it-list) [feel like marketing](http://www.business2community.com/marketing/the-best-marketing-doesnt-feel-like-marketing-0148780). Why do stock photos always have smiling people? It's because it makes you associate being happy with their product or service.

[As much as I dislike Skype](https://discordapp.com/), I have it on my cellphone. When I get a message from a group, the status bar at the top of my Android phone briefly shows the topic of the Skype group, and a snippet from the beginning of the received message. I got the idea to use the topic as a way to send positive subliminal messages. I strongly believe that every time a positive message appears on your screen, whether you realize that you see it or not, it subconsciously makes you _that_ much happier. This is a way to get positive messages on your screen more often.

tl;dr: Positive subliminal messaging.

## How to use?

**The group chat must be a cloud chat, not a P2P chat. From the readme of [Skype4J](https://github.com/samczsun/Skype4J):**
> "It does not support P2P chats. You can tell whether you're in a P2P chat or not based on the output of the /help command. If it contains commands such as /kickban, you're in a P2P chat and should switch to cloud chats immediately (try using /fork)"

This is a command line program. Simply run it and pass **only** two parameters: username and password of the Skype account it run should on. **WARNING: the password is stored in memory, use common sense.**

The bot is enabled by default. To make the bot start or stop listening for topic changes in a specific group: open that group while the bot is running and type `./togglegroup`. Only bot masters may use that command. A bot master is someone authorized to control the bot. By default, only the account that the bot is running from is allowed to control it. You'll have to log into that account and use the `./addbotmaster` command in any chat.

Go in a group chat and use these commands. Some such as `./help`, `./start`, and `./ping` may also be used in private chats. If the usage cell is empty, the command takes no parameters. For example, to add `echo123` as a bot master: send `./addbotmaster echo123` in a group chat that the bot is currently running in.

| Command | Usage | Description | Requires Bot Master? |
|---------|-------|-------------|----------------------|
| ./toggleall |  | Toggles the bot on or off for ALL groups | Yes |
| ./showall |  | Says whether the bot is enabled for ALL groups | No |
| ./togglegroup |  | Toggles the bot on or off for this group | Yes |
| ./showgroup |  | Shows whether the bot is enabled for this group | No |
| ./addbotmaster | username | Add a user as a bot master | Yes |
| ./removebotmaster | username | Demote a bot master | Yes |
| ./listbotmasters |  | Show a list of the bot masters **Note: non bot masters can use this command if `tell bot masters` is enabled.** | Yes |
| ./tellbotmasters |  | Toggles the ability for regular users to see who the bot masters are | Yes |
| ./istellingbotmasters |  | Show whether regular users can see who the bot masters are by using `./listbotmasters` | Yes |
| ./addtopic | topic | Add a topic to the set of possible positive topics, the topic must be less than the number of characters shown by the `./status` command | Yes |
| ./removetopic | topic | Remove the given topic from the set of possible positive topics | Yes |
| ./listtopics |  | Lists all the (hopefully positive) topics that might be randomly selected | Yes |
| ./change |  | Changes the group topic to a random positive topic | Yes |
| ./ping |  | Responds with a 'pong' as a quick and easy way to see if the bot is running | No |
| ./status |  | Responds with the status of the bot and current settings | No |
| ./help |  | Responds with approximately this message | No |

## Roadmap
* Get people to use [Discord](https://discordapp.com/) instead and drop Skype.
* Add support for other chat services such as [Discord](https://discordapp.com/), IRC, [Telegram](https://telegram.org/), Slack, etc.

## Did this make you happy?
I'm pleased to hear that! You're very welcome. :-)

Although money doesn't buy happiness, I greatly appreciate all donations. My Bitcoin address is `1H3nuby12kEkHyFAKyRa1k6fCTFA7ykoAD`.

## Want to make the world a better place?
Hire me to help you! :-) The local part of my e-mail address is `bitsigned` and domain is `gmail.com`.
