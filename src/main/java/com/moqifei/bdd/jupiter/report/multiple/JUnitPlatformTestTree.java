package com.moqifei.bdd.jupiter.report.multiple;

import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.StringUtils;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.platform.suite.api.UseTechnicalNames;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

/**
 * JUnitPlatformTestTree 来源于junit ->org.junit.platform.runner.JUnitPlatformTestTree
 */
public class JUnitPlatformTestTree {

  private final Map<TestIdentifier, Description> descriptions = new HashMap<>();
  private final TestPlan testPlan;
  private final Function<TestIdentifier, String> nameExtractor;
  private final Description suiteDescription;

  JUnitPlatformTestTree(TestPlan testPlan, Class<?> testClass) {
    this.testPlan = testPlan;
    this.nameExtractor =
        useTechnicalNames(testClass) ? this::getTechnicalName : TestIdentifier::getDisplayName;
    this.suiteDescription = generateSuiteDescription(testPlan, testClass);
  }

  private static boolean useTechnicalNames(Class<?> testClass) {
    return testClass.isAnnotationPresent(UseTechnicalNames.class);
  }

  void addDynamicDescription(TestIdentifier newIdentifier, String parentId) {
    Description parent = getDescription(this.testPlan.getTestIdentifier(parentId));
    buildDescription(newIdentifier, parent, this.testPlan);
  }

  private Set<TestIdentifier> applyFilterToDescriptions(Filter filter) {
    // @formatter:off
    return descriptions.entrySet().stream()
        .filter(entry -> filter.shouldRun(entry.getValue()))
        .map(Map.Entry::getKey)
        .collect(toSet());
    // @formatter:on
  }

  private void buildDescription(TestIdentifier identifier, Description parent, TestPlan testPlan) {
    Description newDescription = createJUnit4Description(identifier, testPlan);
    parent.addChild(newDescription);
    this.descriptions.put(identifier, newDescription);
    testPlan
        .getChildren(identifier)
        .forEach(testIdentifier -> buildDescription(testIdentifier, newDescription, testPlan));
  }

  private void buildDescriptionTree(Description suiteDescription, TestPlan testPlan) {
    testPlan
        .getRoots()
        .forEach(testIdentifier -> buildDescription(testIdentifier, suiteDescription, testPlan));
  }

  private Description createJUnit4Description(TestIdentifier identifier, TestPlan testPlan) {
    String name = nameExtractor.apply(identifier);
    if (identifier.isTest()) {
      String containerName = testPlan.getParent(identifier).map(nameExtractor).orElse("<unrooted>");
      return Description.createTestDescription(containerName, name, identifier.getUniqueId());
    }
    return Description.createSuiteDescription(name, identifier.getUniqueId());
  }

  private Description generateSuiteDescription(TestPlan testPlan, Class<?> testClass) {
    String displayName =
        useTechnicalNames(testClass) ? testClass.getName() : getSuiteDisplayName(testClass);
    Description suiteDescription = Description.createSuiteDescription(displayName);
    buildDescriptionTree(suiteDescription, testPlan);
    return suiteDescription;
  }

  Description getDescription(TestIdentifier identifier) {
    return this.descriptions.get(identifier);
  }

  Set<TestIdentifier> getFilteredLeaves(Filter filter) {
    Set<TestIdentifier> identifiers = applyFilterToDescriptions(filter);
    return removeNonLeafIdentifiers(identifiers);
  }

  Description getSuiteDescription() {
    return this.suiteDescription;
  }

  private String getSuiteDisplayName(Class<?> testClass) {
    // @formatter:off
    return AnnotationUtils.findAnnotation(testClass, SuiteDisplayName.class)
        .map(SuiteDisplayName::value)
        .filter(StringUtils::isNotBlank)
        .orElse(testClass.getName());
    // @formatter:on
  }

  private String getTechnicalName(TestIdentifier testIdentifier) {
    Optional<TestSource> optionalSource = testIdentifier.getSource();
    if (optionalSource.isPresent()) {
      TestSource source = optionalSource.get();
      if (source instanceof ClassSource) {
        return ((ClassSource) source).getJavaClass().getName();
      } else if (source instanceof MethodSource) {
        MethodSource methodSource = (MethodSource) source;
        String methodParameterTypes = methodSource.getMethodParameterTypes();
        if (StringUtils.isBlank(methodParameterTypes)) {
          return methodSource.getMethodName();
        }
        return String.format("%s(%s)", methodSource.getMethodName(), methodParameterTypes);
      }
    }

    // Else fall back to display name
    return testIdentifier.getDisplayName();
  }

  public TestPlan getTestPlan() {
    return testPlan;
  }

  Set<TestIdentifier> getTestsInSubtree(TestIdentifier ancestor) {
    // @formatter:off
    return testPlan.getDescendants(ancestor).stream()
        .filter(TestIdentifier::isTest)
        .collect(toCollection(LinkedHashSet::new));
    // @formatter:on
  }

  private Predicate<? super TestIdentifier> isALeaf(Set<TestIdentifier> identifiers) {
    return testIdentifier -> {
      Set<TestIdentifier> descendants = testPlan.getDescendants(testIdentifier);
      return identifiers.stream().noneMatch(descendants::contains);
    };
  }

  private Set<TestIdentifier> removeNonLeafIdentifiers(Set<TestIdentifier> identifiers) {
    return identifiers.stream().filter(isALeaf(identifiers)).collect(toSet());
  }
}
