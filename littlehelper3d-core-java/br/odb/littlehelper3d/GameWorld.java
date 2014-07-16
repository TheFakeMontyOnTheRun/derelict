/**
 * 
 */
package br.odb.littlehelper3d;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;
import br.odb.derelict.engine.bzk3.android.AndroidGameActor;
import br.odb.gamelib.gameapp.GameAssetManager;
import br.odb.gamelib.gameworld.exceptions.InvalidSlotException;
import br.odb.libscene.Actor;
import br.odb.libscene.ObjMesh;
import br.odb.libscene.Sector;
import br.odb.libscene.World;
import br.odb.libstrip.MeshFactory;
import br.odb.utils.FileServerDelegate;
import br.odb.utils.Utils;
import br.odb.utils.math.Vec3;

/**
 * @author monty
 * 
 */
public class GameWorld extends World {

	private ObjMesh detailMesh;
	private GameSector[] cachedSectors;
	private HashMap<String, ObjMesh> models = new HashMap<String, ObjMesh>();

	public GameWorld() {

	}

	public void internalize(String path, String detail,
			FileServerDelegate server, GameAssetManager gam ) {

		super.internalize(path, true, server, null);

		if (detail != null) {

			detailMesh = gam.meshForName( detail );
		}
	}

	public ObjMesh getDetailMesh() {
		// TODO Auto-generated method stub
		return detailMesh;
	}

	public void consolidate( GameDelegate delegate ) {
		
		GameDoor thisDoor;
		GameDoor otherDoor;
		

		

		
		
//		Log.d("BZK3", "Consolidating doors");
//		GameSector sector;
//		GameSector another;
//		GameSector another_master;
//		GameSector master;
//		int n;
//		int f;
//		int e;
//
//		for (int c = 0; c < getTotalSectors(); ++c) {
//
//			sector = (GameSector) getSector(c);
//
//			if (sector.isMaster())
//				continue;
//
//			sector.removeAllDoors();
//		}
//
//		for (int c = 0; c < getTotalSectors(); ++c) {
//
//			sector = (GameSector) getSector(c);
//
//			// process leaf nodes
//			sector.cachedParent = getSector( sector.getParent() );
//			master = sector.cachedParent;
//			
//			for (int d = 0; d < 6; ++d) {
//
//				f = Utils.getOppositeDirection( d );
//
//				if (master.meshWalls[d] != null) {
//					sector.meshWalls[d] = master.meshWalls[d];
//				}
//				
//				if (master.getDoor(d) != null) {
//					try {
//						sector.setDoorAt( d, sector.getLink( d ) );
//						sector.getDoor( d ).setMesh( master.getDoor( d ).getMesh() );
//						getSector( sector.getLink( d ) ).setDoorAt( Utils.getOppositeDirection( d ), c );
//						getSector( sector.getLink( d ) ).getDoor( Utils.getOppositeDirection( d ) ).setMesh( sector.getDoor( d ).getMesh() );
//						getSector( sector.getLink( d ) ).getDoor( Utils.getOppositeDirection( d ) ).setLinkedDoor( sector.getDoor( d ) );
//						sector.getDoor( d ).setLinkedDoor( getSector( sector.getLink( d ) ).getDoor( Utils.getOppositeDirection( d ) ) );
//					} catch (InvalidSlotException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} 
//				}
//
//
//				try {
//					n = sector.getLink(d);
//
//					sector.cachedNeighBours[d] = (GameSector) ((n == 0) ? null : getSector(n));
//					
//					another = sector.cachedNeighBours[d];
//					
//					if ( another == null )
//						continue;
//					
//					another_master = getSector( another.getParent() );
//
//					if (sector.getDoor(d) != null) {
//
//						sector.getDoor(d).setSector( n );
//						
//						e = Utils.getOppositeDirection(d);
//						Log.d("BZK3", "dir:" + d + "<->" + e);
//						another = (GameSector) getSector(sector.getLink(d));
//						Log.d("BZK3", "another:" + sector.getLink(d));
//						
//						if (another.getDoor(e) != null) {
//							Log.d("BZK3", "Door consolidated between " + c
//									+ ":" + d + " and " + sector.getLink(d)
//									+ ":" + e);
//							another.setDoorAt(e, c);
//							sector.getDoor(d).setLinkedDoor(another.getDoor(e));
//							another.getDoor(e).setLinkedDoor(sector.getDoor(d));
//						}
//
//						if (master != null && master.getDoor(d) != null) {
//							Log.d("BZK3", "master is " + master.getId() + ":"
//									+ d);
//							((GameDoor) sector.getDoor(d))
//									.setMaster((GameDoor) master.getDoor(d));
//							((GameDoor) sector.getDoor(d).getLinkedDoor())
//									.setMaster((GameDoor) master.getDoor(d));
//							((GameDoor) master.getDoor(d))
//									.addSon((GameDoor) sector.getDoor(d));
//							((GameDoor) master.getDoor(d))
//									.addSon((GameDoor) sector.getDoor(d)
//											.getLinkedDoor());
//						} else {
//							if (another.getDoor(e) != null) {
//								master = (GameSector) getSector(another
//										.getParent());
//								if (master != null && master.getDoor(e) != null) {
//									Log.d("BZK3", "master is " + master.getId()
//											+ ":" + e);
//									((GameDoor) sector.getDoor(d))
//											.setMaster((GameDoor) master
//													.getDoor(e));
//									((GameDoor) sector.getDoor(d)
//											.getLinkedDoor())
//											.setMaster((GameDoor) master
//													.getDoor(e));
//									((GameDoor) master.getDoor(e))
//											.addSon((GameDoor) sector
//													.getDoor(d));
//									((GameDoor) master.getDoor(e))
//											.addSon((GameDoor) sector
//													.getDoor(d).getLinkedDoor());
//								} else {
//									Log.d("BZK3", "1=-(");
//								}
//							} else {
//								Log.d("BZK3", "2=-(");
//							}
//						}
//					}
//				} catch (InvalidSlotException e1) {
//					Log.e("Derelict", "erro: " + e1);
//					e1.printStackTrace();
//				}
//			}
//		}
		
		//create the damn cache
		cachedSectors = new GameSector[getSectorList().size()];
		cachedSectors = getSectorList().toArray(cachedSectors);
//		cachedSectors[ 0 ] = null; //heh
		setSectorList(null);
		System.gc();
		
		
		
		//properly configure all sectors
		for ( GameSector sector : cachedSectors ) {
		
			//skip first sector			
			if ( sector == null ) 
				continue;
			
			sector.cachedParent = cachedSectors[ sector.getParent() ];
//			sector.removeAllDoors();
			
			for ( int f = 0; f < 6; ++f ) {
				
				if ( sector.isMaster() && sector.getDoor( f ) != null )
					sector.getDoor( f ).setDelegate( delegate );
				
				try {					
					if ( sector.getLink( f ) != 0 )
						sector.cachedNeighBours[ f ] = cachedSectors[ sector.getLink( f ) ];
					else
						sector.cachedNeighBours[ f ] = null;
					
				} catch (InvalidSlotException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		
		//have to do it again, to make sure all sectors are configured properly
		
		for ( GameSector sector : cachedSectors ) {
			
			
			//skip first sector
			if ( sector == null ) 
				continue;
			
			if ( sector.isMaster() )
				continue;
						
			for ( int f = 0; f < 6; ++f ) {

				//edge sector
				if ( sector.cachedNeighBours[ f ] == null ) {
				
					if ( sector.cachedParent.meshWalls[ f ] != null ) {
						
						sector.meshWalls[ f ] = sector.cachedParent.meshWalls[ f ];
					}
					
				} else {						
					//edge sector (to this master )
					if ( sector.cachedNeighBours[ f ].cachedParent != sector.cachedParent ) {
						
						if ( sector.getDoor( f ) == null && sector.cachedParent.getDoor( f ) != null ) {
							
							sector.setDoorAt( f, sector.cachedNeighBours[ f ].getId() );
							thisDoor = (GameDoor) sector.getDoor( f ); 
							sector.cachedParent.getDoor( f ).addSon( thisDoor );
							
							//the other side will never have a door...
							sector.cachedNeighBours[ f ].setDoorAt( Utils.getOppositeDirection( f ), sector.getId() );							
							otherDoor = (GameDoor) sector.cachedNeighBours[ f ].getDoor( Utils.getOppositeDirection( f ) );
							thisDoor.setLinkedDoor( otherDoor );
							otherDoor.setLinkedDoor( thisDoor );
							sector.cachedParent.getDoor( f ).addSon( otherDoor );
						}
					}
				}					
			}
			
			sector.silentlyCloseAllDoors();
		}		
		Log.d("BZK3", "doors consolidated");
	}

	final public int getTotalSectors() {

		if (cachedSectors != null)
			return cachedSectors.length;
		else
			return super.getTotalSectors();
	}

	final public GameSector getSector(int n) {

		if (cachedSectors != null)
			return cachedSectors[n];
		else
			return (GameSector) super.getSector(n);
	}

	@Override
	public Iterator<Sector> iterator() {

		if (cachedSectors != null)
			return new Iterator<Sector>() {

				int index = 0;

				@Override
				public boolean hasNext() {

					return index < cachedSectors.length;
				}

				@Override
				public Sector next() {

					Sector toReturn = cachedSectors[index++];
					return toReturn;
				}

				@Override
				public void remove() {

				}

			};
		else
			return super.iterator();
	}

	// public void flushToGLES() {
	// GameSector sector;
	//
	// for (int c = 0; c < getTotalSectors(); ++c) {
	// sector = (GameSector) getSector(c);
	// for (int d = 0; d < 6; ++d) {
	// try {
	// if (sector.getLink(d) == Constants.NO_LINK
	// || sector.getDoor(d) != null) {
	//
	// }
	// } catch (InvalidSlotException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	public void makeDoorAt(FileServerDelegate fileServer, Sector origin,
			int face, int sectorId, String decalName) {

		super.makeDoorAt(origin, face, sectorId, decalName);

		if (origin instanceof GameSector && decalName != null
				&& decalName.length() > 0)
			((GameSector) origin).setDecalAt(fileServer, face, decalName);
	}

	@Override
	public Actor makeActorAt(int sector, int candelas) {
		Log.d("bzk3", "gameactor created");
		AndroidGameActor actor = new AndroidGameActor();
		actor.setCurrentSector(sector);
		actor.setEmissiveLightningIntensity(candelas);
		return actor;
	}

	public void applyDecalTo(FileServerDelegate fileServer, Sector origin,
			int face, String decalName) {

		if (origin instanceof GameSector && decalName != null
				&& decalName.length() > 0)
			((GameSector) origin).setDecalAt(fileServer, face, decalName);
	}

	@Override
	public Sector makeSector(float x0, float x1, float y0, float y1, float z0,
			float z1, MeshFactory factory ) {

		return new GameSector(x0, x1, y0, y1, z0, z1 );
	}

	public int getPlayerActorIndex() {

		return 0;
	}

	public void saveSnapshotAt(OutputStream os) throws IOException {

		os.write(getTotalActors());

		for (int c = 0; c < this.getTotalActors(); ++c) {
			((AndroidGameActor) getActor(c)).writeSnapshot(os);
		}
	}

	public void loadSnapshotAt(InputStream is) throws IOException {

		byte total = (byte) is.read();
		int myTotal = getTotalActors();

		AndroidGameActor actor;

		for (int c = 0; c < total; ++c) {

			if (c < myTotal) {
				actor = (AndroidGameActor) getActor(c);
				actor.loadSnapshot(is);
			}
		}

	}

	public void destroy() {

		super.destroy();

		if (detailMesh != null)
			detailMesh.destroy();

		detailMesh = null;
		cachedSectors = null;

		if (models != null)
			models.clear();

		models = null;
	}

	public void checkpointActors() {

		for (Actor actor : actorList) {
			actor.checkpointPosition();
		}

	}
}
