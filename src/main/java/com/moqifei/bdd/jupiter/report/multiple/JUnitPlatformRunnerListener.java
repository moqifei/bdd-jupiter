package com.moqifei.bdd.jupiter.report.multiple;

import lombok.extern.log4j.Log4j2;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.google.common.eventbus.Subscribe;
import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.report.testframe.Explains;
import com.moqifei.bdd.jupiter.report.testframe.InfoEntity;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.platform.engine.TestExecutionResult.Status.ABORTED;
import static org.junit.platform.engine.TestExecutionResult.Status.FAILED;

/**
 * Junit 运行环境监听
 */
@Log4j2
public class JUnitPlatformRunnerListener implements TestExecutionListener {

	private static LinkedList<InfoEntity> infoEntityList = null;

	private static Map<String, Object> store = null;
	
	private static Set<String> uniqueSet = new HashSet<>();

	private final JUnitPlatformTestTree testTree;
	private final RunNotifier notifier;
	private Date startDate;

	/** 成功总数 */
	private Integer successfulSize = 0;
	/** 终止总数 */
	private Integer abortedSize = 0;
	/** 失败总数 */
	private Integer failedSize = 0;
	/** 禁用总数 */
	private Integer disabledSize = 0;

	public JUnitPlatformRunnerListener(JUnitPlatformTestTree testTree, RunNotifier notifier) {

		this.testTree = testTree;
		this.notifier = notifier;

		this.init();
	}

	public JUnitPlatformRunnerListener() {
		this.notifier = new RunNotifier();
		this.testTree = null;
		store = new HashMap<>();
	}

	/** 动态测试注册 */
	@Override
	public void dynamicTestRegistered(TestIdentifier testIdentifier) {
		log.debug("动态测试注册=>" + testIdentifier);

		String parentId = testIdentifier.getParentId().get();
		testTree.addDynamicDescription(testIdentifier, parentId);
	}

	/** 已跳过执行 */
	@Override
	public void executionSkipped(TestIdentifier testIdentifier, String reason) {
		log.debug("已跳过执行=>" + testIdentifier);

		if (testIdentifier.isTest()) {
			fireTestIgnored(testIdentifier);
		} else {
			testTree.getTestsInSubtree(testIdentifier).forEach(this::fireTestIgnored);
		}
	}

	/** 开始执行 */
	@Override
	public void executionStarted(TestIdentifier testIdentifier) {
		startDate = new Date();

		log.debug("开始执行=>" + testIdentifier);
		Description description = findJUnit4Description(testIdentifier);
		if (description.isTest()) {
			this.notifier.fireTestStarted(description);
		}
	}

	/** 执行完毕 */
	@Override
	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		// log.info("执行完毕=>" + testIdentifier);
		// MethodSource source = (MethodSource) testIdentifier.getSource().get();
		// log.info("<--请求处理类数据---------------------------------------------->");
		// log.info("className:" + source.getClassName());
		// log.info("methodName:" + source.getMethodName());
		// log.info("methodParameterTypes:" + source.getMethodParameterTypes());
		// log.info("<--执行结果数据------------------------------------------------>");
		// log.info("执行结果:" + testExecutionResult.getStatus());
		// log.info(
		// "异常信息:"
		// + (testExecutionResult.getThrowable() != null
		// ? testExecutionResult.getThrowable().get().getMessage()
		// : null));
		// log.info("<--处理完成--------------------------------------------------->");

		Optional<TestSource> source = testIdentifier.getSource();
		if (!source.equals(Optional.empty())) {
			String className = null;
			String methodName = null;

			// 测试数据信息
			TestSource testSource = source.get();

			// 具体方法
			if (testSource instanceof MethodSource) {
				className = ((MethodSource) testSource).getClassName();
				methodName = ((MethodSource) testSource).getMethodName();

				// testSource.

				// 添加数据
				if(!uniqueSet.contains(className+methodName)) {
					infoEntityList.add(setInfoEntity(className, methodName, testExecutionResult.getStatus(),
							testExecutionResult.getThrowable()));
					uniqueSet.add(className+methodName);
				}
				
			}
			// 类
			else if (testSource instanceof ClassSource) {
				className = ((ClassSource) testSource).getClassName();
				methodName = null;
			}
		}

		// @DisplayName 注解
		Description description = findJUnit4Description(testIdentifier);
		TestExecutionResult.Status status = testExecutionResult.getStatus();
		if (status == ABORTED) {
			this.notifier.fireTestAssumptionFailed(toFailure(testExecutionResult, description));
		} else if (status == FAILED) {
			this.notifier.fireTestFailure(toFailure(testExecutionResult, description));
		}
		if (description.isTest()) {
			this.notifier.fireTestFinished(description);
		}
	}

	/** 报告 */
	@Override
	public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
		System.out.println("entry=>" + entry);
	}

	/** 查找JUnit4描述 */
	private Description findJUnit4Description(TestIdentifier testIdentifier) {
		this.testTree.getTestPlan();
		return this.testTree.getDescription(testIdentifier);
	}

	/** 忽略测试 */
	private void fireTestIgnored(TestIdentifier testIdentifier) {
		Optional<TestSource> source = testIdentifier.getSource();
		if (!source.equals(Optional.empty())) {
			disabledSize++;

			TestSource testSource = source.get();
			if (testSource instanceof MethodSource) {
				// 添加忽略数据
				InfoEntity entity = setInfoEntity(((MethodSource) testSource).getClassName(),
						((MethodSource) testSource).getMethodName(), null, null);
				entity.setExecuteState(InfoEntity.ExecuteState.DISABLED);
				infoEntityList.add(entity);
			}
		}

		Description description = findJUnit4Description(testIdentifier);
		this.notifier.fireTestIgnored(description);
	}

	/** 终止数量 */
	public Integer getAbortedSize() {
		return abortedSize;
	}

	/** 禁用数量 */
	public Integer getDisabledSize() {
		return disabledSize;
	}

	/** 失败数量 */
	public Integer getFailedSize() {
		return failedSize;
	}

	/** 获取处理结果数据 */
	public LinkedList<InfoEntity> getInfoEntityList() {
		return infoEntityList;
	}

	/**
	 * 获取类->方法注解
	 *
	 * @param clazz  要获取的测试类
	 * @param method 测试类方法
	 */
	public Explains getMethod(String clazz, String method) {
		if (clazz == null || method == null) {
			return null;
		}

		Explains explains = null;
		try {
			Class<?> classObject = Class.forName(clazz);
			for (Method methodTemp : classObject.getMethods()) {
				if (methodTemp.getName().equals(method)) {
					explains = methodTemp.getAnnotation(Explains.class);
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return explains;
	}

	public ScenarioTest getScenarioTest(String clazz, String method) {
		if (clazz == null || method == null) {
			return null;
		}

		ScenarioTest scenarioTest = null;
		try {
			Class<?> classObject = Class.forName(clazz);
			for (Method methodTemp : classObject.getMethods()) {
				if (methodTemp.getName().equals(method)) {
					scenarioTest = methodTemp.getAnnotation(ScenarioTest.class);
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return scenarioTest;
	}

	public Story getStory(String clazz, String method) {
		if (clazz == null || method == null) {
			return null;
		}

		Story story = null;
		try {
			Class<?> classObject = Class.forName(clazz);
			story = classObject.getAnnotation(Story.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return story;
	}

	public Scene getScene(String clazz, String method) {
		if (clazz == null || method == null) {
			return null;
		}

		Scene scene = null;

		try {
			Class<?> classObject = Class.forName(clazz);
			for (Method methodTemp : classObject.getMethods()) {
				if (methodTemp.getName().equals(method)) {
					// scene = methodTemp.getParameters()[0].get;
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return scene;
	}

	/** 成功数量 */
	public Integer getSuccessfulSize() {
		return successfulSize;
	}

	/** 初始化 */
	private void init() {
		infoEntityList = new LinkedList<>();
		store = new HashMap<>();
	}

	/**
	 * 设置测试数据信息
	 *
	 * @param className  类路径名称
	 * @param methodName 方法名称
	 * @param state      执行状态
	 * @param throwable  执行产生异常简要信息
	 */
	public InfoEntity setInfoEntity(String className, String methodName, TestExecutionResult.Status state,
			Optional<Throwable> throwable) {
		InfoEntity entity = new InfoEntity();

		// 类名
		entity.setClassName(className);
		// 方法
		entity.setMethodName(methodName);
		// 执行结果
		if (state != null) {
			if (state.equals(TestExecutionResult.Status.ABORTED)) {
				abortedSize++;
				entity.setExecuteState(InfoEntity.ExecuteState.ABORTED);
			} else if (state.equals(TestExecutionResult.Status.SUCCESSFUL)) {
				successfulSize++;
				entity.setExecuteState(InfoEntity.ExecuteState.SUCCESSFUL);
				entity.setRemarks(getRemarks(entity.getClassName(), entity.getMethodName()));
			} else if (state.equals(TestExecutionResult.Status.FAILED)) {
				failedSize++;
				entity.setExecuteState(InfoEntity.ExecuteState.FAILED);
			}
		}
		// 获取Explains
		entity.setExplains(this.getMethod(entity.getClassName(), entity.getMethodName()));

		entity.setScenarioTest(this.getScenarioTest(entity.getClassName(), entity.getMethodName()));

		entity.setStory(this.getStory(entity.getClassName(), entity.getMethodName()));

		// 备注
		if (throwable != null && !throwable.equals(Optional.empty())) {
			entity.setRemarks(throwable.get().getMessage());
		}
		// 执行时间
		Date date = new Date();
		date.setTime(new Date().getTime() - startDate.getTime());
		entity.setTime(DateTimeUtils.TIME_COUNT_SSS.formatDate(date));
		// 完整类方法
		entity.setComplete(getComplete(entity));

		return entity;
	}

	private String getRemarks(String clazz, String method) {

		StringBuffer sb = new StringBuffer();
		ScenarioTest scenarioTest = getScenarioTest(clazz, method);
		if (scenarioTest != null) {
			sb.append("Scenario: " + scenarioTest.value());
		}

		String key = clazz + "#" + method;
//		System.out.println(Thread.currentThread()+"get key is " + key);
//		
//		store.forEach((key1, value) -> System.out.println(key1 + " : " + value));

		Scene scene = (Scene) store.get(key.trim());
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

	@Subscribe
	public void listen(Scene scene) {
//		System.out.println(Thread.currentThread()+" Event listener 1 event.message = " + scene.given());
//		System.out.println(Thread.currentThread()+"set key is " + scene.getKey());
		store.put(scene.getKey(), scene);
	}

	public String getComplete(InfoEntity entity) {
		StringBuffer sb = new StringBuffer();
		Story story = entity.getStory();
		if (story != null) {
			sb.append("Story: " + story.name());
			sb.append("\nDescription: " + story.description());
		}
		String str = entity.getClassName();
		str += "." + entity.getMethodName();

		Explains explains = entity.getExplains();

		if (explains != null) {
			str += explains.value();
		}

		sb.append("\nTestCase: " + str);

		return sb.toString();
	}

	/** 失败 */
	private Failure toFailure(TestExecutionResult testExecutionResult, Description description) {
		return new Failure(description, testExecutionResult.getThrowable().orElse(null));
	}

}
