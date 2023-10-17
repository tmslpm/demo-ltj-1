package com.jtorleonstudios.bettervillage;

import com.jtorleonstudios.bettervillage.compat.CompatResourcesListener;
import com.jtorleonstudios.bettervillage.compat.ICompatProcess;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;    
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
  public static final String MOD_ID = "bettervillage";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

  //public static final ItemGroup CREATIVE_BAR = FabricItemGroupBuilder.create(new Identifier(MOD_ID, MOD_ID)).icon(() -> new ItemStack(net.minecraft.block.Blocks.CAKE)).build();

  public void onInitialize() {

    ResourceManagerHelper serverData = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
    serverData.registerReloadListener(new CompatResourcesListener());
    ServerWorldEvents.LOAD.register(ICompatProcess::apply);
  }

}