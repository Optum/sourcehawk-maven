import org.apache.commons.io.IOUtils

import java.nio.charset.Charset

// Assert the custom report file exists
File reportFile = new File(basedir, "target/sourcehawk/sourcehawk-custom.txt")
assert reportFile.isFile()

// Assert the report content is as expected
InputStream fileInputStream = new FileInputStream(reportFile)
String reportContent = IOUtils.toString(fileInputStream, Charset.defaultCharset())
assert reportContent == "Scan passed without any errors!"