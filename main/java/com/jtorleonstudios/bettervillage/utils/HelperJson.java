package com.jtorleonstudios.bettervillage.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.jtorleonstudios.bettervillage.Main;
import com.jtorleonstudios.libraryferret.LibraryFerret;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public interface HelperJson {
     Gson GSON = new Gson();

    static Item getOrThrowItem(Identifier identifier, @NotNull JsonObject jsonObject, String memberName) {
        if (!jsonObject.has(memberName))
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", missing field: \"" + memberName + "\"");

        JsonElement tryGetElement = jsonObject.get(memberName);
        if (tryGetElement == null)
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", null field: \"" + memberName + "\"");

        try {
            Identifier itemIdentifier = new Identifier(tryGetElement.getAsString());
            if (!Registry.ITEM.containsId(itemIdentifier)) {
                throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", not found item in registry: \"" + memberName + "\"");
            }
            return Registry.ITEM.get(itemIdentifier);
        } catch (ClassCastException e0) {
            throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected String");
        } catch (IllegalStateException e0) {
            throw new IllegalStateException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\"");
        }
    }

     static String getOrThrowString(Identifier identifier, @NotNull JsonObject jsonObject, String memberName) {
        if (!jsonObject.has(memberName))
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", missing field: \"" + memberName + "\"");

        JsonElement tryGetElement = jsonObject.get(memberName);
        if (tryGetElement == null)
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", null field: \"" + memberName + "\"");

        try {
            return tryGetElement.getAsString();
        } catch (ClassCastException e0) {
            throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected String");
        } catch (IllegalStateException e0) {
            throw new IllegalStateException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\"");
        }
    }

    static int getOrThrowInt(Identifier identifier, @NotNull JsonObject jsonObject, String memberName) {
        if (!jsonObject.has(memberName))
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", missing field: \"" + memberName + "\"");

        JsonElement tryGetElement = jsonObject.get(memberName);
        if (tryGetElement == null)
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", null field: \"" + memberName + "\"");

        try {
            return tryGetElement.getAsInt();
        } catch (ClassCastException e0) {
            throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected number(int)");
        } catch (IllegalStateException e0) {
            throw new IllegalStateException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\"");
        }
    }

    static boolean getOrThrowBoolean(Identifier identifier, @NotNull JsonObject jsonObject, String memberName) {
        if (!jsonObject.has(memberName))
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", missing field: \"" + memberName + "\"");

        JsonElement tryGetElement = jsonObject.get(memberName);
        if (tryGetElement == null)
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", null field: \"" + memberName + "\"");

        try {
            return tryGetElement.getAsBoolean();
        } catch (ClassCastException e0) {
            throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected Boolean");
        } catch (IllegalStateException e0) {
            throw new IllegalStateException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\"");
        }
    }

     static String[] getOrThrowStringArray(Identifier identifier, @NotNull JsonObject jsonObject, String memberName) {
        if (!jsonObject.has(memberName))
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", missing field: \"" + memberName + "\"");

        JsonElement tryGetElement = jsonObject.get(memberName);
        if (tryGetElement == null)
            throw new NullPointerException("A JSON file is invalid file: " + identifier.toString() + ", null field: \"" + memberName + "\"");

        try {
            try {
                return GSON.fromJson(tryGetElement, String[].class);
            } catch (JsonSyntaxException e) {
                Main.LOGGER.error(e);
                throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected String primitive array");
            }
        } catch (ClassCastException e) {
            Main.LOGGER.error(e);
            throw new ClassCastException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\", expected String array");
        } catch (IllegalStateException e) {
            Main.LOGGER.error(e);
            throw new IllegalStateException("A JSON file is invalid file: " + identifier.toString() + ", bad type field: \"" + memberName + "\"");
        }
    }

}
