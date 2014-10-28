/**
 * 
 */
package br.odb.derelict.core.items;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

/**
 * @author monty
 * 
 */
public class Book extends Item {

	public final String title;


	public Book(String name, String content) {
		super("book");
		
		setDescription(  "Excerpt: " + content );

		this.title = name;
	}
	
	@Override
	public String toString() {
	
		return "Book. Title: '" + title + "', " + getDescription();
	}
	
	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "This is not *usable*. But might contain useful information nonetheless" );
	}
	
	
	@Override
	public void useWith(Item item) throws ItemActionNotSupportedException {
		if (item instanceof Destructive ) {
			this.setDescription( "A badly burn't book" );
			this.setIsDepleted(true);
		} else {
			throw new ItemActionNotSupportedException( "This is not *usable*. But might contain useful information nonetheless" );
		}
	}	
}
