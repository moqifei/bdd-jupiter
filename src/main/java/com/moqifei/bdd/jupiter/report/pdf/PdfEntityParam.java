package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.layout.Document;
import lombok.*;
/**
 * PDF回调参数<br>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PdfEntityParam {

  /** 文档对象 */
  private Document document;
  /** 文档区域宽度 */
  private float pageWidth;
}
