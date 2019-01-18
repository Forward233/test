/**
 * 
 */
package com.jdbc;

/**
* @Title: SqlDbType
* @Description: 
* @Version:1.0.0  
* @author jzn
* @date 2019年1月16日
*/
public enum SqlDbType {

	/**
	 *System.Int64。64 位的有符号整数。
	 **/
	BigInt("BigInt", java.sql.Types.BIGINT),

	/**
	 * System.Byte 类型的 System.Array。二进制数据的固定长度流，范围在 1 到 8,000 个字节之间。
	 * 
	 */
	Binary("Binary", java.sql.Types.BINARY),

	/**
	 * System.Boolean。无符号数值，可以是 0、1 或null。 Bit,
	 * 
	 * /** System.String。非 Unicode 字符的固定长度流，范围在 1 到 8,000 个字符之间。
	 */
	Char("Char", java.sql.Types.CHAR),

	/**
	 * System.DateTime。日期和时间数据，值范围从 1753 年 1 月 1 日到 9999 年 12 月 31 日，精度为 3.33
	 * 毫秒。
	 */
	DateTime("DateTime", java.sql.Types.DATE),

	/**
	 * System.Decimal。固定精度和小数位数数值，在 -10 38 -1 和 10 38 -1 之间。
	 */
	Decimal("Decimal", java.sql.Types.DECIMAL),

	/**
	 * System.Double。-1.79E +308 到 1.79E +308 范围内的浮点数。
	 */
	Float("Float", java.sql.Types.FLOAT),

	/**
	 * System.Byte 类型的 System.Array。二进制数据的可变长度流，范围在 0 到 2 31 -1（即 2("BigInt",
	 * 0),147 ,483 ,647）java.sql.Types节之间。 (注:可能不支持)
	 */
	Image("Image", java.sql.Types.BIT),
	/**
	 * System.Int32。32 位的有符号整数。
	 */
	Int("Int", java.sql.Types.INTEGER),
	/**
	 * System.Decimal。货币值，范围在 -2 63（即 -922,337("BigInt", 0),203("BigInt",
	 * java.sql.Types),685,477.5808）到 2 63 -1（即 +922("BigInt",
	 * 0),337,203,685("BigInt", 0),477.5807）之间，精度为千分之十个货币单位。 (注:可能不支持)
	 */
	Money("Money", java.sql.Types.DECIMAL),

	/**
	 * System.String。Unicode 字符的固定长度流，范围在 1 到 4,000 个字符之间。
	 */
	NChar("NChar", java.sql.Types.NCHAR),

	/**
	 * System.String。Unicode 数据的可变长度流，最大长度为 2 30 - 1（即 1("BigInt",
	 * 0),073,741,823）个字符。
	 */
	NText("NText", java.sql.Types.NVARCHAR),

	/**
	 * System.String。Unicode 字符的可变长度流，范围在 1 到 4,000 个字符之间。如果字符串大于 4,000
	 * 个字符，隐式转换会失败。在使用比 4,000 个字符更长的字符串时，请显式设置对象。
	 */
	NVarChar("NVarChar", java.sql.Types.NVARCHAR),

	/**
	 * System.Single。-3.40E +38 到 3.40E +38 范围内的浮点数。
	 */
	Real("Real", java.sql.Types.REAL),

	/**
	 * System.Guid。全局唯一标识符（或 GUID）。
	 */
	UniqueIdentifier("UniqueIdentifier", java.sql.Types.VARCHAR),

	/**
	 * System.DateTime。日期和时间数据，值范围从 1900 年 1 月 1 日到 2079 年 6 月 6 日，精度为 1 分钟。
	 */
	SmallDateTime("SmallDateTime", java.sql.Types.DATE),

	/**
	 * System.Int16。16 位的有符号整数。
	 */
	SmallInt("SmallInt", java.sql.Types.SMALLINT),

	/**
	 * System.Decimal。货币值，范围在 -214,748.3648 到 +214("BigInt", 0),748.3647
	 * 之间，精度为千分之十个货币单位。
	 */
	SmallMoney("SmallMoney", java.sql.Types.DECIMAL),

	/**
	 * System.String。非 Unicode 数据的可变长度流，最大长度为 2 31 -1（即 2("BigInt",
	 * 0),147("BigInt", java.sql.Types),483,647）个字符。
	 */
	Text("Text", java.sql.Types.NVARCHAR),

	/**
	 * System.Byte 类型的 System.Array。自动生成的二进制数，并保证其在数据库中唯一。timestamp
	 * 通常用作对表中各行的版本进行标记的机制。存储大小为 8 字节。
	 */
	Timestamp("Timestamp", java.sql.Types.TIMESTAMP),

	/**
	 * System.Byte。8 位的无符号整数。
	 */
	TinyInt("TinyInt", java.sql.Types.TINYINT),

	/**
	 * System.Byte 类型的 System.Array。二进制数据的可变长度流，范围在 1 到 8("BigInt",
	 * java.sql.Types),000 个字节之间。如果字节数组大于 8,000 个字节，隐式转换会失败。在使用比 8,000
	 * 个字节大的字节数组时，请显式设置对象。
	 */
	VarBinary("VarBinary", java.sql.Types.VARBINARY),

	/**
	 * System.String。非 Unicode 字符的可变长度流，范围在 1 到 8("BigInt", java.sql.Types),000
	 * 个字符之间。
	 */
	VarChar("VarChar", java.sql.Types.VARCHAR),

	/**
	 * System.Object。特殊数据类型，可以包含数值、字符串、二进制或日期数据，以及 SQL Server 值 Empty 和
	 * Null，后两个值在未声明其他类型的情况下采用。
	 */
	// Variant("Variant", java.sql.Types.VAR),

	/**
	 * XML 值。使用 System.Data.SqlClient.SqlDataReader.GetValue(System.Int32) 方法或
	 * System.Data.SqlTypes.SqlXml.Value 属性获取字符串形式的 XML，或通过调用
	 * System.Data.SqlTypes.SqlXml.CreateReader() 方法获取 System.Xml.XmlReader 形式的
	 * XML。
	 */
	Xml("Xml", java.sql.Types.NVARCHAR),

	/**
	 * SQL Server 2005 用户定义的类型 (UDT)。
	 */
	// Udt("Udt", java.sql.Types.),
	/**
	 * 
	 */
	Structured("Structured", java.sql.Types.STRUCT),
	/**
	 * 
	 */
	// Dte("Dte", java.sql.Types.DTE),
	/**
	 * 
	 */
	Time("Time", java.sql.Types.TIME),
	/**
	 * 
	 */
	DateTime2("DateTime2", java.sql.Types.DATE),
	/**
	 * 
	 */
	DateTimeOffset("DateTimeOffset", java.sql.Types.DATE);
	/**
	 * 包名后缀
	 */
	private String name;
	/**
	 * 包名对应的索引，即config.properties中DBType对应值
	 */
	private int index;

	/**
	 * 构造函数
	 * 
	 * @param name
	 * @param index
	 */
	private SqlDbType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public static String getName(int index) {
		for (SqlDbType c : SqlDbType.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		} 
		return null;
	}

	/**
	 *获取包名后缀
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置包名后缀
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取索引值
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 设置索引值
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
}
