package com.moqifei.bdd.jupiter.report.multiple;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.StringUtils;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.suite.api.*;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;

import com.moqifei.bdd.jupiter.report.testframe.TestFrameConfig;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apiguardian.api.API.Status.STABLE;
import static org.junit.platform.engine.discovery.ClassNameFilter.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.PackageNameFilter.excludePackageNames;
import static org.junit.platform.engine.discovery.PackageNameFilter.includePackageNames;
import static org.junit.platform.launcher.EngineFilter.excludeEngines;
import static org.junit.platform.launcher.EngineFilter.includeEngines;
import static org.junit.platform.launcher.TagFilter.excludeTags;
import static org.junit.platform.launcher.TagFilter.includeTags;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

/**
 * 多类测试平台<br>
 */
@API(status = STABLE, since = "1.0")
public class MultipleJunitPlatform extends Runner implements Filterable {

  private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  private static final String[] STANDARD_INCLUDE_PATTERN_ARRAY =
      new String[] {STANDARD_INCLUDE_PATTERN};

  private final Class<?> testClass;
  private final Launcher launcher;
  private JUnitPlatformTestTree testTree;
  private Date startDate;

  public MultipleJunitPlatform(Class<?> testClass) {
    this(testClass, LauncherFactory.create());
  }

  // For testing only
  public MultipleJunitPlatform(Class<?> testClass, Launcher launcher) {
    this.init();

    this.launcher = launcher;
    this.testClass = testClass;
    this.testTree = generateTestTree(createDiscoveryRequest());
  }

  /** 添加排除类名模式筛选器 */
  private void addExcludeClassNamePatternFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] patterns = getExcludeClassNamePatterns();
    if (patterns.length > 0) {
      requestBuilder.filters(excludeClassNamePatterns(patterns));
    }
  }

  /** 添加排除包筛选器 */
  private void addExcludePackagesFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] excludedPackages = getExcludedPackages();
    if (excludedPackages.length > 0) {
      requestBuilder.filters(excludePackageNames(excludedPackages));
    }
  }

  /** 添加排除的引擎筛选器 */
  private void addExcludedEnginesFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] engineIds = getExcludedEngineIds();
    if (engineIds.length > 0) {
      requestBuilder.filters(excludeEngines(engineIds));
    }
  }

  /** 添加排除标题筛选器 */
  private void addExcludedTagsFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] excludedTags = getExcludedTags();
    if (excludedTags.length > 0) {
      requestBuilder.filters(excludeTags(excludedTags));
    }
  }

  /** 从批注添加筛选器 */
  private void addFiltersFromAnnotations(
      LauncherDiscoveryRequestBuilder requestBuilder, boolean isSuite) {
    addIncludeClassNamePatternFilter(requestBuilder, isSuite);
    addExcludeClassNamePatternFilter(requestBuilder);

    addIncludePackagesFilter(requestBuilder);
    addExcludePackagesFilter(requestBuilder);

    addIncludedTagsFilter(requestBuilder);
    addExcludedTagsFilter(requestBuilder);

    addIncludedEnginesFilter(requestBuilder);
    addExcludedEnginesFilter(requestBuilder);
  }

  /** 添加包含类名模式筛选器 */
  private void addIncludeClassNamePatternFilter(
      LauncherDiscoveryRequestBuilder requestBuilder, boolean isSuite) {
    String[] patterns = getIncludeClassNamePatterns(isSuite);
    if (patterns.length > 0) {
      requestBuilder.filters(includeClassNamePatterns(patterns));
    }
  }

  /** 添加包含包筛选器 */
  private void addIncludePackagesFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] includedPackages = getIncludedPackages();
    if (includedPackages.length > 0) {
      requestBuilder.filters(includePackageNames(includedPackages));
    }
  }

  /** 添加包含引擎筛选器 */
  private void addIncludedEnginesFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] engineIds = getIncludedEngineIds();
    if (engineIds.length > 0) {
      requestBuilder.filters(includeEngines(engineIds));
    }
  }

  /** 添加包含标签筛选器 */
  private void addIncludedTagsFilter(LauncherDiscoveryRequestBuilder requestBuilder) {
    String[] includedTags = getIncludedTags();
    if (includedTags.length > 0) {
      requestBuilder.filters(includeTags(includedTags));
    }
  }

  /** 创建发现请求 */
  private LauncherDiscoveryRequest createDiscoveryRequest() {
    List<DiscoverySelector> selectors = getSelectorsFromAnnotations();

    // Allows to simply add @RunWith(JUnitPlatform.class) to any test case
    boolean isSuite = !selectors.isEmpty();
    if (!isSuite) {
      selectors.add(selectClass(this.testClass));
    }
    //去重
    //selectors = selectors.stream().distinct().collect(toList());
    LauncherDiscoveryRequestBuilder requestBuilder = request().selectors(selectors);
    addFiltersFromAnnotations(requestBuilder, isSuite);
    return requestBuilder.build();
  }

  /** 创建唯一发现请求 */
  private LauncherDiscoveryRequest createDiscoveryRequestForUniqueIds(
      Set<TestIdentifier> testIdentifiers) {
    // @formatter:off
    List<DiscoverySelector> selectors =
        testIdentifiers.stream()
            .map(TestIdentifier::getUniqueId)
            .map(DiscoverySelectors::selectUniqueId)
            .collect(toList());
    // @formatter:on
    return request().selectors(selectors).build();
  }

  @Override
  public void filter(Filter filter) throws NoTestsRemainException {
    Set<TestIdentifier> filteredIdentifiers = testTree.getFilteredLeaves(filter);
    if (filteredIdentifiers.isEmpty()) {
      throw new NoTestsRemainException();
    }
    this.testTree = generateTestTree(createDiscoveryRequestForUniqueIds(filteredIdentifiers));
  }

  private JUnitPlatformTestTree generateTestTree(LauncherDiscoveryRequest discoveryRequest) {
    TestPlan testPlan = this.launcher.discover(discoveryRequest);
    return new JUnitPlatformTestTree(testPlan, this.testClass);
  }

  @Override
  public Description getDescription() {
    return this.testTree.getSuiteDescription();
  }

  @Override
  public void run(RunNotifier notifier) {
    // 测试数据信息
    JUnitPlatformRunnerListener runnerListener =
        new JUnitPlatformRunnerListener(this.testTree, notifier);

    this.launcher.execute(this.testTree.getTestPlan(), runnerListener);
    // 生产报表
    this.runReports(runnerListener);
  }

  private String[] getExcludeClassNamePatterns() {
    return trimmed(
        getValueFromAnnotation(
            ExcludeClassNamePatterns.class, ExcludeClassNamePatterns::value, EMPTY_STRING_ARRAY));
  }

  private String[] getExcludedEngineIds() {
    return getValueFromAnnotation(ExcludeEngines.class, ExcludeEngines::value, EMPTY_STRING_ARRAY);
  }

  private String[] getExcludedPackages() {
    return getValueFromAnnotation(
        ExcludePackages.class, ExcludePackages::value, EMPTY_STRING_ARRAY);
  }

  private String[] getExcludedTags() {
    return getValueFromAnnotation(ExcludeTags.class, ExcludeTags::value, EMPTY_STRING_ARRAY);
  }

  private String[] getIncludeClassNamePatterns(boolean isSuite) {
    String[] patterns =
        trimmed(
            getValueFromAnnotation(
                IncludeClassNamePatterns.class,
                IncludeClassNamePatterns::value,
                EMPTY_STRING_ARRAY));
    if (patterns.length == 0 && isSuite) {
      return STANDARD_INCLUDE_PATTERN_ARRAY;
    }
    return patterns;
  }

  private String[] getIncludedEngineIds() {
    return getValueFromAnnotation(IncludeEngines.class, IncludeEngines::value, EMPTY_STRING_ARRAY);
  }

  private String[] getIncludedPackages() {
    return getValueFromAnnotation(
        IncludePackages.class, IncludePackages::value, EMPTY_STRING_ARRAY);
  }

  private String[] getIncludedTags() {
    return getValueFromAnnotation(IncludeTags.class, IncludeTags::value, EMPTY_STRING_ARRAY);
  }

  private Class<?>[] getSelectedClasses() {
    return getValueFromAnnotation(SelectClasses.class, SelectClasses::value, EMPTY_CLASS_ARRAY);
  }

  private String[] getSelectedPackageNames() {
    return getValueFromAnnotation(SelectPackages.class, SelectPackages::value, EMPTY_STRING_ARRAY);
  }

  private List<DiscoverySelector> getSelectorsFromAnnotations() {
    List<DiscoverySelector> selectors = new ArrayList<>();

    selectors.addAll(transform(getSelectedClasses(), DiscoverySelectors::selectClass));
    selectors.addAll(transform(getSelectedPackageNames(), DiscoverySelectors::selectPackage));

    return selectors;
  }

  private <A extends Annotation, V> V getValueFromAnnotation(
      Class<A> annotationClass, Function<A, V> extractor, V defaultValue) {

    A annotation = this.testClass.getAnnotation(annotationClass);
    return (annotation != null ? extractor.apply(annotation) : defaultValue);
  }

  /** 初始化 */
  private void init() {
    startDate = new Date();
  }

  /** 测试完成生产报表数据 */
  public void runReports(JUnitPlatformRunnerListener runnerListener) {
    Date totalTime = new Date();
    totalTime.setTime(new Date().getTime() - startDate.getTime());

    PDFTestMultipleImpl testMultiple =
        new PDFTestMultipleImpl(
            runnerListener.getInfoEntityList(),
            runnerListener.getSuccessfulSize(),
            runnerListener.getAbortedSize(),
            runnerListener.getFailedSize(),
            runnerListener.getDisabledSize(),
            totalTime);

    // 自定义配置
    if (testClass.getAnnotation(TestFrameConfig.class) != null) {
      testMultiple.setUserConfig(testClass.getAnnotation(TestFrameConfig.class).value());
    }

    // 执行
    testMultiple.run();
  }

  private <T> List<DiscoverySelector> transform(
      T[] sourceElements, Function<T, DiscoverySelector> transformer) {
    return stream(sourceElements).map(transformer).collect(toList());
  }

  private String[] trimmed(String[] patterns) {
    if (patterns.length == 0) {
      return patterns;
    }
    return Arrays.stream(patterns)
        .filter(StringUtils::isNotBlank)
        .map(String::trim)
        .toArray(String[]::new);
  }
}
