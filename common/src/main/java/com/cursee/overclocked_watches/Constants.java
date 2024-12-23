package com.cursee.overclocked_watches;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;

public class Constants {

	public static final String MOD_ID = "overclocked_watches";
	public static final String MOD_NAME = "Overclocked Watches";
	public static final String MOD_VERSION = "1.0.0";
	public static final String MC_VERSION_RAW = "[1.20.1]";
	public static final Pair<String, String> PUBLISHER_AUTHOR = new Pair<String, String>("Lupin", "Jason13");
	public static final Triplet<String, String, String> PRIMARY_CURSEFORGE_MODRINTH = new Triplet<String, String, String>("https://www.curseforge.com/members/lupin/projects", "https://www.curseforge.com/members/lupin/projects", "https://modrinth.com/user/Lupin/mods");
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final String KEY_CATEGORY_DAY_NIGHT = "key.category.overclocked_watches.day_night";
	public static final String KEY_DAY_NIGHT = "key.overclocked_watches.day_night";

	// four minutes
	public static final int AGE_PROGRESSION_NETHERITE = 60 * 4;

	// thirty seconds
	public static final int AGE_PROGRESSION_DIAMOND = 30;

	// 5 seconds
	public static final int AGE_PROGRESSION_GOLD = 5;
}
