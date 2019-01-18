package com.test.pattern.vistit;

import java.util.ArrayList;
import java.util.List;

public class EmployeeList {
	
	 private List<IEmployee> empLists = new ArrayList<IEmployee>();;
	 
	 public void AddEmployee(IEmployee emp)
     {
         this.empLists.add(emp);
     }
	 
	 // 重点
	 public void accept(Department department) {
		 for (IEmployee empList : empLists) {
			 empList.accept(department);
		 }
	 }
}
