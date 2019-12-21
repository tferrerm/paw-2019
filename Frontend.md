## Frontend configuration

### Install NodeJS and NPM

*Warning*: do **NOT** install `npm` or `nodejs` with `apt` or `apt-get` or `brew`.
It is probably recommended to uninstall `npm` and `node` (as explained in [this post](https://stackoverflow.com/questions/11177954/how-do-i-completely-uninstall-node-js-and-reinstall-from-beginning-mac-os-x)) and then install it using `nvm`, as detailed below. If you choose to use a system-wide installation of node, you will need `sudo` to install node packages globally.

Tested versions:
```bash
npm -v
 # 6.7.0
node -v
 # v11.12.0
```

Recommended installation:
```bash
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
exit

# New terminal
command -v nvm # verify nvm vas installed
nvm ls # list Node versions (probably none)
nvm install v11.12.0 # or any other version
nvm alias default v11.12.0
```

### Install Ruby
Ruby is necessary in order to use Compass and Sass.
*Note*: macOS already comes with Ruby preinstalled. Though it is recommended to install it using `rbenv`, you can use the provided version of Ruby if you have Xcode installed. If Xcode is not installed, you won't be able to install gems.

Tested versions:
```bash
ruby -v
 # 2.6.5
```

#### Install Ruby with `rbenv`
macOS:
```bash
brew install rbenv
rbenv init
exit

# new terminal
curl -fsSL https://github.com/rbenv/rbenv-installer/raw/master/bin/rbenv-doctor | bash

mkdir -p "$(rbenv root)"/plugins
git clone https://github.com/rbenv/ruby-build.git "$(rbenv root)"/plugins/ruby-build

rbenv install 2.6.5 # or any other version, like 2.5.0
```

Linux:
```bash
git clone https://github.com/rbenv/rbenv.git ~/.rbenv
cd ~/.rbenv && src/configure && make -C src # do not worry if it fails

# Ubuntu
echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bashrc
# Bash
echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bash_profile
# zsh
echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.zshrc

~/.rbenv/bin/rbenv init
exit

# New terminal
curl -fsSL https://github.com/rbenv/rbenv-installer/raw/master/bin/rbenv-doctor | bash
mkdir -p "$(rbenv root)"/plugins
git clone https://github.com/rbenv/ruby-build.git "$(rbenv root)"/plugins/ruby-build

rbenv install 2.6.5 # or any other version, like 2.5.0
```

### Project Configuration

Run the following commands (do not run sudo if using nvm):
```bash
cd paw-2019/frontend
npm install -g yo # might need sudo
npm install -g generator-angular-require-fullstack # might need sudo
npm install -g grunt-cli # might need sudo
npm install -g bower # might need sudo
npm install -g karma # might need sudo
npm install -g karma-cli # For unit tests; might need sudo
npm install
bower install
gem update --system && gem install compass

# To solve the following error:
# Warning: ENOSPC: System limit for number of file watchers reached, watch '~/paw-2019/frontend/'
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
sudo sysctl --system
```

To start the server locally:
```bash
grunt serve
```
To build:
```bash
grunt build
```
To run unit tests:
```bash
karma start # generation of karma configuration is needed first (test-main.js and karma.conf.js files)
```


*Note*: When initializing the yeoman project, older versions of AngularJS will be used (with serious bugs), so it is recommended to use the latest version of AngularJS (1.7.9 to date). It might be necessary to upgrade some other gems.
