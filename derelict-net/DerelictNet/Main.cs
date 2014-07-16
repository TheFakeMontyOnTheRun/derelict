using System;
using br.odb.derelict.core;

namespace DerelictNet
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			
			br.odb.derelict.core.DerelictGame game = new br.odb.derelict.core.DerelictGame();
			game.createDefaultClient().setAppName("DERELICT1D")
				.setAuthorName("Daniel Monteiro")
				.setLicenseName("3 Clause BSD").setReleaseYear(2014).start();
		}
	}
}
