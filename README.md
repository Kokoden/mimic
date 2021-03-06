# Mimic

Mimic Bot is a discord bot built originally for [The Programmer's Hangout](https://theprogrammershangout.com/) that creates sentences based on it's users.

## Add me!

Invite link coming soon!

Server admins/owners will need to run the configuration command before using the bot. If you want to lock the `mimic!opt-in` command behind a role use `mimic!config -o yourRoleName`. If you do not want to lock the opt-in command behind a role use `mimic!config`.

### My required permissions

* Read Message History
* Send Messages
* Use External Emojis

## Features

* Generates sentences using a markov chain 
* Users can opt-in/opt-out
* Admins can add/remove/list channels with customised permissions per channel

### Commands

Command | Description | Example
------------ | ------------- | ------------- 
about | Displays info about the bot | `mimic!about`
all | Generate a random number of sentences from all opted in user messages | `mimic!all`
allstats | Display statistics for all users | `mimic!allstats`
channels | Lists all readable channels | `mimic!channels`
channels.add | Adds a channel. Default to read only access | `mimic!channels add 123456789 -r -w`
channels.edit | Edits added channel permissions. Collects user message history if read permission granted and deletes it if revoked. | `mimic!channels edit 123456789`
channels.full | Lists all channels registered | `mimic!channels full`
channels.remove | Removes a channel from the database. All related messages are also removed. | `mimic!channels remove 123456789`
help | show all commands or detailed help of one command | `mimic!help` or `mimic!help about`
opt-in | Opt-in for your messages to be read | `mimic!opt-in`
opt-out | Opt-out for all messages to be removed | `mimic!opt-out`
rand | Generate a random number of sentences from random user's messages! | `mimic!rand`
self | Generate a random number of sentences from your own messages! | `mimic!self` 
start | Provide the start of a sentence and let mimic finish it! | `mimic!start I like to`
stats | Display statistics of your messages | `mimic!stats`

### Privacy

Mimic is explicitly opt-in. 
* Messages from users that are not opted-in will not be stored.
* Message edits from opted-in users will be reflected in the database.
* Messages that are deleted by opted-in users will be deleted from the database.
* Opting out will remove all user messages and user details from the database.
* Users that are opted-in and leave a server will have their details and messages deleted.