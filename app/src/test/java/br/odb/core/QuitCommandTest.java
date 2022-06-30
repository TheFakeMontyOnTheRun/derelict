package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.commands.QuitCommand;

public class QuitCommandTest {

	@Test
	public void test() {
		DummyApplicationClient client = new DummyApplicationClient();
		Assert.assertTrue( client.isConnected() );
		client.sendQuit();
		Assert.assertFalse( client.isConnected() );
		
		client.connected = true;
		
		QuitCommand cmd = new QuitCommand();
		
		Assert.assertTrue( client.isConnected() );
		try {
			cmd.run( null, null, null, client);
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.assertFalse( client.isConnected() );
		Assert.assertEquals( 0, cmd.requiredOperands() );
	}
}
