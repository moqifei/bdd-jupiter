/*
 * Redistributing "JSON.simple" library. Please visit https://github.com/fangyidong/json-simple
 *
 * Minor modification: using StringBuilder.
 */

package com.moqifei.bdd.jupiter.json;

/**
 * Beans that support customized output of JSON text shall implement this interface.  
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface JSONAware {
	/**
	 * @return JSON text
	 */
	String toJSONString();
}
