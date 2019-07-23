package br.odb.libsvg;

import java.util.ArrayList;

import br.odb.gameutils.Color;
import br.odb.gameutils.math.Vec2;

/**
 * 
 * @author Daniel "Monty" Monteiro
 */
public class SVGUtils {

	private static final char PATHCMD_ABSOLUTE_MOVE = 'M';
	private static final char PATHCMD_ABSOLUTE_LINE = 'L';
	private static final char PATHCMD_ABSOLUTE_CURVE = 'C';
	private static final char PATHCMD_ABSOLUTE_END = 'Z';
	private static final char PATHCMD_RELATIVE_MOVE = 'm';
	private static final char PATHCMD_RELATIVE_LINE = 'l';
	private static final char PATHCMD_RELATIVE_CURVE = 'c';
	private static final char PATHCMD_RELATIVE_END = 'z';

	private static float penX = 0;
	private static float penY = 0;

	public static ColoredPolygon parseD(String attribute, int width, int height) {
		
		char[] tokens = { PATHCMD_ABSOLUTE_LINE, PATHCMD_ABSOLUTE_MOVE,
				PATHCMD_RELATIVE_END, PATHCMD_ABSOLUTE_END, ' ',
				PATHCMD_RELATIVE_MOVE, PATHCMD_RELATIVE_LINE, ',',
				PATHCMD_RELATIVE_CURVE, PATHCMD_ABSOLUTE_CURVE };

		ColoredPolygon pol = new ColoredPolygon();
		int nearer = 0;
		StringBuilder currentToken = new StringBuilder();
		String split1 = null;
		String split2 = null;
		ArrayList<String> pendingTokens = new ArrayList<>();
		ArrayList<String> brokenTokens = new ArrayList<>();
		String lastCmd = String.valueOf(PATHCMD_ABSOLUTE_MOVE);
		String currentCmd = String.valueOf(PATHCMD_ABSOLUTE_MOVE);
		penX = 0;
		penY = 0;
		float checkX = 0;
		float checkY = 0;
		int vertexPair = 0;

		// separa os tokens, acumulando os caracteres e adicionando na lista ao
		// encontrar um separador
		for (int c = 0; c < attribute.length(); ++c) {

			if (attribute.charAt(c) == ' ' || attribute.charAt(c) == ',') {

				if (currentToken.toString().trim().length() > 0)
					pendingTokens.add(currentToken.toString().trim());

				currentToken = new StringBuilder();
			} else {
				currentToken.append(attribute.charAt(c));
			}
		}
		// adiciona o ultimo token acumulado, caso ele seja
		// válido.
		if (attribute.charAt(attribute.length() - 1) != ' '
				|| attribute.charAt(attribute.length() - 1) != ',')
			if (currentToken.toString().trim().length() > 0)
				pendingTokens.add(currentToken.toString().trim());

		// para cada token encontrado...
		while (pendingTokens.size() > 0) {

			// obtem e remove da fila
			currentToken = new StringBuilder(pendingTokens.get(0));
			pendingTokens.remove(0);

			// se
			// não
			// é
			// imediatamente um comando
			if (currentToken.length() > 1) {
				for (char token : tokens) {

					// procura ver se tem algum comando no meio.
					nearer = currentToken.toString().indexOf(token);

					if (nearer != -1) {
						if (nearer == 0) {
							if (currentToken.length() > 1) {
								split1 = currentToken.substring(0, 1);
								split2 = currentToken.substring(1);
								pendingTokens.add(0, split2);
								currentToken = new StringBuilder(split1);
							}
						} else {
							split1 = currentToken.substring(0, nearer);
							split2 = currentToken.substring(nearer);
							pendingTokens.add(0, split2);
							currentToken = new StringBuilder(split1);
						}
					}
				}
			}

			brokenTokens.add(currentToken.toString());
		}

		while (brokenTokens.size() > 0) {

			currentToken = new StringBuilder(brokenTokens.get(0));
			brokenTokens.remove(0);

			if (currentToken.charAt(0) == PATHCMD_RELATIVE_MOVE
					|| currentToken.charAt(0) == PATHCMD_ABSOLUTE_MOVE
					|| currentToken.charAt(0) == PATHCMD_ABSOLUTE_CURVE
					|| currentToken.charAt(0) == PATHCMD_RELATIVE_CURVE
					|| currentToken.charAt(0) == PATHCMD_RELATIVE_LINE
					|| currentToken.charAt(0) == PATHCMD_ABSOLUTE_LINE) {

				vertexPair = 0;
				split1 = brokenTokens.get(0);
				brokenTokens.remove(0);

				split2 = brokenTokens.get(0);
				brokenTokens.remove(0);

				checkX = penX;
				checkY = penY;
				currentCmd = currentToken.toString();

			} else if (currentToken.charAt(0) != PATHCMD_RELATIVE_END
					&& currentToken.charAt(0) != PATHCMD_ABSOLUTE_END) {

				split1 = currentToken.toString();

				if (brokenTokens.size() > 0) {

					split2 = brokenTokens.get(0);
					brokenTokens.remove(0);
				} else {
					split2 = null;
				}
				currentCmd = lastCmd;

			}

			switch (currentCmd.charAt(0)) {
			// comandos absolutos
			case PATHCMD_ABSOLUTE_MOVE:
			case PATHCMD_ABSOLUTE_CURVE:
			case PATHCMD_ABSOLUTE_LINE:

				penX = Float.parseFloat(split1);
				penY = Float.parseFloat(split2);
				break;

			case PATHCMD_RELATIVE_CURVE:
				penX = checkX + Float.parseFloat(split1);
				penY = checkY + Float.parseFloat(split2);
				break;

			case PATHCMD_RELATIVE_MOVE:
			case PATHCMD_RELATIVE_LINE:
				try {

					penX += Float.parseFloat(split1);
					penY += Float.parseFloat(split2);
				} catch (Exception e) {
					System.out.println("erro no parsing..." + split1 + " e "
							+ split2);
				}

				break;
			}

			if (currentCmd.charAt(0) == PATHCMD_ABSOLUTE_CURVE
					|| currentCmd.charAt(0) == PATHCMD_RELATIVE_CURVE) {

				if (vertexPair == 1)
					pol.controlPoints.set(pol.npoints - 1,
							(new Vec2(penX, penY)));
				else
					pol.addPoint(penX, penY);

				vertexPair++;

				if (vertexPair == 3) {
					checkX = penX;
					checkY = penY;
					vertexPair = 0;
				}
			}

			// finalmente temos um ponto sendo formado.
			if (currentCmd.charAt(0) == PATHCMD_RELATIVE_LINE
					|| currentCmd.charAt(0) == PATHCMD_ABSOLUTE_LINE
					|| currentCmd.charAt(0) == PATHCMD_RELATIVE_MOVE
					|| currentCmd.charAt(0) == PATHCMD_ABSOLUTE_MOVE) {

				pol.addPoint(penX, penY);
				vertexPair++;
			}

			lastCmd = currentCmd;

		}

		return pol;
	}

	public static ColoredPolygon CullXNoMoreThan(float aSpan,
			ColoredPolygon aSrc) {
		ColoredPolygon pa = new ColoredPolygon();
		pa.color = aSrc.color;
		pa.z = aSrc.z;
		pa.originalStyle = aSrc.originalStyle;

		int qtd = aSrc.npoints;
		if (qtd == 0) {
			return pa;
		}
		boolean lastinside = (aSrc.xpoints[qtd - 1] < aSpan);
		float DX;
		int prev;
		float a2, b1, b2;
		for (int c = 0; c < qtd; c++) {

			if (aSrc.xpoints[c] < aSpan) {

				if (!lastinside) {

					prev = c - 1;
					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					b1 = aSrc.xpoints[c] - aSpan;
					b2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					DX = aSrc.ypoints[c] - ((b1 * b2) / a2);

					pa.addPoint((int) aSpan, (int) DX);
					lastinside = true;
				}

				pa.addPoint(aSrc.xpoints[c], aSrc.ypoints[c]);
			} else {
				if (lastinside) {
					prev = c - 1;

					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					b1 = aSrc.xpoints[c] - aSpan;
					b2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					DX = aSrc.ypoints[c] - ((b1 * b2) / a2);
					pa.addPoint((int) aSpan, (int) DX);
					lastinside = false;
				}
			}
		}
		return pa;

	}

	public static ColoredPolygon CullXNoLessThan(float aSpan,
			ColoredPolygon aSrc) {
		ColoredPolygon pa = new ColoredPolygon();
		pa.color = aSrc.color;
		pa.z = aSrc.z;
		pa.originalStyle = aSrc.originalStyle;
		int qtd = aSrc.npoints;
		if (qtd == 0) {
			return pa;
		}
		boolean lastinside = (aSrc.xpoints[qtd - 1] > aSpan);
		float DX;
		int prev;
		float a2, b1, b2;
		for (int c = 0; c < qtd; c++) {
			if (aSrc.xpoints[c] > aSpan) {
				if (!lastinside) {

					prev = c - 1;
					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					b1 = aSrc.xpoints[c] - aSpan;
					b2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					DX = aSrc.ypoints[c] - ((b1 * b2) / a2);

					pa.addPoint((int) aSpan, (int) DX);
					lastinside = true;
				}
				pa.addPoint(aSrc.xpoints[c], aSrc.ypoints[c]);
			} else {
				if (lastinside) {
					prev = c - 1;

					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					b1 = aSrc.xpoints[c] - aSpan;
					b2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					DX = aSrc.ypoints[c] - ((b1 * b2) / a2);

					pa.addPoint((int) aSpan, (int) DX);
					lastinside = false;
				}
			}
		}
		return pa;

	}

	public static ColoredPolygon CullYNoMoreThan(float aSpan,
			ColoredPolygon aSrc) {
		ColoredPolygon pa = new ColoredPolygon();
		pa.color = aSrc.color;
		pa.z = aSrc.z;
		pa.originalStyle = aSrc.originalStyle;
		int qtd = aSrc.npoints;
		if (qtd == 0) {
			return pa;
		}
		boolean lastinside = (aSrc.ypoints[qtd - 1] < aSpan);
		float DY;
		int prev;
		float a2, b1, b2;
		for (int c = 0; c < qtd; c++) {
			if (aSrc.ypoints[c] < aSpan) {

				if (!lastinside) {

					prev = c - 1;
					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					b1 = aSrc.ypoints[c] - aSpan;
					b2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					DY = aSrc.xpoints[c] - ((b1 * b2) / a2);
					pa.addPoint((int) DY, (int) aSpan);
					lastinside = true;
				}

				pa.addPoint(aSrc.xpoints[c], aSrc.ypoints[c]);
			} else {
				if (lastinside) {
					prev = c - 1;

					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;

					a2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					b1 = aSrc.ypoints[c] - aSpan;
					b2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					DY = aSrc.xpoints[c] - ((b1 * b2) / a2);
					pa.addPoint((int) DY, (int) aSpan);
					lastinside = false;
				}
			}
		}
		return pa;
	}

	public static ColoredPolygon CullYNoLessThan(float aSpan,
			ColoredPolygon aSrc) {
		ColoredPolygon pa = new ColoredPolygon();
		pa.color = aSrc.color;
		pa.z = aSrc.z;
		pa.originalStyle = aSrc.originalStyle;
		int qtd = aSrc.npoints;
		if (qtd == 0) {
			return pa;
		}
		boolean lastinside = (aSrc.ypoints[qtd - 1] > aSpan);
		float DY;
		int prev;
		float a2, b1, b2;
		for (int c = 0; c < qtd; c++) {
			if (aSrc.ypoints[c] > aSpan) {
				if (!lastinside) {

					prev = c - 1;
					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;
					a2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					b1 = aSrc.ypoints[c] - aSpan;
					b2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					DY = aSrc.xpoints[c] - ((b1 * b2) / a2);
					pa.addPoint((int) DY, (int) aSpan);
					lastinside = true;
				}

				pa.addPoint(aSrc.xpoints[c], aSrc.ypoints[c]);
			} else {
				if (lastinside) {
					prev = c - 1;

					while (prev < 0) {
						prev += qtd;
					}
					prev = prev % qtd;
					a2 = aSrc.ypoints[c] - aSrc.ypoints[prev];
					b1 = aSrc.ypoints[c] - aSpan;
					b2 = aSrc.xpoints[c] - aSrc.xpoints[prev];
					DY = aSrc.xpoints[c] - ((b1 * b2) / a2);
					pa.addPoint((int) DY, (int) aSpan);
					lastinside = false;
				}
			}
		}
		return pa;
	}

	// Gotta delete those two...
	static class ord {
		float v;
		int i;

	}

	public static void order(float[] xv, int[] xi) {

		ord[] _xi = new ord[xi.length];

		for (int f = 0; f < xi.length; ++f) {
			_xi[f] = new ord();
			_xi[f].i = xi[f];
			_xi[f].v = xv[f];
		}

		int size = xv.length;
		int menori = 0;
		float menorv = xv[menori];
		ord tmp;

		for (int c = 0; c < size; ++c) {

			menori = c;
			menorv = _xi[menori].v;

			for (int d = c + 1; d < size; ++d) {

				if (menorv > _xi[d].v) {

					menorv = _xi[d].v;
					menori = d;

				}

			}

			tmp = _xi[c];
			_xi[c] = _xi[menori];
			_xi[menori] = tmp;
		}

		for (int e = 0; e < size; ++e) {
			xv[e] = _xi[e].v;
			xi[e] = _xi[e].i;
		}
	}

	private static String getValueForProperty(String in) {
		String fill;
		String attribute = new String(in);
		int indexOf = attribute.indexOf("fill");
		attribute = attribute.substring(indexOf);

		if (attribute.contains(";"))
			attribute = attribute.substring(0, attribute.indexOf(";"));

		fill = attribute.substring(attribute.indexOf(":") + 1).trim();

		return fill;
	}

	public static Color parseColorFromStyle(String original) {
		return parseColorFromStyle(original, "fill", "opacity");
	}

	public static String parseGradientFromStyle(String style) {

		String fill = getValueForProperty(style);
		String toReturn = null;

		if (fill != null && fill.charAt(0) != '#' && fill.startsWith("url")) {
			// 0-2: url
			// 3: (
			// 4:#
			toReturn = fill.substring(5, fill.indexOf(")")).trim();
		}

		return toReturn;
	}

	public static Color parseColorFromStyle(String original,
			String colorString, String opacityString) {
		String fill;
		Color color = new Color();
		String attribute = new String(original);

		int indexOf = attribute.indexOf(colorString);

		if (indexOf == -1) {
			return new Color(0, 0, 0);
		}

		attribute = attribute.substring(indexOf);

		if (attribute.contains(";"))
			attribute = attribute.substring(0, attribute.indexOf(";"));

		fill = attribute.substring(attribute.indexOf(":") + 1).trim();

		if (fill.length() >= 3 && fill.substring(0, 3).equalsIgnoreCase("rgb")) {
			fill = fill.substring(fill.indexOf('(') + 1).trim();
			String r = fill.substring(0, fill.indexOf(',')).trim();
			fill = fill.substring(fill.indexOf(',') + 1).trim();
			String g = fill.substring(0, fill.indexOf(',')).trim();
			fill = fill.substring(fill.indexOf(',') + 1).trim();
			String b = fill.substring(0, fill.indexOf(')')).trim();

			color = new Color(Integer.parseInt(r), Integer.parseInt(g),
					Integer.parseInt(b));

		} else {
			try {

				fill = fill.substring(fill.indexOf('#') + 1).trim();
				if (!fill.trim().equals("none")) {

					String r = fill.substring(0, 2);
					String g = fill.substring(2, 4);
					String b = fill.substring(4, 6);

					color = new Color(Integer.parseInt(r, 16),
							Integer.parseInt(g, 16), Integer.parseInt(b, 16));
				}
			} catch (Exception ignored) {
			}

		}

		attribute = new String(original);
		indexOf = attribute.indexOf(opacityString);
		if (indexOf != -1) {

			attribute = attribute.substring(indexOf);

			if (attribute.contains(";"))
				attribute = attribute.substring(0, attribute.indexOf(";"));
			fill = attribute.substring(attribute.indexOf(":") + 1).trim();
			fill = fill.substring(fill.indexOf('#') + 1).trim();
			if (!fill.trim().equals("none")) {
				color.a = ((int) (255 * Float.parseFloat(fill)));
			}
		}

		return color;
	}
}
