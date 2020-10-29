import org.apache.commons.io.IOUtils

import java.nio.charset.Charset

// Assert the report file exists
File reportFile = new File(basedir, "target/sourcehawk/report.txt")
assert reportFile.isFile()

// Assert the report content is as expected
InputStream fileInputStream = new FileInputStream(reportFile)
String reportContent = IOUtils.toString(fileInputStream, Charset.defaultCharset())
assert reportContent.contains("[ERROR] test.properties :: Property [foo] is missing")