java_binary(
  name = "object_size",
  create_executable = 0,
  srcs = [
    "java/blam/instrumentation/ObjectSize.java"
  ],
  deploy_manifest_lines = [
    "Premain-Class: blam.instrumentation.ObjectSize"
  ],
)

java_import(
  name = "jars",
  jars = [
    "lib/guava-19.0.jar",
  ],
)

java_library(
  name = "lib",
  srcs = glob([
    "java/blam/*.java",
    "java/blam/instrumentation/*.java"
  ]),
  deps = [
    ":jars"
  ],
)

java_binary(
  name = "blam",
  main_class = "blam.Driver",
  resources = [":object_size_deploy.jar"],
  jvm_flags = [
    "-javaagent:bazel-bin/object_size_deploy.jar",
  ],
  runtime_deps = [
    ":lib",
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
