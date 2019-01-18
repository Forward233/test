/**
 * 
 */
package com.jdbc;

/**
* @Title: SqlParameter
* @Description: 
* @Version:1.0.0  
* @author jzn
* @date 2019年1月16日
*/
public class SqlParameter extends DbParameter {
	/**
	 * 构造函数
	 */
	public SqlParameter() {
	}

	/**
	 * 构造函数
	 * 
	 * @param parameterName
	 *            ：参数名称
	 * @param value
	 *            :参数值
	 */
	public SqlParameter(String parameterName, Object value) {
		super.ParameterName = parameterName;
		super.value = value;
		super.Direction = ParameterDirection.Input;
	}

	/**
	 * 构造函数
	 * 
	 * @param parameterName
	 *            ：参数名称
	 * @param dbType
	 *            ：参数输入输出类型
	 */
	public SqlParameter(String parameterName, SqlDbType dbType) {
		this(parameterName, dbType, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param parameterName
	 *            ：参数名称
	 * @param dbType
	 *            ：参数输入输出类型
	 * @param size
	 *            :数据大小(长度)
	 */
	public SqlParameter(String parameterName, SqlDbType dbType, int size) {
		this(parameterName, dbType, size, ParameterDirection.Input);
	}

	/**
	 * 构造函数
	 * 
	 * @param parameterName
	 *            ：参数名称
	 * @param dbType
	 *            ：参数输入输出类型
	 * @param size
	 *            :数据大小(长度)
	 * @param direction
	 *            :参数输入输出类型
	 */
	protected SqlParameter(String parameterName, SqlDbType dbType, int size, ParameterDirection direction) {
		super.ParameterName = parameterName;
		sqlDbType = dbType;
		super.Size = size;
		super.Direction = direction;
	}

	/**
	 * 获取参数字段的SQL类型
	 * 
	 * @return
	 */
	public SqlDbType getSqlDbType() {
		return sqlDbType;
	}

	/**
	 * 设置参数字段的SQL类型
	 * 
	 * @param sqlDbType
	 */
	public void setSqlDbType(SqlDbType sqlDbType) {
		this.sqlDbType = sqlDbType;
	}

	/**
	 * 获取参数的类型
	 * 
	 * @return
	 */
	public DbType getDbType() {
		return DbType;
	}

	/**
	 * 设置参数的类型
	 * 
	 * @param dbType
	 */
	public void setDbType(DbType dbType) {
		DbType = dbType;
	}

	/**
	 * 获取参数的大小(长度)
	 * 
	 * @return
	 */
	public int getSize() {
		return Size;
	}

	/**
	 * 设置参数的大小(长度)
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		Size = size;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return
	 */
	public String getParameterName() {
		return ParameterName;
	}

	/**
	 * 设置参数名称
	 * 
	 * @param parameterName
	 */
	public void setParameterName(String parameterName) {
		ParameterName = parameterName;
	}

	/**
	 * 获取参数值
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 *设置 参数值
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 获取指定查询内参数的输入输出类型
	 * 
	 * @return
	 */
	public ParameterDirection getDirection() {
		return Direction;
	}

	/**
	 * 设置指定查询内参数的输入输出类型
	 * 
	 * @param ParameterDirection
	 */
	public void setDirection(ParameterDirection direction) {
		Direction = direction;
	}

	/**
	 * 克隆
	 */
	public SqlParameter clone() {
		try {
			return (SqlParameter) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
