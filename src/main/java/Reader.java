import java.io.*;
import java.util.*;

public class Reader {

    private final File saveFile;
    FileCommands subSections;
    FileCommands eventsFix;

    public Reader(File saveFile) {
        this.saveFile = saveFile;
/*
        subSections = new FileCommands("C:\\Users\\troxbanv\\IdeaProjects\\Remnant\\src\\main\\resources\\SubLocations.txt");
        eventsFix = new FileCommands("C:\\Users\\troxbanv\\IdeaProjects\\Remnant\\src\\main\\resources\\Events.txt");
*/
        subSections = new FileCommands(".\\SubLocations.txt");
        eventsFix = new FileCommands(".\\Events.txt");
    }

    public HashMap<GameType, String> readFile() {

        LinkedHashMap<Integer, String> myMap = new LinkedHashMap<>();
        HashMap<GameType, String> result = new HashMap<>();

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
                        result.put(GameType.CAMPAIGN, string.getValue());
                        camMinCount = camLocation;
                    }
                }
            }

            if(string.getValue().contains("Quest_AdventureMode")){
                if(advlocation > string.getValue().indexOf("Quest_AdventureMode")) {
                    advlocation = string.getValue().indexOf("Quest_AdventureMode");

                    if(advlocation < advminCount) {
                        result.put(GameType.ADVENTURE, string.getValue());
                        advminCount = advlocation;
                    }
                }
            }

//            System.out.println("Count: " + location);
//            System.out.println("Line No: " + string.getKey());
//            System.out.println("Line: " + string.getValue());
        }

        return result;
    }

    public void findThings(String string) {

        ArrayList<event> events = new ArrayList<>();
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
                events.add(new event(world, eventType, eventName, fixedName, location, inSmallDungeon));
            }
        }

        events = eliminateDuplicates(events);

        for(event cur : events) {
             cur.print();
        }

        String stop = "";
    }

    private ArrayList<event> eliminateDuplicates(ArrayList<event> list) {

        ArrayList<event> result = new ArrayList<>();

        for(event current : list) {
            if(!result.contains(current)) {
                result.add(current);
            }
        }

        return result;
    }

    private class event {

        public String world;
        public String eventType;
        public String eventName;
        public String fixedName;
        public String location;
        public boolean smallDungeon;

        public event(String world, String eventType, String eventName, String fixedName, String location, boolean smallDungeon) {
            this.world = world;
            this.eventType = eventType;
            this.eventName = eventName;
            this.fixedName = fixedName;
            this.location = location;
            this.smallDungeon = smallDungeon;
        }

        public void print() {
            System.out.println("World: " + world);
            System.out.println("Event Type: " + eventType);
            System.out.println("Event Name: " + eventName);
            System.out.println("Fixed Name: " + fixedName);
            System.out.println("Location: " + location);
            System.out.println("Small Dungeon: " + smallDungeon);
            System.out.println();
            System.out.println();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            event event = (event) o;
            return smallDungeon == event.smallDungeon &&
                    Objects.equals(world, event.world) &&
                    Objects.equals(eventType, event.eventType) &&
                    Objects.equals(eventName, event.eventName) &&
                    Objects.equals(fixedName, event.fixedName) &&
                    Objects.equals(location, event.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(world, eventType, eventName, fixedName, location, smallDungeon);
        }
    }
}
