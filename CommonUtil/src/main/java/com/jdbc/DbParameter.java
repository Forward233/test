package com.jdbc;

/**
 * @Title: DbParameter
 * @Description:
 * @Version:1.0.0
 * @author jzn
 * @date 2019年1月16日
 */
public abstract class DbParameter implements IDataParameter, Cloneable {
	/**
	 * 构造函数
	 */
	protected DbParameter() {
	}

	/**
	 * 数据类型
	 */
	protected DbType DbType;

	/**
	 * 数据大小
	 */
	protected int Size;

	/**
	 * 参数名称
	 */
	protected String ParameterName;
	/**
	 * 指定查询内参数的输入输出类型
	 */
	protected ParameterDirection Direction;
	/**
	 * 
	 */
	protected SqlDbType sqlDbType;

	/**
	 * 参数值
	 */
	protected Object value;
}
