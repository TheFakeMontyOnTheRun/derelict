/**
 * 
 */
package br.odb.derelict.core;

import br.odb.gameapp.ApplicationClient;
import br.odb.utils.ScheduledEvent;
import br.odb.gameworld.Location;

/**
 * @author monty
 * 
 */
public class ScheduledSpookySound extends ScheduledEvent {

	private String uri;
	private Location location;
	private String alt;
	ApplicationClient client;

	public ScheduledSpookySound(ApplicationClient client, long time,
			String uri, String alt, Location location) {
		super(null, time);

		this.client = client;
		this.uri = uri;
		this.alt = alt;
		this.location = location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gamelib.gameapp.ScheduledEvent#run()
	 */
	@Override
	public void run() {
		super.run();

		if (client != null) {
			client.alert( alt + "( near " + location.getName() +  " )" );
			client.playMedia(uri, alt);
		}
	}

}
