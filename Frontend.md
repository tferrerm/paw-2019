## Frontend configuration

### Install NodeJS and NPM

*Warning*: do **NOT** install `npm` or `nodejs` with `apt` or `apt-get`.
It is probably recommended to uninstall `npm` and `node` (as explained in this post https://stackoverflow.com/questions/11177954/how-do-i-completely-uninstall-node-js-and-reinstall-from-beginning-mac-os-x ) and then install it using `nvm`, as detailed below.

Tested versions:
```bash
npm -v
 # 6.9.0
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
nvm install v10.16.2 # or any other version
nvm alias default v10.16.2
```

### Install Ruby

*Note*: macOS already comes with ruby 2.3.7 preinstalled. It may cause some trouble, so it is probably recommended to install it using `rbenv`.
Xcode needs to be installed.

Tested versions:
```bash
ruby -v
 # 2.5.0
```

macOS:
```bash
brew install rbenv
rbenv init
exit

# new terminal
curl -fsSL https://github.com/rbenv/rbenv-installer/raw/master/bin/rbenv-doctor | bash

mkdir -p "$(rbenv root)"/plugins
git clone https://github.com/rbenv/ruby-build.git "$(rbenv root)"/plugins/ruby-build

rbenv install <desired-ruby-version>
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

rbenv install <desired-ruby-version>
```

### Project Configuration

Run the following commands (do not run sudo if using nvm):
```bash
cd paw-2019/frontend
sudo npm install -g yo
sudo npm install -g generator-angular-require-fullstack
sudo npm install -g grunt-cli
sudo npm install -g bower
npm install
bower install
gem update --system && gem install compass

# To solve the following error:
# Warning: ENOSPC: System limit for number of file watchers reached, watch '~/paw-2019/frontend/'
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
sudo sysctl --system
```

To serve:
```bash
grunt serve
```
