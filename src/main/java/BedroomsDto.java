import java.util.HashMap;
import java.util.Map;

public class BedroomsDto {

    private int uniqueRoomGroups;
    private int numberOfChildren;
    private Map<String, Integer> rooms = new HashMap<>();

    public BedroomsDto(){
        this.setNumberOfChildren(0);
        this.setUniqueRoomGroups(0);
        this.setRooms(new HashMap<>());
    }

    public BedroomsDto(int uniqueRoomGroups, int numberOfChildren, Map<String, Integer> bedroomsForAdultsPerCapacity){
        this.uniqueRoomGroups = uniqueRoomGroups;
        this.numberOfChildren = uniqueRoomGroups;
        this.rooms = bedroomsForAdultsPerCapacity;
    }

    public void setUniqueRoomGroups(int uniqueRoomGroups) {
        this.uniqueRoomGroups = uniqueRoomGroups;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public void setRooms(Map<String, Integer> bedroomsForAdultsPerCapacity) {
        this.rooms = bedroomsForAdultsPerCapacity;
    }

    public int getUniqueRoomGroups() {
        return uniqueRoomGroups;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

}
