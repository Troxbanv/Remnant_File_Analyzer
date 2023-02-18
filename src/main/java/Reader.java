import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Reader {

    private final File saveFile;
    FileCommands subSections;
    FileCommands eventsFix;

    public Reader(File saveFile) {
        this.saveFile = saveFile;

        subSections = new FileCommands(Paths.get("resources/SubLocations.txt").toAbsolutePath().toString());
        eventsFix = new FileCommands(Paths.get("resources/Events.txt").toAbsolutePath().toString());
    }

    public HashMap<GameType, ArrayList<Event>> readFile() {

        LinkedHashMap<Integer, String> myMap = new LinkedHashMap<>();
        HashMap<GameType, ArrayList<Event>> result = new HashMap<>();

        String fileContent = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile))) {

            String line = "";
            int i = 1;
            while((line = bufferedReader.readLine()) != null) {

                if(line.contains("/Game/Campaign_Main/Quest_Campaign") || line.contains("/Game/World_City/Quests/Quest_AdventureMode")) {
                    myMap.put(i, line);
                }

                i++;
                String stop = "";
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int advminCount = Integer.MAX_VALUE;
        int advlocation = Integer.MAX_VALUE;
        int camMinCount = Integer.MAX_VALUE;
        int camLocation = Integer.MAX_VALUE;
        for(Map.Entry<Integer, String> string : myMap.entrySet()) {

            if(string.getValue().contains("/Game/Campaign_Main/Quest_Campaign")){
                if(camLocation > string.getValue().indexOf("/Game/Campaign_Main/Quest_Campaign")) {
                    camLocation = string.getValue().indexOf("/Game/Campaign_Main/Quest_Campaign");

                    if(camLocation < camMinCount) {
                        ArrayList<Event> found = findThings(string.getValue());
                        if(!found.isEmpty()) {
                            result.put(GameType.CAMPAIGN, found);
                            camMinCount = camLocation;
                        }
                    }
                }
            }

            if(string.getValue().contains("Quest_AdventureMode")){
                if(advlocation > string.getValue().indexOf("Quest_AdventureMode")) {
                    advlocation = string.getValue().indexOf("Quest_AdventureMode");

                    if(advlocation < advminCount) {
                        ArrayList<Event> found = findThings(string.getValue());
                        if(!found.isEmpty()) {
                            result.put(GameType.ADVENTURE, found);
                            advminCount = advlocation;
                        }
                    }
                }
            }
        }

        return result;
    }

    public ArrayList<Event> findThings(String string) {

        ArrayList<Event> events = new ArrayList<>();
        String test[] = string.split("/Game");

        for(String current : test) {

            String world = "";
            String eventType = "";
            String eventName = "";
            String fixedName = "";
            String location = "";
            boolean inSmallDungeon = false;

            if(current.contains("World_City")) {
                world = "Earth";
            } else if (current.contains("World_Wasteland")) {
                world = "Rhom";
            } else if (current.contains("World_Jungle")) {
                world = "Yaesha";
            } else if (current.contains("World_Swamp")) {
                world = "Corsus";
            }

            if(world.equals("")) {
                continue;
            }

            if (current.contains("SmallD")) {
                eventType = "Side Dungeon";
                eventName = current.split("/")[3].split("_")[2];
                location = subSections.getItemString(eventName);
                inSmallDungeon = true;
            }
            if (current.contains("OverworldPOI")) {
                eventType = "Point of Interest";
                eventName = current.split("/")[3].split("_")[2];
                location = "Main";
                inSmallDungeon = true;
            }
            if (current.contains("Quest_Boss")) {
                eventType = "World Boss";
                eventName = current.split("/")[3];
                eventName = eventName.substring(eventName.lastIndexOf("_") + 1);
                fixedName = eventsFix.getItemString(eventName);
                location = subSections.getItemString(eventName);
            }
            if (current.contains("Siege")) {
                eventType = "Siege";
                eventName = current.split("/")[3].split("_")[2];
                location = subSections.getItemString(eventName);
            }
            if (current.contains("Mini")) {
                eventType = "Miniboss";
                eventName = current.split("/")[3].split("_")[2];
                fixedName = eventsFix.getItemString(eventName);
                location = subSections.getItemString(eventName);
            }
            if(current.contains("Quest_Event")) {
                eventType = "Item";
                eventName = current.split("/")[3].split("_")[2];
                location = subSections.getItemString(eventName);
            }

            if(!eventType.equals("")) {
                events.add(new Event(world, eventType, eventName, fixedName, location, inSmallDungeon));
            }
        }

        events = eliminateDuplicates(events);

        return events;
    }

    private ArrayList<Event> eliminateDuplicates(ArrayList<Event> list) {

        ArrayList<Event> result = new ArrayList<>();

        for(Event current : list) {
            if(!result.contains(current)) {
                result.add(current);
            }
        }

        return result;
    }
}
