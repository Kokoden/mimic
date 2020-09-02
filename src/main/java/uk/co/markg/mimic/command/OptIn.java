package uk.co.markg.mimic.command;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import disparse.discord.jda.DiscordRequest;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Populate;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.co.markg.mimic.database.ChannelRepository;
import uk.co.markg.mimic.database.UserRepository;
import uk.co.markg.mimic.db.tables.pojos.Channels;
import uk.co.markg.mimic.markov.MarkovSender;

public class OptIn {
  private static final Logger logger = LogManager.getLogger(OptIn.class);
  private MessageReceivedEvent event;
  private UserRepository userRepo;
  private ChannelRepository channelRepo;

  /**
   * Required for static invokation of savehistory. Injecting other dependencies is unnecessary for
   * this case.
   */
  private OptIn() {
  }

  /**
   * Command execution method held by Disparse
   *
   * @param request     The discord request dispatched to this command
   * @param userRepo    The user repository used to communicate with the database
   * @param channelRepo The channel repository used to communicate with the database
   */
  @Populate
  public OptIn(DiscordRequest request, UserRepository userRepo, ChannelRepository channelRepo) {
    this.event = request.getEvent();
    this.userRepo = userRepo;
    this.channelRepo = channelRepo;
  }

  /**
   * Command execution method held by Disparse
   */
  @CommandHandler(commandName = "opt-in", description = "Opt-in for your messages to be read.",
      roles = "Active")
  public void optInCommand() {
    logger.info("Starting opt-in");
    this.execute();
  }

  /**
   * Executes the command. If the user is already opted in a message is returned to discord.
   * Otherwise the user is opted in.
   */
  private void execute() {
    long userid = event.getAuthor().getIdLong();
    if (userRepo.isUserOptedIn(userid)) {
      MarkovSender.alreadyOptedIn(event.getChannel());
    } else {
      optInUser(userid);
    }
  }

  /**
   * Opts in a user by saving their id to the database and saving their history from all added
   * channels to the database.
   *
   * @param userid the discord userid of the user
   */
  private void optInUser(long userid) {
    userRepo.save(userid);
    MarkovSender.optedIn(event.getChannel());
    var channels = channelRepo.getAll();
    for (Channels channel : channels) {
      var textChannel = event.getJDA().getTextChannelById(channel.getChannelid());
      if (textChannel != null && channel.getReadPerm()) {
        new HistoryGrabber(textChannel, List.of(userid)).execute();
      }
    }
  }
}