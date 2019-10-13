package br.odb.derelict2d.graphics2d;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.items.PlasmaGun;
import br.odb.derelict.core.items.PlasmaPellet;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.gamerendering.rendering.TextNode;
import br.odb.gameutils.Color;
import br.odb.gameutils.math.Vec2;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.ItemNotFoundException;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGGraphic;

public class DerelictGraphicsAdapter {

	private final HashMap<String, RenderingNode> cache = new HashMap<>();

	public final DisplayList parse(DerelictGame game, AssetManager resManager,
								   boolean showText) {

		TotautisSpaceStation station = game.station;
		DisplayList dl = new DisplayList("game-display-list");
		ArrayList<RenderingNode> nodes = new ArrayList<>();

		SVGRenderingNode heroNode = new SVGRenderingNode(
				resManager.getGraphics("heroGraphic"), "heroGraphic");
		RenderingNode newNode;
		RenderingNode item;
		Location location;

		int offset = 0;

		SVGGraphic stationGraphics = resManager.getGraphics(station
				.getAstronaut().getLocation().getFloor());

		for (ColoredPolygon cp : stationGraphics.shapes) {

			try {
				System.out.println("location: " + cp.id);

				try {
					location = station.getLocation(cp.id);
				} catch (Exception e) {
					continue;
				}

				if (!location.hasBeenExplored
						&& !location.hasExploredNeighbour()) {
					continue;
				}

				if (cache.containsKey(cp.id)) {
					newNode = cache.get(cp.id);
				} else {

					newNode = new SVGRenderingNode(new SVGGraphic(
							stationGraphics.getShapesStartingWith(cp.id)),
							cp.id);
					cache.put(cp.id, newNode);
				}

				nodes.add(newNode);

				ColoredPolygon active;

				for (Item i : location.getCollectableItems()) {

					System.out.println("object" + i.getName());

					offset += 10;

					if (cache.containsKey(i.getName())) {
						item = cache.get(i.getName());
					} else {

						item = new SVGRenderingNode(resManager.getGraphics(
								i.getName()).scaleTo(32, 32), i.getName());
						cache.put(i.getName(), item);
					}

					if (i instanceof ActiveItem) {

						active = ((SVGRenderingNode) item).graphic
								.getShapeById("active");

						if (active != null) {
							active.visible = ((ActiveItem) i).isActive();
						}
					}

					nodes.add(item);
					item.translate.set(cp.getCenter().add(
							new Vec2(offset, offset)));
				}
				if (showText) {

					Color c = new Color(255, 255, 255, 196);
					TextNode textNode = new TextNode(cp.id + "_label",
							(cp.id.substring(0, 1).toUpperCase() + cp.id
									.substring(1)), c, 25);

					Vec2 textPosition = new Vec2();
					textPosition.y = cp.getCenter().y;
					textPosition.x = cp.getCenter().x;

					textNode.translate.set(textPosition);
					nodes.add(textNode);
				}

				offset = 0;

				if (station.getAstronaut().getLocation().getName()
						.equals(cp.id)) {
					heroNode.translate.set(cp.getCenter());
				}

				try {
					PlasmaGun plasmaGun = (PlasmaGun) station
							.getItem("plasma-gun");

					for (PlasmaPellet pp : plasmaGun.firedPellets) {
						if (pp.location == location && !pp.isDepleted()) {
							item = new SVGRenderingNode(
									resManager.getGraphics("plasma-pellet"),
									"plasma-pellet");
							nodes.add(item);
							item.translate.set(cp.getCenter().add(
									new Vec2(offset, offset)));

						}
					}

				} catch (ItemNotFoundException e) {
					e.printStackTrace();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		nodes.add(heroNode);

		dl.setItems(nodes.toArray(new RenderingNode[0]));
		dl.translate.set(0.0f, 0.0f);
		return dl;
	}
}
