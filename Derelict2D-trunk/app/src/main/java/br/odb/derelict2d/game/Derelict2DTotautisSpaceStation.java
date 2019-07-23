package br.odb.derelict2d.game;

import java.io.IOException;

import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.FileServerDelegate;
import br.odb.libsvg.SVGGraphic;
import br.odb.libsvg.SVGParsingUtils;

public class Derelict2DTotautisSpaceStation extends GameLevel {

	private final TotautisSpaceStation station;
	private SVGGraphic heroGraphic;

	public Derelict2DTotautisSpaceStation( FileServerDelegate delegate) {
		this( new TotautisSpaceStation(), delegate );

	}
	
	public Derelict2DTotautisSpaceStation( TotautisSpaceStation station, FileServerDelegate delegate) {
		super();
		
		this.station = station;
		
		try {
			heroGraphic = SVGParsingUtils.readSVG( delegate.openAsInputStream( "overview-map/astronaut-icon.svg" ) );			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
