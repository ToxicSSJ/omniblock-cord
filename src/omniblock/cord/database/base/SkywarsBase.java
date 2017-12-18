package omniblock.cord.database.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import omniblock.cord.database.sql.make.MakeSQLQuery;
import omniblock.cord.database.sql.make.MakeSQLUpdate;
import omniblock.cord.database.sql.make.MakeSQLUpdate.TableOperation;
import omniblock.cord.database.sql.type.TableType;
import omniblock.cord.database.sql.util.Resolver;
import omniblock.cord.database.sql.util.SQLResultSet;
import omniblock.cord.database.sql.util.VariableUtils;

public class SkywarsBase {
	
	protected static String map_inserter_sql = "INSERT INTO top_maps_skywars (map_name, votes) SELECT * FROM (SELECT VAR_P_SKYWARS_MAP a, VAR_P_SKYWARS_MAP_INITIAL b) AS tmp WHERE NOT EXISTS (SELECT 1 FROM top_maps_skywars WHERE map_name = VAR_P_SKYWARS_MAP );";
	protected static String top_weekprize_pos_sql = "SELECT id, p_id, p_points, FIND_IN_SET( p_points, ( SELECT GROUP_CONCAT( p_points ORDER BY p_points DESC ) FROM skywars_weekprize ) ) AS rank FROM skywars_weekprize WHERE p_id =  'VAR_P_ID'";
	
	public static String getStats(ProxiedPlayer player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.TOP_STATS_SKYWARS)
				.select("kills")
				.select("assists")
				.select("games")
				.select("wins")
				.select("average")
				.where("p_id", Resolver.getNetworkID(player));
		
		try {
			
			SQLResultSet sqr = msq.execute();
			
			if(sqr.next()) {
				
				return StringUtils.join(new Object[] { sqr.get("kills"),
													   sqr.get("assists"),
													   sqr.get("games"),
													   sqr.get("wins"),
													   (double) sqr.get("average") == 0.0 ?
													   "NEW" : sqr.get("average")
													 },
									   ";");
				
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return "0;0;0;0;NEW";
		
	}
	
	public static void setStats(ProxiedPlayer player, String stats) {
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.TOP_STATS_SKYWARS, TableOperation.UPDATE);
		
		msu.rowOperation("p_stats", stats);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static int getKills(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String KILLS_STR = data_array[0];
				
				int kills = Integer.valueOf(KILLS_STR);
				return kills;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getAssistences(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String ASISTENCES_STR = data_array[1];
				
				int assistences = Integer.valueOf(ASISTENCES_STR);
				return assistences;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getPlayedGames(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String PLAYED_GAMES_STR = data_array[2];
				
				int played_games = Integer.valueOf(PLAYED_GAMES_STR);
				return played_games;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getWinnedGames(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String WINNED_GAMES_STR = data_array[3];
				
				int winned_games = Integer.valueOf(WINNED_GAMES_STR);
				return winned_games;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static String getAverage(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String AVERAGE_STR = data_array[4];
				
				return AVERAGE_STR;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "NEW";
	}
	
	public static void setAverage(ProxiedPlayer player, String average) {
		 
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_last_games_average", average);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static String[] getItems(ProxiedPlayer player) {
		return getItems(player.getName());
	}
	
	public static String[] getItems(String player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
				.select("p_items")
				.where("p_id", Resolver.getNetworkIDByName(player));
		
		String[] items = new String[] { "{}" };
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				items = ((String) sqr.get("p_items")).split(",");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public static void addItem(ProxiedPlayer player, String item) {
		
		addItem(player.getName(), item);
		return;
		
	}
	
	public static void addItem(String player, String item) {
		
		List<String> items = new ArrayList<String>();
		List<String> olditems = Arrays.asList(getItems(player));
		
		for(String k : olditems){
			
			if(k.equalsIgnoreCase(item)) return;
			else items.add(k);
			
		}
		
		items.add(item);
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_items", StringUtils.join(items, ";"));
		msu.whereOperation("p_id", Resolver.getNetworkIDByName(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static void removeItem(ProxiedPlayer player, String item) {
		
		removeItem(player.getName(), item);
		return;
		
	}
	
	public static void removeItem(String player, String item) {
		
		List<String> items = new ArrayList<String>();
		List<String> olditems = Arrays.asList(getItems(player));
		
		for(String k : olditems){
			
			if(k.equalsIgnoreCase(item)) continue;
			else items.add(k);
			
		}
		
		if(items.contains(item))
			items.remove(item);
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_items", StringUtils.join(items, ";"));
		msu.whereOperation("p_id", Resolver.getLastNameByNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static double[] getAverages(ProxiedPlayer player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
				.select("p_last_games_average")
				.where("p_id", Resolver.getNetworkID(player));
		
		String averages = "{}";
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				averages = sqr.get("p_last_games_average");
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		if(averages != "{}") {
			if(averages.contains(";")) {
				
				String[] av_array = averages.split(";");
				double[] co_array = new double[] { };
				
				if(av_array.length >= 50) {
					
					int pos_x = 0;
					for(String k : av_array) {
						
						try {
							
							double av = Double.valueOf(k);
							co_array[pos_x] = av;
							pos_x++;
							
						}catch(Exception e) {
							return new double[] { 0.0 };
						}
						
					}
					
				}
				
				if(co_array.length >= 50) {
					return co_array;
				}
				
			}
		}
		
		return new double[] { 0.0 };
	}
	
	public static void setAverages(ProxiedPlayer player, Double[] averages) {
		 
		setStats(player, StringUtils.join(averages, ";"));
		return;
		
	}
	
	public static int getExtraPoints(ProxiedPlayer player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
					.select("p_extra_points")
					.where("p_id", Resolver.getNetworkID(player));
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				return sqr.get("p_extra_points");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return VariableUtils.SKYWARS_INITIAL_EXTRA_POINTS;
	}
	
	public static void setExtraPoints(ProxiedPlayer player, int quantity) {
		 
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_extra_points", quantity);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public enum SelectedItemType {
		
		CAGE,
		KIT,
		EXTRA_INFO,
		BOW_EFFECT,
		DEATH_EFFECT,
		
		;
		
	}
	
	public static class AccountInfo {
		
		private String items = VariableUtils.SKYWARS_INITIAL_ITEMS;
		private String stats = "0;0;0;0;NEW";
		private String selected = VariableUtils.SKYWARS_INITIAL_SELECTED;
		
		private double[] averages = new double[] { 0.0 };
		
		public AccountInfo(String stats, String items, String selected, double[] averages) {
			
			this.items = items;
			
			this.stats = stats;
			this.selected = selected;
			
			this.averages = averages;
			
		}

		public String getStats() {
			return stats;
		}

		public void setStats(String stats) {
			this.stats = stats;
		}

		public double[] getAverages() {
			return averages;
		}

		public void setAverages(double[] averages) {
			this.averages = averages;
		}

		public String getSelected() {
			return selected;
		}

		public void setSelected(String selected) {
			this.selected = selected;
		}

		public String getItems() {
			return items;
		}

		public void setItems(String items) {
			this.items = items;
		}
		
	}
	
}
