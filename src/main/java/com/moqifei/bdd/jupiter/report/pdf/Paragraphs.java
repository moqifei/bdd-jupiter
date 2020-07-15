package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.*;
/**
 * 段落
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Paragraphs {

  private Object object; //  数据String/Image/Number
  private Float width;
  private Float height;
  private TextAlignment alignment; // 文本对齐
  private HorizontalAlignment halignment; // 左右对齐
  private VerticalAlignment valignment; // 上下对齐
  private Float fontSize; // 字体大小
  private PdfFont font; // 字体
  private Color fontColor; // 字体颜色
  private Boolean isBold; // 是否加粗
  private Boolean isItalic; // 是否斜体

  public Paragraphs(Object object) {
    this.object = object;
  }

  public Paragraphs(Object object, Float width) {
    this.object = object;
    this.width = width;
  }

  public Paragraphs(Float height, Object object) {
    this.object = object;
    this.height = height;
  }

  public Paragraphs(Object object, Float width, Float height) {
    this.object = object;
    this.width = width;
    this.height = height;
  }

  public Paragraphs(Object object, Float width, Float height, TextAlignment alignment) {
    this.object = object;
    this.width = width;
    this.height = height;
    this.alignment = alignment;
  }

  public Paragraphs(Object object, Float width, TextAlignment alignment) {
    this.object = object;
    this.width = width;
    this.alignment = alignment;
  }

  public Paragraphs(
      Object object,
      Float width,
      TextAlignment alignment,
      Float fontSize,
      PdfFont font,
      Boolean isBold) {
    this.object = object;
    this.width = width;
    this.alignment = alignment;
    this.fontSize = fontSize;
    this.font = font;
    this.isBold = isBold;
  }

  public Paragraphs(Object object, TextAlignment alignment, Float width) {
    this.object = object;
    this.width = width;
    this.alignment = alignment;
  }

  /**
   * paragraph 上下布局问题
   *
   * @param paragraph 段容器
   * @param valignment 上中下位置
   */
  public static void VerticalAlignment(Paragraph paragraph, VerticalAlignment valignment) {
    paragraph.setPadding(0);
    // 获取具体定义的 数据 paragraph.getProperty(Property.HEIGHT)

    for (IElement element : paragraph.getChildren()) {
      if (element instanceof Image) {
        System.out.println(
            ((Image) element).getImageWidth() + "  " + ((Image) element).getImageHeight());
      }
      System.out.println(element.toString());
    }
  }

  /** 获取Paragraph对象 */
  public Paragraph paragraph() {
    Paragraph paragraph = new Paragraph();
    paragraph.setPadding(0);

    // object
    if (object != null) {
      if (object instanceof String) paragraph.add(object.toString());
      else if (object instanceof Image) paragraph.add((Image) object);
      else if (object instanceof Number) paragraph.add(String.valueOf(object));
    }

    // width
    if (width != null) paragraph.setWidth(width);
    // height
    if (height != null) paragraph.setHeight(height);
    // alignment
    if (alignment != null) paragraph.setTextAlignment(alignment);
    // halignment
    if (halignment != null) paragraph.setHorizontalAlignment(halignment);
    // valignment
    if (valignment != null) paragraph.setVerticalAlignment(valignment);
    // fontSize
    if (fontSize != null) paragraph.setFontSize(fontSize);
    // font
    if (font != null) paragraph.setFont(font);
    // fontColor
    if (fontColor != null) paragraph.setFontColor(fontColor);
    // isBold
    if (isBold != null) paragraph.setBold();
    // isItalic
    if (isItalic != null) paragraph.setItalic();

    return paragraph;
  }
}
