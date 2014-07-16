package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Place;

public class ItemsCommand extends DerelictUserMetaCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
			ApplicationClient client) {

		Astronaut hero = (Astronaut) actor;

		client.printNormal("\ninventory:"); 

		for (Item i : hero.getItems()) {
			client.printNormal("- "+ i); 
		}
		client.printNormal("end of inventory\n"); 
	}

	@Override
	public String toString() {
		return "items"; 
	}

    @Override
    public int requiredOperands() {
        return 0;
    }
        
	@Override
	public String getHelp() {
		return "- list all items currently held.";
	}
        
}
