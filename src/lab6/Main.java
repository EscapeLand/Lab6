package lab6;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Main {

  public static void logger() throws SecurityException, IOException {
    Logger log = Logger.getLogger("log");
    FileHandler handler = new FileHandler("log.txt");
    handler.setFormatter(new SimpleFormatter());
    log.addHandler(handler);
    log.setUseParentHandlers(false);
    log.setLevel(Level.INFO);
  }
  
  public static void v1() throws Exception {
    //read XML
    File file = new File("src/lab6/Index.xml");
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = builder.parse(file);
    int n = Integer.valueOf(doc.getElementsByTagName("n").item(0).getFirstChild().getNodeValue());
    int h = Integer.valueOf(doc.getElementsByTagName("h").item(0).getFirstChild().getNodeValue());
    int t = Integer.valueOf(doc.getElementsByTagName("t").item(0).getFirstChild().getNodeValue());
    int N = Integer.valueOf(doc.getElementsByTagName("N").item(0).getFirstChild().getNodeValue());
    int k = Integer.valueOf(doc.getElementsByTagName("k").item(0).getFirstChild().getNodeValue());
    int MV = Integer.valueOf(doc.getElementsByTagName("MV").item(0).getFirstChild().getNodeValue());
    
    //logger
    logger();
    
    //environment configuration
    Scene scene = new Scene(n, h);
    MonkeyFactory factory = new MonkeyFactory(n,h,t,N,k,MV, scene);
    factory.start();
  }

  public static void v3() throws Exception {
    //Set<Monkey> monkeys = Collections.synchronizedSet(new TreeSet<>((a, b) -> a.compareTo(b)));
    MonkeyFactory factory = new MonkeyFactory();
    // logger 
    logger();

    // read file
    MonkeyReadFile rf = new MonkeyReadFile(factory);
    rf.channelReadFile("doc/Competition_3.txt");
    Scene scene = new Scene(rf.num(), rf.len());
    factory.setScene(scene);

    // run
    factory.start();
  }

  public static void main(String[] args) throws Exception {
    v1();
  }
}
