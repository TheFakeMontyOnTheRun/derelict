package br.odb.gameworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.odb.gameutils.Direction;
import br.odb.gameutils.Updatable;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

/**
 * @author monty
 */
public class Location implements Updatable {

    public Location(String name) {
        this.name = name;
        connections = new Location[6];
        material = new String[6];
    }

    private String[] material = new String[Direction.values().length];
    private Place place;
    private final Door[] door = new Door[Direction.values().length];
    private final ArrayList<Item> items = new ArrayList<>();
    private final String name;
    private final Location[] connections;
    private String description;
    private final List<CharacterActor> characters = new ArrayList<>();
    public boolean hasBeenExplored;
    private String floorId;

    @Override
    public void update(long milisseconds) {

        for (Door d : door) {
            if (d != null) {

                d.update(milisseconds);
            }
        }

        for (Item i : items) {
            i.update(milisseconds);
        }

        for (CharacterActor a : characters) {
            a.update(milisseconds);
        }
    }

    @Override
    public String toString() {

        StringBuilder collectables = new StringBuilder();
        StringBuilder connections = new StringBuilder();

        if (items.size() > 0) {

            collectables.append("\nObjects available in current location:");
        }

        collectables.append("\n");
        for (Item i : this.items) {

            //This is getting ugly...but does make some sense in the bigger picture. Plasma pellets move too fast.
            if (i.location != this) {
                continue;
            }
            collectables.append("- ");
            collectables.append(i);
            collectables.append("\n");
        }

        connections.append("\nPlaces to go:");
        connections.append("\n");
        for (Direction d : Direction.values()) {
            if (this.connections[d.ordinal()] != null) {
                connections.append(d).append(" : ").append(this.connections[d.ordinal()].getName());
                connections.append(". \n");
            }
        }

        return description + "\n" + collectables + connections;
    }

    public Location setCollectables(Item[] collectableItems) {

        for (Item i : collectableItems) {
            addItem(i);
        }

        return this;
    }

    public Item[] getCollectableItems() {

        return items.toArray(new Item[0]);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + Arrays.hashCode(door);
        result = prime * result + Arrays.hashCode(material);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        if (!Arrays.equals(connections, other.connections)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (!Arrays.equals(door, other.door)) {
            return false;
        }
        if (!Arrays.equals(material, other.material)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (place == null) {
            return other.place == null;
        } else return place.equals(other.place);
    }

    public Direction getConnectionDirectionForLocation(Location location)
            throws InvalidSlotException {

        if (location == null) {
            throw new InvalidSlotException();
        }

        for (Direction d : Direction.values()) {
            if (connections[d.ordinal()] == location) {
                return d;
            }
        }

        throw new InvalidSlotException();
    }

    public Location setDescription(String description) {
        this.description = description;

        return this;
    }

    public void setConnected(Direction d, Location location) {

        connections[d.ordinal()] = location;
        door[d.ordinal()] = new Door();

    }

    public void addCharacter(CharacterActor character) {
        characters.add(character);
        character.setLocation(this);
        hasBeenExplored = true;
    }

    public void removeCharacter(CharacterActor character) {
        characters.remove(character);
        character.setLocation(null);
    }

    public Location addItem(Item item) {
        items.add(item);
        item.location = this;
        return this;
    }

    public Location[] getConnections() {

        return connections;
    }

    public Item getItem(String obj) throws ItemNotFoundException {

        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(obj)) {
                return i;
            }
        }

        throw new ItemNotFoundException();
    }

    private void removeItem(Item item) throws ItemNotFoundException {

        if (!items.contains(item)) {
            throw new ItemNotFoundException();
        }

        items.remove(item);
    }

    public boolean hasConnection(String entry) {

        for (Location l : connections) {
            if (l != null && l.getName().equalsIgnoreCase(entry)) {
                return true;
            }
        }

        return false;
    }

    public Door getDoor(Direction d) {
        return this.door[d.ordinal()];
    }

    public Item giveItemTo(String itemName, CharacterActor c)
            throws InventoryManipulationException, ItemNotFoundException,
            ItemActionNotSupportedException {
        Item item = getItem(itemName);

        if (!item.isPickable()) {
            throw new ItemActionNotSupportedException(Item.PICK_DENIAL_MESSAGE);
        }

        c.addItem(item.getName(), item);
        c.getLocation().removeItem(item);

        return item;
    }

    public Item takeItemFrom(String itemName, CharacterActor c)
            throws ItemNotFoundException {
        Item item = c.getItem(itemName);
        c.removeItem(item);
        c.getLocation().addItem(item);

        return item;
    }

    public boolean hasExploredNeighbour() {

        boolean toReturn = false;

        for (Location l : connections) {
            if (l != null) {

                toReturn = toReturn || l.hasBeenExplored;
            }
        }
        return toReturn;
    }

    public String getFloor() {

        return floorId;
    }

    public Location setFloorId(String floorId) {
        this.floorId = floorId;
        return this;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    protected Door[] getDoors() {
        return door;
    }
}
