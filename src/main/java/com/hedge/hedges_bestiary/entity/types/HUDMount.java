package com.hedge.hedges_bestiary.entity.types;

import com.hedge.hedges_bestiary.HedgesBestiary;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public interface HUDMount {
    ResourceLocation SPRITE = new ResourceLocation(HedgesBestiary.MODID, "textures/misc/mount_hud_overlays.png");

    default void renderHUD(GuiGraphics guiGraphics) {

        int screenWidth = guiGraphics.guiWidth(), screenHeight = guiGraphics.guiHeight();

        Vec2 dimensions = getSpriteDimensions();
        Vec2 UVOffset = getUVOffset();
        int imageWidth = (int) dimensions.x;
        int imageHeight = (int) dimensions.y;

        int x = (screenWidth - imageWidth) / 2;
        int y = screenHeight - 80;

        guiGraphics.blit(SPRITE, x, y, UVOffset.x, UVOffset.y, imageWidth, imageHeight, 256, 512);

        int visibleHeight = (int) (imageHeight * getSpriteHeight());

        int spriteYOffset = imageHeight - visibleHeight;

        guiGraphics.blit(SPRITE, x, y + spriteYOffset, UVOffset.x, spriteYOffset + imageHeight + UVOffset.y, imageWidth, imageHeight, 256, 512);
    }

    Vec2 getUVOffset();
    Vec2 getSpriteDimensions();
    float getSpriteHeight();

}
