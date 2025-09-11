So you want to use CraftBukkit or known as NMS (net.minecraft.server). Head over to SpigotMC and download the BuildTools @ https://hub.spigotmc.org/jenkins/job/BuildTools/.

> [!NOTE]
> Please note, messing with the NMS package directly can be dangerous.
> This can cause irreversible damage to you world, player data or system. Be cautious on \*what\* you do.
> World backups should ALWAYS be a priority.

Create a folder for this tool, it can get messy quick.

Once you've download it, you will want to package up the CraftBukkit code. To do do you will want to run `java -jar BuildTools.java --compile craftbukkit --rev <version>`. Replace `<version>` with either `latest` or the version identifer (ie. `1.20.1`).

Wait for the BuildTools to complete and move the `craftbukkit-1.XX.Y-R0.1-SNAPSHOT.jar` into this folder (`/libs`). Uncomment the dependency in `pom.xml`, change the filename and version to match the craftbukkit jar in this folder.

Now, reload your IDE and you should see the NMS package when developing. Good luck.
