package com.moqifei.bdd.jupiter.report.testframe;


import java.io.IOException;
import java.io.OutputStream;

import com.moqifei.bdd.jupiter.report.util.StringUtils;
/**
 * Description:打印工具类 <br>
 */
public class Printing {

  // 空格
  private static final String SPACE = " ";
  // 换行 String
  private static final String SBR = System.getProperty("line.separator");
  // 换行
  private static final byte[] BR = SBR.getBytes();
  // 打印输出每行前缀
  private String PREFIX = "<";
  // 打印输出每行后缀
  private String SUFFIX = ">";
  // 内容默认绘制符号
  private String DEFAULT_SYMBOL = "-";
  // 每行默认打印长度
  private Integer DEFAULT_CONTEXT_LENGTH = 120;
  // 输出流
  private OutputStream out;

  public Printing(OutputStream out) {
    this(out, null, null, null, null);
  }

  public Printing(OutputStream out, String PREFIX) {
    this(out, PREFIX, null, null, null);
  }

  public Printing(OutputStream out, String PREFIX, String SUFFIX) {
    this(out, PREFIX, SUFFIX, null, null);
  }

  public Printing(OutputStream out, String PREFIX, String SUFFIX, String DEFAULT_SYMBOL) {
    this(out, PREFIX, SUFFIX, DEFAULT_SYMBOL, null);
  }

  public Printing(OutputStream out, Integer DEFAULT_CONTEXT_LENGTH) {
    this(out, null, null, null, DEFAULT_CONTEXT_LENGTH);
  }

  /**
   * 创建打印工具类
   *
   * @param out 输出流
   * @param PREFIX 每行前缀
   * @param SUFFIX 每行后缀
   * @param DEFAULT_SYMBOL 默认每行打印字符
   * @param DEFAULT_CONTEXT_LENGTH 每行总长度, 超出直接打印
   */
  public Printing(
      OutputStream out,
      String PREFIX,
      String SUFFIX,
      String DEFAULT_SYMBOL,
      Integer DEFAULT_CONTEXT_LENGTH) {
    if (out == null) {
      throw new NullPointerException("输出流不能为空!");
    } else {
      this.out = out;
    }

    if (PREFIX != null) {
      this.PREFIX = PREFIX;
    }

    if (SUFFIX != null) {
      this.SUFFIX = SUFFIX;
    }

    if (DEFAULT_SYMBOL != null) {
      this.DEFAULT_SYMBOL = DEFAULT_SYMBOL;
    }

    if (DEFAULT_CONTEXT_LENGTH != null) {
      this.DEFAULT_CONTEXT_LENGTH = DEFAULT_CONTEXT_LENGTH;
    }
  }

  public static Printing New(
      OutputStream out,
      String PREFIX,
      String SUFFIX,
      String DEFAULT_SYMBOL,
      Integer DEFAULT_CONTEXT_LENGTH) {
    return new Printing(out, PREFIX, SUFFIX, DEFAULT_SYMBOL, DEFAULT_CONTEXT_LENGTH);
  }

  public static Printing New(
      OutputStream out, String PREFIX, String SUFFIX, String DEFAULT_SYMBOL) {
    return new Printing(out, PREFIX, SUFFIX, DEFAULT_SYMBOL);
  }

  public static Printing New(OutputStream out, String PREFIX, String SUFFIX) {
    return new Printing(out, PREFIX, SUFFIX);
  }

  public static Printing New(OutputStream out, String PREFIX) {
    return new Printing(out, PREFIX);
  }

  public static Printing New(OutputStream out) {
    return new Printing(out);
  }

  /**
   * 换行-单行
   *
   * @return
   */
  public Printing br() {
    // 输出
    outln(null);
    return this;
  }

  /**
   * 换行
   *
   * @return
   */
  public Printing br(int size) {
    // 输出
    try {
      for (int i = 0; i < size; i++) {
        out.write(BR);
      }
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this;
  }

  /**
   * 切换笔刷
   *
   * @param brush
   * @return
   */
  public Printing brush(String brush) {
    this.DEFAULT_SYMBOL = brush;
    return this;
  }

  /**
   * ** 打印数据-中间
   *
   * @param msg 输出内容
   */
  public Printing center(String msg) {
    print(msg, Layout.CENTER);
    return this;
  }

  /**
   * 默认笔刷
   *
   * @return
   */
  public Printing defaultBrush() {
    this.DEFAULT_SYMBOL = "-";
    return this;
  }

  /**
   * 添加描述信息, 中文 英文不截断换行
   *
   * @param msg 描述信息
   * @return
   */
  public Printing describe(String msg) {
    if (msg == null) {
      return this;
    }

    String text = "";
    int mLen = StringUtils.len(msg);
    if (mLen > DEFAULT_CONTEXT_LENGTH) {
      // 首行2空格
      text += SPACE + SPACE;

      int i = 0;
      int count = 0;
      String str = "";
      int row =
          (mLen % DEFAULT_CONTEXT_LENGTH == 0
              ? mLen / DEFAULT_CONTEXT_LENGTH
              : mLen / DEFAULT_CONTEXT_LENGTH + 1);

      while (true) {
        // 结束
        if (i >= msg.length()) {
          break;
        }

        // 换行
        if (count >= DEFAULT_CONTEXT_LENGTH && i + 1 < msg.length() && msg.charAt(i) == ' ') {
          text += SBR + SPACE;
          count = 0;
        }

        text += msg.charAt(i);
        count += StringUtils.lenChinese(String.valueOf(msg.charAt(i)));
        i++;
      }
    } else {
      text = msg;
    }

    // 输出
    outln(text);
    text = null;
    return this;
  }

  /** 销毁资源 */
  public void destroy() {
    try {
      out.flush();
      out.close();
      out = null;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 打印默认格式化
   *
   * @return
   */
  public Printing layout() {
    String text = "";

    // 前缀
    text += PREFIX;

    // 内容输出
    for (int i = 0; i < DEFAULT_CONTEXT_LENGTH; i++) {
      text += DEFAULT_SYMBOL;
    }

    // 结尾输出
    text += SUFFIX;
    outln(text);
    text = null;
    return this;
  }

  /**
   * ** 打印数据-左侧
   *
   * @param msg 输出内容
   */
  public Printing left(String msg) {
    print(msg, Layout.LEFT);
    return this;
  }

  /**
   * 输出
   *
   * @param msg
   */
  private void out(String msg) {
    // 输出
    try {
      if (msg != null) {
        out.write(msg.getBytes());
      }
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 输出换行, msg =null, 输出换行
   *
   * @param msg
   */
  private void outln(String msg) {
    // 输出
    try {
      if (msg != null) {
        out.write(msg.getBytes());
      }
      out.write(BR);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * ** 打印数据
   *
   * @param leftMsg 左边内容
   * @param centerMsg 中间内容
   * @param rightMsg 右边内容
   */
  public Printing print(String leftMsg, String centerMsg, String rightMsg) {
    String text = "";
    int leftMsgLen = StringUtils.len(leftMsg);
    int centerMsgLen = StringUtils.len(centerMsg);
    int rightMsgLen = StringUtils.len(rightMsg);
    int ps = PREFIX.length() + SUFFIX.length();
    int length = ps + leftMsgLen + centerMsgLen + rightMsgLen;
    int maxLen = ps + DEFAULT_CONTEXT_LENGTH;
    // 与原字符差
    int s =
        (length
                - ps
                - ((leftMsg != null ? leftMsg.length() : 0)
                    + (centerMsg != null ? centerMsg.length() : 0)
                    + (rightMsg != null ? rightMsg.length() : 0)))
            / 2;

    if (length < maxLen) {
      int c = (leftMsg == null ? 1 : 0);
      c += (centerMsg == null ? 1 : 0);
      c += (rightMsg == null ? 1 : 0);

      int difference = maxLen - length + s + c;
      int sign = difference / 2;

      // 内容输出
      for (int i = 0; i < difference; i++) {
        // 左侧输出
        if (i == 0) {
          text += (leftMsg == null ? DEFAULT_SYMBOL : SPACE + leftMsg);
        }
        // 中间输出
        else if (i == (centerMsgLen == 0 ? sign : sign - 1)) {
          text += (centerMsg == null ? DEFAULT_SYMBOL : SPACE + centerMsg);
        }
        // 右侧输出
        else if (i == (rightMsgLen == 0 ? difference : difference - 1)) {
          text += (rightMsg == null ? DEFAULT_SYMBOL : SPACE + rightMsg);
        }
        // 输出默认定义字符
        else {
          text += DEFAULT_SYMBOL;
        }
      }
    } else {
      text = leftMsg + SPACE + centerMsg + SPACE + rightMsg;
    }

    // 输出
    outln(text);
    text = null;
    return this;
  }

  /**
   * ** 打印数据
   *
   * @param msg 打印信息
   */
  public Printing print(String msg, Layout layout) {
    if (layout == null || layout.equals(Layout.BOTH_ENDS)) {
      throw new RuntimeException("Layout.BOTH_ENDS 定义错误!");
    }

    String text = "";
    int msgLen = StringUtils.len(msg);
    int ps = PREFIX.length() + SUFFIX.length();
    int length = ps + msgLen;
    int maxLen = ps + DEFAULT_CONTEXT_LENGTH;

    if (length < maxLen) {
      int difference = maxLen - length + (msgLen - msg.length() - ps / 2) / 2;
      int sign = difference / 2;
      // 前缀
      text += PREFIX;

      // 内容输出
      for (int i = 0; i < difference; i++) {
        // @formatter:off
        if ( // 左侧输出
        layout.equals(Layout.LEFT) && i == 0
            ||
            // 中间输出
            layout.equals(Layout.CENTER) && i == (sign - 1)
            ||
            // 右侧输出
            layout.equals(Layout.RIGHT) && i == (difference - 1)) {
          // @formatter:on
          text += msg;
        }
        // 输出默认定义字符
        else {
          text += DEFAULT_SYMBOL;
        }
      }

      // 结尾输出
      text += SUFFIX;
    } else {
      text = PREFIX + msg + SUFFIX;
    }
    // 输出
    outln(text);
    text = null;
    return this;
  }

  /**
   * ** 打印数据-右侧
   *
   * @param msg 输出内容
   */
  public void right(String msg) {
    print(msg, Layout.RIGHT);
  }

  /** 打印布局方式 */
  public enum Layout {
    /** 居中 */
    CENTER,
    /** 左对齐 */
    LEFT,
    /** 右对齐 */
    RIGHT,
    /** 两端对齐 */
    BOTH_ENDS;
  }
}
