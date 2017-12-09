/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */
package omniblock.cord.database.sql.make;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import omniblock.cord.database.Database;
import omniblock.cord.database.sql.type.OrderByType;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.SQLResultSet;

public class MakeSQLQuery {

	private String tablename;
	private Set<String> columnsToSelect = Sets.newHashSet();
	private Map<String, String> where = Maps.newLinkedHashMap();
	private String orderByColumn = null;
	private OrderByType order = null;
	private int limitStart = 0;
	private int limitEnd = 0;
	private String absoluteSQL = null; // Si se define, entonces se ignorarán
										// las funciones select, where, limit,
										// etc.

	public MakeSQLQuery(TableType table) {
		this.tablename = table.getTableName();
	}

	public MakeSQLQuery(String tablename) {
		this.tablename = tablename;
	}

	public MakeSQLQuery select(String... columnsNames) {

		for (String columnName : columnsNames) {
			columnsToSelect.add(columnName);
		}

		return this;
	}

	public MakeSQLQuery where(String columnName, String condition) {
		where.put(columnName, condition);

		return this;
	}

	public MakeSQLQuery orderBy(String columnName, OrderByType order) {
		this.orderByColumn = columnName;
		this.order = order;

		return this;
	}

	public MakeSQLQuery limit(int start, int end) {
		this.limitStart = start;
		this.limitEnd = end;

		return this;
	}

	public SQLResultSet execute() throws SQLException {
		String executeSQL;

		if (absoluteSQL == null) {
			StringBuilder sql = new StringBuilder("SELECT ");

			if (columnsToSelect.isEmpty()) {
				sql.append("*");
			} else {
				Iterator<String> iterator = columnsToSelect.iterator();
				while (iterator.hasNext()) {
					sql.append(iterator.next());
					if (iterator.hasNext()) {
						sql.append(",");
					}
				}
			}

			sql.append(" FROM " + tablename);

			if (!where.isEmpty()) {
				sql.append(" WHERE ");
				Iterator<Entry<String, String>> iterator = where.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					sql.append(entry.getKey() + " = '" + entry.getValue() + "'");
					if (iterator.hasNext()) {
						sql.append(" AND ");
					}
				}
			}

			if (limitStart != 0 || limitEnd != 0) {
				sql.append(" LIMIT " + limitStart + ", " + limitEnd);
			}

			if (orderByColumn != null) {
				sql.append(" ORDER BY " + orderByColumn + " " + order.toString());
			}

			executeSQL = sql.toString();
		} else {
			executeSQL = absoluteSQL;
		}

		Statement stm = null;
		
		try {
			
			stm = Database.getConnection().createStatement();
			ResultSet results = stm.executeQuery(executeSQL);
			ResultSetMetaData metadata = results.getMetaData();

			Set<String> columns = Sets.newHashSet();
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				columns.add(metadata.getColumnName(i));
			}

			List<Map<String, Object>> data = Lists.newArrayList();

			while (results.next()) {
				Map<String, Object> entryData = Maps.newHashMap();
				for (String columnName : columns) {
					entryData.put(columnName, results.getObject(columnName));
				}

				data.add(entryData);
			}

			return new SQLResultSet(data);
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public static MakeSQLQuery fromSql(String sql, TableType table) {
		MakeSQLQuery instance = new MakeSQLQuery(table);
		instance.absoluteSQL = sql;
		return instance;
	}

	public static MakeSQLQuery fromSql(String sql, String tableName) {
		MakeSQLQuery instance = new MakeSQLQuery(tableName);
		instance.absoluteSQL = sql;
		return instance;
	}
}
