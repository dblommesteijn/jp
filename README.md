## Java Packer

JP is a toolkit for Java that allows packing Maven Central, and local JARs into your project. The toolkit manages your `pom.xml` files, so you don't have to handle tons of XML.

Packer is inspired by Ruby's [Bundler](http://bundler.io/) and Python's [virtualenv](https://virtualenv.readthedocs.org/en/latest/) with [Pip](https://pip.readthedocs.org/en/latest/).

**Dependencies**

* Unix OS (OS X, Linux)
* Shell
* make
* Java6/7/8
* Maven3

*Sorry Windows users, no support for you.*


### Known issues

* stub


### Installation

```bash
git clone git@github.com:dblommesteijn/js.git
cd js
make
sudo make install
```

Uninstall: `sudo make uninstall`.


### Usage

**Init scaffold**

```bash
cd /path/to/your/repos
jp new myproject net.domain.myproject
```



