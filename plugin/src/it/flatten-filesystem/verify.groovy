import org.apache.commons.io.IOUtils

import java.nio.charset.Charset

// Assert the report file exists
File reportFile = new File(basedir, "sourcehawk-flattened.yml")
assert reportFile.isFile()

// Assert the report content is as expected
InputStream fileInputStream = new FileInputStream(reportFile)
String reportContent = IOUtils.toString(fileInputStream, Charset.defaultCharset())

assert reportContent.trim() == """file-protocols:
- name: "Test Properties"
  description: "Test Properties"
  group: "props"
  repository-path: "test.properties"
  required: true
  tags:
  - "props"
  severity: "ERROR"
  enforcers:
  - enforcer: ".common.StringPropertyEquals"
    property-name: "key"
    expected-property-value: "value"
""".trim()

