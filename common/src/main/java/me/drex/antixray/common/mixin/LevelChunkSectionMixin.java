package me.drex.antixray.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.drex.antixray.common.util.Arguments;
import me.drex.antixray.common.util.Util;
import me.drex.antixray.common.util.controller.ChunkPacketBlockController;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunkSection.class)
public abstract class LevelChunkSectionMixin {
    @WrapOperation(
        method = "<init>(Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/PalettedContainerFactory;createForBlockStates()Lnet/minecraft/world/level/chunk/PalettedContainer;"
        )
    )
    public PalettedContainer<BlockState> setPresetValuesArgument(PalettedContainerFactory instance, Operation<PalettedContainer<BlockState>> original) {
        if (((Object) this).getClass() != LevelChunkSection.class) {
            // Compatibility with Flywheel's VirtualChunkSection
            return original.call(instance);
        }
        // custom arguments
        ChunkAccess chunkAccess = Arguments.CHUNK_ACCESS.get();
        Integer chunkSectionIndex = Arguments.CHUNK_SECTION_INDEX.get();

        Level level = Util.getLevel(chunkAccess.levelHeightAccessor);
        ChunkPacketBlockController controller = Util.getBlockController(level);
        if (controller != null) {
            final BlockState[] presetValues = controller.getPresetBlockStates(level, chunkSectionIndex << 4);
            var previous = Arguments.PRESET_VALUES.get();
            Arguments.PRESET_VALUES.set(presetValues);
            try {
                return original.call(instance);
            } finally {
                Arguments.PRESET_VALUES.set(previous);
            }
        }
        return original.call(instance);
    }

    @WrapOperation(
        method = "<init>(Lnet/minecraft/world/level/chunk/PalettedContainerFactory;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/PalettedContainerFactory;createForBiomes()Lnet/minecraft/world/level/chunk/PalettedContainer;"
        )
    )
    public PalettedContainer<Holder<Biome>> setPacketInfoArgumentNull(PalettedContainerFactory instance, Operation<PalettedContainer<Holder<Biome>>> original) {
        var previous = Arguments.PACKET_INFO.get();
        Arguments.PACKET_INFO.remove();
        try {
            return original.call(instance);
        } finally {
            Arguments.PACKET_INFO.set(previous);
        }
    }

    @WrapOperation(
        method = "write",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/chunk/PalettedContainerRO;write(Lnet/minecraft/network/FriendlyByteBuf;)V"
        )
    )
    public void setPacketInfoArgumentNull(PalettedContainerRO<Holder<Biome>> instance, FriendlyByteBuf buf, Operation<Void> original) {
        var previous = Arguments.PACKET_INFO.get();
        Arguments.PACKET_INFO.remove();
        try {
            original.call(instance, buf);
        } finally {
            Arguments.PACKET_INFO.set(previous);
        }
    }
}
