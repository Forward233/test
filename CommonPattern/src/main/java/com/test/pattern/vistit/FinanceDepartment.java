package com.test.pattern.vistit;

public class FinanceDepartment extends Department{

	@Override
	public void Visit(FullTimeEmployee employee) {
		System.out.println("FullTimeEmployee正在处理");
	}

	@Override
	public void Visit(PartTimeEmployee employee) {
		System.out.println("PartTimeEmployee正在处理");
	}

}
