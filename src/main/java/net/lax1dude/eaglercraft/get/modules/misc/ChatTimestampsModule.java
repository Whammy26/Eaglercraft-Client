package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.module.Module;

/** Prepends a [HH:mm:ss] timestamp to incoming chat lines (patched into NewChatGui). */
public class ChatTimestampsModule extends Module {

    public ChatTimestampsModule() {
        super("Chat Timestamps", "Prefixes chat messages with the time they were received", Category.MISC, false, -1);
    }
}
