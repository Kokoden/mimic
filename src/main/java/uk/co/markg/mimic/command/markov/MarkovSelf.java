package uk.co.markg.mimic.command.markov;

import disparse.discord.jda.DiscordRequest;
import disparse.parser.reflection.CommandHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.co.markg.mimic.database.ChannelRepository;
import uk.co.markg.mimic.database.UsageRepository;
import uk.co.markg.mimic.database.UserRepository;
import uk.co.markg.mimic.markov.Markov;
import uk.co.markg.mimic.markov.MarkovSender;

public class MarkovSelf {

  @CommandHandler(commandName = "self",
      description = "Generate a random number of sentences from your own messages!")
  public static void execute(DiscordRequest request, ChannelRepository channelRepo,
      UserRepository userRepo) {
    MessageReceivedEvent event = request.getEvent();
    if (!channelRepo.hasWritePermission(event.getChannel().getIdLong())) {
      return;
    }
    long userid = event.getAuthor().getIdLong();
    if (!userRepo.isUserOptedIn(userid)) {
      MarkovSender.notOptedIn(event.getChannel());
      return;
    }
    if (!userRepo.isMarkovCandidate(userid)) {
      MarkovSender.notMarkovCandidate(event.getChannel());
      return;
    }
    UsageRepository.getRepository().save(MarkovSelf.class, event);
    event.getChannel().sendTyping().queue();
    MarkovSender.sendMessageWithDelay(event, Markov.load(userid).generateRandom());
  }
}