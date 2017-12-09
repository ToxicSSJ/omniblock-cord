/*
 *  Omniblock Developers Team - Copyright (C) 2016
 *
 *  This program is not a free software; you cannot redistribute it and/or modify it.
 *
 *  Only this enabled the editing and writing by the members of the team. 
 *  No third party is allowed to modification of the code.
 *
 */
package omniblock.cord.database.sql.util;

import java.util.List;
import java.util.Map;

public class SQLResultSet {

	private List<Map<String, Object>> data;
	private int cursor = -1;

	public SQLResultSet(List<Map<String, Object>> data) {
		this.data = data;
	}

	public boolean next() {
		if (cursor < (data.size() - 1)) {
			cursor++;
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String columnName) {
		Map<String, Object> data = this.data.get(cursor);
		return (T) data.get(columnName);
	}

}
