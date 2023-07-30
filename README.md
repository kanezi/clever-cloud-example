# Deploying spring apps to Clever Cloud

[Clever Cloud](https://www.clever-cloud.com) is cloud provider that enables hosting of varioud runtimes, as well as Docker containers. They also provide auxiliary services like databases, redis, s3 like storage, etc. 

Clever Cloud offers clever cloud CLI with which you can programmatically provision and deploy various services and addons. 

## Configuring clever cloud apps

Application uses postgres database and cellar (amazon aws s3 like) storage.



1. create app

```clever create --type docker avatar```

2. add postgres database add on

```clever addon create postgresql-addon avatar_db --link avatar```

3. add cellar, s3 like, storage add on

```clever addon create cellar-addon avatar_s3 --plan S --link avatar```


4. add redis - requires profile setup

```clever addon create redis-addon avatar_redis --plan m_mono --link avatar```

5. set up clever_cloud spring profile

```clever env set SPRING_PROFILES_ACTIVE "clever_cloud"```