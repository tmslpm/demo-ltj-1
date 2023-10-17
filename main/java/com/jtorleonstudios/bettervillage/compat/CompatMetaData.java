package com.jtorleonstudios.bettervillage.compat;

import com.google.gson.*;
import com.jtorleonstudios.bettervillage.Main;
import com.jtorleonstudios.bettervillage.utils.HelperJson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings({"SameParameterValue", "unused"})
public final class CompatMetaData {
  private static final String CURSE_URL_BETTER_VILLAGES = "";

  private final String modId;
  private final String modName;
  private final boolean enabled;
  private final boolean replaceStructures;
  private final String[] poolPlains;
  private final String[] poolDesert;
  private final String[] poolSnowy;
  private final String[] poolSavanna;
  private final String[] poolTaiga;

  private CompatMetaData(String modId, String modName, boolean enabled, boolean replaceStructures, String[] poolPlains, String[] poolDesert, String[] poolSnowy, String[] poolSavanna, String[] poolTaiga) {
    this.modId = modId;
    this.modName = modName;
    this.enabled = enabled;
    this.replaceStructures = replaceStructures;
    this.poolPlains = poolPlains;
    this.poolDesert = poolDesert;
    this.poolSnowy = poolSnowy;
    this.poolSavanna = poolSavanna;
    this.poolTaiga = poolTaiga;
  }

  public boolean isLoadedModForCompat() {
    return FabricLoader.getInstance().isModLoaded(this.modId);
  }

  public boolean requireApplyCompat() {
    return this.isLoadedModForCompat() && this.enabled;
  }

  public void debugInformationForEndUser() {
    if (this.isLoadedModForCompat()) {
      if (this.enabled)
        Main.LOGGER.info(this.modName + " compatibility is enabled for Better Villages.");
      else
        Main.LOGGER.error(String.format("%s compatibility is not enabled for Better Village and %<s is present in your mod list.%n"
                + "     - Please install the datapack for better compatibility between Better Villages and %<s.%n"
                + "     - You can find the datapack URL in the `compatibility` section of the Better Villages description.%n"
                + "     - URL=%s", this.modName, CURSE_URL_BETTER_VILLAGES
        ));
    }
  }

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  // Deserialize

  @Contract("_, null -> fail; _, !null -> new")
  public static @NotNull CompatMetaData deserialize(Identifier identifier, JsonObject jsonObject) {
    if (jsonObject == null)
      throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + "." +
              " Please undo your last changes in this files. Or reinstall the mods." +
              " If the error continues, please open an issue on Github with all info. Thanks you.");
    return new CompatMetaData(
            HelperJson.getOrThrowString(identifier, jsonObject, "mod_id"),
            HelperJson.getOrThrowString(identifier, jsonObject, "mod_name"),
            HelperJson.getOrThrowBoolean(identifier, jsonObject, "enabled"),
            HelperJson.getOrThrowBoolean(identifier, jsonObject, "replace_structures"),
            HelperJson.getOrThrowStringArray(identifier, jsonObject, "pool_plains"),
            HelperJson.getOrThrowStringArray(identifier, jsonObject, "pool_desert"),
            HelperJson.getOrThrowStringArray(identifier, jsonObject, "pool_snowy"),
            HelperJson.getOrThrowStringArray(identifier, jsonObject, "pool_savanna"),
            HelperJson.getOrThrowStringArray(identifier, jsonObject, "pool_taiga")
    );
  }


  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  // Getter

  public String getModId() {
    return modId;
  }

  public String getModName() {
    return modName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isReplaceStructures() {
    return replaceStructures;
  }

  public String[] getPoolPlains() {
    return poolPlains;
  }

  public String[] getPoolDesert() {
    return poolDesert;
  }

  public String[] getPoolSnowy() {
    return poolSnowy;
  }

  public String[] getPoolSavanna() {
    return poolSavanna;
  }

  public String[] getPoolTaiga() {
    return poolTaiga;
  }

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  // Object


  @Override
  public @NotNull String toString() {
    return "CompatMetaData{" +
            "modId='" + modId + '\'' +
            ", modName='" + modName + '\'' +
            ", enabled=" + enabled +
            ", replaceStructures=" + replaceStructures +
            ", poolPlains=" + Arrays.toString(poolPlains) +
            ", poolDesert=" + Arrays.toString(poolDesert) +
            ", poolSnowy=" + Arrays.toString(poolSnowy) +
            ", poolSavanna=" + Arrays.toString(poolSavanna) +
            ", poolTaiga=" + Arrays.toString(poolTaiga) +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CompatMetaData that = (CompatMetaData) o;
    return enabled == that.enabled && replaceStructures == that.replaceStructures && Objects.equals(modId, that.modId) && Objects.equals(modName, that.modName) && Arrays.equals(poolPlains, that.poolPlains) && Arrays.equals(poolDesert, that.poolDesert) && Arrays.equals(poolSnowy, that.poolSnowy) && Arrays.equals(poolSavanna, that.poolSavanna) && Arrays.equals(poolTaiga, that.poolTaiga);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(modId, modName, enabled, replaceStructures);
    result = 31 * result + Arrays.hashCode(poolPlains);
    result = 31 * result + Arrays.hashCode(poolDesert);
    result = 31 * result + Arrays.hashCode(poolSnowy);
    result = 31 * result + Arrays.hashCode(poolSavanna);
    result = 31 * result + Arrays.hashCode(poolTaiga);
    return result;
  }

}
