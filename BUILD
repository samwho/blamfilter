java_import(
  name = "jars",
  jars = [
    "lib/guava-19.0.jar",
  ],
)

java_library(
  name = "lib",
  srcs = glob(["java/blam/*.java"]),
  deps = [
    ":jars"
  ],
)

java_binary(
  name = "blam",
  main_class = "blam.Driver",
  runtime_deps = [
    ":lib"
  ],
)

java_import(
  name = "test_jars",
  jars = [
    "lib/hamcrest-all-1.3.jar",
    "lib/junit-4.12.jar",
  ],
)

java_test(
  name = "all_tests",
  srcs = glob(["javatests/blam/*.java"]),
  size = "small",
  deps = [
    ":lib",
    ":jars",
    ":test_jars"
  ],
)
