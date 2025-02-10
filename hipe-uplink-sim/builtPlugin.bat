cd bin
"c:\Program Files\java\jdk1.6.0_23\bin\jar" -cvf ..\jars\hipe-uplink-sim.jar *
cd ..
"c:\Program Files\java\jdk1.6.0_23\bin\jar" -cvf jars\mib.jar mib
"c:\Program Files\java\jdk1.6.0_23\bin\jar" -cvf jars\moc.jar moc
"c:\Program Files\java\jdk1.6.0_23\bin\jar" -cvf jars\doc.jar vega
"c:\Program Files\java\jdk1.6.0_23\bin\jar" -cvf hus_1.0.jar vega src jars plugin.py plugin.xml vega.properties


