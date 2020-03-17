package com.moqifei.bddjupiter.bddjupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.Story;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.provider.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.provider.ScenarioSource;

import net.bytebuddy.asm.Advice.This; 


/**
 * Unit test for simple App.
 */
@Story(name = "Returns go back to the stockpile",
description = "As a store owner, in order to keep track of stock," +
        " I want to add items back to stock when they're returned.")
public class AppTest 
{
	@ScenarioTest("test")
    @ScenarioSource(value= {AnotherScene.class, StoreFrontScene.class})
    public void testScenairo(Scene scene) {
    	 scene.
         given("that a customer previously bought a black sweater from me",
                 () -> scene.put("store", new StoreFront(0, 4).buyBlack(1))).

         and("I have three black sweaters in stock",
                 () -> assertEquals(3, scene.<StoreFront>get("store").getBlacks(),
                         "Store should carry 3 bl  v ack sweaters")).

         when("the customer returns the black sweater for a refund",
                 () -> scene.<StoreFront>get("store").refundBlack(1)).

         then("I should have four black sweaters in stock",
                 () -> assertEquals(4, scene.<StoreFront>get("store").getBlacks(),
                         "Store should carry 4 black sweaters")).
         and("nothing", () -> {
        	 String str1 = "text";
     	     String str2 = "text";
     	     assertThat(str1, is(str2));
         }).
         run();
    	 
		//System.out.println(scene.getDescription());
		//assertEquals("test", scene.getDescription());
    }
	
	@ScenarioTest("test")
	@ValueSource(strings = {"one","two"})
    public void testString(String s) {
    	 System.out.println(s);
		//assertEquals("test", scene.getDescription());
    	 System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
    }
	
	@ScenarioTest("JSontest")
	@ScenarioJsonSource(resources="/dataSet/AppTest.json", instance = StoryDetails.class, key ="StoryDetailsKey")
	public void testJSonScenarioSource(Scene scene) {
		//Scene scene = new Scene();
		scene.given("given", ()->{})
		.when("when", () -> {})
		.then("then", () ->assertEquals("Returns go back to the stockpile", scene.<StoryDetails>get("StoryDetailsKey").getName()))
		.run();
		StoryDetails storyDetails = scene.<StoryDetails>get("StoryDetailsKey");
		System.out.println(scene.<StoryDetails>get("StoryDetailsKey").getName());
	}
	
	@Test
	public void given2Strings_whenIsEqual_thenCorrect() {
	    String str1 = "text";
	    String str2 = "text";
	    assertThat(str1, is(str2));
	    
	 
	}
}
