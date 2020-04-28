package net.slimevoid.dynamictransport.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.slimevoid.dynamictransport.entities.ElevatorEntity;

import java.util.Random;

import static net.minecraft.client.renderer.RenderType.getBlockRenderTypes;
import static net.slimevoid.dynamictransport.core.RegistryHandler.ELEVATOR_BLOCK;
import static net.slimevoid.dynamictransport.tileentity.CamoTileEntity.CAMO_STATE;

@OnlyIn(Dist.CLIENT)
public class ElevatorRenderer extends EntityRenderer<ElevatorEntity> {
    public ElevatorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }


    @Override

    public void render(ElevatorEntity entity, float f1, float f2, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i1) {
        World world = entity.getWorldObj();

        matrixStack.push();
        BlockPos blockpos = new BlockPos(
                entity.getPosX(), //get x
                entity.getBoundingBox().maxY,
                entity.getPosZ() //get z
        );
        BlockState blockstate = ELEVATOR_BLOCK.get().getDefaultState();
        matrixStack.translate(-0.5D, 0.0D, -0.5D); //translate
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        for (net.minecraft.client.renderer.RenderType type : getBlockRenderTypes()) { //for each render type
            if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
                net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(
                        world,
                        blockrendererdispatcher.getModelForState(blockstate),
                        blockstate,
                        blockpos,
                        matrixStack,
                        renderTypeBuffer.getBuffer(type),
                        false,
                        new Random(),
                        blockstate.getPositionRandom(entity.getOrigin()),
                        OverlayTexture.NO_OVERLAY,
                        new ModelDataMap.Builder().withInitial(CAMO_STATE, entity.getClientBlockStates()).build()
                );
            }
        }
        net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
        matrixStack.pop();
        super.render(entity, f1, f2, matrixStack, renderTypeBuffer, i1);
    }

    public ResourceLocation getEntityTexture(ElevatorEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
