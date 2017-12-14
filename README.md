#本地开发环境配置：

* 安装Vagrant
	1. 下载安装包</br>
	下载地址: <https://www.vagrantup.com/downloads.html>
	2. 直接安装
	3. 安装完毕后在终端输入`vagrant version`, 安装成功的话会显示版本信息

* 安装VirtualBox
	1. 下载安装包</br>
	下载地址: <https://www.virtualbox.org/wiki/Downloads>
	2. 直接安装
	3. 安装完毕后在终端输入`virtualBox`, 安装成功的话会弹出虚拟机图形界面

* 初始化虚拟机环境
	1. 创建虚拟机所在目录:</br>
		`cd ~`</br>
		`mkdir workspace`
	2. 向同事索要虚拟机打包的box文件, 并将该文件放在`~/workspace`下，假设该文件名为vm.box
	3. 生成初始化虚拟机文件:</br>
	      `cd ~/workspace`</br>
	      `vagrant init vm.box`</br>
	      执行成功后`~/workspace`目录下会出现名为Vagrantfile的文件
	4. 修改Vagrantfile文件:</br>
	      `cd ~/workspace`
	      `vim Vagrantfile`
	      打开编辑界面后将配置全部替换成文末的Vagrantfile配置，然后将`config.vm.synced_folder "/Users/UserName/workspace", "/workspace”`这行配置中的UserName换成本地用户名
	5. 验证虚拟机可以正常启动</br>
	      `cd ~/workspace`</br>
	      `vagrant up     #出现选择网卡的选项时输入1`</br>
	      `vagrant ssh`</br>
	      如果Vagrantfile修改没有问题的话可以进入到虚拟机环境中
	6. 在虚拟机中配置redis</br>
	      在<https://redis.io/>下载redis并解压，将解压缩后的文件夹放在`~/workspace`目录下。该目录是虚拟机和本地环境间的共享文件夹，本地在该目录下放置的所有文件都会在虚拟机中的`/workspace`目录下出现，因此之后可以利用该文件从本地向虚拟机中传输文件。</br>
	      文件移动完后进入虚拟机环境
	      `cd /workspace/redis-version  #version根据下载的redis版本有所差异`</br>
	      `vim redis.conf  #将其中的protected-mode 选项由yes改成no, 并注释掉bind 127.0.0.1这行配置`</br>
	      `sudo make install #编译redis`</br>
	      `cd src`</br>
	      `./redis-server ../redis.conf #redis安装成功的话可以看到redis启动画面`</br>
	      验证安装成功后，按Ctrl + C关闭redis服务
	7. 在虚拟机中配置本地连接虚拟机MySQL的账号</br>
	      `mysql -uroot`</br>
	      `create user ‘root’@‘%’`</br>
	      `grant all on *.* to 'root'@'%'`</br>
	      
* 安装home-brew
	1. `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"`
	2. 安装成功的话在终端输入`brew -v`会显示版本信息
	
* 安装git
	1. `brew install git`
	2. 安装成功的话在终端输入`git --version`会显示版本信息
	      
* 下载代码</br>
	`cd ~`</br>
	`mkdir Work`</br>
	`cd Work  #之后所有工程相关的东西都会放在Work目录下`</br>
	`git clone XXX # XXX是github上的相关clone ssh`</br>

* 安装mysql</br>
	1. 下载安装包</br>
		下载地址: <https://dev.mysql.com/downloads/mysql/>
	2. 直接安装
	3. 安装成功后，打开系统偏好设置，在mysql选项中打开mysql server

* 配置python环境
	1. 安装pip</br>
	在<https://pip.pypa.io/en/stable/installing/>下载get-pip.py文件，然后进入该文件所在目录安装pip</br>
	`python get-pip.py`</br>
	2. 安装virtualenv</br>
	`pip install virtualenv`</br>
	3. 创建虚拟环境</br>
	`cd ~/Work`</br>
	`virtualenv -p /usr/bin/python2.7 venv`</br>
	4. 使用虚拟环境</br>
	`source ~/Work/venv/bin/active #成功后终端命令行前边会出现(venv)`</br>
	5. 在虚拟环境下安装需要的软件</br>
	`cd ~/Work/tuotian/ttsd-user-rest-service`</br>
	`pip install -r requirements.txt`</br>
	中途如果出现`mysql_config not found`的问题的话需要输入下列命令后再重新使用pip安装</br>
	`export PATH=$PATH:/usr/local/mysql/bin`</br>
	使用pip安装完后,向同事索要aliyun-mns-python-sdk-1.1.3安装包，解压缩后进入解压的目录中</br>
	`sudo python setup.py install`</br>
	安装完毕后执行以下命令，配置没有问题的话登录服务会正常开启</br>
	`cd ~/Work/tuotian/ttsd-user-rest-service`</br>
	`sudo python web.py`</br>

* 安装gradle
	1. `brew install gradle`
	2. 安装成功的话在终端输入`gradle -v`会显示版本信息

* 安装前端所需插件
	1. 安装nodejs和npm
	`brew install node`</br>
	安装成功后在终端输入`node -v`和`npm -v`会显示版本信息
	2. 根目录路径： ~/Work/tuotian/ttsd-frontend-manage ，以下简称<根目录>
	`cd <根目录>`</br>
	`npm install --registry=https://registry.npm.taobao.org`</br>
	安装成功后，执行 `npm start` 启动虚拟服务器 ，再启动tomcat项目，注意先后顺序，会看到服务成功运行 </br>

	打包本地文件：</br>
    `npm run start2` </br>

    package.json里script里的命令： </br>
    `npm run json` 生成所有打包文件的json文件，并根据项目生成  json-ask.json， json-web.json ， json-mobile.json ， json-point.json, json-activity.json</br>
    `npm run plugin`    用DllPlugin打包插件文件  </br>
    `npm run mock`  开启node express 服务器，用来模拟假数据 </br>
    `npm run showimage` </br>

    `npm run json` 和 `npm run plugin`以及 `npm run showimage`已经集成在 npm run build 和 npm start里，无需额外执行</br></br>

    生成项目图片文件：  进入<根目录></br>


    清除项目中无用的图片</br>
    `npm run start2` </br>
    `npm run removeUselessImgs` </br></br>

    上线发布的： 进入<根目录></br>
    `npm install --registry=https://registry.npm.taobao.org` </br>
    `npm run build` </br>

* 安装Java
	1. 下载安装包
	下载地址: <http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>
	2. 直接安装
	3. 安装完后在终端输入`java -version`会显示版本信息，版本应当是java8

* 安装Idea
	1. 下载安装包
	下载地址: <http://www.jetbrains.com/idea/download/download-thanks.html>
	2. 直接安装
	3. 安装成功后可以打开软件, 正版授权自己想办法

* 安装tomcat
	1. 下载安装包
	下载地址: <http://tomcat.apache.org/download-90.cgi>
	2. 将安装包解压到`~/Work`目录下
	3. 在idea中把tomcat路径配置成`~/Work/apache-tomcat-version`, version根据下载的版本不同而有所差异

* 配置虚拟机MySQL数据
	1. 在虚拟机环境下输入`mysql -uroot`进入mysql
	2. 根据`~/Work/tuotian/ttsd-config/src/main/resources/ttsd-env.properties`文件中的数据库配置信息在虚拟机的mysql中创建用户</br>
	`create user ‘username’@‘%’ identified by ‘password’ --username和password需要根据配置文件替换成对应的用户名密码`</br>
	之后给这些账号授予权限</br>
	`grant all on *.* to ‘username’@‘%’ --username需要替换成对应的用户名`
	3. 根据`~/Work/tuotian/ttsd-config/src/main/resources/ttsd-env.properties`文件中的数据库配置信息在虚拟机的mysql中创建库，字符格式全部是utf-8。需要注意的是还需要创建一个名为`job_worker`的库，这个库没有出现在配置文件里</br>
	4. 进入本地环境</br>
	`cd ~/Work/tuotian`</br>
	向数据库中迁入数据</br>
	`gradle -Pdatabase=dbname ttsd-config:flywayMigrate --其中dbname应当替换成相应库名`

* 配置anxinsign.jks文件</br>
	1. 向同事索要anxinsign-test.jks文件
	2. 将anxinsign-test.jks放在`~/Work/tuotian/ttsd-anxin-sign/src/main/resources `目录下，并重命名为anxinsign.jks

* 安装etcd
    1. 在 MAC 下安装
        1. 安装
        ```
        # 打开命令行，执行以下命令
        brew install etcd
        ```
        2. 启动
        ```
        # 打开命令行，执行以下命令
        etcd
        ```
    2. 在虚机里安装
    
        首先ssh到虚拟机
        1. 安装
        ```
        # 打开命令行，执行以下命令
        curl -L -O https://github.com/ttsdzzg/python-repository/raw/master/etcd-install.sh
        chmod +x etcd-install.sh
        sudo ./etcd-install.sh
        ```
        2. 启动 停止
        ```
        # 打开命令行，执行以下命令
        sudo service etcd start # 启动
        sudo service etcd stop # 停止
        ```
    3. 将配置信息写入本地etcd: 
        ```
        # 打开命令行，执行以下命令
        cd ttsd-config
        # 如果在 Mac 里安装的 etcd
        paver flush_etcd
        # 如果是在虚拟机里安装的 etcd
        paver flush_etcd.host=192.168.33.10 flush_etcd
        ```

* 启动注意事项
	1. 确保虚拟机开启，并在redis的src目录下使用`./redis-server ../redis.conf`打开redis服务
	2. 确保python切换到了venv的虚拟环境，并打开了web.py服务
	3. 确保在`tuotian/ttsd-frontend-manage`下运行了`npm start`,打开了前端服务

---

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