// Assert the report file exists
File reportFile = new File(basedir, "sourcehawk-flattened.yml")
assert !reportFile.isFile()
