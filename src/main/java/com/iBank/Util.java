package com.ibank;

import org.bukkit.Bukkit;

import java.util.UUID;

public class Util {
    public static UUID getUniqueId(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }
}
