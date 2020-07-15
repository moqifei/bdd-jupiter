package com.moqifei.bddjupiter.bddjupiter.test.subpackage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bddjupiter.bddjupiter.test.AnotherScene;

@Story(name = "乌鸦喝水", description = "聪明的乌鸦利用自己的智慧喝到了水")
public class NewDemoTest {

	@ScenarioTest(value = "放石子喝水场景")
	@ScenarioJsonSource(resources = "/dataSet/AppTest.json", instance = StoryDetails.class, key = "StoryDetailsKey")
	public void drinkTest(Scene scene) {
		scene.given("乌鸦口渴了", () -> {}
		).and("乌鸦找到半瓶水", ()->{}
		).when("乌鸦往瓶子里面丢石头",()->{}
		).then("瓶内水面上升",()->{}
		).and("乌鸦喝到水了", ()->{
			assertThat(true, is(true));
		});
	}
}
