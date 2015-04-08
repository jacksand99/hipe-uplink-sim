# Git Configuration #

Hipe, with the UPPS plugin installed can have one local directory configured as connected to a GIT repositoty.


# Git account configuration #

In hipe, go to Edit Preferences and select Git in the tree.

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/GitConfigurationPanel.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/GitConfigurationPanel.png)

Configure the GIT account.
  * Local Path: Path to a directory in your local computer. If you have not connected to GIT in this directory before, the directory should not exist.
  * Remote Path: Path to the GIT repository. It should be of the type your\_user@git\_host:/home/git\_repo/repo.git
  * Password: The password used to connect to the repository

# Git operations #

Once the configuration is done you can, from the menu Tools perform a Git Clone, Git Push or Git Pull.

![https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/GitMenu.png](https://hipe-uplink-sim.googlecode.com/git/hipe-uplink-sim/vega/doc/images/GitMenu.png)

From the hipe console line you can operate with git with the following commands
```

HipeGit.getInstance().gitPull()
HipeGit.getInstance().gitClone()
HipeGit.getInstance().gitCommit("Git commit message")
HipeGit.getInstance().gitPush()
HipeGit.getInstance().gitClone()
HipeGit.getInstance().gitTrackMaster()
HipeGit.getInstance().add("directory/newFile.txt")
```


