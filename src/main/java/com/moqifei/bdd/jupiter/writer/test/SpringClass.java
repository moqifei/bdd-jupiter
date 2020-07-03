package com.moqifei.bdd.jupiter.writer.test;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;

//@Service
public class SpringClass {
	//@Autowired
	private PoJoClass poJoClass;
	
	public int doAdd() {
		int a = poJoClass.testAdd(poJoClass.testReduce(3, 1), 2);
		return a;
	}
}
