package com.moqifei.bddjupiter.bddjupiter.test.subpackage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.report.single.impl.PDFTestWatcherImpl;
import com.moqifei.bdd.jupiter.report.testframe.TestFrameConfig;

@Story(name = "乌鸦喝水", description = "聪明的乌鸦利用自己的智慧喝到了水")
@ExtendWith(PDFTestWatcherImpl.class)
@TestFrameConfig(value = "classpath:/crows-drink.testframe.config.yml")
public class NewDemoTest {

	@ScenarioTest(value = "放石子喝水场景")
	@ScenarioJsonSource(resources = "/dataSet/AppTest.json", instance = StoryDetails.class, key = "StoryDetailsKey")
	public void drinkTest(Scene scene) {
		scene.given("乌鸦口渴了", () -> {
		}).and("乌鸦找到半瓶水", () -> {
		}).when("乌鸦往瓶子里面丢石头", () -> {
		}).then("瓶内水面上升", () -> {
		}).and("乌鸦喝到水了", () -> {
			assertThat(true, is(true));
		});
	}
	
	@ScenarioTest(value = "伸脖子喝水场景")
	@ScenarioJsonSource(resources = "/dataSet/AppTest.json", instance = StoryDetails.class, key = "StoryDetailsKey")
	public void thirstyTest(Scene scene) {
		scene.given("乌鸦口渴了", () -> {
		}).and("乌鸦找到半瓶水", () -> {
		}).when("乌鸦尝试往瓶子里伸脖子", () -> {
		}).then("脖子太短，够不睡眠", () -> {
		}).and("乌鸦继续口渴", () -> {
			assertThat(false, is(false));
		});
	}
	
	@Test
	@Disabled
	public void disableTest() {
		
	}
	
	@Test
	public void failTest() {
		throw new RuntimeException("乌鸦喝水失败了！");
	} 
}
