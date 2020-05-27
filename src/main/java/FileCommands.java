import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileCommands {

    private final String filePath;
    private Map<String, String> items;

    public FileCommands(String filePath) {
        this.filePath = filePath;
        items = new HashMap<>();
        loadMap();
    }

    public boolean containsItem(String name) {
        return items.containsKey(name);
    }

    public String getItemString(String name) {
        return items.get(name);
    }

    private void loadMap() {

        File subLocationsFile = new File(filePath);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(subLocationsFile))) {

            String line;
            while((line = bufferedReader.readLine()) != null) {
                if(!line.startsWith("#") && line.contains("=")) {
                    String sections[] = line.split("=");
                    items.put(sections[0], sections[1]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}