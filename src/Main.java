import app.MainScreen;
import databases.DbConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Main class for running whole project
 */
public class Main {
    private static final String queriesFile = "queries.sql";
    private static final String DELIMITER = ";";
    private static final Connection dbConnection = DbConnection.getConnection();

    private static void createTables(File file) {
        Scanner scanner;
        try {
            scanner = new Scanner(file).useDelimiter(DELIMITER);
        } catch (FileNotFoundException ase) {
            ase.printStackTrace();
            return;
        }
        Statement currentStatement = null;
        while (scanner.hasNext()) {
            String rawStatement = scanner.next().trim();
            if (!rawStatement.equals("") && !rawStatement.startsWith("--")) {
                try {
                    currentStatement = dbConnection.createStatement();
                    currentStatement.execute(rawStatement + DELIMITER);
                } catch (SQLException ase) {
                    ase.printStackTrace();
                } finally {
                    if (currentStatement != null) {
                        try {
                            currentStatement.close();
                        } catch (SQLException ase) {
                            ase.printStackTrace();
                        }
                    }
                    currentStatement = null;
                }
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        String queriesFilePath = Paths.get(".").normalize().toAbsolutePath() + "/" + queriesFile;
        Main.createTables(new File(queriesFilePath));
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
    }
}

//java -Dfile.encoding=UTF-8 -classpath /usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jfr.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/rt.jar:/home/shubham/Desktop/Classes/CS611/exam/trueOnlineBank/out/production/trueOnlineBank:/home/shubham/Desktop/Classes/CS611/exam/trueOnlineBank/bin/sqlite-jdbc-3.40.0.0.jar:/home/shubham/.m2/repository/com/jgoodies/forms/1.2.1/forms-1.2.1.jar Main
//javac -classpath /usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/charsets.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/dnsns.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/icedtea-sound.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/jaccess.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/localedata.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/nashorn.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunec.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/ext/zipfs.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jce.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jfr.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/jsse.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/management-agent.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/resources.jar:/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/rt.jar:/home/shubham/Desktop/Classes/CS611/exam/trueOnlineBank/out/production/trueOnlineBank:/home/shubham/Desktop/Classes/CS611/exam/trueOnlineBank/bin/sqlite-jdbc-3.40.0.0.jar:/home/shubham/.m2/repository/com/jgoodies/forms/1.2.1/forms-1.2.1.jar Main