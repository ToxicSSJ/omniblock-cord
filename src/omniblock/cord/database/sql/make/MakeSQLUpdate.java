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

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import omniblock.cord.database.Database;
import omniblock.cord.database.sql.type.TableType;

public class MakeSQLUpdate {

	private TableType table;
	private TableOperation operation;
	private HashMap<String, String> rows = new HashMap<String, String>();
	private HashMap<String, String> where = new HashMap<String, String>();

	public MakeSQLUpdate(TableType table, TableOperation operation) {
		this.table = table;
		this.operation = operation;
	}

	public MakeSQLUpdate rowOperation(String row, Object value) {
		rows.put(row, value.toString());
		return this;
	}

	public MakeSQLUpdate whereOperation(String row, Object value) {
		where.put(row, value.toString());
		return this;
	}

	public void execute() throws SQLException, IllegalArgumentException {
		if (operation == TableOperation.INSERT || operation == TableOperation.UPDATE) {
			if (rows.isEmpty()) {
				throw new IllegalArgumentException(
						"Una sentencia debe tener al menos una instruccion para realizar la operacion.");
			}
		}
		if (operation == TableOperation.UPDATE) {
			if (where.isEmpty()) {
				throw new IllegalArgumentException(
						"La operacion UPDATE debe tener al menos una instruccion del tipo WHERE");
			}
		}

		if (operation == TableOperation.INSERT) {
			String prepareSQL = "INSERT INTO " + table.getTableName() + " ";
			String rowPart = "(";
			String valuePart = "(";

			Iterator<Entry<String, String>> iterator = rows.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if (iterator.hasNext()) {
					rowPart += entry.getKey() + ", ";
					valuePart += "'" + entry.getValue() + "', ";
				} else {
					rowPart += entry.getKey() + ")";
					valuePart += "'" + entry.getValue() + "')";
				}
			}

			prepareSQL += rowPart + " VALUES " + valuePart;

			Statement stm = Database.getConnection().createStatement();
			stm.executeUpdate(prepareSQL);
			stm.close();
		} else if (operation == TableOperation.UPDATE) {
			String prepareSQL = "UPDATE " + table.getTableName() + " SET ";

			Iterator<Entry<String, String>> iterator = rows.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if (iterator.hasNext()) {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "', ";
				} else {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "'";
				}
			}

			prepareSQL += " WHERE ";

			iterator = where.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if (iterator.hasNext()) {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "' AND ";
				} else {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "'";
				}
			}

			Statement stm = Database.getConnection().createStatement();
			stm.executeUpdate(prepareSQL);
			stm.close();
		} else if (operation == TableOperation.DELETE) {
			
			String prepareSQL = "DELETE FROM " + table.getTableName() + " WHERE ";

			Iterator<Entry<String, String>> iterator = where.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				if (iterator.hasNext()) {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "' AND ";
				} else {
					prepareSQL += entry.getKey() + " = '" + entry.getValue() + "'";
				}
			}

			Statement stm = Database.getConnection().createStatement();
			stm.executeUpdate(prepareSQL);
			stm.close();
		}
	}

	public enum TableOperation {
		INSERT, UPDATE, DELETE;
	}
}
