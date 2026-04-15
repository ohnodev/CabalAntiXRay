<div align="center">
 <h1>CabalAntiXRay</h1>

 <div>
  <a href="https://github.com/ohnodev/CabalAntiXRay/actions">
   <img alt="Build" src="https://img.shields.io/github/actions/workflow/status/ohnodev/CabalAntiXRay/build.yml?style=flat&logo=github&label=build"/>
  </a>&nbsp;&nbsp;
  <a href="https://modrinth.com">
   <img alt="Modrinth" src="https://img.shields.io/badge/modrinth-upcoming-1bd96a?style=flat&logo=modrinth&logoColor=white"/>
  </a>&nbsp;&nbsp;
  <a href="https://smp.thecabal.app">
   <img alt="Website" src="https://img.shields.io/badge/website-smp.thecabal.app-4caf50?style=flat"/>
  </a>
 </div>
 <br>
</div>

CabalAntiXRay is a Fabric-only, performance-oriented anti-xray fork for Cabal servers.
It starts from DrexHD's AntiXray implementation and keeps the proven packet obfuscation model,
while focusing on operational stability for always-on survival servers.

## Goals

- Keep anti-xray effective without introducing server tick stalls.
- Stay simple to deploy and maintain for Fabric server operators.
- Preserve predictable behavior across snapshot/release updates.
- Prioritize production-safe defaults over complex feature growth.

## Downloads

- GitHub Releases: coming soon
- Modrinth: coming soon

## Requirements

- Java 25+
- Fabric Loader 0.18.4+
- Minecraft 26.1 line (current base branch)

## Configuration

Config file location:

- `server/config/antixray.toml`

Recommended baseline for the overworld:

```toml
[overworld]
enabled = true
engineMode = 3
maxBlockHeight = 256
updateRadius = 2
lavaObscures = false
hiddenBlocks = ["#c:ores", "raw_copper_block", "raw_iron_block", "raw_gold_block", "!#antixray:hidden_only_ores"]
replacementBlocks = ["#antixray:hidden_only_ores", "stone", "deepslate", "andesite", "calcite", "diorite", "dirt", "granite", "gravel", "sand", "tuff", "mossy_cobblestone", "obsidian", "clay", "infested_stone", "amethyst_block", "budding_amethyst", "chest"]
```

Engine mode summary:

- `1`: Most conservative, lowest obfuscation complexity.
- `2`: Broader obfuscation with random hidden block substitution.
- `3`: Recommended default for player experience on slower links.

## Build From Source

```bash
git clone https://github.com/ohnodev/CabalAntiXRay.git
cd CabalAntiXRay
./gradlew :fabric:build
```

Build artifact:

- `fabric/build/libs/antixray-fabric-<version>.jar`

## Scope

- Fabric only.
- Single purpose: chunk/block obfuscation for anti-xray.
- No gameplay rewrites, no protocol translation, no anticheat replacement.

## Upstream Credit

This project is based on and credits:

- [DrexHD/AntiXray](https://github.com/DrexHD/AntiXray)
- Paper Async Anti-Xray patch lineage
