package com.moqifei.bdd.jupiter.report.testframe;


import lombok.extern.log4j.Log4j2;
import org.iherus.codegen.Codectx;
import org.iherus.codegen.qrcode.QreyesFormat;
import org.yaml.snakeyaml.Yaml;

import com.moqifei.bdd.jupiter.report.util.FileUtil;
import com.moqifei.bdd.jupiter.report.util.StringUtils;

import java.io.IOException;
import java.util.Map;
/**
 * Description:配置文件解析 <br>
 */
@Log4j2
public class ConfigAnalysis {

  /** 默认配置文件 * */
  private String CONFIG_PATH = "/testframe.config.yml";

  /** 绑定 TestEntity-> QRcode -> Logo 数据 */
  private void bindLogo(TestEntity.QRcode.Logo logo, Map<String, Object> data) {
    // path
    if (data.get("path") != null) logo.setPath(data.get("path").toString());
    // ratio
    if (data.get("ratio") != null) logo.setRatio(Integer.valueOf(data.get("ratio").toString()));
    // arcWidth
    if (data.get("arcWidth") != null)
      logo.setArcWidth(Integer.valueOf(data.get("arcWidth").toString()));
    // arcHeight
    if (data.get("arcHeight") != null)
      logo.setArcHeight(Integer.valueOf(data.get("arcHeight").toString()));
    // borderSize
    if (data.get("borderSize") != null)
      logo.setBorderSize(Integer.valueOf(data.get("borderSize").toString()));
    // padding
    if (data.get("padding") != null)
      logo.setPadding(Integer.valueOf(data.get("padding").toString()));
    // borderColor
    if (data.get("borderColor") != null) logo.setBorderColor(data.get("borderColor").toString());
    // backgroundColor
    if (data.get("backgroundColor") != null)
      logo.setBackgroundColor(data.get("backgroundColor").toString());
    // margin
    if (data.get("margin") != null) logo.setMargin(Integer.valueOf(data.get("margin").toString()));
    // panelColor
    if (data.get("panelColor") != null) logo.setPanelColor(data.get("panelColor").toString());
    // panelArcWidth
    if (data.get("panelArcWidth") != null)
      logo.setPanelArcWidth(Integer.valueOf(data.get("panelArcWidth").toString()));
    // panelArcHeight
    if (data.get("panelArcHeight") != null)
      logo.setPanelArcHeight(Integer.valueOf(data.get("panelArcHeight").toString()));
    // logoShape
    if (data.get("logoShape") != null) {
      logo.setLogoShape(
          (Codectx.LogoShape)
              StringUtils.enums(Codectx.LogoShape.values(), data.get("logoShape").toString()));
    }
  }

  /** 绑定 TestEntity-> QRcode 数据 */
  private void bindQRcode(TestEntity.QRcode qrcode, Map<String, Object> data) {
    // margin
    if (data.get("margin") != null)
      qrcode.setMargin(Integer.valueOf(data.get("margin").toString()));
    // padding
    if (data.get("padding") != null)
      qrcode.setPadding(Integer.valueOf(data.get("padding").toString()));
    // borderSize
    if (data.get("borderSize") != null)
      qrcode.setBorderSize(Integer.valueOf(data.get("borderSize").toString()));
    // borderRadius
    if (data.get("borderRadius") != null)
      qrcode.setBorderRadius(Integer.valueOf(data.get("borderRadius").toString()));
    // borderColor
    if (data.get("borderColor") != null) qrcode.setBorderColor(data.get("borderColor").toString());
    // borderStyle
    if (data.get("borderStyle") != null) {
      qrcode.setBorderStyle(
          (Codectx.BorderStyle)
              StringUtils.enums(Codectx.BorderStyle.values(), data.get("borderStyle").toString()));
    }
    // borderDashGranularity
    if (data.get("borderDashGranularity") != null)
      qrcode.setBorderDashGranularity(
          Integer.valueOf(data.get("borderDashGranularity").toString()));
    // masterColor
    if (data.get("masterColor") != null) qrcode.setMasterColor(data.get("masterColor").toString());
    // codeEyesBorderColor
    if (data.get("codeEyesBorderColor") != null)
      qrcode.setCodeEyesBorderColor(data.get("codeEyesBorderColor").toString());
    // codeEyesPointColor
    if (data.get("codeEyesPointColor") != null)
      qrcode.setCodeEyesPointColor(data.get("codeEyesPointColor").toString());
    // codeEyesFormat
    if (data.get("codeEyesFormat") != null) {
      qrcode.setCodeEyesFormat(
          (QreyesFormat)
              StringUtils.enums(QreyesFormat.values(), data.get("codeEyesFormat").toString()));
    }
    // width
    if (data.get("width") != null) qrcode.setWidth(Float.valueOf(data.get("width").toString()));
    // logo
    if (data.get("logo") != null) {
      qrcode.setLogo(qrcode.new Logo());

      // 绑定子节点
      bindLogo(qrcode.getLogo(), (Map<String, Object>) data.get("logo"));
    }
  }

  /** 绑定 TestEntity 数据 */
  private void bindTestEntity(TestEntity entity, Map<String, Object> pdf) {
    // logo
    if (pdf.get("logo") != null) entity.setLogo(FileUtil.file(pdf.get("logo").toString()));
    // companyName
    if (pdf.get("companyName") != null) entity.setCompanyName(pdf.get("companyName").toString());
    // title
    if (pdf.get("title") != null) entity.setTitle(pdf.get("title").toString());
    // department
    if (pdf.get("department") != null) entity.setDepartment(pdf.get("department").toString());
    // person
    if (pdf.get("person") != null) entity.setPerson(pdf.get("person").toString());
    // rootPath
    if (pdf.get("rootPath") != null) entity.setRootPath(pdf.get("rootPath").toString());
    // codeType
    if (pdf.get("codeType") != null)
      entity.setCodeType(
          (CodeType) StringUtils.enums(CodeType.values(), pdf.get("codeType").toString()));
    // codeData
    if (pdf.get("codeData") != null) entity.setCodeData(pdf.get("codeData").toString());
    // password
    if (pdf.get("password") != null) entity.setPassword(pdf.get("password").toString());
    // fileNamePrefix
    if (pdf.get("fileNamePrefix") != null)
      entity.setFileNamePrefix(pdf.get("fileNamePrefix").toString());
    // isExecuteStateText
    if (pdf.get("isExecuteStateText") != null)
      entity.setIsExecuteStateText(Boolean.valueOf(pdf.get("isExecuteStateText").toString()));
    // environment
    if (pdf.get("environment") != null) entity.setEnvironment(pdf.get("environment").toString());
    // version
    if (pdf.get("version") != null) entity.setVersion(pdf.get("version").toString());
    // isUserCodeData
    if (pdf.get("isUserCodeData") != null)
      entity.setIsExecuteStateText(Boolean.valueOf(pdf.get("isUserCodeData").toString()));

    /** ********************************************************* */
    /** 二维码数据 */
    /** ******************************************************** */
    // qrcode
    if (pdf.get("qrcode") != null) {
      entity.setQrcode(entity.new QRcode());

      // 绑定子节点
      bindQRcode(entity.getQrcode(), (Map<String, Object>) pdf.get("qrcode"));
    }
  }

  /**
   * 绑定用户配置
   *
   * @param path 用户配置路径
   */
  public TestEntity build(String path) throws IOException {
    // 加载用户定义 合并数据
    Map<String, Object> defaultConfig = loadConfig();
    Map<String, Object> userConfig = loadConfig(path);

    // 执行数据合并
    StringUtils.merge(defaultConfig, userConfig);
    return initTestEntity(defaultConfig);
  }

  /** 绑定配置 */
  public TestEntity build() {
    return initTestEntity(loadConfig());
  }

  /** 返回初始化 TestEntity 数据 */
  private TestEntity initTestEntity(Map<String, Object> data) {
    TestEntity entity = new TestEntity();

    if (data != null && data.get("testframe") != null) {
      // pdf
      Object pdfObject = ((Map<String, Object>) data.get("testframe")).get("pdf");
      if (pdfObject != null) {
        bindTestEntity(entity, (Map<String, Object>) pdfObject);
      }
    }

    return entity;
  }

  /** 加载用户设置配置 */
  private Map<String, Object> loadConfig(String path) throws IOException {
    Yaml yaml = new Yaml();
    Map<String, Object> data = yaml.load(FileUtil.fileInputStream(path));
    return data;
  }

  /** 加载默认配置 */
  private Map<String, Object> loadConfig() {
    Yaml yaml = new Yaml();
    Map<String, Object> data = yaml.load(this.getClass().getResourceAsStream(CONFIG_PATH));
    return data;
  }
}
