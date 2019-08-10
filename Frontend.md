## Frontend configuration

Warning: do *NOT* install `npm` or `nodejs` with `apt` or `apt-get`.

Tested versions:
```bash
npm -v
 # 6.9.0
node -v
 # v11.12.0
```

Run the following commands:
```bash
cd paw-2019/frontend
sudo npm install -g yo
sudo npm install -g generator-angular-require-fullstack
sudo npm install -g grunt-cli
sudo npm install -g bower
npm install
bower install

# To solve the following error:
# Warning: ENOSPC: System limit for number of file watchers reached, watch '~/paw-2019/frontend/'
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
sudo sysctl --system

```

To serve:
```bash
grunt serve --force # force is used to avoid a deprecation warning
```
