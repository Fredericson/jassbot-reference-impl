package ch.jass.jassbot;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import ch.jass.model.Card;
import ch.jass.model.Color;
import ch.jass.model.Trumpf;
import ch.jass.model.schieber.AbstractJassBot;
import ch.jass.model.schieber.api.SchieberServerService;

public class RandomSchieberJassbot extends AbstractJassBot {

	private static final Logger LOGGER = Logger.getLogger(RandomSchieberJassbot.class.getName());

	private final Set<Card> rememberPlayedCards = new HashSet<Card>();

	public RandomSchieberJassbot(final SchieberServerService schieberService, final String playerName) {
		super(schieberService, playerName);
	}

	@Override
	public void requestCardRejected(final Card card) {
		LOGGER.info("ActualTrumpf is: " + getSchieberTableInfo().getActualTrumpf());
		LOGGER.info("ActualTrumpfablePlayer is: " + getSchieberTableInfo().getActualTrumpfablePlayer());
		LOGGER.info("AllPlayedCards are: " + getSchieberTableInfo().getAllPlayedCards());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Card chooseCard(final Color requestedColor) {
		if (mustPlayTrumpf(requestedColor)) {
			return pickingRandomCard(getTrumpfCards());
		}
		if (canPlayTrumpf()) {
			return getHighestTrumpfCard();
		}
		Set<Card> cardsForRequestedColor = getOtherCards(requestedColor);
		if (requestedColor == null || cardsForRequestedColor.size() == 0) {
			return pickingRandomCard(getOtherCards());
		}
		return pickingRandomCard(cardsForRequestedColor);
	}

	private boolean mustPlayTrumpf(final Color requestedColor) {
		return getTrumpfCards().size() > 0 && requestedColor != null
				&& requestedColor == getSchieberTableInfo().getActualTrumpf().getColor();
	}

	@Override
	public Trumpf chooseTrumpf(final boolean geschoben) {

		Trumpf[] trumpfValues = Trumpf.values();
		int item = new Random().nextInt(trumpfValues.length);
		if (item == trumpfValues.length) {
			return null;
		}
		return trumpfValues[item];
		// int topDownStich countTopDownStich();
		// int possibleTopDownStich possibleTopDownStich();
		// TODO Auto-generated method stub
	}

	@Override
	public void rememberPlayedCard(final Card card) {
		rememberPlayedCards.add(card);
	}

	private Card pickingRandomCard(final Set<Card> cards) {
		int size = cards.size();
		int item = new Random().nextInt(size);
		int i = 0;
		for (Card card : cards) {
			if (i == item) {
				return card;
			}
			i = i + 1;
		}
		return null;
	}
}
