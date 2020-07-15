package com.moqifei.bdd.jupiter.report.pdf;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/** 
 * Description:Pdf 生成工厂
 *  
*/
public class PdfFactory {

  /**
   * * 创建PdfFactory
   *
   * @return
   */
  public static PdfFactory PDF() throws IOException {
    return new PdfFactory();
  }

  /**
   * 生成pdf数据文件
   *
   * @param path 文件路径
   * @param pdfTestTheme 数据生成处理类
   */
  public void execute(String path, PdfTestTheme pdfTestTheme) throws Exception {
    execute(new File(path), pdfTestTheme, null);
  }

  /**
   * 生成pdf数据文件
   *
   * @param path 文件路径
   * @param pdfTestTheme 数据生成处理类
   * @param password pdf密码
   */
  public void execute(String path, PdfTestTheme pdfTestTheme, String password) throws Exception {
    execute(new File(path), pdfTestTheme, password);
  }

  /**
   * 生成pdf数据文件
   *
   * @param pdfFile 文件
   * @param pdfTestTheme 数据生成处理类
   */
  public void execute(File pdfFile, PdfTestTheme pdfTestTheme) throws Exception {
    execute(pdfFile, pdfTestTheme, null);
  }

  /**
   * 生成pdf数据文件
   *
   * @param pdfFile 文件
   * @param pdfTestTheme 数据生成处理类
   * @param password pdf密码
   */
  public void execute(File pdfFile, PdfTestTheme pdfTestTheme, String password) throws Exception {
    PdfDocument pdf = null;
    if (password != null) {
      // @formatter:off
      WriterProperties properties =
          new WriterProperties()
              .setStandardEncryption(
                  password.getBytes(), // 用户密码
                  password.getBytes(), // 所有者密码
                  EncryptionConstants.ALLOW_PRINTING, // 操作权限 允许打印
                  EncryptionConstants.ENCRYPTION_AES_128 // 加密处理
                      | EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
      // @formatter:on

      PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(pdfFile), properties);
      pdf = new PdfDocument(pdfWriter);
    } else {
      pdf = new PdfDocument(new PdfWriter(pdfFile));
    }

    // 执行绘制数据处理
    if (pdfTestTheme == null) {
      throw new NullPointerException("PdfTestTheme 对象为空.");
    }

    // 创建
    pdfTestTheme.build(pdf);
  }
}
