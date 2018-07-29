# zuhause-api

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0822e76a97d644d2afcefc032e9453d5)](https://app.codacy.com/app/edufolly/zuhause-api?utm_source=github.com&utm_medium=referral&utm_content=edufolly/zuhause-api&utm_campaign=badger)

**Nano Java API Server for IoT to Raspberry Pi**

If you :heart:, support us with a :star:!

## Installing Zuhause
Using RASPBIAN STRETCH LITE that is a **minimal** image based on Debian Stretch.

### Packages Installation
After the Raspbian default installation is done, execute the following commands:

```
$ sudo apt install oracle-java8-jdk ant git sysstat arp-scan nginx mariadb-server
```

To compile the Arduino code, this packages will be needed:

```
$ sudo apt install lightdm arduino
```

### App Folder

Create the app folder.

```
$ cd /opt
```

```
$ sudo mkdir projects
```

```
$ sudo chown pi:pi projects/
```

```
$ ls -la
```

To verify if the folder was created with success, the `ls -la | grep projects` command must return:

`drwxr-xr-x  2 pi   pi   4096 May 21 02:12 projects`

Continuing to cloning the code:

```
$ cd projects/
```

```
$ git clone https://github.com/edufolly/zuhause-api.git
```

```
$ cd zuhause-api/
```

Compiling and building:

```
$ ant compile
```

```
$ ant jar
```

### Database Configuration

```
$ mysql -u root -p -h localhost
```

Inside MariaDB prompt:

```sql
UPDATE mysql.user SET plugin = 'mysql_native_password' 
AND authentication_string = PASSWORD('new_password') 
WHERE user = 'root' AND plugin = 'unix_socket';
```

```sql
FLUSH PRIVILEGES;
```

```sql
exit;
```

Back to bash:

```
$ sudo service mariadb restart
```

```
$ mysql -u root -p -h localhost < /opt/projects/zuhause-api/SQL.sql
```

### Nginx Configuration

Configure Nginx Authentication

We are using rasp as our username, but you can use whatever name you'd like:

```
$ sudo sh -c "echo -n 'rasp:' >> /etc/nginx/.htpasswd"
```

Next, add an encrypted password entry for the username by typing:

```
$ sudo sh -c "openssl passwd -apr1 >> /etc/nginx/.htpasswd"
```

You can repeat this process for additional usernames. You can see how the usernames and encrypted passwords are stored within the file by typing:

```
$ cat /etc/nginx/.htpasswd
```

Output:

`rasp:$apr1$wI1/T0nB$jEKuTJHkTOOWkopnXqC1d1`

Then...

```
$ sudo nano /etc/nginx/sites-enabled/default
```

Inside, search by:

```
location / {
    try_files $uri $uri/ =404;
}
```

And add:

```
location / {
    try_files $uri $uri/ =404;
    auth_basic "Zuhause";
    auth_basic_user_file /etc/nginx/.htpasswd;
}
```

Create a reverse proxy to zuhause-api:

```
location /api {
    proxy_buffering off;
    proxy_pass http://127.0.0.1:8088;
}
```

You can also use the `auth_basic` for `/api`, just put the `auth_basic` lines out of location braces:

```
auth_basic "Zuhause";
auth_basic_user_file /etc/nginx/.htpasswd;
 
location / {
    try_files $uri $uri/ =404;
}
 
location /api {
    proxy_buffering off;
    proxy_pass http://127.0.0.1:8088;
}
```

Save and close the file when you are finished.

```
$ sudo service nginx restart
```

### Init Configuration
Configure Zuhause to init with the system.

```
$ sudo nano /etc/rc.local
```

The file modifications:

```
# Print the IP address
_IP=$(hostname -I) || true
if [ "$_IP" ]; then
  printf "My IP address is %s\n" "$_IP"
fi
 
/bin/sh /opt/projects/zuhause-api/start.sh
 
exit 0
```


## Thanks for:
* [Anderson Brantes](https://github.com/andersonbrantes)
* [Aurino Salvador](https://github.com/aurinosalvador)
* [Bruno Ferreira](https://github.com/brunobdaferreira)
* [Lucas Hilleshein](https://github.com/lucashilles)
