package com.pattern.vistit;

public class FullTimeEmployee implements IEmployee {

	public String Name;
	public double WeeklyWage;
	public int WorkTime;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public double getWeeklyWage() {
		return WeeklyWage;
	}

	public void setWeeklyWage(double weeklyWage) {
		WeeklyWage = weeklyWage;
	}

	public int getWorkTime() {
		return WorkTime;
	}

	public void setWorkTime(int workTime) {
		WorkTime = workTime;
	}
	
	public FullTimeEmployee(String name, double weeklyWage, int workTime) {
		super();
		Name = name;
		WeeklyWage = weeklyWage;
		WorkTime = workTime;
	}

	public void accept(Department department) {
		department.Visit(this);
	}

}
