/**
 * 
 */
package com.jdbc;

/**
 * @Title: ParameterDirection
 * @Description:
 * @Version:1.0.0
 * @author jzn
 * @date 2019年1月16日
 */
public enum ParameterDirection {
	// 摘要:
	// 参数是输入参数。
	Input,
	//
	// 摘要:
	// 参数是输出参数。
	Output,
	//
	// 摘要:
	// 参数既能输入，也能输出。
	InputOutput,
	//
	// 摘要:
	// 参数表示诸如存储过程、内置函数或用户定义函数之类的操作的返回值。
	ReturnValue,
}
