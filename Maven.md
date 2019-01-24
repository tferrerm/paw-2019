## Maven Setup

### Root POM
```
mvn archetype:generate
org.codehaus.mojo.archetypes:pom-root # archetype
1 # selected archetype
# select default version (newest)
ar.edu.itba.paw # groupId
project-name # artifactId
1.0-SNAPSHOT # version
ar.edu.itba.paw # package
```

### Webapp
```
mvn archetype:generate
org.apache.maven.archetypes:maven-archetype-webapp
1 # selected archetype
# select default version (newest)
ar.edu.itba.paw # groupId
webapp # artifactId
# default version
# default package
```

Make sure to remove `config` in all appearances of `ar.edu.itba.paw.webapp.config.WebConfig`.

By default, jetty starts at `http://localhost:8080/<WEBAPP_ARTIFACT_ID>`.

### Model

```
mvn archetype:generate
org.apache.maven.archetypes:maven-archetype-quickstart
1 # selected archetype
# select default version
ar.edu.itba.paw # groupId
model # artifactId
# default
# default
```

Then, rename the package `ar.edu.itba.paw` to `ar.edu.itba.paw.model`.

### Interfaces

```
mvn archetype:generate
org.apache.maven.archetypes:maven-archetype-quickstart
1 # selected archetype
# select default version
ar.edu.itba.paw # groupId
interfaces # artifactId (note that interface is a reserved word)
# default
# default
```

Then, rename the package `ar.edu.itba.paw` to `ar.edu.itba.paw.interfaces`.

### Service

```
mvn archetype:generate
org.apache.maven.archetypes:maven-archetype-quickstart
1 # selected archetype
# select default version
ar.edu.itba.paw # groupId
service # artifactId
# default
# default
```

Then, rename the package `ar.edu.itba.paw` to `ar.edu.itba.paw.service`.
Do not forget to add the dependency `persistence` in `pom.xml`.

### Persistence

```
mvn archetype:generate
org.apache.maven.archetypes:maven-archetype-quickstart
1 # selected archetype
# select default version
ar.edu.itba.paw # groupId
persistence # artifactId
# default
# default
```

Then, rename the package `ar.edu.itba.paw` to `ar.edu.itba.paw.persistence`.
Do not forget to add the dependency `interfaces` in `pom.xml`.
