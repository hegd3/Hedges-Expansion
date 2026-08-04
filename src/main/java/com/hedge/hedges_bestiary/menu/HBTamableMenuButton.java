package com.hedge.hedges_bestiary.menu;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.message.EntityKeyMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HBTamableMenuButton extends ImageButton {
    protected final HBTamableMenuScreen screen;
    private final float scale;
    private static final HBOnPress CHANGE_COMMAND = new HBOnPress();
    public HBTamableMenuButton(int pX, int pY, HBTamableMenuScreen screen, Component message, float scale) {
        super(pX, pY, 45, 29, 211, 198, 29, HBTamableMenuScreen.RESOURCE_LOCATION, CHANGE_COMMAND);
        this.screen = screen;
        this.setMessage(message);
        this.scale = scale;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().scale(scale, scale, 1);

        pGuiGraphics.drawCenteredString(this.screen.getMinecraft().font, this.getMessage(),
                (int)((this.getX() + 23) / scale),
                (int)((this.getY() + 5) / scale),
                0xFFFFFF);
    }



    private static class HBOnPress implements ImageButton.OnPress {

        @Override
        public void onPress(Button pButton) {

        }
    }
}
