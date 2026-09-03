package net.lax1dude.eaglercraft.get.modules.misc;

import net.lax1dude.eaglercraft.get.Category;
import net.lax1dude.eaglercraft.get.gui.ChatSearchScreen;
import net.lax1dude.eaglercraft.get.module.Module;
import net.minecraft.client.Minecraft;

public class ChatSearchModule extends Module {

    public ChatSearchModule() {
        super("Chat Search", "Opens a searchable view of your recent chat history", Category.MISC, false, -1);
    }

    @Override
    protected void onEnable() {
        Minecraft.getInstance().displayGuiScreen(new ChatSearchScreen());
        setEnabled(false);
    }
}
