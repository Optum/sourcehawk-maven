package com.optum.sourcehawk.maven


import org.apache.maven.it.VerificationException
import org.apache.maven.it.Verifier
import org.apache.maven.it.util.ResourceExtractor
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class ScanMojoSpec extends Specification {

    def "test fail on warnings"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/scan-failed-build-warnings")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        try {
            verifier.executeGoal("sourcehawk:scan")
        } catch (VerificationException ignore) {
        }

        then:
        verifier.verifyTextInLog("BUILD FAILURE")
        verifier.resetStreams()
    }

    def "test passed on warnings"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/scan-passed-build-warnings")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        verifier.executeGoal("sourcehawk:scan")

        then:
        verifier.verifyTextInLog("BUILD SUCCESS")
        verifier.resetStreams()
    }
}
