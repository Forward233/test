package com.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title: DBUtil
 * @Description: 数据库JDBC连接工具类
 * @Version:1.0.0
 * @author pancm
 * @date 2018年1月5日
 */
public class DBUtil {

	private static Logger logger = LoggerFactory.getLogger(DBUtil.class);

	/**
	 * 新增/修改
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static int executeUpdate(String sql) throws SQLException {
		Statement stmt = null;
		Connection connection = null;
		int i = 0;
		try {
			connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.createStatement();
			i = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(null, stmt, connection);
		}
		return i;
	}

	public static int executeUpdate(StringBuilder[] sql) throws SQLException {
		Statement stmt = null;
		Connection connection = null;
		int i = 0;
		try {
			connection = ConnectionManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			for (int j = 0; j < sql.length; j++) {
				i = stmt.executeUpdate(sql[j].toString());
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			close(null, stmt, connection);
		}
		return i;
	}

	/**
	 * 批量插入/更新
	 * 
	 * @param sqls
	 * @return
	 * @throws SQLException
	 */
	public int[] updateBatch(List<String> sqls) throws SQLException {
		Statement stmt = null;
		Connection connection = null;
		int[] t = { 0, 0 };
		try {
			connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.createStatement();
			/** 设置不自动提交，以便于在出现异常的时候数据库回滚 **/
			connection.setAutoCommit(false);
			for (int i = 0, j = sqls.size(); i < j; i++) {
				stmt.addBatch(sqls.get(i));
			}
			t = stmt.executeBatch();
			connection.commit();
		} catch (Exception e) {
			if (connection != null) {
				logger.warn("开始数据回滚...");
				try {
					connection.rollback();
				} catch (SQLException e1) {
					logger.error("数据回滚失败...", e1);
				}
			}
		} finally {
			close(null, stmt, connection);
		}
		return t;
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> executeQuery(String sql) throws SQLException {
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = null;
		try {
			connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			list = convertList(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs, stmt, connection);
		}
		return list;
	}

	/**
	 * 将查询的数据转换成List类型
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
		if (null == rs) {
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 执行数据库插入操作
	 *
	 * @param valueMap  插入数据表中key为列名和value为列对应的值的Map对象
	 * @param tableName 要插入的数据库的表名
	 * @return 影响的行数
	 * @throws Exception 
	 */
	public static int insert(String tableName, Map<String, Object> valueMap) throws Exception {

		/** 获取数据库插入的Map的键值对的值 **/
		Set<String> keySet = valueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		/** 要插入的字段sql，其实就是用key拼起来的 **/
		StringBuilder columnSql = new StringBuilder();
		/** 要插入的字段值，其实就是？ **/
		StringBuilder unknownMarkSql = new StringBuilder();
		Object[] bindArgs = new Object[valueMap.size()];
		int i = 0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			columnSql.append(i == 0 ? "" : ",");
			columnSql.append(key);

			unknownMarkSql.append(i == 0 ? "" : ",");
			unknownMarkSql.append("?");
			bindArgs[i] = valueMap.get(key);
			i++;
		}
		/** 开始拼插入的sql语句 **/
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		sql.append(columnSql);
		sql.append(" )  VALUES (");
		sql.append(unknownMarkSql);
		sql.append(" )");
		return executeUpdate(sql.toString(), bindArgs);
	}

	/**
	 * 执行数据库插入操作
	 *
	 * @param datas     插入数据表中key为列名和value为列对应的值的Map对象的List集合
	 * @param tableName 要插入的数据库的表名
	 * @return 影响的行数
	 * @throws Exception 
	 */
	public static int insertAll(String tableName, List<Map<String, Object>> datas) throws Exception {
		/** 影响的行数 **/
		int affectRowCount = -1;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			/** 从数据库连接池中获取数据库连接 **/
			connection = ConnectionManager.getInstance().getConnection();

			Map<String, Object> valueMap = datas.get(0);
			/** 获取数据库插入的Map的键值对的值 **/
			Set<String> keySet = valueMap.keySet();
			Iterator<String> iterator = keySet.iterator();
			/** 要插入的字段sql，其实就是用key拼起来的 **/
			StringBuilder columnSql = new StringBuilder();
			/** 要插入的字段值，其实就是？ **/
			StringBuilder unknownMarkSql = new StringBuilder();
			Object[] keys = new Object[valueMap.size()];
			int i = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				keys[i] = key;
				columnSql.append(i == 0 ? "" : ",");
				columnSql.append(key);

				unknownMarkSql.append(i == 0 ? "" : ",");
				unknownMarkSql.append("?");
				i++;
			}
			/** 开始拼插入的sql语句 **/
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(tableName);
			sql.append(" (");
			sql.append(columnSql);
			sql.append(" )  VALUES (");
			sql.append(unknownMarkSql);
			sql.append(" )");

			/** 执行SQL预编译 **/
			preparedStatement = connection.prepareStatement(sql.toString());
			/** 设置不自动提交，以便于在出现异常的时候数据库回滚 **/
			connection.setAutoCommit(false);
			System.out.println(sql.toString());
			for (int j = 0; j < datas.size(); j++) {
				for (int k = 0; k < keys.length; k++) {
					preparedStatement.setObject(k + 1, datas.get(j).get(keys[k]));
				}
				preparedStatement.addBatch();
			}
			int[] arr = preparedStatement.executeBatch();
			connection.commit();
			affectRowCount = arr.length;
			System.out.println("成功了插入了" + affectRowCount + "行");
			System.out.println();
		} catch (Exception e) {
			if (connection != null) {
				connection.rollback();
			}
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return affectRowCount;
	}

	/**
	 * 执行更新操作
	 *
	 * @param tableName 表名
	 * @param valueMap  要更改的值
	 * @param whereMap  条件
	 * @return 影响的行数
	 * @throws Exception 
	 */
	public static int update(String tableName, Map<String, Object> valueMap, Map<String, Object> whereMap) throws Exception {
		/** 获取数据库插入的Map的键值对的值 **/
		Set<String> keySet = valueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		/** 开始拼插入的sql语句 **/
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");

		/** 要更改的的字段sql，其实就是用key拼起来的 **/
		StringBuilder columnSql = new StringBuilder();
		int i = 0;
		List<Object> objects = new ArrayList<Object>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			columnSql.append(i == 0 ? "" : ",");
			columnSql.append(key + " = ? ");
			objects.add(valueMap.get(key));
			i++;
		}
		sql.append(columnSql);

		/** 更新的条件:要更改的的字段sql，其实就是用key拼起来的 **/
		StringBuilder whereSql = new StringBuilder();
		int j = 0;
		if (whereMap != null && whereMap.size() > 0) {
			whereSql.append(" WHERE ");
			iterator = whereMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				whereSql.append(j == 0 ? "" : " AND ");
				whereSql.append(key + " = ? ");
				objects.add(whereMap.get(key));
				j++;
			}
			sql.append(whereSql);
		}
		return executeUpdate(sql.toString(), objects.toArray());
	}

	/**
	 * 执行删除操作
	 *
	 * @param tableName 要删除的表名
	 * @param whereMap  删除的条件
	 * @return 影响的行数
	 * @throws Exception 
	 */
	public static int delete(String tableName, Map<String, Object> whereMap) throws Exception {
		/** 准备删除的sql语句 **/
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(tableName);

		/** 更新的条件:要更改的的字段sql，其实就是用key拼起来的 **/
		StringBuilder whereSql = new StringBuilder();
		Object[] bindArgs = null;
		if (whereMap != null && whereMap.size() > 0) {
			bindArgs = new Object[whereMap.size()];
			whereSql.append(" WHERE ");
			/** 获取数据库插入的Map的键值对的值 **/
			Set<String> keySet = whereMap.keySet();
			Iterator<String> iterator = keySet.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				whereSql.append(i == 0 ? "" : " AND ");
				whereSql.append(key + " = ? ");
				bindArgs[i] = whereMap.get(key);
				i++;
			}
			sql.append(whereSql);
		}
		return executeUpdate(sql.toString(), bindArgs);
	}

	/**
	 * 可以执行新增，修改，删除
	 *
	 * @param sql      sql语句
	 * @param bindArgs 绑定参数
	 * @return 影响的行数
	 * @throws Exception 
	 */
	public static int executeUpdate(String sql, Object[] bindArgs) throws Exception {
		/** 影响的行数 **/
		int affectRowCount = -1;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			/** 从数据库连接池中获取数据库连接 **/
			connection = ConnectionManager.getInstance().getConnection();
			/** 执行SQL预编译 **/
			preparedStatement = connection.prepareStatement(sql.toString());
			/** 设置不自动提交，以便于在出现异常的时候数据库回滚 **/
			connection.setAutoCommit(false);
			System.out.println(getExecSQL(sql, bindArgs));
			if (bindArgs != null) {
				/** 绑定参数设置sql占位符中的值 **/
				for (int i = 0; i < bindArgs.length; i++) {
					preparedStatement.setObject(i + 1, bindArgs[i]);
				}
			}
			/** 执行sql **/
			affectRowCount = preparedStatement.executeUpdate();
			connection.commit();
			String operate;
			if (sql.toUpperCase().indexOf("DELETE FROM") != -1) {
				operate = "删除";
			} else if (sql.toUpperCase().indexOf("INSERT INTO") != -1) {
				operate = "新增";
			} else {
				operate = "修改";
			}
			System.out.println("成功" + operate + "了" + affectRowCount + "行");
			System.out.println();
		} catch (Exception e) {
			if (connection != null) {
				connection.rollback();
			}
			e.printStackTrace();
			throw e;
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return affectRowCount;
	}

	/**
	 * 通过sql查询数据, 慎用，会有sql注入问题
	 *
	 * @param sql
	 * @return 查询的数据集合
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> query(String sql) throws Exception {
		return executeQuery(sql, null);
	}

	/**
	 * 执行sql通过 Map<String, Object>限定查询条件查询
	 *
	 * @param tableName 表名
	 * @param whereMap  where条件
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> query(String tableName, Map<String, Object> whereMap) throws Exception {
		String whereClause = "";
		Object[] whereArgs = null;
		if (whereMap != null && whereMap.size() > 0) {
			Iterator<String> iterator = whereMap.keySet().iterator();
			whereArgs = new Object[whereMap.size()];
			int i = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				whereClause += (i == 0 ? "" : " AND ");
				whereClause += (key + " = ? ");
				whereArgs[i] = whereMap.get(key);
				i++;
			}
		}
		return query(tableName, false, null, whereClause, whereArgs, null, null, null, null);
	}

	/**
	 * 执行sql条件参数绑定形式的查询
	 *
	 * @param tableName   表名
	 * @param whereClause where条件的sql
	 * @param whereArgs   where条件中占位符中的值
	 * @return List<Map<String, Object>>
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> query(String tableName, String whereClause, String[] whereArgs) throws Exception {
		return query(tableName, false, null, whereClause, whereArgs, null, null, null, null);
	}

	/**
	 * 执行全部结构的sql查询
	 *
	 * @param tableName     表名
	 * @param distinct      去重
	 * @param columns       要查询的列名
	 * @param selection     where条件
	 * @param selectionArgs where条件中占位符中的值
	 * @param groupBy       分组
	 * @param having        筛选
	 * @param orderBy       排序
	 * @param limit         分页
	 * @return List<Map<String, Object>>
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> query(String tableName, boolean distinct, String[] columns, String selection, Object[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws Exception {
		String sql = buildQueryString(distinct, tableName, columns, selection, groupBy, having, orderBy, limit);
		return executeQuery(sql, selectionArgs);

	}

	/**
	 * 执行查询
	 *
	 * @param sql      要执行的sql语句
	 * @param bindArgs 绑定的参数
	 * @return List<Map<String, Object>>结果集对象
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object[] bindArgs) throws Exception {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			/** 获取数据库连接池中的连接 **/
			connection = ConnectionManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(sql);
			if (bindArgs != null) {
				/** 设置sql占位符中的值 **/
				for (int i = 0; i < bindArgs.length; i++) {
					preparedStatement.setObject(i + 1, bindArgs[i]);
				}
			}
			System.out.println(getExecSQL(sql, bindArgs));
			/** 执行sql语句，获取结果集 **/
			resultSet = preparedStatement.executeQuery();
			datas = getDatas(resultSet);
		} catch (Exception e) {
			throw e;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
		return datas;
	}

	/**
	 * 将结果集对象封装成List<Map<String, Object>> 对象
	 *
	 * @param resultSet 结果多想
	 * @return 结果的封装
	 * @throws SQLException
	 */
	private static List<Map<String, Object>> getDatas(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		/** 获取结果集的数据结构对象 **/
		ResultSetMetaData metaData = resultSet.getMetaData();
		while (resultSet.next()) {
			Map<String, Object> rowMap = new HashMap<String, Object>();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
			}
			datas.add(rowMap);
		}
		System.out.println("成功查询到了" + datas.size() + "行数据");
		for (int i = 0; i < datas.size(); i++) {
			Map<String, Object> map = datas.get(i);
			System.out.println("第" + (i + 1) + "行：" + map);
		}
		return datas;
	}

	/**
	 * Build an SQL query string from the given clauses.
	 *
	 * @param distinct true if you want each row to be unique, false otherwise.
	 * @param tables   The table names to compile the query against.
	 * @param columns  A list of which columns to return. Passing null will return
	 *                 all columns, which is discouraged to prevent reading data
	 *                 from storage that isn't going to be used.
	 * @param where    A filter declaring which rows to return, formatted as an SQL
	 *                 WHERE clause (excluding the WHERE itself). Passing null will
	 *                 return all rows for the given URL.
	 * @param groupBy  A filter declaring how to group rows, formatted as an SQL
	 *                 GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *                 will cause the rows to not be grouped.
	 * @param having   A filter declare which row groups to include in the cursor,
	 *                 if row grouping is being used, formatted as an SQL HAVING
	 *                 clause (excluding the HAVING itself). Passing null will cause
	 *                 all row groups to be included, and is required when row
	 *                 grouping is not being used.
	 * @param orderBy  How to order the rows, formatted as an SQL ORDER BY clause
	 *                 (excluding the ORDER BY itself). Passing null will use the
	 *                 default sort order, which may be unordered.
	 * @param limit    Limits the number of rows returned by the query, formatted as
	 *                 LIMIT clause. Passing null denotes no LIMIT clause.
	 * @return the SQL query string
	 */
	private static String buildQueryString(boolean distinct, String tables, String[] columns, String where, String groupBy, String having, String orderBy, String limit) {
		if (isEmpty(groupBy) && !isEmpty(having)) {
			throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
		}
		if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
			throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
		}

		StringBuilder query = new StringBuilder(120);

		query.append("SELECT ");
		if (distinct) {
			query.append("DISTINCT ");
		}
		if (columns != null && columns.length != 0) {
			appendColumns(query, columns);
		} else {
			query.append(" * ");
		}
		query.append("FROM ");
		query.append(tables);
		appendClause(query, " WHERE ", where);
		appendClause(query, " GROUP BY ", groupBy);
		appendClause(query, " HAVING ", having);
		appendClause(query, " ORDER BY ", orderBy);
		appendClause(query, " LIMIT ", limit);
		return query.toString();
	}

	/**
	 * Add the names that are non-null in columns to s, separating them with commas.
	 */
	private static void appendColumns(StringBuilder s, String[] columns) {
		int n = columns.length;

		for (int i = 0; i < n; i++) {
			String column = columns[i];

			if (column != null) {
				if (i > 0) {
					s.append(", ");
				}
				s.append(column);
			}
		}
		s.append(' ');
	}

	/**
	 * addClause
	 *
	 * @param s      the add StringBuilder
	 * @param name   clauseName
	 * @param clause clauseSelection
	 */
	private static void appendClause(StringBuilder s, String name, String clause) {
		if (!isEmpty(clause)) {
			s.append(name);
			s.append(clause);
		}
	}

	/**
	 * Returns true if the string is null or 0-length.
	 *
	 * @param str the string to be examined
	 * @return true if str is null or zero length
	 */
	private static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * the pattern of limit
	 */
	private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

	/**
	 * After the execution of the complete SQL statement, not necessarily the actual
	 * implementation of the SQL statement
	 *
	 * @param sql      SQL statement
	 * @param bindArgs Binding parameters
	 * @return Replace? SQL statement executed after the
	 */
	private static String getExecSQL(String sql, Object[] bindArgs) {
		StringBuilder sb = new StringBuilder(sql);
		if (bindArgs != null && bindArgs.length > 0) {
			int index = 0;
			for (int i = 0; i < bindArgs.length; i++) {
				index = sb.indexOf("?", index);
				sb.replace(index, index + 1, String.valueOf(bindArgs[i]));
			}
		}
		return sb.toString();
	}

	/**
	 * @Description: 获取数据条数
	 * @param sql
	 * @throws Exception
	 * @return: Long
	 */
	public static long getCount(String sql) throws Exception {
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		long count = 0;
		try {
			connection = ConnectionManager.getInstance().getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getLong(1);
			}
			return count;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(null, stmt, connection);
		}
	}

	public static void close(ResultSet rs, Statement stmt, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("数据连接关闭失败！", e);
		}
	}

	/*****************************
	 * JONLLEN
	 ******************************************/
	/**
	 * 
	 */
	private static HashMap<String, SqlParameter[]> parmCache = new HashMap<String, SqlParameter[]>();

	/**
	 * 组装存储过程执行命令语句
	 * 
	 * @param procedureName :存储过程名称
	 * @param length        :参数长度
	 * @return
	 */
	protected static String build(String procedureName, SqlParameter[] params) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(" { call %s(", procedureName));
		int index = 0, length = params.length;
		while (index < length) {
			sb.append((index == length - 1) ? "?" : "?,");
			index++;
		}
		sb.append(")}");
		return sb.toString();
	}

	/**
	 * 准备命令
	 * 
	 * @param commandText
	 * @param params
	 * @return String
	 */
	protected static String prepareCommand(String commandText, SqlParameter[] params) {
		if (params == null || params.length <= 0)
			return commandText;

		for (SqlParameter param : params) {
			SqlDbType type = param.getSqlDbType();
			String value = (param.getValue() != null) ? param.getValue().toString() : "";
			// 未完善
			if (type == SqlDbType.VarChar || type == SqlDbType.Char || type == SqlDbType.Text) {
				int size = param.getSize();
				if (size > 0 && size < value.length())
					value = value.substring(0, size);
				commandText = commandText.replaceAll(param.getParameterName(), "'" + Matcher.quoteReplacement(value) + "'");
			} else if (type == SqlDbType.NVarChar) {
				int size = param.getSize();
				if (size > 0 && size < value.length())
					value = value.substring(0, size);
				commandText = commandText.replaceAll(param.getParameterName(), "N'" + Matcher.quoteReplacement(value) + "'");
			} else {
				commandText = commandText.replaceAll(param.getParameterName(), value);
			}
		}

		return commandText;
	}

	private  static  Connection getConnection() throws ClassNotFoundException, SQLException {
		return ConnectionManager.getInstance().getConnection();
	}

	public static  int execute(String cmdText, CommandType cmdType) throws Exception {
		if (cmdType == CommandType.Text)
			return execute(cmdText);

		return executeProcedure(cmdText); // 未完善

	}

	public static  int execute(String cmdText) throws Exception { 
		Connection conn = null;
		PreparedStatement ps = null;
		try { 
			conn = getConnection(); 
			ps = conn.prepareStatement(cmdText);
			int result = ps.executeUpdate();

			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			close(null, ps, conn);
		}
	}

	public static  int executeProcedure(String procedureName, SqlParameter... params) throws Exception {
		// 组装存储过程命令
		String proName = build(procedureName, params);

		// log.debug(String.format("execute :%s", procedureName));
		Connection conn = null;
		CallableStatement cs = null;
		try {
			conn = getConnection();
			// 创建存储过程的对象
			cs = conn.prepareCall(proName);
			// 为参数设值
			setParameterValue(cs, params);
			// 注册输出参数;
			registerOutParameter(cs, params);
			// 执行存储过程
			boolean result = cs.execute();
			// 设置返回值
			setRegisterOutValue(cs, params);

			return result ? 0 : 1;
		} catch (Exception e) {
			throw e;
		} finally {
			if (cs != null)
				cs.close();
			DBUtil.close(null, null, conn);
		}
	}

	/**
	 * 取得输出参数的值并赋给相应的参数对象
	 * 
	 * @param cs
	 * @param param
	 * @throws SQLException
	 */
	protected static void setRegisterOutValue(CallableStatement cs, SqlParameter[] params) throws SQLException {
		if (cs == null || params == null || params.length <= 0)
			return;
		for (int i = 0, j = params.length; i < j; i++) {
			SqlParameter param = params[i];
			// 取得参数的输入输出类型
			ParameterDirection direction = param.getDirection();
			// 判断是否为输出参数
			if (direction == ParameterDirection.Output || //
					direction == ParameterDirection.InputOutput) {
				String parameterName = param.getParameterName();
				// 取得输出参数的值，并赋给该对参数对象
				try {
					String name = parameterName.substring(1);
					Object o = cs.getObject(name);
					param.setValue(o);
				} catch (Exception e) {
					e.printStackTrace();
					param.setValue(0);
				}
			}
		}
	}

	/**
	 * 注册参数为输出参数
	 * 
	 * @param cs
	 * @param params
	 * @throws SQLException
	 */
	protected static void registerOutParameter(CallableStatement cs, SqlParameter[] params) throws SQLException {
		if (cs == null || params == null || params.length <= 0)
			return;
		// 遍历参数数组
		for (SqlParameter param : params) {
			// 取得参数的输入输出类型
			ParameterDirection direction = param.getDirection();
			// 判断是否为输出参数
			if (direction == ParameterDirection.Output || //
					direction == ParameterDirection.InputOutput) {
				String parameterName = param.getParameterName();
				// 注册参数为输出参数
				cs.registerOutParameter(parameterName.substring(1), param.getSqlDbType().getIndex());
			}
		}
	}

	/**
	 * 给CallableStatement对象赋参数值
	 * 
	 * @param cs     ：CallableStatement对象
	 * @param params ：SqlParameter[]
	 * @throws Exception
	 */
	protected static void setParameterValue(CallableStatement cs, SqlParameter[] params) throws Exception {
		if (cs == null || params == null || params.length <= 0)
			return;
		for (int i = 0, j = params.length; i < j; i++) {
			SqlDbType type = params[i].getSqlDbType();
			String parameterName = params[i].getParameterName().substring(1);
			String v = (params[i].getValue() != null) ? params[i].getValue().toString() : "";
			if (params[i].getDirection() == ParameterDirection.Output) {
				cs.registerOutParameter(parameterName, params[i].getSqlDbType().getIndex());
			} else if (type == SqlDbType.VarChar || type == SqlDbType.Char || type == SqlDbType.NVarChar || type == SqlDbType.Text)
				cs.setString(parameterName, v);
			else if (type == SqlDbType.Int)
				cs.setInt(parameterName, Integer.parseInt(v));
			else if (type == SqlDbType.TinyInt)
				cs.setByte(parameterName, Byte.parseByte(v));
			else if (type == SqlDbType.DateTime)
				cs.setDate(parameterName, Date.valueOf(v));
			else if (type == SqlDbType.BigInt)
				cs.setLong(parameterName, Long.parseLong(v));
			else if (type == SqlDbType.Decimal)
				cs.setDouble(parameterName, Double.parseDouble(v));
			// 未完善
		}
	}

	
	/**
	 * 通过KEY获取到参数数组
	 * 
	 * @param cacheKey
	 *            : KEY String
	 * @return SqlParameter[]
	 */
	public static SqlParameter[] getCacheParameters(String cacheKey) {
		SqlParameter[] cacheParms = parmCache.get(cacheKey);
		if (cacheParms == null)
			return null;
		SqlParameter[] clonedParms = new SqlParameter[cacheParms.length];
		for (int i = 0, j = clonedParms.length; i < j; i++) {
			clonedParms[i] = cacheParms[i].clone();
		}
		return clonedParms;
	}
	/**
	 * 加入缓存中
	 * 
	 * @param cacheKey
	 *            : KEY String
	 * @param params
	 *            ：SqlParameter[]
	 */
	public static void cacheParameters(String cacheKey, SqlParameter[] params) {
		parmCache.put(cacheKey, params);
	}
}
