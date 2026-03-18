package me.drex.antixray.fabric;

import me.drex.antixray.common.AntiXray;
import me.drex.antixray.common.util.Platform;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public class AntiXrayFabric extends AntiXray {

    public AntiXrayFabric() {
        super(Platform.FABRIC);
    }

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public String getConfigFileName() {
        return "antixray-fabric.toml";
    }

    @Override
    public boolean hasBypassPermission(ServerPlayer player) {
        return false;
//        return Permissions.check(player, "antixray.bypass");
    }
}
