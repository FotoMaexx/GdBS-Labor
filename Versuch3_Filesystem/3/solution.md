# Erste Rechnung (nutzbare Größe):

0x13: 20 03 --> 800 Sektoren insgesamt

0x0E: 20 00 --> 32 reservierte Sektoren: 800 - 32 = 768 

0x24: 01 00 00 00 --> ein Sektor pro FAT, wir haben zwei FATs --> 768 - 2 = 766

---> 766 Sektoren frei 

0x0B: 00 02 --> 512 (Bytes pro Sektor)

750 * 512 = 392.192 Bytes = 392,192 MB

# Zweite Rechnung (freier/benutzter Speicher):

## belegter Speicher: 
11 belegte Cluster in den FATs --> 8 * 512 * 11 = 45.056 Bytes 

## freier Speicher: 
nutzbare Größe - belegter Speicher:
392.192 - 45.056 = 347.136 Bytes