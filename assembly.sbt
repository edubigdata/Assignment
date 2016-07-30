// https://github.com/sbt/sbt-assembly#-provided-configuration
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

runMain in Compile <<= Defaults.runMainTask(fullClasspath in Compile, runner in (Compile, run))

//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.2")
