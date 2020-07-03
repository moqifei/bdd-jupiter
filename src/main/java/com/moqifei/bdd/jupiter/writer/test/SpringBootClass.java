package com.moqifei.bdd.jupiter.writer.test;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;


//@Service
public class SpringBootClass {
	//@Autowired
	private PoJoClass poJoClass;
	
	public int doReduce() {
		int a = poJoClass.testReduce(poJoClass.testAdd(3, 1), poJoClass.testReduce(2, 3));
		return a;
	}
}
