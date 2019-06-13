import static java.lang.System.exit;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Main {
  static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
  static Logger logger;
  private static FileHandler fh;

  static {
    final String logPath = "out/log/";
    logger = Logger.getLogger("MonkeyGenerator.GeneralLogger");
    logger.getParent().getHandlers()[0].setLevel(Level.OFF);
    try {
      File lp = new File(logPath);
      if (!lp.exists()) {
        if (lp.mkdir()) {
          exit(0);
        }
      }
      fh = new FileHandler(logPath + "monkey.log", true);
      java.util.logging.Formatter fm = new Formatter() {
        @Override
        public String format(LogRecord record) {
          return record.getInstant() + "\t" + record.getLevel() + "\n" + record.getMessage() + "\n";
        }
      };
      fh.setFormatter(fm);
      logger.addHandler(fh);

    } catch (IOException e) {
      e.printStackTrace();
      exit(1);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    MonkeyGenerator.start();
    fh.close();
  }
}
