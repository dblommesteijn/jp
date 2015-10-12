# Copyright 2014 D.Blommesteijn
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

VERSION=0.1

PREFIX=/usr/local
INSTALLDIR=$(PREFIX)
INSTALL=install
TARGETS=

build:
	mvn assembly:assembly -DdescriptorId=jar-with-dependencies
	
build-no-test:
	mvn assembly:assembly -DdescriptorId=jar-with-dependencies -DskipTests=true

test:
	mvn test

install: $(TARGETS)
	$(INSTALL) -c -v -g 0 -m 0755 -o root scripts/jp $(INSTALLDIR)/bin
	$(INSTALL) -c -v -g 0 -m 0755 -o root target/jp-$(VERSION)-jar-with-dependencies.jar $(INSTALLDIR)/lib

uninstall: $(TARGETS)
	rm $(INSTALLDIR)/bin/jp
	rm $(INSTALLDIR)/lib/jp-$(VERSION)-jar-with-dependencies.jar
	
deploy:
	make && sudo make install
	
deploy-fast: $(TARGETS)
	make build-no-test && sudo make install

dev-eclipse:
	mvn eclipse:eclipse

clean:
	mvn clean

# EOB