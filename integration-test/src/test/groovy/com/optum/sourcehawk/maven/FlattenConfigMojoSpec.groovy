package com.optum.sourcehawk.maven

import org.apache.maven.it.Verifier
import org.apache.maven.it.util.ResourceExtractor
import spock.lang.Specification

class FlattenConfigMojoSpec extends Specification {

    def "test flatten console"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/flatten-console")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        verifier.executeGoal("sourcehawk:flatten-config")

        then:
        verifier.verifyTextInLog("BUILD SUCCESS")
        verifier.resetStreams()
    }

    def "test flatten file system"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/flatten-filesystem")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        verifier.executeGoal("sourcehawk:flatten-config")

        then:
        verifier.verifyTextInLog("BUILD SUCCESS")
        verifier.assertFilePresent("sourcehawk-flattened.yml")
        verifier.resetStreams()
    }

    def "test flatten file system custom name"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/flatten-filesystem-custom")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        verifier.executeGoal("sourcehawk:flatten-config")

        then:
        verifier.verifyTextInLog("BUILD SUCCESS")
        verifier.assertFilePresent("sourcehawk-flattened-custom.yml")
        verifier.resetStreams()
    }

    def "test flatten file system bad config"() {
        given:
        File testDir = ResourceExtractor.simpleExtractResources(getClass(), "/flatten-bad-config")
        Verifier verifier = new Verifier(testDir.getAbsolutePath())

        when:
        try {
            verifier.executeGoal("sourcehawk:flatten-config")
        } catch (Exception ignored) {
        }

        then:
        verifier.verifyTextInLog("Error reading configuration file")
        verifier.verifyTextInLog("BUILD FAILURE")
        verifier.assertFileNotPresent("sourcehawk-flattened.yml")
        verifier.resetStreams()
    }
}
