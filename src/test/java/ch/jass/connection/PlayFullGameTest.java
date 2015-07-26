package ch.jass.connection;

import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.jass.jassbot.RandomSchieberJassbot;
import ch.jass.model.schieber.table.SchieberScore;
import ch.jass.model.schieber.table.TeamScore;

public class PlayFullGameTest {

	private RandomSchieberJassbot bot1;
	private RandomSchieberJassbot bot2;
	private RandomSchieberJassbot bot3;
	private RandomSchieberJassbot bot4;

	private boolean broadcastGameFinishedReceived = false;
	private boolean broadcastWinnerTeamReceived = false;

	@Before
	public void setup() {
		URI endpointURI = PropertyFileHelper.getJassServerEndpointUri();
		WebsocketClientEndpoint socket1 = new WebsocketClientEndpoint(endpointURI);
		WebsocketClientEndpoint socket2 = new WebsocketClientEndpoint(endpointURI);
		WebsocketClientEndpoint socket3 = new WebsocketClientEndpoint(endpointURI);
		WebsocketClientEndpoint socket4 = new WebsocketClientEndpoint(endpointURI);

		this.bot1 = new RandomSchieberJassbot(socket1, "bot1"){
			
			@Override
			public void broadcastGameFinished(final SchieberScore pointsWonAllTeams) {
				broadcastGameFinishedReceived = true;
			}

			@Override
			public void broadcastWinnerTeam(final TeamScore pointsWonByWinnerTeam) {
				broadcastWinnerTeamReceived = true;
			}
		};
		this.bot2 = new RandomSchieberJassbot(socket2, "bot2");
		this.bot3 = new RandomSchieberJassbot(socket3, "bot3");
		this.bot4 = new RandomSchieberJassbot(socket4, "bot4");
	}

	@Test
	public void executeTest() {
		StartGame();
		Assert.assertTrue(broadcastGameFinishedReceived);
		Assert.assertTrue(broadcastWinnerTeamReceived);
	}

	private void StartGame() {
		try {
			bot1.connectToServer();
			Thread.sleep(1000);
			bot2.connectToServer();
			// Thread.sleep(1000);
			bot3.connectToServer();
			// Thread.sleep(1000);
			bot4.connectToServer();
			// wait 10 seconds for messages from websocket
			Thread.sleep(4000);
		} catch (InterruptedException ex) {
			System.err.println("InterruptedException exception: " + ex.getMessage());
		}
	}
}
