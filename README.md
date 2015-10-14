## Java Packer

Java Packer (JP) is a toolkit for Java that automates packing Maven Central dependencies, and local JARs into a single project. JP manages your `pom.xml` dependencies, integrates with local, and [Github](http://github.com) projects (jars)!

JPacker is inspired by Ruby's [Bundler](http://bundler.io/), Python's [virtualenv](https://virtualenv.readthedocs.org/en/latest/) and [Pip](https://pip.readthedocs.org/en/latest/).

[![Build Status](https://travis-ci.org/java-packer/jp.svg?branch=master)](https://travis-ci.org/java-packer/jp)

**Prerequisites**

* Unix based OS
* Java (6,7,8)
* Maven 3

### Installation

Clone the github repository and install it.

```bash
git clone git@github.com:java-packer/jp.git
cd jp
make
sudo make install
MAVEN_OPTS=-Xmx512m # to speedup the build (a little)
```

*Uninstall: `sudo make uninstall`.*


### Usage

This requires JP to be installed onto your system.

**Create Project**

Creating a JP project in the current folder named `myproject` with groupId `net.domain.myproject`.

```bash
cd /path/to/your/repos
# jp new `groupId` [`artifactId`]
jp new net.domain.myproject (myproject-alt)
cd myproject/
```

*NOTE: all JPacker projects are based on Maven 3, thus all your `mvn` commands will run.*
*NOTE2: optionally you can set an alternate name for the artifactId, it defaults to the least siginificant namespace of your groupId.*

#### Initialize existing Project

* Not available.


#### JPacker Metadata File

JP generates an JPacker metadata file, which will contain all your dependencies. By appending JSON objects to the dependency list they will become available in your project.

*NOTE: jp has a default dependency on junit.*

```json
{ "dependencies": [
	{"name": "junit#junit#4.12"}
] }
```

**Maven Central deps**

```json
{
	"name": "groupId#artifactId#version",
	"scope": "test"
}
```

**Github integration**

*NOTE: github https supported only, optional `"commit": "hash"`.*

```json
{
	"name": "groupId#artifactId#version",
	"github": "https://github.com/username/yourproject",
	"target": "target/yourproject-version.jar",
	"scope": "compile",
	"goal": "the mvn goal to build yourproject-version.jar file"
}
```

**Local JAR**

```json
{
	"name": "groupId#artifactId#version",
	"file": "/path/to/your/jarfile.jar",
	"scope": "compile"
}
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

Running single tests via `-Dtest=***`. Available tests are: `JPackerTest`, `PomTest`, `RepositoryTest`, `TestCommands`.

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







