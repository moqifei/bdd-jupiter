package com.moqifei.bdd.jupiter.report.single;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import com.itextpdf.kernel.geom.Line;
import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.extension.StoryExtension;
import com.moqifei.bdd.jupiter.json.Constants;
import com.moqifei.bdd.jupiter.json.Json;
import com.moqifei.bdd.jupiter.modle.StoryDetails;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.report.testframe.Explains;
import com.moqifei.bdd.jupiter.report.testframe.InfoEntity;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Description: TestWatcher抽象测试报告接口 <br>
 */
@Log4j2
public abstract class AbstractTestReport implements TestReport {

	private static final Namespace NAMESPACE = Namespace.create(StoryExtension.class);
	/** 执行数据集 * */
	private static LinkedList<InfoEntity> infoEntitiesList = new LinkedList<>();
	// 开始执行时间
	private static Date startDate;
	// 测试总开始时间
	private static Date testStartDate;
	// 执行对象
	private static TestReport testReport;

	// 测试统计数据信息
	private static Integer disabledSize = 0; // 禁用数量
	private static Integer failedSize = 0; // 失败数量
	private static Integer successfulSize = 0; // 成功数量
	private static Integer abortedSize = 0; // 终止数量

	@AfterAll
	public static void afters() {
		for (InfoEntity entity : infoEntitiesList) {
			log.info(entity);
		}
	}

	@BeforeAll
	public static void befores() {
		// 初始时间
		startDate = new Date();
		testStartDate = new Date();
	}

	public static Integer getAbortedSize() {
		return abortedSize;
	}

	public static Integer getDisabledSize() {
		return disabledSize;
	}

	public static Integer getFailedSize() {
		return failedSize;
	}

	/** 获取执行数据 * */
	public static LinkedList<InfoEntity> getInfoEntitiesList() {
		return infoEntitiesList;
	}

	public static Integer getSuccessfulSize() {
		return successfulSize;
	}

	/** 获取TestReport执行对象 */
	public static TestReport getTestReport() {
		return testReport;
	}

	/** 设置TestReport执行对象 * */
	public static void setTestReport(TestReport testReport) {
		AbstractTestReport.testReport = testReport;
	}

	/** 获取总执行时间 */
	public static Date getTotalTime() {
		Date date = new Date();
		date.setTime(new Date().getTime() - testStartDate.getTime());
		return date;
	}

	/** 添加执行数据信息 * */
	public void add(InfoEntity entity) {
		infoEntitiesList.add(entity);
	}

	@AfterEach
	@Override
	public void after() {
	}

	@BeforeEach
	@Override
	public void before() {
		startDate = new Date();
	}

	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		disabledSize++;

		this.add(new InfoEntity(getClazz(context), getMethod(context), getExplains(context), getStory(context),
				getScenario(context), getScene(context), InfoEntity.ExecuteState.DISABLED, reason.get(), getRunTime(),
				getComplete(context)));
	}

	@Override
	public void testSuccessful(ExtensionContext context) {
		successfulSize++;

		this.add(new InfoEntity(getClazz(context), getMethod(context), getExplains(context), getStory(context),
				getScenario(context), getScene(context), InfoEntity.ExecuteState.SUCCESSFUL, getRemarks(context),
				getRunTime(), getComplete(context)));
	}

	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		abortedSize++;

		this.add(new InfoEntity(getClazz(context), getMethod(context), getExplains(context), getStory(context),
				getScenario(context), getScene(context), InfoEntity.ExecuteState.ABORTED, cause.getMessage(),
				getRunTime(), getComplete(context)));
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		failedSize++;

		this.add(new InfoEntity(getClazz(context), getMethod(context), getExplains(context), getStory(context),
				getScenario(context), getScene(context), InfoEntity.ExecuteState.FAILED, cause.getMessage(),
				getRunTime(), getComplete(context)));
	}

	/** 获取测试类 */
	public String getClazz(ExtensionContext context) {
		return context.getRequiredTestClass().getName();
	}

	/**
	 * 获取:类名.方法:说明注释
	 *
	 * @param context
	 */
	public String getComplete(ExtensionContext context) {
		StringBuffer sb = new StringBuffer();
		Story story = getStory(context);
		sb.append("Story: " + story.name());
		sb.append("\nDescription: " + story.description());
		String str = context.getRequiredTestClass().getName();
		str += "." + context.getTestMethod().get().getName();

		Method method = context.getTestMethod().get();
		Explains explains = method.getAnnotation(Explains.class);
		str += (explains != null ? ":" + explains.value() : "");

		sb.append("\nTestCase: " + str);

		// sb.append("\n"+ getScene(context));
		return sb.toString();
	}

	private String getRemarks(ExtensionContext context) {

		StringBuffer sb = new StringBuffer();
		ScenarioTest scenarioTest = getScenario(context);
		if (scenarioTest != null) {
			sb.append("Scenario: " + scenarioTest.value());
		}

		Scene scene = getScene(context);
		if (scene != null) {
			if (scene.given().length() > 0) {

				sb.append("\n" + "Given: " + scene.given());
				List<String> ands = scene.givenAnds();
				if (!ands.isEmpty()) {
					for (String and : ands) {
						sb.append("\n" + "And: " + and);
					}
				}
			}

			if (scene.when().length() > 0) {
				sb.append("\n" + "When: " + scene.when());
			}

			if (scene.then().length() > 0) {

				sb.append("\n" + "Then: " + scene.then());
				List<String> ands = scene.thenAnds();
				if (!ands.isEmpty()) {
					for (String and : ands) {
						sb.append("\n" + "And: " + and);
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 获取Explains
	 *
	 * @param context
	 */
	public Explains getExplains(ExtensionContext context) {
		return context.getTestMethod().get().getAnnotation(Explains.class);
	}

	/**
	 * 获取ScenarioTest
	 * 
	 * @param context
	 * @return
	 */
	public ScenarioTest getScenario(ExtensionContext context) {
		return context.getTestMethod().get().getAnnotation(ScenarioTest.class);
	}

	private Scene getScene(ExtensionContext context) {
		Class<?> clazz = context.getRequiredTestClass();
		StoryDetails storyDetails = context.getStore(NAMESPACE).get(clazz.getName(), StoryDetails.class);
		String menthodNameString = context.getRequiredTestMethod().getName();
		Scene scene = (Scene) storyDetails.getStore().get(menthodNameString);
		return scene;
	}

	private Story getStory(ExtensionContext context) {
		Class<?> clazz = context.getRequiredTestClass();
		Story story = clazz.getAnnotation(Story.class);
		return story;
	}

	/** 获取测试方法名称 * */
	public String getMethod(ExtensionContext context) {
		return context.getTestMethod().get().getName();
	}

	/** 获取测试方法名称 * */
	public String getMethodExplains(ExtensionContext context) {
		Method method = context.getTestMethod().get();
		Explains explains = method.getAnnotation(Explains.class);
		return explains != null ? explains.value() : null;
	}

	/** 获取执行时间 */
	public String getRunTime() {
		return DateTimeUtils.TIME_COUNT_SSS.format(startDate, new Date());
	}
}
