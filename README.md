# bdd-jupiter
## 1. Overview
本文档的编写目标是为编写单元测试的程序员、Junit 5扩展模型实践者以及BDD参与践行者等提供综合参考文献。
### 1.1. What is bdd-jupiter?
bdd-jupiter = bdd style unit testing + junit 5 jupiter.

bdd style unit tesing 意味着使用具体的用户故事场景来指导单元测试的开发，对于BDD（行为驱动开发）的更多介绍，请参看行为驱动开发（BDD）全面介绍。

junit 5 jupiter 是Junit 5 为编写单元测试提供的新 programming model 和 extension model 组合利器。
### 1.2. Supported Java Versions
bdd-jupiter需要Java 8及以上的JVM运行时环境。
### 1.3. Getting Started
设置mvn依赖：
```
<dependency>
    <groupId>com.moqifei.bdd.jupiter</groupId>
    <artifactId>bdd-jupiter</artifactId>
    <version>1.0.1-GA</version>
    <scope>test</scope>
</dependency>
```
## 2. BDD Style Unit Testing
bdd-jupiter测试框架可以被用来创建、执行基于故事和行为的BDD测试
### 2.1. Annotations
bdd-jupiter支持如下用于配置BDD Style单测用例的Annotations
<table>
        <tr>
            <th>Annotation</th>
            <th>Description</th>
        </tr>
        <tr>
            <th>@Story</th>
            <th  align="left">用于标识测试类是一个BDD Style的测试cases，可配置的name及description属性，可用于生成的测试报告展示。@Story注解被@TestTemplate标注，@ExtendWith(StoryExtension.class) 注解显式地标注，StoryExtension类通过Junit 5 Jupiter提供的测试生命周期回调扩展机制，实现了执行测试用例完毕后，打印测试报告的扩展功能</th>
        </tr>
        <tr>
            <th>@ScenarioTest</th>
            <th align="left">代表了在@Story标注的测试类中的一个测试case，可配置的value属性，可用于生成的测试报告展示。@ScenarioTest注解被@TestTemplate及@ExtendWith(ScenarioTestExtension.class)注解显示地标注，ScenarioTestExtension类通过Juint 5 Jupiter提供的TestTemplate扩展机制，实现了基于用户故事场景，BDD Style的测试案列编写支撑能力。此外，@ScenarioTest注解包装了Junit 5 Jupiter 内置的TestTemplate扩展机制实现@ParameterizedTest，这意味着，你可以透明地使用@ValueSource、@MethodSource、@CsvFileSource等@ParameterizedTest内置的数据源与@ScenarioTest搭配，进而丰富了@ScenarioTest编写一般测试案列的支持能力</th>
        </tr>
        <tr>
            <th>@ScenarioSource</th>
            <th align="left">@ScenarioTest内置的数据源之一，推荐使用方式为（@ScenarioSource(Scene.class)），一个Scene实例，是BDD Style测试用例的核心，它描述了在一个Story中测试细节，囊括了执行这个测试Sceneario的所有必要信息。Scene实例可以存储客户自定义的测试要素，并在BDD Style测试用例不同阶段（given,when,then）的上下文中传递测试要素</th>
        </tr>
        <tr>
            <th>@ScenarioJsonSource</th>
            <th align="left">@ScenarioTest内置的数据源之一，使用方式为@ScenarioJsonSource(resources = "/dataSet/xxx.json", instance = T.class, key = "key"), @ScenarioJsonSource完成了将xxx.json中的json格式文件，注入到Scene实例中，以"key"为键，T类为实例存储，故xxx.json测试要素得以在DD Style测试用例不同阶段（given,when,then）的上下文中使用</th>
        </tr>
    </table>  
    
### 2.2. BDD Style Design Ideas
类图  
![Image](https://github.com/moqifei/bdd-jupiter/blob/BRANCH_1.0.1/src/pic/class.jpg)

