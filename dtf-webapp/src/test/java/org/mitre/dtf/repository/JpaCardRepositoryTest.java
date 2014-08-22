package org.mitre.dtf.repository;

import static org.junit.Assert.*;
import static org.mitre.dtf.test.TestData.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.dtf.model.Card;
import org.mitre.dtf.model.Dependency;
import org.mitre.dtf.model.Tag;
import org.mitre.dtf.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * WARNING: This unit test currently uses actual application context for testing.
 * This may have undesirable results on your persistent data store.
 * 
 * Unit testing of JpaCardRepository class. These tests makes some assumptions
 * about the initial state of the db as scripted in the Card.sql resource file.
 * 
 * @author wkim
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/application-context.xml")
public class JpaCardRepositoryTest {

	@Autowired
	CardRepository cardRepository;

	// get testing data from with static import from TestData.java
	
	@Before
	public void setUp() {
		TestData.initialize();
	}
	
	@Test
	public void testInitialState() {

		Set<Card> cards = cardRepository.getAll();
		
		assertTrue(cards.size() == 2);
		assertTrue(cards.contains(CARD1));
		assertTrue(cards.contains(CARD2));
	}
	
	@Test
	public void testRoundTrip() {
		
		Card testCard = new Card();
		testCard.setTitle("oidc.mit.edu");
		testCard.setProvidesTags(new HashSet<Tag>());
		testCard.setDependencies(new ArrayList<Dependency>());
		
		assertFalse(cardRepository.getAll().contains(testCard)); // should not exist in the repository already
		
		cardRepository.save(testCard);
		testCard.setId(3L); // card in db will have been autogenerated an id of 3
		assertTrue(cardRepository.getAll().contains(testCard));
	}
	
	@Test
	public void testDependencies() {
		
		Card c = cardRepository.getById(1);
		List<Dependency> dependencies = c.getDependencies();
		
		assertTrue(dependencies.size() == 2);
		assertTrue(dependencies.contains(DEPENDENCY1));
		assertTrue(dependencies.contains(DEPENDENCY2));
	}
	
	@Test
	public void testProvides() {
		
		Card c = cardRepository.getById(2);
		Set<Tag> tags = c.getProvidesTags();
		
		assertTrue(tags.size() == 1);
		assertTrue(tags.contains(TAG1));
	}
	
}
