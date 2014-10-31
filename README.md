## Java Packer

JP is a toolkit for Java that allows packing Maven Central, and local JARs into your project. The toolkit manages your `pom.xml` files, so you don't have to handle tons of XML.

Packer is inspired by Ruby's [Bundler](http://bundler.io/) and Python's [virtualenv](https://virtualenv.readthedocs.org/en/latest/) with [Pip](https://pip.readthedocs.org/en/latest/).

**Prerequisites**

* Unix OS (OS X, Linux)
* Shell
* make
* Java6/7/8
* Maven3

*Sorry Windows users, no support for you.*


### Known issues

* stub


### Installation

Clone the github repository and install it.

```bash
git clone git@github.com:dblommesteijn/js.git
cd js
make
sudo make install
```

*Uninstall: `sudo make uninstall`.*


### Usage

**Create Project**

Creating a JP project in the current folder named `myproject` with groupId `net.domain.myproject`.

```bash
cd /path/to/your/repos
jp new myproject net.domain.myproject
```

*NOTE: projects are Maven3 projects (with a few settings), thus you can run all your `mvn` commands.*

**JPacker metadata**

Project metadata is kept in the JPacker file, and is located in the project root. `jp new ...` will create this file for you.

```json
{ "dependencies": [
	~add your dependencies here~
	{
		"name": "com.domain.myproject#myproject",
		"version": "0.1",
		"file": "/path/to/your/project/binary.jar"
	}
]}
```

**Collect Dependencies**

In order to update local dependencies you have to collect the changed `.jar` files. Dependencies are stored in the project's `/repo` folder. Effectively all `.jar` files will be copied there. Moreover, global dependencies are resolved via Maven Central, they keep working! 

JPacker will add your dependencies to your `pom.xml` file when they are missing. So no need to update them on two separate places.

*NOTE: Each time you add a local dependency (or update the code in the dependency) you need to collect the changes again.*

```bash
# NOT YET IMPLEMENTED!!
jp collect
```





