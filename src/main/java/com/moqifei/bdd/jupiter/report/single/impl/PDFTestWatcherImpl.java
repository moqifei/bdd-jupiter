package com.moqifei.bdd.jupiter.report.single.impl;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;

import com.moqifei.bdd.jupiter.report.pdf.DefaultPdfTestTheme;
import com.moqifei.bdd.jupiter.report.pdf.PdfFactory;
import com.moqifei.bdd.jupiter.report.single.AbstractTestReport;
import com.moqifei.bdd.jupiter.report.testframe.ConfigAnalysis;
import com.moqifei.bdd.jupiter.report.testframe.TestEntity;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;
import com.moqifei.bdd.jupiter.report.util.FileUtil;

import java.io.File;
import java.util.Date;
/**
 * Description:PDF Junit监控测试 <br>
 */
@Log4j2
public class PDFTestWatcherImpl extends AbstractTestReport {

  /** * 自定义配置文件 */
  private static String CONFIG_PATH = null;

  private static String DEFAULT_FILE_NAME_PREFIX = "/test-report-";

  public PDFTestWatcherImpl() {
    setTestReport(this);
  }

  @AfterAll
  public static void afters() {
    TestEntity testEntity = null;

    try {
      // 加载配置
      if (CONFIG_PATH != null) {
        testEntity = new ConfigAnalysis().build(CONFIG_PATH);
      } else {
        testEntity = new ConfigAnalysis().build();
      }

      // 测试数据
      testEntity.setInfoEntitiesList(getInfoEntitiesList());
      testEntity.setCreateDate(new Date());
      testEntity.setAbortedSize(getAbortedSize());
      testEntity.setDisabledSize(getDisabledSize());
      testEntity.setFailedSize(getFailedSize());
      testEntity.setSuccessfulSize(getSuccessfulSize());
      testEntity.setTotalTime(getTotalTime());

      // 执行打印
      getTestReport().print(testEntity, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void print(TestEntity testEntity, Boolean isApple) throws Exception {
    // 文件夹
    // String path = System.getProperty("user.dir") + "/file";
    String path = null;
    if (testEntity.getRootPath() == null || testEntity.getRootPath().trim().isEmpty()) {
      path = "./file";
    } else {
      path = testEntity.getRootPath() + "/file";
    }
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }

    // 清理文件
    FileUtil.cleanFile(file, 5);

    // 文件
    path =
        path
            + ((testEntity.getFileNamePrefix() != null && !testEntity.getFileNamePrefix().isEmpty())
                ? "/" + testEntity.getFileNamePrefix()
                : DEFAULT_FILE_NAME_PREFIX)
            + DateTimeUtils.DATE_TIME_MILLI_COMPACT_FILESTYLE.formatNow()
            + ".pdf";
    file = new File(path);
    if (!file.exists()) {
      file.createNewFile();
    }

    // 生成pdf 文档加密
    if (testEntity.getPassword() != null && !testEntity.getPassword().isEmpty()) {
      PdfFactory.PDF().execute(file, new DefaultPdfTestTheme(testEntity), testEntity.getPassword());
    }
    // 文档非加密
    else {
      PdfFactory.PDF().execute(file, new DefaultPdfTestTheme(testEntity));
    }

    // 打开pdf文件
    FileUtil.openFile(file);
    file = null;
  }

  /**
   * 用户自定义 testframe 配置文件
   *
   * @param path 文件路径
   */
  public void setUserConfig(String path) {
    this.CONFIG_PATH = path;
  }
}
