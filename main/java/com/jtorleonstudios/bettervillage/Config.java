package com.jtorleonstudios.bettervillage;

import com.jtorleonstudios.libraryferret.conf.Configuration;

public class Config {
    private static Configuration INSTANCE = null;

    private static void init() {
      Config.INSTANCE = new Configuration(Main.MOD_ID);
    }

    public static Configuration get() {
      if (INSTANCE == null)
        init();
      return INSTANCE;
    }
  }