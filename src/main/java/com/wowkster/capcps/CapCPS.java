package com.wowkster.capcps;


import com.wowkster.capcps.gui.GuiModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = CapCPS.MODID, name = CapCPS.MOD_NAME, version = CapCPS.VERSION, acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class CapCPS {
    public static final String MODID = "capcps";
    public static final String MOD_NAME = "CapCPS";
    public static final String VERSION = "0.0.1";

    public static List<KeyBinding> keyBindings;

    public static final int UNLIMITED = 16;

    public static int leftCpsMax = UNLIMITED;
    public static int rightCpsMax = UNLIMITED;

    public static boolean showDebugHud = true;
    public static boolean showDebugMessages = false;

    ClickCache leftMB = new ClickCache();
    ClickCache rightMB = new ClickCache();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        keyBindings = new ArrayList<>();

        keyBindings.add(new KeyBinding("key.menu.desc", Keyboard.KEY_RSHIFT, "key.capcps.category"));

        for (KeyBinding key : keyBindings)
            ClientRegistry.registerKeyBinding(key);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (event.button == -1 || !event.buttonstate) return;

        if (event.button == 0) {
            if (leftCpsMax == UNLIMITED || leftMB.getCPS() < leftCpsMax) {
                leftMB.increment();
                return;
            }

            event.setCanceled(true);
            if (!showDebugMessages) return;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§cBlocked Left Mouse Button Click!"));
        }
        else if (event.button == 1) {
            if (rightCpsMax == UNLIMITED || rightMB.getCPS() < rightCpsMax) {
                rightMB.increment();
                return;
            }

            event.setCanceled(true);
            if (!showDebugMessages) return;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§cBlocked Right Mouse Button Click!"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onTick(RenderGameOverlayEvent event) {

        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) return;

        if (!showDebugHud) return;
        font.drawStringWithShadow("§bCPS Limiter Mod", 10, 10, 0xffffff);
        font.drawStringWithShadow("§7LMB CPS: §e" + leftMB.getCPS(), 10, 20, 0xffffff);
        font.drawStringWithShadow("§7RMB CPS: §e" + rightMB.getCPS(), 10, 30, 0xffffff);
        font.drawStringWithShadow("§7Max Left CPS: " + GuiModOptions.displaySupplier.getDisplayString(leftCpsMax), 10, 40, 0xffffff);
        font.drawStringWithShadow("§7Max Right CPS: " + GuiModOptions.displaySupplier.getDisplayString(rightCpsMax), 10, 50, 0xffffff);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        if (keyBindings.get(0).isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new GuiModOptions());
    }
}