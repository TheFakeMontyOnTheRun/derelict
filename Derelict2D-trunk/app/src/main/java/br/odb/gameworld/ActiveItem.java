package br.odb.gameworld;

public class ActiveItem extends Item {

    private boolean active;

    public String getTurnOnSound() {
        return "click";
    }

    public String getTurnOffSound() {
        return "click";
    }

    protected ActiveItem(String name) {
        super(name);

        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean newState) {
        active = newState;
    }

    public ActiveItem activate() {
        active = true;
        return this;
    }

    @Override
    public String toString() {
        return (active ? "(active)" : "(inactive)") + super.toString();
    }

    public ActiveItem toggle() {
        active = !active;
        return this;
    }
}
