package com.moqifei.bdd.jupiter.writer.test;

import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.extension.Scene;

 
/** 
* This bdd-jupiter style test cases file was auto-generated, 
* you should completed it by your own intelligence, come on & have fan!
*/
@Story(name = "name", description = "描述")
public class PoJoClassTest {


 
    @ScenarioTest(value = "name")
    @ScenarioJsonSource(resources = "/xxx/xxx.json", instance = Scene.class, key = "xxx")
    public void testTestAdd(Scene scene) { 
        scene.given("given phase desc",()->{//put your given code here.
        })
        .and("given and phase desc",()->{//put your given and code here.
        })
        .when("when phase desc",()->{//put your when code here.
        })
        .then("then phase desc",()->{//put your then code here.
        })
        .and("then and phase desc",()->{//put your then and code here.
        })
        .run();
    }
}