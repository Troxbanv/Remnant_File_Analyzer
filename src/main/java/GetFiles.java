import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class GetFiles {

    public static HashMap<Configuration_Items, String> getSaveFilePath() {

        String configurationPath = Paths.get("resources/Configure.txt").toAbsolutePath().toString();

        System.out.println(configurationPath);

        HashMap<Configuration_Items, String> result = new HashMap<>();

        FileCommands save = new FileCommands(configurationPath);
        if(save.containsItem("DefaultFile")) {
            result.put(Configuration_Items.FILEPATH, save.getItemString("SavePath") + "/" + save.getItemString("DefaultFile"));
        } else {
            File savedFiles = new File(save.getItemString("SavePath"));
            HashMap<Integer, String> fileList = new HashMap<>();

            System.out.println("Please type the number of the file you would like to analyze.");
            int i = 1;
            for(String file : savedFiles.list()) {
                if(file.endsWith(".sav")) {
                    System.out.print(i + ": ");
                    System.out.println(file);
                    fileList.put(i, file);
                    i++;
                }
            }

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            System.out.println("You chose:");
            System.out.println(fileList.get(choice));

            result.put(Configuration_Items.FILEPATH, save.getItemString("SavePath") + "\\" + fileList.get(choice));
        }

        if(save.containsItem("DefaultType")) {
            result.put(Configuration_Items.TYPE, save.getItemString("DefaultType"));
        } else {
            System.out.println("Which game type do you want me to analyze?");
            System.out.println("1: All");
            System.out.println("2: Campaign");
            System.out.println("3: Adventure");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            String convertedChoice = "";
            switch (choice) {
                case 1:
                    convertedChoice = "All";
                    break;
                case 2:
                    convertedChoice = "Campaign";
                    break;
                case 3:
                    convertedChoice = "Adventure";
                    break;
                default:
                    convertedChoice = "All";
            }

            result.put(Configuration_Items.TYPE, convertedChoice);
        }

        return result;
    }




}
