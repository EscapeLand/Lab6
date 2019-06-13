package lab6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonkeyReadFile extends ReadFile {
  private final String monkey = "monkey=<(" + "\\d*,\\d*,((R->L)|(L->R)),\\d*)>";
  private final String numoflad = "n=(\\d*)";
  private final String lenoflad = "h=(\\d*)";

  private Pattern pattern;
  private Matcher matcher;
  private String[] str;

  private MonkeyFactory factory;
  private int num;
  private int len;

  public MonkeyReadFile(MonkeyFactory factory) {
    this.factory = factory;
  }

  public int num() {
    return this.num;
  }

  public int len() {
    return this.len;
  }

  @Override
  public void dealWithInput(String cur) {
    String text = cur.replaceAll("\\s*", "");
    
    pattern = Pattern.compile(monkey);
    matcher = pattern.matcher(text);
    while (matcher.find()) {
      str = matcher.group(1).split(",");
      factory.monkey(str);
    }

    pattern = Pattern.compile(numoflad);
    matcher = pattern.matcher(text);
    if (matcher.find()) {
      num = Integer.valueOf(matcher.group(1));
    }

    pattern = Pattern.compile(lenoflad);
    matcher = pattern.matcher(text);
    if (matcher.find()) {
      len = Integer.valueOf(matcher.group(1));
    }
  }

  public static void main(String[] args) {
  }
}
