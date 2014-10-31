#!/bin/sh
cd bin
jar -cvf ../jars/hipe-uplink-sim.jar *
cd ..
jar -cvf jars/moc.jar moc
jar -cvf jars/doc.jar vega
jar -cvf hus_1.0.jar vega src jars plugin.py plugin.xml vega.properties


