---
name: generate-vanilla-source
description: Generate vanilla Minecraft source code.
---

1. Run genSources
   ```shell
   ./gradlew genSources
   ```
2. Source jars will be generated under following paths:
   - `./.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-common-<hash>/<version>/minecraft-common-<hash>-<version>-sources.jar`
   - `./.gradle/loom-cache/remapped_mods/<mapping_version>/net/fabricmc/fabric-api/<module>/<version>/<module>-<version>-sources.jar`
