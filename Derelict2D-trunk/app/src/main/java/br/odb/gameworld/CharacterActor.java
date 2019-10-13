package br.odb.gameworld;

import java.util.HashMap;

import br.odb.gameutils.Updatable;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class CharacterActor implements Updatable {

    private final HashMap<String, Item> items = new HashMap<>();
    private final String name;
    private Location location;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */

    public CharacterActor(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CharacterActor)) {
            return false;
        }
        CharacterActor other = (CharacterActor) obj;
        if (location == null) {
            if (other.location != null) {
                return false;
            }
        } else if (!location.equals(other.location)) {
            return false;
        }
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }

    @Override
    public void update(long milisseconds) {
        for (Item i : items.values()) {
            i.update(milisseconds);
        }
    }

    public Item[] getItems() {
        return items.values().toArray(new Item[0]);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location l) {
        this.location = l;
    }

    public String getName() {
        return name;
    }

    public Item getItem(String itemName) throws ItemNotFoundException {

        if (!items.containsKey(itemName)) {
            throw new ItemNotFoundException();
        }

        return items.get(itemName);
    }

    public void addItem(String name, Item item)
            throws InventoryManipulationException {

        if (items.containsKey(name)) {
            throw new InventoryManipulationException("GENERAL ERROR@CharacterActor.addItem()");
        }

        item.carrier = this;
        items.put(name, item);
    }

    public void removeItem(Item item) {
        items.remove(item.getName());
    }

    public boolean isMovable() {
        return true;
    }

    public ActiveItem toggleItem(String name) throws ItemActionNotSupportedException,
            ItemNotFoundException {

        Item item = getItem(name);

        if (!(item instanceof ActiveItem)) {
            throw new ItemActionNotSupportedException(Item.TOGGLE_DENIAL_MESSAGE);
        }

        ((ActiveItem) item).toggle();

        return (ActiveItem) item;
    }

    public Item useItem(String entry) throws ItemNotFoundException, ItemActionNotSupportedException {

        Item item = getItem(entry);

        item.use(this);

        if (item.isDepleted()) {
            removeItem(item);
        }

        return item;
    }
}
