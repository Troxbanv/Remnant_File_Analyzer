import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        String pathStart = "C:\\Users\\troxbanv\\Desktop\\Remnant Saves\\";
        String path;
        HashMap<Configuration_Items, String> items = new HashMap<>();

        if(args.length == 0) {
            items = GetFiles.getSaveFilePath();
            path = items.get(Configuration_Items.FILEPATH);
        } else {
            path = args[0];
        }

        String choice = items.get(Configuration_Items.TYPE);

        System.out.println("Starting!");

        Reader reader = new Reader(new File(path));

        HashMap<GameType, String> world = reader.readFile();

        if(choice.equalsIgnoreCase("All") || choice.equalsIgnoreCase("Campaign")) {
            System.out.println("Campaign Items");
            System.out.println();
            reader.findThings(world.get(GameType.CAMPAIGN));
            System.out.println();
            System.out.println();
        }

        if(choice.equalsIgnoreCase("All") || choice.equalsIgnoreCase("Adventure")) {
            System.out.println("Adventure Items");
            System.out.println();
            reader.findThings(world.get(GameType.ADVENTURE));
        }
    }
}
