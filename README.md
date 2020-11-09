# Sourcehawk Maven

[![Maven Central](https://img.shields.io/maven-central/v/com.optum.sourcehawk.maven/sourcehawk-maven.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.optum.sourcehawk.maven%22%20AND%20a:%22sourcehawk-maven%22) 
 
[![Build Status](https://github.com/optum/sourcehawk/workflows/Maven%20CI/badge.svg)](https://github.com/optum/sourcehawk/actions) 
[![Sourcehawk Scan](https://github.com/optum/sourcehawk/workflows/Sourcehawk%20Scan/badge.svg)](https://github.com/optum/sourcehawk/actions) 
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.optum.sourcehawk.maven%3Asourcehawk-maven&metric=coverage)](https://sonarcloud.io/dashboard?id=com.optum.sourcehawk.maven%3Asourcehawk-maven)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.optum.sourcehawk.maven%3Asourcehawk-maven&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.optum.sourcehawk.maven%3Asourcehawk-maven)

![OSS Lifecycle](https://img.shields.io/osslifecycle/optum/sourcehawk-maven) 
[![Sonatype OSS Index](https://img.shields.io/badge/Sonatype%20OSS%20Index-sourcehawk--maven--plugin-informational)](https://ossindex.sonatype.org/component/pkg:maven/com.optum.sourcehawk.maven/sourcehawk-maven-plugin)

Contains the following projects:

## Sourcehawk Maven Plugin
A Maven plugin which can be used to execute [Sourcehawk](https://github.com/optum/sourcehawk) scans on code.

[Documentation](https://optum.github.io/sourcehawk-maven)

### Usage

Include the following in `pom.xml` in the `build / plugins` section.
```xml
<plugin>
    <groupId>com.optum.sourcehawk.maven</groupId>
    <artifactId>sourcehawk-maven-plugin</artifactId>
    <version>0.1.1</version>
    <executions>
        <execution>
            <id>sourcehawk-scan</id>
            <goals>
                <goal>scan</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Demo

[![Maven Plugin Demo](https://optum.github.io/sourcehawk-docs/img/demo/maven.gif)](https://optum.github.io/sourcehawk-docs/img/demo/maven.gif)

Click the demo to enlarge.

## Contributing
If you wish to contribute to the development of Sourcehawk please read [CONTRIBUTING.md](CONTRIBUTING.md) for more information.
