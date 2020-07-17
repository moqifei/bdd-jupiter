package com.moqifei.bdd.jupiter.report.single.impl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.moqifei.bdd.jupiter.report.single.AbstractTestReport;
import com.moqifei.bdd.jupiter.report.testframe.InfoEntity;
import com.moqifei.bdd.jupiter.report.testframe.TestEntity;

/**
 * Description:控制台 Junit监控测试报告
 */
@Log4j2
public class ConsoleTestWatcherImpl extends AbstractTestReport {

	@AfterAll
	public static void afters() {
		for (InfoEntity entity : getInfoEntitiesList()) {
			log.info(entity);
		}
	}

	@Override
	public void print(TestEntity testEntity, Boolean isApple) {
	}

	@Override
	public void before() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAll(ExtensionContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
