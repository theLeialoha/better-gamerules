If you want to mess with hotswapping, here are a few things I'd recommend you do.

> [!NOTE]
> This is for my IDE (I'm using VS Code, get over it). If you aren't using VS Code, your steps will be slightly different and you're on your own.

  1) Download a compatible server jar:
     - Paper: https://papermc.io/downloads
     - Spigot: https://getbukkit.org/download/spigot
     - Bukkit: https://getbukkit.org/download/craftbukkit
  2) Place the jar inside this folder.
  4) Download a DCEVM-compatible JDK and install it:
     - Java 17/21: https://github.com/JetBrains/JetBrainsRuntime/releases

Once you've downloaded everything:
  
  1) Change the config values in `.vscode/settings.json`:
     - Update `runtime.javaJBR` to the path of DCEVM-compatible JDK.
     - Update `runtime.serverJar` to the name of the server jar
       - If you placed the server jar in a subdirectory within the `run/` folder, you can use that folder instead (ie. `versions/paper-1.20.1-196.jar`).
  2) Start the server using `Tasks: Run Task` > `Start Server`.
  3) Hook into the process, do this by either:
     - a. Either using port 8080
     - b. or under `Run and Debug` use the task `Hook Hotswap`
  4) When saving a file, it's code will propagate into the minecraft server.