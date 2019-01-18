package com.test.pattern.vistit;

/**
 * @ClassName: Department
 * @Description: 处理抽象类
 * @author: yhl
 * @date: 2019年1月18日 上午9:12:24
 */
public abstract class Department {
	public abstract void Visit(FullTimeEmployee employee);
	public abstract void Visit(PartTimeEmployee employee);
}
