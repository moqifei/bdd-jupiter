package com.moqifei.bdd.jupiter.report.single;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.moqifei.bdd.jupiter.report.testframe.TestEntity;

import java.util.Optional;
/**
 * Description: 测试报告接口 <br>
 */
public interface TestReport extends TestWatcher {

  /** 每个测试方法后执行 */
  void after();

  /** 每个方法执行之前 */
  void before();

  /**
   * 打印输出
   *
   * @param testEntity 数据源
   * @param isApple 是否追加
   */
  default void print(TestEntity testEntity, Boolean isApple) throws Exception {}

  /** 测试已禁用 */
  void testDisabled(ExtensionContext context, Optional<String> reason);

  /** 测试成功 */
  void testSuccessful(ExtensionContext context);

  /** 测试中止 */
  void testAborted(ExtensionContext context, Throwable cause);

  /** 测试失败 */
  void testFailed(ExtensionContext context, Throwable cause);
}
