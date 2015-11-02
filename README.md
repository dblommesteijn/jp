## Java Packer - JP

[![Build Status](https://travis-ci.org/java-packer/jp.svg?branch=master)](https://travis-ci.org/java-packer/jp)

Java Packer (JP) is a toolkit for Java that automates packing Maven Central dependencies, and local JARs into a single project. JP manages your `pom.xml` dependencies, integrates with local, and [Git](https://git-scm.com)/ [Github](http://github.com) projects!

JPacker is inspired by Ruby's [Bundler](http://bundler.io/), Python's [virtualenv](https://virtualenv.readthedocs.org/en/latest/), and [Pip](https://pip.readthedocs.org/en/latest/).

JPacker's projects are based on Maven3, thus all your `mvn` commands will run as expected.

**Prerequisites**

* Unix based OS
* Java (6,7,8)
* Maven 3
* Environment variables `M3_HOME` and `JAVA_HOME` set


### Installation

Clone the github repository and install it.

```bash
git clone git@github.com:java-packer/jp.git
cd jp/
make
sudo make install
MAVEN_OPTS=-Xmx512m # to speedup the build (a little)
```

*Uninstall: `sudo make uninstall`.*


### Usage

This requires JP to be installed onto your system. Or you can run jp from the `scripts/` folder directly, but this is not recommended.

**Create Project**

Creating a JP project in the current folder named `myproject` with groupId `net.domain.myproject`.

```bash
cd /path/to/your/repos
jp new net.domain.myproject myproject_alt_name
cd myproject_alt_name/
# OR
jp new net.domain.myproject
cd myproject
```

*NOTE: you can set an alternate name for the artifactId, it defaults to the least significant namespace of your groupId.*

**JP Help**

JP help is available with a list of all applicable commands.

```bash
jp help
```


#### JPacker Metadata File

JP generates an JPacker metadata file (JSON), which will contain all your dependencies. By appending to the dependency list they will become available in your project. Below you can find the default structure.

*NOTE: JP generated projects have a default dependency on `junit`, loaded from maven-central, and is always included.*

```json
{ "dependencies": [
  { "name": "junit#junit#4.12" }
] }
```

#### Setting JPacker Dependencies

JP supports `maven-central`, `git`, and local `jar` dependencies. For each you can find examples below.

**Maven-Central**

Maven-central dependencies require the `name` field only. The basic structure is described below.

```json
{
  "name": "groupId#artifactId#version"
}
```

**Git**

Git dependencies need a reference to the repository, which needs to be cloned (jp does this for you). Below, the basic structure is described.

```json
{
  "name": "groupId#artifactId#version",
  "github": "https://github.com/username/yourproject",
  "target": "target/yourproject-version.jar",
  "goal": "the mvn goal to build yourproject-version.jar file"
}
```

*NOTE: github HTTPS supported only*
*NOTE2: optional `"commit": "hash"`.*

**JAR File**

```json
{
  "name": "groupId#artifactId#version",
  "file": "/path/to/your/jarfile.jar"
}
```
#### JPacker specification

```json
{"dependencies": [
  {
    "name": "groudId#ArtifactId#version"
  }
]}
```


#### Collect Dependencies

Reading the `JPacker` metadata file, and processing three types of sources: `buildin`, `local file`, and `Github`. Subsequently local file will deploy the supplied `.jar` to the `repo/` folder. Github will clone the repository (or supplied branch, commit, tag), build the project based on the goal and deploy the `.jar` to `repo/`. Build-ins are appended to `pom.xml` only. All dependencies from `JPacker` are appeded to `pom.xml`.

*NOTE: the `dependencies` element of `pom.xml` will be cleared and reappended.*

```bash
jp collect
```


## Development/ Contributing

You can contribute to Java Packer by forking and creating pull-requests. Below are some commands that can be helpful.

**Running Tests**

Requires internet.

```bash
mvn test
```

Running single test classes via `-Dtest=***`. Available tests are listed in `src/test/java/net/thepinguin/jp/*`.

*NOTE: by default Main.app stderr/stdout are disabled in tests, enable it via `export JP_VERBOSE="true"`, before running `mvn test`.*

**Creating Eclipse project**

```bash
mvn eclipse:eclipse
```

*NOTE: after importing the project into your workspace convert it to a Maven project.*

**Shorthands**

A few shorthand commands for building and installing.

* `make deploy`: `make build-no-test && sudo make install` (with tests)
* `make deploy-fast`: `make build-no-test && sudo make install` (without tests)
* `make test`: `mvn test`
* `make dev-eclipse`: `mvn eclipse:eclipse`

### Vagrant

Run a vagrant instance of Debian Jessy, and provision JP on it.

```bash
vagrant up
```







