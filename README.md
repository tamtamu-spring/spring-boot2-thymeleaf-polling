# spring-boot2-thymeleaf-polling

## Sample Web App on Heroku
### System Administrator Page
https://spring-boot2-polling.herokuapp.com/home

### User Page
https://spring-boot2-polling.herokuapp.com/questionnaire/top

## Spring Boot2アプリの起動
### 1. Mavenを利用する場合
#### PostgreSQLの起動
デフォルトスキーマ`public`を設定する必要あり。
以下のコマンドで、日本語の文字コードを設定したpostgreSQLのDockerイメージ生成・起動を行う（テスト用なのでデータの永続化は行っていない）。
```bash
docker build -t postgres-dev -f env/postgresql/Dockerfile env/postgresql/
docker run -it --rm --name postgres-dev -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -e POSTGRES_DB=questionnairedb -p 5433:5432 -d postgres-dev
```

以下のコマンドで、postgreSQLのデータベースが生成されているかチェック。
```bash
docker exec -it postgres-dev psql -U postgres
postgres#=\l
```

#### MariaDBの起動
文字コードを`utf8mb4_unicode_ci`に設定する必要あり。
```bash
docker run -it --rm --name mariadb-dev -e MYSQL_DATABASE=questionnairedb_test -e MYSQL_ROOT_PASSWORD=password -p 3307:3306 -d mariadb:10.3.10 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

#### MySQLの起動
```bash
docker run -it --rm --name mysql-dev -e MYSQL_DATABASE=questionnairedb_test -e MYSQL_ROOT_PASSWORD=password -p 3307:3306 -d mysql:8.0.15 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

#### Spring bootアプリの起動
以下のコマンドを実行し、Webアプリケーションをビルド・起動する。
```bash
# For MacOS, Linux
./mvnw spring-boot:run

# For Windows
mvnw.cmd spring-boot:run
```

WAR・JARパッケージを作成する
```bash
# For MacOS, Linux
./mvnw clean package

# For Windows
mvnw.cmd -Dmaven.test.skip=true clean package
```

TomcatサーバでのWARファイルのサイズ制限でエラーになることがあるので、`conf/server.xml`に以下の設定を追加する。
```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxPostSize="70428800" />
```

もしくは、`/webapps/manager/WEB-INF/web.xml`に、
```xml
<multipart-config>
   <max-file-size>80428800</max-file-size>
   <max-request-size>80428800</max-request-size>
   <file-size-threshold>0</file-size-threshold>
</multipart-config>
```

### 2. jibを使ったSpring Bootアプリの起動

#### 2.1. PostgreSQLを使う場合
まず、`application.yml`ファイルでProfileを`postgres-docker`に設定する。  
次に、以下のコマンドを実行し、docker-composeにより、DBサーバとAPPサーバをDockerコンテナ上で起動する。

```bash
# PostgreSQLのDockerイメージの作成
docker build -t postgres-dev -f env/postgresql/Dockerfile env/postgresql/

# Spring bootアプリ実行環境用のDockerイメージの作成
mvn compile jib:dockerBuild

# docker-composeの起動
cd ./env/postgresql
docker-compose up -d
```

#### 2.2. MySQLを使う場合
まず、`application.yml`ファイルでProfileを`mysql-docker`に設定する。  
次に、以下のコマンドを実行し、docker-composeにより、DBサーバとAPPサーバをDockerコンテナ上で起動する。

```bash
# Spring bootアプリ実行環境用のDockerイメージの作成
mvn compile jib:dockerBuild

# docker-composeの起動
cd ./env/mysql
docker-compose up -d
```

## Demo
![](img/toppage.png)

![](img/questionnaireNew1.png)

![](img/questionnaireNew2.png)

![](img/questionnaireEdit.png)

![](img/usertop.png)

![](img/userform.png)

![](img/finish.png)

![](img/chart1.png)

![](img/chart2.png)