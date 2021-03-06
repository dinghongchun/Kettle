package com.ukettle.basics.page.dialect.db;

import java.util.List;

import com.ukettle.basics.page.Sorting;
import com.ukettle.basics.page.dialect.Dialect;
import com.ukettle.basics.page.plugin.BaseParameter;


/**
 * Mysql方言的实现
 * 
 * @author Kimi Liu
 * @Date Aug 16, 2014
 * @Time 11:12:31
 * @email 839536@QQ.com
 * @version 1.0
 * @since JDK 1.6
 */
public class MySQLDialect implements Dialect {

	public boolean limit() {
		return true;
	}

	@Override
	public String getLimit(String sql, int offset, int limit) {
		return getLimit(sql, offset, Integer.toString(offset),
				Integer.toString(limit));
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
	 * 
	 * <pre>
	 * 如mysql
	 * dialect.getLimit("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 * 
	 * @param sql
	 *            实际SQL语句
	 * @param offset
	 *            分页开始纪录条数
	 * @param offsetPlaceholder
	 *            分页开始纪录条数－占位符号
	 * @param limitPlaceholder
	 *            分页纪录条数占位符号
	 * @return 包含占位符的分页sql
	 */
	public String getLimit(String sql, int offset, String offsetPlaceholder,
			String limitPlaceholder) {
		StringBuilder stringBuilder = new StringBuilder(sql);
		stringBuilder.append(" limit ");
		if (offset > 0) {
			stringBuilder.append(offsetPlaceholder).append(",")
					.append(limitPlaceholder);
		} else {
			stringBuilder.append(limitPlaceholder);
		}
		return stringBuilder.toString();
	}

	@Override
	public String getCount(String sql) {
		return "select count(1) from (" + BaseParameter.removeOrders(sql)
				+ ") as dialect";
	}

	@Override
	public String getSort(String sql, List<Sorting> sort) {
		if (sort == null || sort.isEmpty()) {
			return sql;
		}
		StringBuffer buffer = new StringBuffer("select * from (").append(sql)
				.append(") dialect order by ");
		for (Sorting s : sort) {
			buffer.append(s.getColumn()).append(" ").append(s.getSort())
					.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		return buffer.toString();
	}

}