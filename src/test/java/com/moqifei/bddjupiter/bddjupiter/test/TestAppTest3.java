package com.moqifei.bddjupiter.bddjupiter.test;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.report.testframe.Explains;

@Story(name = "故事", description = "一个测试故事")
public class TestAppTest3 {
	
	 private static Random RM = new Random();
	 
	@Test
	@Explains("我是测试")
	public void lskjdf() {
		Assertions.assertTrue(RM.nextBoolean());
	}
}
