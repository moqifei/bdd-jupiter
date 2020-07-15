package com.moqifei.bdd.jupiter.report.single.impl;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.moqifei.bdd.jupiter.report.single.TestReport;
import com.moqifei.bdd.jupiter.report.testframe.Explains;
import com.moqifei.bdd.jupiter.report.testframe.Printing;
import com.moqifei.bdd.jupiter.report.util.DateTimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;
/**
 * Description: JUnit监控case运行状态
 */
@Log4j2
public class TestWatcherImpl implements TestReport {

    //打印对象
    public static Printing printing;//= Printing.New (System.out);
    //开始执行时间
    public static Date startDate;
    private static String PREFIX = "➛";
    private static String filePath = System.getProperty ("user.dir") + "/logs/test-" + DateTimeUtils.DATE_TIME_MILLI_COMPACT.formatNow () + ".log";

    /***************************************************************************/
    //IDEA中获取类加载路径和项目根路径

    //第一种：获取类加载的根路径  D:\Work\IdeaProjects\HelloVelocity\target\classes
    // File f = new File (ControllerUtils.class.getClass ().getResource ("/").getPath ());
    // System.out.println ("第一种：获取类加载的根路径");
    // System.out.println (f);
    // 获取当前类的所在工程路径; 如果不加“/”获取当前类的加载目录  D:\IdeaProjects\cdby_wan\WebRoot\WEB-INF\classes\test\com
    // File f2 = new File (ControllerUtils.class.getClass ().getResource ("").getPath ());
    // System.out.println (f2);

    //第二种：获取项目路径 D:\Work\IdeaProjects\HelloVelocity
    // File directory = new File ("");// 参数为空
    // String courseFile = directory.getCanonicalPath (); // Tomcat环境中运行，会获取Tomcat安装目录的bin目录，不推荐使用
    // System.out.println ("第二种：获取项目路径");
    // System.out.println (courseFile);

    //第三种： /D:/Work/IdeaProjects/HelloVelocity/target/classes/
    // String path = ControllerUtils.class.getClassLoader ().getResource ("").getPath ();
    // System.out.println ("第三种：");
    // System.out.println (path);

    //第四种： D:\Work\IdeaProjects\HelloVelocity
    //结果： C:\Documents and Settings\Administrator\workspace\projectName
    //获取当前工程路径
    // System.out.println ("第四种：");
    // System.out.println (System.getProperty ("user.dir"));

    //第五种：  获取所有的类路径 包括jar包的路径
    // System.out.println (System.getProperty ("java.class.path"));
    /***************************************************************************/
    /***
     * 默认初始化
     */
    static {
        File file = new File (filePath);
        if (!file.exists ()) {
            //文件夹创建
            if (!file.getParentFile ().exists () && file.getParentFile ().isDirectory ()) {
                file.getParentFile ().mkdirs ();
            }

            //创建文件
            try {
                file.createNewFile ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

        log.debug (file.getAbsolutePath ());

        //创建打印对象
        try {
            printing = Printing.New (new FileOutputStream (file));
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }

        file = null;
    }

    /**
     * 测试结束执行
     */
    @AfterAll
    public static void afters () {
        printing.center ("[测试结束]");
        printing.br (3);

        printing.brush (" ");
        printing.right ("日期:            签字:                 ");
    }

    /**
     * 测试开始执行
     */
    @BeforeAll
    public static void befores () {
        printing.layout ();
        printing.center ("XXX有限公司ERP测试报告");
        printing.left ("日期:" + DateTimeUtils.DATE_TIME.formatNow ());
        printing.print ("IT部 ", "LCH ", "Version 1.0");
        printing.layout ();
    }

    /**
     * 获取执行时间
     */
    public String getRunTime () {
        return "[" + DateTimeUtils.TIME_COUNT_SSS.format (startDate, new Date ()) + "]";
    }

    /**
     * 每个测试方法后执行
     */
    @AfterEach
    public void after () {

    }

    /**
     * 每个测试方法前执行
     */
    @BeforeEach
    public void before () {
        //初始时间
        startDate = new Date ();
    }

    /**
     * 输出类 方法信息
     * @param context
     */
    public String getInfo (ExtensionContext context) {
        //获取测试类
        Class<?> clazz = context.getRequiredTestClass ();
        //获取测试方法
        Optional<Method> method = context.getTestMethod ();
        //获取注解
        //Explains story = clazz.getAnnotation (Explains.class);
        Explains story = method.get ().getAnnotation (Explains.class);

        //@formatter:off
        return PREFIX +
               getRunTime () + " " +
               (story != null ? story.value ()+" " : "") +
                clazz.getName () + "." +
                method.get ().getName () +
                " ";
        //@formatter:on
    }

    /**
     * 测试中止
     * @param context
     * @param cause
     */
    @Override
    public void testAborted (ExtensionContext context, Throwable cause) {
        printing.left (getRunTime ());
    }

    /**
     * 测试已禁用
     * @param context
     * @param reason
     */
    @Override
    public void testDisabled (ExtensionContext context, Optional<String> reason) {
        printing.left (getRunTime ());
    }

    /**
     * 测试失败
     * @param context
     * @param cause
     */
    @Override
    public void testFailed (ExtensionContext context, Throwable cause) {
        printing.print (getInfo (context), null, "[✖]");
        printing.describe (cause.getMessage ());
    }

    /**
     * 测试成功
     * @param context
     */
    @Override
    public void testSuccessful (ExtensionContext context) {
        printing.print (getInfo (context), null, "[✔]");
        //描述输出
        //printing.describe ("Java 中判断char 是否为空格 和空 - renxiaoren - 博客园 2016年2月28日 - Java 中判断char 是否为空格 和空 //判断是否char是否为空importjava.util.*; CSDN技术社区 - 百度快照");
    }

}
