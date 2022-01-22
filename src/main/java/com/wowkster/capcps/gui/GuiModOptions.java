package com.wowkster.capcps.gui;

import com.wowkster.capcps.CapCPS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

public class GuiModOptions extends GuiScreen {
    public static final GuiSlider.GuiDisplaySupplier displaySupplier = (value) -> {
        if (value == CapCPS.UNLIMITED) return "§aUnlimited";
        if (value == 0) return "§cZERO";
        if (value < 8) return String.format("§6%.1f", value);
        return String.format("§e%.1f", value);
    };

    int previousMaxLeftCps;
    int previousMaxRightCps;

    GuiSlider maxLeftSlider;
    GuiSlider maxRightSlider;

    public GuiModOptions() {
        previousMaxLeftCps = CapCPS.leftCpsMax;
        previousMaxRightCps = CapCPS.rightCpsMax;
    }

    @Override
    public void initGui() {
        super.initGui();

        maxLeftSlider = new GuiSlider(0, "Max Left CPS", this.width / 2 - 155, this.height / 6, CapCPS.leftCpsMax, 0, 16, 1,
                displaySupplier, sliderValue -> CapCPS.leftCpsMax = (int) sliderValue);

        maxRightSlider = new GuiSlider(0, "Max Right CPS", this.width / 2 + 5, this.height / 6, CapCPS.rightCpsMax, 0, 16, 1,
                displaySupplier, sliderValue -> CapCPS.rightCpsMax = (int) sliderValue);

        this.buttonList.add(maxLeftSlider);

        this.buttonList.add(maxRightSlider);

        this.buttonList.add(new GuiActionButton(2, "Randomize Left CPS", this.width / 2 - 155, this.height / 6 + 24, () -> {
            CapCPS.leftCpsMax = genRandom(CapCPS.leftCpsMax, 17);
            maxLeftSlider.setValue(CapCPS.leftCpsMax);
        }));

        this.buttonList.add(new GuiActionButton(2, "Randomize Right CPS", this.width / 2 + 5, this.height / 6 + 24, () -> {
            CapCPS.rightCpsMax = genRandom(CapCPS.rightCpsMax, 17);
            maxRightSlider.setValue(CapCPS.rightCpsMax);
        }));

        this.buttonList.add(new GuiToggle(1, "Show Debug Info", this.width / 2 - 155, this.height / 6 + 48, CapCPS.showDebugHud,
                (state) -> state ? "§aON" : "§cOFF", (state) -> CapCPS.showDebugHud = state));

        this.buttonList.add(new GuiToggle(1, "Show Debug Messages", this.width / 2 + 5, this.height / 6 + 48, CapCPS.showDebugMessages,
                (state) -> state ? "§aON" : "§cOFF", (state) -> CapCPS.showDebugMessages = state));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float _unknown) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "CPS Limiter Settings", this.width / 2, 40, 16777215);
        this.drawString(this.fontRendererObj, "§7Made by Wowkster#0001", this.width - this.fontRendererObj.getStringWidth("§7Made by Wowkster#0001") - 10, this.height - 15, 16777215);
//        this.drawString(this.fontRendererObj, "§c[WARNING] §eThis mod has not been paid for!", 10, this.height - 25, 16777215);
//        this.drawString(this.fontRendererObj, "§7To remove this message contact §bWowkster#0001 §7for a licensed copy.", 10, this.height - 15, 16777215);
        super.drawScreen(mouseX, mouseY, _unknown);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        if (previousMaxLeftCps == CapCPS.leftCpsMax && previousMaxRightCps == CapCPS.rightCpsMax) return;

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
                String.format("§bMaximum CPS Set\n§7Left: %s\n§7Right: %s", displaySupplier.getDisplayString(CapCPS.leftCpsMax), displaySupplier.getDisplayString(CapCPS.rightCpsMax))
        ));
//        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
//                "§c[WARNING] §eThis mod has not been paid for! §7To remove this message contact §bWowkster#0001 §7for a licensed copy."
//        ));
    }

    private static int genRandom(int original, int max) {
        int num = (int) Math.floor(Math.random() * max);

        if (num == original) return genRandom(original, max);
        return  num;
    }
}
