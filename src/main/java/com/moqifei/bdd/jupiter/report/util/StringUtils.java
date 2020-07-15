package com.moqifei.bdd.jupiter.report.util;


import java.util.Map;
import java.util.Random;

import com.moqifei.bdd.jupiter.report.exception.MismatchException;

public class StringUtils {

  public static final Random RM = new Random();

  /**
   * 获取枚举类型
   *
   * @param enums 枚举对象
   * @param key 枚举字符串
   */
  public static Enum enums(Enum[] enums, String key) {
    if (key == null || key.trim().isEmpty()) return null;

    for (Enum em : enums) {
      if (em.name().equals(key)) {
        return em;
      }
    }

    return null;
  }

  /**
   * 中文长度
   *
   * @param chars 制定法
   */
  public static Boolean isChinese(String chars) {
    if (chars != null && chars.matches("[\u0391-\uFFE5]")) return true;
    else return false;
  }

  /**
   * 获取字符串的长度，如果有中文，则每个中文字符计为2位
   *
   * @param value 指定的字符串
   * @return 字符串的长度
   */
  public static int len(String value) {
    // null
    if (value == null) return 0;

    int valueLength = 0;
    String temp;
    String chinese = "[\u0391-\uFFE5]";

    /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
    for (int i = 0; i < value.length(); i++) {
      /* 获取一个字符 */
      temp = value.substring(i, i + 1);
      /* 判断是否为中文字符 */
      if (temp.matches(chinese)) {
        /* 中文字符长度为2 */
        valueLength += 2;
      } else {
        /* 其他字符长度为1 */
        valueLength += 1;
      }
    }

    return valueLength;
  }

  /**
   * 中文长度
   *
   * @param chars 字符
   */
  public static int lenChinese(String chars) {
    if (chars == null) return 0;
    else if (chars.matches("[\u0391-\uFFE5]")) return 2;
    else return 1;
  }

  /**
   * Map 合并
   *
   * @param mapLeft 默认MAP
   * @param mapRight 合并MAP
   */
  // after calling this mapLeft holds the combined data
  public static void merge(Map<String, Object> mapLeft, Map<String, Object> mapRight) {
    // go over all the keys of the right map
    for (String key : mapRight.keySet()) {
      // if the left map already has this key, merge the maps that are behind that key
      if (mapLeft.containsKey(key)) {
        if (mapLeft.get(key) instanceof Map) {
          // 拷贝对象 名称相同 类型不同
          if (!(mapRight.get(key) instanceof Map)) {
            throw new MismatchException(
                "类型不匹配:当前对象类型定义错误, Key:" + key + ", Val:" + mapRight.get(key).toString());
          }

          merge((Map<String, Object>) mapLeft.get(key), (Map<String, Object>) mapRight.get(key));
        } else {
          mapLeft.put(key, mapRight.get(key));
        }
      } else {
        // otherwise just add the map under that key
        mapLeft.put(key, mapRight.get(key));
      }
    }
  }
}
