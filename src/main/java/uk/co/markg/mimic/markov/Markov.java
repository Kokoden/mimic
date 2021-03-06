package uk.co.markg.mimic.markov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.markg.mimic.database.MessageRepository;

public class Markov {

  private static final Logger logger = LogManager.getLogger(Markov.class);
  private static final WeightedCollection SENTENCE_ENDS = getSentenceEnds();
  private static final String END_WORD = "END_WORD";
  private static final List<String> VALID_END_WORD_STOPS = List.of("?", "!", ".");
  private static final Kryo kryo = initKryo();

  private Map<String, WeightedCollection> wordMap;
  private Set<String> startWords;
  private Set<String> endWords;

  /**
   * Creates a {@link uk.co.markg.mimic.markov.Markov Markov} instance.
   * 
   * @param inputs The list of strings to be parsed
   */
  private Markov(List<String> inputs) {
    wordMap = new HashMap<>();
    startWords = new HashSet<>();
    endWords = new HashSet<>();
    parseInput(inputs);
  }

  /**
   * Initialises a custom {@link com.esotericsoftware.kryo.Kryo Kryo} serializer.
   * 
   * @return The initialised {@link com.esotericsoftware.kryo.Kryo Kryo} instance
   */
  private static Kryo initKryo() {
    Kryo kryo = new Kryo();
    kryo.register(Markov.class);
    kryo.register(HashSet.class);
    kryo.register(HashMap.class);
    kryo.register(WeightedCollection.class);
    kryo.register(WeightedElement.class);
    kryo.register(ArrayList.class);
    return kryo;
  }

  private Markov() {
  }

  /**
   * Creates a collection of sentence ends with probabilities taken from a subset of user messages.
   * 
   * @return The collection of sentence ends
   */
  private static WeightedCollection getSentenceEnds() {
    var collection = new WeightedCollection();
    collection.add(new WeightedElement(".", 0.4369));
    collection.add(new WeightedElement("!", 0.1660));
    collection.add(new WeightedElement("?", 0.2733));
    collection.add(new WeightedElement("!!", 0.0132));
    collection.add(new WeightedElement("??", 0.0114));
    collection.add(new WeightedElement("!?", 0.0027));
    collection.add(new WeightedElement("...", 0.0965));
    return collection;
  }

  /**
   * Creates a new {@link uk.co.markg.mimic.markov.Markov Markov} instance with the saved user
   * messages loaded from the {@link uk.co.markg.mimic.database.MessageRepository
   * MessageRepository}.
   * 
   * @param userid   The target user
   * @param serverid The discord server the user is from
   * @return The {@link uk.co.markg.mimic.markov.Markov Markov} instance containing the saved user
   *         messages
   */
  public static Markov load(long userid, long serverid) {
    return load(List.of(userid), serverid);
  }


  /**
   * Creates a new {@link uk.co.markg.mimic.markov.Markov Markov} instance with the saved user
   * messages loaded from the {@link uk.co.markg.mimic.database.MessageRepository
   * MessageRepository}. Allows batch loading.
   * 
   * @param userids  The list of users
   * @param serverid The discord server the users are from
   * @return The {@link uk.co.markg.mimic.markov.Markov Markov} instance containing the saved users
   *         messages
   */
  public static Markov load(List<Long> userids, long serverid) {
    logger.info("Loaded chain for {}", userids);
    var inputs = MessageRepository.getRepository().getByUsers(userids, serverid);
    return new Markov(inputs);
  }

  /**
   * Creates a new {@link uk.co.markg.mimic.markov.Markov Markov} instance loaded from the file.
   * 
   * @param f The file containing the saved messages
   * @return The {@link uk.co.markg.mimic.markov.Markov Markov} instance containing the saved users
   *         messages
   * @throws IOException If the file is not found
   */
  public static Markov load(File f) throws IOException {
    logger.info("Loaded from file");
    Input input = new Input(new FileInputStream(f.getName()));
    Markov markov = kryo.readObject(input, Markov.class);
    input.close();
    return markov;
  }

  /**
   * Saves the {@link uk.co.markg.mimic.markov.Markov Markov} instance onto a file.
   * 
   * @param file The file
   * @throws IOException
   */
  public void save(File file) throws IOException {
    Output output = new Output(new FileOutputStream(file.getName()));
    kryo.writeObject(output, this);
    output.close();
  }

  /**
   * Convenience method to generate multiple sentences.
   * 
   * @return The sentences joined together by a space character
   */
  public String generateRandom() {
    int sentences = ThreadLocalRandom.current().nextInt(5) + 1;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sentences; i++) {
      sb.append(generate()).append(" ");
    }
    return sb.toString();
  }

  /**
   * Gets a word the Markov sentence can start with. If the String given is a valid start word the
   * method returns the argument. If it isn't the method will return a random start word from the
   * database.
   * 
   * @param start Start word provided from the user
   * @return The start word provided or chosen
   */
  private String getStartWord(String start) {
    if (!start.isEmpty() && wordMap.containsKey(start)) {
      return start;
    }
    int startNo = ThreadLocalRandom.current().nextInt(startWords.size());
    Iterator<String> itr = startWords.iterator();
    for (int i = 0; i < startNo; i++) {
      itr.next();
    }
    return itr.next();
  }

  /**
   * Generates a sentence from the markov chain with a given start word.
   * 
   * @param start The provided start word
   * @return The generated markov chain
   */
  public String generate(String start) {
    String word = getStartWord(start);
    List<String> sentence = new ArrayList<>();
    sentence.add(word);
    boolean endWordHit = false;
    while (!endWordHit) {
      var nextEntry = wordMap.get(word);
      word = nextEntry.getRandom().map(WeightedElement::getElement).orElse("");
      if (endWords.contains(word)) {
        endWordHit = true;
      }
      if (word.equals(END_WORD)) {
        break;
      }
      sentence.add(word);
    }
    String s = String.join(" ", sentence);
    logger.debug("Generated: {}", s);
    if (s.matches("(.*[^.!?`+>\\-=_+:@~;'#\\[\\]{}\\(\\)\\/\\|\\\\]$)")) {
      s = s + SENTENCE_ENDS.getRandom().map(WeightedElement::getElement).orElse("@@@@@@@");
    }
    if (!start.isEmpty() && !s.startsWith(start)) {
      s = start + " " + s;
    }
    return s;
  }

  /**
   * Generates a sentence from the markov chain
   * 
   * @return A complete sentence
   */
  public String generate() {
    return generate("");
  }

  /**
   * Convenience method to parse multiple sentences
   * 
   * @param inputs The list of sentences
   */
  private void parseInput(List<String> inputs) {
    for (String input : inputs) {
      parseInput(input);
    }
  }

  /**
   * Parses a sentence into the wordMap
   * 
   * @param input The sentence to parse
   */
  private void parseInput(String input) {
    String[] tokens = input.split("\\s+\\v?");
    if (tokens.length < 3) {
      throw new IllegalArgumentException(
          "Input '" + input + "'is too short. Must be greater than 3 tokens.");
    }
    for (int i = 0; i < tokens.length; i++) {
      String word = tokens[i];
      if (word.isEmpty()) {
        continue;
      }
      if (i == 0) {
        startWords.add(word);
      } else if (isEndWord(word)) {
        endWords.add(word);
        insertWordFrequency(word, END_WORD);
        continue;
      }
      if (i == tokens.length - 1) {
        insertWordFrequency(word, END_WORD);
        break;
      }
      String nextWord = tokens[i + 1];
      if (nextWord.isEmpty()) {
        continue;
      }
      if (wordMap.containsKey(word)) {
        updateWordFrequency(word, nextWord);
      } else {
        insertWordFrequency(word, nextWord);
      }
    }
  }

  /**
   * Checks whether a word can be matched as an end word. i.e. the word ends a sentence.
   * 
   * @param word The word to check
   * @return True if the word can be matched as an end word
   */
  private boolean isEndWord(String word) {
    for (String stop : VALID_END_WORD_STOPS) {
      if (word.endsWith(stop)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Inserts a new word and follow word into the wordMap
   * 
   * @param word       The main word
   * @param followWord The follow word
   */
  private void insertWordFrequency(String word, String followWord) {
    var wc = new WeightedCollection();
    wc.add(new WeightedElement(followWord, 1));
    wordMap.put(word, wc);
  }

  /**
   * Updates the follow word frequency of a word in the wordMap
   * 
   * @param key        The main word
   * @param followWord The follow word
   */
  private void updateWordFrequency(String key, String followWord) {
    var followFrequency = wordMap.get(key);
    followFrequency.get(followWord).ifPresentOrElse(
        fw -> followFrequency.update(fw, fw.getWeight() + 1),
        () -> followFrequency.add(new WeightedElement(followWord, 1)));
  }
}
