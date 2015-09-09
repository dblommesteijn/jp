# -*- mode: ruby -*-
# vi: set ft=ruby :

# vagrant config
Vagrant.configure("2") do |config|

  # testing configuration
  config.vm.define "default" do |testing|
    testing.vm.box = "remram/debian-8-amd64"
    testing.vm.provision :shell, path: ".vagrant/bootstrap/debian.sh"
    testing.vm.provider :virtualbox do |vb|
      vb.customize ["modifyvm", :id, "--memory", "256"]
    end
  end

end