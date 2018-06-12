package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext
@AutoConfigureStubRunner(workOffline = true, ids="com.example:beer-api-producer")
public class BeerVerificationListenerTest extends AbstractTest {

	@Autowired BeerVerificationListener listener;
	@Autowired StubTrigger stubTrigger;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = listener.eligibleCounter.get();

		stubTrigger.trigger("accepted_verification");


		then(listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = listener.notEligibleCounter.get();

    stubTrigger.trigger("rejected_verification");

		then(listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}
