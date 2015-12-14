java_import(
  name = "benchmark_jars",
  jars = [
    "lib/bb.jar",
    "lib/mt-13.jar",
    "lib/jsci-core.jar",
  ],
)

java_binary(
  name = "benchmark",
  main_class = "blam.benchmark.Driver",
  srcs = glob(["java/blam/benchmark/*.java"]),
  deps = [
    ":benchmark_jars",
    ":guava",
    ":blam",
  ],
)

java_import(
  name = "guava",
  jars = [
    "lib/guava-19.0.jar",
  ],
)

java_library(
  name = "blam",
  srcs = glob([
    "java/blam/*.java",
    "java/blam/instrumentation/*.java"
  ]),
  deps = [
    ":guava"
  ],
)

java_binary(
  name = "main",
  main_class = "blam.Driver",
  runtime_deps = [
    ":blam",
  ],
)

java_import(
  name = "junit",
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
    ":guava",
    ":junit",
    ":blam",
  ],
)
