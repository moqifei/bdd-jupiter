package com.moqifei.bddjupiter.bddjupiter.main;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

import com.moqifei.bdd.jupiter.report.multiple.MultipleJunitPlatform;
import com.moqifei.bdd.jupiter.report.testframe.TestFrameConfig;
import com.moqifei.bddjupiter.bddjupiter.test.subpackage.NewDemoTest;

/**
 * @program: EnterpriseSystem<br>
 * @author: LiangChao<br>
 * @description: 多类测试平台<br>
 * @create: 2020-01-09 15:11
 */
@RunWith(MultipleJunitPlatform.class)
//@SelectClasses({
//	AppTest.class,
//	AppTestTest.class,
//	TestClassFive.class,
//	TestAppTest3.class,
//	NewDemoTest.class
//})
@SelectPackages("com.moqifei.bddjupiter.bddjupiter.test")
@TestFrameConfig(value = "classpath:/testframe.config.two.yml")
public class TestMultipleJunitPlatform {}
