# flattensidecar

Ett enkelt sidoprojekt för att platta till PDF-formulär via ett litet Java-API.

## Bygg och provkör

Bygg ett körbart JAR med Maven:

```bash
mvn -f java/pom.xml package
```

Kör flatten-funktionen från kommandorad:

```bash
java -jar java/target/flattener-0.0.1-SNAPSHOT.jar /sökväg/till/input.pdf /sökväg/till/output.pdf
```
