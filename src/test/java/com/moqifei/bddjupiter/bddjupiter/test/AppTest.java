package com.moqifei.bddjupiter.bddjupiter.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.extension.StoryExtension;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.report.testframe.Explains;
import com.moqifei.bdd.jupiter.report.testframe.TestFrameConfig;
import com.moqifei.bdd.jupiter.report.multiple.JUnitPlatformRunnerListener;

import net.bytebuddy.asm.Advice.This;

/**
 * Unit test for simple App.
 */
@Story(name = "Returns go back to the stockpile", description = "As a store owner, in order to keep track of stock,"
		+ " I want to add items back to stock when they're returned. I want to add items back to stock when they're returned.")
public class AppTest {
//	public AppTest() {
//		setUserConfig("classpath:/user-testframe.config.yml");
//	}

	@ScenarioTest("test")
	@ScenarioSource(value = AnotherScene.class)
	public void testScenairo(Scene scene) {
		scene.given("that a customer previously bought a black sweater from me",
				() -> scene.put("store", new StoreFront(0, 4).buyBlack(1))).

				and("I have three black sweaters in stock", () -> assertEquals(3,
						scene.<StoreFront>get("store").getBlacks(), "Store should carry 3 bl  v ack sweaters"))
				.

				when("the customer returns the black sweater for a refund",
						() -> scene.<StoreFront>get("store").refundBlack(1))
				.

				then("I should have four black sweaters in stock", () -> assertEquals(4,
						scene.<StoreFront>get("store").getBlacks(), "Store should carry 4 black sweaters"))
				.run();

		// System.out.println(scene.getDescription());
		// assertEquals("test", scene.getDescription());
	}

	@ScenarioTest("it is a test demoit is a test demoit is a test demo it is a test demoit is a test demoit is a test demoit is a test demo")
	@ScenarioSource(value = { AnotherScene.class })
	// @Explains("it is a test demo")
	public void testScenairo1(Scene scene) {
		scene.given("that a customer previously bought a black sweater from me",
				() -> scene.put("store", new StoreFront(0, 4).buyBlack(1))).

				and("I have three black sweaters in stock", () -> assertEquals(3,
						scene.<StoreFront>get("store").getBlacks(), "Store should carry 3 bl  v ack sweaters"))
				.

				when("the customer returns the black sweater for a refund",
						() -> scene.<StoreFront>get("store").refundBlack(1))
				.

				then("I should have four black sweaters in stock", () -> assertEquals(4,
						scene.<StoreFront>get("store").getBlacks(), "Store should carry 4 black sweaters"))
				.and("nothing", () -> {
					String str1 = "text";
					String str2 = "text";
					assertThat(str1, is(str2));
				}).run();

		// System.out.println(scene.getDescription());
		// assertEquals("test", scene.getDescription());
	}

//	@Disabled
//	@ScenarioTest("test@Disabled")
//	@ValueSource(strings = {"one","two"})
//    public void testString(String s) {
//    	 System.out.println(s);
//		//assertEquals("test", scene.getDescription());
//    	 System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
//    }
//	

	//@Test
	@ScenarioTest("JSontest")
	@ScenarioJsonSource(resources = "/dataSet/AppTest.json", instance = StoryDetails.class, key = "StoryDetailsKey")
	@Disabled
	public void testJSonScenarioSource(Scene scene) {
		// Scene scene = new Scene();
		scene.given("given", () -> {
		}).when("when", () -> {
		}).then("then", () -> assertEquals("Returns go back to the stockpile",
				scene.<StoryDetails>get("StoryDetailsKey").getName())).run();
		StoryDetails storyDetails = scene.<StoryDetails>get("StoryDetailsKey");
		System.out.println(scene.<StoryDetails>get("StoryDetailsKey").getName());
	}

	@Test
	public void given2Strings_whenIsEqual_thenCorrect() {
		String str1 = "text";
		String str2 = "text";
		assertThat(str1, is(str2));

	}

	@Test
	public void fail() {
		throw new RuntimeException("fail");

	}

	@Disabled
	@Test
	public void given2Strings_whenIsEqual_thenCorrect1() {
		String str1 = "text";
		String str2 = "text";
		assertThat(str1, is(str2));

	}

	@Test
	@Disabled
	public void given2Strings_whenIsEqual_thenCorrect2() {
		String str1 = "text";
		String str2 = "text";
		assertThat(str1, is(str2));

	}
}
