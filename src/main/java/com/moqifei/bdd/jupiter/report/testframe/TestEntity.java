package com.moqifei.bdd.jupiter.report.testframe;

import lombok.*;
import org.iherus.codegen.Codectx;
import org.iherus.codegen.qrcode.QreyesFormat;

import java.util.Date;
import java.util.LinkedList;
/**
 * Description:Test数据对象<br>
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

  private Barcode barcode; // 条形码数据
  private Object codeData; // 条码数据
  private CodeType codeType; // 条码类型
  private String companyName; // 公司名称
  private Date createDate; // 创建日期
  private String department; // 部门
  private LinkedList<InfoEntity> infoEntitiesList; // 测试数据信息
  private String logo; // Logo
  private String person; // 测试人员
  private QRcode qrcode; // 二维码数据
  private String rootPath; // 文件生成存放根路径
  private String title; // 标题
  private String password; // 密码
  private String fileNamePrefix; // 文件名前缀
  private Boolean isExecuteStateText; // ExecuteState 文本显示
  private String version; // 版本
  private String environment; // 版本测试环境
  private Boolean isUserCodeData; // 使用用户定义数据

  // 测试统计数据信息
  private Integer disabledSize; // 禁用数量
  private Integer failedSize; // 失败数量
  private Integer successfulSize; // 成功数量
  private Integer abortedSize; // 终止数量

  private Date totalTime; // 执行总时间

  /** 条形码 */
  @Setter
  @Getter
  @ToString
  @AllArgsConstructor
  public class Barcode {}

  /** 二维码 */
  @Setter
  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public class QRcode {
    private Integer margin; // 外边距
    private Integer padding; // 内边距
    private Integer borderSize; // 边框大小
    private Integer borderRadius; // 边框圆角
    private String borderColor; // 边框颜色
    private Codectx.BorderStyle borderStyle; // 边框样式
    private Integer borderDashGranularity; // 虚线间隔
    private String masterColor; // 二维码主色

    // 码眼参数
    private String codeEyesBorderColor; // 码眼边框颜色
    private String codeEyesPointColor; // 码眼颜色
    private QreyesFormat codeEyesFormat; // 码眼样式

    // 条码/二维码宽度 超出范围默认宽度
    private Float width;

    // Logo数据
    private Logo logo;

    // 二维码logo
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public class Logo {
      private String path; // 路径
      private Integer ratio; // logo比例 调整logo 大小
      private Integer arcWidth; // 宽度圆角
      private Integer arcHeight; // 高度圆角
      private Integer borderSize; // 边框大小
      private Integer padding; // 内边距
      private String borderColor; // 边框颜色
      private String backgroundColor; // 背景颜色
      private Integer margin; // 外边距
      private String panelColor; // 容器颜色
      private Integer panelArcWidth; // 容器宽度圆角
      private Integer panelArcHeight; // 容器高度圆角
      private Codectx.LogoShape logoShape; // Logo 样式
    }
  }
}
