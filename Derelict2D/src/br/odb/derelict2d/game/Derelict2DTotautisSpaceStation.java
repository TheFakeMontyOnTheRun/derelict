package br.odb.derelict2d.game;

import java.io.IOException;

import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.libsvg.SVGGraphic;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.utils.FileServerDelegate;

public class Derelict2DTotautisSpaceStation extends GameLevel {

	public TotautisSpaceStation station;
	public SVGGraphic heroGraphic;

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
