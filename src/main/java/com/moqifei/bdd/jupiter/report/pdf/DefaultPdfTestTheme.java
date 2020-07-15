package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.Barcode1D;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.LineRenderer;
import com.moqifei.bdd.jupiter.report.testframe.CodeType;
import com.moqifei.bdd.jupiter.report.testframe.InfoEntity;
import com.moqifei.bdd.jupiter.report.testframe.TestEntity;
import com.moqifei.bdd.jupiter.report.util.ChineseCharacterHelper;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;
import com.moqifei.bdd.jupiter.report.util.FileUtil;

import javafx.util.Callback;
import org.iherus.codegen.qrcode.LogoConfig;
import org.iherus.codegen.qrcode.QrcodeConfig;
import org.iherus.codegen.qrcode.SimpleQrcodeGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Description:默认格式 <br>
 * 
 */
public class DefaultPdfTestTheme implements PdfTestTheme {

  private static final String ABORTED = "/images/sysicon/ABORTED.png";
  private static final String DISABLED = "/images/sysicon/DISABLED.png";
  private static final String FAILED = "/images/sysicon/FAILED.png";
  private static final String SUCCESSFUL = "/images/sysicon/SUCCESSFUL.png";

  /** 条码容器最大宽度 */
  private static final Float MAX_BARCODE_WIDTH = 120F;
  /** 数据源 */
  public TestEntity entity;
  /** 粗体 */
  private PdfFont boldFont;
  /** pdf默认字体 */
  private PdfFont font;
  /** 标题用户自定义区域 */
  private Callback<PdfEntityParam, Void> startCallback;
  /** 结尾用户自定义区域 */
  private Callback<PdfEntityParam, Void> endCallback;

  /** 构建PDF数据 */
  public DefaultPdfTestTheme(TestEntity entity) throws IOException {
    this(entity, null, null);
  }

  /** 构建PDF数据 */
  public DefaultPdfTestTheme(TestEntity entity, Callback<PdfEntityParam, Void> endCallback)
      throws IOException {
    this(entity, null, endCallback);
  }

  /**
   * 构建PDF数据
   *
   * @param entity 数据源
   * @param startCallback 头部用户定义回调处理
   * @param endCallback 尾部用户定义回调处理
   */
  public DefaultPdfTestTheme(
      TestEntity entity,
      Callback<PdfEntityParam, Void> startCallback,
      Callback<PdfEntityParam, Void> endCallback)
      throws IOException {
    this.entity = entity;
    this.startCallback = startCallback;
    this.endCallback = endCallback;
    this.init();
  }

  /** 构建PDF数据 */
  public DefaultPdfTestTheme(Callback<PdfEntityParam, Void> startCallback, TestEntity entity)
      throws IOException {
    this(entity, startCallback, null);
  }

  /**
   * 创建直线
   *
   * @param size 直线粗细
   * @return
   */
  public static LineSeparator createLines(Float size) {
    return createLines(false, size, null, null, null);
  }

  /**
   * 创建线
   *
   * @param isDottedLine true:虚线; false:实线
   * @param size 线条大小
   * @param dash 分段长度
   * @param phase 分段
   * @param gap 间隔
   * @return
   */
  public static LineSeparator createLines(
      Boolean isDottedLine, Float size, Float dash, Float phase, Float gap) {
    Lines line = new Lines();

    // 虚线
    if (isDottedLine != null && isDottedLine) {
      line.setDash(dash);
      line.setPhase(phase);
      line.setGap(gap);
    }

    // 线条宽度
    line.setLineWidth(size);
    LineSeparator lineSeparator = new LineSeparator(line);
    lineSeparator.setMarginBottom(line.getLineWidth() < 1 ? 1 : (line.getLineWidth() + 0.5f));

    line = null;
    return lineSeparator;
  }

  /** 绑定 TestEntity.QRcode.Logo 数据 */
  private void bindLogo(TestEntity.QRcode.Logo logo, LogoConfig logoConfig) {
    if (logo.getRatio() != null) logoConfig.setRatio(logo.getRatio());
    if (logo.getArcWidth() != null) logoConfig.setArcWidth(logo.getArcWidth());
    if (logo.getArcHeight() != null) logoConfig.setArcHeight(logo.getArcHeight());
    if (logo.getBorderSize() != null) logoConfig.setBorderSize(logo.getBorderSize());
    if (logo.getPadding() != null) logoConfig.setPadding(logo.getPadding());
    if (logo.getBorderColor() != null) logoConfig.setBorderColor(logo.getBorderColor());
    if (logo.getBackgroundColor() != null) logoConfig.setBackgroundColor(logo.getBackgroundColor());
    if (logo.getMargin() != null) logoConfig.setMargin(logo.getMargin());
    if (logo.getPanelColor() != null) logoConfig.setPanelColor(logo.getPanelColor());
    if (logo.getPanelArcWidth() != null) logoConfig.setPanelArcWidth(logo.getPanelArcWidth());
    if (logo.getPanelArcHeight() != null) logoConfig.setPanelArcHeight(logo.getPanelArcHeight());
  }

  /** 绑定 TestEntity.QRcode 数据 */
  private void bindQrcode(TestEntity.QRcode qRcode, QrcodeConfig qrcodeConfig) {
    if (qRcode.getMargin() != null) qrcodeConfig.setMargin(qRcode.getMargin());
    if (qRcode.getPadding() != null) qrcodeConfig.setPadding(qRcode.getPadding());
    if (qRcode.getBorderSize() != null) qrcodeConfig.setBorderSize(qRcode.getBorderSize());
    if (qRcode.getBorderRadius() != null) qrcodeConfig.setBorderRadius(qRcode.getBorderRadius());
    if (qRcode.getBorderColor() != null) qrcodeConfig.setBorderColor(qRcode.getBorderColor());
    if (qRcode.getBorderStyle() != null) qrcodeConfig.setBorderStyle(qRcode.getBorderStyle());
    if (qRcode.getBorderDashGranularity() != null)
      qrcodeConfig.setBorderDashGranularity(qRcode.getBorderDashGranularity());
    if (qRcode.getMasterColor() != null) qrcodeConfig.setMasterColor(qRcode.getMasterColor());

    // 码眼
    if (qRcode.getCodeEyesBorderColor() != null)
      qrcodeConfig.setCodeEyesBorderColor(qRcode.getCodeEyesBorderColor());
    if (qRcode.getCodeEyesPointColor() != null)
      qrcodeConfig.setCodeEyesPointColor(qRcode.getCodeEyesPointColor());
    if (qRcode.getCodeEyesFormat() != null)
      qrcodeConfig.setCodeEyesFormat(qRcode.getCodeEyesFormat());
  }

  @Override
  public void build(PdfDocument pdf) throws Exception {
    // TODO
    // TODO
    // TODO
    Document document = new Document(pdf, PageSize.A4);
    float pageWidth = PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin();
    // 支持中文字体
    document.setFont(font);

    /** ************************************************************** */
    /** TODO 绘制数据区域 */
    /** ************************************************************** */
    document.add(new Paragraph(""));
    document.add(new Paragraph(""));

    // 创建头部标题
    createHead(pdf, document, pageWidth);
    // 分割线
    document.add(createLines(3f));
    // 测试头部信息
    createTestHead1(document, pageWidth);
    document.add(createLines(0.5f));
    createTestHead2(document, pageWidth);
    document.add(createLines(0.5f));
    createTestHead3(document, pageWidth);
    document.add(createLines(0.5f));
    createTestHead4(document, pageWidth);

    // 用户定义数据区域
    if (startCallback != null) {
      document.add(createLines(0.5f));
      startCallback.call(new PdfEntityParam(document, pageWidth));
    }

    // 创建数据table
    createDataTable(document, pageWidth);

    // 用户定义数据区域
    if (endCallback != null) {
      endCallback.call(new PdfEntityParam(document, pageWidth));
      document.add(createLines(0.5f));
    }

    // 结论
    createConclusion(document, pageWidth);
    document.add(createLines(3f));

    // 创建结尾
    createFoot(document, pageWidth);

    /** ************************************************************** */
    /** 绘制数据区域结束 清理数据 */
    /** ************************************************************** */
    document.close();
    document = null;
  }

  /**
   * 设置table 行数据
   *
   * @param text 标题
   * @param context 详情说明
   * @param isLine 是否只显示上下边线
   */
  private Cell cell(String text, String context, Boolean isLine) {
    Cell cell = new Cell();
    cell.setTextAlignment(TextAlignment.LEFT);
    enjambment(cell);

    // 上下边线
    if (isLine) {
      cell.setBorder(Border.NO_BORDER);
      cell.setBorderBottom(new SolidBorder(0.5f));
      // cell.setBorderTop (new SolidBorder (0.5f));
    }

    // 标题
    Paragraph paragraph1 = new Paragraph(text);
    cell.add(paragraph1);

    // 详细说明
    if (context != null) {
      Paragraph paragraph2 = new Paragraph(context);
      paragraph2.setFontSize(7);
      paragraph2.setPaddingLeft(2 * 7f);
      cell.add(paragraph2);

      paragraph2 = null;
    }

    paragraph1 = null;
    return cell;
  }

  /**
   * 设置table 行数据
   *
   * @param text 文本/图片
   * @param isLine 是否只显示上下边线
   */
  private Cell cell(Object text, Boolean isLine) {
    return cell(text, null, isLine);
  }

  /**
   * 设置table 行数据
   *
   * @param text 文本/图片
   * @param alignment 对齐方式
   * @param isLine 是否只显示上下边线
   */
  private Cell cell(Object text, TextAlignment alignment, Boolean isLine) {
    Cell cell = new Cell();
    enjambment(cell);

    // 上下边线
    if (isLine != null && isLine) {
      cell.setBorder(Border.NO_BORDER);
      cell.setBorderBottom(new SolidBorder(0.5f));
      // cell.setBorderTop (new SolidBorder (0.5f));
    }

    // 设置数据
    if (text instanceof String) {
      cell.add(new Paragraph(text != null ? text.toString() : null));
    } else if (text instanceof Image) {
      cell.add((Image) text);
    }

    // 设置对齐方式
    if (alignment != null) cell.setTextAlignment(alignment);

    return cell;
  }

  /** 获取图片数据 */
  public Image convertImage(InfoEntity.ExecuteState executeState) throws MalformedURLException {
    Image image = null;

    if (InfoEntity.ExecuteState.ABORTED.equals(executeState)) {
      image = new Image(ImageDataFactory.create(this.getClass().getResource(ABORTED)));
    } else if (InfoEntity.ExecuteState.DISABLED.equals(executeState)) {
      image = new Image(ImageDataFactory.create(this.getClass().getResource(DISABLED)));
    } else if (InfoEntity.ExecuteState.FAILED.equals(executeState)) {
      image = new Image(ImageDataFactory.create(this.getClass().getResource(FAILED)));
    } else if (InfoEntity.ExecuteState.SUCCESSFUL.equals(executeState)) {
      image = new Image(ImageDataFactory.create(this.getClass().getResource(SUCCESSFUL)));
    }

    image.setMaxHeight(10f);
    image.setMaxHeight(10f);
    image.setMarginTop(3f);
    return image;
  }

  /** 获取文本数据 */
  private String convertText(InfoEntity.ExecuteState executeState) {
    String text = null;
    if (InfoEntity.ExecuteState.ABORTED.equals(executeState)) text = "终止";
    else if (InfoEntity.ExecuteState.DISABLED.equals(executeState)) text = "禁用";
    else if (InfoEntity.ExecuteState.FAILED.equals(executeState)) text = "失败";
    else if (InfoEntity.ExecuteState.SUCCESSFUL.equals(executeState)) text = "成功";
    return text;
  }

  /**
   * 创建条形码
   *
   * @param width 图片宽度
   */
  public Paragraph createBarcode(
      PdfDocument document, Float width, Float imageHeight, Float logoHeight) throws IOException {
    Paragraph paragraph = new Paragraph();
    paragraph.setWidth(width);
    // 修正量
    int s = 2;

    if (entity.getCodeType() != null && entity.getCodeData() != null) {
      if (entity.getCodeType().equals(CodeType.BARCODE)) {
        Image barcode = drawBarcode(document, width - s, imageHeight - s);
        paragraph.add(barcode);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        //        barcode.setBackgroundColor(DeviceGray.GRAY);
        //        paragraph.setBackgroundColor(DeviceRgb.RED);

        // Paragraphs.VerticalAlignment(paragraph, VerticalAlignment.BOTTOM);
        if (logoHeight > barcode.getImageHeight() + 11) {
          paragraph.setMarginTop(logoHeight - barcode.getImageHeight() + 11);
        }
      } else if (entity.getCodeType().equals(CodeType.QRCODE)) {
        paragraph.add(drawQRcode(imageHeight - s));
      }
    }

    paragraph.setTextAlignment(TextAlignment.RIGHT);
    return paragraph;
  }

  /** 创建条形码数据 */
  public String createCodeData() {
    // 使用用户定义数据
    if (entity.getIsExecuteStateText() != null && entity.getIsExecuteStateText()) {
      return entity.getCodeData().toString();
    }

    // 条形码数据 环境第一个字母
    String codeData =
        ChineseCharacterHelper.getSpells(String.valueOf(entity.getEnvironment().charAt(0)))
            .toUpperCase();
    // 部门第一个字母
    codeData +=
        ChineseCharacterHelper.getSpells(String.valueOf(entity.getDepartment().charAt(0)))
            .toUpperCase();
    // 人员第一个字母
    codeData +=
        ChineseCharacterHelper.getSpells(String.valueOf(entity.getPerson().charAt(0)))
            .toUpperCase();
    // 日期时间戳
    codeData += DateTimeUtils.DATE_TIME_MILLI_COMPACT.formatNow();
    return codeData;
  }

  /** 测试结论 */
  public void createConclusion(Document document, float pageWidth) {
    Div div = new Div();
    div.setFontSize(8);
    div.add(new Paragraphs("测试结论", pageWidth, TextAlignment.LEFT).paragraph());
    div.add(new Paragraphs(null, pageWidth, 30f, TextAlignment.LEFT).paragraph());

    document.add(div);
  }

  /** 创建数据区域table */
  public void createDataTable(Document document, float pageWidth) throws MalformedURLException {
    /** ************************************************************** */
    /** Table定义 */
    /** ************************************************************** */
    Table table = new Table(new float[] {50, pageWidth - 80, 30});
    table.setWidth(pageWidth);
    table.setFontSize(8);
    // table.setBorder (Border.NO_BORDER);
    table.setBorderTop(new DoubleBorder(Border.DOUBLE));
    table.setBorderBottom(new DoubleBorder(Border.DOUBLE));
    // 折叠设置
    table.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

    /** ************************************************************** */
    /** Table Head定义 */
    /** ************************************************************** */
    String[] tableTitle = {"Time", "内容", "结果"};
    for (String str : tableTitle) {
      table.addHeaderCell(str).setFontSize(10);
    }
    table.getHeader().setBold();

    /** ************************************************************** */
    /** Table Body */
    /** ************************************************************** */
    InfoEntity infoEntity;

    if (entity.getInfoEntitiesList() == null || entity.getInfoEntitiesList().size() <= 0) {
      // 设置宽度
      table.getHeader().getCell(0, 0).setWidth(200f);

      table.addCell(cell("无测试数据", TextAlignment.LEFT, true));
      table.addCell(cell("", true));
      table.addCell(cell("", true));
    } else {
      Boolean isExecuteStateText = entity.getIsExecuteStateText();

      for (int i = 0; i < entity.getInfoEntitiesList().size(); i++) {
        infoEntity = entity.getInfoEntitiesList().get(i);
        table.addCell(cell(infoEntity.getTime(), TextAlignment.CENTER, true));
       // table.addCell(cell("Story: "+infoEntity.getStory().name()+" "+infoEntity.getStory().description(), TextAlignment.CENTER, true));
        table.addCell(cell(infoEntity.getComplete(), infoEntity.getRemarks(), true));
        //table.addCell(cell("SCENARIO: "+infoEntity.getScenarioTest().value(), TextAlignment.CENTER, true));
       // table.addCell(cell("SCENE: "+infoEntity.getScene(), TextAlignment.CENTER, true));
       // table.addCell(cell(infoEntity.getComplete(), infoEntity.getScenarioTest().value(), true));
        table.addCell(
            cell(
                (isExecuteStateText != null && isExecuteStateText)
                    ? convertText(infoEntity.getExecuteState())
                    : convertImage(infoEntity.getExecuteState()),
                TextAlignment.CENTER,
                true));
      }
    }

    document.add(table);
    table = null;
  }

  /** 创建页脚数据 */
  public void createFoot(Document document, float pageWidth) {
    Div foot = new Div();
    foot.setWidth(pageWidth);
    // 设置布局方式 单行
    foot.setNextRenderer(new LineRenderer());

    float width = pageWidth / 4;
    foot.add(new Paragraphs("", width).paragraph());
    foot.add(new Paragraphs("", width).paragraph());
    foot.add(new Paragraphs("日期", width).paragraph());
    foot.add(new Paragraphs("姓名", width).paragraph());
    document.add(foot);
    foot = null;
  }

  /** 创建文档头部 */
  public void createHead(PdfDocument pdfDocument, Document document, float pageWidth)
      throws IOException {
    float rightSpacing = 7f;
    float logoHeight = 0;

    float width = (pageWidth - 100 - MAX_BARCODE_WIDTH - 3 * rightSpacing);
    float barcodeWidth =
        (entity.getQrcode() != null
                && entity.getQrcode().getWidth() != null
                && entity.getQrcode().getWidth() < MAX_BARCODE_WIDTH)
            ? entity.getQrcode().getWidth()
            : MAX_BARCODE_WIDTH;

    Image logo = null;
    if (entity.getLogo() != null) {
      logo = new Image(ImageDataFactory.create(entity.getLogo()));
      logo.setWidth(100);

      // 图片高度
      logoHeight = 100 / logo.getImageWidth() * logo.getImageHeight();
    }

    Table table = new Table(new float[] {100f, width, MAX_BARCODE_WIDTH});
    table.setBorder(Border.NO_BORDER);

    // 左侧logo
    Paragraph leftHead = new Paragraphs(logo, 100f).paragraph();
    Cell leftHeadCell = new Cell();
    leftHeadCell.setBorder(Border.NO_BORDER);
    leftHeadCell.setTextAlignment(TextAlignment.LEFT);
    leftHeadCell.add(leftHead);

    // 中间标题
    Paragraph bodyHead =
        new Paragraphs(entity.getTitle(), width, TextAlignment.CENTER, 14f, boldFont, false)
            .paragraph();

    Cell bodyHeadCell = new Cell();
    bodyHeadCell.setBorder(Border.NO_BORDER);
    bodyHeadCell.setTextAlignment(TextAlignment.CENTER);
    bodyHeadCell.add(bodyHead);

    // 右侧条形码
    Paragraph rightHead = createBarcode(pdfDocument, MAX_BARCODE_WIDTH, barcodeWidth, logoHeight);
    Cell rightHeadCell = new Cell();
    rightHeadCell.setBorder(Border.NO_BORDER);
    rightHeadCell.setTextAlignment(TextAlignment.RIGHT);
    rightHeadCell.add(rightHead);

    table.addHeaderCell(leftHeadCell);
    table.addHeaderCell(bodyHeadCell);
    table.addHeaderCell(rightHeadCell);
    document.add(table);
    table = null;
  }

  /**
   * 创建头部行模板
   *
   * @param document 文档
   * @param pageWidth 文档宽度
   * @param cellOneKey 第一列名称
   * @param cellOneVal 第一列值
   * @param cellTwoKey 第二列名称
   * @param cellTwoVal 第二列值
   * @param cellThreeKey 第三列名称
   * @param cellThreeVal 第三列值
   */
  public void createHeadCellTemplate(
      Document document,
      float pageWidth,
      String cellOneKey,
      Object cellOneVal,
      String cellTwoKey,
      Object cellTwoVal,
      String cellThreeKey,
      Object cellThreeVal) {
    Paragraph div = new Paragraph();
    div.setWidth(pageWidth);
    div.setFontSize(10);
    div.setBold();
    div.setPadding(0);
    div.setMargin(0);
    div.setBorder(Border.NO_BORDER);

    int cell = 6;
    float rightSpacing = 5f;
    // (页面宽度(pageWidth)-自定义宽度(28 - 52 - 52 - 100)-右边距(((cell - 1) * rightSpacing + 1)))/剩余列(2)
    float width = (pageWidth - 28 - 52 - 52 - 100 - ((cell - 1) * rightSpacing + 1)) / 2;

    // 第一列
    div.add(new Paragraphs(cellOneKey, 28f, TextAlignment.RIGHT).paragraph());
    // 第二列
    div.add(new Paragraphs(cellOneVal, width).paragraph());
    // 第三列
    div.add(new Paragraphs(cellTwoKey, 52f, TextAlignment.RIGHT).paragraph());
    // 第四列
    div.add(new Paragraphs(cellTwoVal, width).paragraph());
    // 第五列
    div.add(new Paragraphs(cellThreeKey, 52f, TextAlignment.RIGHT).paragraph());
    // 第六列
    div.add(new Paragraphs(cellThreeVal, 100f, TextAlignment.LEFT).paragraph());

    document.add(div);
    div = null;
  }

  /** 测试头部信息 第一行 */
  public void createTestHead1(Document document, float pageWidth) {
    createHeadCellTemplate(
        document,
        pageWidth,
        "部门:",
        entity.getDepartment(),
        "人员:",
        entity.getPerson(),
        "日期:",
        entity.getCreateDate() != null
            ? DateTimeUtils.DATE_TIME.formatDate(entity.getCreateDate())
            : null);
  }

  /** 测试头部信息 第二行 */
  public void createTestHead2(Document document, float pageWidth) {
    createHeadCellTemplate(
        document,
        pageWidth,
        "环境:",
        entity.getEnvironment(),
        "成功:",
        entity.getSuccessfulSize(),
        "失败:",
        entity.getFailedSize());
  }

  /** 测试头部信息 第三行 */
  public void createTestHead3(Document document, float pageWidth) {
    createHeadCellTemplate(
        document,
        pageWidth,
        "版本:",
        (entity.getVersion() == null ? "未定义" : entity.getVersion()),
        "忽略:",
        entity.getDisabledSize(),
        "终止:",
        entity.getAbortedSize());
  }

  /** 测试头部信息 第四行 */
  public void createTestHead4(Document document, float pageWidth) {
    createHeadCellTemplate(
        document,
        pageWidth,
        "耗时:",
        (entity.getTotalTime() != null)
            ? DateTimeUtils.TIME_COUNT_SSS.formatDate(entity.getTotalTime())
            : null,
        null,
        null,
        null,
        null);
  }

  /**
   * 绘制条形码
   *
   * @param width 条形码宽度
   * @param height 条形码高度
   */
  public Image drawBarcode(PdfDocument document, float width, float height) {
    Barcode128 code128 = new Barcode128(document);

    // 文本上下
    //    code128.setBaseline(-1);
    code128.setBarHeight(40);
    code128.setCode(createCodeData());
    code128.setCodeType(Barcode128.CODE128);
    // 文本左右
    code128.setTextAlignment(Barcode1D.ALIGN_CENTER);
    PdfFormXObject formXObject = code128.createFormXObject(document);
    Image image = new Image(formXObject);
    image.setWidth(width);
    image.setMaxHeight(40);
    // image.setBackgroundColor(DeviceGray.GRAY);

    return image;
  }

  /**
   * 创建二维码
   *
   * @param width 绘制宽度
   */
  public Image drawQRcode(Float width) throws IOException {
    // 条形码配置
    QrcodeConfig qrcodeConfig =
        new QrcodeConfig(Float.valueOf(width).intValue(), Float.valueOf(width).byteValue());
    if (entity.getQrcode() != null) {
      bindQrcode(entity.getQrcode(), qrcodeConfig);
    }

    // 创建条形码
    SimpleQrcodeGenerator qrcodeGenerator = new SimpleQrcodeGenerator(qrcodeConfig);
    // 添加 logo
    if (entity.getQrcode() != null
        && entity.getQrcode().getLogo() != null
        && entity.getQrcode().getLogo().getPath() != null) {
      // 添加logo
      qrcodeGenerator.setLogo(FileUtil.file(entity.getQrcode().getLogo().getPath()));

      // logo样式
      if (entity.getQrcode().getLogo().getLogoShape() != null)
        qrcodeConfig.setLogoShape(entity.getQrcode().getLogo().getLogoShape());

      // 绑定logo数据
      bindLogo(entity.getQrcode().getLogo(), qrcodeConfig.getLogoConfig());
    }

    BufferedImage bufferedImage = qrcodeGenerator.generate(createCodeData()).getImage();

    // 绘制在新的画布
    // BufferedImage newBufferedImage =
    //     new BufferedImage(
    //         bufferedImage.getWidth(), bufferedImage.getHeight(),
    // BufferedImage.TYPE_USHORT_GRAY);
    // newBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);

    // 转换 BufferedImage=> PDF Image
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
    Image image = new Image(ImageDataFactory.create(byteArrayOutputStream.toByteArray()));
    image.setHeight(width);

    byteArrayOutputStream.flush();
    byteArrayOutputStream.close();
    bufferedImage = null;
    qrcodeGenerator = null;
    byteArrayOutputStream = null;

    return image;
  }

  /** 跨行设置 */
  public void enjambment(Cell cell) {
    // 表格跨页分行
    cell.setKeepTogether(true);
    cell.setKeepWithNext(true);
  }

  /** 初始化 */
  private void init() throws IOException {
    font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);

    // 加载自定义字体
    FontProgram fontProgram =
        FontProgramFactory.createFont(
            this.getClass().getResource("/font/NotoSansCJKjp-Bold.otf").getFile());
    boldFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, false);
  }
}
