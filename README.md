本地开发环境配置：

1.安装Vagrant
	1.1.下载安装包:
	     下载地址：https://www.vagrantup.com/downloads.html
	1.2.直接安装
	1.3.安装完毕后在终端输入vagrant version,安装成功的话会显示版本信息

2.安装VirtualBox
	2.1.下载安装包:
	      下载地址：https://www.virtualbox.org/wiki/Downloads
	2.2.直接安装
	2.3.安装完毕后在终端输入virtualBox,安装成功的话会弹出虚拟机图形界面

3.初始化虚拟机环境
	3.1.创建虚拟机所在目录:
	      cd ~
	      mkdir workspace
	3.2.向同事索要虚拟机打包的box文件, 并将该文件放在~/workspace 下，假设该文件名为vm.box
	3.3.生成初始化虚拟机文件:
	      cd ~/workspace
	      vagrant init vm.box
	      执行成功后~/workspace目录下会出现名为Vagrantfile的文件
	3.4.修改Vagrantfile文件:
	      cd ~/workspace
	      vim Vagrantfile
	      打开编辑界面后将配置全部替换成文末的Vagrantfile配置，然后将config.vm.synced_folder "/Users/UserName/workspace", "/workspace”这行配置中的UserName换成本地用户名
	3.5.验证虚拟机可以正常启动
	      cd ~/workspace
	      vagrant up     #出现选择网卡的选项时输入1
	      vagrant ssh
	      Vagrantfile修改没有文件的话可以进入虚拟机环境中。验证没有问题后输入以下命令退出虚拟机并关闭虚拟机
	      exit
	      vagrant halt
	3.6.在虚拟机中配置redis
	      在https://redis.io/ 下载redis并解压缩，将解压缩后的文件夹放在~/workspace 目录下。该目录已经成为虚拟机和本地环境间的共享文件夹，本地在该目录下放置的所有文件都会在虚拟机中的/workspace目录下出现，因此之后可以利用该文件进行两套环境间的文件传输
	      文件移动完后进入虚拟机环境
	      cd /workspace/redis-version  #version根据下载的redis版本有所差异
	      vim redis.conf  #将其中的protected-mode 选项由yes改成no, 并注释掉bind 127.0.0.1这行配置
	      sudo make install #编译redis
	      cd src
	      ./redis-server ../redis.conf #redis安装成功的话可以看到redis启动画面
	      验证安装成功后，Ctrl + C关闭redis服务
	3.7.在虚拟机中配置MySQL
	      mysql -uroot
	      create user ‘root’@‘%’
	      grant all on *.* to 'root'@'%'
	      之后就可以在本地用root的无密码账号连接虚拟机的数据库了
	      
4.下载代码
	cd ~
	mkdir Work
	cd Work  #之后所有工程相关的东西都会放在Work目录下
	git clone XXX # XXX是github上的相关clone ssh

5.安装mysql
	5.1.在https://dev.mysql.com/downloads/mysql/ 下载mysql安装文件
	5.2.直接安装
	5.3.安装成功后，打开系统偏好设置，在mysql选项中打开mysql server

6.配置python环境
	6.1.安装pip
	在https://pip.pypa.io/en/stable/installing/ 下载get-pip.py文件，然后进入该文件所在目录，输入以下命令安装pip:
	python get-pip.py
	6.2.安装virtualenv
	pip install virtualenv
	6.3.创建虚拟环境
	cd ~/Work
	virtualenv -p /usr/bin/python2.7 vent
	6.4.使用虚拟环境
	source ~/Work/venv/bin/active #成功后终端命令行前边会出现(venv)
	6.5.在虚拟环境下安装需要的软件
	cd ~/Work/tuotian/signin_service
	pip install -r requirements.txt
	中途如果出现mysql_config not found的问题的话需要输入下列命令后再重新使用pip安装所需软件。
	export PATH=$PATH:/usr/local/mysql/bin
	向同事索要aliyun-mns-python-sdk-1.1.3安装包，解压缩后进入解压的目录中，执行
	sudo python setup.py install
	安装完毕后执行以下命令，配置没有问题的话登录服务会正常开启
	cd ~/Work/tuotian/signin_service
	sudo python web.py

7.安装home-brew
	7.1.在终端输入
	/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
	7.2.安装成功的话在终端输入brew -v 会显示版本信息

8.安装gradle
	8.1.在终端输入
	brew install gradle
	8.2.安装成功的话在终端输入gradle -v 会显示版本信息

9.安装前端所需插件
	9.1.安装nodejs和npm
	brew install node
	安装成功后在终端输入node -v 和npm -v 会显示版本信息
	9.2.安装前端所需插件
	cd ~/Work/tuotian/ttsd-frontend-manage
	npm install --registry=https://registry.npm.taobao.org
	安装成功后输入npm start ，会看到服务成功运行

10.安装Java
	10.1.下载安装包
	在http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html 下载安装包
	10.2.直接安装，安装成功后在终端输入java -version 会显示版本信息，版本应当是java8

11.安装Idea
	11.1.下载安装包
	在http://www.jetbrains.com/idea/download/download-thanks.html 下载安装包
	11.2.直接安装，正版授权自己想办法

12.安装tomcat
	12.1.下载安装包
	在http://tomcat.apache.org/download-90.cgi 下载安装包
	12.2.将安装包解压到~/Work 目录下
	12.3.在idea中直接把tomcat路径配置成~/Work/apache-tomcat-version即可, version根据下载的版本不同而有所差异

13.配置虚拟机MySQL数据
	13.1.在虚拟机环境下输入mysql -uroot 进入mysql
	13.2.根据~/Work/tuotian/ttsd-config/src/main/resources/ttsd-env.properties文件中的数据库配置信息在虚拟机的mysql中创建用户，命令如下:
	create user ‘username’@‘%’ identified by ‘password’ —username和password需要根据配置文件替换成对应的用户名密码
	之后给这些账号授予权限，命令如下:
	grant all on *.* to ‘username’@‘%’  —username需要替换成对应的用户名
	13.3.根据~/Work/tuotian/ttsd-config/src/main/resources/ttsd-env.properties文件中的数据库配置信息在虚拟机的mysql中创建库，格式全部是utf-8。需要注意的是还需要创建一个名为job_worker的库，这个库在配置文件中没有配置
	13.4.进入本地环境
	cd ~/Work/tuotian
	使用以下命令向数据库中迁入数据
	gradle -Pdatabase=dbname ttsd-config:flywayMigrate —其中dbname应当替换成相应库名

14.配置anxinsign.jks文件
	14.1.向同事索要anxinsign-test.jks文件
	14.2.将anxinsign-test.jks放在~/Work/tuotian/ttsd-anxin-sign/src/main/resources 目录下，并重命名为anxinsign.jks

15.启动注意事项
	15.1.确保虚拟机开启，并在redis的src目录下使用./redis-server ../redis.conf 打开了redis服务
	15.2.确保python切换到了venv的虚拟环境，并打开了web.py服务
	15.3.确保在tuotian/ttsd-frontend-manage 下运行了npm start 打开了前端服务


Vagrantfile配置:
# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "workVM"
  config.ssh.username = 'vagrant'
  config.ssh.password = 'vagrant'

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # config.vm.network "forwarded_port", guest: 80, host: 8080

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network "private_network", ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder "/Users/UserName/workspace", "/workspace"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
    # Display the VirtualBox GUI when booting the machine
    # vb.gui = true
  
    # Customize the amount of memory on the VM:
    vb.memory = "2048"
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Define a Vagrant Push strategy for pushing to Atlas. Other push strategies
  # such as FTP and Heroku are also available. See the documentation at
  # https://docs.vagrantup.com/v2/push/atlas.html for more information.
  # config.push.define "atlas" do |push|
  #   push.app = "YOUR_ATLAS_USERNAME/YOUR_APPLICATION_NAME"
  # end

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  # config.vm.provision "shell", inline: <<-SHELL
  #   sudo apt-get update
  #   sudo apt-get install -y apache2
  # SHELL
end
