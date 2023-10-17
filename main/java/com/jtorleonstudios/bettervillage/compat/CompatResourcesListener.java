package com.jtorleonstudios.bettervillage.compat;

import com.google.gson.JsonObject;
import com.jtorleonstudios.bettervillage.Main;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CompatResourcesListener implements SimpleSynchronousResourceReloadListener {
  public static final List<CompatMetaData> COMPAT_META_DATA_LIST = new ArrayList<>();

  @Override
  public Identifier getFabricId() {
    return new Identifier(Main.MOD_ID, "bettervillage_compat_listener");
  }

  @Override
  public void reload(ResourceManager manager) {
    COMPAT_META_DATA_LIST.clear();
    for (Identifier id : manager.findResources("bettervillage_compat", path -> path.endsWith(".json"))) {
      try (InputStream stream = manager.getResource(id).getInputStream()) {
        JsonObject result = JsonHelper.deserialize(new InputStreamReader(stream));
        CompatMetaData compat = CompatMetaData.deserialize(id, result);
        compat.debugInformationForEndUser();
        COMPAT_META_DATA_LIST.add(compat);
      } catch (Exception e) {
        Main.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
      }
    }
  }

}
