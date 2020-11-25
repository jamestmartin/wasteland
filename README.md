# Wasteland Plugin Suite
This is the plugin suite I am developing for my work-in-progress Minecraft server, Wasteland.

Wasteland is a post-nuclear zombie apocalypse server with a gameplay experience dramatically different from any other.
It includes custom world generation, a custom (sophisticated!) spawn algorithm, a radiation system,
world and weather events, a player rank system based on the Marines, handles chat,
and includes many, many other supplementary features.

The server is spiritual successor to the servers Skuli_Steinulf and I used to run.
Huge credit goes to Skuli_Steinulf for founding it all, and he and my friends Lyokan and Obamallama for many ideas and much help.

## The Wasteland Plugin
I don't feel like documenting all of this plugin's features right now; I'll do it later.

## The Wasteland Permissions Plugin
This plugin was motivated because existing permissions were either
* buggy,
* incompatible with the `/reload` command,
* not fully compatible with Java 9+ (LuckPerms in particular), or
* encompassed a broader scope than I wanted, interfering with other features.

So I implemented my own, an extremely simple plugin which loads groups and player permissions from a YAML config file.
It only took two or three hours to write, but is exactly what I need and does the job perfectly.

For documentation on how to use this plugin, please see the default `config.yml`.

## The Wasteland ModTools plugin
**This plugin is currently only partially implemented.**

This plugin provides tools which both help moderators track what rule infractions a player has committed,
and help server administrators hold moderators accountable through an audit log.

This plugin provides these basic commands:
* `/ban`
* `/kick`
* `/mute`
* `/warn`

All bans, kicks, mutes, and warnings are logged,
both to help moderators track what rule infractions a player has committed,
and to help server administrators hold moderators accountable through an audit log.

It is also possible to set the maximum length of bans or mutes that a moderator can issue
in the configuration file.

You may view the currently active sentences against any player,
the infractions that a moderator has issued,
or see the history of infractions a player has committed using `/infractions query`.
This command does not include infractions that have been cleared from a player's record,
or old versions of infractions from before they were commuted.

You can commute, extend, or update a sentence using `/infractions commute`,
remove and clear an infraction from a player's record using `/infractions clear`
or remove a sentence without clearing the infraction from the player's record using `/infractions appeal`.
These commands do not remove the infraction from the audit log, and in fact create a new entry in the audit log.

You may view the audit log using `/auditlog`.
The audit log includes infractions that have been cleared, appealed, or commuted,
the history of those actions.

For more information on how to use these commands please see the in game `/help` or the `plugin.yml`
For more information on how to configure this plugin, please see the default `config.yml`.

### Manuals
There are two built-in manuals: `/rules` and `/faq`.
These manuals provide both brief overviews and more detail which players may view if they wish.

It is also possible for moderators to (forcibly) show players portions of the rules or faq,
as a brief reminder of the rules or to quickly answer a question they were asking.

Please see the in-game `/help` menu or `plugin.yml` for more information on how to use these commands.
