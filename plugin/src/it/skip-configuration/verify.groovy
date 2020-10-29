
// Assert the report file does not exist
File reportFile = new File(basedir, "target/sourcehawk/report.txt")
assert !reportFile.exists()