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
```

*Uninstall: `sudo make uninstall`.*


### Usage

This requires JP to be installed onto your system.

**Create Project**

Creating a JP project in the current folder named `myproject` with groupId `net.domain.myproject`.

```bash
cd /path/to/your/repos
jp new myproject net.domain.myproject
cd myproject/
```

*NOTE: all JPacker projects are based on Maven 3, thus all your `mvn` commands will run.*

**Initialize existing Project**

* Coming soon


**JPacker Metadata File**

JP generates an empty JPacker metadata file, which will contain all your dependencies. By appending JSON objects to the dependency list they will become available in your project.

```json
{ "dependencies": [ ] }
```

*Maven Central deps*

```json
{
	"name": "groupId#artifactId#version",
	"scope": "test"
}
```

*Github integration*

NOTE: github https supported only, optional `"commit": "hash"`.

```json
{
	"name": "groupId#artifactId#version",
	"github": "https://github.com/username/yourproject",
	"target": "target/yourproject-version.jar",
	"scope": "compile",
	"goal": "the mvn goal to build yourproject-version.jar file"
}
```

*Local JAR*

```json
{
	"name": "groupId#artifactId#version",
	"file": "/path/to/your/jarfile.jar",
	"scope": "compile"
}
```

**Collect Dependencies**

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

**Creating Eclipse project**

```bash
mvn eclipse:eclipse
```

*NOTE: after importing the project into your workspace convert it to a Maven project.*


## Vagrant

Run a vagrant instance of Debian Jessy, and provision JP on it.

```bash
vagrant up
```





