package omniblock.cord.database.sql.make;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omniblock.cord.database.Database;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.SQLResultSet;

public class MakeAdvancedSQLQuery {

	private List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

	private StringBuffer buffer = new StringBuffer();
	private TableType table;

	public MakeAdvancedSQLQuery(TableType table) {
		this.table = table;
	}

	public MakeAdvancedSQLQuery append(String SQL) {

		buffer.append(SQL);
		return this;

	}

	public SQLResultSet execute() throws SQLException {

		Statement stm = Database.getConnection().createStatement();

		ResultSet res = stm.executeQuery(buffer.toString());

		HashMap<Integer, String> columns = new HashMap<Integer, String>();
		ResultSetMetaData rsmd = res.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int i = 1; i <= columnCount; i++) {
			String name = rsmd.getColumnName(i);
			columns.put(i, name);
		}

		results.clear();

		while (res.next()) {
			HashMap<String, Object> armResult = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				armResult.put(columns.get(i), res.getObject(i));
			}
			results.add(armResult);
		}

		stm.close();

		return new SQLResultSet(results);

	}

	public TableType getTable() {
		return table;
	}

}
