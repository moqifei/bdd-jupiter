package com.moqifei.bdd.jupiter.report.util;

import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class FileUtil {

  /**
   * 清理文件
   *
   * @param file 文件目录
   * @param retainSize 文件按最新日期保留数量
   */
  public static void cleanFile(File file, int retainSize) {
    // 清理文件
    if (file.exists() && file.isDirectory() && file.listFiles().length > retainSize) {
      File[] files = FileUtil.orderByDate(file);

      for (int i = 0; i < files.length; i++) {
        if (i <= files.length - retainSize) {
          if (files[i].delete()) {
            log.debug("清理文件=>:" + files[i].getName());
          }
        }
      }

      files = null;
    }

    log.debug("清理文件完成");
  }

  /**
   * 获取文件路径 前缀[classpath:]为类路径
   *
   * @param path 路径
   * @return
   */
  public static String file(String path) {
    if (path != null && path.toLowerCase().trim().indexOf("classpath:") == 0) {
      return FileUtil.class.getClass().getResource(path.substring("classpath:".length())).getFile();
    }
    return path;
  }

  /**
   * 获取文件流
   *
   * @param path 文件路径 前缀[classpath:]为类路径
   * @return
   * @throws FileNotFoundException
   */
  public static InputStream fileInputStream(String path) throws FileNotFoundException {
    if (path != null && path.toLowerCase().trim().indexOf("classpath:") == 0) {
      return FileUtil.class.getClass().getResourceAsStream(path.substring("classpath:".length()));
    }
    return new FileInputStream(new File(path));
  }

  /**
   * 打开文件
   *
   * @param file
   */
  public static void openFile(File file) {
    try {
      // 启动已在本机桌面上注册的关联应用程序，打开文件文件file。
      Desktop.getDesktop().open(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 文件按日期排序 * */
  public static File[] orderByDate(File file) {
    File[] files = file.listFiles();
    Arrays.sort(
        files,
        new Comparator<File>() {
          public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) return 1;
            else if (diff == 0) return 0;
            else return -1; // 如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
          }

          public boolean equals(Object obj) {
            return true;
          }
        });

    return files;
  }

  /** 文件按大小排序 */
  public static List orderByLength(File file) {
    List<File> fileList = Arrays.asList(file.listFiles());
    Collections.sort(
        fileList,
        new Comparator<File>() {
          public int compare(File f1, File f2) {
            long diff = f1.length() - f2.length();
            if (diff > 0) return 1;
            else if (diff == 0) return 0;
            else return -1; // 如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
          }

          public boolean equals(Object obj) {
            return true;
          }
        });
    return fileList;
  }

  /** 文件按名称排序 */
  public static List orderByName(File file) {
    List fileList = Arrays.asList(file.listFiles());
    Collections.sort(
        fileList,
        new Comparator<File>() {
          @Override
          public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) return -1;
            if (o1.isFile() && o2.isDirectory()) return 1;
            return o1.getName().compareTo(o2.getName());
          }
        });
    return fileList;
  }
}
