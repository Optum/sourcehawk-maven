import org.apache.commons.io.IOUtils

import java.nio.charset.Charset

// Assert the report file exists
File buildLog = new File(basedir, "build.log")
assert buildLog.isFile()

InputStream buildStream = new FileInputStream(buildLog)
String buildContent = IOUtils.toString(buildStream, Charset.defaultCharset())
assert buildContent.contains("BUILD SUCCESS")

// Assert the report file exists
File reportFile = new File(basedir, "target/sourcehawk/report.txt")
assert reportFile.isFile()

// Assert the report content is as expected
InputStream fileInputStream = new FileInputStream(reportFile)
String reportContent = IOUtils.toString(fileInputStream, Charset.defaultCharset())
assert reportContent.contains("[WARNING] test.properties :: Property [foo] is missing")
