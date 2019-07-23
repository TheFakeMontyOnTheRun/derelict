package br.odb.derelict.core;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.Location;

/**
 * @author monty
 * 
 */
class ScheduledSpookySound  {

	private final String uri;
	private final Location location;
	private final String alt;
	private final ApplicationClient client;

	public ScheduledSpookySound(ApplicationClient client, long time,
			String uri, String alt, Location location) {


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
	public void run() {


		if (client != null) {
			client.alert( alt + "( near " + location.getName() +  " )" );
			client.playMedia(uri, alt);
		}
	}

}
