package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
/**
 * Description:测试生成PDF文件主题
 */
public interface PdfTestTheme {

    /*** 构建数据 */
    void build (PdfDocument pdf) throws Exception;

}
