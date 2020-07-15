package com.moqifei.bdd.jupiter.report.multiple;


import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;

import com.moqifei.bdd.jupiter.report.pdf.DefaultPdfTestTheme;
import com.moqifei.bdd.jupiter.report.pdf.PdfFactory;
import com.moqifei.bdd.jupiter.report.testframe.ConfigAnalysis;
import com.moqifei.bdd.jupiter.report.testframe.InfoEntity;
import com.moqifei.bdd.jupiter.report.testframe.TestEntity;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;
import com.moqifei.bdd.jupiter.report.util.FileUtil;

/**
 * Description:PDF Junit 多类监控测试 <br>
 */
@Log4j2
public class PDFTestMultipleImpl {

  /** * 自定义配置文件 */
  private static String CONFIG_PATH = null;
  /** 生成文件默认前缀 * */
  private static String DEFAULT_FILE_NAME_PREFIX = "/test-report-";

  /** 执行结果数据 * */
  private LinkedList<InfoEntity> infoEntityList;
  /** 成功总数 */
  private Integer successfulSize = 0;
  /** 终止总数 */
  private Integer abortedSize = 0;
  /** 失败总数 */
  private Integer failedSize = 0;
  /** 禁用总数 */
  private Integer disabledSize = 0;
  /** 总时间 */
  private Date totalTime;

  /**
   * PDF Junit 多类监控测试
   *
   * @param infoEntityList 测试数据信息
   * @param successfulSize 成功数量
   * @param abortedSize 终止数量
   * @param failedSize 失败数量
   * @param disabledSize 禁用数量
   * @param totalTime 执行总时间
   */
  public PDFTestMultipleImpl(
      LinkedList<InfoEntity> infoEntityList,
      Integer successfulSize,
      Integer abortedSize,
      Integer failedSize,
      Integer disabledSize,
      Date totalTime) {
    this.infoEntityList = infoEntityList;
    this.successfulSize = successfulSize;
    this.abortedSize = abortedSize;
    this.failedSize = failedSize;
    this.disabledSize = disabledSize;
    this.totalTime = totalTime;
  }

  /**
   * 打印数据
   *
   * @param testEntity 测试数据
   * @param isApple 是否追加 无效
   */
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

  /** 执行生产报表 */
  public void run() {
    TestEntity testEntity = null;

    try {
      // 加载配置
      if (CONFIG_PATH != null) {
        testEntity = new ConfigAnalysis().build(CONFIG_PATH);
      } else {
        testEntity = new ConfigAnalysis().build();
      }

      // 测试数据
      testEntity.setInfoEntitiesList(infoEntityList);
      testEntity.setCreateDate(new Date());
      testEntity.setAbortedSize(abortedSize);
      testEntity.setDisabledSize(disabledSize);
      testEntity.setFailedSize(failedSize);
      testEntity.setSuccessfulSize(successfulSize);
      testEntity.setTotalTime(totalTime);

      // 执行打印
      this.print(testEntity, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
