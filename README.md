# flattensidecar

## Bygg och provkör

Bygg ett körbart JAR med Maven:

```bash
mvn -f java/pom.xml package
```

Kör flatten-funktionen från kommandorad:

```bash
java -jar java/target/flattener-0.0.1-SNAPSHOT.jar /sökväg/till/input.pdf /sökväg/till/output.pdf
```
