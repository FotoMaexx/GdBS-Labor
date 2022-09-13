# Lösungen Aufgabe 0.2

## Datum und Zeit
```
echo 'Heute ist der' $(date +"%d.%m.%Y") > datum
echo 'Es ist' $(date +"%R") 'Uhr' >> datum
```

## Leere Verzeichnisse suchen

```
find -type d -empty
```
