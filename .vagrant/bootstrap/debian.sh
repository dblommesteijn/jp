echo "********************************"
echo " - Provisioning Debian with JP"
echo "********************************"

export DEBIAN_FRONTEND=noninteractive

echo " ** Installing prerequisites **"
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
apt-get -qyo Dpkg::Options::="--force-confdef" update
apt-get -qyo Dpkg::Options::="--force-confdef" install python-software-properties debconf-utils vim

echo " ** Installing Java/ Maven **"

echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" > /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" >> /etc/apt/sources.list.d/webupd8team-java.list
add-apt-repository -y ppa:webupd8team/java
apt-get -qyo Dpkg::Options::="--force-confdef" update
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
apt-get -qyo Dpkg::Options::="--force-confdef" install oracle-java8-installer maven

export JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")
export M3_HOME=$(readlink -f /usr/bin/mvn | sed "s:/bin/mvn::")

echo " ** Building package **"

cd /vagrant/
make build-no-test
sudo make install

echo " ** Testing package **"
cd /home/vagrant/
jp new testJpApp net.thepinguin.testJpApp
cd testJpApp
if [[ $? != 0 ]]; then
    echo "jp not correctly installed"
    exit 1
fi
