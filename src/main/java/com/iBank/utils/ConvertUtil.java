package com.ibank.utils;

import com.ibank.Database.DataSource;
import com.ibank.Database.Database;
import com.ibank.system.Configuration;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConvertUtil {
    public static void convert() throws SQLException {
        Database db = DataSource.getDatabase();
        convertAccounts(db);
        convertLoans(db);
        convertRegions(db);
    }

    private static void convertAccounts(Database db) throws SQLException{
        ResultSet rs = db.query("SELECT * FROM `" + Configuration.Entry.DatabaseAccountsTable.getValue() + "`");
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println("[iBank] Converting account: " + name);

            db.execute("UPDATE `" + Configuration.Entry.DatabaseAccountsTable.getValue() + "` SET `owners`='" + convertUsers(rs.getString("owners")) + "' WHERE `name`='" + name + "'");
            db.execute("UPDATE `" + Configuration.Entry.DatabaseAccountsTable.getValue() + "` SET `users`='" + convertUsers(rs.getString("users")) + "' WHERE `name`='" + name + "'");
        }
    }

    private static void convertLoans(Database db) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM `" + Configuration.Entry.DatabaseLoanTable.getValue() + "`");
        while (rs.next()) {
            int id = rs.getInt("id");
            String user = rs.getString("user");
            String uuid = Bukkit.getOfflinePlayer(user).getUniqueId().toString();
            db.execute("UPDATE `" + Configuration.Entry.DatabaseLoanTable.getValue() + "` SET `user`='" + uuid + "' WHERE `id`=" + id);
        }
    }

    private static void convertRegions(Database db) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM `" + Configuration.Entry.DatabaseRegionTable.getValue() + "`");
        while (rs.next()) {
            String name = rs.getString("name");
            db.execute("UPDATE `" + Configuration.Entry.DatabaseRegionTable.getValue() + "` SET `owners`='" + convertUsers(rs.getString("owners")) + "' WHERE `name`='" + name + "'");
        }
    }

    private static String convertUsers(String oldString) {
        String[] names = oldString.split(",");
        String convertedStr = "";
        for (String s : names) {
            if(s == null ||s.trim().length() < 1|| s.trim().equals("")) {
                continue;
            }

            //Already converted
            if(s.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")){
                if (convertedStr.equals("")) {
                    convertedStr = s;
                    continue;
                }

                convertedStr += "," + s;
                continue;
            }

            String uuid = Bukkit.getOfflinePlayer(s).getUniqueId().toString();
            if (convertedStr.equals("")) {
                convertedStr = uuid;
                continue;
            }

            convertedStr += "," + uuid;
        }
        return convertedStr;
    }
}
