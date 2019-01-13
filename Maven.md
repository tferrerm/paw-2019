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

### Services
