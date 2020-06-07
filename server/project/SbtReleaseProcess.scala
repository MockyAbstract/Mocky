object SbtReleaseProcess {

  import sbt._
  import sbtrelease.ReleasePlugin.autoImport._
  import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
  import sbtrelease.Git
  import sbtrelease.Utilities._

  private def getGITreference(st: State): Git = st.extract.get(releaseVcs).get.asInstanceOf[Git]

  /**
    * Merge the `devBranch` into the `deployBranch`.
    * Usually, merge the develop branch into master
    */
  private def mergeReleaseVersion(devBranch: String, deployBranch: String): (State) => State = { st: State =>
    val git = getGITreference(st)
    st.log.info(s"Merging $devBranch in $deployBranch")
    git.cmd("checkout", deployBranch) ! st.log
    git.cmd("pull") ! st.log
    git.cmd("merge", devBranch, "--no-edit") ! st.log
    git.cmd("push", "origin", s"$deployBranch:$deployBranch") ! st.log
    git.cmd("checkout", devBranch) ! st.log
    st.log.info("Merge complete with success")
    st
  }

  /**
    * Check if the repository is currently on the required branch
    */
  private def checkCurrentBranch(requiredBranch: String): (State) => State = { st: State =>
    val git = getGITreference(st)
    val currentBranch = (git.cmd("rev-parse", "--abbrev-ref", "HEAD") !!).trim
    if (currentBranch != requiredBranch)
      sys.error(
        s"You must be on the $requiredBranch branch to release a version. Actually you're on the $currentBranch")
    st
  }

  val steps = Seq[ReleaseStep](
    checkCurrentBranch("develop"),
    inquireVersions,
    setReleaseVersion,
    commitReleaseVersion,
    mergeReleaseVersion("develop", "master"),
    tagRelease,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )

}
