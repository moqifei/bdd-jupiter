package com.moqifei.bdd.jupiter.report.testframe;

import com.moqifei.bdd.jupiter.extension.Scene;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.Story;

import lombok.*;
/**
 * Description:数据记录信息 <br>
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InfoEntity {

  public String className;
  public String methodName;
  public Explains explains;
  public Story story;
  public ScenarioTest scenarioTest;
  public Scene scene;
  public ExecuteState executeState; // 执行结果
  public String remarks; // 备注
  public String time; // 执行时间
  public String complete; // 完整说明 类名.方法:简要方法说明

  public enum ExecuteState {
    /** 禁用 * */
    DISABLED,
    /** 失败 * */
    FAILED,
    /** 成功 * */
    SUCCESSFUL,
    /** 终止 * */
    ABORTED;
  }
}
