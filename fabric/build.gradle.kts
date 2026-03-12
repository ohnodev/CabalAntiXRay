plugins {
    id("fabric-loom")
    id("multiloader-loader")
}

version = "fabric-${project.property("mod_version")}+${project.property("minecraft_version")}"

base {
    archivesName = "${project.property("archives_base_name")}"
}

repositories {
    maven ("https://jitpack.io") // ImmersivePortalsMod
    maven ("https://maven.nucleoid.xyz/")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())


    implementation(include("com.moandjiezana.toml:toml4j:${project.property("toml_version")}")!!)
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")

    modCompileOnly("com.github.iPortalTeam:ImmersivePortalsMod:${project.property("fabric_imm_ptl_core_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
        isTransitive = false
    }
    modImplementation(include("me.lucko:fabric-permissions-api:${project.property("fabric_permission_api_version")}")!!)
}

loom {
    accessWidenerPath.set(project(":common").file("src/main/resources/antixray.accesswidener"))

    runConfigs.all {
        ideConfigGenerated(true)
    }
}

publishMods {
    file.set(tasks.remapJar.get().archiveFile)

    displayName.set("AntiXray ${version.get()}")
    modLoaders.addAll("fabric", "quilt")

    curseforge {
        projectId.set("511697")
    }
}