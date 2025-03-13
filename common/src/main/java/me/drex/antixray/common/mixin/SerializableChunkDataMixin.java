package me.drex.antixray.common.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import me.drex.antixray.common.util.Arguments;
import me.drex.antixray.common.util.Util;
import net.minecraft.core.IdMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SerializableChunkData.class, priority = 1500)
public abstract class SerializableChunkDataMixin {

    @Inject(
        method = "parse",
        at = @At(
            value = "INVOKE",
            // compoundTag3.getCompound("block_states")
            target = "Lnet/minecraft/nbt/CompoundTag;getCompound(Ljava/lang/String;)Ljava/util/Optional;",
            ordinal = 2
        )
    )
    // Arguments need to be passed to the lambda methods creating the PalettedContainer<BlockState>
    private static void setArguments(
        LevelHeightAccessor levelHeightAccessor, RegistryAccess registryAccess, CompoundTag compoundTag,
        CallbackInfoReturnable<SerializableChunkData> cir, @Local(ordinal = 1) int chunkSectionIndex
    ) {
        // chunkSectionIndex = compoundTag3.getByteOr("Y", (byte)0);
        Arguments.SerializableChunkData_LEVEL_HEIGHT_ACCESSOR.set(levelHeightAccessor);
        Arguments.SerializableChunkData_SECTION_INDEX.set(chunkSectionIndex);
    }

    @Inject(
        method = "parse",
        at = @At(
            value = "INVOKE",
            // compoundTag3.getCompound("biomes")
            target = "Lnet/minecraft/nbt/CompoundTag;getCompound(Ljava/lang/String;)Ljava/util/Optional;",
            ordinal = 3
        )
    )
    private static void removeArguments(
        LevelHeightAccessor levelHeightAccessor, RegistryAccess registryAccess, CompoundTag compoundTag,
        CallbackInfoReturnable<SerializableChunkData> cir
    ) {
        // chunkSectionIndex = compoundTag3.getByteOr("Y", (byte)0);
        Arguments.SerializableChunkData_LEVEL_HEIGHT_ACCESSOR.remove();
        Arguments.SerializableChunkData_SECTION_INDEX.remove();
    }

    @WrapOperation(
        method = "method_68299",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/core/IdMap;Ljava/lang/Object;Lnet/minecraft/world/level/chunk/PalettedContainer$Strategy;)Lnet/minecraft/world/level/chunk/PalettedContainer;"
        )
    )
    private static PalettedContainer<BlockState> setPresetValuesArgument(
        IdMap<BlockState> idMap, Object defaultValue, PalettedContainer.Strategy strategy,
        Operation<PalettedContainer<BlockState>> original
    ) {
        LevelHeightAccessor levelHeightAccessor = Arguments.SerializableChunkData_LEVEL_HEIGHT_ACCESSOR.get();
        Integer sectionIndex = Arguments.SerializableChunkData_SECTION_INDEX.get();
        BlockState[] presetValues = null;
        if (levelHeightAccessor != null && sectionIndex != null) {
            Level level = Util.getLevel(levelHeightAccessor);
            presetValues = Util.getBlockController(level).getPresetBlockStates(level, sectionIndex << 4);
        }

        var previous = Arguments.PRESET_VALUES.get();
        Arguments.PRESET_VALUES.set(presetValues);
        try {
            return original.call(idMap, defaultValue, strategy);
        } finally {
            Arguments.PRESET_VALUES.set(previous);
        }
    }

    @WrapOperation(
        method = "method_68291",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"
        )
    )
    private static DataResult<PalettedContainer<BlockState>> setPresetValuesArgument(
        Codec<PalettedContainer<BlockState>> instance, DynamicOps<Tag> dynamicOps, Object data,
        Operation<DataResult<PalettedContainer<BlockState>>> original
    ) {
        LevelHeightAccessor levelHeightAccessor = Arguments.SerializableChunkData_LEVEL_HEIGHT_ACCESSOR.get();
        Integer sectionIndex = Arguments.SerializableChunkData_SECTION_INDEX.get();
        BlockState[] presetValues = null;
        if (levelHeightAccessor != null && sectionIndex != null) {
            Level level = Util.getLevel(levelHeightAccessor);
            presetValues = Util.getBlockController(level).getPresetBlockStates(level, sectionIndex << 4);
        }

        var previous = Arguments.PRESET_VALUES.get();
        Arguments.PRESET_VALUES.set(presetValues);
        try {
            return original.call(instance, dynamicOps, data);
        } finally {
            Arguments.PRESET_VALUES.set(previous);
        }
    }
}
