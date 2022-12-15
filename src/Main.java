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
        String queriesFilePath = Paths.get(".").normalize().toAbsolutePath() + "/src/databases/" + queriesFile;
        Main.createTables(new File(queriesFilePath));
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
    }
}
