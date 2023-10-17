package com.jtorleonstudios.bettervillage.compat;

import com.jtorleonstudios.bettervillage.Main;
import com.jtorleonstudios.bettervillage.mixin.StructurePoolAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface ICompatProcess {

  static void apply(MinecraftServer server, @NotNull ServerWorld serverWorld) {
    if (serverWorld.getRegistryKey().equals(World.OVERWORLD)) {
      DynamicRegistryManager manager = server.getRegistryManager();
      Registry<StructurePool> pools = manager.get(Registry.TEMPLATE_POOL_WORLDGEN);

      // Get plain pool
      final StructurePool plainsPool = pools.get(new Identifier("minecraft:village/plains/houses"));
      List<Pair<StructurePoolElement, Integer>> plainsStructurePoolElementCounts = getPoolElementCounts(plainsPool);
      List<StructurePoolElement> plainsStructurePoolElement = getPoolElements(plainsPool);
      // Get desert pool
      final StructurePool desertPool = pools.get(new Identifier("minecraft:village/desert/houses"));
      List<Pair<StructurePoolElement, Integer>> desertStructurePoolElementCounts = getPoolElementCounts(desertPool);
      List<StructurePoolElement> desertStructurePoolElement = getPoolElements(desertPool);
      // Get snowy pool
      final StructurePool snowyPool = pools.get(new Identifier("minecraft:village/snowy/houses"));
      List<Pair<StructurePoolElement, Integer>> snowyStructurePoolElementCounts = getPoolElementCounts(snowyPool);
      List<StructurePoolElement> snowyStructurePoolElement = getPoolElements(snowyPool);
      // Get savanna pool
      final StructurePool savannaPool = pools.get(new Identifier("minecraft:village/savanna/houses"));
      List<Pair<StructurePoolElement, Integer>> savannaStructurePoolElementCounts = getPoolElementCounts(savannaPool);
      List<StructurePoolElement> savannaStructurePoolElement = getPoolElements(savannaPool);
      // Get taiga pool
      final StructurePool taigaPool = pools.get(new Identifier("minecraft:village/taiga/houses"));
      List<Pair<StructurePoolElement, Integer>> taigaStructurePoolElementCounts = getPoolElementCounts(taigaPool);
      List<StructurePoolElement> taigaStructurePoolElement = getPoolElements(taigaPool);

      // Local replace/delete
      CompatResourcesListener.COMPAT_META_DATA_LIST.forEach(v -> {
        if (v.requireApplyCompat()) {
          poolReplacementProcess(plainsPool, plainsStructurePoolElementCounts, plainsStructurePoolElement, v, v.getPoolPlains());
          poolReplacementProcess(desertPool, desertStructurePoolElementCounts, desertStructurePoolElement, v, v.getPoolDesert());
          poolReplacementProcess(savannaPool, savannaStructurePoolElementCounts, savannaStructurePoolElement, v, v.getPoolSavanna());
          poolReplacementProcess(snowyPool, snowyStructurePoolElementCounts, snowyStructurePoolElement, v, v.getPoolSnowy());
          poolReplacementProcess(taigaPool, taigaStructurePoolElementCounts, taigaStructurePoolElement, v, v.getPoolTaiga());
        }
      });

      // Apply change
      setPool(plainsPool, plainsStructurePoolElementCounts, plainsStructurePoolElement);
      setPool(desertPool, desertStructurePoolElementCounts, desertStructurePoolElement);
      setPool(snowyPool, snowyStructurePoolElementCounts, snowyStructurePoolElement);
      setPool(savannaPool, savannaStructurePoolElementCounts, savannaStructurePoolElement);
      setPool(taigaPool, taigaStructurePoolElementCounts, taigaStructurePoolElement);
    }
  }

  static void poolReplacementProcess(StructurePool pool, List<Pair<StructurePoolElement, Integer>> elementCounts, List<StructurePoolElement> element, CompatMetaData compatMetaData, String[] sourcePoolCompat) {
    if (pool != null && !elementCounts.isEmpty()) {
      for (String structurePathId : sourcePoolCompat) {
        int countRemovedStructuredWeight = 0;
        // (1) delete structure
        elementCounts.removeIf(currentPair -> currentPair.getFirst().toString().contains(structurePathId));
        Iterator<StructurePoolElement> iteratorElement = element.iterator();
        while (iteratorElement.hasNext()) {
          final StructurePoolElement currentElement = iteratorElement.next();
          if (currentElement.toString().contains(structurePathId)) {
            iteratorElement.remove();
            countRemovedStructuredWeight++; // increment counter for calculate the weight
          }
        }

        // (2) add replacement if required
        if (compatMetaData.isReplaceStructures() && countRemovedStructuredWeight > 0) {
          SinglePoolElement piece = StructurePoolElement.method_30426(Main.MOD_ID + "_" + structurePathId, StructureProcessorLists.EMPTY).apply(StructurePool.Projection.RIGID);
          for (int i = 0; i < countRemovedStructuredWeight; i++)
            element.add(piece);
          elementCounts.add(new Pair<>(piece, countRemovedStructuredWeight));

          Main.LOGGER.info(String.format("Replaced structure of %s mod for compatibility between Better Villages and %<s."
                  + " (Structure weight defined by the mod %<s is set to %d,"
                  + " you can change the weight in the configuration of the concerned mod)", compatMetaData.getModName(), countRemovedStructuredWeight));
        }
        // (2.1) don't replace just debug
        else {
          Main.LOGGER.info(String.format("Deleted structure of %s mod for compatibility between Better Villages and %<s."
                  + " (Better Villages include the structures by default)", compatMetaData.getModName()));
        }
      }
    }
  }

  static void setPool(StructurePool pool, List<Pair<StructurePoolElement, Integer>> poolElementCounts, List<StructurePoolElement> poolElement) {
    if (pool != null && !poolElementCounts.isEmpty() && !poolElement.isEmpty()) {
      ((StructurePoolAccessor) pool).setElements(poolElement);
      ((StructurePoolAccessor) pool).setElementCounts(poolElementCounts);
    }
    // T.ODO DEBUG DEV
    // poolElement.forEach(System.out::println);
  }

  static List<StructurePoolElement> getPoolElements(StructurePool pool) {
    return pool == null ? new ArrayList<>() : ((StructurePoolAccessor) pool).getElements();
  }

  @NotNull
  static ArrayList<Pair<StructurePoolElement, Integer>> getPoolElementCounts(StructurePool pool) {
    return pool == null ? new ArrayList<>() : new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());
  }

}
