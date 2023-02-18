import java.util.Objects;

public class Event {

    private String world;
    private String eventType;
    private String eventName;
    private String fixedName;
    private String location;
    private boolean smallDungeon;

    public Event(String world, String eventType, String eventName, String fixedName, String location, boolean smallDungeon) {
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
        Event event = (Event) o;
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
