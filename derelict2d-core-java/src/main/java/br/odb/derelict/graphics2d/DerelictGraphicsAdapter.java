package br.odb.derelict.graphics2d;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.items.PlasmaGun;
import br.odb.derelict.core.items.PlasmaPellet;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.ItemNotFoundException;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.math.Vec2;

/**
 * 
 * @author monty
 */
public class DerelictGraphicsAdapter {

	public static final DisplayList parse(DerelictGame game,
			AssetManager resManager) {

		TotautisSpaceStation station = game.station;
		DisplayList dl = new DisplayList("game-display-list");
		ArrayList<RenderingNode> nodes = new ArrayList<RenderingNode>();

		SVGRenderingNode heroNode = new SVGRenderingNode(
				resManager.getGraphics("heroGraphic"), "heroGraphic");
		RenderingNode newNode;
		RenderingNode item;
		Location location;

		int offset = 0;

		// SVGRenderingNode backdrop = new SVGRenderingNode(
		// resManager.getGraphics( "backdrop" ), "backdrop" );
		// nodes.add( backdrop );

		SVGGraphic stationGraphics = resManager.getGraphics(station
				.getAstronaut().getLocation().getFloor());

		for (ColoredPolygon cp : stationGraphics.shapes) {

			try {
				location = station.getLocation(cp.id);
			} catch (InvalidLocationException e1) {
				continue;
			}

			if (!location.hasBeenExplored && !location.hasExploredNeighbour()) {
				continue;
			}

			newNode = new SVGRenderingNode(new SVGGraphic(
					stationGraphics.getShapesStartingWith(cp.id)), cp.id);
			nodes.add(newNode);

			ColoredPolygon active;

			for (Item i : location.getCollectableItems()) {

				offset += 10;

				item = new SVGRenderingNode( resManager.getGraphics(i.getName()).scaleTo( 32, 32), i.getName());

				if (i instanceof ActiveItem) {

					System.out.println("is " + i.getName() + " active?");

					active = ((SVGRenderingNode) item).graphic
							.getShapeById("active");

					System.out.println("active is " + active);

					if (active != null) {
						System.out.println("active");
						active.visible = ((ActiveItem) i).isActive();
						System.out.println("caught");
					}

				}

				nodes.add(item);
				item.translate
						.set(cp.getCenter().add(new Vec2(offset, offset)));
			}

			offset = 0;

			if (station.getAstronaut().getLocation().getName().equals(cp.id)) {
				heroNode.translate.set(cp.getCenter());
			}

			try {
				PlasmaGun plasmaGun = (PlasmaGun) station.getItem("plasma-gun");

				for (PlasmaPellet pp : plasmaGun.firedPellets) {
					if (pp.location == location && !pp.isDepleted() ) {
						item = new SVGRenderingNode(
								resManager.getGraphics("plasma-gun"),
								"plasma-gun");
						nodes.add(item);
						item.translate.set(cp.getCenter().add(
								new Vec2(offset, offset)));

					}
				}

			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// backdrop.translate = heroNode.translate;

		nodes.add(heroNode);

		dl.setItems(nodes.toArray(new RenderingNode[nodes.size()]));
		dl.translate.set(0.0f, 0.0f);
		return dl;
	}
}
